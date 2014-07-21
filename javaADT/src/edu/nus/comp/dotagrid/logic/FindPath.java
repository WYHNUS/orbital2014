package edu.nus.comp.dotagrid.logic;


import java.util.LinkedList;
import java.util.Queue;

public class FindPath {
	
	private int[][] path;
	private int pathLength;
	
	private int pathXPos, pathYPos;
	
	private int searchRange;
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
				&& GridFrame.gridButtonMap[startingXPos-1][startingYPos].getCharacter() == null) {
				
					GridFrame.highlightedMap[startingXPos-1][startingYPos] = 1;
					highlightMovableGrids(startingXPos - 1, startingYPos, movementPoint - 1);
				
			}
				
			if (GridFrame.gridButtonMap[startingXPos][startingYPos-1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos-1].getCharacter() == null) {
				
					GridFrame.highlightedMap[startingXPos][startingYPos-1] = 1;
					highlightMovableGrids(startingXPos, startingYPos - 1, movementPoint - 1);
				
			}
			
			if (GridFrame.gridButtonMap[startingXPos][startingYPos+1].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos][startingYPos+1].getCharacter() == null) {
				
					GridFrame.highlightedMap[startingXPos][startingYPos+1] = 1;
					highlightMovableGrids(startingXPos, startingYPos + 1, movementPoint - 1);
				
			} 
			
			if (GridFrame.gridButtonMap[startingXPos+1][startingYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[startingXPos+1][startingYPos].getCharacter() == null) {
				
					GridFrame.highlightedMap[startingXPos+1][startingYPos] = 1;
					highlightMovableGrids(startingXPos + 1, startingYPos, movementPoint - 1);
				
			}
		}
	}
	
	
	
	// find shortest path between two points
	public int findShortestPath(int startingXPos, int startingYPos, int XPos, int YPos){
		searchRange = Math.max(GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getNumberOfMovableGrid(),
				GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getSight());

		Queue<int[]> uncheckedQueue = new LinkedList<int[]>();
		int[] startingPos = {pathXPos, pathYPos, startingXPos, startingYPos};
		uncheckedQueue.add(startingPos);
		
		createShortestPath(uncheckedQueue);
		
		return path[XPos - startingXPos + (pathLength-1)/2][YPos - startingYPos + (pathLength-1)/2];
	}
	

	// create path int-2D-array which store the shortest path to each grid
	private void createShortestPath(Queue<int[]> uncheckedQueue) {
		// can move only if the grid is movable and not occupied
		// and only need to calculate if the position has not been calculated before
		
		// terminating
		if (uncheckedQueue.isEmpty()) {
			// queue is empty, no more path!
			return;
		}
		
		// treat all grids that are not within searchRange as non-occupied
		checkPathPos(uncheckedQueue, -1, 0);
		checkPathPos(uncheckedQueue, 1, 0);
		checkPathPos(uncheckedQueue, 0, -1);
		checkPathPos(uncheckedQueue, 0, 1);

		uncheckedQueue.poll();
		createShortestPath(uncheckedQueue);
	}

	
	private void checkPathPos(Queue<int[]> uncheckedQueue, int xIncrease, int yIncrease) {
		
		// check and manipulate pathQueue
		int pathXPos = uncheckedQueue.peek()[0];
		int pathYPos = uncheckedQueue.peek()[1];
		int startingXPos = uncheckedQueue.peek()[2];
		int startingYPos = uncheckedQueue.peek()[3];
				
		// cannot go beyond the 2D array size (same length for x and y)
		if (pathXPos+xIncrease >=0 && pathXPos+xIncrease < path.length && pathYPos+yIncrease >= 0 && pathYPos+yIncrease < path.length) {
			// check if within grid frame
			if (startingXPos+xIncrease >= 0 && startingXPos+xIncrease < GridFrame.COLUMN_NUMBER
					&& startingYPos+yIncrease >= 0 && startingYPos+yIncrease < GridFrame.ROW_NUMBER) {

				// the position is movable and has not been calculated before
				if (GridFrame.gridButtonMap[startingXPos+xIncrease][startingYPos+yIncrease].getIsMovable() 
						&& path[pathXPos+xIncrease][pathYPos+yIncrease] == -1) {
					// check if within search range
					if (isWithinSearchRange(pathXPos+xIncrease, pathYPos+yIncrease)) {
						// if within range, position cannot be occupied
						if (GridFrame.gridButtonMap[startingXPos+xIncrease][startingYPos+yIncrease].getCharacter() == null) {
							// set the value for this position
							setPathValue(pathXPos+xIncrease, pathYPos+yIncrease, pathLength);

							int[] tempArray = new int[4];
							tempArray[0] = pathXPos+xIncrease;
							tempArray[1] = pathYPos+yIncrease;
							tempArray[2] = startingXPos+xIncrease;
							tempArray[3] = startingYPos+yIncrease;

							uncheckedQueue.add(tempArray);
						}
					} else {
						// not within range, treat as non-occupied grids

						// set the value for this position 
						setPathValue(pathXPos+xIncrease, pathYPos+yIncrease, pathLength);

						int[] tempArray = new int[4];
						tempArray[0] = pathXPos+xIncrease;
						tempArray[1] = pathYPos+yIncrease;
						tempArray[2] = startingXPos+xIncrease;
						tempArray[3] = startingYPos+yIncrease;

						uncheckedQueue.add(tempArray);
					}
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
