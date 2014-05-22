package edu.nus.comp.dotagrid.ui.renderers;
import java.io.Closeable;
import java.io.IOException;

import org.lwjgl.*;
import org.lwjgl.opengl.GL20;

/* Generic Program
 * Default Shaders Compiling Routines
 */
public class GenericProgram implements Closeable {
	int programId, vs, fs;
	public GenericProgram (String vsSrc, String fsSrc) {
		vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vs, vsSrc);
		GL20.glCompileShader(vs);
		String errStr = GL20.glGetShaderInfoLog(vs, 65536);
		if (errStr.length() > 0)
			throw new RuntimeException("Vertex Shader error:\n" + errStr);
		
		fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fs, fsSrc);
		GL20.glCompileShader(fs);
		errStr = GL20.glGetShaderInfoLog(fs, 65536);
		if (errStr.length() > 0)
			throw new RuntimeException("Fragment Shader error:\n" + errStr);
		
		programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vs);
		GL20.glAttachShader(programId, fs);
		GL20.glLinkProgram(programId);
		errStr = GL20.glGetProgramInfoLog(programId, 65536);
		if (errStr.length() > 0)
			throw new RuntimeException("Program Linking error:\n" + errStr);
	}
	
	public int getProgramId () {
		return programId;
	}

	@Override
	public void close() throws IOException {
		GL20.glDeleteProgram(programId);
		GL20.glDeleteShader(vs);
		GL20.glDeleteProgram(fs);
	}
}
