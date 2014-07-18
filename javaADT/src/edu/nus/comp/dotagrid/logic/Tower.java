package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;

public class Tower extends Character {
	
	public static int towerBountyExp = 0;
	public static int startingMovementSpeed = -99999;
	public static int startingMagicResistance = 100;
	public static int towerStartingSight = 8;
	
	private ArrayList<int[]> protectionPosList = new ArrayList<int[]>();
	
	public static final int TOWER_ATTACK_PRIORITY = 7;

	public Tower(String name, int bountyMoney, int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, int actionPoint, int teamNumber) {
		
		super(name, bountyMoney, towerBountyExp, towerStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, actionPoint, TOWER_ATTACK_PRIORITY, teamNumber);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<int[]> getProtectionPosList() {
		return protectionPosList;
	}

	public void setProtectionPosList(ArrayList<int[]> protectionPosList) {
		this.protectionPosList = protectionPosList;
	}

}
