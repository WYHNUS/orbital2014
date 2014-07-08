package edu.nus.comp.dotagridandroid.logic;

public class Tower extends GameCharacter {
	
	public static int towerBountyExp = 0;
	public static int startingMovementSpeed = -99999;
	public static int startingMagicResistance = 100;
	public static int towerStartingSight = 8;

	public Tower(String name, int bountyMoney, int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, int actionPoint, int teamNumber) {
		
		super(name, bountyMoney, towerBountyExp, towerStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, actionPoint, teamNumber);
		setObjectType(GAMEOBJECT_TYPE_TOWER);
	}

}
