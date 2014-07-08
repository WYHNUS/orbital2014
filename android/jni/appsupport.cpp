#include <string>
#include <jni.h>
#include <android/log.h>
#include <android/asset_manager_jni.h>
#include <SoundEngine.h>
#include <ExtensionEngine.h>
#include <ResourceManager.h>
#include <GLES2/gl2.h>
#include <iostream>
#include <fstream>
#include <json/json.h>

void testExtensionEngine() {
	ExtensionEngine* engine = ExtensionEngine::Create();
	engine->loadScript(std::string("var a = new ExtensionInterface();a.gameDelegate=function(a,b){return 1;};a.gameDelegate();"));
	engine->execute();
	engine->notifyUpdateCallback = [](const std::string&){};
	engine->applyRule(std::string(), std::string(), std::string("{\"Action\":\"Play\"}"));
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

void setupExtensionCallback(JNIEnv *env, jobject obj, ExtensionEngine *ee) {
	ee->notifyUpdateCallback = [=](const std::string &update) {
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "notifyUpdate", "(Ljava/lang/String;)V");
		env->CallVoidMethod(obj, method, env->NewStringUTF(update.c_str()));
	};
	ee->turnNextRoundCallback = [=]() {
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "turnNextRound", "()V");
		env->CallVoidMethod(obj, method);
	};
	ee->getSelectedGridCallback = [=]() -> const std::string {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "getSelectedGridCallback called");
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "getSelectedGrid", "()Ljava/lang/String;");
		jstring result = (jstring) env->CallObjectMethod(obj, method);
		const char *grid = env->GetStringUTFChars(result, 0);
		std::string ret(grid);
		env->ReleaseStringUTFChars(result, grid);
		return ret;
	};
	ee->getCharacterPositionCallback = [=](const std::string &chars) -> const std::string {
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "getCharacterPosition", "(Ljava/lang/String;)Ljava/lang/String;");
		jstring result = (jstring) env->CallObjectMethod(obj, method, env->NewStringUTF(chars.c_str()));
		const char *pos = env->GetStringUTFChars(result, 0);
		std::string ret(pos);
		env->ReleaseStringUTFChars(result, pos);
		return ret;
	};
	ee->setCharacterPositionCallback = [=](const std::string &character, const std::string &position) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "setCharacterPositionCallback called");
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "setCharacterPosition", "(Ljava/lang/String;Ljava/lang/String;)V");
		env->CallVoidMethod(obj, method, env->NewStringUTF(character.c_str()), env->NewStringUTF(position.c_str()));
	};
	ee->getCharacterPropertyCallback = [=](const std::string &character, const std::string &property)->const std::string {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "getCharacterPropertyCallback called");
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "getCharacterProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
		jstring result = (jstring) env->CallObjectMethod(obj, method, env->NewStringUTF(character.c_str()), env->NewStringUTF(property.c_str()));
		const char *ans = env->GetStringUTFChars(result, 0);
		std::string ret(ans);
		env->ReleaseStringUTFChars(result, ans);
		return ret;
	};
	ee->setCharacterPropertyCallback = [=](const std::string &character, const std::string &property, const std::string &value) {
		__android_log_print(ANDROID_LOG_DEBUG, "EE", "setCharacterPropertyCallback called");
		jclass clazz = env->FindClass("edu/nus/comp/dotagridandroid/appsupport/ExtensionEngine");
		jmethodID method = env->GetMethodID(clazz, "setCharacterProperty", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
		env->CallVoidMethod(obj, method,
				env->NewStringUTF(character.c_str()),
				env->NewStringUTF(property.c_str()),
				env->NewStringUTF(value.c_str()));
	};
}

extern "C" {

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testSL(JNIEnv *env, jobject obj) {
//	testSoundEngine();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testJS(JNIEnv *env, jobject obj) {
	testExtensionEngine();
}

// ExtensionEngine

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_initiateExtensionEngine(JNIEnv *env, jobject obj) {
	return (jlong) ExtensionEngine::Create();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_destroyExtensionEngine(JNIEnv *env, jobject obj, jlong ptr) {
	ExtensionEngine::Destroy((ExtensionEngine*) ptr);
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ExtensionEngine_loadScript(JNIEnv *env, jobject obj, jlong ptr, jstring script) {
	ExtensionEngine *ee = (ExtensionEngine*) ptr;
	const char *theScript = env->GetStringUTFChars(script, 0);
	ee->loadScript(std::string(theScript));
	env->ReleaseStringUTFChars(script, theScript);
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ExtensionEngine_execute(JNIEnv *env, jobject obj, jlong ptr) {
	ExtensionEngine *ee = (ExtensionEngine*) ptr;
	ee->execute();
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ExtensionEngine_applyRule(JNIEnv *env, jobject obj, jlong ptr, jstring character, jstring action, jstring optionsJSON) {
	ExtensionEngine *ee = (ExtensionEngine*) ptr;
	const char *characterParam = env->GetStringUTFChars(character, 0);
	const char *actionParam = env->GetStringUTFChars(action, 0);
	const char *optionParam = env->GetStringUTFChars(optionsJSON, 0);
	__android_log_print(ANDROID_LOG_DEBUG, "EE", "JNIEnv=%p", env);
	setupExtensionCallback(env, obj, ee);
	ee->applyRule(std::string(characterParam), std::string(actionParam), std::string(optionParam));
	env->ReleaseStringUTFChars(character, characterParam);
	env->ReleaseStringUTFChars(action, actionParam);
	env->ReleaseStringUTFChars(optionsJSON, optionParam);
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ExtensionEngine_automate(JNIEnv *env, jobject obj, jlong ptr, jstring character) {
	ExtensionEngine *ee = (ExtensionEngine*) ptr;
	const char *characterParam = env->GetStringUTFChars(character, 0);
	setupExtensionCallback(env, obj, ee);
	ee->automate(std::string(characterParam));
	env->ReleaseStringUTFChars(character, characterParam);
}

// SoundEngine

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
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_prepareAssetPlayer(
		JNIEnv *env,
		jobject obj,
		jlong ptr,
		jobject assetMan,
		jstring file,
		jstring name) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		const char * fileName = env->GetStringUTFChars(file, 0);
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Load %s for sound '%s'", fileName, strName);
		AAssetManager* mgr = AAssetManager_fromJava(env, assetMan);
		AAsset* asset = AAssetManager_open(mgr, fileName, AASSET_MODE_UNKNOWN);
		env->ReleaseStringUTFChars(file, fileName);

		off_t start, length;
		int fd = AAsset_openFileDescriptor(asset, &start, &length);
		AAsset_close(asset);

		se->prepareAssetPlayer(std::string(strName), fd, start, length);
		env->ReleaseStringUTFChars(name, strName);
	}
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_setAssetPlayerPlayState(JNIEnv *env, jobject obj, jlong ptr, jstring name, jboolean playState) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		se->setAssetPlayerPlayState(std::string(strName), playState);
		env->ReleaseStringUTFChars(name, strName);
	}
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_setAssetPlayerStop(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		se->setAssetPlayerStop(std::string(strName));
		env->ReleaseStringUTFChars(name, strName);
	}
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_setAssetPlayerLoop(JNIEnv *env, jobject obj, jlong ptr, jstring name, jboolean loop) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		se->setAssetPlayerLoop(std::string(strName), loop);
		env->ReleaseStringUTFChars(name, strName);
	}
}

JNIEXPORT void JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_SoundEngine_setAssetPlayerSeek(JNIEnv *env, jobject obj, jlong ptr, jstring name, jlong position) {
	SoundEngine *se = (SoundEngine*) ptr;
	if (se) {
		const char * strName = env->GetStringUTFChars(name, 0);
		se->setAssetPlayerSeek(std::string(strName), SLmillisecond(position));
		env->ReleaseStringUTFChars(name, strName);
	}
}

// ResourceManager

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

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getTextureWidth(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	const char *textureName = env->GetStringUTFChars(name, 0);
	ResourceManager *man = (ResourceManager*)ptr;
	jlong ret = man->getTextureWidth(std::string(textureName));
	env->ReleaseStringUTFChars(name, textureName);
	return ret;
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getTextureHeight(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	const char *textureName = env->GetStringUTFChars(name, 0);
	ResourceManager *man = (ResourceManager*)ptr;
	jlong ret = man->getTextureHeight(std::string(textureName));
	env->ReleaseStringUTFChars(name, textureName);
	return ret;
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getModelHandler(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	const char *textureName = env->GetStringUTFChars(name, 0);
	ResourceManager *man = (ResourceManager*)ptr;
	jlong ret = man->getModelHandler(std::string(textureName));
	env->ReleaseStringUTFChars(name, textureName);
	return ret;
}

JNIEXPORT jlong JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getModelSize(JNIEnv *env, jobject obj, jlong ptr, jstring name) {
	const char *textureName = env->GetStringUTFChars(name, 0);
	ResourceManager *man = (ResourceManager*)ptr;
	jlong ret = man->getModelSize(std::string(textureName));
	env->ReleaseStringUTFChars(name, textureName);
	return ret;
}

JNIEXPORT jboolean JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_isExtensionEnabled(JNIEnv *env, jobject obj, jlong ptr) {
	ResourceManager *man = (ResourceManager*)ptr;
	return man->isExtensionEnabled();
}

JNIEXPORT jstring JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getAllScript(JNIEnv *env, jobject obj, jlong ptr) {
	return env->NewStringUTF(((ResourceManager*)ptr)->getAllScript().c_str());
}

JNIEXPORT jstring JNICALL
Java_edu_nus_comp_dotagridandroid_appsupport_ResourceManager_getTerrainConfiguration(JNIEnv *env, jobject obj, jlong ptr) {
	return env->NewStringUTF(((ResourceManager*)ptr)->getTerrainConfiguration().c_str());
}

}
