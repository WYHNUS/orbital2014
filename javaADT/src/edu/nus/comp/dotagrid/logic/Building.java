package edu.nus.comp.dotagrid.logic;

public class Building  extends Character {

	public static int buildingBountyExp = 0;
	public static int buildingStartingSight = 7;
	public static int buildingStartingMP = 0;
	public static int buildingPhysicalAttack = 0;
	public static int buildingPhysicalAttackArea = 0;
	public static int buildingPhysicalAttackSpeed = 0;
	public static int buildingMagicResistance = 100;
	public static int buildingMovementSpeed = -99999;
	public static int buildingActionPoint = 0;
	
	public Building(String name, int bountyMoney, int startingHP, double startingPhysicalDefence, int teamNumber) {
		
		super(name, bountyMoney, buildingBountyExp, buildingStartingSight, startingHP, buildingStartingMP,
				buildingPhysicalAttack, buildingPhysicalAttackArea,
				buildingPhysicalAttackSpeed, startingPhysicalDefence,
				buildingMagicResistance, buildingMovementSpeed, buildingActionPoint, teamNumber);


		this.setCharacterImage("buildings", this.getName());
	}

}
