package edu.nus.comp.dotagridandroid.appsupport;

import edu.nus.comp.dotagridandroid.Closeable;

public class GridRendererGraphicsImpl implements Closeable {
	private long ptr;
	
	private native static long newInstance();
	private native static void initialise(
			long ptr,
			int columns,
			int rows,
			String gridvs,
			String gridfs,
			String mapvs,
			String mapfs,
			String shadowvs,
			String shadowfs,
			String shadowobjvs,
			String shadowobjfs,
			int resolution,
			int gridBuffer,
			int gridIndexBuffer,
			int mapTexture,
			int terrainBuffer,
			int terrainNormalMap,
			float[] lightProjection);
	
	private native static void setModelMatrix(long ptr, float[] mat);
	private native static void setViewMatrix(long ptr, float[] mat);
	private native static void setProjectionMatrix(long ptr, float[] mat);
	
	private native static void setLight(long ptr, String name, float[] lightSrc, boolean lightOn, int[] pos, int range);
	private native static void setLightOn (long ptr, String name, boolean lightOn);
	private native static void setDrawable(long ptr, String name, int textureHandler, int modelHandler, int modelSize, float[] modelMatrix, int[] pos);
	private native static void setDrawableVisible(long ptr, String name, boolean visible);
	
	private native static void setSelectedGrid(long ptr, boolean hasSelectedGrid, int[] pos);
	private native static void setHighlightedGrid(long ptr, boolean hasHighlightedGrid, int[][] pos);
	
	private native static void configureShadow(long ptr, String name);
	private native static void draw(long ptr);
	
	private native static void close(long ptr);
	
	public GridRendererGraphicsImpl() {
		ptr = newInstance();
	}

	public void setModelMatrix(float[] mat) {
		setModelMatrix(ptr, mat);
	}
	public void setViewMatrix(float[] mat) {
		setViewMatrix(ptr, mat);
	}
	public void setProjectionMatrix(float[] mat) {
		setProjectionMatrix(ptr, mat);
	}
	
	public void initialise (
			int columns,
			int rows,
			String gridvs,
			String gridfs,
			String mapvs,
			String mapfs,
			String shadowvs,
			String shadowfs,
			String shadowobjvs,
			String shadowobjfs,
			int resolution,
			int gridBuffer,
			int gridIndexBuffer,
			int mapTexture,
			int terrainBuffer,
			int terrainNormalMap,
			float[] lightProjection) {
		initialise(
				ptr,
				columns,
				rows,
				gridvs,
				gridfs,
				mapvs,
				mapfs,
				shadowvs,
				shadowfs,
				shadowobjvs,
				shadowobjfs,
				resolution,
				gridBuffer,
				gridIndexBuffer,
				mapTexture,
				terrainBuffer,
				terrainNormalMap,
				lightProjection);
	}
	
	public void setDrawable(String name, int textureHandler, int modelHandler, int modelSize, float[] modelMatrix, int[] pos) {
		setDrawable(ptr, name, textureHandler, modelHandler, modelSize, modelMatrix, pos);
	}
	
	public void setDrawableVisible(String name, boolean visible) {
		setDrawableVisible(ptr, name, visible);
	}
	
	public void setLight (String name, float[] lightSrc, boolean lightOn, int[] pos, int range) {
		setLight(ptr, name, lightSrc, lightOn, pos, range);
	}
	
	public void setLightOn (String name, boolean lightOn) {
		setLightOn (ptr, name, lightOn);
	}
	
	public void setSelectedGrid(boolean hasSelectedGrid, int[] pos) {
		setSelectedGrid(ptr, hasSelectedGrid, pos);
	}
	
	public void setHighlightedGrid(boolean hasHighlightedGrid, int[][] pos) {
		setHighlightedGrid(ptr, hasHighlightedGrid, pos);
	}
	
	public void configureShadow(String name) {
		configureShadow(ptr, name);
	}
	
	public void draw() {
		draw(ptr);
	}
	
	@Override
	public void close() {
		close(ptr);
	}
}
