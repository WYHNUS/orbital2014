package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class AICharacter {
	public static boolean isAttack = false;
	private boolean endRound = false;
	
	private int teamNumber;
	private int startingXPos;
	private int startingYPos;
	
	private int attackRange;
	private int movement;
	private int sight;
	
	public static final int LINE_CREEP_TARGET_RANGE = 3;
	public static final int NEUTRAL_CREEP_ATTACK_RANGE = 4;
	public static final int NEUTRAL_CREEP_TARGET_RANGE = 4;
	public static final int NEUTRAL_CREEP_RETURN_INDEX = 10;
	
	LinkedList<int[]> inSightEnemyPos = new LinkedList<int[]>();
	private int[] nearestEnemyPos = new int[2];
	private int enemyCounter;
	
	
	public AICharacter(int XPos, int YPos) {
		startingXPos = XPos;
		startingYPos = YPos;
		teamNumber = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTeamNumber();
		attackRange = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTotalPhysicalAttackArea();
		movement = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getNumberOfMovableGrid();
		sight = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getSight();
		isAttack = false;
		
		if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof LineCreep){
			System.out.println("LineCreep AI:   " + GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getName());
			System.out.println("Starting Position :  XPos = " + startingXPos + "     YPos = " + startingYPos);
			
			// check if AI has enough AP to perform any actions
			if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() >= 
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedInMovingOneGrid()){
				
				boolean prepareForAttack = false;
				// line creep AI
				searchForInSightEnemies();
				sortInSightEnemiesList();
				
				// check if there is any enemy within sight
				while (!inSightEnemyPos.isEmpty() && !endRound) {
					prepareForAttack = true;
					System.out.println("move to attack");
					// if yes, move towards the enemy until it's within the attack range
					moveTowardsEnemy();
					// then attack!
					AIAttack();
				} 
				
				// if AI is not ready for attack
				if (!prepareForAttack) {
					// move to next targeted position
					System.out.println("move to target positions");
					moveTowardsNextTarget(1);
					checkReachTargetPosition();
				}
			} else {
				System.out.println("no more AP to perform any actions!");
			}
			
			System.out.println("end AI round");
			System.out.println();
		}
		
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof NeutralCreep){
			// NeutralCreep AI
			System.out.println("NeutralCreep AI:   " + GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getName());
			System.out.println("Starting Position :  XPos = " + startingXPos + "     YPos = " + startingYPos);
			
			// check if need to move back to base position
			if (((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).isMoveBack()) {
				// move back!
				moveTowardsNextTarget(2);
				
			} else {
				boolean prepareForAttack = false;
				
				// check and attack enemy
				searchEnemiesForNeutralCreep();
				
				while (!inSightEnemyPos.isEmpty() && !endRound) {
					prepareForAttack = true;
					System.out.println("move to attack");
					// if yes, move towards the enemy until it's within the attack range
					moveTowardsEnemy();
					// then attack!
					AIAttack();
				}
				
				checkToMoveBack(prepareForAttack);
			}
			
			System.out.println("end AI round");
			System.out.println();
		}
		
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Tower){
			// tower AI
			System.out.println("Tower AI:   " + GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getName());
			System.out.println("Starting Position :  XPos = " + startingXPos + "     YPos = " + startingYPos);
			
			// attack when have enough action point
			AIAttack();
			
			System.out.println("end AI round");
			System.out.println();
		}
		
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Hero){
			// comp Hero AI
			
		}
	}



	private void sortInSightEnemiesList() {
		// sort the inSightEnemyPos to arrange enemies based on their attackPriority
		Collections.sort(inSightEnemyPos, new Comparator<int[]>() {
			@Override
			public int compare(int[] char1Pos, int[] char2Pos) {
				// compare current attack priority for characters at position char1Pos and char2Pos
				return (GridFrame.gridButtonMap[char1Pos[0]][char1Pos[1]].getCharacter().getCurrentAttackPriority() 
						- GridFrame.gridButtonMap[char2Pos[0]][char2Pos[1]].getCharacter().getCurrentAttackPriority());
			}
		});
	}



	private void searchEnemiesForNeutralCreep() {
		// find the coordinates for in range enemy for neutral creeps, store their coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
						
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
						
		findNearbyEnmeies(startingPosition, checkedPosition, NEUTRAL_CREEP_ATTACK_RANGE, inSightEnemyPos);
	}



	private void checkToMoveBack(boolean hasAttack) {
		// check if the neutral creep need to move back to targeted position
		int[] targetPos = ((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		if (Math.abs(startingXPos-targetPos[0]) + Math.abs(startingYPos-targetPos[1]) > NEUTRAL_CREEP_RETURN_INDEX) {
			// out of range, set move back to true
			((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).setMoveBack(true);
		} else {
			// in range, check if within target range
			if (hasAttack) {
				((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).setMoveBack(false);
			} else {
				if (Math.abs(targetPos[0] - startingXPos) + Math.abs(targetPos[1] - startingYPos) <= NEUTRAL_CREEP_TARGET_RANGE) {
					((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).setMoveBack(false);
				} else {
					((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).setMoveBack(true);
				}
			}
		}
	}



	private void checkReachTargetPosition() {
		// check if the AI has moved in the targeted position's range 
		int[] targetPos = ((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		
		if (Math.abs(targetPos[0] - startingXPos) + Math.abs(targetPos[1] - startingYPos) <= LINE_CREEP_TARGET_RANGE) {
			System.out.println("reach target position");
			// within target range, discard current target position and search for next target
			((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().remove(0);
		}
	}


	private void moveTowardsNextTarget(int index) {
		// index 1 : Line Creep
		// index 2 : Neutral Creep
		
		// draw 2D map from current position
		int[] targetPos; 
		if (index == 1) {
			targetPos = ((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		} else if (index == 2) {
			targetPos = ((NeutralCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		} else {
			targetPos = null;
		}
		
		System.out.println("target position is at : x = " + targetPos[0] + "   y = " + targetPos[1]);
		
		int distance = Math.abs(targetPos[0] - startingXPos) + Math.abs(targetPos[1] - startingYPos);
		
		FindPath tempPath = new FindPath(distance);
		tempPath.findShortestPath(startingXPos, startingYPos, targetPos[0], targetPos[1]);
		int[][] tempPathMap = tempPath.getPath();
		
		/*
		for (int i=0; i<tempPathMap.length; i++) {
			for (int j=0; j<tempPathMap.length; j++) {
				System.out.printf("%3s", tempPathMap[j][i]);
			}
			System.out.println();
		}
		*/
		
		// backtrack from targeted position to search for nearest reachable position
		Queue<int[]> uncheckedPosition = new LinkedList<int[]>();
		
		// reset targetPos in tempMap coordinates
		int[] startingUncheckedPos = {targetPos[0] + distance - startingXPos, targetPos[1] + distance - startingYPos};
		uncheckedPosition.add(startingUncheckedPos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		
		int[] nearestNonOccupiedTargetPos = findNearestReachableGrid(tempPathMap, uncheckedPosition, checkedPosition, index); 
		
		// check if the AI can move to the wait position
		if (tempPathMap[nearestNonOccupiedTargetPos[0]][nearestNonOccupiedTargetPos[1]] > movement) {
			// backtrack and move!
			// move towards the enemy!
			nearestNonOccupiedTargetPos = backTrackMove(tempPathMap, nearestNonOccupiedTargetPos[0], nearestNonOccupiedTargetPos[1], movement, tempPathMap.length);
		}
		
		// reset target position in world map frame
		nearestNonOccupiedTargetPos[0] += (startingXPos - distance);
		nearestNonOccupiedTargetPos[1] += (startingYPos - distance);
			
		// move the AI!
		new CharacterActions(1, startingXPos, startingYPos, nearestNonOccupiedTargetPos[0], nearestNonOccupiedTargetPos[1]);
			
		// reset AI's position
		startingXPos = nearestNonOccupiedTargetPos[0];
		startingYPos = nearestNonOccupiedTargetPos[1];
	}


	private void moveTowardsEnemy() {
		// move towards nearestEnemyPos until the Enemy is within AI's attack range
		nearestEnemyPos[0] = inSightEnemyPos.get(0)[0];
		nearestEnemyPos[1] = inSightEnemyPos.get(0)[1];
		
		// check if the enemy is within attack range
		if ((Math.abs(nearestEnemyPos[0] - startingXPos) + Math.abs(nearestEnemyPos[1] - startingYPos)) <= attackRange) {
			// within attack range, no need to move
			System.out.println("within attack range!");
			return;
		} else {
			// find nearest path in order to attack
			FindPath tempPath = new FindPath(sight);
			tempPath.findShortestPath(startingXPos, startingYPos, nearestEnemyPos[0], nearestEnemyPos[1]);
			
			int[][] tempPathMap = tempPath.getPath();
			
			// check each of the outer-most grid within attack range of the AI character, search for the least amount used in order to attack the enemy
			int enemyXPos = sight + nearestEnemyPos[0] - startingXPos;
			int enemyYPos = sight + nearestEnemyPos[1] - startingYPos;

			// store the position for any existing path's position
			int shortestPathLength = Integer.MAX_VALUE;
			int shortestPathXPos = -1; 
			int shortestPathYPos = -1;
			boolean havePath = false;
			
			for (int x=-attackRange; x<=attackRange; x++) {
				for (int y=-attackRange; y<=attackRange; y++) {
					// within attack range
					if (Math.abs(x) + Math.abs(y) <= attackRange) {
						// within sight map
						if (enemyXPos+x >= 0 && enemyXPos+x < 2*sight && enemyYPos+y >= 0 && enemyYPos+y < 2*sight) {
							// reachable
							if (tempPathMap[enemyXPos+x][enemyYPos+y] != -1) {
								havePath = true;
								
								// check if the path is the shortest path
								if (tempPathMap[enemyXPos+x][enemyYPos+y] < shortestPathLength) {
									shortestPathLength = tempPathMap[enemyXPos+x][enemyYPos+y];
									shortestPathXPos = enemyXPos + x;
									shortestPathYPos = enemyYPos + y;
								}
							}
						}
					}
				}
			}
			
			// check if there is a existing path towards the target enemy
			if (havePath) {
				System.out.println("have path");
				// check if AI can move to the position which enemy is within attack range in current round
				if (shortestPathLength <= movement) {
					System.out.println("can be reached in this round!");
					// if yes, set the position
					shortestPathXPos += (startingXPos - sight);
					shortestPathYPos += (startingYPos - sight);
					
					// move the AI!
					new CharacterActions(1, startingXPos, startingYPos, shortestPathXPos, shortestPathYPos);
					
					// reset AI's position
					startingXPos = shortestPathXPos;
					startingYPos = shortestPathYPos;
					
					movement = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getNumberOfMovableGrid();
					
				} else {
					System.out.println("not reachable in this round, but still move towards enemy!");
					
					// move towards the enemy!
					int[] movetoPos = backTrackMove(tempPathMap, shortestPathXPos, shortestPathYPos, movement, tempPathMap.length);
					
					// reset target position in world map frame
					movetoPos[0] += (startingXPos - sight);
					movetoPos[1] += (startingYPos - sight);
					
					System.out.println("is movable :  " + GridFrame.gridButtonMap[movetoPos[0]][movetoPos[1]].getIsMovable());
					
					// move the AI!
					new CharacterActions(1, startingXPos, startingYPos, movetoPos[0], movetoPos[1]);
					
					// reset AI's position
					startingXPos = movetoPos[0];
					startingYPos = movetoPos[1];
					
					// since no more action point
					endRound = true;
				}
				
			} else {
				System.out.println("no existing path -...-");
				// if such a path does not exist, check if the in sight enemy list has been traveled once
				if (inSightEnemyPos.size() <= enemyCounter) {
					// no other enemy in sight! move towards the enemy and wait for chance to attack
					Queue<int[]> uncheckedPosition = new LinkedList<int[]>();
					int[] startingPos = {enemyXPos, enemyYPos};
					uncheckedPosition.add(startingPos);
					
					ArrayList<int[]> checkedPos = new ArrayList<int[]>();
					
					int[] movetoPos = findNearestReachableGrid(tempPathMap, uncheckedPosition, checkedPos, 1);
					
					// check if the AI can move to the wait position
					if (tempPathMap[movetoPos[0]][movetoPos[1]] > movement) {
						// backtrack and move...
						
						// move towards the enemy!
						movetoPos = backTrackMove(tempPathMap, movetoPos[0], movetoPos[1], movement, tempPathMap.length);
					}
					
					// reset target position in world map frame
					movetoPos[0] += (startingXPos - sight);
					movetoPos[1] += (startingYPos - sight);
						
					// move the AI!
					new CharacterActions(1, startingXPos, startingYPos, movetoPos[0], movetoPos[1]);
						
					// reset AI's position
					startingXPos = movetoPos[0];
					startingYPos = movetoPos[1];
					
					System.out.println("wait for chance");
					endRound = true;
				} else {
					// there are opportunities to attack other enemies, put current enemy to end of the target list
					inSightEnemyPos.add(inSightEnemyPos.remove(0));
					enemyCounter++;
					nearestEnemyPos[0] = inSightEnemyPos.get(0)[0];
					nearestEnemyPos[1] = inSightEnemyPos.get(0)[1];
				}
				
			}
		
		}
		
	}


	private int[] findNearestReachableGrid(int[][] tempPathMap, Queue<int[]> uncheckedPosition,
			ArrayList<int[]> checkedPosition, int index) {
		// starting from position X and Y, search for nearest reachable grid
		
		// add surrounding grids into position
		int mapRange = tempPathMap.length;
		
		if (canBeAdded(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1], mapRange, index)){
			int[] newPos = {uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1]};
			uncheckedPosition.add(newPos);
		}
						
		if (canBeAdded(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1, mapRange, index)){
			int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1};
			uncheckedPosition.add(newPos);
		}
						
		if (canBeAdded(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1], mapRange, index)){
			int[] newPos = {uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1]};
			uncheckedPosition.add(newPos);
		}
						
		if (canBeAdded(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1, mapRange, index)){
			int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1};
			uncheckedPosition.add(newPos);
		}
						
		// add the current position into checked queue
		int[] temp = uncheckedPosition.peek().clone();
		checkedPosition.add(temp);
		
		// terminating condition :
		if (tempPathMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]] >= 0) {
			// first element in the unchecked queue satisfy the searching condition
			int[] result = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]};
			return result;
		} else {	
			uncheckedPosition.poll();
			// recursive call!
			int[] result = findNearestReachableGrid(tempPathMap, uncheckedPosition, checkedPosition, index);
			return result;
		}
	}



	private boolean canBeAdded(ArrayList<int[]> checkedPosition, Queue<int[]> uncheckedPosition, int XPos, int YPos, int mapRange, int index) {
		// each int[] in checkedPosition stores a pair of xpos and ypos
		boolean isUnchecked = true;
				
		// XPos and YPos need to be within range
		if (XPos >= 0 && XPos <mapRange 
				&& YPos >= 0 && YPos <mapRange){
			// check if the position has been visited before
			for (int[] element : checkedPosition){
				if (element[0] == XPos && element[1] == YPos){
					isUnchecked = false;
					break;
				}
			}
			
			// check if the position has already been added
			for (int[] element : uncheckedPosition){
				if (element[0] == XPos && element[1] == YPos){
					isUnchecked = false;
					break;
				}
			}
		} else{
			// not within range! unable to visit!
			isUnchecked = false;
		}
		
		if (index == 2) {
			// check if the grid is movable on the grid map
			if (!GridFrame.gridButtonMap[startingXPos+XPos-(mapRange-1)/2][startingYPos+YPos-(mapRange-1)/2].getIsMovable()) {
				// non-movable grid cannot be added
				isUnchecked = false;
			}
		}
						
		return isUnchecked;
	}



	private int[] backTrackMove(int[][] map, int xPos, int yPos, int searchNumber, int mapRange) {
		// back track and search for the grid number which satisfies searchNumber condition
		
		while (map[xPos][yPos] > searchNumber) {
			if (xPos-1 >= 0 && xPos-1 < mapRange
					&& (map[xPos-1][yPos] == map[xPos][yPos] - 1)) {
				xPos--;
			} else if (xPos+1 >= 0 && xPos+1 < mapRange
					&& (map[xPos+1][yPos] == map[xPos][yPos] - 1)) {
				xPos++;
			} else if (yPos-1 >= 0 && yPos-1 < mapRange
					&& (map[xPos][yPos-1] == map[xPos][yPos] - 1)) {
				yPos--;
			} else if (yPos+1 >= 0 && yPos+1 < mapRange
					&& (map[xPos][yPos+1] == map[xPos][yPos] - 1)) {
				yPos++;
			} 
		}
		
		int[] result = {xPos, yPos};
		
		return result;
	}


	private void searchForInSightEnemies() {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
				
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
				
		findNearbyEnmeies(startingPosition, checkedPosition, sight, inSightEnemyPos);
	}
	

	
	private void AIAttack(){
		// attack method : attack any enemy within attack range
		boolean hasAttack = false;
		
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		LinkedList<int[]> enemyList = new LinkedList<int[]>();
		
		findNearbyEnmeies(startingPosition, checkedPosition, attackRange, enemyList);
		
		// check if there is any enemy within attack range
		if (!enemyList.isEmpty()) {
			// if yes, attack ENEMY UNTIL LAST BULLET!
			do {
				hasAttack = true;
				isAttack = true;
				new CharacterActions(2, startingXPos, startingYPos, enemyList.get(0)[0], enemyList.get(0)[1]);
			} while ((GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() -
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedWhenAttack() >= 0)
					&& (isAttack == true));
		}
		
		if (hasAttack)	endRound = true;
	}

	
	private void findNearbyEnmeies(Queue<int[]> uncheckedPosition, ArrayList<int[]> checkedPosition, int checkRange,
			LinkedList<int[]> enemyStoreList) {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[], if there isn't any enemy, return {-1, -1}

		// base case : 
		if (uncheckedPosition.isEmpty() == true) {
			return;
		} else {
			
			// add surrounding grids into position
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			} 
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
				if (!isChecked(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
					int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
				if (!isChecked(checkedPosition, uncheckedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
					int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1};
					uncheckedPosition.add(newPos);
				}
			}
			
			// add the current position into checked queue
			checkedPosition.add(uncheckedPosition.peek());
			
			// check is current position has a character
			if (GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getCharacter() != null) {
				// check if the character is attackable
				if (GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getCharacter().isAttackable()) {
					System.out.println("name : " + GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getCharacter().getName());
					// check if the character is enemy's character
					if (GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getCharacter().getTeamNumber() != this.teamNumber){
						// add enemy's position in to inSightEnemyPos ArrayList
						int[] temp = {uncheckedPosition.peek()[0], uncheckedPosition.poll()[1]};
						enemyStoreList.add(temp);
					} else {
						// discard the position
						uncheckedPosition.poll();
					}
				} else {
					// discard the position
					uncheckedPosition.poll();
				}
			} else {
				// discard the position
				uncheckedPosition.poll();
			}
					
			// recursive call!
			findNearbyEnmeies(uncheckedPosition, checkedPosition, checkRange, enemyStoreList);
		}
	}



	private boolean isWithinRange(int checkRange, int xPos, int yPos) {
		// check if position xPos, yPos is within attackRange from character with coordinates [startingXPos, startingYPos]
		return (Math.abs(xPos - startingXPos) + Math.abs(yPos - startingYPos) <= checkRange);
	}

	
	public static boolean isChecked(ArrayList<int[]> checkedPosition, Queue<int[]> uncheckedPosition, int XPos, int YPos) {
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
			
			for (int[] element : uncheckedPosition){
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
