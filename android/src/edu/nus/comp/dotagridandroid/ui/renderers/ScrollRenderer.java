package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;
		
public class ScrollRenderer implements Renderer {
	public static final float DRAG_THRESHOLD = .01f;
	private GameLogicManager manager;
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GraphicsResponder responder;
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	private float scrollPositionX = 0, scrollPositionY = 0, processingScrollPositionX, processingScrollPositionY;
	private float scrollMinX = 0, scrollMinY = 0, scrollMaxX = 0, scrollMaxY = 0;

	private List<String> renderingOrder = new ArrayList<>();
	private Map<String, Renderer> renderers = new HashMap<>();
	private Map<String, float[]> rendererTransforms = new HashMap<>();
	private boolean captured;
	private Renderer eventCapturer;
	private boolean processing;

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
	
	public void setScrollLimit (Float scrollMinX, Float scrollMinY, Float scrollMaxX, Float scrollMaxY) {
		if (scrollMinX != null)
			this.scrollMinX = scrollMinX;
		if (scrollMinY != null)
			this.scrollMinY = scrollMinY;
		if (scrollMaxX != null)
			this.scrollMaxX = scrollMaxX;
		if (scrollMaxY != null)
			this.scrollMaxY = scrollMaxY;
	}
	
	public void setScrollPosition(Float scrollPositionX, Float scrollPositionY) {
		if (scrollPositionX != null)
			this.scrollPositionX = scrollPositionX;
		if (scrollPositionY != null)
			this.scrollPositionY = scrollPositionY;
	}
	
	public void setRenderer(String name, Renderer r, float[] transform) {
		if (r != null) {
			if (!renderers.containsKey(name))
				renderingOrder.add(name);
			renderers.put(name, r);
		}
		else if (renderers.containsKey(name)) {
			renderers.remove(name);
			renderingOrder.remove(name);
		}
		if (name != null && transform != null)
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
		boolean ready = true;
		for (Map.Entry<String, Renderer> entry : renderers.entrySet()) {
			entry.getValue().setMVP(
					FlatMatrix4x4Multiplication(
							model,
							processing ?
									FlatTranslationMatrix4x4(processingScrollPositionX, processingScrollPositionY, 0) :
									FlatTranslationMatrix4x4(scrollPositionX, scrollPositionY, 0),
							rendererTransforms.get(entry.getKey())),
					view, projection);
			ready &= entry.getValue().getReadyState();
		}
		return ready;
	}

	@Override
	public void draw() {
		for (String name : renderingOrder)
			renderers.get(name).draw();
//		for (Renderer r : renderers.values())
//			r.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		final float[] inverseMVP = FlatInverseMatrix4x4(FlatMatrix4x4Multiplication(this.projection, this.view, this.model));
		final float[] displacement = FlatMatrix4x4Vector4Multiplication(inverseMVP, new float[]{e.data.deltaX, e.data.deltaY, 0, 0});
		final float[] position = FlatMatrix4x4Vector4Multiplication(inverseMVP, new float[]{e.data.x[0], e.data.y[0], -1, 1});
		if (e.type == ControlEvent.TYPE_DOWN) {
			captured = false;
			for (Renderer r : renderers.values()) {
				eventCapturer = r;
				if (eventCapturer.passEvent(e))
					return true;
			}
			return e.data.pointerCount == 1 && position[0] >= -1 && position[0] <= 1 && position[1] >= -1 && position[1] <= 1;
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			if (captured) {
				processing = true;
				if (scrollPositionX + displacement[0] > scrollMaxX)
					processingScrollPositionX = scrollMaxX;
				else if (scrollPositionX + displacement[0] < scrollMinX)
					processingScrollPositionX = scrollMinX;
				else
					processingScrollPositionX = scrollPositionX + displacement[0];
				if (scrollPositionY + displacement[1] > scrollMaxY)
					processingScrollPositionY = scrollMaxY;
				else if (scrollPositionY + displacement[1] < scrollMinY)
					processingScrollPositionY = scrollMinY;
				else
					processingScrollPositionY = scrollPositionY + displacement[1];
				responder.updateGraphics();
				return true;
			} else if (eventCapturer == null || !eventCapturer.passEvent(e)) {
				Renderer prevEventCapturer = eventCapturer;
				for (Renderer r : renderers.values()) {
					if (r == prevEventCapturer)
						continue;
					eventCapturer = r;
					if (eventCapturer.passEvent(e))
						return true;
				}
			}
			eventCapturer = null;
			if (Math.abs(displacement[0]) > DRAG_THRESHOLD || Math.abs(displacement[1]) > DRAG_THRESHOLD)
				captured = true;
			return true;
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (captured) {
				scrollPositionX = processingScrollPositionX;
				scrollPositionY = processingScrollPositionY;
				processing = false;
			} else if (eventCapturer == null || !eventCapturer.passEvent(e)) {
				Renderer prevEventCapturer = eventCapturer;
				for (Renderer r : renderers.values()) {
					if (r == prevEventCapturer)
						continue;
					eventCapturer = r;
					eventCapturer.passEvent(e);
				}
			}
			eventCapturer = null;
			captured = false;
			return false;
		}
		captured = false;
		return false;
	}

	@Override
	public void close() {
		for (Renderer r : renderers.values())
			r.close();
	}

}
