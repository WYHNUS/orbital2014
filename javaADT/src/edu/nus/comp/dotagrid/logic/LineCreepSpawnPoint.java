package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LineCreepSpawnPoint {
	
	public static final int SPAWN_NORMAL_ROUND_INDEX = 10;
	public static final int SPAWN_ENHANCED_ROUND_INDEX = 50;
	public static final int UPGRADE_ALL_LINECREEPS_ROUND_INDEX = 200;
	public static final int UPGRADE_RANGED_LINECREEPS_NUMBER_ROUND_INDEX = 600;
	
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
		 * spawn new normal wave every spawnNormalRoundIndex turns
		 * spawn new extended wave every spawnEnhancedRoundIndex turns
		*/
		
		if (GameFrame.turn % SPAWN_NORMAL_ROUND_INDEX == 0) {
			createWave();
		}
		
	}



	private static void createWave() {
		// reset level for creeps : maximum level 30
		if (LineCreep.levelSentinel <= LineCreep.MAXIMUM_LEVEL) {
			LineCreep.levelSentinel = GameFrame.turn / UPGRADE_ALL_LINECREEPS_ROUND_INDEX;
		}
		if (LineCreep.levelScourge <= LineCreep.MAXIMUM_LEVEL) {
			LineCreep.levelScourge = GameFrame.turn / UPGRADE_ALL_LINECREEPS_ROUND_INDEX;
		}
		
		
		// change the number of spawning creeps
		if (GameFrame.turn <= UPGRADE_RANGED_LINECREEPS_NUMBER_ROUND_INDEX) {
			MEELE_CREEP_NUMBER = 3 + GameFrame.turn / UPGRADE_ALL_LINECREEPS_ROUND_INDEX;
			RANGED_CREEP_NUMBER = 1 + GameFrame.turn / UPGRADE_RANGED_LINECREEPS_NUMBER_ROUND_INDEX;
			SIEGE_CREEP_NUMBER = 1 + GameFrame.turn / UPGRADE_RANGED_LINECREEPS_NUMBER_ROUND_INDEX;
		}
		

		// create line creep database
		new LineCreepDatabase();
		
		
		// each spawn point spawn MEELE_CREEP_NUMBER meele creeps + RANGED_CREEP_NUMBER ranged creep
		
		// spawn sentinel line creeps
		
		Queue <LineCreep> topSentinelCreeps = new LinkedList<LineCreep>();
		
		int[] sentinelTopPosition = {SENTINEL_TOP_LINE_SPAWN_X_POS, SENTINEL_TOP_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelTop = new LinkedList<int[]>(); 
		sentinelTop.add(sentinelTopPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition1 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedScourgeTopMeeleBarrack) {
			// create normal wave of sentinel meele creeps
			LineCreepDatabase.createSentinelMeeleCreeps(topSentinelCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of sentinel meele creeps
			LineCreepDatabase.createSentinelSuperMeeleCreeps(topSentinelCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedScourgeTopRangedBarrack) {
			// create normal wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelRangedCreeps(topSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSiegeCreeps(topSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelSuperRangedCreeps(topSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSuperSiegeCreeps(topSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setSentinelTopCreepsTargets(topSentinelCreeps);
		
		// spawn!
		spawn(sentinelTop, checkedPosition1, topSentinelCreeps);
		
		
		
		// mid line

		Queue <LineCreep> midSentinelCreeps = new LinkedList<LineCreep>();
		
		int[] sentinelMidPosition = {SENTINEL_MID_LINE_SPAWN_X_POS, SENTINEL_MID_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelMid = new LinkedList<int[]>(); 
		sentinelMid.add(sentinelMidPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition2 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedScourgeMidMeeleBarrack) {
			// create normal wave of sentinel meele creeps
			LineCreepDatabase.createSentinelMeeleCreeps(midSentinelCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of sentinel meele creeps
			LineCreepDatabase.createSentinelSuperMeeleCreeps(midSentinelCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedScourgeMidRangedBarrack) {
			// create normal wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelRangedCreeps(midSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSiegeCreeps(midSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelSuperRangedCreeps(midSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSuperSiegeCreeps(midSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setSentinelMidCreepsTargets(midSentinelCreeps);
		
		// spawn!
		spawn(sentinelMid, checkedPosition2, midSentinelCreeps);
		
		
		
		// bottom line
		
		Queue <LineCreep> botSentinelCreeps = new LinkedList<LineCreep>();
		
		int[] sentinelBotPosition = {SENTINEL_BOT_LINE_SPAWN_X_POS, SENTINEL_BOT_LINE_SPAWN_Y_POS};
		Queue<int[]> sentinelBot = new LinkedList<int[]>(); 
		sentinelBot.add(sentinelBotPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition3 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedScourgeBotMeeleBarrack) {
			// create normal wave of sentinel meele creeps
			LineCreepDatabase.createSentinelMeeleCreeps(botSentinelCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of sentinel meele creeps
			LineCreepDatabase.createSentinelSuperMeeleCreeps(botSentinelCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedScourgeBotRangedBarrack) {
			// create normal wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelRangedCreeps(botSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSiegeCreeps(botSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of sentinel ranged + siege creeps
			LineCreepDatabase.createSentinelSuperRangedCreeps(botSentinelCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createSentinelSuperSiegeCreeps(botSentinelCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setSentinelBotCreepsTargets(botSentinelCreeps);
		
		// spawn!
		spawn(sentinelBot, checkedPosition3, botSentinelCreeps);
		
		
		
		
		// spawn scourge line creeps
		

		Queue <LineCreep> topScourgeCreeps = new LinkedList<LineCreep>();
		
		int[] scourgeTopPosition = {SCOURGE_TOP_LINE_SPAWN_X_POS, SCOURGE_TOP_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeTop = new LinkedList<int[]>(); 
		scourgeTop.add(scourgeTopPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition4 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedSentinelTopMeeleBarrack) {
			// create normal wave of scourge meele creeps
			LineCreepDatabase.createScourgeMeeleCreeps(topScourgeCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of scourge meele creeps
			LineCreepDatabase.createScourgeSuperMeeleCreeps(topScourgeCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedSentinelTopRangedBarrack) {
			// create normal wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeRangedCreeps(topScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSiegeCreeps(topScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeSuperRangedCreeps(topScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSuperSiegeCreeps(topScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setScourgeTopCreepsTargets(topScourgeCreeps);
		
		// spawn!
		spawn(scourgeTop, checkedPosition4, topScourgeCreeps);
		
		
		
		// mid line

		Queue <LineCreep> midScourgeCreeps = new LinkedList<LineCreep>();
		
		int[] scourgeMidPosition = {SCOURGE_MID_LINE_SPAWN_X_POS, SCOURGE_MID_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeMid = new LinkedList<int[]>(); 
		scourgeMid.add(scourgeMidPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition5 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedSentinelMidMeeleBarrack) {
			// create normal wave of scourge meele creeps
			LineCreepDatabase.createScourgeMeeleCreeps(midScourgeCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of scourge meele creeps
			LineCreepDatabase.createScourgeSuperMeeleCreeps(midScourgeCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedSentinelMidRangedBarrack) {
			// create normal wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeRangedCreeps(midScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSiegeCreeps(midScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeSuperRangedCreeps(midScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSuperSiegeCreeps(midScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setScourgeMidCreepsTargets(midScourgeCreeps);
		
		// spawn!
		spawn(scourgeMid, checkedPosition5, midScourgeCreeps);
		
		
		
		// bottom line
		
		Queue <LineCreep> botScourgeCreeps = new LinkedList<LineCreep>();
		
		int[] scourgeBotPosition = {SCOURGE_BOT_LINE_SPAWN_X_POS, SCOURGE_BOT_LINE_SPAWN_Y_POS};
		Queue<int[]> scourgeBot = new LinkedList<int[]>(); 
		scourgeBot.add(scourgeBotPosition);
		
		// initialize checkedPosition
		ArrayList<int[]> checkedPosition6 = new ArrayList<int[]>();
		
		if (!BuildingDatabase.isDestroyedSentinelBotMeeleBarrack) {
			// create normal wave of scourge meele creeps
			LineCreepDatabase.createScourgeMeeleCreeps(botScourgeCreeps, MEELE_CREEP_NUMBER);
		} else {
			// create super wave of scourge meele creeps
			LineCreepDatabase.createScourgeSuperMeeleCreeps(botScourgeCreeps, MEELE_CREEP_NUMBER);
		}
		
		if (!BuildingDatabase.isDestroyedSentinelBotRangedBarrack) {
			// create normal wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeRangedCreeps(botScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSiegeCreeps(botScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		} else {
			// create super wave of scourge ranged + siege creeps
			LineCreepDatabase.createScourgeSuperRangedCreeps(botScourgeCreeps, RANGED_CREEP_NUMBER);
			
			// add siege creeps every spawnEnhancedRoundIndex turns
			if (GameFrame.turn % SPAWN_ENHANCED_ROUND_INDEX == 0) {
				LineCreepDatabase.createScourgeSuperSiegeCreeps(botScourgeCreeps, SIEGE_CREEP_NUMBER);
			}
		}
		
		// add targeted position
		LineCreepDatabase.setScourgeBotCreepsTargets(botScourgeCreeps);
		
		// spawn!
		spawn(scourgeBot, checkedPosition6, botScourgeCreeps);
		
		
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
					&& GridFrame.gridButtonMap[positionQueue.peek()[0]][positionQueue.peek()[1]].getCharacter() == null) {
				// can only place the character onto a movable and non-occupied grid
				
				// change the grid, add a character!
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
