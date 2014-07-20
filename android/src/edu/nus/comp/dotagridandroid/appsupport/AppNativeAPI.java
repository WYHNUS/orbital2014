package edu.nus.comp.dotagridandroid.appsupport;

import android.content.Context;

public class AppNativeAPI {
	static {
		System.loadLibrary("appsupport");
	}
	protected static native long initiateResourceManager(String path);
	protected static native void destroyResourceManager(long ptr);
	
	protected static native long initiateExtensionEngine();
	protected static native void destroyExtensionEngine(long ptr);

	protected static native long initiateSoundEngine();
	protected static native void destroySoundEngine(long ptr);
	
	
	// java api
	public static ResourceManager createResourceManager(String path) {
		return new ResourceManager(initiateResourceManager(path));
	}
	
	public static SoundEngine createSoundEngine(Context context) {
		return new SoundEngine(initiateSoundEngine(), context.getAssets());
	}
	
	public static ExtensionEngine createExtensionEngine() {
		return new ExtensionEngine(initiateExtensionEngine());
	}
}
