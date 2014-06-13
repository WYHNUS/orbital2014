package edu.nus.comp.dotagrid.logic;

public class TowerDatabase {
	public final int TOTAL_TOWER_NUMBER = 4;

	public Tower[] towerDatabase = new Tower[TOTAL_TOWER_NUMBER]; 
	
	public TowerDatabase(){
		/* 
		 * String name, 
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
		 * 
		*/
		Tower sentinelTower1 = new Tower("Sentinel Tower 1", 1300, 0, 120, 5, 1.0, 18, 50, -522, 50);
		sentinelTower1.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower2 = new Tower("Sentinel Tower 2", 1600, 0, 140, 5, 1.0, 18, 60, -522, 50);
		sentinelTower2.setCharacterImage("towers", "Sentinel Tower");	
		Tower sentinelTower3 = new Tower("Sentinel Tower 3", 1600, 0, 180, 5, 1.0, 25, 70, -522, 50);
		sentinelTower3.setCharacterImage("towers", "Sentinel Tower");
		Tower sentinelTower4 = new Tower("Sentinel Tower 4", 1600, 0, 180, 5, 1.0, 30, 80, -522, 50);
		sentinelTower4.setCharacterImage("towers", "Sentinel Tower");
	
		towerDatabase[0] = sentinelTower1;
		towerDatabase[1] = sentinelTower2;
		towerDatabase[2] = sentinelTower3;
		towerDatabase[3] = sentinelTower4;
		
	}
}
