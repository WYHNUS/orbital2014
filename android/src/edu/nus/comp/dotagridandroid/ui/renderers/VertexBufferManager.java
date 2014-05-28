package edu.nus.comp.dotagridandroid.ui.renderers;
import java.util.*;
import java.nio.*;
import static android.opengl.GLES20.*;
/*
 * Centralised static vertex buffer storage
 */
public class VertexBufferManager {
	private int[] bufs = new int[2];
	private int vBufHandler, iBufHandler;
	private Map<String, Integer> vOffset = new HashMap<>(), iOffset = new HashMap<>();
	private Map<String, float[]> vBuf = new HashMap<>();
	private Map<String, int[]> iBuf = new HashMap<>();
	private boolean dirtyVertex = false, dirtyIndex = false;
	private int capacity = 0, indexes = 0;
	final static int FLOAT_BYTES = Float.SIZE / 8, INT_BYTES = Integer.SIZE / 8;
	public VertexBufferManager () {
		glGenBuffers(2, bufs, 0);
		vBufHandler = bufs[0];
		iBufHandler = bufs[1];
	}
	
	public void setVertexBuffer (String name, float[] v) {
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
	public void setIndexBuffer (String name, int[] idx) {
		if (name == null || iBuf.get(name) == idx || Arrays.equals(iBuf.get(name), idx))
			return;
		dirtyIndex = true;
		if (idx == null) {
			indexes -= iBuf.remove(name).length;
			iOffset.remove(name);
		} else {
			int[] org = iBuf.put(name, idx);
			if (org == null)
				indexes += idx.length;
			else
				indexes = indexes - org.length + idx.length;
		}
	}
	public int getVertexBuffer () {return vBufHandler;}
	public int getIndexBuffer () {return iBufHandler;}
	public int getVertexBufferOffset (String name) {
		if (dirtyVertex)
			updateVertex();
		return vOffset.get(name) * FLOAT_BYTES;
	}
	public int getIndexBufferOffset (String name) {
		if (dirtyIndex)
			updateIndex();
		return iOffset.get(name) * INT_BYTES;
	}
	public void updateVertex() {
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
	public void updateIndex() {
		IntBuffer intBuf = BufferUtils.createIntBuffer(indexes);
		int i = 0;
		for (Map.Entry<String, int[]> v : iBuf.entrySet()) {
			iOffset.put(v.getKey(), i);
			int[] data = v.getValue();
			intBuf.put(data);
			i += data.length;
		}
		if (i != indexes)
			throw new RuntimeException ("Index count mismatch");
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iBufHandler);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes * INT_BYTES, intBuf.position(0), GL_STATIC_DRAW);
		dirtyIndex = false;
	}
	
	public void close() throws Exception {
		glDeleteBuffers(2, bufs, 0);
	}
}
