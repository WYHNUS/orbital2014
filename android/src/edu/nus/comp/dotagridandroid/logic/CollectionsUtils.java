package edu.nus.comp.dotagridandroid.logic;

import java.util.*;

public class CollectionsUtils {

	public static void deepCopy(Map<String, Object> dst, Map<String, Object> src) {
		if (src == null || dst == null)
			return;
		for (Map.Entry<String, Object> entry : src.entrySet())
			if (entry.getValue() == null)
				continue;
			else if (entry.getValue() instanceof Map) {
				Map<String, Object> map = new HashMap<>();
				deepCopy(map, (Map<String, Object>) entry.getValue());
				dst.put(entry.getKey(), map);
			} else if (entry.getValue() instanceof List) {
				List list = new ArrayList<>();
				deepCopy(list, (List) entry.getValue());
				dst.put(entry.getKey(), list);
			} else if (entry.getValue() instanceof Set) {
				Set set = new HashSet<>();
				deepCopy(set, (Set) entry.getValue());
				dst.put(entry.getKey(), set);
			} else
				dst.put(entry.getKey(), entry.getValue());
	}
	
	public static void deepCopy(List dst, List src) {
		if (src == null || dst == null)
			return;
		for (Object entry : src)
			if (entry == null)
				continue;
			else if (entry instanceof Map) {
				Map<String, Object> map = new HashMap<>();
				deepCopy(map, (Map<String, Object>) entry);
				dst.add(map);
			} else if (entry instanceof List) {
				List list = new ArrayList<>();
				deepCopy(list, (List) entry);
				dst.add(list);
			} else if (entry instanceof Set) {
				Set set = new HashSet<>();
				deepCopy(set, (Set) entry);
				dst.add(set);
			} else
				dst.add(entry);
	}
	
	public static void deepCopy(Set dst, Set src) {
		if (src == null || dst == null)
			return;
		for (Object entry : src)
			if (entry == null)
				continue;
			else if (entry instanceof Map) {
				Map<String, Object> map = new HashMap<>();
				deepCopy(map, (Map<String, Object>) entry);
				dst.add(map);
			} else if (entry instanceof List) {
				List list = new ArrayList<>();
				deepCopy(list, (List) entry);
				dst.add(list);
			} else if (entry instanceof Set) {
				Set set = new HashSet<>();
				deepCopy(set, (Set) entry);
				dst.add(set);
			} else
				dst.add(entry);
	}

}
