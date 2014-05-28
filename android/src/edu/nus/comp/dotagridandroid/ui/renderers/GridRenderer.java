package edu.nus.comp.dotagridandroid.ui.renderers;

import java.io.IOException;
import java.nio.*;
import java.util.Map;

import static android.opengl.GLES20.*;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private int rows, columns;
	private GenericProgram gridProgram, mapProgram;
	private FloatBuffer mMVPBuf;
	private boolean firstTime = true;
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
		final String
			vsSrc = "attribute vec4 vPosition;"
					+ "uniform mat4 mMVP;"
					+ "void main () {"
						+ "gl_Position = mMVP * vPosition;"
					+ "}",
			fsSrc = "uniform vec4 vColor;"
					+ "void main () {"
						+ "gl_FragColor = vec4(1,0,0,1);"
					+ "}";
		gridProgram = new GenericProgram(vsSrc, fsSrc);
		// configure indices buffer
		setMVP(IdentityMatrix4x4());
		// configure map
		final String
			mapvsSrc = "attribute vec4 vPosition;"
					+ "attribute vec2 textureCoord;"
					+ "uniform mat4 mMVP;"
					+ "varying vec2 autoTextureCoord;"
					+ "void main() {"
						+ "gl_Position = mMVP * vPosition;"
						+ "autoTextureCoord = textureCoord;"
					+ "}",
			mapfsSrc = "uniform sampler2D texture;"
					+ "varying vec2 autoTextureCoord;"
					+ "void main () {"
						+ "gl_FragColor = texture2D (texture, autoTextureCoord).rgba;"
					+ "}";
		mapProgram = new GenericProgram (mapvsSrc, mapfsSrc);
	}
	private void drawGrid() {
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		int vPosition, mMVP;
		glUseProgram(gridProgram.getProgramId());
		vPosition = glGetAttribLocation(gridProgram.getProgramId(), "vPosition");
		mMVP = glGetUniformLocation(gridProgram.getProgramId(), "mMVP");
		// it seems that screen goes black if mMVPBuf.flip(); is done twice
		glUniformMatrix4fv(mMVP, 1, true, mMVPBuf);	// 2nd param set to false to use perspective transformation
//		glEnableVertexAttribArray(vPosition);
//		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GridPointBuffer"));
//		glDrawElements(GL_LINES, 2 * (columns + rows + 2), GL_UNSIGNED_INT, vBufMan.getIndexBufferOffset("GridPointMeshIndex"));
//		glDisableVertexAttribArray(vPosition);
	}
	private void drawMap() {
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		int vPosition, mMVP, textureLocation, textureCoord;
		// enable grid program
		glUseProgram(mapProgram.getProgramId());
		// draw background first
		vPosition = glGetAttribLocation(mapProgram.getProgramId(), "vPosition");
		mMVP = glGetUniformLocation(mapProgram.getProgramId(), "mMVP");
		glUniformMatrix4fv(mMVP, 1, true, mMVPBuf);
		// vertex buffer bind
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GenericFullSquare"));
		// bind texture
		glBindTexture(GL_TEXTURE_2D, textures.get("GridMapBackground").getTexture());
		// texture vertex
		textureLocation = glGetUniformLocation(mapProgram.getProgramId(), "texture");
		glUniform1i(textureLocation, 0);
		textureCoord = glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
		glEnableVertexAttribArray(textureCoord);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GenericFullSquareTexture"));
		// draw
		glDrawElements(GL_TRIANGLE_STRIP, 6, GL_UNSIGNED_INT, vBufMan.getIndexBufferOffset("GenericFullSquareIndex"));
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
	}
	@Override
	public void draw() {
		// draw grid lines
		mMVPBuf.position(0);
		drawGrid();
//		drawMap();
	}

	public void close() {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
	}
	@Override
	public void setMVP(float[] matrix) {
		if (matrix.length != 16)
			throw new RuntimeException ("Invalid Matrix");
		mMVPBuf = BufferUtils.createFloatBuffer(matrix.length).put(matrix);
	}
	
	private void checkError() {
		int error = glGetError();
		if (error != 0)
			throw new RuntimeException ("OpenGL: " + error);
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {}
	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}
}
