#include <stdio.h>
#include <string>
#include <map>
#include <jni.h>
#include <v8.h>

extern "C" {

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_test(JNIEnv *env, jobject obj) {

}

}

class ExtensionEngine {
	std::map<std::string, int> textureMap;
public:
	ExtensionEngine();
	void loadScript (std::string&);
};

ExtensionEngine::ExtensionEngine() {
	v8::Isolate * iso = v8::Isolate::GetCurrent();
	textureMap["aka"] = 4;
	v8::HandleScope scope (iso);
	v8::Handle<v8::Context> context = v8::Context::New(iso);
}
