package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;
import static android.opengl.GLES20.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private int rows, columns;
	private GenericProgram gridProgram, mapProgram;
	private float[] mvp;
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
		gridProgram = new 			GenericProgram(
				new String(CommonShaders.VS_IDENTITY),
				new String(CommonShaders.FS_IDENTITY));
		// configure indices buffer
		setMVP(IdentityMatrix4x4());
		// configure map
//		final String
//			mapvsSrc = "attribute vec4 vPosition;"
//					+ "attribute vec2 textureCoord;"
//					+ "uniform mat4 mMVP;"
//					+ "varying vec2 autoTextureCoord;"
//					+ "void main() {"
//						+ "gl_Position = mMVP * vPosition;"
//						+ "autoTextureCoord = textureCoord;"
//					+ "}",
//			mapfsSrc = "precision mediump float;"
//					+ "uniform sampler2D texture;"
//					+ "varying vec2 autoTextureCoord;"
//					+ "void main () {"
//						+ "gl_FragColor = texture2D (texture, autoTextureCoord).rgba;"
//					+ "}";
//		mapProgram = new GenericProgram (mapvsSrc, mapfsSrc);
		mapProgram = new GenericProgram (
				new String(CommonShaders.VS_IDENTITY_TEXTURED),
				new String(CommonShaders.FS_IDENTITY_TEXTURED_TONED));
	}
	private void drawGrid() {
		int vOffset = vBufMan.getVertexBufferOffset("GridPointBuffer"),
			iOffset = vBufMan.getIndexBufferOffset("GridPointMeshIndex");
		int vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition"),
			mMVP = glGetUniformLocation(gridProgram.getProgramId(), "mMVP"),
			vColor = glGetUniformLocation(gridProgram.getProgramId(), "vColor");
		glUseProgram(gridProgram.getProgramId());
		glUniformMatrix4fv(mMVP, 1, false, mvp, 0);
		glUniform4f(vColor, 1, 0, 0, 1);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glDrawElements(GL_LINES, 2 * (columns + rows + 2), GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
	}
	private void drawMap() {
		int vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTexture"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		int vPosition = glGetAttribLocation(mapProgram.getProgramId(), "vPosition"),
			mMVP = glGetUniformLocation(mapProgram.getProgramId(), "mMVP"),
			textureColorTone = glGetUniformLocation(mapProgram.getProgramId(), "textureColorTone"),
			textureLocation = glGetUniformLocation(mapProgram.getProgramId(), "texture"),
			textureCoord = glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
		glUseProgram(mapProgram.getProgramId());
		glUniformMatrix4fv(mMVP, 1, false, mvp, 0);
		glUniform4f(textureColorTone, 0.5f, 0, 0, -0.9f);
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
	@Override
	public void draw() {
		// draw grid lines
//		drawMap();
		drawGrid();
		drawMap();
	}

	public void close() {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
	}
	@Override
	public void setMVP(float[] matrix) {
		mvp = matrix;
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}
}
