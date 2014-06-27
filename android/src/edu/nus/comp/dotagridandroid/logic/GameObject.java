package edu.nus.comp.dotagridandroid.logic;
import java.util.*;
public class GameObject {
	private final Map<String, Object> extendedProperties = new HashMap<>();
	public GameObject() {}
	public GameObject(GameObject obj) {
		CollectionsUtils.deepCopy(extendedProperties, obj.extendedProperties);
	}
	
	public void setExtendedProperty(String name, Object value) {
		if (value == null)
			extendedProperties.remove(name);
		else
			extendedProperties.put(name, value);
	}
	
	public Object getExtendedProperty (String name) {
		return extendedProperties.get(name);
	}
}
