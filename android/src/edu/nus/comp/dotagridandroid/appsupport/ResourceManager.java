package edu.nus.comp.dotagridandroid.appsupport;

import edu.nus.comp.dotagridandroid.Closeable;

public class ResourceManager implements Closeable{
	static {
		System.loadLibrary("appsupport");
	}
	private static native long getTextureHandler(long ptr, String name);
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
}
