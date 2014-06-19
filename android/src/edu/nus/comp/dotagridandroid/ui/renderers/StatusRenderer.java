package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

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
		controls.put("Skill", new ButtonRenderer());
		controls.put("SkillLabel", new TextRenderer());
	}

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		this.vBufMan = manager;
		for (Map.Entry<String, Renderer> entry : controls.entrySet())
			entry.getValue().setVertexBufferManager(manager);;
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
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "Attack"));
		button.setLongPressEnabled(true);
		button.setLongPressRespondName("GameAction");
		button.setLongPressRespondData(Collections.singletonMap("Action", (Object) "RequestAttackArea"));
		button.setRenderReady();
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
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "Move"));
		button.setLongPressEnabled(true);
		button.setLongPressRespondName("GameAction");
		button.setLongPressRespondData(Collections.singletonMap("Action", (Object) "RequestMoveArea"));
		button.setRenderReady();
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
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "RequestShop"));
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
		scroll.setMVP(FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(.375f, 0, 0), FlatScalingMatrix4x4(.75f, 1, 1)), null, null);
		// skill button
		button = (ButtonRenderer) controls.remove("Skill");
		button.setTapEnabled(true);
		button.setTapRespondName("GameAction");
		button.setTapRespondData(Collections.singletonMap("Action", (Object) "RequestSkill"));
		scroll.setRenderer("Skill", button,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, -2.25f, 0), FlatScalingMatrix4x4(.25f, .25f, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(2.25f, 0, 0), FlatScalingMatrix4x4(.25f, .25f, 1)));
		// skill label
		text = (TextRenderer) controls.remove("SkillLabel");
		text.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		text.setRenderReady();
		text.setText("Skill ");
		text.setTextColour(new float[]{1,-1,-1,1});
		scroll.setRenderer("SkillLabel", text,
				landscape ? FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(-.25f, -2, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)) :
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(2, .25f, 0), FlatScalingMatrix4x4(.5f/6, .5f/6, 1)));
		// scroll positioning
		scroll.setMVP(FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(.375f, 0, 0), FlatScalingMatrix4x4(.75f, 1, 1)), null, null);
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
		glBindTexture(GL_TEXTURE_2D, textures.get("StatusControlBackground").getTexture());
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
		if (updates.containsKey("ChosenGrid")) {
			System.out.println("Update buttons");
			// attack button
			((ButtonRenderer) ((ScrollRenderer) controls.get("Scroll")).getRenderer("Attack")).setTapEnabled(
					manager.getCurrentGameState().areActionPossible(Collections.singletonMap("GameAction", Collections.singletonMap("Action", (Object) "Attack"))).get("GameAction")
					);
			// move button
		}
	}

	@Override
	public void close() {
		frameProgram.close();
		for (Renderer r : controls.values())
			r.close();
	}

}
