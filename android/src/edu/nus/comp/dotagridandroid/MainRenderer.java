package edu.nus.comp.dotagridandroid;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;
import static android.opengl.GLES20.*;

public class MainRenderer implements GLSurfaceView.Renderer {
	public MainRenderer () {
		// initialise
	}

	@Override
	public void onDrawFrame(GL10 gl) {
	     gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glEnable(GL_BLEND);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_TEXTURE);
		gl.glDepthFunc(GL_LESS);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// black background
		glClearColor(0f, 0f, 0f, 1f);
	}

}