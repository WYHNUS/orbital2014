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
	
	public FindPath(int searchRange) {
		pathLength = 1 + searchRange * 2;
		path = new int[pathLength][pathLength];
		
		// reset the 2D array
		for (int i=0; i<pathLength; i++) {
			for (int j=0; j<pathLength; j++) {
				path[i][j] = -1;
			}
		}
		
		pathXPos = searchRange;
		pathYPos = searchRange;
		
		// set the starting position to be 0
		path[pathXPos][pathYPos] = 0;
		
		pathQueue = new LinkedList<int[]>();
	}
	

	// getter
	public int[][] getPath() {
		return path;
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
	public int findShortestPath(int startingXPos, int startingYPos, int XPos, int YPos){
		createShortestPath(pathXPos, pathYPos, startingXPos, startingYPos);
		return path[XPos - startingXPos + (pathLength-1)/2][YPos - startingYPos + (pathLength-1)/2];
	}
	

	// create path int-2D-array which store the shortest path to each grid
	private void createShortestPath(int pathXPos, int pathYPos, int startingXPos, int startingYPos) {
		// can move only if the grid is movable and not occupied
		// and only need to calculate if the position has not been calculated before
		
		// cannot go beyond the 2D array size (same length for x and y)
		if (pathXPos-1 >=0 && pathXPos-1 < path.length && pathYPos >= 0 && pathYPos < path.length) {
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
						
				pathQueue.add(tempArray);
			}
		}
			
		if (pathXPos >=0 && pathXPos < path.length && pathYPos-1 >= 0 && pathYPos-1 < path.length) {
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
						
				pathQueue.add(tempArray);
					
			}
		}
				
		if (pathXPos >=0 && pathXPos < path.length && pathYPos+1 >= 0 && pathYPos+1 < path.length) {
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
						
				pathQueue.add(tempArray);
			} 
		}
		
		if (pathXPos+1 >=0 && pathXPos+1 < path.length && pathYPos >= 0 && pathYPos < path.length) {
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
						
				pathQueue.add(tempArray);
			}
		}
		
		if (pathQueue.isEmpty() == true) {
			// queue is empty, no more path!
			return;
		} else {
			createShortestPath(pathQueue.peek()[0], pathQueue.peek()[1], pathQueue.peek()[2], pathQueue.poll()[3]);
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
