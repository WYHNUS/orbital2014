package edu.nus.comp.dotagridandroid.logic;
import java.util.*;
public class GameObject {
	public static final int GAMEOBJECT_TYPE_ROUNDFLAG = 0;
	public static final int GAMEOBJECT_TYPE_OBJECT = 1;
	public static final int GAMEOBJECT_TYPE_CHARACTER = 2;
	public static final int GAMEOBJECT_TYPE_HERO = 3;
	public static final int GAMEOBJECT_TYPE_LINECREEP = 4;
	public static final int GAMEOBJECT_TYPE_TOWER = 5;
	public static final int GAMEOBJECT_TYPE_TREE = 6;
//	public static final int GAMEOBJECT_TYPE_LINECREEPBARACK = 6;
	private final Map<String, Object> extendedProperties = new HashMap<>();
	private int type;
	public GameObject(int type) {
		this.type = type;
	}
	public GameObject(GameObject obj) {
		this.type = obj.type;
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
	
	public int getObjectType() {
		return type;
	}
	
	protected void setObjectType(int type) {
		this.type = type;
	}
}
