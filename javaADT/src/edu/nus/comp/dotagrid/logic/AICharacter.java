package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AICharacter {
	
	public static boolean isAttack = false;
	
	private int teamNumber;
	private int startingXPos;
	private int startingYPos;
	
	private int[] nearestEnmeyPos = new int[2];
	
	
	public AICharacter(int XPos, int YPos) {
		startingXPos = XPos;
		startingYPos = YPos;
		teamNumber = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTeamNumber();
		isAttack = false;

		if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof LineCreep){
			// line creep AI
			
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
	
	public void AIAttack(){
		// attack method : attack any enemy in sight
		int attackRange = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTotalPhysicalAttackArea();
		
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[]
		int[] pos = {startingXPos, startingYPos};
		Queue<int[]> startingPosition = new LinkedList<int[]>();
		startingPosition.add(pos);
		
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		
		findNearestEnmey(startingPosition, checkedPosition, attackRange);
		
		// check if there is any enemy within attack range
		if (nearestEnmeyPos[0] != -1 && nearestEnmeyPos[1] != -1) {
			// if yes, attack ENEMY UNTIL LAST BULLET!
			do {
				isAttack = true;
				new CharacterActions(2, startingXPos, startingYPos, nearestEnmeyPos[0], nearestEnmeyPos[1]);
			} while ((GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() -
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedWhenAttack() >= 0)
					&& (isAttack == true));
		}
		
		
	}

	
	private void findNearestEnmey(Queue<int[]> uncheckedPosition, ArrayList<int[]> checkedPosition, int attackRange) {
		// find the coordinates for nearest enemy within attack range, store its coordinates in an int[], if there isn't any enemy, return {-1, -1}

		// base case :
		if (uncheckedPosition.isEmpty() == true) {
			nearestEnmeyPos[0] = -1;
			nearestEnmeyPos[1] = -1;
			return;
			
		} else {
			
			// add surrounding grids into position
			
			if (isWithinRange(attackRange, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]+1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			} 
			
			if (isWithinRange(attackRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1)){
					int[] newPos = {uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]+1};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(attackRange, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
				if (!isChecked(checkedPosition, uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1])){
					int[] newPos = {uncheckedPosition.peek()[0]-1, uncheckedPosition.peek()[1]};
					uncheckedPosition.add(newPos);
				}
			}
			
			if (isWithinRange(attackRange, uncheckedPosition.peek()[0], uncheckedPosition.peek()[1]-1)){
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
					nearestEnmeyPos[0] = uncheckedPosition.peek()[0];
					nearestEnmeyPos[1] = uncheckedPosition.poll()[1];
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
			findNearestEnmey(uncheckedPosition, checkedPosition, attackRange);
		}
	}



	private boolean isWithinRange(int attackRange, int xPos, int yPos) {
		// check if position xPos, yPos is within attackRange from character with coordinates [startingXPos, startingYPos]
		return (Math.abs(xPos - startingXPos) + Math.abs(yPos - startingYPos) <= attackRange);
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
