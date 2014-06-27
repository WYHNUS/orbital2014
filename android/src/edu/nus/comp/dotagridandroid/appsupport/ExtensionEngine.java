package edu.nus.comp.dotagridandroid.appsupport;

import edu.nus.comp.dotagridandroid.Closeable;

public class ExtensionEngine implements Closeable {
	static {
		System.loadLibrary("appsupport");
	}
	private long ptr;
	private static final native void loadScript(long ptr, String script);
	private static final native void execute(long ptr);
	protected ExtensionEngine(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public void close () {
		AppNativeAPI.destroyExtensionEngine(ptr);
	}
	
	public void loadScript(String script) {
		loadScript(ptr, script);
	}
	
	public void execute() {
		execute(ptr);
	}
}
