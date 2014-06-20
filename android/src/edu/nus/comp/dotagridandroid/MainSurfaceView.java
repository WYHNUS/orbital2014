package edu.nus.comp.dotagridandroid;

import android.content.Context;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.support.v4.view.*;
import android.util.AttributeSet;
import android.view.*;
import edu.nus.comp.dotagridandroid.ui.event.*;

public class MainSurfaceView
	extends GLSurfaceView {
	private MainRenderer r = null;
	private int pointerActive = -1;
	private float pointerStartX = -1, pointerStartY = -1;
	private long eventStartTime;
	private boolean wasMultiTouch = false;
	
	public MainSurfaceView(Context context) {
		super(context);
		// Keep a reference to the Main class (Activity)
		// We will retrieve resources and assets from it
		// Proceed to init method
		init(context);
	}
	
	public MainSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		// Here we must configure OpenGL settings
		// This is where we set anti-aliasing by loading game settings and put it in
		EGLConfiguration config = new EGLConfiguration(((Main) context).getGameLogicManager());
		setEGLConfigChooser(config);
		setEGLContextClientVersion(2);
		// Load our MainRenderer - most important
		setRenderer(r = new MainRenderer(context, this));
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (r != null)
			r.close();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// clean up
		super.onConfigurationChanged(newConfig);
		if (r != null)
			r.close();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Starting point of Event Dispatch
		int action = MotionEventCompat.getActionMasked(event), actionIndex;
		EventData d;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			eventStartTime = event.getDownTime();
			wasMultiTouch = false;
			actionIndex = MotionEventCompat.getActionIndex(event);
			d = new EventData(1);
			d.x[actionIndex] = pointerStartX = MotionEventCompat.getX(event, actionIndex);
			d.y[actionIndex] = pointerStartY = MotionEventCompat.getY(event, actionIndex);
			pointerActive = MotionEventCompat.getPointerId(event, 0);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_DOWN, d));
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_MOVE:
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			d = new EventData(MotionEventCompat.getPointerCount(event));
			d.deltaX = MotionEventCompat.getX(event, actionIndex) - pointerStartX;
			d.deltaY = MotionEventCompat.getY(event, actionIndex) - pointerStartY;
			for (int i = MotionEventCompat.getPointerCount(event) - 1; i >= 0; i--) {
				d.x[i] = MotionEventCompat.getX(event, i);
				d.y[i] = MotionEventCompat.getY(event, i);
			}
			d.startTime = eventStartTime;
			d.eventTime = event.getEventTime();
			d.wasMultiTouch = wasMultiTouch;
			r.passEvent(new ControlEvent(ControlEvent.TYPE_DRAG, d));
			return true;
		case MotionEvent.ACTION_CANCEL:
			// TODO: Is this useful?
		case MotionEvent.ACTION_UP:
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			d = new EventData(1);
			d.startTime = eventStartTime;
			d.eventTime = event.getEventTime();
			d.x[actionIndex] = MotionEventCompat.getX(event, actionIndex);
			d.y[actionIndex] = MotionEventCompat.getY(event, actionIndex);
			d.deltaX = MotionEventCompat.getX(event, actionIndex) - pointerStartX;
			d.deltaY = MotionEventCompat.getY(event, actionIndex) - pointerStartY;
			d.wasMultiTouch = wasMultiTouch;
			pointerActive = -1;
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLEAR, d));
			return true;
		case MotionEvent.ACTION_POINTER_UP:
			wasMultiTouch = true;
			eventStartTime = event.getEventTime();
			actionIndex = MotionEventCompat.getActionIndex(event);
			d = new EventData(MotionEventCompat.getPointerCount(event));
			if (MotionEventCompat.getPointerId(event, actionIndex) == pointerActive) {
				if (actionIndex == 0)
					actionIndex = 1;
				else
					actionIndex = 0;
				System.out.println("Switch Active Pointer");
				pointerActive = MotionEventCompat.getPointerId(event, actionIndex);
			}
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			pointerStartX = MotionEventCompat.getX(event, actionIndex);
			pointerStartY = MotionEventCompat.getY(event, actionIndex);
			for (int i = MotionEventCompat.getPointerCount(event) - 1; i >= 0; i--) {
				d.x[i] = MotionEventCompat.getX(event, i);
				d.y[i] = MotionEventCompat.getY(event, i);
			}
			d.wasMultiTouch = true;
			d.startTime = eventStartTime;
			d.eventTime = eventStartTime;
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLEAR, d));
			return true;
		}
		return false;
	}
}
