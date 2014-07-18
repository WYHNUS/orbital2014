package edu.nus.comp.dotagrid.logic;

import java.util.LinkedList;
import java.util.Queue;

public class LineCreepDatabase {
	
	public static final int TOTAL_LINECREEP_NUMBER = 12;
	
	public static final int MEELE_CREEP_UPGRADE_HP = 10;
	public static final int RANGED_CREEP_UPGRADE_HP = 10;
	
	public static final double MEELE_CREEP_UPGRADE_ATTACK = 1.0;
	public static final double RANGED_CREEP_UPGRADE_ATTACK = 2.0;
	
	private static int meeleHPSentinel;
	private static int rangedHPSentinel;
	private static double meeleAttackSentinel;
	private static double rangedAttackSentinel;
	
	private static int meeleHPScourge;
	private static int rangedHPScourge;
	private static double meeleAttackScourge;
	private static double rangedAttackScourge;
	
	private static int superMeeleHPSentinel;
	private static int superRangedHPSentinel;
	private static double superMeeleAttackSentinel;
	private static double superRangedAttackSentinel;
	
	private static int superMeeleHPScourge;
	private static int superRangedHPScourge;
	private static double superMeeleAttackScourge;
	private static double superRangedAttackScourge;
	private static double superSiegeAttack;
	
	public static int[] botCreepCheckPoint = {90, 88};
	
	public LineCreepDatabase(){
		/*
		 * String name, 
		 * int bountyMoney, 
		 * int bountyExp, 
		 * 
		 * int startingHP, 
		 * int startingMP, 	
		 * 
		 * double startingPhysicalAttack, 
		 * double basicPhysicalAttack
		 * int startingPhysicalAttackArea, 
		 * double startingPhysicalAttackSpeed, 	
		 * 
		 * double startingPhysicalDefence, 
		 * double startingMagicResistance,		
		 * 
		 * int startingMovementSpeed, 
		 * int teamNumber
		 * */
		
		
		/*
		 * after every 200 rounds, meele and ranged creeps will automatically level up (maximum level : 30)
		 * 
		 * each level will increase for 10HP and 1 physicalAttack for meele creeps
		 * each level will increase for 10HP and 2 physicalAttack for ranged creeps
		 * 
		*/
		
		meeleHPSentinel = LineCreepDatabase.MEELE_CREEP_UPGRADE_HP * LineCreep.levelSentinel;
		rangedHPSentinel = LineCreepDatabase.RANGED_CREEP_UPGRADE_HP * LineCreep.levelSentinel;
		meeleAttackSentinel = LineCreepDatabase.MEELE_CREEP_UPGRADE_ATTACK * LineCreep.levelSentinel;
		rangedAttackSentinel = LineCreepDatabase.RANGED_CREEP_UPGRADE_ATTACK * LineCreep.levelSentinel;
		
		meeleHPScourge = LineCreepDatabase.MEELE_CREEP_UPGRADE_HP * LineCreep.levelScourge;
		rangedHPScourge = LineCreepDatabase.RANGED_CREEP_UPGRADE_HP * LineCreep.levelScourge;
		meeleAttackScourge = LineCreepDatabase.MEELE_CREEP_UPGRADE_ATTACK * LineCreep.levelScourge;
		rangedAttackScourge = LineCreepDatabase.RANGED_CREEP_UPGRADE_ATTACK * LineCreep.levelScourge;
		
		superMeeleHPSentinel = 150 + 9 * LineCreep.levelSentinel;
		superRangedHPSentinel = 175 + 8 * LineCreep.levelSentinel;
		superMeeleAttackSentinel = 19 + LineCreep.levelSentinel;
		superRangedAttackSentinel = 20 + LineCreep.levelSentinel;
		
		superMeeleHPScourge = 150 + 9 * LineCreep.levelScourge;
		superRangedHPScourge = 175 + 8 * LineCreep.levelScourge;
		superMeeleAttackScourge = 19 + LineCreep.levelScourge;
		superRangedAttackScourge = 20 + LineCreep.levelScourge;
		superSiegeAttack = 16;
		
	}

	// Create Sentinel Creeps Methods
	
	public static void createSentinelMeeleCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep sentinelMeeleCreep = new LineCreep("Sentinel Meele Creep", 44 , 62 , 550 + meeleHPSentinel, 0 , 21, meeleAttackSentinel, 1, 0.9, 2, 25, 325, 1);
			SentinelCreeps.offer(sentinelMeeleCreep);
		}
	}
	
	public static void createSentinelSuperMeeleCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superSentinelMeeleCreep = new LineCreep("Sentinel Meele Creep", 44 , 62 , 550 + meeleHPSentinel + superMeeleHPSentinel, 0 , 21, meeleAttackSentinel + superMeeleAttackSentinel, 1, 0.9, 3, 25, 325, 1);
			SentinelCreeps.offer(superSentinelMeeleCreep);
		}
	}
	
	public static void createSentinelRangedCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep sentinelRangedCreep = new LineCreep("Sentinel Ranged Creep", 49, 41, 300 + rangedHPSentinel, 300, 24, rangedAttackSentinel, 3, 0.9, 0, 25, 325, 1);
			SentinelCreeps.offer(sentinelRangedCreep);
		}
	}
	
	public static void createSentinelSuperRangedCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superSentinelRangedCreep = new LineCreep("Sentinel Ranged Creep", 49, 41, 300 + rangedHPSentinel + superRangedHPSentinel, 300, 24, rangedAttackSentinel + superRangedAttackSentinel, 3, 0.9, 1, 25, 325, 1);
			SentinelCreeps.offer(superSentinelRangedCreep);
		}
	}
	
	public static void createSentinelSiegeCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep sentinelSiegeCreep = new LineCreep("Sentinel Siege Creep", 74, 88, 500, 0, 40, 0, 4, 0.7, 0, 100, 325, 1);
			SentinelCreeps.offer(sentinelSiegeCreep);
		}
	}
	
	public static void createSentinelSuperSiegeCreeps(Queue <LineCreep> SentinelCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superSentinelSiegeCreep = new LineCreep("Sentinel Siege Creep", 74, 88, 500, 0, 40, superSiegeAttack, 4, 0.7, 0, 100, 325, 1);
			SentinelCreeps.offer(superSentinelSiegeCreep);
		}
	}
	
	
	// Create Scourge Creeps Methods

	public static void createScourgeMeeleCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep scourgeMeeleCreep = new LineCreep("Scourge Meele Creep", 44, 62, 550 + meeleHPScourge, 0, 21, meeleAttackScourge, 1, 0.9, 2, 25, 325, 2);
			ScourgeCreeps.offer(scourgeMeeleCreep);
		}
	}
	
	public static void createScourgeSuperMeeleCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superScourgeMeeleCreep = new LineCreep("Scourge Meele Creep", 44, 62, 550 + meeleHPScourge + superMeeleHPScourge, 0, 21, meeleAttackScourge + superMeeleAttackScourge, 1, 0.9, 3, 25, 325, 2);
			ScourgeCreeps.offer(superScourgeMeeleCreep);
		}
	}
	
	public static void createScourgeRangedCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep scourgeRangedCreep = new LineCreep("Scourge Ranged Creep", 49, 41, 300 + rangedHPScourge, 300, 24, rangedAttackScourge, 3, 0.9, 0, 25, 325, 2);
			ScourgeCreeps.offer(scourgeRangedCreep);
		}
	}
	
	public static void createScourgeSuperRangedCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superScourgeRangedCreep = new LineCreep("Scourge Ranged Creep", 49, 41, 300 + rangedHPScourge + superRangedHPScourge, 300, 24, rangedAttackScourge + superRangedAttackScourge, 3, 0.9, 1, 25, 325, 2);
			ScourgeCreeps.offer(superScourgeRangedCreep);
		}
	}
	
	public static void createScourgeSiegeCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep scourgeSiegeCreep = new LineCreep("Scourge Siege Creep", 74, 88, 500, 0, 40, 0, 4, 0.7, 0, 100, 325, 2);
			ScourgeCreeps.offer(scourgeSiegeCreep);
		}
	}
	
	public static void createScourgeSuperSiegeCreeps(Queue <LineCreep> ScourgeCreeps, int creepNumber){
		for (int i=0; i<creepNumber; i++) {
			LineCreep superScourgeSiegeCreep = new LineCreep("Scourge Siege Creep", 74, 88, 500, 0, 40, superSiegeAttack, 4, 0.7, 0, 100, 325, 2);
			ScourgeCreeps.offer(superScourgeSiegeCreep);
		}
	}
	
	
	// set sentinel creeps target positions methods
	
	public static void setSentinelTopCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.sentinelTopTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelTopTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeTopTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeTopTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeTopTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeLeftTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeRightTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.scourgeBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}
	
	public static void setSentinelMidCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.sentinelMidTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelMidTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeMidTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeMidTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeMidTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeLeftTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeRightTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.scourgeBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}
	
	public static void setSentinelBotCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.sentinelBotTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelBotTower1Position);
			creeps.peek().addAItargetPos(botCreepCheckPoint);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeBotTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeBotTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeBotTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeRightTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeLeftTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.scourgeBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}
	
	
	// set scourge creeps target positions methods
	
	public static void setScourgeTopCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.scourgeTopTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeTopTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelTopTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelTopTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelTopTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelLeftTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelRightTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.sentinelBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}
	
	public static void setScourgeMidCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.scourgeMidTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeMidTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelMidTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelMidTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelMidTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelLeftTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelRightTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.sentinelBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}
	
	public static void setScourgeBotCreepsTargets(Queue<LineCreep> creeps){
		Queue<LineCreep> tempQueue = new LinkedList<LineCreep>();
		while (!creeps.isEmpty()) {
			creeps.peek().addAItargetPos(TowerDatabase.scourgeBotTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.scourgeBotTower1Position);
			creeps.peek().addAItargetPos(botCreepCheckPoint);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelBotTower1Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelBotTower2Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelBotTower3Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelRightTower4Position);
			creeps.peek().addAItargetPos(TowerDatabase.sentinelLeftTower4Position);
			creeps.peek().addAItargetPos(BuildingDatabase.sentinelBasePos);
			tempQueue.add(creeps.poll());
		}
		creeps.addAll(tempQueue);
	}

}
