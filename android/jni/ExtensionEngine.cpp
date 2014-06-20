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
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value>& info) {
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
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate Registered");
	interfaceTemplate.Reset(iso, newInterfaceTemplate);
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

ExtensionEngine* ExtensionEngine::Create() {
	return new ExtensionEngine();
}

void ExtensionEngine::Destroy(const ExtensionEngine* ee) {
	delete ee;
}
