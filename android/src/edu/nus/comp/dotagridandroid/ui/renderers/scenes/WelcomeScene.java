package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.*;
import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class WelcomeScene implements SceneRenderer {

	private GLResourceManager glResMan;
	private Map<String, Texture2D> textures;
	private GameLogicManager manager;
	private GraphicsResponder responder;
	private MainSceneRenderer mainRenderer;
	private float ratio;
	private boolean landscape, ready = false;
	private Map<String, Renderer> ui = new LinkedHashMap<>();
	private Renderer eventCapturer;
	
	public WelcomeScene () {
		ui.put("Background", new ButtonRenderer());
		ui.put("NewGame", new ButtonRenderer());
		ui.put("GameSave", new ButtonRenderer());
		ui.put("Exit", new ButtonRenderer());
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
		landscape = ratio > 1;
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
				mainRenderer.switchScene("Game", null);
			}
		}
	}

	@Override
	public boolean getReadyState() {
		if (!ready) {
			for (Renderer r : ui.values())
				r.setRenderReady();
			ButtonRenderer r = (ButtonRenderer) ui.get("NewGame");
			r.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -.25f, -1), FlatScalingMatrix4x4(.4f, .1f, 1)), null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("NewGame", null));
			
			r = (ButtonRenderer) ui.get("GameSave");
			r.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -.5f, -1), FlatScalingMatrix4x4(.4f, .1f, 1)), null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("GameSave", null));
			
			r = (ButtonRenderer) ui.get("Exit");
			r.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -.75f, -1), FlatScalingMatrix4x4(.4f, .1f, 1)), null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("APPLICATION");
			r.setTapRespondData(Collections.singletonMap("Cancel", null));
			
			r = (ButtonRenderer) ui.get("Background");
			r.setButtonTexture("WelcomeBackground");
			r.setMVP(
					landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .5f, -1), FlatScalingMatrix4x4(.5f / ratio, .5f, 1)) :
						FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, ratio, -1), FlatScalingMatrix4x4(.5f, .5f * ratio, 1)), null, null);
			r.setTapEnabled(true);
			r.setTapRespondName("");
			r.setTapRespondData(Collections.EMPTY_MAP);
			
			TextRenderer t = (TextRenderer) ui.get("NewGameLabel");
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setText("New Game ");
			t.setMVP(
					landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f / ratio, -.2f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme() / ratio, .5f / t.getXExtreme(), 1)) :
						FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -.2f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme(), ratio * .5f / t.getXExtreme(), 1)), null, null);
			
			t = (TextRenderer) ui.get("GameSaveLabel");
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setText("Load Game");
			t.setMVP(
					landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f / ratio, -.45f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme() / ratio, .5f / t.getXExtreme(), 1)) :
						FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -.45f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme(), ratio * .5f / t.getXExtreme(), 1)), null, null);
			
			t = (TextRenderer) ui.get("ExitLabel");
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setText("  Quit   ");
			t.setMVP(
					landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f / ratio, -.7f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme() / ratio, .5f / t.getXExtreme(), 1)) :
						FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -.7f, -1), FlatScalingMatrix4x4(.5f / t.getXExtreme(), ratio * .5f / t.getXExtreme(), 1)), null, null);
			ready = true;
		}
		return true;
	}

	@Override
	public void draw() {
		glClearColor(1, 1, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		for (Map.Entry<String, Renderer> r : ui.entrySet())
			r.getValue().draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		if (eventCapturer != null && eventCapturer.passEvent(e))
			return true;
		else {
			for (Map.Entry<String, Renderer> r : ui.entrySet())
				if ((eventCapturer = r.getValue()).passEvent(e))
					return true;
			return false;
		}
	}

	@Override
	public void close() {
		for (Map.Entry<String, Renderer> r : ui.entrySet())
			r.getValue().close();
	}

	@Override
	public void onTransferToView(Map<String, Object> configuration) {
	}

	@Override
	public void setMainSceneRenderer(MainSceneRenderer renderer) {
		mainRenderer = renderer;
	}

}
