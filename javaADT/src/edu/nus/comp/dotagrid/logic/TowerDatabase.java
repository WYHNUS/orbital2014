package edu.nus.comp.dotagrid.logic;

public class TowerDatabase {
	public final int TOTAL_TOWER_NUMBER = 8;

	public Tower[] towerDatabase = new Tower[TOTAL_TOWER_NUMBER]; 
	
	public TowerDatabase(){
		/* 
		 * String name, 
		 * int heroBountyMoney
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
		 * int actionPoint
		 * int teamNumber
		 * 
		*/
		Tower sentinelTower1 = new Tower("Sentinel Tower 1", 250, 1300, 0, 120, 5, 1.0, 18, 50, -522, 50, 1);
		sentinelTower1.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower2 = new Tower("Sentinel Tower 2", 300, 1600, 0, 140, 5, 1.0, 18, 60, -522, 50, 1);
		sentinelTower2.setCharacterImage("towers", "Sentinel Tower");	
		Tower sentinelTower3 = new Tower("Sentinel Tower 3", 350,  1600, 0, 180, 5, 1.0, 25, 70, -522, 50, 1);
		sentinelTower3.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower4 = new Tower("Sentinel Tower 4", 400, 1600, 0, 180, 5, 1.0, 30, 80, -522, 50, 1);
		sentinelTower4.setCharacterImage("towers", "Sentinel Tower");
	
		towerDatabase[0] = sentinelTower1;
		towerDatabase[1] = sentinelTower2;
		towerDatabase[2] = sentinelTower3;
		towerDatabase[3] = sentinelTower4;
		
		Tower scourgeTower1 = new Tower("Scourge Tower 1", 250, 1300, 0, 120, 5, 1.0, 18, 50, -522, 50, 2);
		scourgeTower1.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower2 = new Tower("Scourge Tower 2", 300, 1600, 0, 140, 5, 1.0, 18, 60, -522, 50, 2);
		scourgeTower2.setCharacterImage("towers", "Scourge Tower");	
		Tower scourgeTower3 = new Tower("Scourge Tower 3", 350,  1600, 0, 180, 5, 1.0, 25, 70, -522, 50, 2);
		scourgeTower3.setCharacterImage("towers", "Scourge Tower");
		Tower scourgeTower4 = new Tower("Scourge Tower 4", 400, 1600, 0, 180, 5, 1.0, 30, 80, -522, 50, 2);
		scourgeTower4.setCharacterImage("towers", "Scourge Tower");
	
		towerDatabase[4] = scourgeTower1;
		towerDatabase[5] = scourgeTower2;
		towerDatabase[6] = scourgeTower3;
		towerDatabase[7] = scourgeTower4;
		
	}
}
