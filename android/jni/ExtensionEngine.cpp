#include <ExtensionEngine.h>
#include <android/log.h>

std::map<v8::Isolate*, ExtensionEngine*> DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP;

ExtensionEngine::ExtensionEngine() {
	v8::V8::Initialize();
	iso = v8::Isolate::New();
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "ISO: %p\n", iso);
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker(iso);
	v8::HandleScope scope(iso);
	v8::Handle<v8::Context> newContext = v8::Context::New(iso);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Context: ISO: %p\n", iso);
	context.Reset(iso, newContext);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Context Stored");
	v8::Handle<v8::FunctionTemplate> newInterfaceTemplate
		= v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
			__android_log_print(ANDROID_LOG_DEBUG, "EE", "ExtensionInterface constructor");
			v8::Isolate* iso = args.GetIsolate();
			v8::Isolate::Scope iso_scope(iso);
			v8::Locker locker(iso);
			ExtensionEngine* self = DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
			if (self) {
				v8::HandleScope scope (iso);
				if (args[0]->IsExternal()) {
					__android_log_print(ANDROID_LOG_DEBUG, "EE", "ExtensionInterface constructor: EXTERNAL");
					args.This()->SetInternalField(0, args[0]);
					args.GetReturnValue().Set(args.This());
				} else {
					__android_log_print(ANDROID_LOG_DEBUG, "EE", "ExtensionInterface constructor: NEW");
					args.GetReturnValue().Set(self->wrapInterface());
				}
			}
		});
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Constructor Registered");
	newInterfaceTemplate->InstanceTemplate()->SetInternalFieldCount(1);
	newInterfaceTemplate->InstanceTemplate()->SetAccessor(v8::String::NewFromUtf8(iso, "gameDelegate"),
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value> &info) {
				v8::Isolate* iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate getter");
				v8::HandleScope scope (iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				ExtensionEngine *eng = itf->engine;
				info.GetReturnValue().Set(v8::Local<v8::Value>::New(iso, itf->gameDelegate));
			},
			[] (v8::Local<v8::String> property, v8::Local<v8::Value> value, const v8::PropertyCallbackInfo<void>& info) {
				v8::Isolate* iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate setter");
				v8::HandleScope scope (info.GetIsolate());
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				ExtensionEngine *eng = itf->engine;
				itf->gameDelegate.Reset(info.GetIsolate(), value);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate settle down");
			});
	newInterfaceTemplate->InstanceTemplate()->SetAccessor(v8::String::NewFromUtf8(iso, "notifyUpdate"),
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value>& info) {
				v8::Isolate* iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope (iso);
				v8::Locker locker(iso);
				v8::Handle<v8::FunctionTemplate> func = v8::FunctionTemplate::New(iso,
						[] (const v8::FunctionCallbackInfo<v8::Value> &args) {
							v8::Isolate* iso = args.GetIsolate();
							v8::Isolate::Scope iso_scope(iso);
							v8::HandleScope scope(iso);
							v8::Locker locker(iso);
							ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
							if (args[0].IsEmpty())
								return;
							if (args[0]->IsString()) {
								v8::String::Utf8Value jsonUpdate (args[0]);
								itf->engine->notifyUpdate(*jsonUpdate);
							} else if (args[0]->IsObject()) {
								v8::Handle<v8::Value> obj = args[0];
								v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, itf->engine->jsonUtil);
								v8::Handle<v8::Value> result = v8::Local<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "stringify")))
										->CallAsFunction(json, 1, &obj);
								v8::String::Utf8Value jsonUpdate (result);
								itf->engine->notifyUpdate(*jsonUpdate);
							}
						});
				info.GetReturnValue().Set(func->GetFunction());
			});
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate Registered");
	interfaceTemplate.Reset(iso, newInterfaceTemplate);
	v8::Context::Scope context_scope(newContext);
	v8::Handle<v8::Object> json = v8::Local<v8::Object>::Cast(newContext->Global()->Get(v8::String::NewFromUtf8(iso, "JSON")));
	jsonUtil.Reset(iso, json);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "JSON Stored");
	DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso] = this;
	currentInterface = new ExtensionInterface(this);
}

v8::Handle<v8::Object> ExtensionEngine::wrapInterface() {
	v8::EscapableHandleScope scope (iso);
	v8::Handle<v8::Value> ext = v8::External::New(iso, currentInterface);
	v8::Handle<v8::FunctionTemplate> ctr = v8::Local<v8::FunctionTemplate>::New(iso, interfaceTemplate);
	return scope.Escape(ctr->GetFunction()->NewInstance(1, &ext));
}

ExtensionEngine::~ExtensionEngine() {
	DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP.erase(iso);
	context.Reset();
	iso->Dispose();
	delete currentInterface;
}

void ExtensionEngine::loadScript(const std::string& src) {
	this->src = std::string(src);
}

void ExtensionEngine::execute() {
	// create isolate scope - important
	v8::Isolate::Scope iso_scope(iso);
	// create locker - important
	v8::Locker locker (iso);
	// create handlescope - important
	v8::HandleScope scope (iso);
	v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, this->context);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Using stored context");
	v8::Handle<v8::FunctionTemplate> interfaceTemplate = v8::Local<v8::FunctionTemplate>::New(iso, this->interfaceTemplate);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Restore Handle to interfaceTemplate");
	v8::Handle<v8::String> source = v8::String::NewFromUtf8(iso, src.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Source");
	// create context scope for execution
	v8::Context::Scope context_scope(context);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Context Scope");
	context->Global()->Set(v8::String::NewFromUtf8(iso, "ExtensionInterface"), interfaceTemplate->GetFunction());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Expose ExtensionInterface");
	v8::Handle<v8::Script> script = v8::Script::Compile(source);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Compile");
	v8::TryCatch tryCatch;
	v8::Handle<v8::Value> result = script->Run();
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Run");
	if (result.IsEmpty()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Error: No return value");
	} else if (!result->IsUndefined()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Debug: convert to string handler");
		v8::String::Utf8Value str (result);
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Result: %s", *str);
	}
}

void ExtensionEngine::applyRule(const std::string& name, const std::string& options, const std::function<void(const std::string&)> &callback) {
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Applying rule: name=%s, options=%s", name.c_str(), options.c_str());
	this->callback = callback;
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker (iso);
	v8::HandleScope scope (iso);
	v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, this->context);
	v8::Handle<v8::Function> gameDelegate = v8::Local<v8::Function>::Cast(v8::Local<v8::Value>::New(iso, this->currentInterface->gameDelegate));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Loading JSON");
	v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, jsonUtil);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Entering context scope");
	v8::Context::Scope context_scope(context);
	v8::TryCatch tryCatch;
	v8::Handle<v8::Value> params[2];
	params[0] = v8::String::NewFromUtf8(iso, options.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Parsing JSON");
	params[1] = v8::Handle<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "parse")))->CallAsFunction(json, 1, params);
	if (params[1].IsEmpty())
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "JSON error:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Parsing complete");
	params[0] = v8::String::NewFromUtf8(iso, name.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Preparing to call game delegate\nParameters:");
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "1. %s", *v8::String::Utf8Value(params[0]));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "2. %s", *v8::String::Utf8Value(params[1]));
	v8::Handle<v8::Value> result;
	if (params[1].IsEmpty())
		result = gameDelegate->CallAsFunction(context->Global(), 1, params);
	else
		result = gameDelegate->CallAsFunction(context->Global(), 2, params);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Apply Rule Success");
	if (result.IsEmpty() || result->IsUndefined() || result->IsNull()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Action is dropped by the script");
	} else {
		v8::String::Utf8Value str (result);
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Result: %s", *str);
	}
}

void ExtensionEngine::notifyUpdate(const char *update) {
	callback(update);
}

ExtensionEngine* ExtensionEngine::Create() {
	return new ExtensionEngine();
}

void ExtensionEngine::Destroy(const ExtensionEngine* ee) {
	delete ee;
}
