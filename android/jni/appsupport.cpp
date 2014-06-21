#include <string>
#include <jni.h>
#include <android/log.h>
#include <SoundEngine.h>
#include <ExtensionEngine.h>
#include <ResourceManager.h>
#include <GLES2/gl2.h>
#include <iostream>
#include <fstream>
#include <json/json.h>

void testExtensionEngine() {
	ExtensionEngine* engine = ExtensionEngine::Create();
	engine->loadScript(std::string("var a = new ExtensionInterface();a.gameDelegate=function(){return 1;};a.gameDelegate();"));
	engine->execute();
	engine->applyRule();
	ExtensionEngine::Destroy(engine);
}

void testSoundEngine() {
	SoundEngine *se = SoundEngine::Create();
	se->prepareBufferQueuePlayer();
	SoundEngine::Destroy(se);
}

void testGL() {
	GLuint vs = glCreateShader(GL_VERTEX_SHADER);
	GLuint fs = glCreateShader(GL_FRAGMENT_SHADER);
	char const *vsSrc = "attribute vec4 vPosition;void main() {gl_Position=vPosition;}",
			*fsSrc = "precision mediump float;void main(){gl_FragColor=vec4(1,1,1,1);}";
	glShaderSource(vs, 1, &vsSrc,NULL);
	glShaderSource(fs, 1, &fsSrc,NULL);
	glCompileShader(vs); glCompileShader(fs);
	GLuint prog = glCreateProgram();
	glAttachShader(prog,vs); glAttachShader(prog,fs);
	glLinkProgram(prog);
	glUseProgram(prog);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	GLuint pos = glGetAttribLocation(prog,"vPosition");
	const float vertices[] = {
			-.5,-.5,1,1,
			-.5,.5,1,1,
			.5,-.5,1,1,
			.5,.5,1,1
	};
	glVertexAttribPointer(pos,4,GL_FLOAT,false,0,vertices);
	glEnableVertexAttribArray(pos);
	glDrawArrays(GL_TRIANGLE_STRIP,0,4);
	glDisableVertexAttribArray(pos);
	glDeleteProgram(prog);
	glDeleteShader(vs);
	glDeleteShader(fs);
}

extern "C" {

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testSL(JNIEnv *env, jobject obj) {
	testSoundEngine();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testJS(JNIEnv *env, jobject obj) {
	testExtensionEngine();
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_createExtensionEngine(JNIEnv *env, jobject obj) {
	return (jlong) ExtensionEngine::Create();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroyExtensionEngine(JNIEnv *env, jobject obj, jlong ptr) {
	ExtensionEngine::Destroy((ExtensionEngine*) ptr);
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_initiateSoundEngine(JNIEnv *env, jobject obj) {
	return (jlong) SoundEngine::Create();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroySoundEngine(JNIEnv *env, jobject obj, jlong ptr) {
	SoundEngine::Destroy((SoundEngine*) ptr);
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_prepareBufferQueuePlayer(JNIEnv *env, jobject obj, jlong ptr) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se)
		se->prepareBufferQueuePlayer();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_prepareAssetPlayer(JNIEnv *env, jobject obj, jlong ptr, jstring name, jint fd, jlong start, jlong length) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		std::string strNameParam (strName);
		se->prepareAssetPlayer(strNameParam, fd, start, length);
		env->ReleaseStringUTFChars(name, strName);
	}
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_initiateResourceManager(JNIEnv *env, jobject obj, jstring path) {
	ResourceManager *man;
	if (path) {
		const char *pkgPath = env->GetStringUTFChars(path, 0);
		man = new ResourceManager(pkgPath);
		env->ReleaseStringUTFChars(path, pkgPath);
		return (jlong) man;
	} else {
		man = new ResourceManager();
		return (jlong) man;
	}
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroyResourceManager(JNIEnv *env, jobject obj, jlong ptr) {
	ResourceManager *man = (ResourceManager*)ptr;
	delete man;
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getTextureHandler(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	const char *textureName = env->GetStringUTFChars(name, 0);
	ResourceManager *man = (ResourceManager*)ptr;
	jlong ret = man->getTextureHandler(std::string(textureName));
	env->ReleaseStringUTFChars(name, textureName);
	return ret;
}

}
