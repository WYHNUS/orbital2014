package edu.nus.comp.dotagridandroid;

import android.content.Context;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.support.v4.view.*;
import android.util.AttributeSet;
import android.view.*;

public class MainSurfaceView extends GLSurfaceView {
	private MainRenderer r = null;
	public MainSurfaceView(Context context) {
		super(context);
		init(context);
	}
	
	public MainSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setEGLContextClientVersion(2);
		setRenderer(r = new MainRenderer(context));
		// just in case: turn on the below will reduce draw cycles, but we probably don't need it
//		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		int action = MotionEventCompat.getActionMasked(e);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_MOVE:
			return true;
		case MotionEvent.ACTION_UP:
			// apply changes to glview
			return true;
		}
		return super.onTouchEvent(e);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// clean up
		super.onConfigurationChanged(newConfig);
		r.close();
	}
}
