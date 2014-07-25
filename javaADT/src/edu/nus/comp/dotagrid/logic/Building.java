package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;

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
	
	private ArrayList<int[]> protectionPosList = new ArrayList<int[]>();
	
	public static final int BUILDING_ATTACK_PRIORITY = 9;
	
	public Building(String name, int bountyMoney, int startingHP, double startingPhysicalDefence, int teamNumber) {
		
		super(name, bountyMoney, buildingBountyExp, buildingStartingSight, startingHP, buildingStartingMP,
				buildingPhysicalAttack, buildingPhysicalAttackArea,
				buildingPhysicalAttackSpeed, startingPhysicalDefence,
				buildingMagicResistance, buildingMovementSpeed, buildingActionPoint, BUILDING_ATTACK_PRIORITY, teamNumber);


		this.setCharacterImage("Buildings", this.getName());
	}

	public Building(Building building) {
		super(building.getName(), building.getBountyMoney(), buildingBountyExp, buildingStartingSight, building.getStartingHP(), buildingStartingMP,
				buildingPhysicalAttack, buildingPhysicalAttackArea,
				buildingPhysicalAttackSpeed, building.getStartingPhysicalDefence(),
				buildingMagicResistance, buildingMovementSpeed, buildingActionPoint, BUILDING_ATTACK_PRIORITY, building.getTeamNumber());
		
		this.setProtectionPosList(building.getProtectionPosList());
		this.setCurrentAttackPriority(building.getCurrentAttackPriority());
		this.setCharacterImage("Buildings", this.getName());
	}

	public ArrayList<int[]> getProtectionPosList() {
		return protectionPosList;
	}

	public void setProtectionPosList(ArrayList<int[]> protectionPosList) {
		this.protectionPosList = protectionPosList;
	}

}
