package edu.nus.comp.dotagridandroid.appsupport;

import edu.nus.comp.dotagridandroid.Closeable;

public class SoundEngine implements Closeable{
	static {
		System.loadLibrary("appsupport");
	}
	private static native void prepareBufferQueuePlayer(long ptr);
	private static native void prepareAssetPlayer(long ptr, String name, int fd, long start, long length);
	private long ptr;
	protected SoundEngine(long ptr) {
		this.ptr = ptr;
	}
	public void prepareBufferQueuePlayer() {
		prepareBufferQueuePlayer(ptr);
	}
	@Override
	public void close () {
		AppNativeAPI.destroySoundEngine(ptr);
	}
}
