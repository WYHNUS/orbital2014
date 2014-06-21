package edu.nus.comp.dotagridandroid.appsupport;

import android.content.res.AssetManager;
import edu.nus.comp.dotagridandroid.Closeable;

public class SoundEngine implements Closeable{
	static {
		System.loadLibrary("appsupport");
	}
	private static native void prepareBufferQueuePlayer(long ptr);
	private static native void prepareAssetPlayer(long ptr, AssetManager mgr, String file, String name);
	private static native void setAssetPlayerPlayState(long ptr, String name, boolean playState);
	private static native void setAssetPlayerSeek(long ptr, String name, long position);
	private static native void setAssetPlayerStop(long ptr, String name);
	private long ptr;
	private AssetManager mgr;
	protected SoundEngine(long ptr, AssetManager mgr) {
		this.ptr = ptr;
		this.mgr = mgr;
	}
	public void prepareBufferQueuePlayer() {
		prepareBufferQueuePlayer(ptr);
	}
	public void prepareAssetPlayer(String file, String name) {
		prepareAssetPlayer(ptr, mgr, file, name);
	}
	public void setAssetPlayerSeek(String name, long position) {
		setAssetPlayerSeek(ptr, name, position);
	}
	public void setAssetPlayerPlayState(String name, boolean playState) {
		setAssetPlayerPlayState(ptr, name, playState);
	}
	public void setAssetPlayerStop(String name) {
		setAssetPlayerStop(ptr, name);
	}
	@Override
	public void close () {
		AppNativeAPI.destroySoundEngine(ptr);
	}
}
