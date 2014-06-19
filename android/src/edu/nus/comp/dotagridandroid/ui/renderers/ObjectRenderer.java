package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class ObjectRenderer implements Renderer {

	private GLResourceManager vBufMan;
	private float[] model, view, projection;

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		this.vBufMan = manager;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
	}

	@Override
	public void setAspectRatio(float ratio) {
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
	}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
		// dont use
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {
		if (model != null)
			this.model = model;
		if (view != null)
			this.view = view;
		if (projection != null)
			this.projection = projection;
	}

	@Override
	public void setRenderReady() {
	}

	@Override
	public boolean getReadyState() {
		return true;
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		// dont capture any event
		return false;
	}

	@Override
	public void notifyUpdate(Map<String, Object> updates) {}
	@Override
	public void close() {
	}

}
