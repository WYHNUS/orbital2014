package edu.nus.comp.dotagrid.ui.renderers;

import java.io.IOException;
import java.nio.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import static edu.nus.comp.dotagrid.math.RenderMaths.*;

public class GridRenderer implements Renderer {
	private int rows, columns;
	private int vertexBuf, indexBuf;
	private GenericProgram program;
	private FloatBuffer mMVPBuf;
	public GridRenderer (int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		float[] v = new float[4 * 2 * (rows + columns + 2)];
		int c = 0;
		for (int i = 0; i <= rows; i++) {
			v[c++] = -1;
			v[c++] = 2f / rows * i - 1;
			v[c++] = 0;
			v[c++] = 1;
			v[c++] = 1;
			v[c++] = 2f / rows * i - 1;
			v[c++] = 0;
			v[c++] = 1;
		}
		for (int i = 0; i <= columns; i++) {
			v[c++] = 2f / columns * i - 1;
			v[c++] = -1;
			v[c++] = 0;
			v[c++] = 1;
			v[c++] = 2f / columns * i - 1;
			v[c++] = 1;
			v[c++] = 0;
			v[c++] = 1;
		}
		int[] idx = new int[2 * (rows + columns + 2)];
		for (int i = 0; i < 2 * (rows + columns + 2); i++)
			idx[i] = i;
		final String
			vsSrc = "attribute vec4 vPosition;"
					+ "uniform mat4 mMVP;"
					+ "void main () {"
						+ "gl_Position = mMVP * vPosition;"
					+ "}",
			fsSrc = "uniform vec4 vColor;"
					+ "void main () {"
						+ "gl_FragColor = vec4(1,1,1,0);"
					+ "}";
		program = new GenericProgram(vsSrc, fsSrc);
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
		setMVP(IdentityMatrix4x4);
	}
	@Override
	public void draw() {
		int error;
		// enable grid program
		GL20.glUseProgram(program.getProgramId());
		// get attribute index in the program
		int vPosition = GL20.glGetAttribLocation(program.getProgramId(), "vPosition");
		int mMVP = GL20.glGetUniformLocation(program.getProgramId(), "mMVP");
		mMVPBuf.flip();
		GL20.glUniformMatrix4(mMVP, false, mMVPBuf);
		// pass pointer of the vertex buffer to the attribute
		GL20.glVertexAttribPointer(vPosition, 4, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(vPosition);
		error = GL11.glGetError();
		if (error != 0)
			throw new RuntimeException ("OpenGL: error: " + error);
		// pass vertices to vPosition
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuf);
		// pass MVP values
		// pass indices to gl
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuf);
		// TODO: set uniform colour vector
		int vColor = GL20.glGetAttribLocation(program.getProgramId(), "vColor");
		GL20.glUniform4f(vColor, 1f, 1f, 1f, 0f);
		error = GL11.glGetError();
		if (error != 0)
			throw new RuntimeException ("OpenGL: error: " + error);
		// draw
		GL11.glDrawElements(GL11.GL_LINES, 2 * (rows + columns + 2), GL11.GL_UNSIGNED_INT, 0);
		error = GL11.glGetError();
		if (error != 0)
			throw new RuntimeException ("OpenGL: error: " + error);
		GL20.glDisableVertexAttribArray(vPosition);
	}
	@Override
	public void close() throws IOException {
		// delete buffers
		GL15.glDeleteBuffers(vertexBuf);
	}
	@Override
	public void setMVP(float[] matrix) {
		if (matrix.length != 16)
			throw new RuntimeException ("Invalid Matrix");
		mMVPBuf = BufferUtils.createFloatBuffer(matrix.length).put(matrix);
	}
}
