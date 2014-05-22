package edu.nus.comp.dotagrid.ui.renderers;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class TriangleRenderer implements Renderer {
	private GenericProgram program;
	private int vertexBuf, indexBuf;
	public TriangleRenderer () {
		final String
			vsSrc = "attribute vec3 vPosition;"
				+ "void main () {"
					+ "vec4 v = vec4(vPosition, 0.0);"
					+ "gl_Position = v;"
				+ "}",
			fsSrc = "uniform vec4 vColor;"
				+ "void main () {"
					+ "gl_FragColor = vColor;"
				+ "}";
		FloatBuffer fBuf = BufferUtils.createFloatBuffer(9);
		fBuf.put(new float[]{-1,-1,0,1,-1,0,1,0,0});
		ShortBuffer sBuf = BufferUtils.createShortBuffer(3);
		sBuf.put(new short[]{0,1,2});
		program = new GenericProgram(vsSrc, fsSrc);
		vertexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuf);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuf, GL15.GL_STATIC_DRAW);
		indexBuf = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuf);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, sBuf, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void draw() {
		int error;
		GL20.glUseProgram(program.getProgramId());
		int vPosition = GL20.glGetAttribLocation(program.getProgramId(), "vPosition");
		int vColor = GL20.glGetAttribLocation(program.getProgramId(), "vColor");
		
		GL20.glVertexAttribPointer(vPosition, 3, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(vPosition);
		error = GL11.glGetError();
		if (error != 0) {
			throw new RuntimeException ("OpenGL: error: " + error);
		}
		
		GL20.glUniform4f(vColor, 0f, 0f, 0f, 0f);
	}

	@Override
	public void close() throws IOException {
		GL15.glDeleteBuffers(vertexBuf);
		GL15.glDeleteBuffers(indexBuf);
	}

	@Override
	public void setMVP(float[] matrix) {
	}

}
