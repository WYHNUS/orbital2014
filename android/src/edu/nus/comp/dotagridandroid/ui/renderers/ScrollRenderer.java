package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;
		
public class ScrollRenderer implements Renderer {
	public static final float DRAG_THRESHOLD = .01f;
	private GameLogicManager manager;
	private GLResourceManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GraphicsResponder responder;
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	private float scrollPositionX = 0, scrollPositionY = 0, processingScrollPositionX, processingScrollPositionY;
	private float scrollMinX = 0, scrollMinY = 0, scrollMaxX = 0, scrollMaxY = 0;

	private GenericProgram frameProgram;
	private List<String> renderingOrder = new ArrayList<>();
	private Map<String, Renderer> renderers = new HashMap<>();
	private Map<String, float[]> rendererTransforms = new HashMap<>();
	private boolean captured;
	private Renderer eventCapturer;
	private boolean processing;

	public ScrollRenderer () {
		frameProgram = new GenericProgram(CommonShaders.VS_IDENTITY, CommonShaders.FS_IDENTITY);
	}
	@Override
	public void setGLResourceManager(GLResourceManager manager) {
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
		if (scrollMinX != null && scrollMinX < 0)
			this.scrollMinX = scrollMinX;
		if (scrollMinY != null && scrollMinY < 0)
			this.scrollMinY = scrollMinY;
		if (scrollMaxX != null && scrollMaxX >= 0 && scrollMaxX > scrollMinX)
			this.scrollMaxX = Math.max(this.scrollMinX, scrollMaxX);
		if (scrollMaxY != null && scrollMaxY >= 0 && scrollMaxY > scrollMinY)
			this.scrollMaxY = Math.max(this.scrollMinY, scrollMaxY);
	}
	
	public void setScrollPosition(Float scrollPositionX, Float scrollPositionY) {
		if (scrollPositionX != null)
			this.scrollPositionX = scrollPositionX;
		if (scrollPositionY != null)
			this.scrollPositionY = scrollPositionY;
	}
	
	public void setRenderer(String name, Renderer r, float[] transform) {
		if (name == null)
			return;
		if (r != null) {
			if (!renderers.containsKey(name))
				renderingOrder.add(name);
			renderers.put(name, r);
		}
		else if (renderers.containsKey(name)) {
			renderers.remove(name);
			renderingOrder.remove(name);
		}
		if (transform != null)
			rendererTransforms.put(name, transform);
		else
			rendererTransforms.put(name, IdentityMatrix4x4());
	}
	
	public void clearRenderer() {
		renderingOrder.clear();
		renderers.clear();
		rendererTransforms.clear();
	}
	
	public Renderer getRenderer(String name) {
		return renderers.get(name);
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
		final int
			vPosition = glGetAttribLocation(frameProgram.getProgramId(), "vPosition"),
			vColor = glGetUniformLocation(frameProgram.getProgramId(), "vColor"),
			mModel = glGetUniformLocation(frameProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(frameProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(frameProgram.getProgramId(), "projection"),
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glEnable(GL_STENCIL_TEST);
		glClearStencil(0);
		glClear(GL_STENCIL_BITS);
		glColorMask(false, false, false, false);
		glDepthMask(false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
		// mask
		glUseProgram(frameProgram.getProgramId());
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glEnableVertexAttribArray(vPosition);
		glUniform4f(vColor, 0, 0, 0, 1);
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		// actual
		glColorMask(true, true, true, true);
		glDepthMask(true);
		glStencilFunc(GL_EQUAL, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		for (String name : renderingOrder)
			renderers.get(name).draw();
		glDisable(GL_STENCIL_TEST);
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
			if (Math.abs(e.data.deltaX) > DRAG_THRESHOLD / ratio * 2 || Math.abs(e.data.deltaY) > DRAG_THRESHOLD * ratio * 2)
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
		frameProgram.close();
		for (Renderer r : renderers.values())
			r.close();
	}

}
