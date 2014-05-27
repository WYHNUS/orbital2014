package edu.nus.comp.dotagrid.ui.renderers;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import static edu.nus.comp.dotagrid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private int rows, columns;
	private int vertexBuf, indexBuf, textBuf = -1, mapVertexBuf, mapIndexBuf, textVertexBuf, frameBuf;
	private GenericProgram gridProgram, mapProgram;
	private FloatBuffer mMVPBuf;
	public GridRenderer (int rows, int columns) {
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
		final String
			vsSrc = "attribute vec4 vPosition;"
					+ "uniform mat4 mMVP;"
					+ "void main () {"
						+ "gl_Position = mMVP * vPosition;"
					+ "}",
			fsSrc = "uniform vec4 vColor;"
					+ "void main () {"
						+ "gl_FragColor = vec4(1,1,1,1);"
					+ "}";
		gridProgram = new GenericProgram(vsSrc, fsSrc);
		FloatBuffer fBuf = BufferUtils.createFloatBuffer(v.length).put(v);
		fBuf.flip();
		IntBuffer iBuf = BufferUtils.createIntBuffer(idx.length).put(idx);
		iBuf.flip();
		// configure vertex buffer
		vertexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuf);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuf, GL15.GL_STATIC_DRAW);
		// configure indices buffer
		indexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuf);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuf, GL15.GL_STATIC_DRAW);
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
						+ "color = /*texture2D (texture, autoTextureCoord).rgba*/ vec4 (1,1,1,1);"
					+ "}";
		mapProgram = new GenericProgram (mapvsSrc, mapfsSrc);
		fBuf = BufferUtils.createFloatBuffer(16).put(new float[]{1,1,0,1,1,-1,0,1,-1,-1,0,1,-1,1,0,1});
		fBuf.flip();
		iBuf = BufferUtils.createIntBuffer(4).put(new int[]{0,1,3,2});
		iBuf.flip();
		mapVertexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mapVertexBuf);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuf, GL15.GL_STATIC_DRAW);
		mapIndexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mapIndexBuf);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuf, GL15.GL_STATIC_DRAW);
		fBuf = BufferUtils.createFloatBuffer(8).put(new float[]{0,0,1,0,1,1,1,0});
		textVertexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textVertexBuf);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuf, GL15.GL_STATIC_DRAW);
		// initiate framebuffer
	}
	public GridRenderer (int row, int height, Texture2D texture) {
		this (row, height);
		// TODO: texture support
		textBuf = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textBuf);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		IntBuffer buf = texture.getBuffer();
		buf.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	}
	@Override
	public void draw() {
		int vPosition, mMVP;
		// enable grid program
		GL20.glUseProgram(mapProgram.getProgramId());
		// draw background first
		vPosition = GL20.glGetAttribLocation(mapProgram.getProgramId(), "vPosition");
		mMVP = GL20.glGetUniformLocation(mapProgram.getProgramId(), "mMVP");
		mMVPBuf.flip();
		GL20.glUniformMatrix4(mMVP, true, mMVPBuf);
		// vertex buffer bind
		GL20.glEnableVertexAttribArray(vPosition);
		GL20.glVertexAttribPointer(vPosition, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mapVertexBuf);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mapIndexBuf);
		// bind texture
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, textBuf);
		// texture vertex
//		int textureLocation = GL20.glGetUniformLocation(mapProgram.getProgramId(), "texture");
//		GL20.glUniform1i(textureLocation, 0);
//		int textureCoord = GL20.glGetAttribLocation(mapProgram.getProgramId(), "textureCoord");
//		GL20.glEnableVertexAttribArray(textureCoord);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textVertexBuf);
//		GL20.glVertexAttribPointer(textureCoord, 2, GL11.GL_FLOAT, false, 0, 0);
		// draw
		//GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 6, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(vPosition);
//		GL20.glDisableVertexAttribArray(textureCoord);
		// draw grid lines
		GL20.glUseProgram(gridProgram.getProgramId());
		// get attribute index in the program
		vPosition = GL20.glGetAttribLocation(gridProgram.getProgramId(), "vPosition");
		checkError();
		mMVP = GL20.glGetUniformLocation(gridProgram.getProgramId(), "mMVP");
		//mMVPBuf.flip();
		GL20.glUniformMatrix4(mMVP, true, mMVPBuf);	// 2nd param set to false to use perspective transformation
		// pass pointer of the vertex buffer to the attribute
		GL20.glVertexAttribPointer(vPosition, 4, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(vPosition);
		// pass vertices to vPosition
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuf);
		// pass MVP values
		// pass indices to gl
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuf);
		// TODO: set uniform colour vector
		int vColor = GL20.glGetAttribLocation(gridProgram.getProgramId(), "vColor");
		GL20.glUniform4f(vColor, 1f, 1f, 1f, 0f);
		checkError();
		// draw
		GL11.glDrawElements(GL11.GL_LINES, 2 * (columns + rows + 2), GL11.GL_UNSIGNED_INT, 0);
		checkError();
		GL20.glDisableVertexAttribArray(vPosition);
	}
	@Override
	public void close() throws IOException {
		// delete buffers
		GL15.glDeleteBuffers(vertexBuf);
		GL15.glDeleteBuffers(indexBuf);
		if (textBuf >= 0)
			GL11.glDeleteTextures(textBuf);;
	}
	@Override
	public void setMVP(float[] matrix) {
		if (matrix.length != 16)
			throw new RuntimeException ("Invalid Matrix");
		mMVPBuf = BufferUtils.createFloatBuffer(matrix.length).put(matrix);
	}
	
	private void checkError() {
	}
	@Override
	public void setFrameBufferHandler(int framebuffer) {frameBuf = framebuffer;}
}
