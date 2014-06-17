package edu.nus.comp.dotagrid.logic;

public class BuildingDatabase {
	public static final int TOTAL_BUILDING_NUMBER = 6;
	
	public static Building[] buildingDatabase = new Building[TOTAL_BUILDING_NUMBER];
	
	// sentinel buildings
	public static int[] sentinelBasePosition = {14, 84};
	
	public static int[] sentinelTopMeeleBarrackPosition = {5, 68};
	public static int[] sentinelTopRangedBarrackPosition = {11, 68};
	
	public static int[] sentinelMidMeeleBarrackPosition = {21, 72};
	public static int[] sentinelMidRangedBarrackPosition = {27, 78};
	
	public static int[] sentinelBotMeeleBarrackPosition = {30, 87};
	public static int[] sentinelBotRangedBarrackPosition = {30, 93};
	
	
	// scourge buildings
	public static int[] scourgeBasePosition = {85, 15};
	
	public static int[] scourgeTopMeeleBarrackPosition = {69, 6};
	public static int[] scourgeTopRangedBarrackPosition = {69, 12};
	
	public static int[] scourgeMidMeeleBarrackPosition = {72, 21};
	public static int[] scourgeMidRangedBarrackPosition = {78, 27};
	
	public static int[] scourgeBotMeeleBarrackPosition = {87, 31};
	public static int[] scourgeBotRangedBarrackPosition = {93, 31};
	
	public BuildingDatabase(){
		/*
		 * String name, 
		 * int bountyMoney, 
		 * int startingHP, 
		 * double startingPhysicalDefence, 
		 * int teamNumber
		 *
		*/
		
		Building sentinelBase = new Building("Sentinel Base", 0, 4250, 15, 1);
		buildingDatabase[0] = sentinelBase;
		
		Building scourgeBase = new Building("Scourge Base", 0, 4250, 15, 2);
		buildingDatabase[1] = scourgeBase;
		
		Building sentinelMeeleBarrack = new Building("Sentinel Meele Barrack", 0, 1500, 15, 1);
		Building sentinelRangedBarrack = new Building("Sentinel Ranged Barrack", 0, 1500, 5, 1);
		
		buildingDatabase[2] = sentinelMeeleBarrack;
		buildingDatabase[3] = sentinelRangedBarrack;
		
		Building scourgeMeeleBarrack = new Building("Scourge Meele Barrack", 0, 1500, 15, 2);
		Building scourgeRangedBarrack = new Building("Scourge Ranged Barrack", 0, 1500, 5, 2);
		
		buildingDatabase[4] = scourgeMeeleBarrack;
		buildingDatabase[5] = scourgeRangedBarrack;
		
	}
}
