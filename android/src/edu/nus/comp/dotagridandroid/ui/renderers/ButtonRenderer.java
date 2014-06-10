package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;
import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class ButtonRenderer implements Renderer {

	private float[] model, view, projection;
	private GenericProgram buttonProgram;
	private boolean pressed = false;
	private Renderer eventResponder;
	private GraphicsResponder responder;
	public ButtonRenderer () {
		model = view = projection = IdentityMatrix4x4();
		buttonProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED);
	}

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
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
		responder = mainRenderer;
	}
	
	public void setPressResponder(Renderer r) {
		eventResponder = r;
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
		return false;
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		final boolean hit;
		final float[] mvp = FlatMatrix4x4Multiplication(projection, view, model);
		final float[] pos = FlatMatrix4x4Multiplication(FlatInverseMatrix4x4(mvp), new float[]{e.data.x[0], e.data.y[0], -1, 1});
		hit = pos[0] >= -1 && pos[0] <= 1 && pos[1] >= -1 && pos[1] <= 1; 
		if (e.type == ControlEvent.TYPE_DOWN) {
			pressed = true;
			return hit && e.data.pointerCount == 1;
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			return pressed;
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (hit && e.data.pointerCount == 1) {
				
			}
			return false;
		}
		pressed = false;
		return false;
	}

	@Override
	public void close() {
	}

}
