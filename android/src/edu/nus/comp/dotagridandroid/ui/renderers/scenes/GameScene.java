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
	
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private GameState state;
	private float ratio;
	private Map<String, Texture2D> textures;
	private GLResourceManager vBufMan;
	// resources
	private Renderer grid, status, eventCapturer;
	private final Map<String, Renderer> dialogControl = new LinkedHashMap<>();	// IMPORTANT: necessary to maintain order of drawing
	private float[] dialogMat;
	private GenericProgram dialogProgram;
	private boolean landscape;
	private boolean hasDialog;
	
	public GameScene () {
		dialogProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED);
		
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
	}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {
		hasDialog = false;
		if (updates.containsKey("Dialog"))
			prepareDialog((String) updates.get("Dialog"));
		grid.notifyUpdate(updates);
		status.notifyUpdate(updates);
	}
	
	private void prepareDialog(String dialogType) {
		hasDialog = false;
		for (Renderer r : dialogControl.values())
			r.close();
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
		boolean success = grid.getReadyState() && status.getReadyState();
		for (Renderer r : dialogControl.values())
			success &= r.getReadyState();
		return success;
	}

	@Override
	public void draw() {
		grid.draw();
		status.draw();
		drawDialog();
	}
	
	private void drawDialog() {
		// draw background
		if (hasDialog) {
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
		if (manager != null)
			manager.getCurrentGameState().close();
		dialogProgram.close();
	}

	@Override
	public SceneConfiguration onTransferToView() {
		return null;
	}

}
