package edu.nus.comp.dotagridandroid.appsupport;

public class AppNativeAPI {
	static {
		System.loadLibrary("appsupport");
	}
	public static native void testJS();
	public static native void testGL();
}
