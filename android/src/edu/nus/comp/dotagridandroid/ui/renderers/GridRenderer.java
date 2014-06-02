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
	private MainRenderer responder;
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
	// owned resources
	private GenericProgram gridProgram, mapProgram;
	private float[]
			model = IdentityMatrix4x4(),
			view = IdentityMatrix4x4(),
			projection = IdentityMatrix4x4(),
			mvpCache;
	private float[] selectGridMat;
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
	public GridRenderer (VertexBufferManager bufMan, int rows, int columns) {
		vBufMan = bufMan;
		this.rows = rows;
		this.columns = columns;
		float[] v = new float[4 * 2 * (rows + columns)]; // changed
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
		vBufMan.setVertexBuffer("GridPointBuffer", v);
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
		vBufMan.setIndexBuffer("GridPointMeshIndex", idx);
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
		glBindTexture(GL_TEXTURE_2D, textures.get("GridMapBackground").getTexture());
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
	private void composeMVP() {
		if (mvpDirty) {
			mvpCache = FlatMatrix4x4Multiplication(projection, view, model);
			mvpDirty = false;
		}
	}
	@Override
	public void draw() {
		composeMVP();
		drawMap();
		drawGrid();
		drawRay();
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {this.textures = textures;}
	
	// coordinate calculations
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
		responder.updateGraphics();
	}
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
	// game logic
	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}
	// event intepretors
	@Override
	public boolean passEvent(ControlEvent e) {
		// Remember: normalise
		if (e.type == ControlEvent.TYPE_CLICK) {
			// unknown just yet
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
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (Math.abs(e.data.deltaX) < ControlEvent.TAP_DRIFT_LIMIT / ratio && Math.abs(e.data.deltaY) < ControlEvent.TAP_DRIFT_LIMIT * ratio) {
				// tap
				if (e.data.wasMultiTouch) {
					// DO NOTHING
				} else if (e.data.eventTime - e.data.startTime > ControlEvent.TAP_LONG_TIME_LIMIT) {
//					System.out.println("Long tap");	// long tap
				} else {
//					System.out.println("Short tap");	// short tap
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
		}
		return true;
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
//		System.out.println("Angle="+perspectiveRotationAngle);
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
	}
	@Override
	public void setGraphicsResponder(MainRenderer mainRenderer) {
		// TODO Auto-generated method stub
		responder = mainRenderer;
	}
}
