package edu.nus.comp.dotagridandroid.appsupport;

import org.json.*;

import java.util.*;

import edu.nus.comp.dotagridandroid.logic.*;
import edu.nus.comp.dotagridandroid.Closeable;

public final class ExtensionEngine implements Closeable {
	static {
		System.loadLibrary("appsupport");
	}
	private long ptr;
	private GameState state;
	private static native void loadScript(long ptr, String script);
	private static native void execute(long ptr);
	private native void applyRule(long ptr, String character, String action, String options);
	private native void automate(long ptr, String character);
	
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
	
	public void applyRule(String character, String action, String options) {
		applyRule(ptr, character, action, options);
	}
	
	public void automate(String character) {
		automate(ptr, character);
	}
	
	private void notifyUpdate(String updates) {
		try {
			JSONObject object = new JSONObject(updates);
			Map<String, Object> map = JsonConverter.JsonToMap(object);
			state.notifyUpdate(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void turnNextRound() {
		state.turnNextRound();
	}
	
	private String getCharacterPosition(String character) {
		return JsonConverter.ArrayToJson(state.getCharacterPosition(character)).toString();
	}
	
	private void setCharacterPosition(String character, String position) {
		try {
			System.out.println("setCharacterPosition JAVA called");
			JSONArray array = new JSONArray(position);
			if (array.length() != 2)
				return;
			int[] pos = {array.getInt(0), array.getInt(1)};
			state.setCharacterPosition(character, pos);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private String getCharacterProperty(String character, String property) {
		Object value = state.getCharacterProperty(character, property);
		if (value == null)
			return "null";
		try {
			return JsonConverter.MapToJson(Collections.singletonMap("value", value)).get("value").toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "null";
		}
	}
	
	private void setCharacterProperty(String character, String property, String value) {
		try {
			Object val = new JSONTokener(value).nextValue();
			if (val instanceof JSONObject)
				state.setCharacterProperty(character, property, JsonConverter.JsonToMap((JSONObject) val));
			else if (val instanceof JSONArray)
				state.setCharacterProperty(character, property, JsonConverter.JsonToArray((JSONArray) val));
			else
				state.setCharacterProperty(character, property, val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getSelectedGrid() {
		return JsonConverter.ArrayToJson(state.getChosenGrid()).toString();
	}
}
