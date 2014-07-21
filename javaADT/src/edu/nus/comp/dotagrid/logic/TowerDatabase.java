package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;

public class TowerDatabase {
	
	public static int towerActionPoint = 100;
	public static int fountainActionPoint = 200;
	
	// sentinel tower position :
	public static int[] sentinelTopTower1Position = {8, 32};
	public static int[] sentinelMidTower1Position = {45, 53};
	public static int[] sentinelBotTower1Position = {77, 90};
	
	public static int[] sentinelTopTower2Position = {8, 49};
	public static int[] sentinelMidTower2Position = {35, 63};
	public static int[] sentinelBotTower2Position = {54, 90};
	
	public static int[] sentinelTopTower3Position = {8, 66};
	public static int[] sentinelMidTower3Position = {25, 73};
	public static int[] sentinelBotTower3Position = {31, 90};
	
	public static int[] sentinelLeftTower4Position = {14, 80};
	public static int[] sentinelRightTower4Position = {18, 84};
	
	public static int[] sentinelFountainPosition = {5, 93};
	
	
	// scourge tower position :
	public static int[] scourgeTopTower1Position = {22, 9};
	public static int[] scourgeMidTower1Position = {54, 46};
	public static int[] scourgeBotTower1Position = {91, 66};
	
	public static int[] scourgeTopTower2Position = {45, 9};
	public static int[] scourgeMidTower2Position = {64, 36};
	public static int[] scourgeBotTower2Position = {91, 50};
	
	public static int[] scourgeTopTower3Position = {68, 9};
	public static int[] scourgeMidTower3Position = {74, 26};
	public static int[] scourgeBotTower3Position = {91, 33};
	
	public static int[] scourgeLeftTower4Position = {81, 15};
	public static int[] scourgeRightTower4Position = {85, 19};
	
	public static int[] scourgeFountainPosition = {94, 6};
	
	
	public TowerDatabase(){}
	
	public static void initializeAllTowers(){
		System.out.println("initialize towers!");
		initializeSentinelTowers();
		initializeScourgeTowers();
	}
	
	
	private static void initializeScourgeTowers() {
		// initialize scourge towers on grid frame
		
		// initialize scourge top line Towers
		createTower(createScourgeTower1(), scourgeTopTower1Position);
				
		Tower topTower2 = createScourgeTower2();
		ArrayList<int[]> protectionTopList2 = new ArrayList<int[]>();
		protectionTopList2.add(scourgeTopTower1Position);
		topTower2.setProtectionPosList(protectionTopList2);
		createTower(topTower2, scourgeTopTower2Position);
				
		Tower topTower3 = createScourgeTower3();
		ArrayList<int[]> protectionTopList3 = new ArrayList<int[]>();
		protectionTopList3.add(scourgeTopTower2Position);
		topTower3.setProtectionPosList(protectionTopList3);
		createTower(topTower3, scourgeTopTower3Position);
				
		// initialize scourge middle line Towers
		createTower(createScourgeTower1(), scourgeMidTower1Position);
				
		Tower midTower2 = createScourgeTower2();
		ArrayList<int[]> protectionMidList2 = new ArrayList<int[]>();
		protectionMidList2.add(scourgeMidTower1Position);
		midTower2.setProtectionPosList(protectionMidList2);
		createTower(midTower2, scourgeMidTower2Position);
				
		Tower midTower3 = createScourgeTower3();
		ArrayList<int[]> protectionMidList3 = new ArrayList<int[]>();
		protectionMidList3.add(scourgeMidTower2Position);
		midTower3.setProtectionPosList(protectionMidList3);
		createTower(midTower3, scourgeMidTower3Position);
		
		// initialize scourge bottom line Towers
		createTower(createScourgeTower1(), scourgeBotTower1Position);
		
		Tower botTower2 = createScourgeTower2();
		ArrayList<int[]> protectionBotList2 = new ArrayList<int[]>();
		protectionBotList2.add(scourgeBotTower1Position);
		botTower2.setProtectionPosList(protectionBotList2);
		createTower(botTower2, scourgeBotTower2Position);
		
		Tower botTower3 = createScourgeTower3();
		ArrayList<int[]> protectionBotList3 = new ArrayList<int[]>();
		protectionBotList3.add(scourgeBotTower2Position);
		botTower3.setProtectionPosList(protectionBotList3);
		createTower(botTower3, scourgeBotTower3Position);
		
		// initialize scourge left and right towers, fountain
		createTower(createScourgeTower4(), scourgeLeftTower4Position);
		createTower(createScourgeTower4(), scourgeRightTower4Position);
		createTower(createScourgeFountain(), scourgeFountainPosition);
		
	}	

	private static void initializeSentinelTowers() {
		// initialize sentinel towers on grid frame
		
		// initialize sentinel top line Towers
		createTower(createSentinelTower1(), sentinelTopTower1Position);
		
		Tower topTower2 = createSentinelTower2();
		ArrayList<int[]> protectionTopList2 = new ArrayList<int[]>();
		protectionTopList2.add(sentinelTopTower1Position);
		topTower2.setProtectionPosList(protectionTopList2);
		createTower(topTower2, sentinelTopTower2Position);
		
		Tower topTower3 = createSentinelTower3();
		ArrayList<int[]> protectionTopList3 = new ArrayList<int[]>();
		protectionTopList3.add(sentinelTopTower2Position);
		topTower3.setProtectionPosList(protectionTopList3);
		createTower(topTower3, sentinelTopTower3Position);
		
		// initialize sentinel middle line Towers
		createTower(createSentinelTower1(), sentinelMidTower1Position);
		
		Tower midTower2 = createSentinelTower2();
		ArrayList<int[]> protectionMidList2 = new ArrayList<int[]>();
		protectionMidList2.add(sentinelMidTower1Position);
		midTower2.setProtectionPosList(protectionMidList2);
		createTower(midTower2, sentinelMidTower2Position);
		
		Tower midTower3 = createSentinelTower3();
		ArrayList<int[]> protectionMidList3 = new ArrayList<int[]>();
		protectionMidList3.add(sentinelMidTower2Position);
		midTower3.setProtectionPosList(protectionMidList3);
		createTower(midTower3, sentinelMidTower3Position);
		
		// initialize sentinel bottom line Towers
		createTower(createSentinelTower1(), sentinelBotTower1Position);
				
		Tower botTower2 = createSentinelTower2();
		ArrayList<int[]> protectionBotList2 = new ArrayList<int[]>();
		protectionBotList2.add(sentinelBotTower1Position);
		botTower2.setProtectionPosList(protectionBotList2);
		createTower(botTower2, sentinelBotTower2Position);
				
		Tower botTower3 = createSentinelTower3();
		ArrayList<int[]> protectionBotList3 = new ArrayList<int[]>();
		protectionBotList3.add(sentinelBotTower2Position);
		botTower3.setProtectionPosList(protectionBotList3);
		createTower(botTower3, sentinelBotTower3Position);
		
		// initialize sentinel left and right towers, fountain
		createTower(createSentinelTower4(), sentinelLeftTower4Position);
		createTower(createSentinelTower4(), sentinelRightTower4Position);
		createTower(createSentinelFountain(), sentinelFountainPosition);
		
	}

	private static void createTower(Tower tower, int[] pos) {
		// create a new tower on grid button map 
		GridFrame.gridButtonMap[pos[0]][pos[1]] = new GridButton(tower);
	}

	public static Tower createSentinelTower1() {
		Tower sentinelTower1 = new Tower("Sentinel Tower 1", 250, 1300, 0, 120, 5, 1.2, 18, towerActionPoint, 1);
		sentinelTower1.setCharacterImage("towers", "Sentinel Tower");
		return sentinelTower1;
	}
	
	public static Tower createSentinelTower2() {
		Tower sentinelTower2 = new Tower("Sentinel Tower 2", 300, 1600, 0, 140, 5, 1.2, 18, towerActionPoint, 1);
		sentinelTower2.setCharacterImage("towers", "Sentinel Tower");
		return sentinelTower2;
	}
	
	public static Tower createSentinelTower3() {
		Tower sentinelTower3 = new Tower("Sentinel Tower 3", 350, 1600, 0, 180, 5, 1.2, 25, towerActionPoint, 1);
		sentinelTower3.setCharacterImage("towers", "Sentinel Tower");
		return sentinelTower3;
	}
	
	public static Tower createSentinelTower4() {
		Tower sentinelTower4 = new Tower("Sentinel Tower 4", 400, 1600, 0, 180, 5, 1.2, 30, towerActionPoint, 1);
		sentinelTower4.setCharacterImage("towers", "Sentinel Tower");
		return sentinelTower4;
	}
	
	public static Tower createScourgeTower1() {
		Tower scourgeTower1 = new Tower("Scourge Tower 1", 250, 1300, 0, 120, 5, 1.2, 18, towerActionPoint, 2);
		scourgeTower1.setCharacterImage("towers", "Scourge Tower");
		return scourgeTower1;
	}
	
	public static Tower createScourgeTower2() {
		Tower scourgeTower2 = new Tower("Scourge Tower 2", 300, 1600, 0, 140, 5, 1.2, 18, towerActionPoint, 2);
		scourgeTower2.setCharacterImage("towers", "Scourge Tower");
		return scourgeTower2;
	}
	
	public static Tower createScourgeTower3() {
		Tower scourgeTower3 = new Tower("Scourge Tower 3", 350, 1600, 0, 180, 5, 1.2, 25, towerActionPoint, 2);
		scourgeTower3.setCharacterImage("towers", "Scourge Tower");
		return scourgeTower3;
	}
	
	public static Tower createScourgeTower4() {
		Tower scourgeTower4 = new Tower("Scourge Tower 4", 400, 1600, 0, 180, 5, 1.2, 30, towerActionPoint, 2);
		scourgeTower4.setCharacterImage("towers", "Scourge Tower");
		return scourgeTower4;
	}
	
	public static Tower createSentinelFountain() {
		Tower sentinelFountain = new Tower("Sentinel Fountain", 1000, 50000, 0, 190, 7, Character.MAX_PHYSICAL_ATTACK_SPEED, 
				15, fountainActionPoint, 1);
		sentinelFountain.setCharacterImage("towers", "Sentinel Fountain");
		return sentinelFountain;
	}
	
	public static Tower createScourgeFountain() {
		Tower scourgeFountain = new Tower("Scourge Fountain", 1000, 50000, 0, 190, 7, Character.MAX_PHYSICAL_ATTACK_SPEED, 
				15, fountainActionPoint, 2);
		scourgeFountain.setCharacterImage("towers", "Scourge Fountain");
		return scourgeFountain;
	}
}
