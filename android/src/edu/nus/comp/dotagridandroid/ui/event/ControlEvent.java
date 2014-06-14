package edu.nus.comp.dotagridandroid.ui.event;

import edu.nus.comp.dotagridandroid.ui.renderers.Renderer;

public class ControlEvent {
	public int type;
	public String extendedType;
	public EventData data;
	public Renderer emitter;
	public ControlEvent(int type, EventData data) {
		this.type = type;
		if (data == null)
			this.data = new EventData();
		else
			this.data = new EventData(data);
	}
	public ControlEvent(ControlEvent e) {
		type = e.type;
		data = new EventData (e.data);
		if ((type & TYPE_INTERPRETED) > 0)
			extendedType = new String(e.extendedType);
		emitter = e.emitter;
	}
	public static final int TYPE_CLEAR = 0;
	public static final int TYPE_DOWN = 0x01;
	public static final int TYPE_DRAG = 0x02;
	public static final int TYPE_CLICK = 0x04;	// TODO what is this?
	public static final int TYPE_INTERPRETED = 0X08;
	// event definitions
	public static final double TAP_DRIFT_LIMIT = 0.05;
	public static final long TAP_LONG_TIME_LIMIT = 800;
	public static final long TAP_DOUBLE_TIME_LIMIT = 300;
	public static final double TAP_DOUBLE_DRIFT_LIMIT = 0.1;
	public static final long DRAG_TIME_LIMIT = 80;
}
