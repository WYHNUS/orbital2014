package edu.nus.comp.dotagridandroid.ui.renderers;
import java.nio.*;

public class BufferUtils {
	final static int FLOAT_BYTES = Float.SIZE / 8, INT_BYTES = Integer.SIZE / 8;
	public static FloatBuffer createFloatBuffer(int size) {
		return ByteBuffer
			.allocateDirect(size * FLOAT_BYTES)
			.order(ByteOrder.nativeOrder())
			.asFloatBuffer();
	}
	public static IntBuffer createIntBuffer(int size) {
		return ByteBuffer
				.allocate(size * INT_BYTES)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
	}
}
