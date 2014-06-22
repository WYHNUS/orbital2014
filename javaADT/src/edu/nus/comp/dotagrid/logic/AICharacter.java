package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AICharacter {
	
	public static boolean isAttack = false;
	
	private int teamNumber;
	private int startingXPos;
	private int startingYPos;
	
	private int attackRange;
	private int movement;
	private int sight;
	
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
			// line creep AI
			findNearestEnemyInSight();
			
			// check if there is any enemy within sight
			if (nearestEnemyPos[0] != -1 && nearestEnemyPos[1] != -1) {
				// if yes, move towards the enemy until it's within the attack range
				moveTowardsEnemy();
				// then attack!
				AIAttack();
			} else {
				// move to next targeted position
				
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
	
	
	private void moveTowardsEnemy() {
		// move towards nearestEnemyPos until the Enemy is within AI's attack range
		
		// check if the enemy is within attack range
		if ((Math.abs(nearestEnemyPos[0] - startingXPos) + Math.abs(nearestEnemyPos[1] - startingYPos)) <= attackRange) {
			// within attack range, no need to move
			return;
		} else {
			// find nearest path in order to attack
			FindPath tempPath = new FindPath(sight);
			tempPath.findShortestPath(startingXPos, startingYPos, nearestEnemyPos[0], nearestEnemyPos[1], sight);
			
			int[][] tempPathMap = tempPath.getPath();
			
			// check each of the outer-most grid within attack range of the AI character, search for the least amount used in order to attack the enemy
			int enemyXPos = sight + nearestEnemyPos[0] - startingXPos;
			int enemyYPos = sight + nearestEnemyPos[1] - startingYPos;
			
			int shortestPathLength = movement + 1;
			int shortestPathXPos = -1; 
			int shortestPathYPos = -1;
			
			for (int x=-attackRange; x<=attackRange; x++) {
				for (int y=-attackRange; y<=attackRange; y++) {
					// within attack range
					if (Math.abs(x) + Math.abs(y) <= attackRange) {
						// within sight map
						if (enemyXPos+x >= 0 && enemyXPos+x < 2*sight && enemyYPos+y >= 0 && enemyYPos+y < 2*sight) {
							// reachable
							if (tempPathMap[enemyXPos+x][enemyYPos+y] != -1) {
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
			}
		
		}
		
	}


	private void findNearestEnemyInSight() {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
				
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
				
		findNearestEnmey(startingPosition, checkedPosition, sight);
	}
	

	private void AIAttack(){
		// attack method : attack any enemy within attack range
		
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		
		findNearestEnmey(startingPosition, checkedPosition, attackRange);
		
		// check if there is any enemy within attack range
		if (nearestEnemyPos[0] != -1 && nearestEnemyPos[1] != -1) {
			// if yes, attack ENEMY UNTIL LAST BULLET!
			do {
				isAttack = true;
				new CharacterActions(2, startingXPos, startingYPos, nearestEnemyPos[0], nearestEnemyPos[1]);
			} while ((GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() -
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedWhenAttack() >= 0)
					&& (isAttack == true));
		}
		
	}

	
	private void findNearestEnmey(Queue<int[]> uncheckedPosition, ArrayList<int[]> checkedPosition, int checkRange) {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[], if there isn't any enemy, return {-1, -1}

		// base case :
		if (uncheckedPosition.isEmpty() == true) {
			nearestEnemyPos[0] = -1;
			nearestEnemyPos[1] = -1;
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
					// return enemy's position
					nearestEnemyPos[0] = uncheckedPosition.peek()[0];
					nearestEnemyPos[1] = uncheckedPosition.poll()[1];
					return;
				} else {
					// discard the position
					uncheckedPosition.poll();
				}
			} else {
				// discard the position
				uncheckedPosition.poll();
			}
					
			// recursive call!
			findNearestEnmey(uncheckedPosition, checkedPosition, checkRange);
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
