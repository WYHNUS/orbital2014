package edu.nus.comp.dotagrid.logic;

import javax.swing.JOptionPane;


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
		 * actionNumber 7 : end this round, start next round: 
		 * 						1. reset all AP 
		 * 						2. check if need to spawn new wave of creeps
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
		 * actionNumber 20 - 27 : invoke ready to cast spell event
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
						
				
				
			case 20 :
				playerHeroCastSpell(0);
				break;
				
			case 21 :
				playerHeroCastSpell(1);
				break;
				
			case 22 :
				playerHeroCastSpell(2);
				break;
				
			case 23 :
				playerHeroCastSpell(3);
				break;
				
			case 24 :
				playerHeroCastSpell(4);
				break;
				
			case 25 :
				playerHeroCastSpell(5);
				break;
				
			case 26 :
				playerHeroCastSpell(6);
				break;
				
			case 27 :
				playerHeroCastSpell(7);
				break;
		}
	}

	

	private void playerHeroCastSpell(int playerSkillIndex) {
		// based on playerSkillIndex, determine which player's skill to cast
		
		// check if hero's skill list is empty
		if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex] != null) {
			((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].invokeSkillAction();
		}
	}


	private void sellItem() {
		// sell an item from player's bought item list
		System.out.println("show sell item interface!");
		new SellItem();
	}


	private void showItemShop() {
		// create a new frame to show item shop list
		System.out.println("show item shop interface!");
		new ItemShop();
	}


	private void endRound() {
		GameFrame.turn++;
		GameFrame.allCharacterInfoGameButtons.get(25).setString("Turn : " + GameFrame.turn);
		
		// reset AP for all existing characters
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				if (GridFrame.gridButtonMap[x][y].getCharacter() != null){
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentActionPoint(GridFrame.gridButtonMap[x][y].getCharacter().getMaxActionPoint());
				}
			}
		}	
		
		// spawn a new wave of creeps
		LineCreepSpawnPoint.spawnNewWave();
		
		// reselect the grid
		GridFrame.invokeEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
		System.out.println("End Round!");
	}	

	
	private void readyToAttack() {
		// get ready for player's hero to perform physical attack if player's character is selected
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToAttack = true;
		} else {
			String str = "You need to select your own hero to execute this action!";
			JOptionPane.showMessageDialog(null, str);
		}
	}


	private void readyToMove() {
		// get ready for player's hero to move
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToMove = true;
		} else {
			String str = "You need to select your own hero to execute this action!";
			JOptionPane.showMessageDialog(null, str);
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
