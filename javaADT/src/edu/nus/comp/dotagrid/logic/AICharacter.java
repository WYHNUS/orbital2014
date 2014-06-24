package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
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
	
	private int targetRange = 3;
	
	ArrayList<int[]> inSightEnemyPos = new ArrayList<int[]>();
	private int[] nearestEnemyPos = new int[2];
	
	
	public AICharacter(int XPos, int YPos) {
		startingXPos = XPos;
		startingYPos = YPos;
		teamNumber = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTeamNumber();
		attackRange = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTotalPhysicalAttackArea();
		movement = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getNumberOfMovableGrid();
		sight = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getSight();
		isAttack = false;
		
		if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof LineCreep){
			boolean prepareForAttack = false;
			
			// line creep AI
			searchForInSightEnemies();
			
			// check if there is any enemy within sight
			while (!inSightEnemyPos.isEmpty() && !endRound) {
				prepareForAttack = true;
				
				// if yes, move towards the enemy until it's within the attack range
				moveTowardsEnemy();
				// then attack!
				AIAttack();
			} 
			
			// if AI is not ready for attack
			if (!prepareForAttack) {
				// move to next targeted position
				moveTowardsNextTarget();
				checkReachTargetPosition();
			}
			
		}
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Tower){
			// tower AI
			
			// attack when have enough action point
			AIAttack();
		}
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Hero){
			// comp Hero AI
			
		}
	}



	private void checkReachTargetPosition() {
		// check if the AI has moved in the targeted position's range 
		int[] targetPos = ((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		
		if (Math.abs(targetPos[0] - startingXPos) + Math.abs(targetPos[1] - startingYPos) <= targetRange) {
			// within target range, discard current target position and search for next target
			((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().remove(0);
		}
	}


	private void moveTowardsNextTarget() {
		// find nearest non-occupied grid around targeted position x,y
		int[] targetPos = ((LineCreep)GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter()).getAItargetPos().get(0);
		Queue<int[]> uncheckedPosition = new LinkedList<int[]>();
		uncheckedPosition.add(targetPos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		
		int[] nearestNonOccupiedTargetPos = new int[2];
		boolean findTarget = false;
		
		// keep selecting targeted position until a reachable targeted position is chosen
		while (!findTarget) {

			findNearestNonOccupiedPos(uncheckedPosition, checkedPosition);
			
			// store the nearest non-occupied grid's position
			int targetXPos = uncheckedPosition.peek()[0];
			int targetYPos = uncheckedPosition.peek()[1];
			
			// discard the position
			uncheckedPosition.poll();
			
			// find the path moving the AI character from (startingXPos, startingYPos) to (targetXPos, targetYPos)
			
			// store the distance between the two positions
			int distance = Math.abs(targetXPos - startingXPos) + Math.abs(targetYPos - startingYPos);
			
			FindPath tempPath = new FindPath(distance);
			tempPath.findShortestPath(startingXPos, startingYPos, targetXPos, targetYPos);
			
			// store path map into tempPathMap
			int[][] tempPathMap = tempPath.getPath();
			
			// store target's coordinates' positions in tempPathMap
			int targetMapXPos = distance + targetXPos - startingXPos;
			int targetMapYPos = distance + targetYPos - startingYPos;
			
					
			if (tempPathMap[targetMapXPos][targetMapYPos] == -1) {
				// not reachable target, do nothing
			} else {
				// find a reachable target!
				findTarget = true;
				nearestNonOccupiedTargetPos = backTrackMove(tempPathMap, targetMapXPos, targetMapYPos, movement, tempPathMap.length);
				
				// reset targeted position
				nearestNonOccupiedTargetPos[0] += (startingXPos - distance);
				nearestNonOccupiedTargetPos[1] += (startingYPos - distance);
			}
		}
		
		// now, the number of grids moved from (startingXPos, startingYPos) to (targetXPos, targetYPos) <= movement
		// and the grid position of (targetXPos, targetYPos) is on the path to targeted position
		new CharacterActions(1, startingXPos, startingYPos, nearestNonOccupiedTargetPos[0], nearestNonOccupiedTargetPos[1]);
		
		// reset starting position for AI
		startingXPos = nearestNonOccupiedTargetPos[0];
		startingYPos = nearestNonOccupiedTargetPos[1];
	}


	private void findNearestNonOccupiedPos(Queue<int[]> uncheckedPosition,
			ArrayList<int[]> checkedPosition) {
		// check nearest non-occupied position starting from the first element from the unchecked queue
		
		// add surrounding grids into position
		
		if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
			int[] newPos = {uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1]};
			uncheckedPosition.add(newPos);
		}
						
		if (!isChecked(checkedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
			int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1};
			uncheckedPosition.add(newPos);
		}
						
		if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
			int[] newPos = {uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1]};
			uncheckedPosition.add(newPos);
		}
						
		if (!isChecked(checkedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
			int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1};
			uncheckedPosition.add(newPos);
		}
						
		// add the current position into checked queue
		checkedPosition.add(uncheckedPosition.peek());
			

		// terminating condition :
		if (GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getIsMovable() == true
				&& GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getIsOccupied() == false) {
			// first element in the unchecked queue satisfy the searching condition
			return;
		} else {	
			uncheckedPosition.poll();
			// recursive call!
			findNearestNonOccupiedPos(uncheckedPosition, checkedPosition);
		}
		
	}


	private void moveTowardsEnemy() {
		// move towards nearestEnemyPos until the Enemy is within AI's attack range
		nearestEnemyPos[0] = inSightEnemyPos.get(0)[0];
		nearestEnemyPos[1] = inSightEnemyPos.get(0)[1];
		
		// check if the enemy is within attack range
		if ((Math.abs(nearestEnemyPos[0] - startingXPos) + Math.abs(nearestEnemyPos[1] - startingYPos)) <= attackRange) {
			// within attack range, no need to move
			return;
		} else {
			// find nearest path in order to attack
			FindPath tempPath = new FindPath(sight);
			tempPath.findShortestPath(startingXPos, startingYPos, nearestEnemyPos[0], nearestEnemyPos[1]);
			
			int[][] tempPathMap = tempPath.getPath();
			
			// check each of the outer-most grid within attack range of the AI character, search for the least amount used in order to attack the enemy
			int enemyXPos = sight + nearestEnemyPos[0] - startingXPos;
			int enemyYPos = sight + nearestEnemyPos[1] - startingYPos;
			
			int shortestPathLength = movement + 1;
			int shortestPathXPos = -1; 
			int shortestPathYPos = -1;
			
			// store the position for any existing path's position
			int existingPathXPos = -1; 
			int existingPathYPos = -1;
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
								existingPathXPos = enemyXPos + x;
								existingPathYPos = enemyYPos + y;
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
				// check if AI can move to the position which enemy is within attack range
				if (shortestPathLength <= movement) {
					// if yes, set the position
					shortestPathXPos += (startingXPos - sight);
					shortestPathYPos += (startingYPos - sight);
					
					// move the AI!
					new CharacterActions(1, startingXPos, startingYPos, shortestPathXPos, shortestPathYPos);
					
					// reset AI's position
					startingXPos = shortestPathXPos;
					startingYPos = shortestPathYPos;
					
				} else {
					// move towards the enemy!
					int[] movetoPos = backTrackMove(tempPathMap, existingPathXPos, existingPathYPos, movement, tempPathMap.length);
					
					// reset target position in world map frame
					movetoPos[0] += (startingXPos - sight);
					movetoPos[1] += (startingYPos - sight);
					
					// move the AI!
					new CharacterActions(1, startingXPos, startingYPos, movetoPos[0], movetoPos[1]);
					
					// reset AI's position
					startingXPos = movetoPos[0];
					startingYPos = movetoPos[1];
				}
			} else {
				// if such a path does not exist, check if there are any other enemies in sight
				if (!inSightEnemyPos.isEmpty()) {
					// no other enemy in sight! move towards the enemy and wait for chance to attack
					System.out.println("wait for chance");
					endRound = true;
				} else {
					// there are opportunities to attack other enemies, remove current enemy from target list
					inSightEnemyPos.remove(0);
					nearestEnemyPos[0] = inSightEnemyPos.get(0)[0];
					nearestEnemyPos[1] = inSightEnemyPos.get(0)[1];
				}
				
			}
		
		}
		
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
		
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		ArrayList<int[]> enemyList = new ArrayList<int[]>();
		
		findNearbyEnmeies(startingPosition, checkedPosition, attackRange, enemyList);
		
		// check if there is any enemy within attack range
		if (!enemyList.isEmpty()) {
			// if yes, attack ENEMY UNTIL LAST BULLET!
			do {
				isAttack = true;
				new CharacterActions(2, startingXPos, startingYPos, enemyList.get(0)[0], enemyList.get(0)[1]);
			} while ((GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() -
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedWhenAttack() >= 0)
					&& (isAttack == true));
		}
		
		endRound = true;
	}

	
	private void findNearbyEnmeies(Queue<int[]> uncheckedPosition, ArrayList<int[]> checkedPosition, int checkRange,
			ArrayList<int[]> enemyStoreList) {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[], if there isn't any enemy, return {-1, -1}

		// base case : 
		if (uncheckedPosition.isEmpty() == true) {
			return;
		} else {
			
			// add surrounding grids into position
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			} 
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
					int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(checkRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
					int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1};
					uncheckedPosition.add(newPos);
				}
			}
			
			// add the current position into checked queue
			checkedPosition.add(uncheckedPosition.peek());
			
			// check is current position has a character
			if (GridFrame.gridButtonMap[uncheckedPosition.peek()[0]][uncheckedPosition.peek()[1]].getCharacter() != null) {
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
					
			// recursive call!
			findNearbyEnmeies(uncheckedPosition, checkedPosition, checkRange, enemyStoreList);
		}
	}



	private boolean isWithinRange(int checkRange, int xPos, int yPos) {
		// check if position xPos, yPos is within attackRange from character with coordinates [startingXPos, startingYPos]
		return (Math.abs(xPos - startingXPos) + Math.abs(yPos - startingYPos) <= checkRange);
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
