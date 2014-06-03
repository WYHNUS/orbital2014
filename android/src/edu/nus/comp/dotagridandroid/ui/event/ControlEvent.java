package edu.nus.comp.dotagridandroid.ui.event;

public class ControlEvent {
	public int type;
	public EventData data;
	public ControlEvent(int type, EventData data) {
		this.type = type;
		this.data = data;
	}
	public ControlEvent(ControlEvent e) {
		type = e.type;
		data = new EventData (e.data);
	}
	public static final int TYPE_CLEAR = 0;
	public static final int TYPE_DOWN = 0x01;
	public static final int TYPE_CLICK = 0x02;
	public static final int TYPE_DRAG = 0x04;
	// event definitions
	public static final double TAP_DRIFT_LIMIT = 0.05;
	public static final long TAP_LONG_TIME_LIMIT = 800;
	public static final long TAP_DOUBLE_TIME_LIMIT = 300;
	public static final double TAP_DOUBLE_DRIFT_LIMIT = 0.1;
	public static final long DRAG_TIME_LIMIT = 80;
}
