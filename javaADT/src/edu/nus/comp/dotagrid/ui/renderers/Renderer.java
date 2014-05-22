package edu.nus.comp.dotagrid.ui.renderers;

import java.io.Closeable;

public interface Renderer extends Closeable {
	void draw();
	void setMVP(float[] matrix);
}
