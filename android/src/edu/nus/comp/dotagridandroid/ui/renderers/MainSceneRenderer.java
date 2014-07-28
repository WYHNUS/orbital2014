package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;
import java.util.concurrent.locks.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.*;

public class MainSceneRenderer implements Renderer {
	private final ReentrantLock lock = new ReentrantLock();
	private GLResourceManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private SceneRenderer scene;
	private boolean ready = false, calledSetRenderReady = false;
	private String readyState;

	public MainSceneRenderer () {
		// TODO: Change to scene flows
//		scene = new WelcomeScene();
//		scene = new GameScene ();
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
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
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		// init
		// TODO use game scene
		if (manager.getCurrentScene() == null)
			switchScene("Welcome", null);
		else
			switchScene(manager.getCurrentScene(), manager.getCurrentSceneConfiguration(manager.getCurrentScene()));
	}
	// synchronization is necessary
	@Override
	public boolean getReadyState() {
		lock.lock();
		if (readyState == null || "readyStateNotTested".equals(readyState)) {
			// setRenderReady requires OpenGL context
			if ("readyStateNotTested".equals(readyState)) {
				final boolean result = scene.getReadyState();
				readyState = "Verified";
				responder.updateGraphics();
				lock.unlock();
				return result;
			} else {
				scene.setRenderReady();
				readyState = "readyStateNotTested";
				responder.updateGraphics();
				lock.unlock();
				return false;	// please come back again
			}
		}
		else {
			final boolean result = scene != null && scene.getReadyState();
			lock.unlock();
			return result;
		}
	}

	@Override
	public synchronized void draw() {
		lock.lock();
		if (getReadyState())
			scene.draw();
		lock.unlock();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		lock.lock();
		if ("Verified".equals(readyState))
			scene.passEvent(e);
		lock.unlock();
		return false;	// top of event chain - no bubbling up already
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {}	// should be passed to SceneRenderers, not here
	
	public void switchScene(String name, Map<String, Object> configuration) {
		lock.lock();
		readyState = null;
		if (this.scene != null)
			scene.close();
		manager.setCurrentScene(name, configuration);
		SceneRenderer scene = null;
		switch (name) {
		case "Welcome":
			scene = new WelcomeScene();
			break;
		case "NewGame":
			break;
		case "GameSaves":
			scene = new SelectGameSaveScene();
			break;
		case "Statistics":
			scene = new GameStatisticsScene();
			break;
		case "About":
			break;
		case "Game":
			scene = new GameScene();
		}
		scene.setGLResourceManager(vBufMan);
		scene.setTexture2D(textures);
		scene.setAspectRatio(ratio);
		scene.setGameLogicManager(manager);
		scene.setGraphicsResponder(responder);
		scene.setMainSceneRenderer(this);
		scene.onTransferToView(configuration);
		this.scene = scene;
		manager.setApplicationSceneRenderer(scene);
		responder.updateGraphics();
		lock.unlock();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (ready)
			scene.close();
	}
}
