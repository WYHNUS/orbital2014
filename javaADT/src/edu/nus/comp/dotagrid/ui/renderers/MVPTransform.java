package edu.nus.comp.dotagrid.ui.renderers;
import static edu.nus.comp.dotagrid.math.RenderMaths.*;
public class MVPTransform {
	private float[] model, view, projection, result;
	private boolean dirty = false;
	public float[] getModel() {return model.clone();}
	public float[] setModel(float[] model) {
		dirty = true;
		return this.model = (model == null) ? IdentityMatrix4x4():model;
	}
	
	public float[] getView() {return view.clone();}
	public float[] setView(float[] view) {
		dirty = true;
		return this.view = (view == null) ? IdentityMatrix4x4():view;
	}
	
	public float[] getProjection() {return projection.clone();}
	public float[] setProjection(float[] projection) {
		dirty = true;
		return this.projection = (projection == null) ? IdentityMatrix4x4():projection;
	}
	
	public boolean isDirty() {return dirty;}
	
	public MVPTransform () {
		model = IdentityMatrix4x4();
		view = IdentityMatrix4x4();
		projection = IdentityMatrix4x4();
		result = IdentityMatrix4x4();
	}
	public MVPTransform (MVPTransform t) {
		if (t == null)
			throw new RuntimeException();
		dirty = true;
		model = t.model.clone();
		view = t.view.clone();
		projection = t.projection.clone();
		compose();
	}
	public float[] compose () {
		// P * V * M
		if (dirty) {
			dirty = false;
			return result = FlatMatrix4x4Multiplication(projection, FlatMatrix4x4Multiplication (view, model));
		} else
			return result;
	}
	public void composeInverse () {
		compose();
		result = FlatInverseMatrix4x4 (result);
	}
}
