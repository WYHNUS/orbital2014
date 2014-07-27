package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import java.util.*;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GameScene implements SceneRenderer {
	private static final int CELLS_PER_ROW = 3;
	private static final float CELL_MARGIN = .1f;
	private static final int MAX_LOG_LENGTH = 10;
	private static final int MAX_LOG_WIDTH = 60;
	
	private static final int MESSAGE_CHAR_PER_ROW = 20;
	private float[] waitPromptMat;
	
	private MainSceneRenderer mainRenderer;
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private GameState state;
	private float ratio;
	private Map<String, Texture2D> textures;
	private GLResourceManager vBufMan;
	// resources
	private Renderer grid, status, eventCapturer;
	private TextRenderer log, waitLabel;
	private final Map<String, Renderer> dialogControl = new LinkedHashMap<>();	// IMPORTANT: necessary to maintain order of drawing
	private float[] dialogMat;
	private GenericProgram dialogProgram;
	private boolean landscape;
	private boolean hasDialog;
	private LinkedList<String> logQueue = new LinkedList<>();
	
	public GameScene () {
		dialogProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED);
		log = new TextRenderer();
		waitLabel = new TextRenderer();
	}
	
	@Override
	public void setMainSceneRenderer(MainSceneRenderer renderer) {
		mainRenderer = renderer;
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		this.vBufMan = manager;
		log.setGLResourceManager(manager);
		waitLabel.setGLResourceManager(manager);
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
		log.setTexture2D(textures);
		waitLabel.setTexture2D(textures);
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
		log.setAspectRatio(ratio);
		waitLabel.setAspectRatio(ratio);
		landscape = ratio > 1;
		if (landscape)
			waitPromptMat = FlatMatrix4x4Multiplication(
					FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.6f, .6f * ratio, 1));
		else
			waitPromptMat = FlatMatrix4x4Multiplication(
					FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.6f / ratio, .6f, 1));
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
		log.setGameLogicManager(manager);
		waitLabel.setGameLogicManager(manager);
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
		log.setGraphicsResponder(mainRenderer);
		waitLabel.setGraphicsResponder(mainRenderer);
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		if (manager.getCurrentGameState() == null)
			manager.setCurrentGameState("Current");
		// TODO select character
		manager.getCurrentGameState().initialise("MyHero");
		manager.getCurrentGameState().setCurrentSceneRenderer(this);
		state = manager.getGameState("Current");
		grid = new GridRenderer(state.getGridWidth(), state.getGridHeight(), state.getTerrain());
		status = new StatusRenderer(state, landscape);
		status.setAspectRatio(ratio);
		status.setGameLogicManager(manager);
		status.setGraphicsResponder(responder);
		status.setTexture2D(textures);
		status.setGLResourceManager(vBufMan);
		grid.setAspectRatio(ratio);
		grid.setGameLogicManager(manager);
		grid.setGraphicsResponder(responder);
		grid.setTexture2D(textures);
		grid.setGLResourceManager(vBufMan);
		if (landscape)
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(1-.1f*ratio,0,-1),FlatScalingMatrix4x4(.1f*ratio,1,1)),
					null, null);	// landscape mode - right side 20%
		else
			status.setMVP(
					FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, .1f/ratio-1, -1), FlatScalingMatrix4x4(1,.1f/ratio,1)),
					null, null);	// portrait model - bottom side 20%
		status.setRenderReady(); grid.setRenderReady();
		log.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		log.setRenderReady();
		if (landscape)
			log.setMVP(
					FlatMatrix4x4Multiplication(
							FlatTranslationMatrix4x4(-1, 1, -1),
							FlatScalingMatrix4x4(1f / MAX_LOG_WIDTH, 1f / MAX_LOG_WIDTH * ratio, 1)), null, null);
		else
			log.setMVP(
					FlatMatrix4x4Multiplication(
							FlatTranslationMatrix4x4(-1, 1, -1),
							FlatScalingMatrix4x4(1f / MAX_LOG_WIDTH / ratio, 1f / MAX_LOG_WIDTH, 1)), null, null);
		waitLabel.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
		waitLabel.setRenderReady();
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		hasDialog = false;
		if ("Win".equals(updates.get("GameResult")))
			mainRenderer.switchScene("Statistics", Collections.singletonMap("GameResult", (Object) "Win"));
		else if ("Lost".equals(updates.get("GameResult")))
			mainRenderer.switchScene("Statistics", Collections.singletonMap("GameResult", (Object) "Lost"));
		else if (updates.containsKey("Dialog"))
			prepareDialog((String) updates.get("Dialog"), updates);
		if (updates.containsKey("Log"))
			logQueue.add((String) updates.get("Log"));
		if (logQueue.size() > MAX_LOG_LENGTH)
			logQueue.poll();
		log.setTexts(logQueue.toArray(new String[logQueue.size()]));
		grid.notifyUpdate(updates);
		status.notifyUpdate(updates);
	}
	
	private void prepareDialog(String dialogType, Map<String, Object> options) {
		hasDialog = false;
		for (Renderer r : dialogControl.values())
			r.close();
		dialogControl.clear();
		switch (dialogType) {
		case "Message": {
			// display message
			if (landscape) {
				dialogMat = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.5f, .9f, 1));
			} else {
				dialogMat = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, 0, -1), FlatScalingMatrix4x4(.9f, .5f, 1));
			}
			String[] lines = ((String) options.get("Message")).split("\n");
			List<String> brokenLines = new ArrayList<>();
			for (String line : lines) {
				String brokenLine = line;
				while (brokenLine.length() > MESSAGE_CHAR_PER_ROW) {
					brokenLines.add(brokenLine.substring(0, MESSAGE_CHAR_PER_ROW));
					brokenLine = brokenLine.substring(MESSAGE_CHAR_PER_ROW);
				}
				if (brokenLine.length() > 0)
					brokenLines.add(brokenLine);
			}
			ScrollRenderer scroll = new ScrollRenderer();
			scroll.setGLResourceManager(vBufMan);
			scroll.setGameLogicManager(manager);
			scroll.setTexture2D(textures);
			scroll.setGraphicsResponder(responder);
			scroll.setMVP(FlatMatrix4x4Multiplication(dialogMat,FlatTranslationMatrix4x4(0, .1f, 0),FlatScalingMatrix4x4(1, .9f, 1)), null, null);
			TextRenderer t = new TextRenderer();
			t.setTexture2D(textures);
			t.setGLResourceManager(vBufMan);
			t.setAspectRatio(ratio);
			t.setGraphicsResponder(responder);
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setTextColour(new float[]{-1,-1,1,0});
			t.setRenderReady();
			t.setTexts(brokenLines.toArray(new String[brokenLines.size()]));
			scroll.setRenderer("Message", t, FlatMatrix4x4Multiplication(
					FlatTranslationMatrix4x4(-1, 1, 0),
					FlatScalingMatrix4x4(2f / MESSAGE_CHAR_PER_ROW, 2f / MESSAGE_CHAR_PER_ROW, 1)));
			scroll.setScrollLimit(0f, 0f, 0f, 2 * t.getYExtreme() / MESSAGE_CHAR_PER_ROW - 1);
			dialogControl.put("Scroll", scroll);
			// ok button
			ButtonRenderer r = new ButtonRenderer();
			r.setTexture2D(textures);
			r.setGameLogicManager(manager);
			r.setAspectRatio(ratio);
			r.setGLResourceManager(vBufMan);
			r.setMVP(FlatMatrix4x4Multiplication(
					dialogMat,
					FlatTranslationMatrix4x4(0, -.9f, 0),
					FlatScalingMatrix4x4(.5f, .1f, 1)), null, null);
			r.setRenderReady();
			r.setTapEnabled(true);
			r.setTapRespondName("Cancel");
			t = new TextRenderer();
			t.setGLResourceManager(vBufMan);
			t.setAspectRatio(ratio);
			t.setGraphicsResponder(responder);
			t.setTextColour(new float[]{0,0,0,0});
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setRenderReady();
			t.setMVP(FlatMatrix4x4Multiplication(
					dialogMat,
					FlatTranslationMatrix4x4(-.25f, -.8f, 0),
					FlatScalingMatrix4x4(.5f / 5, .5f / 5, 1)), null, null);
			t.setText("  OK  ");
			dialogControl.put("OK", r);
			dialogControl.put("OKLabel", t);
			hasDialog = true;
			break;
		}
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
			final Map<String, Item> itemInShop = manager.getCurrentGameState().getItemsInShop();
			int column = 0, row = 0;
			final float leftCellOffset = (2 - CELL_MARGIN) / CELLS_PER_ROW,
					cellWidth = leftCellOffset - CELL_MARGIN;
			ScrollRenderer scroll = new ScrollRenderer();
			scroll.setGLResourceManager(vBufMan);
			scroll.setGameLogicManager(manager);
			scroll.setTexture2D(textures);
			scroll.setGraphicsResponder(responder);
			scroll.setMVP(FlatMatrix4x4Multiplication(dialogMat,FlatTranslationMatrix4x4(0, .1f, 0),FlatScalingMatrix4x4(1, .9f, 1)), null, null);
			ButtonRenderer r;
			TextRenderer t;
			for (String name : itemInShop.keySet()) {
				if (column == CELLS_PER_ROW) {
					column = 0; row ++;
				}
				final Map<String, Object> respondData = new HashMap<>();
				respondData.put("Action", "BuyItem");
				respondData.put("Item", name);
				r = new ButtonRenderer();
				r.setTexture2D(textures);
				r.setAspectRatio(ratio);
				r.setGLResourceManager(vBufMan);
				r.setGameLogicManager(manager);
				r.setRenderReady();
				r.setTapEnabled(manager.getCurrentGameState().areActionPossible(Collections.singletonMap("GameAction", respondData)).get("GameAction"));
				r.setTapRespondName("GameAction");
				r.setTapRespondData(respondData);
				r.setLongPressEnabled(true);
				r.setLongPressRespondName("RequestItem");
				r.setLongPressRespondData(Collections.singletonMap("Item", (Object) name));
				r.setRenderReady();
				scroll.setRenderer("Item-" + name, r, FlatMatrix4x4Multiplication(
						FlatTranslationMatrix4x4(
								(CELL_MARGIN + cellWidth) * column + CELL_MARGIN + cellWidth / 2 - 1,
								1 - ((CELL_MARGIN + cellWidth) * row + CELL_MARGIN + cellWidth / 2),
								0),
						FlatScalingMatrix4x4(cellWidth / 2, cellWidth / 2, 1)));
				t = new TextRenderer();
				t.setTexture2D(textures);
				t.setGLResourceManager(vBufMan);
				t.setAspectRatio(ratio);
				t.setGraphicsResponder(responder);
				t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
				t.setTextColour(new float[]{-1,-1,1,0});
				t.setRenderReady();
				t.setText(itemInShop.get(name).getItemName());
				t.setYAligned(true);
				scroll.setRenderer("ItemLabel-" + name, t,
						FlatMatrix4x4Multiplication(
								FlatTranslationMatrix4x4(
										(CELL_MARGIN + cellWidth) * column + CELL_MARGIN - 1,
										1 - ((CELL_MARGIN + cellWidth) * (row + 1) + CELL_MARGIN * .1f),
										0),
								FlatScalingMatrix4x4(.8f * CELL_MARGIN, .8f * CELL_MARGIN, 1)));
				column++;
			}
			scroll.setScrollLimit(0f, 0f, 0f, (row + 1) * (CELL_MARGIN + cellWidth) + CELL_MARGIN);
			dialogControl.put("Scroll", scroll);
			// cancel button
			r = new ButtonRenderer();
			r.setTexture2D(textures);
			r.setGameLogicManager(manager);
			r.setAspectRatio(ratio);
			r.setGLResourceManager(vBufMan);
			r.setMVP(FlatMatrix4x4Multiplication(
					dialogMat,
					FlatTranslationMatrix4x4(0, -.9f, 0),
					FlatScalingMatrix4x4(.5f, .1f, 1)), null, null);
			r.setRenderReady();
			r.setTapEnabled(true);
			r.setTapRespondName("Cancel");
			t = new TextRenderer();
			t.setGLResourceManager(vBufMan);
			t.setAspectRatio(ratio);
			t.setGraphicsResponder(responder);
			t.setTextColour(new float[]{0,0,0,0});
			t.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
			t.setRenderReady();
			t.setMVP(FlatMatrix4x4Multiplication(
					dialogMat,
					FlatTranslationMatrix4x4(-.25f, -.8f, 0),
					FlatScalingMatrix4x4(.5f / 5, .5f / 5, 1)), null, null);
			t.setText("Cancel");
			dialogControl.put("Cancel", r);
			dialogControl.put("CancelLabel", t);
			hasDialog = true;
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
		boolean success = grid.getReadyState() && status.getReadyState() && log.getReadyState();
		for (Renderer r : dialogControl.values())
			success &= r.getReadyState();
		return success;
	}

	@Override
	public void draw() {
		grid.draw();
		status.draw();
		if (hasDialog)
			drawDialog();
		log.draw();
		if (manager.getLongProcessingEventState() ||
				!manager.getCurrentGameState().getCurrentCharacterName().equals(manager.getCurrentGameState().getPlayerCharacterName()))
			drawWait();
	}
	
	private void drawDialog() {
		// draw background
		final int
			vPosition = glGetAttribLocation(dialogProgram.getProgramId(), "vPosition"),
			textureCoord = glGetAttribLocation(dialogProgram.getProgramId(), "textureCoord"),
			textureLocation = glGetUniformLocation(dialogProgram.getProgramId(), "texture"),
			mModel = glGetUniformLocation(dialogProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(dialogProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(dialogProgram.getProgramId(), "projection"),
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTextureYInverted"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glUseProgram(dialogProgram.getProgramId());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get("DialogBackground").getTexture());
		glUniform1i(textureLocation, 0);
		glUniformMatrix4fv(mModel, 1, false, dialogMat, 0);
		glUniformMatrix4fv(mView, 1, false, IdentityMatrix4x4(), 0);
		glUniformMatrix4fv(mProjection, 1, false, IdentityMatrix4x4(), 0);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(textureCoord);
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
		for (Renderer r : dialogControl.values())
			r.draw();
	}

	private void drawWait() {
		final int
			vPosition = glGetAttribLocation(dialogProgram.getProgramId(), "vPosition"),
			textureCoord = glGetAttribLocation(dialogProgram.getProgramId(), "textureCoord"),
			textureLocation = glGetUniformLocation(dialogProgram.getProgramId(), "texture"),
			mModel = glGetUniformLocation(dialogProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(dialogProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(dialogProgram.getProgramId(), "projection"),
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTextureYInverted"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glUseProgram(dialogProgram.getProgramId());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get("DialogBackground").getTexture());
		glUniform1i(textureLocation, 0);
		glUniformMatrix4fv(mModel, 1, false, waitPromptMat, 0);
		glUniformMatrix4fv(mView, 1, false, IdentityMatrix4x4(), 0);
		glUniformMatrix4fv(mProjection, 1, false, IdentityMatrix4x4(), 0);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(textureCoord);
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
		// configure waitLabel
		if (state.getPlayerCharacterName().equals(state.getCurrentCharacterName()))
			waitLabel.setText("Processing...");
		else
			waitLabel.setTexts("Wait for your turn,", state.getCurrentCharacterName(),"is playing");
		if (landscape)
			waitLabel.setMVP(
					FlatMatrix4x4Multiplication(
							FlatTranslationMatrix4x4(-.3f, .3f * waitLabel.getYExtreme() / waitLabel.getXExtreme() * ratio, -1),
							FlatScalingMatrix4x4(.6f / waitLabel.getXExtreme(), .6f / waitLabel.getXExtreme() * ratio, 1)), null, null);
		else
			waitLabel.setMVP(
					FlatMatrix4x4Multiplication(
							FlatTranslationMatrix4x4(-.3f / ratio, .3f * waitLabel.getYExtreme() / waitLabel.getXExtreme(), -1),
							FlatScalingMatrix4x4(.6f / waitLabel.getXExtreme() / ratio, .6f / waitLabel.getXExtreme(), 1)), null, null);
		waitLabel.draw();
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(40);
					responder.updateGraphics();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public boolean passEvent(ControlEvent e) {
		if (eventCapturer == null) {
			eventCapturer = status;
			if (eventCapturer.passEvent(e))
				return true;
			if (hasDialog)
				for (Renderer r : dialogControl.values()) {
					eventCapturer = r;
					if (eventCapturer.passEvent(e))
						return true;
				}
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
		dialogProgram.close();
	}

	@Override
	public void onTransferToView(Map<String, Object> configuration) {
	}

}
