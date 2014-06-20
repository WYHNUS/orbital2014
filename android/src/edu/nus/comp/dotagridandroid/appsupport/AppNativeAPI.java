package edu.nus.comp.dotagridandroid.appsupport;

public class AppNativeAPI {
	static {
		System.loadLibrary("appsupport");
	}
	public static native void testJS();
	public static native void testGL();
	public static native void testZIP();
	protected static native long initiateResourceManager(String path);
	protected static native void destroyResourceManager(long ptr);
	
	// java api
	public static ResourceManager createResourceManager(String path) {
		return new ResourceManager(initiateResourceManager(path));
	}
}
