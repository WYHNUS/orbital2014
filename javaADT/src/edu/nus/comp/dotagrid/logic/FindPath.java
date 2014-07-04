package edu.nus.comp.dotagrid.logic;


import java.util.LinkedList;
import java.util.Queue;

public class FindPath {
	
	private int[][] path;
	private int pathLength;
	
	private int pathXPos, pathYPos;
	
	private int searchRange;
	
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
		searchRange = Math.max(GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getNumberOfMovableGrid(),
				GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getSight());
		
		createShortestPath(pathXPos, pathYPos, startingXPos, startingYPos);
		return path[XPos - startingXPos + (pathLength-1)/2][YPos - startingYPos + (pathLength-1)/2];
	}
	

	// create path int-2D-array which store the shortest path to each grid
	private void createShortestPath(int pathXPos, int pathYPos, int startingXPos, int startingYPos) {
		// can move only if the grid is movable and not occupied
		// and only need to calculate if the position has not been calculated before
		
		// treat all grids that are not within searchRange as non-occupied
		
		checkPathPos(pathXPos, pathYPos, startingXPos, startingYPos, -1, 0);
		checkPathPos(pathXPos, pathYPos, startingXPos, startingYPos, 1, 0);
		checkPathPos(pathXPos, pathYPos, startingXPos, startingYPos, 0, -1);
		checkPathPos(pathXPos, pathYPos, startingXPos, startingYPos, 0, 1);
		
		if (pathQueue.isEmpty() == true) {
			// queue is empty, no more path!
			return;
		} else {
			createShortestPath(pathQueue.peek()[0], pathQueue.peek()[1], pathQueue.peek()[2], pathQueue.poll()[3]);
		}
	}

	
	private void checkPathPos(int pathXPos, int pathYPos, int startingXPos,	int startingYPos, int xIncrease, int yIncrease) {
		// check and manipulate pathQueue
		
		// cannot go beyond the 2D array size (same length for x and y)
		if (pathXPos+xIncrease >=0 && pathXPos+xIncrease < path.length && pathYPos+yIncrease >= 0 && pathYPos+yIncrease < path.length) {
			// the position is movable and has not been calculated before
			if (GridFrame.gridButtonMap[startingXPos+xIncrease][startingYPos+yIncrease].getIsMovable() == true 
					&& path[pathXPos+xIncrease][pathYPos+yIncrease] == -1) {
				// check if within search range
				if (isWithinSearchRange(pathXPos+xIncrease, pathYPos+yIncrease)) {
					// if within range, position cannot be occupied
					if (GridFrame.gridButtonMap[startingXPos+xIncrease][startingYPos+yIncrease].getIsOccupied() == false) {
						// set the value for this position
						setPathValue(pathXPos+xIncrease, pathYPos+yIncrease, pathLength);
								
						int[] tempArray = new int[5];
						tempArray[0] = pathXPos+xIncrease;
						tempArray[1] = pathYPos+yIncrease;
						tempArray[2] = startingXPos+xIncrease;
						tempArray[3] = startingYPos+yIncrease;
								
						pathQueue.add(tempArray);
					}
				} else {
					// not within range, treat as non-occupied grids
					
					// set the value for this position 
					setPathValue(pathXPos+xIncrease, pathYPos+yIncrease, pathLength);
					
					int[] tempArray = new int[5];
					tempArray[0] = pathXPos+xIncrease;
					tempArray[1] = pathYPos+yIncrease;
					tempArray[2] = startingXPos+xIncrease;
					tempArray[3] = startingYPos+yIncrease;
							
					pathQueue.add(tempArray);
				}
			}
		}
	}

	private boolean isWithinSearchRange(int xPos, int yPos) {
		// check if the given position is within search range
		return (Math.abs(this.pathXPos - xPos) + Math.abs(this.pathYPos - yPos) <= this.searchRange);
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
