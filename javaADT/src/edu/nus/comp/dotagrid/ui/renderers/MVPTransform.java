package edu.nus.comp.dotagrid.ui.renderers;

public class MVPTransform {
	public float[] model, view, projection;
	public MVPTransform () {
		model = new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
		view = new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
		projection = new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
	}
	public void compose () {
		// P * V * M
		
	}
	public void composeInverse () {
		//
	}
	public void setZoom (float zoomFactor) {
		
	}
}
