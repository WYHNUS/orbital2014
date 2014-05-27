package edu.nus.comp.dotagrid.ui.renderers;

import java.nio.*;
import java.awt.image.*;

import org.lwjgl.BufferUtils;

public class Texture2D {
	private int width, height;
	private IntBuffer buf;
	public Texture2D(BufferedImage image) {
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		width = image.getWidth();
		height = image.getHeight();
		int c = 0, cs = 0;
		boolean hasAlpha = image.getAlphaRaster() != null;
		int[] pixels;
		if (hasAlpha)
			pixels = new int[data.length / 4];
		else
			pixels = new int[data.length / 3];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				int rgba = 0;
				if (hasAlpha)
					rgba += (data[c++] & 0xff) << 24;
				else
					rgba += -16777216;
				rgba += (data[c++] & 0xff) << 16;
				rgba += (data[c++] & 0xff) << 8;
				rgba += data[c++] & 0xff;
				pixels[cs++] = rgba;
			}
		buf = BufferUtils.createIntBuffer(pixels.length).put(pixels);
	}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public IntBuffer getBuffer() {return buf;}
}
