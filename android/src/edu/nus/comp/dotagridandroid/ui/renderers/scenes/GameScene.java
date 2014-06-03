package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GameScene implements SceneRenderer {
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private GameState state;
	private float ratio;
	private Map<String, Texture2D> textures;
	private VertexBufferManager vBufMan;
	// resources
	private Renderer grid, status, eventCapturer;

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		this.vBufMan = manager;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {this.responder = mainRenderer;}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		state = (GameState) manager.getGameState("Current");
		grid = new GridRenderer(state.getGridHeight(), state.getGridWidth());
		status = new StatusRenderer(state);
		status.setAspectRatio(ratio);
		status.setGameLogicManager(manager);
		status.setGraphicsResponder(responder);
		status.setTexture2D(textures);
		status.setVertexBufferManager(vBufMan);
		if (ratio > 1)
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(1-.1f*ratio,0,-1),FlatScalingMatrix4x4(.1f*ratio,1,1)),
					null, null);	// landscape mode - right side 20%
		else
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .1f/ratio-1, -1), FlatScalingMatrix4x4(1,.1f/ratio,1)),
					null, null);	// portrait model - bottom side 20%
		grid.setAspectRatio(ratio);
		grid.setGameLogicManager(manager);
		grid.setGraphicsResponder(responder);
		grid.setTexture2D(textures);
		grid.setVertexBufferManager(vBufMan);
		status.setRenderReady(); grid.setRenderReady();
	}
	
	@Override
	public boolean getReadyState() {return true;}

	@Override
	public void draw() {
		if (grid.getReadyState())
			grid.draw();
		if (status.getReadyState())
			status.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (eventCapturer == null) {
			eventCapturer = status;
			if (eventCapturer.passEvent(e))
				return true;
			else {
				eventCapturer = grid;
				if (eventCapturer.passEvent(e))
					return true;
				else {
					eventCapturer = null;
					return false;
				}
			}
		} else if (eventCapturer.passEvent(e))
			return true;
		else {
			System.out.println();
			eventCapturer = null;
			return false;
		}
	}

	@Override
	public void close() {
		grid.close();
		status.close();
	}

	@Override
	public SceneConfiguration onTransferToView() {
		// TODO Auto-generated method stub
		return null;
	}

}
