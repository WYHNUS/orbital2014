package edu.nus.comp.dotagridandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MainSurfaceView extends GLSurfaceView {

	public MainSurfaceView(Context context) {
		super(context);
		init();
	}
	
	public MainSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setEGLContextClientVersion(2);
		setRenderer(new MainRenderer());
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

}
