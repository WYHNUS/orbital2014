package edu.nus.comp.dotagrid.logic;

public class TowerDatabase {
	public final int TOTAL_TOWER_NUMBER = 10;

	public static int towerActionPoint = 50;
	public static int fountainActionPoint = 200;

	public Tower[] towerDatabase = new Tower[TOTAL_TOWER_NUMBER]; 
	
	// sentinel tower position :
	public static int[] sentinelTopTower1Position = {8, 34};
	public static int[] sentinelMidTower1Position = {45, 53};
	public static int[] sentinelBotTower1Position = {78, 90};
	
	public static int[] sentinelTopTower2Position = {8, 49};
	public static int[] sentinelMidTower2Position = {35, 63};
	public static int[] sentinelBotTower2Position = {55, 90};
	
	public static int[] sentinelTopTower3Position = {8, 66};
	public static int[] sentinelMidTower3Position = {26, 73};
	public static int[] sentinelBotTower3Position = {32, 90};
	
	public static int[] sentinelLeftTower4Position = {14, 80};
	public static int[] sentinelRightTower4Position = {18, 84};
	
	public static int[] sentinelFountainPosition = {5, 93};
	
	
	// scourge tower position :
	public static int[] scourgeTopTower1Position = {21, 9};
	public static int[] scourgeMidTower1Position = {53, 46};
	public static int[] scourgeBotTower1Position = {91, 66};
	
	public static int[] scourgeTopTower2Position = {44, 9};
	public static int[] scourgeMidTower2Position = {63, 36};
	public static int[] scourgeBotTower2Position = {91, 50};
	
	public static int[] scourgeTopTower3Position = {67, 9};
	public static int[] scourgeMidTower3Position = {73, 26};
	public static int[] scourgeBotTower3Position = {91, 33};
	
	public static int[] scourgeLeftTower4Position = {80, 15};
	public static int[] scourgeRightTower4Position = {84, 19};
	
	public static int[] scourgeFountainPosition = {94, 6};
	
	
	public TowerDatabase(){
		/* 
		 * String name, 
		 * int bountyMoney
		 * 
		 * int startingHP, 
		 * int startingMP,
		 * 
		 * double startingPhysicalAttack, 
		 * int startingPhysicalAttackArea,
		 * double startingPhysicalAttackSpeed, 
		 * double startingPhysicalDefence,
		 * 
		 * int teamNumber
		 * 
		*/
		Tower sentinelTower1 = new Tower("Sentinel Tower 1", 250, 1300, 0, 120, 5, 1.0, 18, towerActionPoint, 1);
		sentinelTower1.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower2 = new Tower("Sentinel Tower 2", 300, 1600, 0, 140, 5, 1.0, 18, towerActionPoint, 1);
		sentinelTower2.setCharacterImage("towers", "Sentinel Tower");	
		Tower sentinelTower3 = new Tower("Sentinel Tower 3", 350, 1600, 0, 180, 5, 1.0, 25, towerActionPoint, 1);
		sentinelTower3.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower4 = new Tower("Sentinel Tower 4", 400, 1600, 0, 180, 5, 1.0, 30, towerActionPoint, 1);
		sentinelTower4.setCharacterImage("towers", "Sentinel Tower");
	
		towerDatabase[0] = sentinelTower1;
		towerDatabase[1] = sentinelTower2;
		towerDatabase[2] = sentinelTower3;
		towerDatabase[3] = sentinelTower4;
		
		Tower scourgeTower1 = new Tower("Scourge Tower 1", 250, 1300, 0, 120, 5, 1.0, 18, towerActionPoint, 2);
		scourgeTower1.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower2 = new Tower("Scourge Tower 2", 300, 1600, 0, 140, 5, 1.0, 18, towerActionPoint, 2);
		scourgeTower2.setCharacterImage("towers", "Scourge Tower");	
		Tower scourgeTower3 = new Tower("Scourge Tower 3", 350, 1600, 0, 180, 5, 1.0, 25, towerActionPoint, 2);
		scourgeTower3.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower4 = new Tower("Scourge Tower 4", 400, 1600, 0, 180, 5, 1.0, 30, towerActionPoint, 2);
		scourgeTower4.setCharacterImage("towers", "Scourge Tower");
	
		towerDatabase[4] = scourgeTower1;
		towerDatabase[5] = scourgeTower2;
		towerDatabase[6] = scourgeTower3;
		towerDatabase[7] = scourgeTower4;
		
		
		Tower sentinelFountain = new Tower("Sentinel Fountain", 1000, 50000, 0, 190, 7, Character.MAX_PHYSICAL_ATTACK_SPEED, 
				15, fountainActionPoint, 1);
		sentinelFountain.setCharacterImage("towers", "Sentinel Fountain");
		Tower scourgeFountain = new Tower("Scourge Fountain", 1000, 50000, 0, 190, 7, Character.MAX_PHYSICAL_ATTACK_SPEED, 
				15, fountainActionPoint, 2);
		scourgeFountain.setCharacterImage("towers", "Scourge Fountain");
		
		towerDatabase[8] = sentinelFountain;
		towerDatabase[9] = scourgeFountain;
		
	}
}
