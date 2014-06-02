package edu.nus.comp.dotagridandroid;

import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import android.opengl.*;

public class EGLConfiguration implements GLSurfaceView.EGLConfigChooser {
	private GameLogicManager manager;

	public EGLConfiguration(GameLogicManager gameLogicManager) {
		manager = gameLogicManager;
	}

	@Override
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
		final int[] attrs = {
				EGL10.EGL_RED_SIZE, 8,
				EGL10.EGL_GREEN_SIZE, 8,
				EGL10.EGL_BLUE_SIZE, 8,
				EGL10.EGL_ALPHA_SIZE, 8,
				EGL10.EGL_DEPTH_SIZE, 16,
				EGL10.EGL_STENCIL_SIZE, 8,
				EGL10.EGL_SAMPLES, (Integer) manager.getGameSetting("DISPLAY_ANTI_ALIAS_SAMPLINGS"),
				EGL10.EGL_NONE
		};
		final int[] configCount = {0};
		final EGLConfig[] configTarget = {null};
		egl.eglChooseConfig(display, attrs, configTarget, 1, configCount);
		return configTarget[0];
	}

}
