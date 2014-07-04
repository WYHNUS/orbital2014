package edu.nus.comp.dotagridandroid.appsupport;
import java.util.*;

import org.json.*;
public final class JsonConverter {
	public static JSONObject MapToJson (Map map) throws JSONException {
		if (map == null)
			return null;
		JSONObject obj = new JSONObject();
		for (Object key : map.keySet())
			if (key instanceof String) {
				Object value = map.get(key);
				if (value == null)
					obj.put((String) key, null);
				else if (value instanceof Map)
					obj.put((String) key, MapToJson((Map) value));
				
				else if (value instanceof Set)
					obj.put((String) key, SetToJson((Set) value));
				else if (value instanceof List)
					obj.put((String) key, ArrayToJson((List) value));
				else if (value instanceof Object[])
					obj.put((String) key, ArrayToJson((Object[]) value));
				else if (value instanceof int[])
					obj.put((String) key, ArrayToJson((int[]) value));
				else if (value instanceof short[])
					obj.put((String) key, ArrayToJson((short[]) value));
				else if (value instanceof byte[])
					obj.put((String) key, ArrayToJson((byte[]) value));
				else if (value instanceof char[])
					obj.put((String) key, ArrayToJson((char[]) value));
				else if (value instanceof long[])
					obj.put((String) key, ArrayToJson((long[]) value));
				else if (value instanceof boolean[])
					obj.put((String) key, ArrayToJson((boolean[]) value));
				else if (value instanceof float[])
					obj.put((String) key, ArrayToJson((float[]) value));
				else if (value instanceof double[])
					obj.put((String) key, ArrayToJson((double[]) value));
				
				else if (value instanceof Integer)
					obj.put((String) key, (Integer) value);
				else if (value instanceof Long)
					obj.put((String) key, (Long) value);
				else if (value instanceof Float)
					obj.put((String) key, ((Number) value).doubleValue());
				else if (value instanceof Double)
					obj.put((String) key, (Double) value);
				else if (value instanceof Short)
					obj.put((String) key, ((Number) value).intValue());
				else if (value instanceof Boolean)
					obj.put((String) key, ((Boolean) value).booleanValue());
				else if (value instanceof Byte)
					obj.put((String) key, ((Byte) value).intValue());
				else if (value instanceof String)
					obj.put((String) key, (String) value);
			}
		return obj;
	}
	public static JSONArray SetToJson (Set set) throws JSONException {
		JSONArray arr = new JSONArray();
		for (Object value : set)
			if (value == null)
				continue;
			else if (value instanceof Map)
				arr.put(MapToJson((Map) value));
		
			else if (value instanceof Set)
				arr.put(SetToJson((Set) value));
			else if (value instanceof List)
				arr.put(ArrayToJson((List) value));
			else if (value instanceof Object[])
				arr.put(ArrayToJson((Object[]) value));
			else if (value instanceof int[])
				arr.put(ArrayToJson((int[]) value));
			else if (value instanceof short[])
				arr.put(ArrayToJson((short[]) value));
			else if (value instanceof byte[])
				arr.put(ArrayToJson((byte[]) value));
			else if (value instanceof char[])
				arr.put(ArrayToJson((char[]) value));
			else if (value instanceof long[])
				arr.put(ArrayToJson((long[]) value));
			else if (value instanceof boolean[])
				arr.put(ArrayToJson((boolean[]) value));
		
			else if (value instanceof Integer)
				arr.put((Integer) value);
			else if (value instanceof Long)
				arr.put((Long) value);
			else if (value instanceof Float)
				arr.put(((Number) value).doubleValue());
			else if (value instanceof Double)
				arr.put((Double) value);
			else if (value instanceof Short)
				arr.put(((Number) value).intValue());
			else if (value instanceof Boolean)
				arr.put(((Boolean) value).booleanValue());
			else if (value instanceof Byte)
				arr.put(((Byte) value).intValue());
			else if (value instanceof String)
				arr.put((String) value);
		return arr;
	}
	public static JSONArray ArrayToJson (List list) throws JSONException {
		JSONArray arr = new JSONArray();
		for (Object value : list)
			if (value == null)
				continue;
			else if (value instanceof Map)
				arr.put(MapToJson((Map) value));
		
			else if (value instanceof Set)
				arr.put(SetToJson((Set) value));
			else if (value instanceof List)
				arr.put(ArrayToJson((List) value));
			else if (value instanceof Object[])
				arr.put(ArrayToJson((Object[]) value));
			else if (value instanceof int[])
				arr.put(ArrayToJson((int[]) value));
			else if (value instanceof short[])
				arr.put(ArrayToJson((short[]) value));
			else if (value instanceof byte[])
				arr.put(ArrayToJson((byte[]) value));
			else if (value instanceof char[])
				arr.put(ArrayToJson((char[]) value));
			else if (value instanceof long[])
				arr.put(ArrayToJson((long[]) value));
			else if (value instanceof boolean[])
				arr.put(ArrayToJson((boolean[]) value));
		
			else if (value instanceof Integer)
				arr.put((Integer) value);
			else if (value instanceof Long)
				arr.put((Long) value);
			else if (value instanceof Float)
				arr.put(((Number) value).doubleValue());
			else if (value instanceof Double)
				arr.put((Double) value);
			else if (value instanceof Short)
				arr.put(((Number) value).intValue());
			else if (value instanceof Boolean)
				arr.put(((Boolean) value).booleanValue());
			else if (value instanceof Byte)
				arr.put(((Byte) value).intValue());
			else if (value instanceof String)
				arr.put((String) value);
		return arr;
	}
	public static JSONArray ArrayToJson (Object[] list) throws JSONException {
		JSONArray arr = new JSONArray();
		for (Object value : list)
			if (value == null)
				continue;
			else if (value instanceof Map)
				arr.put(MapToJson((Map) value));
		
			else if (value instanceof Set)
				arr.put(SetToJson((Set) value));
			else if (value instanceof List)
				arr.put(ArrayToJson((List) value));
			else if (value instanceof Object[])
				arr.put(ArrayToJson((Object[]) value));
			else if (value instanceof int[])
				arr.put(ArrayToJson((int[]) value));
			else if (value instanceof short[])
				arr.put(ArrayToJson((short[]) value));
			else if (value instanceof byte[])
				arr.put(ArrayToJson((byte[]) value));
			else if (value instanceof char[])
				arr.put(ArrayToJson((char[]) value));
			else if (value instanceof long[])
				arr.put(ArrayToJson((long[]) value));
			else if (value instanceof boolean[])
				arr.put(ArrayToJson((boolean[]) value));
		
			else if (value instanceof Integer)
				arr.put((Integer) value);
			else if (value instanceof Long)
				arr.put((Long) value);
			else if (value instanceof Float)
				arr.put(((Number) value).doubleValue());
			else if (value instanceof Double)
				arr.put((Double) value);
			else if (value instanceof Short)
				arr.put(((Number) value).intValue());
			else if (value instanceof Boolean)
				arr.put(((Boolean) value).booleanValue());
			else if (value instanceof Byte)
				arr.put(((Byte) value).intValue());
			else if (value instanceof String)
				arr.put((String) value);
		return arr;
	}
	public static JSONArray ArrayToJson (int[] list) {
		JSONArray arr = new JSONArray();
		for (int value : list)
			arr.put(value);
		return arr;
	}
	public static JSONArray ArrayToJson (short[] list) {
		JSONArray arr = new JSONArray();
		for (short value : list)
			arr.put((int) value);
		return arr;
	}
	public static JSONArray ArrayToJson (long[] list) {
		JSONArray arr = new JSONArray();
		for (long value : list)
			arr.put(value);
		return arr;
	}
	public static JSONArray ArrayToJson (boolean[] list) {
		JSONArray arr = new JSONArray();
		for (boolean value : list)
			arr.put(value);
		return arr;
	}
	public static JSONArray ArrayToJson (byte[] list) {
		JSONArray arr = new JSONArray();
		for (byte value : list)
			arr.put((int) value);
		return arr;
	}
	public static JSONArray ArrayToJson (char[] list) {
		JSONArray arr = new JSONArray();
		for (char value : list)
			arr.put((int) value);
		return arr;
	}
	public static JSONArray ArrayToJson (float[] list) {
		JSONArray arr = new JSONArray();
		for (float value : list)
			arr.put((Double) (double) value);
		return arr;
	}
	public static JSONArray ArrayToJson (double[] list) {
		JSONArray arr = new JSONArray();
		for (double value : list)
			arr.put((Double) value);
		return arr;
	}
	
	// json to java
	public static Map<String, Object> JsonToMap (JSONObject obj) throws JSONException {
		if (obj == null)
			return null;
		Map<String, Object> map = new HashMap<>();
		JSONArray names = obj.names();
		for (int i = 0; i < names.length(); i++) {
			String key = names.getString(i);
			Object value = obj.get(key);
			if (value == null)
				continue;
			else if (value instanceof JSONObject)
				map.put(key, JsonToMap((JSONObject) value));
			else if (value instanceof JSONArray)
				map.put(key, JsonToArray((JSONArray) value));
			else
				map.put(key, value);
		}
		return Collections.unmodifiableMap(map);
	}
	public static List<Object> JsonToArray (JSONArray arr) throws JSONException {
		if (arr == null)
			return null;
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			Object value = arr.get(i);
			if (value == null)
				continue;
			else if (value instanceof JSONObject)
				list.add(JsonToMap((JSONObject) value));
			else if (value instanceof JSONArray)
				list.add(JsonToArray((JSONArray) value));
			else
				list.add(value);
		}
		return Collections.unmodifiableList(list);
	}
}
