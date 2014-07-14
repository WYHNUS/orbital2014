package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.*;

public class MainSceneRenderer implements Renderer {
	
	private GLResourceManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private SceneRenderer scene;

	public MainSceneRenderer () {
		// TODO: Change to scene flows
//		scene = new WelcomeScene();
		scene = new GameScene ();
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		this.vBufMan = manager;
		scene.setGLResourceManager(manager);
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
		scene.setTexture2D(textures);
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
		scene.setAspectRatio(ratio);
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
		scene.setGameLogicManager(manager);
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
		scene.setGraphicsResponder(mainRenderer);
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		// init
		// TODO use game scene
		scene.setRenderReady();
	}

	@Override
	public boolean getReadyState() {
		return scene.getReadyState();
	}

	@Override
	public void draw() {
		scene.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		scene.passEvent(e);
		return false;	// top of event chain - no bubbling up already
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {}	// should be passed to SceneRenderers, not here
	
	public void switchScene(String name, SceneConfiguration configuration) {
		SceneRenderer scene = null;
		switch (name) {
		case "Welcome":
			break;
		case "GameSaves":
			scene = new SelectGameScene();
			break;
		case "Statistics":
			break;
		case "About":
			break;
		case "Game":
			scene = new GameScene();
		}
		manager.setApplicationSceneRenderer(scene);
		scene.onTransferToView(configuration);
		this.scene = scene;
		scene.setGLResourceManager(vBufMan);
		scene.setTexture2D(textures);
		scene.setAspectRatio(ratio);
		scene.setGameLogicManager(manager);
		scene.setGraphicsResponder(responder);
		scene.setRenderReady();	// TODO problematic
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		scene.close();
	}
}
