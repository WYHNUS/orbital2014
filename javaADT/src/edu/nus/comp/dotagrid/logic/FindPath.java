package edu.nus.comp.dotagrid.logic;


import java.util.LinkedList;
import java.util.Queue;

public class FindPath {
	
	private int[][] path;
	private int pathLength;
	
	private int pathXPos, pathYPos;
	
	public Queue<int[]> pathQueue;
	
	
	// constructors
	public FindPath(){}
	
	public FindPath(int movementPoint) {
		pathLength = 1 + movementPoint * 2;
		path = new int[pathLength][pathLength];
		
		// reset the 2D array
		for (int i=0; i<pathLength; i++) {
			for (int j=0; j<pathLength; j++) {
				path[i][j] = -1;
			}
		}
		
		pathXPos = movementPoint;
		pathYPos = movementPoint;
		
		// set the starting position to be 0
		path[pathXPos][pathYPos] = 0;
		
		pathQueue = new LinkedList<int[]>();
	}
	
	
	// methods
	
	// highlight movable grids
	public static void highlightMovableGrids(int startingXPos, int startingYPos, int movementPoint){
		// base condition:
		if (movementPoint <= 0 || startingXPos <= 0 || startingYPos <= 0) {
			
		} else {
			// recursive call 
			
			if (GridFrame.gridButtonMap[startingXPos-1][startingYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos-1][startingYPos].getIsOccupied() == false) {
				
					GridFrame.highlightedMap[startingXPos-1][startingYPos] = 1;
					highlightMovableGrids(startingXPos - 1, startingYPos, movementPoint - 1);
				
			}
				
			if (GridFrame.gridButtonMap[startingXPos][startingYPos-1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos-1].getIsOccupied() == false) {
				
					GridFrame.highlightedMap[startingXPos][startingYPos-1] = 1;
					highlightMovableGrids(startingXPos, startingYPos - 1, movementPoint - 1);
				
			}
			
			if (GridFrame.gridButtonMap[startingXPos][startingYPos+1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos+1].getIsOccupied() == false) {
				
					GridFrame.highlightedMap[startingXPos][startingYPos+1] = 1;
					highlightMovableGrids(startingXPos, startingYPos + 1, movementPoint - 1);
				
			} 
			
			if (GridFrame.gridButtonMap[startingXPos+1][startingYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos+1][startingYPos].getIsOccupied() == false) {
				
					GridFrame.highlightedMap[startingXPos+1][startingYPos] = 1;
					highlightMovableGrids(startingXPos + 1, startingYPos, movementPoint - 1);
				
			}
		}
	}
	
	
	
	// find shortest path between two points
	public int findShortestPath(int startingXPos, int startingYPos, int XPos, int YPos, int movementPoint){
		createShortestPath(pathXPos, pathYPos, startingXPos, startingYPos, movementPoint);
		
		return path[XPos - startingXPos + movementPoint][YPos - startingYPos + movementPoint];
	}
	
	
	// create path int-2D-array which store the shortest path to each grid
	private void createShortestPath(int pathXPos, int pathYPos, int startingXPos, int startingYPos, int movementPoint) {
		
		// base case :
		if (movementPoint <= 0) {
			// no need to do anything
		} else {
			
			// can move only if the grid is movable and not occupied
			// and only need to calculate if the position has not been calculated before

			if (GridFrame.gridButtonMap[startingXPos-1][startingYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos-1][startingYPos].getIsOccupied() == false
				&& path[pathXPos-1][pathYPos] == -1) {
				
					// set the value for this position
					setPathValue(pathXPos-1, pathYPos, pathLength);
					
					int[] tempArray = new int[5];
					tempArray[0] = pathXPos-1;
					tempArray[1] = pathYPos;
					tempArray[2] = startingXPos-1;
					tempArray[3] = startingYPos;
					tempArray[4] = movementPoint-1;
					
					pathQueue.add(tempArray);
			}
				
			if (GridFrame.gridButtonMap[startingXPos][startingYPos-1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos-1].getIsOccupied() == false
				&& path[pathXPos][pathYPos-1] == -1) {
				
					// set the value for this position
					setPathValue(pathXPos, pathYPos-1, pathLength);
					
					int[] tempArray = new int[5];
					tempArray[0] = pathXPos;
					tempArray[1] = pathYPos-1;
					tempArray[2] = startingXPos;
					tempArray[3] = startingYPos-1;
					tempArray[4] = movementPoint-1;
					
					pathQueue.add(tempArray);
				
			}
			
			if (GridFrame.gridButtonMap[startingXPos][startingYPos+1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos+1].getIsOccupied() == false
				&& path[pathXPos][pathYPos+1] == -1) {
				
					// set the value for this position
					setPathValue(pathXPos, pathYPos+1, pathLength);
					
					int[] tempArray = new int[5];
					tempArray[0] = pathXPos;
					tempArray[1] = pathYPos+1;
					tempArray[2] = startingXPos;
					tempArray[3] = startingYPos+1;
					tempArray[4] = movementPoint-1;
					
					pathQueue.add(tempArray);
			} 
			
			if (GridFrame.gridButtonMap[startingXPos+1][startingYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos+1][startingYPos].getIsOccupied() == false
				&& path[pathXPos+1][pathYPos] == -1) {
				
					// set the value for this position
					setPathValue(pathXPos+1, pathYPos, pathLength);
					
					int[] tempArray = new int[5];
					tempArray[0] = pathXPos+1;
					tempArray[1] = pathYPos;
					tempArray[2] = startingXPos+1;
					tempArray[3] = startingYPos;
					tempArray[4] = movementPoint-1;
					
					pathQueue.add(tempArray);
				
			}
		}
		
		if (pathQueue.isEmpty() == true) {
			// queue is empty, no more path!
			return;
		} else {
			createShortestPath(pathQueue.peek()[0], pathQueue.peek()[1], pathQueue.peek()[2], pathQueue.peek()[3], pathQueue.poll()[4]);
		}
	}

	
	private void setPathValue(int startingXPos, int startingYPos, int pathLength) {
		// set the path steps for the grid
		int smallestValue = Integer.MAX_VALUE;
		
		// find the smallest value in the surrounding grids
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				if (startingXPos+i >= 0 && startingYPos+j >= 0 && 
						startingXPos+i < pathLength && startingYPos+j < pathLength &&
						Math.abs(i) != Math.abs(j)){
					if	(path[startingXPos+i][startingYPos+j] != -1 && path[startingXPos+i][startingYPos+j] < smallestValue) {
						smallestValue = path[startingXPos+i][startingYPos+j];
					}
				}
			} 
		}
		
		// move from surrounding grid to current grid need to add 1
		path[startingXPos][startingYPos] = smallestValue + 1;
	}

}
