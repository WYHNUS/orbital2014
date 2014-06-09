#include <ExtensionEngine.h>
#include <android/log.h>

ExtensionEngine::ExtensionEngine() {
	v8::V8::Initialize();
	iso = v8::Isolate::New();
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "ISO: %p\n", iso);
}

ExtensionEngine::~ExtensionEngine() {
	iso->Dispose();
//	v8::V8::Dispose();
}

void ExtensionEngine::loadScript(const std::string& src) {
	this->src = std::string(src);
}

void ExtensionEngine::execute() {
	// create isolate scope - important
	v8::Isolate::Scope iso_scope(iso);
	// create locker - important
	v8::Locker locker (iso);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Locker: ISO: %p\n", iso);
	// create handlescope - important
	v8::HandleScope scope (iso);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Scope");
	v8::Handle<v8::Context> context = v8::Context::New(iso);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Context: ISO: %p\n", iso);
	v8::Context::Scope context_scope(context);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Context Scope");
	v8::Handle<v8::String> source = v8::String::NewFromUtf8(iso, src.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "Source");
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
