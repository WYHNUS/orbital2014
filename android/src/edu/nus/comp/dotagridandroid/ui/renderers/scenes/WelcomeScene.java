package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.*;

import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;

public class WelcomeScene implements SceneRenderer {

	private GLResourceManager glResMan;
	private Map<String, Texture2D> textures;
	private GameLogicManager manager;
	private GraphicsResponder responder;
	private MainSceneRenderer mainRenderer;
	private float ratio;
	private boolean landscape, ready = false;
	private Map<String, Renderer> ui = new LinkedHashMap<>();
	
	public WelcomeScene () {
		ui.put("NewGame", new ButtonRenderer());
		ui.put("GameSave", new ButtonRenderer());
		ui.put("Exit", new ButtonRenderer());
		ui.put("Title", new TextRenderer());
		ui.put("NewGameLabel", new TextRenderer());
		ui.put("GameSaveLabel", new TextRenderer());
		ui.put("ExitLabel", new TextRenderer());
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		glResMan = manager;
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
		landscape = ratio < 1;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
		responder = mainRenderer;
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {
	}

	@Override
	public void setRenderReady() {
		for (Renderer r : ui.values()) {
			r.setAspectRatio(ratio);
			r.setGameLogicManager(manager);
			r.setGLResourceManager(glResMan);
			r.setGraphicsResponder(responder);
			r.setTexture2D(textures);
		}
	}

	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		if (updates.get("APPLICATION") instanceof Map) {
			Map<String, Object> applicationUpdates = (Map<String, Object>) updates.get("APPLICATION");
			if (applicationUpdates.containsKey("Cancel")) {
				// application exit
			} else if (applicationUpdates.containsKey("GameSave")) {
				//
			} else if (applicationUpdates.containsKey("NewGame")) {
				//
			}
		}
	}

	@Override
	public boolean getReadyState() {
		if (!ready) {
			for (Renderer r : ui.values())
				r.setRenderReady();
			ButtonRenderer r = (ButtonRenderer) ui.get("NewGame");
			r.setMVP(null, null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("NewGame", null));
			
			r = (ButtonRenderer) ui.get("GameSave");
			r.setMVP(null, null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("GameSave", null));
			
			r = (ButtonRenderer) ui.get("Exit");
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("Cancel", null));
			ready = true;
		}
		return true;
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		return false;
	}

	@Override
	public void close() {
	}

	@Override
	public void onTransferToView(SceneConfiguration configuration) {
	}

	@Override
	public void setMainSceneRenderer(MainSceneRenderer renderer) {
		mainRenderer = renderer;
	}

}
