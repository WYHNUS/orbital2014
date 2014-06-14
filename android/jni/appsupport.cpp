#include <string>
#include <jni.h>
#include <v8.h>
#include <android/log.h>
#include <SoundEngine.h>
#include <ExtensionEngine.h>
#include <GLES2/gl2.h>
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

void testGL() {
	return;
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
Java_edu_nus_comp_dotagridandroid_appsupport_AppNativeAPI_testGL(JNIEnv *env, jobject obj) {
	testGL();
}

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
