package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LineCreepSpawnPoint {
	
	public static final int SENTINEL_TOP_LINE_SPAWN_X_POS = 8;
	public static final int SENTINEL_TOP_LINE_SPAWN_Y_POS = 69;
	
	public static final int SENTINEL_MID_LINE_SPAWN_X_POS = 24;
	public static final int SENTINEL_MID_LINE_SPAWN_Y_POS = 75;
	
	public static final int SENTINEL_BOT_LINE_SPAWN_X_POS = 28;	
	public static final int SENTINEL_BOT_LINE_SPAWN_Y_POS = 90;
	
	public static final int SCOURGE_TOP_LINE_SPAWN_X_POS = 70;	
	public static final int SCOURGE_TOP_LINE_SPAWN_Y_POS = 9;

	public static final int SCOURGE_MID_LINE_SPAWN_X_POS = 75;
	public static final int SCOURGE_MID_LINE_SPAWN_Y_POS = 24;
	
	public static final int SCOURGE_BOT_LINE_SPAWN_X_POS = 91;	
	public static final int SCOURGE_BOT_LINE_SPAWN_Y_POS = 30;
	
	public static int MEELE_CREEP_NUMBER = 3;
	public static int RANGED_CREEP_NUMBER = 1;
	public static int SIEGE_CREEP_NUMBER = 1;


	public static void spawnNewWave() {
		/* 
		 * check and spawn new wave of creeps  :
		 * 
		 * spawn new normal wave every 10 turns
		 * spawn new extended wave every 50 turns
		*/

		
		if (GameFrame.turn % 10 == 0) {
			createWave();
		}
		
	}



	private static void createWave() {
		// reset level for creeps : maximum level 30
		if (LineCreep.levelSentinel < 30) {
			LineCreep.levelSentinel = GameFrame.turn / 200;
		}
		if (LineCreep.levelScourge < 30) {
			LineCreep.levelScourge = GameFrame.turn / 200;
		}
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		
		LineCreepDatabase lineCreeps = new LineCreepDatabase();
		
		// change the number of spawning creeps
		if (GameFrame.turn <= 600) {
			MEELE_CREEP_NUMBER = 3 + GameFrame.turn / 200;
			RANGED_CREEP_NUMBER = 1 + GameFrame.turn / 600;
			SIEGE_CREEP_NUMBER = 1 + GameFrame.turn / 600;
		}
		
		
		// each spawn point spawn MEELE_CREEP_NUMBER meele creeps + RANGED_CREEP_NUMBER ranged creep
		Queue <LineCreep> SentinelCreeps = new LinkedList<LineCreep>();
		Queue <LineCreep> ScourgeCreeps = new LinkedList<LineCreep>();
		
		Queue <LineCreep> SuperMeeleSentinelCreeps = new LinkedList<LineCreep>();
		Queue <LineCreep> SuperMeeleScourgeCreeps = new LinkedList<LineCreep>();
		
		Queue <LineCreep> SuperRangeSentinelCreeps = new LinkedList<LineCreep>();
		Queue <LineCreep> SuperRangeScourgeCreeps = new LinkedList<LineCreep>();
		
		Queue <LineCreep> SuperSentinelCreeps = new LinkedList<LineCreep>();
		Queue <LineCreep> SuperScourgeCreeps = new LinkedList<LineCreep>();
		
		
		// add meele creeps
		for (int i=0; i<MEELE_CREEP_NUMBER; i++) {
			SentinelCreeps.add(lineCreeps.lineCreepDatabase[0]);
			ScourgeCreeps.add(lineCreeps.lineCreepDatabase[3]);
			SuperRangeSentinelCreeps.add(lineCreeps.lineCreepDatabase[0]);
			SuperRangeScourgeCreeps.add(lineCreeps.lineCreepDatabase[3]);
			SuperMeeleSentinelCreeps.add(lineCreeps.lineCreepDatabase[6]);
			SuperMeeleScourgeCreeps.add(lineCreeps.lineCreepDatabase[9]);
			SuperSentinelCreeps.add(lineCreeps.lineCreepDatabase[6]);
			SuperScourgeCreeps.add(lineCreeps.lineCreepDatabase[9]);
		}
		
		// add ranged creeps
		for (int i=0; i<RANGED_CREEP_NUMBER; i++) {
			SentinelCreeps.add(lineCreeps.lineCreepDatabase[1]);
			ScourgeCreeps.add(lineCreeps.lineCreepDatabase[4]);
			SuperMeeleSentinelCreeps.add(lineCreeps.lineCreepDatabase[1]);
			SuperMeeleScourgeCreeps.add(lineCreeps.lineCreepDatabase[4]);
			SuperRangeSentinelCreeps.add(lineCreeps.lineCreepDatabase[7]);
			SuperRangeScourgeCreeps.add(lineCreeps.lineCreepDatabase[10]);
			SuperSentinelCreeps.add(lineCreeps.lineCreepDatabase[7]);
			SuperScourgeCreeps.add(lineCreeps.lineCreepDatabase[10]);
		}
		
		// add siege creeps every 50 turns
		if (GameFrame.turn % 50 == 0) {
			for (int i=0; i<SIEGE_CREEP_NUMBER; i++) {
				SentinelCreeps.add(lineCreeps.lineCreepDatabase[2]);
				ScourgeCreeps.add(lineCreeps.lineCreepDatabase[5]);
				SuperMeeleSentinelCreeps.add(lineCreeps.lineCreepDatabase[2]);
				SuperMeeleScourgeCreeps.add(lineCreeps.lineCreepDatabase[5]);
				SuperRangeSentinelCreeps.add(lineCreeps.lineCreepDatabase[8]);
				SuperRangeScourgeCreeps.add(lineCreeps.lineCreepDatabase[11]);
				SuperSentinelCreeps.add(lineCreeps.lineCreepDatabase[8]);
				SuperScourgeCreeps.add(lineCreeps.lineCreepDatabase[11]);
			}
		}
		
		
		// make copies for creep queue		
		LinkedList<LineCreep> SentinelCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SentinelCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SentinelCreeps3 = new LinkedList<LineCreep>();
		SentinelCreeps1.addAll(SentinelCreeps);
		SentinelCreeps2.addAll(SentinelCreeps);
		SentinelCreeps3.addAll(SentinelCreeps);
		
		LinkedList<LineCreep> ScourgeCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> ScourgeCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> ScourgeCreeps3 = new LinkedList<LineCreep>();
		ScourgeCreeps1.addAll(ScourgeCreeps);
		ScourgeCreeps2.addAll(ScourgeCreeps);
		ScourgeCreeps3.addAll(ScourgeCreeps);
		
		LinkedList<LineCreep> SuperMeeleSentinelCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperMeeleSentinelCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperMeeleSentinelCreeps3 = new LinkedList<LineCreep>();
		SuperMeeleSentinelCreeps1.addAll(SuperMeeleSentinelCreeps);
		SuperMeeleSentinelCreeps2.addAll(SuperMeeleSentinelCreeps);
		SuperMeeleSentinelCreeps3.addAll(SuperMeeleSentinelCreeps);
		
		LinkedList<LineCreep> SuperMeeleScourgeCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperMeeleScourgeCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperMeeleScourgeCreeps3 = new LinkedList<LineCreep>();
		SuperMeeleScourgeCreeps1.addAll(SuperMeeleScourgeCreeps);
		SuperMeeleScourgeCreeps2.addAll(SuperMeeleScourgeCreeps);
		SuperMeeleScourgeCreeps3.addAll(SuperMeeleScourgeCreeps);
		
		LinkedList<LineCreep> SuperRangeSentinelCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperRangeSentinelCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperRangeSentinelCreeps3 = new LinkedList<LineCreep>();
		SuperRangeSentinelCreeps1.addAll(SuperRangeSentinelCreeps);
		SuperRangeSentinelCreeps2.addAll(SuperRangeSentinelCreeps);
		SuperRangeSentinelCreeps3.addAll(SuperRangeSentinelCreeps);
		
		LinkedList<LineCreep> SuperRangeScourgeCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperRangeScourgeCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperRangeScourgeCreeps3 = new LinkedList<LineCreep>();
		SuperRangeScourgeCreeps1.addAll(SuperRangeScourgeCreeps);
		SuperRangeScourgeCreeps2.addAll(SuperRangeScourgeCreeps);
		SuperRangeScourgeCreeps3.addAll(SuperRangeScourgeCreeps);
		
		LinkedList<LineCreep> SuperSentinelCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperSentinelCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperSentinelCreeps3 = new LinkedList<LineCreep>();
		SuperSentinelCreeps1.addAll(SuperSentinelCreeps);
		SuperSentinelCreeps2.addAll(SuperSentinelCreeps);
		SuperSentinelCreeps3.addAll(SuperSentinelCreeps);
		
		LinkedList<LineCreep> SuperScourgeCreeps1 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperScourgeCreeps2 = new LinkedList<LineCreep>();
		LinkedList<LineCreep> SuperScourgeCreeps3 = new LinkedList<LineCreep>();
		SuperScourgeCreeps1.addAll(SuperScourgeCreeps);
		SuperScourgeCreeps2.addAll(SuperScourgeCreeps);
		SuperScourgeCreeps3.addAll(SuperScourgeCreeps);
		
		
		// spawn sentinel line creeps
		
		int[] sentinelTopPosition = {SENTINEL_TOP_LINE_SPAWN_X_POS, SENTINEL_TOP_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelTop = new LinkedList<int[]>(); 
		sentinelTop.add(sentinelTopPosition);
		
		if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == false) {
			spawn(sentinelTop, checkedPosition, SentinelCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == true) {
			spawn(sentinelTop, checkedPosition, SuperSentinelCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == false) {
			spawn(sentinelTop, checkedPosition, SuperMeeleSentinelCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == true) {
			spawn(sentinelTop, checkedPosition, SuperRangeSentinelCreeps1);
		}
		
		
		int[] sentinelMidPosition = {SENTINEL_MID_LINE_SPAWN_X_POS, SENTINEL_MID_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelMid = new LinkedList<int[]>(); 
		sentinelMid.add(sentinelMidPosition);		
		
		if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == false) {
			spawn(sentinelMid, checkedPosition, SentinelCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == true) {
			spawn(sentinelMid, checkedPosition, SuperSentinelCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == false) {
			spawn(sentinelMid, checkedPosition, SuperMeeleSentinelCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == true) {
			spawn(sentinelMid, checkedPosition, SuperRangeSentinelCreeps2);
		}
		
		
		int[] sentinelBotPosition = {SENTINEL_BOT_LINE_SPAWN_X_POS, SENTINEL_BOT_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelBot = new LinkedList<int[]>(); 
		sentinelBot.add(sentinelBotPosition);
		
		if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == false) {
			spawn(sentinelBot, checkedPosition, SentinelCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == true) {
			spawn(sentinelBot, checkedPosition, SuperSentinelCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == false) {
			spawn(sentinelBot, checkedPosition, SuperMeeleSentinelCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == true) {
			spawn(sentinelBot, checkedPosition, SuperRangeSentinelCreeps3);
		}
		
		
		// spawn scourge line creeps
		
		int[] scourgeTopPosition = {SCOURGE_TOP_LINE_SPAWN_X_POS, SCOURGE_TOP_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeTop = new LinkedList<int[]>(); 
		scourgeTop.add(scourgeTopPosition);
		
		if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == false) {
			spawn(scourgeTop, checkedPosition, ScourgeCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == true) {
			spawn(scourgeTop, checkedPosition, SuperScourgeCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == false) {
			spawn(scourgeTop, checkedPosition, SuperMeeleScourgeCreeps1);
		} else if (BuildingDatabase.isDestroyedScourgeTopMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeTopRangedBarrack == true) {
			spawn(scourgeTop, checkedPosition, SuperRangeScourgeCreeps1);
		}
		
		
		
		int[] scourgeMidPosition = {SCOURGE_MID_LINE_SPAWN_X_POS, SCOURGE_MID_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeMid = new LinkedList<int[]>(); 
		scourgeMid.add(scourgeMidPosition);
		
		if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == false) {
			spawn(scourgeMid, checkedPosition, ScourgeCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == true) {
			spawn(scourgeMid, checkedPosition, SuperScourgeCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == false) {
			spawn(scourgeMid, checkedPosition, SuperMeeleScourgeCreeps2);
		} else if (BuildingDatabase.isDestroyedScourgeMidMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeMidRangedBarrack == true) {
			spawn(scourgeMid, checkedPosition, SuperRangeScourgeCreeps2);
		}
		
		int[] scourgeBotPosition = {SCOURGE_BOT_LINE_SPAWN_X_POS, SCOURGE_BOT_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeBot = new LinkedList<int[]>(); 
		scourgeBot.add(scourgeBotPosition);
		
		if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == false) {
			spawn(scourgeBot, checkedPosition, ScourgeCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == true) {
			spawn(scourgeBot, checkedPosition, SuperScourgeCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == true && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == false) {
			spawn(scourgeBot, checkedPosition, SuperMeeleScourgeCreeps3);
		} else if (BuildingDatabase.isDestroyedScourgeBotMeeleBarrack == false && BuildingDatabase.isDestroyedScourgeBotRangedBarrack == true) {
			spawn(scourgeBot, checkedPosition, SuperRangeScourgeCreeps3);
		}
				
	}
	

	public static <T extends Character> void spawn(Queue<int[]> positionQueue, ArrayList<int[]> checkedPosition, Queue<T> characterQueue) {
		// this method :
		// put all characters in the characterQueue into non-occupied grid position nearest to position : (XPos, YPos)
		// the ArrayList checkedPosition is to store check positions
		
		// base case :
		if (characterQueue.isEmpty() == true) {
			return;
			
		} else {
			
			// add surrounding grids into positionQueue
			if (!isChecked(checkedPosition, positionQueue.peek()[0]+1, positionQueue.peek()[1])){
				int[] newPos = {positionQueue.peek()[0]+1, positionQueue.peek()[1]};
				positionQueue.add(newPos);
			}
			
			if (!isChecked(checkedPosition, positionQueue.peek()[0], positionQueue.peek()[1]+1)){
				int[] newPos = {positionQueue.peek()[0], positionQueue.peek()[1]+1};
				positionQueue.add(newPos);
			}
			
			if (!isChecked(checkedPosition, positionQueue.peek()[0]-1, positionQueue.peek()[1])){
				int[] newPos = {positionQueue.peek()[0]-1, positionQueue.peek()[1]};
				positionQueue.add(newPos);
			}
			
			if (!isChecked(checkedPosition, positionQueue.peek()[0], positionQueue.peek()[1]-1)){
				int[] newPos = {positionQueue.peek()[0], positionQueue.peek()[1]-1};
				positionQueue.add(newPos);
			}
			
			// add the current position into checked queue
			checkedPosition.add(positionQueue.peek());
		
			// check is current position is suitable for adding a character
			if (GridFrame.gridButtonMap[positionQueue.peek()[0]][positionQueue.peek()[1]].getIsMovable() == true 
					&& GridFrame.gridButtonMap[positionQueue.peek()[0]][positionQueue.peek()[1]].getIsOccupied() == false) {
				// can only place the character onto a movable and non-occupied grid
				
				// change the grid, add a character!
				GridFrame.gridButtonMap[positionQueue.peek()[0]][positionQueue.peek()[1]].setIsOccupied(true);
				GridFrame.gridButtonMap[positionQueue.peek()[0]][positionQueue.poll()[1]].setCharacter(characterQueue.poll());
			} else {
				// discard the position
				positionQueue.poll();
			}
			
			// recursive call!
			spawn(positionQueue, checkedPosition, characterQueue);
		}
	}



	private static boolean isChecked(ArrayList<int[]> checkedPosition, int XPos, int YPos) {
		// each int[] in checkedPosition stores a pair of xpos and ypos
		boolean isChecked = false;
		
		// XPos and YPos need to be within range
		if (XPos >= 0 && XPos <GridFrame.COLUMN_NUMBER 
				&& YPos >= 0 && YPos <GridFrame.ROW_NUMBER){
			// check if the position has been visited before
			for (int[] element : checkedPosition){
				if (element[0] == XPos && element[1] == YPos){
					isChecked = true;
					break;
				}
			}
		} else{
			// not within range! unable to visit!
			isChecked = true;
		}
		
		return isChecked;
	}
	
	
}
