package edu.nus.comp.dotagrid.logic;

public class Tower extends Character {
	public static int startingMovementSpeed = -99999;
	public static int startingMagicResistance = 100;
	public static int towerStartingSight = 14;
	public static int actionPoint = 50;

	public Tower(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea,
			double startingPhysicalAttackSpeed, double startingPhysicalDefence, int teamNumber) {
		
		super(name, bountyMoney, bountyExp, towerStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, actionPoint, teamNumber);
		// TODO Auto-generated constructor stub
	}

}
