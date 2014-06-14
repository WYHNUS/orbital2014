package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;
		
public class ScrollRenderer implements Renderer {
	private GameLogicManager manager;
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GraphicsResponder responder;
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	private float scrollPositionX = 0, scrollPositionY = 0, scrollMaxX, scrollMaxY;

	private Map<String, Renderer> renderers;
	private Map<String, float[]> rendererTransforms;

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		this.vBufMan = manager;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
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
	
	public void setScrollMax (float scrollMaxX, float scrollMaxY) {
		this.scrollMaxX = scrollMaxX;
		this.scrollMaxY = scrollMaxY;
	}
	
	public void setRenderer(String name, Renderer r, float[] transform) {
		if (r != null)
			renderers.put(name, r);
		if (r != null)
			rendererTransforms.put(name, transform);
	}

	@Override
	public void setRenderReady() {
	}

	@Override
	public void notifyUpdate(Map<String, Object> updates) {
	}

	@Override
	public boolean getReadyState() {
		for (Renderer r : renderers.values())
			r.setMVP(FlatMatrix4x4Multiplication(model,model), view, projection);
		return true;
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (e.type == ControlEvent.TYPE_DOWN) {
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			//
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			//
		}
		return false;
	}

	@Override
	public void close() {
	}

}
