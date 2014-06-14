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
	
	private GridButtonActions gridButtonActions = null;
	
	
	// constructor
	public GridButton(int imageNumber){
		/* 
		 * imageNumber :
		 * 1 : grass
		 * 2 : road
		 * 3 : river
		 * 4 : tree 
		 * 5 : fence
		 * 
		 * 20 : Sentinel Tower 1
		 * 21 : Sentinel Tower 2
		 * 22 : Sentinel Tower 3
		 * 23 : Sentinel Tower 4
		 * 
		 * 99 : player's hero spawn point
		 * 
		 */
		

		if (imageNumber == 1 || imageNumber == 2 || imageNumber == 3 || imageNumber == 99) {
			this.setIsMovable(true);
		}
		
		if(imageNumber == 20) {
			character = new TowerDatabase().towerDatabase[0];
			isOccupied = true;
		}
		
		if(imageNumber == 21) {
			character = new TowerDatabase().towerDatabase[1];
			isOccupied = true;
		}
		
		if(imageNumber == 22) {
			character = new TowerDatabase().towerDatabase[2];
			isOccupied = true;
		}
		
		if(imageNumber == 23) {
			character = new TowerDatabase().towerDatabase[3];
			isOccupied = true;
		}
		
		if (imageNumber == 99){	
			// randomly select a hero from hero database for player to control
			Random random = new Random();
			character = new HeroDatabase().heroDatabase[random.nextInt(HeroDatabase.totalHeroNumber)];
			
			// create player!
			Screen.user.createPlayer((Hero) character);
			
			isOccupied = true;
			isHero = true;
			isPlayer = true;
		}
		
	}
	
	
	public void actionPerformed(){
		// initialize grid button actions
		if (GridFrame.getPreviouslySelectedXPos() == -1 || GridFrame.getPreviouslySelectedYPos() == -1 ) {
			gridButtonActions = new GridButtonActions(GridFrame.getSelectedXPos(), GridFrame.getSelectedYPos(), 0, 0);
		} else {
			gridButtonActions = new GridButtonActions(GridFrame.getSelectedXPos(), GridFrame.getSelectedYPos(), 
					GridFrame.getPreviouslySelectedXPos(), GridFrame.getPreviouslySelectedYPos());
		}
		
		// execute player's hero's action if previously selected character is controlled by player
		if (GameButtonActions.readyToAct == true) {
			gridButtonActions.updateWhenSomeActionsInvoked();
		} else {
			gridButtonActions.updateWhenNoActionInvoked();
		}	
		
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

	public void setIsPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	public boolean getIsHero() {
		return isHero;
	}

	public void setIsHero(boolean isHero) {
		this.isHero = isHero;
	}

	public boolean getIsMovable() {
		return isMovable;
	}

	public void setIsMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

}
