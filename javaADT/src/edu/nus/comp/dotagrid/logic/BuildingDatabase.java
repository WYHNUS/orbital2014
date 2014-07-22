package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BuildingDatabase {

	// count for number of barracks which been destroyed
	public static int destroyedSentinelBarrackNumber = 0;
	public static int destroyedScourgeBarrackNumber = 0;

	// sentinel buildings
	public static int sentinelAncientXPos = 14;
	public static int sentinelAncientYPos = 84;
	public static int[] sentinelAncientPos = {14, 84};

	public static int sentinelTopMeleeBarrackXPos = 5;
	public static int sentinelTopMeleeBarrackYPos = 68;
	public static boolean isDestroyedSentinelTopMeleeBarrack = false;

	public static int sentinelTopRangedBarrackXPos = 11; 
	public static int sentinelTopRangedBarrackYPos = 68;
	public static boolean isDestroyedSentinelTopRangedBarrack = false;

	public static int sentinelMidMeleeBarrackXPos = 20; 
	public static int sentinelMidMeleeBarrackYPos = 72;
	public static boolean isDestroyedSentinelMidMeleeBarrack = false;

	public static int sentinelMidRangedBarrackXPos = 26; 
	public static int sentinelMidRangedBarrackYPos = 78;
	public static boolean isDestroyedSentinelMidRangedBarrack = false;

	public static int sentinelBotMeleeBarrackXPos = 29; 
	public static int sentinelBotMeleeBarrackYPos = 87;
	public static boolean isDestroyedSentinelBotMeleeBarrack = false;

	public static int sentinelBotRangedBarrackXPos = 29;
	public static int sentinelBotRangedBarrackYPos = 93;
	public static boolean isDestroyedSentinelBotRangedBarrack = false;


	// scourge buildings
	public static int scourgeAncientXPos = 85;
	public static int scourgeAncientYPos = 15;
	public static int[] scourgeAncientPos = {85, 15};

	public static int scourgeTopMeleeBarrackXPos = 70;
	public static int scourgeTopMeleeBarrackYPos = 6;
	public static boolean isDestroyedScourgeTopMeleeBarrack = false;

	public static int scourgeTopRangedBarrackXPos = 70;
	public static int scourgeTopRangedBarrackYPos = 12;
	public static boolean isDestroyedScourgeTopRangedBarrack = false;

	public static int scourgeMidMeleeBarrackXPos = 73;
	public static int scourgeMidMeleeBarrackYPos = 21;
	public static boolean isDestroyedScourgeMidMeleeBarrack = false;

	public static int scourgeMidRangedBarrackXPos = 79;
	public static int scourgeMidRangedBarrackYPos = 27;
	public static boolean isDestroyedScourgeMidRangedBarrack = false;

	public static int scourgeBotMeleeBarrackXPos = 88;
	public static int scourgeBotMeleeBarrackYPos = 31;
	public static boolean isDestroyedScourgeBotMeleeBarrack = false;

	public static int scourgeBotRangedBarrackXPos = 94; 
	public static int scourgeBotRangedBarrackYPos = 31;
	public static boolean isDestroyedScourgeBotRangedBarrack = false;


	public BuildingDatabase(){}

	public static void initializeAllBuildings(){
		System.out.println("initialize buildings!");
		initializeSentinelBuildings();
		initializeScourgeBuildings();
	}

	private static void initializeScourgeBuildings() {
		// initialize scourge buildings

		// top barracks
		ArrayList<int[]> protectionTopBarrackList = new ArrayList<int[]>();
		protectionTopBarrackList.add(TowerDatabase.scourgeTopTower3Position);

		Building topMeleeBarrack = createScourgeMeleeBarrack();
		topMeleeBarrack.setProtectionPosList(protectionTopBarrackList);
		createBuilding(topMeleeBarrack, scourgeTopMeleeBarrackXPos, scourgeTopMeleeBarrackYPos);

		Building topRangedBarrack = createScourgeRangedBarrack();
		topRangedBarrack.setProtectionPosList(protectionTopBarrackList);
		createBuilding(topRangedBarrack, scourgeTopRangedBarrackXPos, scourgeTopRangedBarrackYPos);

		// middle barracks
		ArrayList<int[]> protectionMidBarrackList = new ArrayList<int[]>();
		protectionMidBarrackList.add(TowerDatabase.scourgeMidTower3Position);

		Building midMeleeBarrack = createScourgeMeleeBarrack();
		midMeleeBarrack.setProtectionPosList(protectionMidBarrackList);
		createBuilding(midMeleeBarrack, scourgeMidMeleeBarrackXPos, scourgeMidMeleeBarrackYPos);

		Building midRangedBarrack = createScourgeRangedBarrack();
		midRangedBarrack.setProtectionPosList(protectionMidBarrackList);
		createBuilding(midRangedBarrack, scourgeMidRangedBarrackXPos, scourgeMidRangedBarrackYPos);

		// bottom barracks
		ArrayList<int[]> protectionBotBarrackList = new ArrayList<int[]>();
		protectionBotBarrackList.add(TowerDatabase.scourgeBotTower3Position);

		Building botMeleeBarrack = createScourgeMeleeBarrack();
		botMeleeBarrack.setProtectionPosList(protectionBotBarrackList);
		createBuilding(botMeleeBarrack, scourgeBotMeleeBarrackXPos, scourgeBotMeleeBarrackYPos);

		Building botRangedBarrack = createScourgeRangedBarrack();
		botRangedBarrack.setProtectionPosList(protectionBotBarrackList);
		createBuilding(botRangedBarrack, scourgeBotRangedBarrackXPos, scourgeBotRangedBarrackYPos);

		// base
		createBuilding(createScourgeAncient(), scourgeAncientXPos, scourgeAncientYPos);
	}

	private static void initializeSentinelBuildings() {
		// initialize sentinel buildings

		// top barracks
		ArrayList<int[]> protectionTopBarrackList = new ArrayList<int[]>();
		protectionTopBarrackList.add(TowerDatabase.sentinelTopTower3Position);

		Building topMeleeBarrack = createSentinelMeleeBarrack();
		topMeleeBarrack.setProtectionPosList(protectionTopBarrackList);
		createBuilding(topMeleeBarrack, sentinelTopMeleeBarrackXPos, sentinelTopMeleeBarrackYPos);

		Building topRangedBarrack = createSentinelRangedBarrack();
		topRangedBarrack.setProtectionPosList(protectionTopBarrackList);
		createBuilding(topRangedBarrack, sentinelTopRangedBarrackXPos, sentinelTopRangedBarrackYPos);

		// middle barracks
		ArrayList<int[]> protectionMidBarrackList = new ArrayList<int[]>();
		protectionMidBarrackList.add(TowerDatabase.sentinelMidTower3Position);

		Building midMeleeBarrack = createSentinelMeleeBarrack();
		midMeleeBarrack.setProtectionPosList(protectionMidBarrackList);
		createBuilding(midMeleeBarrack, sentinelMidMeleeBarrackXPos, sentinelMidMeleeBarrackYPos);

		Building midRangedBarrack = createSentinelRangedBarrack();
		midRangedBarrack.setProtectionPosList(protectionMidBarrackList);
		createBuilding(midRangedBarrack, sentinelMidRangedBarrackXPos, sentinelMidRangedBarrackYPos);

		// bottom barracks
		ArrayList<int[]> protectionBotBarrackList = new ArrayList<int[]>();
		protectionBotBarrackList.add(TowerDatabase.sentinelBotTower3Position);

		Building botMeleeBarrack = createSentinelMeleeBarrack();
		botMeleeBarrack.setProtectionPosList(protectionBotBarrackList);
		createBuilding(botMeleeBarrack, sentinelBotMeleeBarrackXPos, sentinelBotMeleeBarrackYPos);

		Building botRangedBarrack = createSentinelRangedBarrack();
		botRangedBarrack.setProtectionPosList(protectionBotBarrackList);
		createBuilding(botRangedBarrack, sentinelBotRangedBarrackXPos, sentinelBotRangedBarrackYPos);

		// base
		createBuilding(createSentinelAncient(), sentinelAncientXPos, sentinelAncientYPos);
	}

	private static void createBuilding(Building building, int xPos, int yPos) {
		// create a new building on grid button map 
		GridFrame.gridButtonMap[xPos][yPos] = new GridButton(building);		
	}

	public static void isBarracksDestroyed() {
		// check if any of the barracks has been destroyed by the attack action

		// sentinel barracks

		// only check if barrack has not been destroyed
		if (!isDestroyedSentinelTopMeleeBarrack) {
			if (GridFrame.gridButtonMap[sentinelTopMeleeBarrackXPos][sentinelTopMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Top Melee Barrack has been Destroyed!!!");
				isDestroyedSentinelTopMeleeBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}	

		if (!isDestroyedSentinelTopRangedBarrack) {
			if (GridFrame.gridButtonMap[sentinelTopRangedBarrackXPos][sentinelTopRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Top Ranged Barrack has been Destroyed!!!");
				isDestroyedSentinelTopRangedBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}

		if (!isDestroyedSentinelMidMeleeBarrack) {
			if (GridFrame.gridButtonMap[sentinelMidMeleeBarrackXPos][sentinelMidMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Mid Melee Barrack has been Destroyed!!!");
				isDestroyedSentinelMidMeleeBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}

		if (!isDestroyedSentinelMidRangedBarrack) {
			if (GridFrame.gridButtonMap[sentinelMidRangedBarrackXPos][sentinelMidRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Mid Ranged Barrack has been Destroyed!!!");
				isDestroyedSentinelMidRangedBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}

		if (!isDestroyedSentinelBotMeleeBarrack) {
			if (GridFrame.gridButtonMap[sentinelBotMeleeBarrackXPos][sentinelBotMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Bot Melee Barrack has been Destroyed!!!");
				isDestroyedSentinelBotMeleeBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}

		if (!isDestroyedSentinelBotRangedBarrack) {
			if (GridFrame.gridButtonMap[sentinelBotRangedBarrackXPos][sentinelBotRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Sentinel Bot Ranged Barrack has been Destroyed!!!");
				isDestroyedSentinelBotRangedBarrack = true;
				destroyedSentinelBarrackNumber++;
			}
		}

		if (destroyedSentinelBarrackNumber == 6) {
			System.out.println("Scourge will spawn super creeps!!!");
			LineCreep.levelScourge = 30;
		}

		// not possible scenario, report error
		if (destroyedSentinelBarrackNumber > 6) 
			JOptionPane.showMessageDialog(null, "An error has occured for value of [destroyedSentinelBarrackNumber]! = " + destroyedSentinelBarrackNumber);

		// scourge  barracks

		if (!isDestroyedScourgeTopMeleeBarrack) {
			if (GridFrame.gridButtonMap[scourgeTopMeleeBarrackXPos][scourgeTopMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Top Melee Barrack has been Destroyed!!!");
				isDestroyedScourgeTopMeleeBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (!isDestroyedScourgeTopRangedBarrack) {
			if (GridFrame.gridButtonMap[scourgeTopRangedBarrackXPos][scourgeTopRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Top Ranged Barrack has been Destroyed!!!");
				isDestroyedScourgeTopRangedBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (!isDestroyedScourgeMidMeleeBarrack) {
			if (GridFrame.gridButtonMap[scourgeMidMeleeBarrackXPos][scourgeMidMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Mid Melee Barrack has been Destroyed!!!");
				isDestroyedScourgeMidMeleeBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (!isDestroyedScourgeMidRangedBarrack) {
			if (GridFrame.gridButtonMap[scourgeMidRangedBarrackXPos][scourgeMidRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Mid Ranged Barrack has been Destroyed!!!");
				isDestroyedScourgeMidRangedBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (!isDestroyedScourgeBotMeleeBarrack) {
			if (GridFrame.gridButtonMap[scourgeBotMeleeBarrackXPos][scourgeBotMeleeBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Bot Melee Barrack has been Destroyed!!!");
				isDestroyedScourgeBotMeleeBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (!isDestroyedScourgeBotRangedBarrack) {
			if (GridFrame.gridButtonMap[scourgeBotRangedBarrackXPos][scourgeBotRangedBarrackYPos].getCharacter().isAlive() == false){
				System.out.println("Scourge Bot Ranged Barrack has been Destroyed!!!");
				isDestroyedScourgeBotRangedBarrack = true;
				destroyedScourgeBarrackNumber++;
			}
		}

		if (destroyedScourgeBarrackNumber == 6) {
			System.out.println("Sentinel will spawn super creeps!!!");
			LineCreep.levelSentinel = 30;
		}

		// not possible scenario, report error
		if (destroyedScourgeBarrackNumber > 6) 
			JOptionPane.showMessageDialog(null, "An error has occured for value of [destroyedScourgeBarrackNumber]! = " + destroyedScourgeBarrackNumber);


	}


	public static Building createSentinelMeleeBarrack() {
		return new Building("Sentinel Melee Barrack", 0, 1500, 15, 1);
	}

	public static Building createSentinelRangedBarrack() {
		return new Building("Sentinel Ranged Barrack", 0, 1500, 5, 1);
	}

	public static Building createScourgeMeleeBarrack() {
		return new Building("Scourge Melee Barrack", 0, 1500, 15, 2);
	}

	public static Building createScourgeRangedBarrack() {
		return new Building("Scourge Ranged Barrack", 0, 1500, 5, 2);
	}

	public static Building createSentinelAncient() {
		Building sentinelAncient = new Building("Sentinel Ancient", 0, 4250, 15, 1);
		ArrayList<int[]> tempList = new ArrayList<int[]>();
		tempList.add(TowerDatabase.sentinelLeftTower4Position);
		tempList.add(TowerDatabase.sentinelRightTower4Position);
		sentinelAncient.setProtectionPosList(tempList);
		return sentinelAncient;
	}

	public static Building createScourgeAncient() {
		Building scourgeAncient = new Building("Scourge Ancient", 0, 4250, 15, 2);
		ArrayList<int[]> tempList1 = new ArrayList<int[]>();
		tempList1.add(TowerDatabase.scourgeLeftTower4Position);
		tempList1.add(TowerDatabase.scourgeRightTower4Position);
		scourgeAncient.setProtectionPosList(tempList1);
		return scourgeAncient;
	}
}
