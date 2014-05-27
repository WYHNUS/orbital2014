package edu.nus.comp.dotagrid.ui.renderers;
import java.util.*;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
/*
 * Centralised static vertex buffer storage
 */
public class VertexBufferManager implements AutoCloseable {
	private int vBufHandler, iBufHandler;
	private Map<String, Integer> vOffset = new HashMap<>(), iOffset = new HashMap<>();
	private Map<String, float[]> vBuf = new HashMap<>();
	private Map<String, int[]> iBuf = new HashMap<>();
	private boolean dirtyVertex = false, dirtyIndex = false;
	private int capacity = 0, indexes = 0;
	
	public VertexBufferManager () {
		vBufHandler = GL15.glGenBuffers();
		iBufHandler = GL15.glGenBuffers();
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
	public long getVertexBufferOffset (String name) {
		if (dirtyVertex)
			updateVertex();
		return vOffset.get(name) * Float.BYTES;
	}
	public long getIndexBufferOffset (String name) {
		if (dirtyIndex)
			updateIndex();
		return iOffset.get(name) * Integer.BYTES;
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
		fBuf.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vBufHandler);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuf, GL15.GL_STATIC_DRAW);
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
		intBuf.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iBufHandler);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuf, GL15.GL_STATIC_DRAW);
		dirtyIndex = false;
	}
	@Override
	public void close() throws Exception {
		GL15.glDeleteBuffers(vBufHandler);
		GL15.glDeleteBuffers(iBufHandler);
	}
}
