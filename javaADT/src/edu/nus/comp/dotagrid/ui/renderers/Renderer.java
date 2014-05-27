package edu.nus.comp.dotagrid.ui.renderers;

public interface Renderer extends AutoCloseable {
	void draw();
	void setMVP(float[] matrix);
	void setFrameBufferHandler (int framebuffer);
}
