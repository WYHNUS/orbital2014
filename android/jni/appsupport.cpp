#include <string>
#include <jni.h>
#include <v8.h>
#include <android/log.h>
#include <SoundEngine.h>
#include <ExtensionEngine.h>

void testExtensionEngine() {
	ExtensionEngine* engine = ExtensionEngine::Create();
	engine->loadScript(std::string("1 + 1;"));
	engine->execute();
	ExtensionEngine::Destroy(engine);
}

void testSoundEngine() {
	SoundEngine *se = SoundEngine::Create();
	SoundEngine::Destroy(se);
}

extern "C" {

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testJS(JNIEnv *env, jobject obj) {
	testExtensionEngine();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_createExtensionEngine(JNIEnv *env, jobject obj) {
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroyExtensionEngine(JNIEnv *env, jobject obj) {
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_initiateSoundEngine(JNIEnv *env, jobject obj) {
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroySoundEngine(JNIEnv *env, jobject obj) {
}
}
