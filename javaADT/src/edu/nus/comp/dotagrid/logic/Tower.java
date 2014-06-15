package edu.nus.comp.dotagrid.logic;

public class Tower extends Character {
	private static int startingMovementSpeed = -99999;
	private static int startingMagicResistance = 100;
	private static int actionPoint = 50;

	public Tower(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea,
			double startingPhysicalAttackSpeed, double startingPhysicalDefence, int teamNumber) {
		
		super(name, bountyMoney, bountyExp, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, actionPoint, teamNumber);
		// TODO Auto-generated constructor stub
	}

}
