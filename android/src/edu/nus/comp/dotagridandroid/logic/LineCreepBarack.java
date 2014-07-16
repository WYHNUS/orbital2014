package edu.nus.comp.dotagridandroid.logic;

public class LineCreepBarack extends GameCharacter {
	
//	private int 

	private int front;
	public LineCreepBarack(String name, int bountyMoney, int bountyExp, int sight, int startingHP, int startingMP, int maxActionPoint, int teamNumber) {
		super(name, bountyMoney, bountyExp, sight, startingHP, startingMP, 0, 0, 0, 0, 0, 0, maxActionPoint, teamNumber);
	}
	
	public LineCreepBarack(LineCreepBarack that) {
		super (that);
	}
	
	public int getFront() {
		return front;
	}
	
	public void setFront(int front) {
		this.front = front;
	}
}
