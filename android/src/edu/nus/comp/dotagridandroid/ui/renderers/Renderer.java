package edu.nus.comp.dotagridandroid.ui.renderers;

public interface Renderer extends Closeable {
	void draw();
	void setMVP(float[] matrix);
	void setFrameBufferHandler (int framebuffer);
}
