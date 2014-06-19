package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.*;

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
	private final Map<String, Renderer> dialogControl = new HashMap<>();
	private float[] dialogMat;
	private GenericProgram dialogProgram;
	private boolean landscape;
	
	public GameScene () {
		dialogProgram = new GenericProgram(CommonShaders.VS_IDENTITY, CommonShaders.FS_IDENTITY);
		
	}

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
		landscape = ratio > 1;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {this.responder = mainRenderer;}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		manager.setCurrentGameState("Current");
		// TODO select character
		manager.getCurrentGameState().initialise("MyHero");
		manager.getCurrentGameState().setCurrentSceneRenderer(this);
		state = (GameState) manager.getGameState("Current");
		manager.getCurrentGameState().isInitialised();
		grid = new GridRenderer(state.getGridWidth(), state.getGridHeight(), state.getTerrain());
		status = new StatusRenderer(state, landscape);
		status.setAspectRatio(ratio);
		status.setGameLogicManager(manager);
		status.setGraphicsResponder(responder);
		status.setTexture2D(textures);
		status.setVertexBufferManager(vBufMan);
		grid.setAspectRatio(ratio);
		grid.setGameLogicManager(manager);
		grid.setGraphicsResponder(responder);
		grid.setTexture2D(textures);
		grid.setVertexBufferManager(vBufMan);
		if (landscape)
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(1-.1f*ratio,0,-1),FlatScalingMatrix4x4(.1f*ratio,1,1)),
					null, null);	// landscape mode - right side 20%
		else
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .1f/ratio-1, -1), FlatScalingMatrix4x4(1,.1f/ratio,1)),
					null, null);	// portrait model - bottom side 20%
		status.setRenderReady(); grid.setRenderReady();
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		if (updates.containsKey("Dialog"))
			prepareDialog((String) updates.get("Dialog"));
		grid.notifyUpdate(updates);
		status.notifyUpdate(updates);
	}
	
	private void prepareDialog(String dialogType) {
		dialogControl.clear();
		switch (dialogType) {
		case "ChooseSkill": {
			// display skill panel
			break;
		}
		case "ItemShop": {
			// display item shop
			if (landscape) {
				dialogMat = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.5f, .9f, 1));
			} else {
				dialogMat = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.9f, .5f, 1));
			}
			break;
		}
		case "SellItem": {
			break;
		}
		case "QuitGame": {
			break;
		}
		}
	}
	
	@Override
	public boolean getReadyState() {
		return grid.getReadyState() && status.getReadyState();
	}

	@Override
	public void draw() {
		grid.draw();
		status.draw();
		drawDialog();
	}
	
	private void drawDialog() {
		// draw background
		for (Renderer r : dialogControl.values())
			r.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (eventCapturer == null) {
			eventCapturer = status;
			if (eventCapturer.passEvent(e))
				return true;
			eventCapturer = grid;
			if (eventCapturer.passEvent(e))
				return true;
			eventCapturer = null;
			return false;
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
		if (grid != null)
			grid.close();
		if (status != null)
			status.close();
		if (manager != null)
			manager.getCurrentGameState().close();
		dialogProgram.close();
	}

	@Override
	public SceneConfiguration onTransferToView() {
		return null;
	}

}
