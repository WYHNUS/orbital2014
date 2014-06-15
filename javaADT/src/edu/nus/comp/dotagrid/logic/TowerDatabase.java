package edu.nus.comp.dotagrid.logic;

public class TowerDatabase {
	public final int TOTAL_TOWER_NUMBER = 8;
	
	public static int towerBountyExp = 0;

	public Tower[] towerDatabase = new Tower[TOTAL_TOWER_NUMBER]; 
	
	public TowerDatabase(){
		/* 
		 * String name, 
		 * int bountyMoney
		 * int bountyExp
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
		Tower sentinelTower1 = new Tower("Sentinel Tower 1", 250, towerBountyExp, 1300, 0, 120, 5, 1.0, 18, 1);
		sentinelTower1.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower2 = new Tower("Sentinel Tower 2", 300, towerBountyExp, 1600, 0, 140, 5, 1.0, 18, 1);
		sentinelTower2.setCharacterImage("towers", "Sentinel Tower");	
		Tower sentinelTower3 = new Tower("Sentinel Tower 3", 350, towerBountyExp, 1600, 0, 180, 5, 1.0, 25, 1);
		sentinelTower3.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower4 = new Tower("Sentinel Tower 4", 400, towerBountyExp, 1600, 0, 180, 5, 1.0, 30, 1);
		sentinelTower4.setCharacterImage("towers", "Sentinel Tower");
	
		towerDatabase[0] = sentinelTower1;
		towerDatabase[1] = sentinelTower2;
		towerDatabase[2] = sentinelTower3;
		towerDatabase[3] = sentinelTower4;
		
		Tower scourgeTower1 = new Tower("Scourge Tower 1", 250, towerBountyExp, 1300, 0, 120, 5, 1.0, 18, 2);
		scourgeTower1.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower2 = new Tower("Scourge Tower 2", 300, towerBountyExp, 1600, 0, 140, 5, 1.0, 18, 2);
		scourgeTower2.setCharacterImage("towers", "Scourge Tower");	
		Tower scourgeTower3 = new Tower("Scourge Tower 3", 350, towerBountyExp, 1600, 0, 180, 5, 1.0, 25, 2);
		scourgeTower3.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower4 = new Tower("Scourge Tower 4", 400, towerBountyExp, 1600, 0, 180, 5, 1.0, 30, 2);
		scourgeTower4.setCharacterImage("towers", "Scourge Tower");
	
		towerDatabase[4] = scourgeTower1;
		towerDatabase[5] = scourgeTower2;
		towerDatabase[6] = scourgeTower3;
		towerDatabase[7] = scourgeTower4;
		
	}
}
