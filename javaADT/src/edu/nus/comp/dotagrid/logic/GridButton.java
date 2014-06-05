package edu.nus.comp.dotagrid.logic;

import java.util.Random;

public class GridButton {
	
	// mark if any character can move across the grid 
	private boolean isMovable = false;
	
	// mark if any character is currently on the grid
	private boolean isOccupied = false;
	
	// mark if the character is a hero
	private boolean isHero = false;
	
	// mark if the character is player's hero
	private boolean isPlayer = false;
	
	private Character character = null; 
	
	public GridButton(int imageNumber){
		/* 
		 * imageNumber :
		 * 1 : grass
		 * 2 : road
		 * 3 : river
		 * 4 : tree 
		 * 
		 * 99 : player's hero spawn point
		 * 
		 */
		

		if (imageNumber == 1 || imageNumber == 2 || imageNumber == 3 || imageNumber == 99) {
			this.setMovable(true);
		}
		
		if (imageNumber == 99){	
			// randomly select a hero from hero database for player to control
			Random random = new Random();
			character = new HeroDatabase().heroDatabase[random.nextInt(HeroDatabase.totalHeroNumber)];
			
			// create player!
			Screen.user.createPlayer((Hero) character);
			
			isOccupied = true;
			isHero = true;
			setPlayer(true);
		}
		
	}
	
	public void actionPerformed(){
		// highlight the selected position
		if (GridFrame.getSelectedXPos() != -1 && GridFrame.getSelectedYPos() != -1) {
			GridFrame.highlightedMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()] = 1;
		}
		
		if (GameButtonActions.readyToAct == true) {
			// get the AP required for such movement
			int usedAP = calculateUsedAP(GridFrame.getPreviouslySelectedXPos(), GridFrame.getPreviouslySelectedYPos(), 
					GridFrame.getSelectedXPos(),GridFrame.getSelectedYPos());
			
			// can only move on non-occupied and movable grid
			if (this.getIsMovable() == true && this.getIsOccupied() == false) {
				// can only move if character has enough AP
				if (GridFrame.gridButtonMap[GridFrame.getPreviouslySelectedXPos()][GridFrame.getPreviouslySelectedYPos()].getCharacter().getCurrentActionPoint() - usedAP >= 0){
					
					// perform move action
					this.resetGridButton(GridFrame.gridButtonMap[GridFrame.getPreviouslySelectedXPos()][GridFrame.getPreviouslySelectedYPos()]); 
					GridFrame.gridButtonMap[GridFrame.getPreviouslySelectedXPos()][GridFrame.getPreviouslySelectedYPos()] = new GridButton(1);
					
					// reduce hero's AP 
					character.setCurrentActionPoint(character.getCurrentActionPoint() - usedAP);
					
				}			
			}
			
			// move action ended
			GameButtonActions.readyToAct = false;
		}
		
		
		// display the selected position's character icon on the characterIcon
		if (this.isOccupied == true) {
			// change allCharacterInfoGameButtons in game frame to the selected character's info
			displayCharacterInfoOnGameFrame(this.character);
			displayMovableGrids();
		} else {
			// display world map icon
			GameFrame.allCharacterInfoGameButtons.get(0).setImage(GridFrame.terrain[GridFrame.map[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()]]);
			GameFrame.allCharacterInfoGameButtons.get(0).setIsReadyToDrawImage(true);
		}
	}
	
	
	
	private int calculateUsedAP(int previouslySelectedXPos, int previouslySelectedYPos, int selectedXPos, int selectedYPos) {
		// calculate AP used by moving from (previouslySelectedXPos, previouslySelectedYPos) to (selectedXPos, selectedYPos)
		int numberOfGridsMoved = Math.abs(previouslySelectedXPos - selectedXPos) + Math.abs(previouslySelectedYPos - selectedYPos);
		
		return (int)(numberOfGridsMoved * GridFrame.gridButtonMap[GridFrame.getPreviouslySelectedXPos()][GridFrame.getPreviouslySelectedYPos()].getCharacter().APUsedInMovingOneGrid());
	}

	
	private void displayMovableGrids() {
		// highlight movable grids
		
		for(int x=GridFrame.getSelectedXPos()-character.getNumberOfMovableGrid(); x<GridFrame.getSelectedXPos()+character.getNumberOfMovableGrid()+1; x++){
			for(int y=GridFrame.getSelectedYPos()-character.getNumberOfMovableGrid(); y<GridFrame.getSelectedYPos()+character.getNumberOfMovableGrid()+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of movable grid
						if (Math.abs(GridFrame.getSelectedXPos() - x) + Math.abs(GridFrame.getSelectedYPos() - y) <= character.getNumberOfMovableGrid()) {
							GridFrame.highlightedMap[x][y] = 1;
						}
					}
				}
			}
		}
		
	}

	
	private void displayCharacterInfoOnGameFrame(Character character) {		
		/*
		 
		 * 0: characterIcon
		 
		 * 1: characterName
		 * 2: characterHP
	  	 * 3: characterMP
	  	 
		 * 4: characterStrength
		 * 5: characterAgility
		 * 6: characterIntelligence
		 
		 * 7: characterAttack
		 * 8: characterDefense
		 
		 * 9: characterLevel
		 * 10: characterExperience
	
		 * 11 - 16 : items[0] - item[5]
		 * 17 - 24 : skill[0] - skill[7]
		 
		 * 25 :Turn
		 
		 * 26: Kill
		 * 27: Death
		 * 28: Assist
		 
		 * 29: money
		 * 30: actionPoints
		 
		 */
		
		// display character icon
		GameFrame.allCharacterInfoGameButtons.get(0).setImage(character.getCharacterImage());
		GameFrame.allCharacterInfoGameButtons.get(0).setIsReadyToDrawImage(true);
		
		GameFrame.allCharacterInfoGameButtons.get(1).setString("Name : " + character.getName());		
		GameFrame.allCharacterInfoGameButtons.get(2).setString("HP : " + character.getCurrentHP() + " / " + character.getmaxHP());
		GameFrame.allCharacterInfoGameButtons.get(3).setString("MP : " + character.getCurrentMP() + " / " + character.getmaxMP());

		GameFrame.allCharacterInfoGameButtons.get(7).setString("Attack : " + character.getTotalPhysicalAttack());
		GameFrame.allCharacterInfoGameButtons.get(8).setString("Defence : " + String.format("%.2f", character.getTotalPhysicalDefense()));
		
		GameFrame.allCharacterInfoGameButtons.get(30).setString("AP : " + character.getCurrentActionPoint() + " / " + character.getActionPoint());
		
		// properties that only hero possess
		if (this.isHero == true) {
			GameFrame.allCharacterInfoGameButtons.get(4).setString("Strength : " +((Hero)character).getTotalStrength());
			GameFrame.allCharacterInfoGameButtons.get(5).setString("Agility : " + ((Hero)character).getTotalAgility());
			GameFrame.allCharacterInfoGameButtons.get(6).setString("Intelligence : " + ((Hero)character).getTotalIntelligence());
			
			GameFrame.allCharacterInfoGameButtons.get(9).setString("Level : " + ((Hero)character).getLevel());
			GameFrame.allCharacterInfoGameButtons.get(10).setString("Exp : " + ((Hero)character).getExperience());
			
			
			// item list
			GameFrame.allCharacterInfoGameButtons.get(11).setIsReadyToDrawImage(true);
			GameFrame.allCharacterInfoGameButtons.get(12).setIsReadyToDrawImage(true);
			GameFrame.allCharacterInfoGameButtons.get(13).setIsReadyToDrawImage(true);
			GameFrame.allCharacterInfoGameButtons.get(14).setIsReadyToDrawImage(true);
			GameFrame.allCharacterInfoGameButtons.get(15).setIsReadyToDrawImage(true);
			GameFrame.allCharacterInfoGameButtons.get(16).setIsReadyToDrawImage(true);
			
			/*
			// skill list
			GameFrame.allCharacterInfoGameButtons.get(17).
			GameFrame.allCharacterInfoGameButtons.get(18).
			GameFrame.allCharacterInfoGameButtons.get(19).
			GameFrame.allCharacterInfoGameButtons.get(20).
			GameFrame.allCharacterInfoGameButtons.get(21).
			GameFrame.allCharacterInfoGameButtons.get(22).
			GameFrame.allCharacterInfoGameButtons.get(23).
			GameFrame.allCharacterInfoGameButtons.get(24).
			*/
			
			// KDA
			GameFrame.allCharacterInfoGameButtons.get(26).setString("Kill : " +((Hero)character).getKill());
			GameFrame.allCharacterInfoGameButtons.get(27).setString("Death : " + ((Hero)character).getDeath());
			GameFrame.allCharacterInfoGameButtons.get(28).setString("Assist : " + ((Hero)character).getAssist());
			
			// money
			GameFrame.allCharacterInfoGameButtons.get(29).setString("Money : " + Screen.user.player.getMoney());
		}

	}
	
	
	private void resetGridButton(GridButton resultGridButton){
		this.setMovable(resultGridButton.getIsMovable());
		this.setIsOccupied(resultGridButton.getIsOccupied());
		this.setPlayer(resultGridButton.getIsPlayer());
		this.setHero(resultGridButton.getIsHero());
		this.setCharacter(resultGridButton.getCharacter());
	}
	
	public void setCharacter(Character character){
		this.character = character;
	}
	
	public Character getCharacter(){
		return character;
	}

	public void setIsOccupied(boolean isOccupied){
		this.isOccupied = isOccupied;
	}
	
	public boolean getIsOccupied(){
		return isOccupied;
	}

	public boolean getIsPlayer() {
		return isPlayer;
	}

	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	public boolean getIsHero() {
		return isHero;
	}

	public void setHero(boolean isHero) {
		this.isHero = isHero;
	}

	public boolean getIsMovable() {
		return isMovable;
	}

	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

}
