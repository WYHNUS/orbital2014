package edu.nus.comp.dotagrid.logic;

public class LineCreepDatabase {
	
	public final int TOTAL_LINECREEP_NUMBER = 6;
	
	public LineCreep[] lineCreepDatabase = new LineCreep[TOTAL_LINECREEP_NUMBER];
	
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
		 * int startingPhysicalAttackArea, 
		 * double startingPhysicalAttackSpeed, 	
		 * double startingPhysicalDefence, 
		 * double startingMagicResistance,		
		 * 
		 * int startingMovementSpeed, 
		 * int teamNumber
		 * */
		
		LineCreep sentinelMeeleCreep = new LineCreep("Sentinel Meele Creep", 44 , 62 , 550 , 0 , 21 , 1 , 1.0 , 2 , 25 , 325, 1);
		LineCreep sentinelRangedCreep = new LineCreep("Sentinel Ranged Creep", 49 , 41 , 300 , 300 , 24 , 3 , 1.0 , 0 , 25 , 325, 1);
		LineCreep sentinelSiegeCreep = new LineCreep("Sentinel Siege Creep", 74 , 88 , 500 , 0 , 40 , 4 , 0.7 , 0 , 100 , 325, 1);
		
		lineCreepDatabase[0] = sentinelMeeleCreep;
		lineCreepDatabase[1] = sentinelRangedCreep;
		lineCreepDatabase[2] = sentinelSiegeCreep;
		
		LineCreep scourgeMeeleCreep = new LineCreep("Scourge Meele Creep", 44 , 62 , 550 , 0 , 21 , 1 , 1.0 , 2 , 25 , 325, 2);
		LineCreep scourgeRangedCreep = new LineCreep("Scourge Ranged Creep", 49 , 41 , 300 , 300 , 24 , 3 , 1.0 , 0 , 25 , 325, 2);
		LineCreep scourgeSiegeCreep = new LineCreep("Scourge Siege Creep", 74 , 88 , 500 , 0 , 40 , 4 , 0.7 , 0 , 100 , 325, 2);
		
		lineCreepDatabase[3] = scourgeMeeleCreep;
		lineCreepDatabase[4] = scourgeRangedCreep;
		lineCreepDatabase[5] = scourgeSiegeCreep;
		
	}

}
