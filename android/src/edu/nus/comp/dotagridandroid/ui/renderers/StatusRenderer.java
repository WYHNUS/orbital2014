package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;
import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class StatusRenderer implements Renderer {
	private GameState state;
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GameLogicManager manager;
	private float[] model;
	private boolean pointerInside = false;
	private final float[] identity = IdentityMatrix4x4();
	private MainRenderer.GraphicsResponder responder;
	private Map<String, Renderer> controls = new HashMap<>();
	// resources
	private GenericProgram frameProgram;
	private Renderer eventCapturer;
	
	public StatusRenderer (GameState state) {
		this.state = state;
		frameProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED);
		Renderer r = new ButtonRenderer();
		controls.put("Button", r);
	}

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		this.vBufMan = manager;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setVertexBufferManager(manager);;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setTexture2D(textures);
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setAspectRatio(ratio);
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
		state = manager.getCurrentGameState();
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setGameLogicManager(manager);
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		responder = mainRenderer;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setGraphicsResponder(mainRenderer);
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {this.model = model;}

	@Override
	public void setRenderReady() {
		// control layout
		ButtonRenderer button = (ButtonRenderer) controls.get("Button");
		button.setMVP(FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(0, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)), null, null);
		button.setPressRespondName("TestButton");
		button.setRenderReady();
		responder.updateGraphics();
	}
	
	@Override
	public boolean getReadyState() {return true;}

	@Override
	public void draw() {
		// other controls
		drawFrame();
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().draw();
	}

	private void drawFrame() {
		final int
			vPosition = glGetAttribLocation(frameProgram.getProgramId(), "vPosition"),
			mModel = glGetUniformLocation(frameProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(frameProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(frameProgram.getProgramId(), "projection"),
			texture = glGetUniformLocation(frameProgram.getProgramId(), "texture"),
			textureCoord = glGetAttribLocation(frameProgram.getProgramId(), "textureCoord");
		final int
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTextureYInverted"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glUseProgram(frameProgram.getProgramId());
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(textureCoord);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get("StatusControlBackground").getTexture());
		glUniform1i(texture, 0);
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, identity, 0);
		glUniformMatrix4fv(mProjection, 1, false, identity, 0);
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (e.type == ControlEvent.TYPE_DOWN) {
			// check if inside
			final float[] pointerPositionOnControls
				= FlatMatrix4x4Vector4Multiplication(
						FlatInverseMatrix4x4(model),
						new float[]{e.data.x[0],e.data.y[0],-1,1});
			if (pointerPositionOnControls[0] <= 1 && pointerPositionOnControls[0] >= -1
					&& pointerPositionOnControls[1] <= 1 && pointerPositionOnControls[1] >= -1) {
				pointerInside = true;
				// tell the controls
				if (eventCapturer == null || !eventCapturer.passEvent(e))
					for (Map.Entry<String, Renderer> entry : controls.entrySet()) {
						eventCapturer = entry.getValue();
						if (eventCapturer.passEvent(e))
							return true;	// captured by this eventCapturer
					}
				eventCapturer = null;	// captured by myself
				return true;
			}
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			if (eventCapturer != null && !eventCapturer.passEvent(e))
				for (Map.Entry<String, Renderer> entry : controls.entrySet()) {
					eventCapturer = entry.getValue();
					if (eventCapturer.passEvent(e))
						return true;	// captured by this eventCapturer
				}
			eventCapturer = null;	// captured by myself
			return true;
		} else if (e.type == ControlEvent.TYPE_CLEAR){
			if (eventCapturer != null)
				eventCapturer.passEvent(e);
			eventCapturer = null;	// captured by myself
			pointerInside = false;
			return false;
		}
		return false;	// no capture for now
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		if (updates.containsKey("SelectedGrid")) {
		}
	}

	@Override
	public void close() {
		frameProgram.close();
	}

}
