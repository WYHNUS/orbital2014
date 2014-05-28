package edu.nus.comp.dotagridandroid.ui.renderers;

import java.io.IOException;
import static android.opengl.GLES20.*;

/* Generic Program
 * Default Shaders Compiling Routines
 */
public class GenericProgram implements Closeable {
	int programId, vs, fs;
	public GenericProgram (String vsSrc, String fsSrc) {
		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, vsSrc);
		glCompileShader(vs);
		String errStr = glGetShaderInfoLog(vs).trim();
		if (errStr.length() > 0)
			throw new RuntimeException("Vertex Shader error:\n" + errStr);
		
		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, fsSrc);
		glCompileShader(fs);
		errStr = glGetShaderInfoLog(fs).trim();
		if (errStr.length() > 0)
			throw new RuntimeException("Fragment Shader error:\n" + errStr);
		
		programId = glCreateProgram();
		glAttachShader(programId, vs);
		glAttachShader(programId, fs);
		glLinkProgram(programId);
		errStr = glGetProgramInfoLog(programId).trim();
		if (errStr.length() > 0)
			throw new RuntimeException("Program Linking error:\n" + errStr);
	}
	
	public int getProgramId () {
		return programId;
	}

	public void close() {
		glUseProgram(0);
		glDeleteProgram(programId);
		glDeleteShader(vs);
		glDeleteProgram(fs);
	}
}
