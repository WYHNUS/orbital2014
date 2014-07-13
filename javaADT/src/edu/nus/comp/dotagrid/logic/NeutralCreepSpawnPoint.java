package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class NeutralCreepSpawnPoint {
	
	public static final int SPAWN_NC_ROUND_INDEX = 30;
	
	public static final int[] SMALL_NC_SPAWN_POS_1 = {32, 19};
	public static final int[] SMALL_NC_SPAWN_POS_2 = {70, 81};
	
	public static final int[] MID_NC_SPAWN_POS_1 = {50, 22};
	public static final int[] MID_NC_SPAWN_POS_2 = {39, 31};
	public static final int[] MID_NC_SPAWN_POS_3 = {50, 66};
	public static final int[] MID_NC_SPAWN_POS_4 = {77, 76};
	
	public static final int[] LARGE_NC_SPAWN_POS_1 = {19, 23};
	public static final int[] LARGE_NC_SPAWN_POS_2 = {57, 29};
	public static final int[] LARGE_NC_SPAWN_POS_3 = {42, 77};
	public static final int[] LARGE_NC_SPAWN_POS_4 = {62, 76};

	
	public static void spawnNewWave() {
		// check and spawn new wave of neutral creeps
		
		if (GameFrame.turn % SPAWN_NC_ROUND_INDEX == 0) {
			createWave();
		}
		
	}

	
	private static void createWave() {
		// create new wave of neutral creeps if there is no characters within spawn position's range
		checkAndSpawnNC(SMALL_NC_SPAWN_POS_1, 1);
		checkAndSpawnNC(SMALL_NC_SPAWN_POS_2, 1);
		
		checkAndSpawnNC(MID_NC_SPAWN_POS_1, 2);
		checkAndSpawnNC(MID_NC_SPAWN_POS_2, 2);
		checkAndSpawnNC(MID_NC_SPAWN_POS_3, 2);
		checkAndSpawnNC(MID_NC_SPAWN_POS_4, 2);
		
		checkAndSpawnNC(LARGE_NC_SPAWN_POS_1, 3);
		checkAndSpawnNC(LARGE_NC_SPAWN_POS_2, 3);
		checkAndSpawnNC(LARGE_NC_SPAWN_POS_3, 3);
		checkAndSpawnNC(LARGE_NC_SPAWN_POS_4, 3);
	}


	private static void checkAndSpawnNC(int[] spawnPos, int index) {
		// spawn NC from spawnPos based on index
		if (noCharaWithinRange(spawnPos)) {
			// prepare for spawning
			Queue<int[]> spawnPosQueue = new LinkedList<int[]>();
			spawnPosQueue.add(spawnPos);
			
			ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
			
			Queue<NeutralCreep> tempCharaQueue = new LinkedList<NeutralCreep>();
			
			switch (index) {
				// small nc
				case 1 :
					NeutralCreepDatabase.createSmallNCWave(tempCharaQueue);
					break;
					
				// mid nc
				case 2 :
					NeutralCreepDatabase.createMiddleNCWave(tempCharaQueue);
					break;
					
				// large nc
				case 3 : 
					NeutralCreepDatabase.createLargeNCWave(tempCharaQueue);
					break;
					
				// super nc
				case 4 :
					NeutralCreepDatabase.createSuperNCWave(tempCharaQueue);
					break;
			}
			
			// spawn!
			LineCreepSpawnPoint.spawn(spawnPosQueue, checkedPosition, tempCharaQueue);
		}
	}


	private static boolean noCharaWithinRange(int[] spawnPos) {
		// TODO Auto-generated method stub
		return true;
	}
}
