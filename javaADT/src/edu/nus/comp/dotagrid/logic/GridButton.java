package edu.nus.comp.dotagrid.logic;

public class GridButton {
	
	// mark if any character can move across the grid 
	private boolean isMovable = false;
	
	// mark if any character is currently on the grid
	private boolean isOccupied = false;
	
	// action number marks the character of the grid button and its corresponding acitons
	private int actionNumber;
	
	private Character character = null; 
	
	public GridButton(int imageNumber){
		/* imageNumber :
		 * 1 : grass
		 * 2 : road
		 * 3 : river
		 * 4 : tree 
		 * 
		 * 99 : hero spawn point
		 * */
		

		if (imageNumber == 1 || imageNumber == 2 || imageNumber == 3 || imageNumber == 99) {
			isMovable = true;
		}
		
		if (imageNumber == 99){
			// create a hero for player to control
			character = new Hero("fur", 150, 10, 10, 1, 10, 2, 20, 10);
			character.setCharacterImage("Heros", "fur");
			
			actionNumber = 99; // set actionNumber
			
			isOccupied = true;
		}
		
	}
	
	public void actionPerformed(){
		// test
		System.out.println(actionNumber + " Action Performed!");	

		
		// highlight the selected position
		if (GridFrame.getSelectedXPos() != -1 && GridFrame.getSelectedYPos() != -1) {
			GridFrame.highlightedMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()] = 1;
		}
		
		
		// display the selected position's character icon on the characterIcon
		if (this.isOccupied == true) {
			// display character icon
			GameFrame.characterIcon.setImage(character.getCharacterImage());
			GameFrame.characterIcon.setIsReadyToDrawImage(true);
		} else {
			// display world map icon
			GameFrame.characterIcon.setImage(GridFrame.terrain[GridFrame.map[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()]]);
			GameFrame.characterIcon.setIsReadyToDrawImage(true);
		}
	}
	
	
	public boolean getIsOccupied(){
		return isOccupied;
	}
	
	public Character getCharacter(){
		return character;
	}

}
