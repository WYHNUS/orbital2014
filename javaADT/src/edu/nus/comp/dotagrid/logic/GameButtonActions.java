package edu.nus.comp.dotagrid.logic;

public class GameButtonActions {
	public static boolean readyToAct = false;
	
	private int moveRowNumberOfGrid = (int) (GridFrame.getGridRowNumberInScreen() / 2.0);
	private int moveHeightNumberOfGrid = (int) (GridFrame.getGridColNumberInScreen() / 2.0);
	
	private double zoomFactor = 2.0;

	public GameButtonActions(int actionNumber) {
		/**
		 * Each actionNumber corresponds to a fixed actionEvent
		 * 
		 * actionNumber 1 : game grid screen moves up by 1/2 of the height of screen's grids
		 * 
		 * actionNumber 2 : game grid screen moves down by 1/2 of the height of screen's grids
		 * 
		 * actionNumber 3 : game grid screen moves left by 1/2 of the width of screen's grids
		 * 
		 * actionNumber 4 : game grid screen moves right by 1/2 of the width of screen's grids
		 * 
		 * actionNumber 5 : zoom in game grid screen by reducing size of a factor of 2
		 * 
		 * actionNumber 6 : zoom out game grid screen by increasing size of a factor of 2
		 * 
		 * actionNumber 7 : invoke ready to movement event
		 * 
		 */

		switch (actionNumber) {
									
			case 0:
				break;
				
			case 1:
				gameGridMoveUp();
				break;
	
			case 2:
				gameGridMoveDown();
				break;
	
			case 3:
				gameGridMoveLeft();
				break;
	
			case 4:
				gameGridMoveRight();
				break;
				
			case 5:
				zoomInGameGrid();
				break;
				
			case 6:
				zoomOutGameGrid();
				break;
				
			case 7:
				readyToMove();
				break;
		}
	}
	

	private void readyToMove() {
		// get ready for player's hero to move
		readyToAct = true;
	}


	private void zoomOutGameGrid() {
		// zoom out game grid screen
		GridFrame.setGridColNumberInScreen((int) (GridFrame.getGridColNumberInScreen() * zoomFactor));
		GridFrame.setGridRowNumberInScreen((int) (GridFrame.getGridRowNumberInScreen() * zoomFactor));
		
		// check the boundary conditions:
		
		// x-axis position has gone beyond the maximum column number
		if(GridFrame.getCurrentGridXPos() + GridFrame.getGridColNumberInScreen() >= GridFrame.COLUMN_NUMBER) {
			GridFrame.setCurrentGridXPos((int) (GridFrame.COLUMN_NUMBER - GridFrame.getGridColNumberInScreen()));
		}
		
		// y-axis position has gone beyond the maximum row number
		if(GridFrame.getCurrentGridYPos() + GridFrame.getGridRowNumberInScreen() >= GridFrame.ROW_NUMBER) {
			GridFrame.setCurrentGridYPos((int) (GridFrame.ROW_NUMBER - GridFrame.getGridRowNumberInScreen()));
		}
		
		System.out.println("zoomOutGameGrid has been invoked!");
	}


	private void zoomInGameGrid() {
		// zoom in game grid screen
		GridFrame.setGridColNumberInScreen((int) (GridFrame.getGridColNumberInScreen() / zoomFactor));
		GridFrame.setGridRowNumberInScreen((int) (GridFrame.getGridRowNumberInScreen() / zoomFactor));
		System.out.println("zoomInGameGrid has been invoked!");
	}


	private void gameGridMoveRight() {
		// change currentGridXPos
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() + moveHeightNumberOfGrid);
		System.out.println("gameGridMoveRight has been invoked!");
	}

	private void gameGridMoveLeft() {
		// change currentGridXPos
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() - moveHeightNumberOfGrid);
		System.out.println("gameGridMoveLeft has been invoked!");
	}

	private void gameGridMoveDown() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() + moveRowNumberOfGrid);
		System.out.println("gameGridMoveDown has been invoked!");
	}

	private void gameGridMoveUp() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() - moveRowNumberOfGrid);
		System.out.println("gameGridMoveUp has been invoked!");
	}

}
