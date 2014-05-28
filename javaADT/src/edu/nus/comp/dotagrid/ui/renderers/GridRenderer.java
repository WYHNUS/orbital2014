package edu.nus.comp.dotagrid.ui.renderers;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import static edu.nus.comp.dotagrid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private VertexBufferManager vBufMan;
	private int rows, columns;
	private int vertexBuf, indexBuf, textBuf = -1, mapVertexBuf, mapIndexBuf, textVertexBuf, frameBuf;
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
			mapvsSrc = "#version 330 core\n"
					+ "in vec4 vPosition;"
					+ "in vec2 textureCoord;"
					+ "uniform mat4 mMVP;"
					+ "out vec2 autoTextureCoord;"
					+ "void main() {"
						+ "gl_Position = mMVP * vPosition;"
						+ "autoTextureCoord = textureCoord;"
					+ "}",
			mapfsSrc = "#version 330 core\n"
					+ "uniform sampler2D texture;"
					+ "in vec2 autoTextureCoord;"
					+ "out vec4 color;"
					+ "void main () {"
						+ "color = texture2D (texture, autoTextureCoord).rgba;"
					+ "}";
		mapProgram = new GenericProgram (mapvsSrc, mapfsSrc);
	}
	public GridRenderer (VertexBufferManager bufMan, int row, int height, Texture2D texture) {
		this (bufMan, row, height);
		// TODO: texture support
		textBuf = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textBuf);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		IntBuffer buf = texture.getBuffer();
		buf.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	}
	private void drawGrid() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		int vPosition, mMVP;
		GL20.glUseProgram(gridProgram.getProgramId());
		vPosition = GL20.glGetAttribLocation(gridProgram.getProgramId(), "vPosition");
		mMVP = GL20.glGetUniformLocation(gridProgram.getProgramId(), "mMVP");
		// it seems that screen goes black if mMVPBuf.flip(); is done twice
		GL20.glUniformMatrix4(mMVP, true, mMVPBuf);	// 2nd param set to false to use perspective transformation
		GL20.glEnableVertexAttribArray(vPosition);
		GL20.glVertexAttribPointer(vPosition, 4, GL11.GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GridPointBuffer"));
		GL11.glDrawElements(GL11.GL_LINES, 2 * (columns + rows + 2), GL11.GL_UNSIGNED_INT, vBufMan.getIndexBufferOffset("GridPointMeshIndex"));
		GL20.glDisableVertexAttribArray(vPosition);
	}
	private void drawMap() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		int vPosition, mMVP, textureLocation, textureCoord;
		// enable grid program
		GL20.glUseProgram(mapProgram.getProgramId());
		// draw background first
		vPosition = GL20.glGetAttribLocation(mapProgram.getProgramId(), "vPosition");
		mMVP = GL20.glGetUniformLocation(mapProgram.getProgramId(), "mMVP");
		GL20.glUniformMatrix4(mMVP, true, mMVPBuf);
		// vertex buffer bind
		GL20.glEnableVertexAttribArray(vPosition);
		GL20.glVertexAttribPointer(vPosition, 4, GL11.GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GenericFullSquare"));
		// bind texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textBuf);
		// texture vertex
		textureLocation = GL20.glGetUniformLocation(mapProgram.getProgramId(), "texture");
		GL20.glUniform1i(textureLocation, 0);
		textureCoord = GL20.glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
		GL20.glEnableVertexAttribArray(textureCoord);
		GL20.glVertexAttribPointer(textureCoord, 2, GL11.GL_FLOAT, false, 0, vBufMan.getVertexBufferOffset("GenericFullSquareTexture"));
		// draw
		GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 6, GL11.GL_UNSIGNED_INT, vBufMan.getIndexBufferOffset("GenericFullSquareIndex"));
		GL20.glDisableVertexAttribArray(vPosition);
		GL20.glDisableVertexAttribArray(textureCoord);
	}
	@Override
	public void draw() {
		// draw grid lines
		mMVPBuf.flip();
		drawGrid();
		drawMap();
	}
	@Override
	public void close() throws IOException {
		// delete buffers
		mapProgram.close();
		gridProgram.close();
		GL15.glDeleteBuffers(vertexBuf);
		GL15.glDeleteBuffers(indexBuf);
		if (textBuf >= 0)
			GL11.glDeleteTextures(textBuf);
	}
	@Override
	public void setMVP(float[] matrix) {
		if (matrix.length != 16)
			throw new RuntimeException ("Invalid Matrix");
		mMVPBuf = BufferUtils.createFloatBuffer(matrix.length).put(matrix);
	}
	
	private void checkError() {
		int error = GL11.glGetError();
		if (error != 0)
			throw new RuntimeException ("OpenGL: " + error);
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {frameBuf = framebuffer;}
}
