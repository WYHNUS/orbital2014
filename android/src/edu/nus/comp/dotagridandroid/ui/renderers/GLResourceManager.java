package edu.nus.comp.dotagridandroid.ui.renderers;
import java.util.*;
import java.nio.*;

import edu.nus.comp.dotagridandroid.Closeable;
import static android.opengl.GLES20.*;
/*
 * Centralised static vertex buffer storage
 * FOR MOBILE:
 * WARNING:
 * OpenGL ES only support unsigned short as indices
 */
public class GLResourceManager implements Closeable {
	public static final int FLOAT_BYTES = Float.SIZE / 8, SHORT_BYTES = Short.SIZE / 8;
	
	private int[] bufs = new int[2];
	private int vBufHandler, iBufHandler;
	private Map<String, Integer> vOffset = new HashMap<>(), iOffset = new HashMap<>();
	private Map<String, float[]> vBuf = new HashMap<>();
	private Map<String, short[]> iBuf = new HashMap<>();
	private Boolean dirtyVertex = false, dirtyIndex = false;
	private Integer capacity = 0, indexes = 0;
	
	private Map<String, GenericProgram> programs = new HashMap<>();
	public GLResourceManager () {
		glGenBuffers(2, bufs, 0);
		vBufHandler = bufs[0];
		iBufHandler = bufs[1];
	}
	
	public synchronized void setVertexBuffer (String name, float[] v) {
		if (name == null || vBuf.get(name) == v || Arrays.equals(vBuf.get(name), v))
			return;
		dirtyVertex = true;
		if (v == null) {
			// deletion
			capacity -= vBuf.remove(name).length;
			vOffset.remove(name);
		} else {
			float[] org = vBuf.put(name, v);
			if (org == null)
				capacity += v.length;
			else
				capacity = capacity - org.length + v.length;
		}
	}
	public synchronized void setIndexBuffer (String name, int[] idx) {
		if (name == null)
			return;
		else if (idx == null) {
			if (iBuf.get(name) != null)
				return;
			indexes -= iBuf.remove(name).length;
			iOffset.remove(name);
			return;
		}
		short[] v = new short[idx.length];
		for (int i = 0; i < v.length; i++)
			v[i] = (short) (0xffff & idx[i]);
		if (Arrays.equals(iBuf.get(name), v))
			return;
		dirtyIndex = true;
		short[] org = iBuf.put(name, v);
		if (org == null)
			indexes += v.length;
		else
			indexes = indexes - org.length + v.length;
	}
	public int getVertexBuffer () {return vBufHandler;}
	public int getIndexBuffer () {return iBufHandler;}
	public synchronized int getVertexBufferOffset (String name) {
		if (dirtyVertex)
			updateVertex();
		return vOffset.get(name) * FLOAT_BYTES;
	}
	public synchronized int getIndexBufferOffset (String name) {
		if (dirtyIndex)
			updateIndex();
		return iOffset.get(name) * SHORT_BYTES;
	}
	public synchronized void updateVertex() {
		FloatBuffer fBuf = BufferUtils.createFloatBuffer(capacity);
		int c = 0;
		for (Map.Entry<String, float[]> v : vBuf.entrySet()) {
			vOffset.put(v.getKey(), c);
			float[] data = v.getValue();
			fBuf.put(data);
			c += data.length;
		}
		if (c != capacity)
			throw new RuntimeException ("Capacity mismatch");
		glBindBuffer(GL_ARRAY_BUFFER, vBufHandler);
		glBufferData(GL_ARRAY_BUFFER, capacity * FLOAT_BYTES, fBuf.position(0), GL_STATIC_DRAW);
		dirtyVertex = false;
	}
	public synchronized void updateIndex() {
		ShortBuffer intBuf = BufferUtils.createShortBuffer(indexes);
		int c = 0;
		for (Map.Entry<String, short[]> v : iBuf.entrySet()) {
			iOffset.put(v.getKey(), c);
			short[] data = v.getValue();
			intBuf.put(data);
			c += data.length;
		}
		if (c != indexes)
			throw new RuntimeException ("Index count mismatch");
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iBufHandler);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes * SHORT_BYTES, intBuf.position(0), GL_STATIC_DRAW);
		dirtyIndex = false;
	}
	
	public GenericProgram getProgram(String name) {
		return programs.get(name);
	}
	public void setProgram(String name, GenericProgram program) {
		if (name != null) {
			if (program != null)
				programs.put(name, program);
			else
				programs.remove(name);
		}
	}
	
	@Override
	public void close() {
		glDeleteBuffers(2, bufs, 0);
		for (GenericProgram program : programs.values())
			program.close();
	}
}
