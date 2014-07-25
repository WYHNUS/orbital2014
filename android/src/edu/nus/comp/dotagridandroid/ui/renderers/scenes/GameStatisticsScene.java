package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.Collections;
import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GameStatisticsScene implements SceneRenderer {

	private GLResourceManager glResMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GameLogicManager manager;
	private GraphicsResponder responder;
	private MainSceneRenderer mainRenderer;
	private TextRenderer text, cancelLabel;
	private ButtonRenderer cancelButton;
	private boolean isGameWon;
	
	public GameStatisticsScene() {
		text = new TextRenderer();
		cancelButton = new ButtonRenderer();
		cancelLabel = new TextRenderer();
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		this.glResMan = manager;
		text.setGLResourceManager(manager);
		cancelButton.setGLResourceManager(manager);
		cancelLabel.setGLResourceManager(manager);
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
		text.setTexture2D(textures);
		cancelButton.setTexture2D(textures);
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
		text.setGameLogicManager(manager);
		cancelButton.setGameLogicManager(manager);
		cancelLabel.setGameLogicManager(manager);
	}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
		text.setGraphicsResponder(mainRenderer);
		cancelButton.setGraphicsResponder(mainRenderer);
		cancelLabel.setGraphicsResponder(mainRenderer);
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {
	}

	@Override
	public void setRenderReady() {
		cancelButton.setRenderReady();
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		cancelLabel.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		cancelLabel.setRenderReady();
		if (isGameWon)
			text.setText("You win the battle");
		else
			text.setText("You lost the  battle");
		text.setMVP(FlatMatrix4x4Multiplication(
				FlatScalingMatrix4x4(1.2f, 1.2f, 1),
				FlatTranslationMatrix4x4(-.5f, text.getYExtreme() / 2, -.9f),
				FlatScalingMatrix4x4(1 / text.getXExtreme(), 1, 1)), null, null);
		cancelButton.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -.8f, -.9f), FlatScalingMatrix4x4(.4f, .2f, 1)), null, null);
		cancelButton.setTapEnabled(true);
		cancelButton.setTapRespondName("APPLICATION");
		cancelButton.setTapRespondData(Collections.singletonMap("BackToWelcome", (Object) true));
		cancelLabel.setText("Back");
		cancelLabel.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.4f, -.6f, -.9f), FlatScalingMatrix4x4(.8f/4, .8f/4, 1)), null, null);
	}

	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		if (updates.containsKey("BackToWelcome"))
			mainRenderer.switchScene("Welcome", null);
	}

	@Override
	public boolean getReadyState() {
		return text.getReadyState() && cancelButton.getReadyState();
	}

	@Override
	public void draw() {
		text.draw();
		cancelButton.draw();
		cancelLabel.draw();
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		return cancelButton.passEvent(e);
	}

	@Override
	public void close() {
		text.close();
		cancelButton.close();
		cancelLabel.close();
	}

	@Override
	public void onTransferToView(Map<String, Object> configuration) {
		isGameWon = "Win".equals(configuration.get("GameResult"));
	}

	@Override
	public void setMainSceneRenderer(MainSceneRenderer renderer) {
		this.mainRenderer = renderer;
	}

}
