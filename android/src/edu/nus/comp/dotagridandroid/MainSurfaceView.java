package edu.nus.comp.dotagridandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.*;
import android.util.AttributeSet;
import android.view.*;

public class MainSurfaceView extends GLSurfaceView{
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
		setEGLContextClientVersion(2);
		setRenderer(r = new MainRenderer(context));
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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
			// changes to glview
			return true;
		}
		return super.onTouchEvent(e);
	}
}
