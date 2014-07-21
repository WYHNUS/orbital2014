package edu.nus.comp.dotagridandroid.logic;

public class Barrack extends GameCharacter {
	private int front;
	private String type;
	
	public Barrack() {
		setObjectType(GAMEOBJECT_TYPE_BARRACK);
	}
	
	public int getFront() {
		return front;
	}
	
	public void setFront(int front) {
		this.front = front;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
