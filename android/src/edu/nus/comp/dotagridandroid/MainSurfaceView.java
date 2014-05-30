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
	
	public MainSurfaceView(Context context) {
		super(context);
		init(context);
	}
	
	public MainSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);
		setEGLContextClientVersion(2);
		setRenderer(r = new MainRenderer(context));
		// just in case: turn on the below will reduce draw cycles, but we probably don't need it
//		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// clean up
		super.onConfigurationChanged(newConfig);
		r.close();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event), actionIndex;
		EventData d;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			actionIndex = MotionEventCompat.getActionIndex(event);
			d = new EventData(1);
			d.x[actionIndex] = pointerStartX = MotionEventCompat.getX(event, actionIndex);
			d.y[actionIndex] = pointerStartY = MotionEventCompat.getY(event, actionIndex);
			pointerActive = MotionEventCompat.getPointerId(event, 0);
			System.out.println("TAP X=" + d.x + " Y= " + d.y);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLICK, d));
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
			System.out.println("ACTION_INDEX: " + actionIndex + "DRAG X= " + d.x + " Y= " + d.y + " DX= " + d.deltaX + " DY " + d.deltaY);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_DRAG, d));
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			actionIndex = MotionEventCompat.findPointerIndex(event, pointerActive);
			d = new EventData(1);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			d.x[actionIndex] = MotionEventCompat.getX(event, actionIndex);
			d.y[actionIndex] = MotionEventCompat.getY(event, actionIndex);
			d.deltaX = MotionEventCompat.getX(event, actionIndex) - pointerStartX;
			d.deltaY = MotionEventCompat.getY(event, actionIndex) - pointerStartY;
			pointerActive = -1;
			System.out.println("CLEAR TIME=" + event.getDownTime() + "X");
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLEAR, d));
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
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
			System.out.println("TAP X=" + d.x[actionIndex] + " Y= " + d.y[actionIndex]);
			d.startTime = event.getDownTime();
			d.eventTime = event.getEventTime();
			r.passEvent(new ControlEvent(ControlEvent.TYPE_CLICK, d));
			return true;
		}
		return false;
	}
}
