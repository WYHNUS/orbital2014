package edu.nus.comp.dotagrid.logic;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameButtonActions {
	public static boolean readyToAct = false;
	public static boolean readyToMove = false;
	public static boolean readyToAttack = false;
	
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
		 * actionNumber 7 : end this round, start next round, reset all AP
		 * 
		 * actionNumber 8 : invoke ready to movement event
		 * 
		 * actionNumber 9 : invoke ready to attack event
		 * 
		 * actionNumber 10 : 
		 * 
		 * actionNumber 11 : 
		 * 
		 * actionNumber 12 : pop up a item shop menu
		 * 
		 * actionNumber 13 : pop up a sell item menu
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
				
			case 7 :
				endRound();
				break;
				
			case 8:
				readyToMove();
				break;
				
			case 9 :
				readyToAttack();
				break;
				
			case 10 :
				break;
			
			case 11 : 
				break;
			
			case 12 : 
				showItemShop();
				break;
				
			case 13 :
				sellItem();
				break;
								
		}
	}
	

	private void readyToAttack() {
		// get ready for player's hero to perform physical attack if player's character is selected
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToAttack = true;
		}
	}


	private void sellItem() {
		// sell an item from player's bought item list
		new SellItem();
	}


	private void showItemShop() {
		// create a new frame to show item shop list
		new ItemShop();
	}


	private void endRound() {
		GameFrame.round++;
		GameFrame.allCharacterInfoGameButtons.get(25).setString("Turn : " + GameFrame.round);
		
		// reset AP for all existing characters
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				if (GridFrame.gridButtonMap[x][y].getCharacter() != null){
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentActionPoint(GridFrame.gridButtonMap[x][y].getCharacter().getActionPoint());
				}
			}
		}	

		// reselect the grid
		GridFrame.invokeEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
		System.out.println("End Round!");
	}


	private void readyToMove() {
		// get ready for player's hero to move
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToMove = true;
		}
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
