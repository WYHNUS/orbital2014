package edu.nus.comp.dotagrid.logic;

public class GameButtonActions {
	
	private int moveNumberOfGrid = 5;

	public GameButtonActions(int actionNumber) {
		/**
		 * Each actionNumber corresponds to a fixed actionEvent
		 * 
		 * actionNumber 1 : game grid screen moves up
		 * 
		 * actionNumber 2 : game grid screen moves down
		 * 
		 * actionNumber 3 : game grid screen moves left
		 * 
		 * actionNumber 4 : game grid screen moves right
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
		}
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
