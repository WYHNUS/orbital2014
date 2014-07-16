package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.*;

import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class SelectGameSaveScene implements SceneRenderer {

	private GLResourceManager glResMan;
	private Map<String, Texture2D> textures;
	private GameLogicManager manager;
	private GraphicsResponder responder;
	
	private Renderer eventCapturer;
	private MainSceneRenderer mainRenderer;
	
	private List<String> gameList;
	private ScrollRenderer scroll;
	
	@Override
	public void setMainSceneRenderer(MainSceneRenderer renderer) {
		mainRenderer = renderer;
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
	}
	
	private void getGameList() {
		if (scroll != null)
			scroll.close();
		gameList = manager.getGameList();
		scroll = new ScrollRenderer();
		scroll.setGameLogicManager(manager);
		scroll.setGraphicsResponder(responder);
		scroll.setGLResourceManager(glResMan);
		scroll.setRenderReady();
		int c = 0;
		for (String gameName : gameList) {
			ButtonRenderer r = new ButtonRenderer();
			r.setTexture2D(textures);
			r.setGLResourceManager(glResMan);
			r.setGameLogicManager(manager);
			r.setGraphicsResponder(responder);
			r.setRenderReady();
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("LoadGame", (Object) gameName));
			scroll.setRenderer(gameName, r, FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .7f - .5f * c, 0), FlatScalingMatrix4x4(.8f, .2f, 1)));
			TextRenderer t = new TextRenderer();
			t.setGameLogicManager(manager);
			t.setGLResourceManager(glResMan);
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setRenderReady();
			t.setText(gameName);
			final float xLimit = t.getXExtreme(), yLimit = t.getYExtreme();
			scroll.setRenderer(gameName + "-text", t,
					FlatMatrix4x4Multiplication(
							FlatTranslationMatrix4x4(-.5f / xLimit, .7f - .5f * c + .5f * yLimit / xLimit, 0),
							FlatScalingMatrix4x4(1f / xLimit, 1f / xLimit, 1)));
			c++;
		}
		scroll.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .2f, 0), FlatScalingMatrix4x4(1, .8f, 1)), null, null);
		scroll.setScrollLimit(.5f * c - 1, 0f, 0f, 0f);
		ButtonRenderer r = new ButtonRenderer();
		r.setTexture2D(textures);
		r.setGLResourceManager(glResMan);
		r.setGameLogicManager(manager);
		r.setMVP(null, null, null);
		r.setRenderReady();
		r.setTapEnabled(true);
		r.setTapRespondName("APPLICATION");
		r.setTapRespondData(Collections.singletonMap("Cancel", null));
	}

	@Override
	public void setRenderReady() {
		getGameList();
	}

	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		if (updates.get("APPLICATION") instanceof Map) {
			Map<String, Object> appUpdates = (Map<String, Object>) updates.get("APPLICATION");
			if (appUpdates.containsKey("Cancel"))
				mainRenderer.switchScene("Welcome", null);
		}
	}

	@Override
	public boolean getReadyState() {
		return scroll.getReadyState();
	}

	@Override
	public void draw() {
		scroll.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (eventCapturer != null && eventCapturer.passEvent(e))
			return true;
		else if ((eventCapturer = scroll).passEvent(e))
			return true;
		else {
			eventCapturer = null;
			return false;
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void onTransferToView(SceneConfiguration configuration) {
	}

}
