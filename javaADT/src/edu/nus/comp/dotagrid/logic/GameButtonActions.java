package edu.nus.comp.dotagrid.logic;

public class GameButtonActions {
	
	private int moveNumberOfGrid = 5;
	
	private double zoomFactor = 2.0;

	public GameButtonActions(int actionNumber) {
		/**
		 * Each actionNumber corresponds to a fixed actionEvent
		 * 
		 * actionNumber 1 : game grid screen moves up by 5 grid
		 * 
		 * actionNumber 2 : game grid screen moves down by 5 grid
		 * 
		 * actionNumber 3 : game grid screen moves left by 5 grid
		 * 
		 * actionNumber 4 : game grid screen moves right by 5 grid
		 * 
		 * actionNumber 5 : zoom in game grid screen by reducing size of a factor of 2
		 * 
		 * actionNumber 6 : zoom out game grid screen by increasing size of a factor of 2
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
		}
	}
	

	private void zoomOutGameGrid() {
		// zoom out game grid screen
		GridFrame.setGridColNumberInScreen((int) (GridFrame.getGridColNumberInScreen() * zoomFactor));
		GridFrame.setGridRowNumberInScreen((int) (GridFrame.getGridRowNumberInScreen() * zoomFactor));
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
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() + moveNumberOfGrid);
		System.out.println("gameGridMoveRight has been invoked!");
	}

	private void gameGridMoveLeft() {
		// change currentGridXPos
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() - moveNumberOfGrid);
		System.out.println("gameGridMoveLeft has been invoked!");
	}

	private void gameGridMoveDown() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() + moveNumberOfGrid);
		System.out.println("gameGridMoveDown has been invoked!");
	}

	private void gameGridMoveUp() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() - moveNumberOfGrid);
		System.out.println("gameGridMoveUp has been invoked!");
	}

}
