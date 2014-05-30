package edu.nus.comp.dotagridandroid.ui.event;

public class EventData {
	public EventData(){}
	public EventData(int pointerCount) {
		this.pointerCount = pointerCount;
		x = new float[pointerCount];
		y = new float[pointerCount];
	}
	public int pointerCount;
	public float[] x = null, y = null;
	public float deltaX = 0, deltaY = 0;
	public long startTime, eventTime;
}
