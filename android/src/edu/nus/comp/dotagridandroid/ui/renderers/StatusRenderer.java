package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

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
	// resources
	private GenericProgram frameProgram;
	
	public StatusRenderer (GameState state) {
		this.state = state;
		frameProgram = new GenericProgram(CommonShaders.VS_IDENTITY, CommonShaders.FS_IDENTITY);
	}

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		// TODO Auto-generated method stub
		this.vBufMan = manager;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {this.textures = textures;}

	@Override
	public void setAspectRatio(float ratio) {this.ratio = ratio;}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		responder = mainRenderer;
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {this.model = model;}

	@Override
	public void setRenderReady() {
		responder.updateGraphics();
	}
	
	@Override
	public boolean getReadyState() {return true;}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		drawFrame();
		// other controls - not yet
	}

	private void drawFrame() {
		int
			vPosition = glGetAttribLocation(frameProgram.getProgramId(), "vPosition"),
			vColor = glGetUniformLocation(frameProgram.getProgramId(), "vColor"),
			mModel = glGetUniformLocation(frameProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(frameProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(frameProgram.getProgramId(), "projection");
		int vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare");
		glUseProgram(frameProgram.getProgramId());
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, identity, 0);
		glUniformMatrix4fv(mProjection, 1, false, identity, 0);
		glUniform4f(vColor, 0, 0, 0, 1);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glDrawArrays(GL_LINE_LOOP, 0, 4);
		glDisableVertexAttribArray(vPosition);
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
				System.out.println();
				return true;
			}
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			return true;
		} else if (e.type == ControlEvent.TYPE_CLEAR){
			pointerInside = false;
			return false;
		}
		return false;	// no capture for now
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
