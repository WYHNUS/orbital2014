package edu.nus.comp.dotagridandroid.appsupport;

import org.json.*;
import java.util.*;
import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.Closeable;

public class ExtensionEngine implements Closeable {
	static {
		System.loadLibrary("appsupport");
	}
	private long ptr;
	private GameState state;
	private static final native void loadScript(long ptr, String script);
	private static final native void execute(long ptr);
	private final native void applyRule(long ptr, String name, String options);
	
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
	
	public void attachGameState(GameState state) {
		this.state = state;
	}
	
	public void applyRule(String name, String options) {
		applyRule(ptr, name, options);
	}
	
	private void notifyUpdate(String updates) {
		try {
			JSONObject object = new JSONObject(updates);
			Map<String, Object> map = MapJsonConverter.JsonToMap(object);
			state.notifyUpdate(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
