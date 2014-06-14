package edu.nus.comp.dotagridandroid.ui.event;
import java.util.*;
public class EventData {
	public EventData(){}
	public EventData(int pointerCount) {
		this.pointerCount = pointerCount;
		x = new float[pointerCount];
		y = new float[pointerCount];
	}
	public EventData (EventData data) {
		pointerCount = data.pointerCount;
		x = data.x.clone();
		y = data.y.clone();
		deltaX = data.deltaX;
		deltaY = data.deltaY;
		startTime = data.startTime;
		eventTime = data.eventTime;
		wasMultiTouch = data.wasMultiTouch;
		extendedData = new HashMap<>();
		extendedData.putAll(data.extendedData);
	}
	public int pointerCount = 0;
	public float[] x = null, y = null;
	public float deltaX = 0, deltaY = 0;
	public long startTime = 0, eventTime = 0;
	public boolean wasMultiTouch = false;
	public Map<String, Object> extendedData = new HashMap<>();
}
