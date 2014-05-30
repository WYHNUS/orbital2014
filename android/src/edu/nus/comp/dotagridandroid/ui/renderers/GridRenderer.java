package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import android.renderscript.*;
import static android.opengl.GLES20.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private int rows, columns;
	private GameLogicManager manager;
	private float ratio;
	// camera - x, y, z, lookX, lookY, lookZ, near, far
	private float[] cameraParams = new float[] {
			0, 0, 1,
			0, 0, 0,
			0.2f, 1f
	};
	// owned resources
	private GenericProgram gridProgram, mapProgram;
	private float[]
			model = IdentityMatrix4x4(),
			view = IdentityMatrix4x4(),
			processingView = IdentityMatrix4x4(),
			projection = IdentityMatrix4x4(),
			processingProjection = IdentityMatrix4x4();
	private boolean processingTranslation = false, processingPerspective = false;
	private boolean mvpDirty = false;
	// debug
	private boolean hasRay = false;
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
	}
	private void drawGrid(float[] mat) {
		int vOffset = vBufMan.getVertexBufferOffset("GridPointBuffer"),
			iOffset = vBufMan.getIndexBufferOffset("GridPointMeshIndex");
		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
			mMVP = glGetUniformLocation(gridProgram.getProgramId(), "mMVP"),
			vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor");
		glUseProgram(gridProgram.getProgramId());
		glUniformMatrix4fv(mMVP, 1, false, mat, 0);
		glUniform4f(vColor, 1, 0, 0, 1);
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
		glUniform4f(textureColorTone, 0.5f, 0, 0, 0f);
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
		float[] v = new float[16];
		glUseProgram(gridProgram.getProgramId());
	}
	@Override
	public void draw() {
		// compose mvp every time
		// TODO: use mvpDirty to save computation
		float[] mat = FlatMatrix4x4Multiplication(projection, FlatMatrix4x4Multiplication(view, model));
		drawGrid(mat);
//		drawMap(mat);
		drawRay(mat);
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {this.textures = textures;}
	@Override
	public void setAspectRatio(float ratio) {
		// model parameter
		this.ratio = ratio;
		float x = 1, y = 1;
		if (rows > columns)
			x = (float) columns / rows;
		else if (rows < columns)
			y = (float) rows / columns;
		if (ratio > 1)
			x /= ratio;
		else
			y *= ratio;
		if (x > y) {
			y /= x;
			x = 1;
		} else {
			x /= y;
			y = 1;
		}
//		projection = FlatScalingMatrix4x4(0.5f,0.5f,1);
		projection = FlatPerspectiveMatrix4x4(cameraParams[6], cameraParams[7], -1, 1, -ratio, ratio);
		model = FlatScalingMatrix4x4(x,y,1);
		model = FlatMatrix4x4Multiplication (FlatTranslationMatrix4x4(0,0,-.3f), model);
	}
	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}
	@Override
	public void passEvent(ControlEvent e) {
		// Remember: normalise
		if (e.type == ControlEvent.TYPE_CLICK) {
			// tap start
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			// tap with movements
			onProcessingTranslation(e);
			onProcessingPerspective(e);
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (Math.abs(e.data.deltaX) < ControlEvent.TAP_DRIFT_LIMIT / ratio && Math.abs(e.data.deltaY) < ControlEvent.TAP_DRIFT_LIMIT * ratio) {
				// tap
				if (e.data.eventTime - e.data.startTime > ControlEvent.TAP_LONG_TIME_LIMIT)
					System.out.println("Long tap");	// long tap
				else
					System.out.println("Short tap");	// short tap
			}
			onProcessingTranslationDone(e);
			onProcessingPerspectiveDone(e);
		}
	}
	private void onProcessingPerspectiveDone(ControlEvent e) {
		// TODO Auto-generated method stub
		if (processingPerspective) {
			
		}
		processingPerspective = false;
	}
	private void onProcessingTranslationDone(ControlEvent e) {
		if (processingTranslation) {
			
		}
		processingTranslation = false;
	}
	private void onProcessingPerspective(ControlEvent e) {
		if (e.data.pointerCount != 2)
			return;
		if (processingTranslation)
			onProcessingTranslationDone(e);
		if (!processingPerspective) {
		}
	}
	private void onProcessingTranslation(ControlEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void rayTrace() {
	}
	@Override
	public void close() {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
	}
}
