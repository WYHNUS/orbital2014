package edu.nus.comp.dotagridandroid.ui.renderers;

//import java.nio.*;
import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static android.opengl.GLES20.*;
import static edu.nus.comp.dotagridandroid.math.RenderMathsAccelerated.*;

public class GridRenderer implements Renderer {
	public static final float BOARD_Z_COORD = 0.1f;
	public static final float BASE_ZOOM_FACTOR = 0.5f;
	
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private int rows, columns;
	private GameLogicManager manager;
	private MainRenderer.GraphicsResponder responder;
	private Texture2D mapTexture;
	// configurations
	// aspect ratio - width : height
	private float ratio;
	// camera - x, y, z, lookX, lookY, lookZ, near, far
	private final float[] cameraParams = {
			0, 0, 0.8f,	// 0: posX, 1: posY, 2: posZ
			0, 0, .01f,	// 3: lookX, 4: lookY, 5: lookZ
			0, 1, 0,	// 6: upX, 7: upY, 8: upZ
			.01f, 5, PI / 3f	// 9: near (const), 10: far (const), 11: FoV (in radians, const)
	};
	// map rotation - in radians
	private float mapRotation = 0;
	// possessing resources
	private GenericProgram gridProgram, mapProgram;
	private float[]
			model = IdentityMatrix4x4(),
			view = IdentityMatrix4x4(),
			projection = IdentityMatrix4x4();
	private float[] selectGridMat, gridLines, terrain;
	private final float[] gridTerrain;
	private int[] gridLinesIndex;
	private TextRenderer textRender;
	private NormalGenerator normalGen;
	// gesture states
	private boolean hasRay = false;
	private boolean processingTranslation = false, processingPerspective = false;
	private boolean mvpDirty = true;
	// translation
	private final float[] translationDeltaCoord = {-1,-1};
	// perspective
	private final float[] perspectiveStartCoord = {-1,-1,-1,-1};
	private final float[] perspectiveLastCoord = {0,0,0,0};
	private final float[] perspectiveLookAt = {0,0};
	private float perspectiveStartDeltaVecX, perspectiveStartDeltaVecY, perspectiveRotationAngle, perspectiveCameraZoom;
	// for tap monitoring - calculate intervals between double taps
	private final long[] tapTimeQueue = {0,0,0};
	private final float[] tapXQueue = {0,0,0}, tapYQueue = {0,0,0};
	private final byte tapQueueLength = 3;
	private byte tapQueueHead = 0;
	private boolean doubleTap = false;
	// ray tracing
	private float[] orgGridPoint;
	private int [] orgGridIndex;
	// multithreading
	private Thread computeTask;
	public GridRenderer (int columns, int rows, float[] terrain) {
		gridTerrain = new float[(rows * NormalGenerator.RESOLUTION + 1) * (columns * NormalGenerator.RESOLUTION + 1) * 4];
		this.terrain = terrain;
		this.rows = rows;
		this.columns = columns;
		gridProgram = new GenericProgram(
				new String(CommonShaders.VS_IDENTITY),
				new String(CommonShaders.FS_IDENTITY));
		// configure map
		mapProgram = new GenericProgram (
				new String(CommonShaders.VS_IDENTITY_TEXTURED),
				new String(CommonShaders.FS_IDENTITY_TEXTURED_TONED));
		// coord configurations
		selectGridMat = FlatMatrix4x4Multiplication(FlatScalingMatrix4x4(1f/columns, 1f/rows, 1), FlatTranslationMatrix4x4(1,1,0));
		// calculate model
		calculateModel();
		// board IS at the origin so not need translation
		// calculate view
		calculateView();
		// text test
		textRender = new TextRenderer();
		computeTask = new Thread() {
			@Override
			public void run() {
				prepareGrid();
			}
		};
		computeTask.start();
	}
	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		responder = mainRenderer;
		textRender.setGraphicsResponder(responder);
	}
	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}
	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		vBufMan = manager;
		textRender.setVertexBufferManager(vBufMan);
	};
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
		textRender.setTextFont(new TextFont(textures.get("DefaultTextFontMap")));
	}
	@Override
	public void setAspectRatio(float ratio) {
		// projection parameters - constant for this aspect ratio
		this.ratio = ratio;
		mvpDirty = true;
		final float lens_radius = cameraParams[9] * (float) Math.tan(cameraParams[11] / 2);
		if (ratio > 1)
			projection = FlatPerspectiveMatrix4x4(cameraParams[9], cameraParams[10], -lens_radius, lens_radius, lens_radius / ratio, -lens_radius / ratio);
		else
			projection = FlatPerspectiveMatrix4x4(cameraParams[9], cameraParams[10], -lens_radius * ratio, lens_radius * ratio, lens_radius, -lens_radius);
		textRender.setAspectRatio(ratio);
	}
	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}
	private void prepareGrid() {

		float[] v = new float[4 * 2 * (rows + columns)];
		int c = 0;
		for (int i = 0; i <= columns; i++) {
			v[c++] = 2f / columns * i - 1;
			v[c++] = 1;
			v[c++] = 0;
			v[c++] = 1;
		}
		for (int i = 0; i <= columns; i++) {
			v[c++] = 2f / columns * i -1;
			v[c++] = -1;
			v[c++] = 0;
			v[c++] = 1;
		}
		for (int i = 1; i < rows; i++) {
			v[c++] = -1;
			v[c++] = 2f / rows * i - 1;
			v[c++] = 0;
			v[c++] = 1;
		}
		for (int i = 1; i < rows; i++) {
			v[c++] = 1;
			v[c++] = 2f / rows * i - 1;
			v[c++] = 0;
			v[c++] = 1;
		}
		gridLines = v;
		c = 0;
		int[] idx = new int[2 * (rows + columns + 2)];
		for (int i = 0; i <= columns; i++) {
			idx[c++] = i;
			idx[c++] = columns + i + 1;
		}
		idx[c++] = 0;
		idx[c++] = columns;
		idx[c++] = columns + 1;
		idx[c++] = 2 * columns + 1;
		for (int i = 2; i <= rows; i++) {
			idx[c++] = 2 * columns + i;
			idx[c++] = 2 * columns + i + rows - 1;
		}
		gridLinesIndex = idx;
		final int RESOLUTION = NormalGenerator.RESOLUTION, arrWidth = columns * RESOLUTION + 1;
		// Terrain Intrapolation - Bilinear
		for (int i = 0; i < rows - 1; i++) {
			final int offset = i * arrWidth * RESOLUTION;
			for (int j = 0; j < columns - 1; j++) {
				int cellOffset = offset + j * RESOLUTION + RESOLUTION / 2 * arrWidth + RESOLUTION / 2;
				for (int s = 0; s <= RESOLUTION; s++)
					for (int t = 0; t <= RESOLUTION; t++) {
						gridTerrain[4 * (cellOffset + arrWidth * s + t)] = (j + t / (float) RESOLUTION + .5f) / columns * 2 - 1;
						gridTerrain[4 * (cellOffset + arrWidth * s + t) + 1] = (i + s / (float) RESOLUTION + .5f) / rows * 2 - 1;
						gridTerrain[4 * (cellOffset + arrWidth * s + t) + 2]
								= (terrain[i * columns + j] * (1 - t / (float) RESOLUTION) * (1 - s / (float) RESOLUTION)
								+ terrain[i * columns + j + 1] * t / (float) RESOLUTION * (1 - s / (float) RESOLUTION)
								+ terrain[(i + 1) * columns + j] * (1 - t / (float) RESOLUTION) * s / (float) RESOLUTION
								+ terrain[(i + 1) * columns + j + 1] * t / (float) RESOLUTION * s / (float) RESOLUTION) * BOARD_Z_COORD;
						gridTerrain[4 * (cellOffset + arrWidth * s + t) + 3] = 1;
					}
			}
		}
		// Edges
		for (int s = 0; s <= RESOLUTION / 2; s++)
			for (int t = 0; t <= RESOLUTION / 2; t++) {
				int offset = arrWidth * s + t;
				// bottom left
				gridTerrain[4 * offset] = t / (float) RESOLUTION / columns * 2 - 1;
				gridTerrain[4 * offset + 1] = s / (float) RESOLUTION / rows * 2 - 1;
				gridTerrain[4 * offset + 2] = terrain[0] * BOARD_Z_COORD;
				gridTerrain[4 * offset + 3] = 1;
				// bottom right
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2)]
						= (columns - 1 + t / (float) RESOLUTION + .5f) / columns * 2 - 1;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 1]
						= s / (float) RESOLUTION / rows * 2 - 1;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 2]
						= terrain[columns - 1] * BOARD_Z_COORD;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 3]
						= 1;
				//top left
				offset += (rows - 1) * arrWidth * RESOLUTION + RESOLUTION / 2 * arrWidth;
				gridTerrain[4 * offset] = t / (float) RESOLUTION / columns * 2 - 1;
				gridTerrain[4 * offset + 1] = (rows - 1 + .5f + s / (float) RESOLUTION) / rows * 2 - 1;
				gridTerrain[4 * offset + 2] = terrain[(rows - 1) * columns] * BOARD_Z_COORD;
				gridTerrain[4 * offset + 3] = 1;
				// top right
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2)]
						= (columns - 1 + t / (float) RESOLUTION + .5f) / columns * 2 - 1;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 1]
						= (rows - 1 + .5f + s / (float) RESOLUTION) / rows * 2 - 1;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 2]
						= terrain[rows * columns - 1] * BOARD_Z_COORD;
				gridTerrain[4 * (offset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 3]
						= 1;
			}
		for (int i = 0; i < rows - 1; i++) {
			final int offset = (i * RESOLUTION + RESOLUTION / 2) * arrWidth;
			for (int s = 0; s <= RESOLUTION; s++)
				for (int t = 0; t <= RESOLUTION / 2; t++) {
					final int cellOffset = offset + s * arrWidth + t;
					// left side
					gridTerrain[4 * cellOffset] = t / (float) RESOLUTION / columns * 2 - 1;
					gridTerrain[4 * cellOffset + 1] = (i + .5f + s / (float) RESOLUTION) / rows * 2 - 1;
					gridTerrain[4 * cellOffset + 2]
							= (terrain[i * columns] * (1 - s / (float) RESOLUTION)
							+ terrain[(i + 1) * columns] * s / RESOLUTION) * BOARD_Z_COORD;
					gridTerrain[4 * cellOffset + 3] = 1;
					// right side
					gridTerrain[4 * (cellOffset + (columns - 1) * RESOLUTION + RESOLUTION / 2)]
							= (columns - 1 + .5f + t / (float) RESOLUTION) / columns * 2 - 1;
					gridTerrain[4 * (cellOffset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 1]
							= (i + .5f + s / (float) RESOLUTION) / rows * 2 - 1;
					gridTerrain[4 * (cellOffset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 2]
							= (terrain[i * columns + columns - 1] * (1 - s / (float) RESOLUTION)
							+ terrain[(i + 1) * columns + columns - 1] * s / RESOLUTION) * BOARD_Z_COORD;
					gridTerrain[4 * (cellOffset + (columns - 1) * RESOLUTION + RESOLUTION / 2) + 3]
							= 1;
				}
		}
		for (int i = 0; i < columns - 1; i++) {
			final int offset = i * RESOLUTION + RESOLUTION / 2;
			for (int s = 0; s <= RESOLUTION / 2; s++)
				for (int t = 0; t <= RESOLUTION; t++) {
					final int cellOffset = offset + s * arrWidth + t;
					// bottom side
					gridTerrain[4 * cellOffset] = (i + .5f + t / (float) RESOLUTION) / columns * 2 - 1;
					gridTerrain[4 * cellOffset + 1] = s / (float) RESOLUTION / rows * 2 - 1;
					gridTerrain[4 * cellOffset + 2]
							= (terrain[i] * (1 - t / (float) RESOLUTION)
							+ terrain[i + 1] * t / RESOLUTION) * BOARD_Z_COORD;
					gridTerrain[4 * cellOffset + 3] = 1;
					// top side
					gridTerrain[4 * (cellOffset + (rows - 1) * RESOLUTION * arrWidth + RESOLUTION * arrWidth / 2)]
							= (i + .5f + t / (float) RESOLUTION) / columns * 2 - 1;
					gridTerrain[4 * (cellOffset + (rows - 1) * RESOLUTION * arrWidth + RESOLUTION * arrWidth / 2) + 1]
							= (rows - 1 + .5f + s / (float) RESOLUTION) / rows * 2 - 1;
					gridTerrain[4 * (cellOffset + (rows - 1) * RESOLUTION * arrWidth + RESOLUTION * arrWidth / 2) + 2]
							= (terrain[(rows - 1) * columns + i] * (1 - t / (float) RESOLUTION)
							+ terrain[(rows - 1) * columns + i + 1] * t / RESOLUTION) * BOARD_Z_COORD;
					gridTerrain[4 * (cellOffset + (rows - 1) * RESOLUTION * arrWidth + RESOLUTION * arrWidth / 2) + 3]
							= 1;
				}
		}
	}
	@Override
	public void setRenderReady() {
		try {computeTask.join();} catch (InterruptedException e) {e.printStackTrace();}
		vBufMan.setVertexBuffer("GridPointBuffer", gridLines);
		vBufMan.setIndexBuffer("GridPointMeshIndex", gridLinesIndex);
		gridLines = null; gridLinesIndex = null;
		textRender.setRenderReady();
		textRender.setText("DOTA-GRID MOBILE (ANDROID) by C-DOTA");
		textRender.setMVP(FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0, 1, 0),FlatScalingMatrix4x4(0.05f/ratio,0.05f,1)), null, null);
		mapTexture = textures.get("GridMapBackground");
		normalGen = new NormalGenerator(columns, rows, gridTerrain, mapTexture.getWidth(), mapTexture.getHeight());
		normalGen.setGraphicsResponder(responder);
		normalGen.setRenderReady();
	}
	@Override
	public boolean getReadyState() {
		try {
			computeTask.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	// draw functions
	private void drawGrid() {
		int vOffset = vBufMan.getVertexBufferOffset("GridPointBuffer"),
			iOffset = vBufMan.getIndexBufferOffset("GridPointMeshIndex");
		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
			mModel = glGetUniformLocation(gridProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(gridProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(gridProgram.getProgramId(), "projection"),
			vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor");
		glUseProgram(gridProgram.getProgramId());
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glUniform4f(vColor, 1, 0, 0, 1);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_LINES, 2 * (columns + rows + 2), GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
	}
	private void drawMap() {
		float[] mat = FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(0,0,-BOARD_Z_COORD));
		int vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTexture"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		int vPosition = glGetAttribLocation(mapProgram.getProgramId(), "vPosition"),
			mModel = glGetUniformLocation(gridProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(gridProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(gridProgram.getProgramId(), "projection"),
			textureColorTone = glGetUniformLocation(mapProgram.getProgramId(), "textureColorTone"),
			textureLocation = glGetUniformLocation(mapProgram.getProgramId(), "texture"),
			textureCoord = glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
		glUseProgram(mapProgram.getProgramId());
		glUniformMatrix4fv(mModel, 1, false, mat, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glUniform4f(textureColorTone, .5f, 0, 0, 0);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, normalGen.getNormalTexture());
//		glBindTexture(GL_TEXTURE_2D, mapTexture.getTexture());
		glUniform1i(textureLocation, 0);
		glEnableVertexAttribArray(textureCoord);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
	}
	private void drawRay() {
		if (!hasRay)
			return;
		float[] mat = FlatMatrix4x4Multiplication(model, FlatTranslationMatrix4x4(2f/columns * orgGridIndex[0]-1, 2f/rows * orgGridIndex[1]-1, 0));
		mat = FlatMatrix4x4Multiplication(mat, selectGridMat);
		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
				mModel = glGetUniformLocation(gridProgram.getProgramId(), "model"),
				mView = glGetUniformLocation(gridProgram.getProgramId(), "view"),
				mProjection = glGetUniformLocation(gridProgram.getProgramId(), "projection"),
				vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor"),
				vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
				iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glUseProgram(gridProgram.getProgramId());
		glUniformMatrix4fv(mModel, 1, false, mat, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glUniform4f(vColor, 1, 0, 0, 0.2f);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glEnableVertexAttribArray(vPosition);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
	}
	@Override
	public void draw() {
		drawMap();
		drawGrid();
		drawRay();
//		if (textRender != null) {
//		textRender.draw();
//		}
	}
	// coordinate calculations
	private void calculateModel() {
		mvpDirty = true;
		if (rows > columns)
			model = FlatScalingMatrix4x4((float) columns / rows * BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR);
		else if (rows < columns)
			model = FlatScalingMatrix4x4(BASE_ZOOM_FACTOR, (float) rows / columns * BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR);
		else
			model = IdentityMatrix4x4();
		// set map rotation
		if (processingPerspective)
			model = FlatMatrix4x4Multiplication(FlatRotationMatrix4x4(mapRotation + perspectiveRotationAngle, 0, 0, 1),model);
		else
			model = FlatMatrix4x4Multiplication(FlatRotationMatrix4x4(mapRotation, 0, 0, 1),model);
		model = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0,0,BOARD_Z_COORD), model);
	}
	private void calculateView () {
		mvpDirty = true;
		if (processingTranslation)
			view = FlatTranslationMatrix4x4(-cameraParams[0]-translationDeltaCoord[0],-cameraParams[1]-translationDeltaCoord[1],-cameraParams[2]);
		else
			view = FlatTranslationMatrix4x4(-cameraParams[0],-cameraParams[1],-cameraParams[2]);
		if (processingPerspective) {
			view = FlatMatrix4x4Multiplication(FlatTranslationMatrix4x4(0,0,-perspectiveCameraZoom),view);
		}
		// TODO: changing angle of attack
		float[] lookVec;
		if (processingPerspective)
			lookVec = NormalisedVector3 (
					new float[]{cameraParams[3]-cameraParams[0]+perspectiveLookAt[0], cameraParams[4]-cameraParams[1]+perspectiveLookAt[1], cameraParams[5]-cameraParams[2]-perspectiveCameraZoom});
		else
			lookVec = NormalisedVector3 (
					new float[]{cameraParams[3]-cameraParams[0], cameraParams[4]-cameraParams[1], cameraParams[5]-cameraParams[2]});
		view = FlatMatrix4x4Multiplication(
				FlatRotationMatrix4x4((float) Math.acos(-lookVec[2]), -lookVec[1], lookVec[0], 0)
				,view);
		view = FlatMatrix4x4Multiplication(
				FlatRotationMatrix4x4((float) Math.acos(cameraParams[7]), -cameraParams[8], 0, cameraParams[6])
				,view);
	}
	// event intepretors
	@Override
	public boolean passEvent(ControlEvent e) {
		// Remember: normalise
		if (e.type == ControlEvent.TYPE_DOWN) {
			// unknown just yet
			return true;
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			// tap with movements
			if (e.data.eventTime - e.data.startTime > ControlEvent.DRAG_TIME_LIMIT ||
					Math.abs(e.data.deltaX) > ControlEvent.TAP_DRIFT_LIMIT / ratio ||
					Math.abs(e.data.deltaY) > ControlEvent.TAP_DRIFT_LIMIT * ratio) {
				if (e.data.pointerCount == 1)
					onProcessingTranslation(e);
				else if (e.data.pointerCount == 2)
					onProcessingPerspective(e);
				else if (e.data.pointerCount == 3)
					onProcessingAttackAngle(e);
				responder.updateGraphics();
			}
			return true;
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (Math.abs(e.data.deltaX) < ControlEvent.TAP_DRIFT_LIMIT / ratio && Math.abs(e.data.deltaY) < ControlEvent.TAP_DRIFT_LIMIT * ratio) {
				// tap
				if (e.data.wasMultiTouch) {
					// DO NOTHING
				} else if (e.data.eventTime - e.data.startTime > ControlEvent.TAP_LONG_TIME_LIMIT) {
					// long tap
				} else {
					// short tap
					if (e.data.eventTime - tapTimeQueue[tapQueueHead] > ControlEvent.TAP_DOUBLE_TIME_LIMIT ||
							Math.abs(tapXQueue[tapQueueHead] - e.data.x[0]) > ControlEvent.TAP_DOUBLE_DRIFT_LIMIT / ratio ||
							Math.abs(tapYQueue[tapQueueHead] - e.data.y[0]) > ControlEvent.TAP_DOUBLE_DRIFT_LIMIT * ratio) {
						final ControlEvent passedEvt = new ControlEvent(e);
						new Thread() {
							private ControlEvent evt;
							// <init>
							{
								evt = passedEvt;
							}
							@Override
							public void run() {
								try{
									Thread.sleep(ControlEvent.TAP_DOUBLE_TIME_LIMIT);
								} catch (Exception e) {}
								if (doubleTap) {
									doubleTap = false;
									onDoubleTap(evt);
								} else
									onSingleTap(evt);
							}
						}.start();
					} else
						doubleTap = true;
					tapQueueHead = (byte) ((tapQueueHead + 1) % tapQueueLength);
					tapTimeQueue[tapQueueHead] = e.data.eventTime;
					tapXQueue[tapQueueHead] = e.data.x[0]; tapYQueue[tapQueueHead] = e.data.y[0];
				}
			}
			onProcessingTranslationDone(e);
			onProcessingPerspectiveDone(e);
			onProcessingAttackAngleDone(e);
			responder.updateGraphics();
			return e.data.pointerCount > 1;
		}
		return false;
	}
	private void onDoubleTap(ControlEvent e) {
		// reset camera
		cameraParams[0] = cameraParams[1] = cameraParams[3] = cameraParams[4] = cameraParams[6] = cameraParams[8] = 0;
		cameraParams[2] = .8f;
		cameraParams[5] = cameraParams[9] = .01f;
		cameraParams[10] = 5;
		cameraParams[11] = PI / 3f;
		mapRotation = 0;
		calculateModel();
		calculateView();
		hasRay = false;
		responder.updateGraphics();
	}
	private void onSingleTap(ControlEvent e) {
		rayTrace(e);
	}
	private void onProcessingAttackAngleDone(ControlEvent e) {
		
	}
	private void onProcessingPerspectiveDone(ControlEvent e) {
		if (processingPerspective) {
			// finalise
			cameraParams[2] += perspectiveCameraZoom;
			cameraParams[3] += perspectiveLookAt[0];
			cameraParams[4] += perspectiveLookAt[1];
			mapRotation += perspectiveRotationAngle;
			processingPerspective = false;
			calculateModel();
			calculateView();
		}
	}
	private void onProcessingTranslationDone(ControlEvent e) {
		if (processingTranslation) {
			cameraParams[0] -= e.data.deltaX;
			cameraParams[1] -= e.data.deltaY;
			cameraParams[3] -= e.data.deltaX;
			cameraParams[4] -= e.data.deltaY;
			processingTranslation = false;
			calculateView();
		}
	}
	private void onProcessingAttackAngle(ControlEvent e) {
		
	}
	private void onProcessingPerspective(ControlEvent e) {
		if (e.data.pointerCount != 2)
			return;
		if (processingTranslation)
			onProcessingTranslationDone(e);
		if (!processingPerspective) {
			perspectiveStartCoord[0] = e.data.x[0];
			perspectiveStartCoord[1] = e.data.y[0];
			perspectiveStartCoord[2] = e.data.x[1];
			perspectiveStartCoord[3] = e.data.y[1];
			final float deltaX = perspectiveStartCoord[2] - perspectiveStartCoord[0],
					deltaY = perspectiveStartCoord[3] - perspectiveStartCoord[1],
					len = (float) Math.hypot(deltaX, deltaY);
			perspectiveStartDeltaVecX = deltaX / len;
			perspectiveStartDeltaVecY = deltaY / len;
			processingPerspective = true;
		}
		perspectiveLastCoord[0] = e.data.x[0];
		perspectiveLastCoord[1] = e.data.y[0];
		perspectiveLastCoord[2] = e.data.x[1];
		perspectiveLastCoord[3] = e.data.y[1];
		final float deltaX = perspectiveLastCoord[2] - perspectiveLastCoord[0],
				deltaY = perspectiveLastCoord[3] - perspectiveLastCoord[1],
				lenLast = (float) Math.hypot(deltaX, deltaY);
		final float perspectiveLastDeltaVecX = deltaX / lenLast,
				perspectiveLastDeltaVecY = deltaY / lenLast;
		// map rotation
		perspectiveRotationAngle = (float) Math.acos(perspectiveStartDeltaVecX * perspectiveLastDeltaVecX + perspectiveStartDeltaVecY * perspectiveLastDeltaVecY);
		if (perspectiveStartDeltaVecX * perspectiveLastDeltaVecY - perspectiveStartDeltaVecY * perspectiveLastDeltaVecX < 0)
			perspectiveRotationAngle = -perspectiveRotationAngle;
		// zoom factor
		final float lenFirst = (float) Math.hypot(perspectiveStartCoord[2] - perspectiveStartCoord[0], perspectiveStartCoord[3] - perspectiveStartCoord[1]);
		final float zoomDelta = lenFirst - lenLast;
		// TODO: Camera Parameter Calibration
		if (zoomDelta + cameraParams[2] < 0.2f)
			perspectiveCameraZoom = 0.2f - cameraParams[2];
		else if (zoomDelta + cameraParams[2] > 4f)
			perspectiveCameraZoom = 4f - cameraParams[2];
		else
			perspectiveCameraZoom = zoomDelta;
		// look-at displacement
		perspectiveLookAt[0] = Math.scalb(perspectiveStartCoord[0] + perspectiveStartCoord[2] - perspectiveLastCoord[0] - perspectiveLastCoord[2], -1);
		perspectiveLookAt[1] = Math.scalb(perspectiveStartCoord[1] + perspectiveStartCoord[3] - perspectiveLastCoord[1] - perspectiveLastCoord[3], -1);
		calculateModel();
		calculateView();
	}
	private void onProcessingTranslation(ControlEvent e) {
		if (e.data.pointerCount != 1)
			return;
		if (processingPerspective)
			onProcessingPerspectiveDone(e);
		if (!processingTranslation) {
			processingTranslation = true;
		}
		translationDeltaCoord[0] = -e.data.deltaX;
		translationDeltaCoord[1] = -e.data.deltaY;
		calculateView();
	}
	// ray trace code
	private void rayTrace(ControlEvent e) {
		// Note: start with normalized device coordinates
		calculateModel();
		calculateView();
		final float[] projPoint = {e.data.x[0] * cameraParams[9], e.data.y[0] * cameraParams[9], -cameraParams[9], cameraParams[9]};
		final float[] orgProjectedPoint = FlatMatrix4x4Vector4Multiplication (FlatInverseMatrix4x4(projection), projPoint);
		final float[] worldCoord = FlatMatrix4x4Vector4Multiplication(FlatInverseMatrix4x4(view), orgProjectedPoint);
		final float lambda = (BOARD_Z_COORD - worldCoord[2]) / (cameraParams[2] - worldCoord[2]);
		if (Math.abs(lambda) > ERROR)
			orgGridPoint = new float[] {
				lambda * cameraParams[0] + (1-lambda) * worldCoord[0],
				lambda * cameraParams[1] + (1-lambda) * worldCoord[1],
				BOARD_Z_COORD, 1
			};
		else
			orgGridPoint = new float[] {worldCoord[0], worldCoord[1], BOARD_Z_COORD, 1};
		orgGridPoint = FlatMatrix4x4Vector4Multiplication(FlatInverseMatrix4x4(model), orgGridPoint);
		// report coordinate
		orgGridIndex = new int[]{
				(int) Math.floor(Math.scalb(orgGridPoint[0] + 1, -1) * columns),
				(int) Math.floor(Math.scalb(orgGridPoint[1] + 1, -1) * rows)
				};
		hasRay = (orgGridIndex[0] >= 0 && orgGridIndex[0] < columns && orgGridIndex[1] >= 0 && orgGridIndex[1] < rows);
		responder.updateGraphics();
	}
	@Override
	public void close() {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
		textRender.close();
	}
}
