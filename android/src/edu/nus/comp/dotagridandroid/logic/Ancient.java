package edu.nus.comp.dotagridandroid.logic;

public class Ancient extends GameCharacter {
	private int front, order;
	
	public Ancient() {
		setObjectType(GAMEOBJECT_TYPE_ANCIENT);
	}

	public int getFront() {
		return front;
	}

	public void setFront(int front) {
		this.front = front;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
}
