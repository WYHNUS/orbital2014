package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static android.opengl.GLES20.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	public static final float BASE_ZOOM_FACTOR = 0.5f;
	
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private int rows, columns;
	private GameLogicManager manager;
	// configurations
	// aspect ratio - width : height
	private float ratio;
	// camera - x, y, z, lookX, lookY, lookZ, near, far
	private final float[] cameraParams = new float[] {
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
	// debug
	private boolean hasRay = false;
	// gesture states
	private boolean processingTranslation = false, processingPerspective = false;
	private boolean mvpDirty = true;
	// translation
	private final float[] translationDeltaCoord = new float[]{-1,-1};
	// perspective
	private final float[] perspectiveStartCoord = new float[]{-1,-1,-1,-1};
	private final float[] perspectiveLastCoord = new float[]{0,0,0,0};
	private float perspectiveStartDeltaVecX, perspectiveStartDeltaVecY, perspectiveRotationAngle, perspectiveCameraZoom;
	// for tap monitoring - calculate intervals between double taps
	private final long[] tapTimeQueue = new long[]{0,0,0};
	private final float[] tapXQueue = new float[]{0,0,0}, tapYQueue = new float[]{0,0,0};
	private final byte tapQueueLength = 3;
	private byte tapQueueHead = 0;
	private boolean doubleTap = false;
//	private final float[] 
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
		// calculate model
		calculateModel();
//		model = FlatMatrix4x4Multiplication (FlatRotationMatrix4x4(-1f,0,0,1), model);
		// board IS at the origin so not need translation
		// calculate view
		calculateView();
	}
	// draw functions
	private void drawGrid(float[] mat) {
		int vOffset = vBufMan.getVertexBufferOffset("GridPointBuffer"),
			iOffset = vBufMan.getIndexBufferOffset("GridPointMeshIndex");
		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
			mMVP = glGetUniformLocation(gridProgram.getProgramId(), "mMVP"),
			vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor");
		glUseProgram(gridProgram.getProgramId());
		glUniformMatrix4fv(mMVP, 1, false, mat, 0);
		glUniform4f(vColor, 0, 1, 0, 1);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_LINES, 2 * (columns + rows + 2), GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
	}
	private void drawMap(float[] mat) {
		int vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTexture"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		int vPosition = glGetAttribLocation(mapProgram.getProgramId(), "vPosition"),
			mMVP = glGetUniformLocation(mapProgram.getProgramId(), "mMVP"),
			textureColorTone = glGetUniformLocation(mapProgram.getProgramId(), "textureColorTone"),
			textureLocation = glGetUniformLocation(mapProgram.getProgramId(), "texture"),
			textureCoord = glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
		glUseProgram(mapProgram.getProgramId());
		glUniformMatrix4fv(mMVP, 1, false, mat, 0);
		glUniform4f(textureColorTone, 0f, 0, 0, 0f);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get("GridMapBackground").getTexture());
		glUniform1i(textureLocation, 0);
		glEnableVertexAttribArray(textureCoord);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_TRIANGLE_STRIP, 6, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
	}
	private void drawRay(float[] mat) {
//		if (!hasRay)
//			return;
//		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
//				mMVP = glGetUniformLocation(gridProgram.getProgramId(), "mMVP"),
//				vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor");
//		float[] v = new float[]{
//				1,-1,0,1,
//				1,1,-.3f,1,
//				-1,-1,0,1,
//				-1,1,-.3f,1
//		};
//		glUseProgram(gridProgram.getProgramId());
//		glUniformMatrix4fv(mMVP, 1, false, mat, 0);
//		glUniform4f(vColor, 1, 0, 0, 1);
//		FloatBuffer fb = BufferUtils.createFloatBuffer(16).put(v);
//		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, fb.position(0));
//		glEnableVertexAttribArray(vPosition);
//		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//		glDisableVertexAttribArray(vPosition);
	}
	@Override
	public void draw() {
		if (mvpDirty) {
			mvpCache = FlatMatrix4x4Multiplication(projection, view, model);
			mvpDirty = false;
		}
		drawGrid(mvpCache);
		drawMap(mvpCache);
//		drawRay(mat);
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {this.textures = textures;}
	@Override
	public void setAspectRatio(float ratio) {
		// model parameter
		this.ratio = ratio;
		mvpDirty = true;
		final float lens_radius = cameraParams[9] * (float) Math.tan(cameraParams[11] / 2);
		if (ratio > 1)
			projection = FlatPerspectiveMatrix4x4(cameraParams[9], cameraParams[10], -lens_radius, lens_radius, lens_radius / ratio, -lens_radius / ratio);
		else
			projection = FlatPerspectiveMatrix4x4(cameraParams[9], cameraParams[10], -lens_radius * ratio, lens_radius * ratio, lens_radius, -lens_radius);
	}
	private void calculateModel() {
		mvpDirty = true;
		if (rows > columns)
			model = FlatScalingMatrix4x4((float) columns / rows * BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR);
		else if (rows < columns)
			model = FlatScalingMatrix4x4(BASE_ZOOM_FACTOR, (float) rows / columns * BASE_ZOOM_FACTOR, BASE_ZOOM_FACTOR);
		// set map rotation
		if (processingPerspective)
			model = FlatMatrix4x4Multiplication(FlatRotationMatrix4x4(mapRotation + perspectiveRotationAngle, 0, 0, 1),model);
		else
			model = FlatMatrix4x4Multiplication(FlatRotationMatrix4x4(mapRotation, 0, 0, 1),model);
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
		if (processingTranslation)
			lookVec = NormalisedVector3 (
					new float[]{cameraParams[3]-cameraParams[0], cameraParams[4]-cameraParams[1], cameraParams[5]-cameraParams[2]});
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
	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}
	@Override
	public void passEvent(ControlEvent e) {
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
//								System.out.println("DoubleTapWaiting");
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
					} else {
//						System.out.println("Double Tap Recognized");
						doubleTap = true;
					}
					tapQueueHead = (byte) ((tapQueueHead + 1) % tapQueueLength);
					tapTimeQueue[tapQueueHead] = e.data.eventTime;
					tapXQueue[tapQueueHead] = e.data.x[0]; tapYQueue[tapQueueHead] = e.data.y[0];
				}
			}
			onProcessingTranslationDone(e);
			onProcessingPerspectiveDone(e);
			onProcessingAttackAngleDone(e);
		}
	}
	private void onDoubleTap(ControlEvent e) {
//		System.out.println("Double Tap");
		// reset camera
		cameraParams[0] = cameraParams[1] = cameraParams[3] = cameraParams[4] = cameraParams[6] = cameraParams[8] = 0;
		cameraParams[2] = .8f;
		cameraParams[5] = cameraParams[9] = .01f;
		cameraParams[10] = 5;
		cameraParams[11] = PI / 3f;
		mapRotation = 0;
		calculateModel();
		calculateView();
	}
	private void onSingleTap(ControlEvent e) {
//		System.out.println("Single Tap");
		rayTrace(e);
	}
	private void onProcessingAttackAngleDone(ControlEvent e) {
		
	}
	private void onProcessingPerspectiveDone(ControlEvent e) {
		if (processingPerspective) {
			// finalise
			cameraParams[2] += perspectiveCameraZoom;
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
		perspectiveRotationAngle = (float) Math.acos(perspectiveStartDeltaVecX * perspectiveLastDeltaVecX + perspectiveStartDeltaVecY * perspectiveLastDeltaVecY);
		if (perspectiveStartDeltaVecX * perspectiveLastDeltaVecY - perspectiveStartDeltaVecY * perspectiveLastDeltaVecX < 0)
			perspectiveRotationAngle = -perspectiveRotationAngle;
		final float lenFirst = (float) Math.hypot(perspectiveStartCoord[2] - perspectiveStartCoord[0], perspectiveStartCoord[3] - perspectiveStartCoord[1]);
		final float zoomDelta = lenFirst - lenLast;
		// TODO: Camera Parameter Calibration
		if (zoomDelta + cameraParams[2] < 0.2f)
			perspectiveCameraZoom = 0.2f - cameraParams[2];
		else if (zoomDelta + cameraParams[2] > 4f)
			perspectiveCameraZoom = 4f - cameraParams[2];
		else
			perspectiveCameraZoom = zoomDelta;
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
		final float x = e.data.x[0], y = e.data.y[0];
	}
	@Override
	public void close() {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
	}
}
