package edu.nus.comp.dotagridandroid.ui.event;

public class ControlEvent {
	public int type;
	public EventData data;
	public ControlEvent(int type, EventData data) {
		this.type = type;
		this.data = data;
	}
	public static final int TYPE_CLEAR = 0;
	public static final int TYPE_CLICK = 0x01;
	public static final int TYPE_DRAG = 0x02;
	public static final double TAP_DRIFT_LIMIT = 0.01;
}
