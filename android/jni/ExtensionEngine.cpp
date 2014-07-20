#include <ExtensionEngine.h>
#include <android/log.h>

std::map<v8::Isolate*, ExtensionEngine*> _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP;
const int __TIMEOUT_HANDLER = 0;
const int __INTERVAL_HANDLER = 1;

ExtensionEngine::TimeoutHandler::TimeoutHandler(
		uint64_t timeout,
		v8::Isolate *iso,
		const v8::Handle<v8::Function> &callback) : timeout(timeout) {
	this->callback.Reset(iso, callback);
	trd = std::thread([&](){
		const auto startTime = std::chrono::high_resolution_clock::now();
		const auto timeoutDuration = std::chrono::milliseconds(this->timeout);
		while (!this->interrupted && std::chrono::high_resolution_clock::now() - startTime < timeoutDuration)
			;
		if (interrupted)
			return;
		running = true;
		v8::Isolate *iso = this->iso;
		v8::Isolate::Scope iso_scope(iso);
		v8::Locker locker (iso);
		v8::HandleScope scope (iso);
		ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[this->iso];
		v8::Handle<v8::Function> callback = v8::Local<v8::Function>::New(this->iso, this->callback);
		v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, engine->context);
		v8::Context::Scope context_scope (context);
		v8::TryCatch tryCatch;
		if (!callback.IsEmpty()) {
			callback->CallAsFunction(context->Global(), 0, 0);
			if (tryCatch.HasCaught())
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "timeout: Script Exception:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
		}
		running = false;
	});
	trd.detach();
}

ExtensionEngine::TimeoutHandler::~TimeoutHandler() {
	if (running)
		trd.join();
	else {
		interrupted = true;
		trd.join();
	}
	callback.Reset();
}

ExtensionEngine::IntervalHandler::IntervalHandler(
		uint64_t interval,
		v8::Isolate *iso,
		const v8::Handle<v8::Function> &callback) : interval(interval) {
	this->callback.Reset(iso, callback);
	trd = std::thread([&](){
		while (running) {
			const auto startTime = std::chrono::high_resolution_clock::now();
			const auto duration = std::chrono::milliseconds(this->interval);
			while (!running && std::chrono::high_resolution_clock::now() - startTime < duration)
				;
			v8::Isolate *iso = this->iso;
			v8::Isolate::Scope iso_scope(iso);
			v8::Locker locker (iso);
			v8::HandleScope scope (iso);
			ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[this->iso];
			v8::Handle<v8::Function> callback = v8::Local<v8::Function>::New(this->iso, this->callback);
			v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, engine->context);
			v8::Context::Scope context_scope (context);
			v8::TryCatch tryCatch;
			if (!callback.IsEmpty()) {
				callback->CallAsFunction(context->Global(), 0, 0);
				if (tryCatch.HasCaught())
					__android_log_print(ANDROID_LOG_DEBUG, "EE", "timeout: Script Exception:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
			}
		}
	});
	running = true;
	trd.detach();
}

ExtensionEngine::IntervalHandler::~IntervalHandler() {
	running = false;
	trd.join();
	callback.Reset();
}

v8::Handle<v8::Value> ExtensionEngine::parseJSON(const std::string& value) {
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker(iso);
	v8::EscapableHandleScope scope (iso);
	v8::Local<v8::Value> result = v8::String::NewFromUtf8(iso, value.c_str());
	v8::Local<v8::Object> json = v8::Local<v8::Object>::New(iso, jsonUtil);
	result = v8::Local<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "parse")))
														->CallAsFunction(json, 1, &result);
	return scope.Escape(result);
}
std::string&& ExtensionEngine::stringifyJSON(v8::Handle<v8::Value> value) {
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker(iso);
	v8::HandleScope scope (iso);
	v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, jsonUtil);
	v8::Handle<v8::Value> result = v8::Local<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "stringify")))
												->CallAsFunction(json, 1, &value);
	return std::move(std::string(*v8::String::Utf8Value(result)));
}

ExtensionEngine::ExtensionEngine() {
	// wrapped functions
	// Initialisation start
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
			ExtensionEngine* self = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
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
				v8::Isolate *iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope (iso);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate getter");
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				info.GetReturnValue().Set(v8::Local<v8::Value>::New(iso, itf->gameDelegate));
			},
			[] (v8::Local<v8::String> property, v8::Local<v8::Value> value, const v8::PropertyCallbackInfo<void>& info) {
				v8::Isolate* iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope (iso);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate setter");
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				ExtensionEngine *eng = itf->engine;
				itf->gameDelegate.Reset(iso, value);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate settle down");
			});
	newInterfaceTemplate->InstanceTemplate()->SetAccessor(v8::String::NewFromUtf8(iso, "terrainConfiguration"),
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value> &info) {
				v8::Isolate *iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope (iso);
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "terrainConfiguration getter");
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				info.GetReturnValue().Set(itf->engine->parseJSON(itf->engine->getTerrainConfigurationCallback()));
			});
	newInterfaceTemplate->InstanceTemplate()->SetAccessor(v8::String::NewFromUtf8(iso, "characters"),
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value> &info) {
				v8::Isolate *iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope (iso);
				v8::Handle<v8::ObjectTemplate> characterListDelegate = v8::ObjectTemplate::New(iso);
				characterListDelegate->SetNamedPropertyHandler(
						// getter
						[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value>& info) {
							v8::Isolate *iso = info.GetIsolate();
							v8::Isolate::Scope iso_scope(iso);
							v8::Locker locker(iso);
							v8::HandleScope scope (iso);
							v8::Handle<v8::ObjectTemplate> characterDelegate = v8::ObjectTemplate::New(iso);
							characterDelegate->SetInternalFieldCount(1);
							characterDelegate->SetNamedPropertyHandler(
									[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value>& info) {
										v8::Isolate *iso = info.GetIsolate();
										v8::Isolate::Scope iso_scope(iso);
										v8::Locker locker(iso);
										v8::HandleScope scope (iso);
										ExtensionInterface *itf = static_cast<ExtensionInterface*>(
												v8::Local<v8::External>::Cast(
														v8::Local<v8::Object>::Cast(info.Data())->GetInternalField(0))->Value());
										v8::Handle<v8::Value> result = itf->engine->parseJSON(
												itf->engine->getCharacterProperty(
													*v8::String::Utf8Value(info.Holder()->GetInternalField(0)),
													*v8::String::Utf8Value(property)));
										info.GetReturnValue().Set(result);
									},
									[] (v8::Local<v8::String> property, v8::Local<v8::Value> value, const v8::PropertyCallbackInfo<v8::Value>& info) {
										v8::Isolate *iso = info.GetIsolate();
										v8::Isolate::Scope iso_scope(iso);
										v8::Locker locker(iso);
										v8::HandleScope scope (iso);
										ExtensionInterface *itf = static_cast<ExtensionInterface*>(
												v8::Local<v8::External>::Cast(
														v8::Local<v8::Object>::Cast(info.Data())->GetInternalField(0))->Value());
										itf->engine->setCharacterProperty(
												*v8::String::Utf8Value(info.Holder()->GetInternalField(0)),
												*v8::String::Utf8Value(property),
												itf->engine->stringifyJSON(value).c_str());
										info.GetReturnValue().Set(value);
									},
									[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Integer>& info) {
										info.GetReturnValue().Set(v8::Integer::New(info.GetIsolate(), v8::DontDelete));
									},
									[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Boolean>& info) {
										info.GetReturnValue().Set(v8::Boolean::New(info.GetIsolate(), false));
									},
									0,
									info.Data());
						},
						// setter
						[] (v8::Local<v8::String> property, v8::Local<v8::Value> value, const v8::PropertyCallbackInfo<v8::Value>& info) {
							info.GetReturnValue().SetUndefined();
						},
						// query
						[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Integer>& info) {
							info.GetReturnValue().Set(v8::Integer::New(info.GetIsolate(), v8::DontDelete | v8::ReadOnly));
						},
						// delete
						[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Boolean>& info) {
							info.GetReturnValue().Set(v8::Boolean::New(info.GetIsolate(), false));
						},
						// enumerate
						[] (const v8::PropertyCallbackInfo<v8::Array>& info) {
							v8::Isolate *iso = info.GetIsolate();
							v8::Isolate::Scope iso_scope(iso);
							v8::Locker locker(iso);
							v8::HandleScope scope (iso);
							v8::Handle<v8::Array> list = v8::Array::New(iso);
						},
						info.Holder());
				v8::Handle<v8::Object> newInstance = characterListDelegate->NewInstance();
				newInstance->SetInternalField(0, property);
				info.GetReturnValue().Set(characterListDelegate->NewInstance());
			});
	newInterfaceTemplate->InstanceTemplate()->SetAccessor(v8::String::NewFromUtf8(iso, "autoDelegate"),
			[] (v8::Local<v8::String> property, const v8::PropertyCallbackInfo<v8::Value> &info) {
				v8::Isolate *iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				info.GetReturnValue().Set(v8::Local<v8::Value>::New(iso, itf->autoDelegate));
			},
			[] (v8::Local<v8::String> property, v8::Local<v8::Value> value, const v8::PropertyCallbackInfo<void>& info) {
				v8::Isolate *iso = info.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker(iso);
				v8::HandleScope scope (iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(info.Holder()->GetInternalField(0))->Value());
				itf->autoDelegate.Reset(iso, value);
			});
	newInterfaceTemplate->PrototypeTemplate()->Set(v8::String::NewFromUtf8(iso, "notifyUpdate"),
			v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "notifyUpdate called");
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Locker locker(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
				if (args[0].IsEmpty() || args[0]->IsUndefined() || args[0]->IsNull())
					return;
				if (args[0]->IsObject()) {
					v8::Handle<v8::Value> obj = args[0];
					itf->engine->notifyUpdate(itf->engine->stringifyJSON(obj).c_str());
				}
			}));
	newInterfaceTemplate->PrototypeTemplate()->Set(v8::String::NewFromUtf8(iso, "turnNextRound"),
			v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
				v8::Isolate *iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Locker locker(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
				itf->engine->turnNextRoundCallback();
			}));
	newInterfaceTemplate->PrototypeTemplate()->Set(v8::String::NewFromUtf8(iso, "getCharacterPosition"),
			v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "getCharacterPosition called");
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Locker locker(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
				if (args[0].IsEmpty() || args[0]->IsUndefined() || args[0]->IsNull())
					return;
				v8::Handle<v8::Value> result = itf->engine->parseJSON(itf->engine->getCharacterPosition(*v8::String::Utf8Value(args[0])));
				args.GetReturnValue().Set(result);
			}));
	newInterfaceTemplate->PrototypeTemplate()->Set(v8::String::NewFromUtf8(iso, "setCharacterPosition"),
			v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "setCharacterPosition called");
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Locker locker(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
				if (args[0].IsEmpty() || args[0]->IsUndefined() || args[0]->IsNull() || !args[0]->IsString())
					return;
				if (args[1].IsEmpty() || args[1]->IsUndefined() || args[1]->IsNull())
					return;
				if (args[1]->IsArray()) {
					v8::Handle<v8::Value> obj = args[1];
					v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, itf->engine->jsonUtil);
					v8::Handle<v8::Value> result = v8::Local<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "stringify")))
																->CallAsFunction(json, 1, &obj);
					itf->engine->setCharacterPosition(*v8::String::Utf8Value(args[0]),*v8::String::Utf8Value(result));
				}
			}));
	newInterfaceTemplate->PrototypeTemplate()->Set(v8::String::NewFromUtf8(iso, "getSelectedGrid"),
			v8::FunctionTemplate::New(iso, [] (const v8::FunctionCallbackInfo<v8::Value> &args) {
				__android_log_print(ANDROID_LOG_DEBUG, "EE", "getSelectedGrid called");
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Locker locker(iso);
				ExtensionInterface *itf = static_cast<ExtensionInterface*>(v8::Local<v8::External>::Cast(args.Holder()->GetInternalField(0))->Value());
				v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, itf->engine->jsonUtil);
				v8::Handle<v8::Value> result = v8::String::NewFromUtf8(iso, itf->engine->getSelectedGrid().c_str());
				result = v8::Local<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "parse")))
																						->CallAsFunction(json, 1, &result);
				if (result.IsEmpty()) {
					args.GetReturnValue().Set(v8::Array::New(iso));
				} else {
					args.GetReturnValue().Set(result);
				}
			}));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "gameDelegate Registered");
	interfaceTemplate.Reset(iso, newInterfaceTemplate);
	v8::Context::Scope context_scope(newContext);
	v8::Handle<v8::Object> json = v8::Local<v8::Object>::Cast(newContext->Global()->Get(v8::String::NewFromUtf8(iso, "JSON")));
	jsonUtil.Reset(iso, json);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "JSON Stored");
	_DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso] = this;
	currentInterface = new ExtensionInterface(this);
}

v8::Handle<v8::Object> ExtensionEngine::wrapInterface() {
	v8::EscapableHandleScope scope (iso);
	v8::Handle<v8::Value> ext = v8::External::New(iso, currentInterface);
	v8::Handle<v8::FunctionTemplate> ctr = v8::Local<v8::FunctionTemplate>::New(iso, interfaceTemplate);
	return scope.Escape(ctr->GetFunction()->NewInstance(1, &ext));
}

ExtensionEngine::~ExtensionEngine() {
	delete currentInterface;
	_DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP.erase(iso);
	interfaceTemplate.Reset();
	jsonUtil.Reset();
	context.Reset();
	iso->Dispose();
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
	// expose some useful functions
	context->Global()->Set(v8::String::NewFromUtf8(iso, "setTimeout"), v8::FunctionTemplate::New(iso,
			[](const v8::FunctionCallbackInfo<v8::Value> &args){
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				if (args[0].IsEmpty() || args[1].IsEmpty() || !args[0]->IsFunction() || !args[1]->IsNumber() || !args[1]->IsNumberObject())
					return;
				v8::Handle<v8::ObjectTemplate> timerHandler = v8::ObjectTemplate::New(iso);
				timerHandler->SetInternalFieldCount(2);
				ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
				TimeoutHandler *handler = new TimeoutHandler(args[1]->IntegerValue(), iso, v8::Local<v8::Function>::Cast(args[0]));
				v8::Handle<v8::Object> handlerInstance = timerHandler->NewInstance();
				handlerInstance->SetInternalField(0, v8::External::New(iso, handler));
				handlerInstance->SetInternalField(1, v8::External::New(iso, (void*) __TIMEOUT_HANDLER));
				engine->timeoutHandlers[handler].Reset(iso, handlerInstance);
				engine->timeoutHandlers[handler].SetWeak(handler,
						[] (const v8::WeakCallbackData<v8::Object, TimeoutHandler> &data) {
							ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[data.GetIsolate()];
							TimeoutHandler *handler = data.GetParameter();
							delete data.GetParameter();
							engine->timeoutHandlers[handler].Reset();
							engine->timeoutHandlers.erase(
									engine->timeoutHandlers.find(handler));
						});
			})->GetFunction());
	context->Global()->Set(v8::String::NewFromUtf8(iso, "clearTimeout"), v8::FunctionTemplate::New(iso,
			[](const v8::FunctionCallbackInfo<v8::Value> &args){
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				if (args[0].IsEmpty() || !args[0]->IsObject())
					return;
				v8::Handle<v8::Object> handlerInstance = v8::Local<v8::Object>::Cast(args[0]);
				if (handlerInstance->GetInternalField(1).IsEmpty()
						|| v8::Local<v8::External>::Cast(handlerInstance->GetInternalField(1))->Value() != (void*) __TIMEOUT_HANDLER)
					return;
				TimeoutHandler *handler = (TimeoutHandler*) v8::Local<v8::External>::Cast(handlerInstance->GetInternalField(0))->Value();
				delete handler;
				ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
				engine->timeoutHandlers[handler].Reset();
				engine->timeoutHandlers.erase(
						engine->timeoutHandlers.find(handler));
			})->GetFunction());
	context->Global()->Set(v8::String::NewFromUtf8(iso, "setInterval"), v8::FunctionTemplate::New(iso,
			[](const v8::FunctionCallbackInfo<v8::Value> &args){
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				v8::Handle<v8::ObjectTemplate> timerHandler = v8::ObjectTemplate::New(iso);
				timerHandler->SetInternalFieldCount(2);
				ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
				IntervalHandler *handler = new IntervalHandler(args[1]->IntegerValue(), iso, v8::Local<v8::Function>::Cast(args[0]));
				v8::Handle<v8::Object> handlerInstance = timerHandler->NewInstance();
				handlerInstance->SetInternalField(0, v8::External::New(iso, handler));
				handlerInstance->SetInternalField(1, v8::External::New(iso, (void*) __INTERVAL_HANDLER));
				engine->intervalHandlers[handler].Reset(iso, handlerInstance);
				engine->intervalHandlers[handler].SetWeak(handler,
						[] (const v8::WeakCallbackData<v8::Object, IntervalHandler> &data) {
							ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[data.GetIsolate()];
							IntervalHandler *handler = data.GetParameter();
							delete data.GetParameter();
							engine->intervalHandlers[handler].Reset();
							engine->intervalHandlers.erase(
									engine->intervalHandlers.find(handler));
						});
			})->GetFunction());
	context->Global()->Set(v8::String::NewFromUtf8(iso, "clearInterval"), v8::FunctionTemplate::New(iso,
			[](const v8::FunctionCallbackInfo<v8::Value> &args){
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::HandleScope scope(iso);
				if (args[0].IsEmpty() || !args[0]->IsObject())
					return;
				v8::Handle<v8::Object> handlerInstance = v8::Local<v8::Object>::Cast(args[0]);
				if (handlerInstance->GetInternalField(1).IsEmpty()
						|| v8::Local<v8::External>::Cast(handlerInstance->GetInternalField(1))->Value() != (void*) __INTERVAL_HANDLER)
					return;
				IntervalHandler *handler = (IntervalHandler*) v8::Local<v8::External>::Cast(handlerInstance->GetInternalField(0))->Value();
				delete handler;
				ExtensionEngine *engine = _DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP[iso];
				engine->intervalHandlers[handler].Reset();
				engine->intervalHandlers.erase(
						engine->intervalHandlers.find(handler));
			})->GetFunction());
	context->Global()->Set(v8::String::NewFromUtf8(iso, "sleep"), v8::FunctionTemplate::New(iso,
			[](const v8::FunctionCallbackInfo<v8::Value> &args){
				v8::Isolate* iso = args.GetIsolate();
				v8::Isolate::Scope iso_scope(iso);
				v8::Locker locker (iso);
				v8::HandleScope scope(iso);
				if (args[0].IsEmpty() || args[0]->IsUndefined() || args[0]->IsNull())
					return;
				std::this_thread::sleep_for(std::chrono::milliseconds(args[0]->IntegerValue()));
			})->GetFunction());
	v8::Handle<v8::Script> script = v8::Script::Compile(source);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Compile");
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Script=\n%s", src.c_str());
	v8::TryCatch tryCatch;
	v8::Handle<v8::Value> result = script->Run();
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Script Exception:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Run");
	if (result.IsEmpty()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Error: No return value");
	} else if (!result->IsUndefined()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Debug: convert to string handler");
		v8::String::Utf8Value str (result);
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Result: %s", *str);
	}
}

void ExtensionEngine::applyRule(const std::string& character, const std::string& action, const std::string& options) {
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Applying rule: name=%s, options=%s", action.c_str(), options.c_str());
	if (currentInterface->gameDelegate.IsEmpty()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Game Delegate is empty!");
		execute();
	}
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker (iso);
	v8::HandleScope scope (iso);
	v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, this->context);
	v8::Handle<v8::Function> gameDelegate = v8::Local<v8::Function>::Cast(v8::Local<v8::Value>::New(iso, currentInterface->gameDelegate));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Loading JSON");
	v8::Handle<v8::Object> json = v8::Local<v8::Object>::New(iso, jsonUtil);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Entering context scope");
	v8::Context::Scope context_scope(context);
	v8::TryCatch tryCatch;
	v8::Handle<v8::Value> params[3];
	params[0] = v8::String::NewFromUtf8(iso, options.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Parsing JSON");
	params[2] = v8::Handle<v8::Function>::Cast(json->Get(v8::String::NewFromUtf8(iso, "parse")))->CallAsFunction(json, 1, params);
	if (params[1].IsEmpty())
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "JSON error:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
	tryCatch.Reset();
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Parsing complete");
	params[0] = v8::String::NewFromUtf8(iso, character.c_str());
	params[1] = v8::String::NewFromUtf8(iso, action.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Preparing to call game delegate\nParameters:");
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "1. %s", *v8::String::Utf8Value(params[0]));
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "2. %s", *v8::String::Utf8Value(params[1]));
	if (currentInterface->gameDelegate.IsEmpty()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Game delegate is still empty!");
		return;
	}
	v8::Handle<v8::Value> result;
	if (params[2].IsEmpty())
		result = gameDelegate->CallAsFunction(context->Global(), 2, params);
	else
		result = gameDelegate->CallAsFunction(context->Global(), 3, params);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Apply Rule Success");
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Script Exception:\n%s", *v8::String::Utf8Value(tryCatch.Exception()));
	if (result.IsEmpty() || result->IsUndefined() || result->IsNull())
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Action is dropped by the script");
	else
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Result: %s", *v8::String::Utf8Value(result));
}

void ExtensionEngine::automate(const std::string& character) {
	v8::Isolate::Scope iso_scope(iso);
	v8::Locker locker (iso);
	v8::HandleScope scope (iso);
	v8::Handle<v8::Context> context = v8::Local<v8::Context>::New(iso, this->context);
	v8::Handle<v8::Function> autoDelegate = v8::Local<v8::Function>::Cast(v8::Local<v8::Value>::New(iso, currentInterface->autoDelegate));
	v8::Context::Scope context_scope(context);
	v8::TryCatch tryCatch;
	v8::Handle<v8::Value> param = v8::String::NewFromUtf8(iso, character.c_str());
	if (autoDelegate.IsEmpty()) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Auto delegate is still empty!");
		return;
	}
	v8::Handle<v8::Value> result = autoDelegate->CallAsFunction(context->Global(), 1, &param);
	if (result.IsEmpty() || result->IsUndefined() || result->IsNull())
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Automation is dropped by the script");
	else
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "Result: %s", *v8::String::Utf8Value(result));
}

void ExtensionEngine::notifyUpdate(const char *update) {
	notifyUpdateCallback(std::string(update));
}

const std::string ExtensionEngine::getCharacterPosition(const char *chars) {
	return getCharacterPositionCallback(std::string(chars));
}

const std::string ExtensionEngine::getCharacterProperty(const char *character, const char *property) {
	return getCharacterPropertyCallback(std::string(character), std::string(property));
}

void ExtensionEngine::setCharacterPosition(const char * character, const char * position) {
	setCharacterPositionCallback(std::string(character), std::string(position));
}

void ExtensionEngine::setCharacterProperty(const char *character, const char *property, const char *value) {
	setCharacterPropertyCallback(std::string(character), std::string(property), std::string(value));
}

const std::string ExtensionEngine::getSelectedGrid() {
	return getSelectedGridCallback();
}

ExtensionEngine* ExtensionEngine::Create() {
	return new ExtensionEngine();
}

void ExtensionEngine::Destroy(const ExtensionEngine* ee) {
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "ExtensionEngine cleaning up");
	delete ee;
}
