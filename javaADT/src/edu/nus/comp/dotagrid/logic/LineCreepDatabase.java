package edu.nus.comp.dotagrid.logic;

public class LineCreepDatabase {
	
	public final int TOTAL_LINECREEP_NUMBER = 12;
	
	public LineCreep[] lineCreepDatabase = new LineCreep[TOTAL_LINECREEP_NUMBER];
	
	public static final int meeleCreepUpgradeHP = 10;
	public static final int rangedCreepUpgradeHP = 10;
	
	public static final double meeleCreepUpgradeAttack = 1.0;
	public static final double rangedCreepUpgradeAttack = 2.0;
	
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
		
		int meeleHPSentinel = LineCreepDatabase.meeleCreepUpgradeHP * LineCreep.levelSentinel;
		int rangedHPSentinel = LineCreepDatabase.rangedCreepUpgradeHP * LineCreep.levelSentinel;
		double meeleAttackSentinel = LineCreepDatabase.meeleCreepUpgradeAttack * LineCreep.levelSentinel;
		double rangedAttackSentinel = LineCreepDatabase.rangedCreepUpgradeAttack * LineCreep.levelSentinel;
		
		int meeleHPScourge = LineCreepDatabase.meeleCreepUpgradeHP * LineCreep.levelScourge;
		int rangedHPScourge = LineCreepDatabase.rangedCreepUpgradeHP * LineCreep.levelScourge;
		double meeleAttackScourge = LineCreepDatabase.meeleCreepUpgradeAttack * LineCreep.levelScourge;
		double rangedAttackScourge = LineCreepDatabase.rangedCreepUpgradeAttack * LineCreep.levelScourge;
		
		LineCreep sentinelMeeleCreep = new LineCreep("Sentinel Meele Creep", 44 , 62 , 550 + meeleHPSentinel, 0 , 21, meeleAttackSentinel, 1, 1.0, 2, 25, 325, 1);
		LineCreep sentinelRangedCreep = new LineCreep("Sentinel Ranged Creep", 49, 41, 300 + rangedHPSentinel, 300, 24, rangedAttackSentinel, 3, 1.0, 0, 25, 325, 1);
		LineCreep sentinelSiegeCreep = new LineCreep("Sentinel Siege Creep", 74, 88, 500, 0, 40, 0, 4, 0.7, 0, 100, 325, 1);
		
		lineCreepDatabase[0] = sentinelMeeleCreep;
		lineCreepDatabase[1] = sentinelRangedCreep;
		lineCreepDatabase[2] = sentinelSiegeCreep;
		
		LineCreep scourgeMeeleCreep = new LineCreep("Scourge Meele Creep", 44, 62, 550 + meeleHPScourge, 0, 21, meeleAttackScourge, 1, 1.0, 2, 25, 325, 2);
		LineCreep scourgeRangedCreep = new LineCreep("Scourge Ranged Creep", 49, 41, 300 + rangedHPScourge, 300, 24, rangedAttackScourge, 3, 1.0, 0, 25, 325, 2);
		LineCreep scourgeSiegeCreep = new LineCreep("Scourge Siege Creep", 74, 88, 500, 0, 40, 0, 4, 0.7, 0, 100, 325, 2);
		
		lineCreepDatabase[3] = scourgeMeeleCreep;
		lineCreepDatabase[4] = scourgeRangedCreep;
		lineCreepDatabase[5] = scourgeSiegeCreep;
		
		
		int superMeeleHPSentinel = 150 + 9 * LineCreep.levelSentinel;
		int superRangedHPSentinel = 175 + 8 * LineCreep.levelSentinel;
		double superMeeleAttackSentinel = 19 + LineCreep.levelSentinel;
		double superRangedAttackSentinel = 20 + LineCreep.levelSentinel;
		
		int superMeeleHPScourge = 150 + 9 * LineCreep.levelScourge;
		int superRangedHPScourge = 175 + 8 * LineCreep.levelScourge;
		double superMeeleAttackScourge = 19 + LineCreep.levelScourge;
		double superRangedAttackScourge = 20 + LineCreep.levelScourge;
		double superSiegeAttack = 16;

		LineCreep superSentinelMeeleCreep = new LineCreep("Sentinel Meele Creep", 44 , 62 , 550 + meeleHPSentinel + superMeeleHPSentinel, 0 , 21, meeleAttackSentinel + superMeeleAttackSentinel, 1, 1.0, 3, 25, 325, 1);
		LineCreep superSentinelRangedCreep = new LineCreep("Sentinel Ranged Creep", 49, 41, 300 + rangedHPSentinel + superRangedHPSentinel, 300, 24, rangedAttackSentinel + superRangedAttackSentinel, 3, 1.0, 1, 25, 325, 1);
		LineCreep superSentinelSiegeCreep = new LineCreep("Sentinel Siege Creep", 74, 88, 500, 0, 40, superSiegeAttack, 4, 0.7, 0, 100, 325, 1);

		lineCreepDatabase[6] = superSentinelMeeleCreep;
		lineCreepDatabase[7] = superSentinelRangedCreep;
		lineCreepDatabase[8] = superSentinelSiegeCreep;
		
		LineCreep superScourgeMeeleCreep = new LineCreep("Scourge Meele Creep", 44, 62, 550 + meeleHPScourge + superMeeleHPScourge, 0, 21, meeleAttackScourge + superMeeleAttackScourge, 1, 1.0, 3, 25, 325, 2);
		LineCreep superScourgeRangedCreep = new LineCreep("Scourge Ranged Creep", 49, 41, 300 + rangedHPScourge + superRangedHPScourge, 300, 24, rangedAttackScourge + superRangedAttackScourge, 3, 1.0, 1, 25, 325, 2);
		LineCreep superScourgeSiegeCreep = new LineCreep("Scourge Siege Creep", 74, 88, 500, 0, 40, superSiegeAttack, 4, 0.7, 0, 100, 325, 2);

		lineCreepDatabase[9] = superScourgeMeeleCreep;
		lineCreepDatabase[10] = superScourgeRangedCreep;
		lineCreepDatabase[11] = superScourgeSiegeCreep;
	}

}
