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
	private boolean wasMultiTouch = false;
	
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
		setRenderer(r = new MainRenderer(context, this));
		// just in case: turn on the below will reduce draw cycles, but we probably don't need it
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
		int action = MotionEventCompat.getActionMasked(event), actionIndex;
		EventData d;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			wasMultiTouch = false;
			actionIndex = MotionEventCompat.getActionIndex(event);
			d = new EventData(1);
			d.x[actionIndex] = pointerStartX = MotionEventCompat.getX(event, actionIndex);
			d.y[actionIndex] = pointerStartY = MotionEventCompat.getY(event, actionIndex);
			pointerActive = MotionEventCompat.getPointerId(event, 0);
//			System.out.println("Down");
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_DOWN, d));
			return true;
		case MotionEvent.ACTION_MOVE:
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			d = new EventData(MotionEventCompat.getPointerCount(event));
			d.deltaX = MotionEventCompat.getX(event, actionIndex) - pointerStartX;
			d.deltaY = MotionEventCompat.getY(event, actionIndex) - pointerStartY;
			for (int i = MotionEventCompat.getPointerCount(event) - 1; i >= 0; i--) {
				d.x[i] = MotionEventCompat.getX(event, i);
				d.y[i] = MotionEventCompat.getY(event, i);
			}
//			System.out.println("Move");
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			d.wasMultiTouch = wasMultiTouch;
			r.passEvent(new ControlEvent(ControlEvent.TYPE_DRAG, d));
			return true;
		case MotionEvent.ACTION_CANCEL:
			// TODO: Is this useful?
//			System.out.println("CANCEL");
		case MotionEvent.ACTION_UP:
//			System.out.println("UP");
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			d = new EventData(1);
			d.startTime = event.getDownTime();
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
		case MotionEvent.ACTION_POINTER_DOWN:
//			System.out.println(action == MotionEvent.ACTION_POINTER_DOWN ? "Pointer Down" : "Pointer Up");
			actionIndex = MotionEventCompat.getActionIndex(event);
			if (MotionEventCompat.getPointerId(event, actionIndex) == pointerActive) {
				if (actionIndex == 0)
					actionIndex = 1;
				else
					actionIndex = 0;
				System.out.println("Switch Active Pointer");
				pointerStartX = MotionEventCompat.getX(event, actionIndex);
				pointerStartY = MotionEventCompat.getY(event, actionIndex);
				pointerActive = MotionEventCompat.getPointerId(event, actionIndex);
			}
			d = new EventData(MotionEventCompat.getPointerCount(event));
			for (int i = MotionEventCompat.getPointerCount(event) - 1; i >= 0; i--) {
				d.x[i] = MotionEventCompat.getX(event, i);
				d.y[i] = MotionEventCompat.getY(event, i);
			}
			d.wasMultiTouch = true;
//			System.out.println("TAP X=" + d.x[actionIndex] + " Y= " + d.y[actionIndex]);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLICK, d));
			return true;
		}
		return false;
	}
}
