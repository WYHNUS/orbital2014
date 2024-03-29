package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class StatusRenderer implements Renderer {
	private GameState state;
	private GLResourceManager vBufMan;
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
	private final boolean landscape;
	
	public StatusRenderer (GameState state, boolean landscape) {
		this.landscape = landscape;
		this.state = state;
		frameProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED);
		controls.put("Scroll", new ScrollRenderer());
		controls.put("Attack", new ButtonRenderer());
		controls.put("AttackLabel", new TextRenderer());
		controls.put("Move", new ButtonRenderer());
		controls.put("MoveLabel", new TextRenderer());
		controls.put("Shop", new ButtonRenderer());
		controls.put("ShopLabel", new TextRenderer());
		controls.put("UseItem", new ButtonRenderer());
		controls.put("UseItemLabel", new TextRenderer());
		controls.put("SellItem", new ButtonRenderer());
		controls.put("SellItemLabel", new TextRenderer());
		controls.put("Skill", new ButtonRenderer());
		controls.put("SkillLabel", new TextRenderer());
		controls.put("NextTurn", new ButtonRenderer());
		controls.put("NextTurnLabel", new TextRenderer());
		controls.put("ThumbnailButton", new ButtonRenderer());
		controls.put("ThumbnailLabel", new TextRenderer());
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		this.vBufMan = manager;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setGLResourceManager(manager);;
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
		ScrollRenderer scroll = (ScrollRenderer) controls.get("Scroll");
		// attack button
		ButtonRenderer button = (ButtonRenderer) controls.remove("Attack");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "Attack"));
		button.setLongPressEnabled(true);
		button.setLongPressRespondName("RequestAttackArea");
		scroll.setRenderer("Attack", button, FlatScalingMatrix4x4(.25f, .25f, 1));
		// attack label
		TextRenderer text = (TextRenderer) controls.remove("AttackLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText("Attack");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("Text", text, FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// move button
		button = (ButtonRenderer) controls.remove("Move");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "Move"));
		button.setLongPressEnabled(true);
		button.setLongPressRespondName("RequestMoveArea");
		scroll.setRenderer("Move", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -.75f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(.75f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// move label
		text = (TextRenderer) controls.remove("MoveLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText(" Move ");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("MoveLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -.5f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(.5f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// shop button
		button = (ButtonRenderer) controls.remove("Shop");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("RequestItemShop");
		scroll.setRenderer("Shop", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -1.5f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(1.5f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// shop label
		text = (TextRenderer) controls.remove("ShopLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText(" Shop ");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("ShopLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -1.25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(1.25f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// use item
		button = (ButtonRenderer) controls.remove("UseItem");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("RequestItemUse");
		scroll.setRenderer("UseItem", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -2.25f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(2.25f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// use item label
		text = (TextRenderer) controls.remove("UseItemLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText(" Items");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("UseItemLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -2, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(2, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// sell item
		button = (ButtonRenderer) controls. remove("SellItem");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("RequestSellItem");
		scroll.setRenderer("SellItem", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -3, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(3, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// sell item label
		text = (TextRenderer) controls.remove("SellItemLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText(" Sell ");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("SellItemLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -2.75f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(2.75f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// skill button
		button = (ButtonRenderer) controls.remove("Skill");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("RequestSkill");
		scroll.setRenderer("Skill", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -3.75f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(3.75f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// skill label
		text = (TextRenderer) controls.remove("SkillLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText("Skill ");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("SkillLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -3.5f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(3.5f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// next turn button
		button = (ButtonRenderer) controls.remove("NextTurn");
		button.setRenderReady();
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "NextTurn"));
		scroll.setRenderer("NextTurn", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -4.5f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(4.5f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// next turn label
		text = (TextRenderer) controls.remove("NextTurnLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setTexts(" Next ", " Turn ");
		scroll.setRenderer("NextTurnLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -4.25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(4.25f, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// scroll positioning
		scroll.setMVP(
				landscape ? FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(0, -.25f, 0), FlatScalingMatrix4x4(1, .75f, 1)) :
					FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(.25f, 0, 0), FlatScalingMatrix4x4(.75f, 1, 1)),
				null, null);
		// Thumbnail Button
		button = (ButtonRenderer) controls.get("ThumbnailButton");
		button.setTapEnabled(true);
		button.setTapRespondData(Collections.EMPTY_MAP);
		button.setTapRespondName("");
		button.setMVP(
				landscape ? FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(0, .85f, 0), FlatScalingMatrix4x4(.5f, .15f, 1)) :
					FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(-.75f, .3f, 0), FlatScalingMatrix4x4(.2f, .7f, 1)), null, null);
		button.setRenderReady();
		// Thumbnail
		text = (TextRenderer) controls.get("ThumbnailLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setYAligned(true);
		text.setText("Test");
		text.setMVP(
				landscape ? FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(-1, .7f, 0), FlatScalingMatrix4x4(.2f / text.getYExtreme(), .2f / text.getYExtreme(), 1)) :
					FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(-1, -.4f, 0), FlatScalingMatrix4x4(.15f / text.getYExtreme(), .6f / text.getYExtreme(), 1)), null, null);
		if (landscape)
			scroll.setScrollLimit(0f, 0f, 0f, 5f);
		else
			scroll.setScrollLimit(-5f, 0f, 0f, 0f);
		responder.updateGraphics();
	}
	
	@Override
	public boolean getReadyState() {
		for (Renderer r : controls.values())
			if (!r.getReadyState())
				return false;
		return true;
	}

	@Override
	public void draw() {
		// other controls
		drawStatusBar();
		drawFrame();
		for (Renderer r : controls.values())
			r.draw();
	}
	
	private void drawStatusBar() {
		// TODO status bar
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
		glBindTexture(GL_TEXTURE_2D, manager.getCurrentGameState().getModelTexture("StatusControlBackground").getTexture());//textures.get("StatusControlBackground").getTexture());
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
			if (eventCapturer == null || !eventCapturer.passEvent(e)) {
				for (Map.Entry<String, Renderer> entry : controls.entrySet()) {
					eventCapturer = entry.getValue();
					if (eventCapturer.passEvent(e))
						return true;	// captured by this eventCapturer
				}
				eventCapturer = null;	// captured by myself
			}
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
		if (updates.containsKey("ChosenGrid") || updates.containsKey("Characters")) {
			// attack button
			((ButtonRenderer) ((ScrollRenderer) controls.get("Scroll")).getRenderer("Attack")).setTapEnabled(
					manager.getCurrentGameState().areActionPossible(Collections.singletonMap("GameAction", Collections.singletonMap("Action", (Object) "Attack"))).get("GameAction")
					);
			// move button
			((ButtonRenderer) ((ScrollRenderer) controls.get("Scroll")).getRenderer("Move")).setTapEnabled(
					manager.getCurrentGameState().areActionPossible(Collections.singletonMap("GameAction", Collections.singletonMap("Action", (Object) "Move"))).get("GameAction")
					);
			final String targetCharacter = state.getCharacterAtPosition(state.getChosenGrid());
			if (targetCharacter != null
					&& state.areActionPossible(Collections.singletonMap("GameAction", Collections.singletonMap("Action", (Object) "Peek"))).get("GameAction")) {
				// update thumbnail
				ButtonRenderer button = (ButtonRenderer) controls.get("ThumbnailButton");
				button.setButtonTexture(state.getModelTexture(state.getCharacterThumbnail(targetCharacter)).getTexture());
				TextRenderer text = (TextRenderer) controls.get("ThumbnailLabel");
				text.setText(targetCharacter);
				text.setMVP(
						landscape ? FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(-1, .7f, 0), FlatScalingMatrix4x4(.2f / text.getYExtreme(), .2f / text.getYExtreme(), 1)) :
							FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(-1, -.4f, 0), FlatScalingMatrix4x4(.15f / text.getYExtreme(), .6f / text.getYExtreme(), 1)), null, null);
			} else {
				ButtonRenderer button = (ButtonRenderer) controls.get("ThumbnailButton");
				button.setButtonTexture(0);
				TextRenderer text = (TextRenderer) controls.get("ThumbnailLabel");
				text.setText(" ");
			}
		}
	}

	@Override
	public void close() {
		frameProgram.close();
		for (Renderer r : controls.values())
			r.close();
	}

}
