package edu.nus.comp.dotagridandroid.appsupport;

import edu.nus.comp.dotagridandroid.Closeable;

public class ResourceManager implements Closeable{
	static {
		System.loadLibrary("appsupport");
	}
	private static native long getTextureHandler(long ptr, String name);
	private static native long getModelHandler(long ptr, String name);
	private static native long getModelSize(long ptr, String name);
	private static native long getTextureWidth(long ptr, String name);
	private static native long getTextureHeight(long ptr, String name);
	private static native String getAllScript(long ptr);
	private static native boolean isExtensionEnabled(long ptr);
	private static native String getTerrainConfiguration(long ptr);
	// java
	private long ptr;
	protected ResourceManager(long ptr) {
		this.ptr = ptr;
	}
	@Override
	public void close() {
		AppNativeAPI.destroyResourceManager(ptr);
	}
	
	public int getTexture(String name) {
		return (int) getTextureHandler(ptr, name);
	}
	
	public int getTextureWidth(String name) {
		return (int) getTextureWidth(ptr, name);
	}

	public int getTextureHeight(String name) {
		return (int) getTextureHeight(ptr, name);
	}
	
	public int getModel(String name) {
		return (int) getModelHandler(ptr, name);
	}
	
	public int getModelSize(String name) {
		return (int) getModelSize(ptr, name);
	}
	
	public String getAllScript() {
		return getAllScript(ptr);
	}
	
	public boolean isExtensionEnabled() {
		return isExtensionEnabled(ptr);
	}
	
	public String getTerrainConfiguration() {
		return getTerrainConfiguration(ptr);
	}
}
