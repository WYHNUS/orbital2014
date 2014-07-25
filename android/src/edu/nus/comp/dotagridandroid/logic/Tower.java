package edu.nus.comp.dotagridandroid.logic;

public class Tower extends GameCharacter {
	
	public static int towerBountyExp = 0;
	public static int startingMovementSpeed = -99999;
	public static int startingMagicResistance = 100;
	public static int towerStartingSight = 8;
	
	private int front;
	private int order;
	
	public Tower() {
		setObjectType(GAMEOBJECT_TYPE_TOWER);
	}

	public Tower(String name, int bountyMoney, int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, int actionPoint, int teamNumber) {
		
		super(name, bountyMoney, towerBountyExp, towerStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, actionPoint, teamNumber);
		setObjectType(GAMEOBJECT_TYPE_TOWER);
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
