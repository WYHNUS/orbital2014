package edu.nus.comp.dotagrid.logic;

public class GridButton {
	
	// mark if any character can move across the grid 
	private boolean isMovable = false;
	
	// mark if any character is currently on the grid
	private boolean isOccupied = false;
	
	// mark if the character is a hero
	private boolean isHero = false;
	
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
		 * 99 : player's hero spawn point
		 * */
		

		if (imageNumber == 1 || imageNumber == 2 || imageNumber == 3 || imageNumber == 99) {
			isMovable = true;
		}
		
		if (imageNumber == 99){
			// create a hero for player to control
			character = new Hero("fur", "intelligence", 150, 10, 10, 3, 1.7, 3.52, 20, 100,
								19, 18, 21, 1.8, 1.9, 2.9, 295);
			
			character.setCharacterImage("Heros", "fur");
			
			actionNumber = 99; // set actionNumber
			
			isOccupied = true;
			isHero = true;
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
			// change allCharacterInfoGameButtons in game frame to the selected character's info
			displayCharacterInfoOnGameFrame(this.character);
		} else {
			// display world map icon
			GameFrame.allCharacterInfoGameButtons.get(0).setImage(GridFrame.terrain[GridFrame.map[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()]]);
			GameFrame.allCharacterInfoGameButtons.get(0).setIsReadyToDrawImage(true);
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
		 
		 * 25: Kill
		 * 26: Death
		 * 27: Assist
		 
		 * 28: money
		 * 29: actionPoints
		 
		 */
		
		// display character icon
		GameFrame.allCharacterInfoGameButtons.get(0).setImage(character.getCharacterImage());
		GameFrame.allCharacterInfoGameButtons.get(0).setIsReadyToDrawImage(true);
		
		GameFrame.allCharacterInfoGameButtons.get(1).setString("Name : " + character.getName());		
		GameFrame.allCharacterInfoGameButtons.get(2).setString("HP : " + character.getCurrentHP() + " / " + character.getmaxHP());
		GameFrame.allCharacterInfoGameButtons.get(3).setString("MP : " + character.getCurrentMP() + " / " + character.getmaxMP());

		GameFrame.allCharacterInfoGameButtons.get(7).setString("Attack : " + character.getTotalPhysicalAttack());
		GameFrame.allCharacterInfoGameButtons.get(8).setString("Defence : " + String.format("%.2f", character.getTotalPhysicalDefense()));
		
		GameFrame.allCharacterInfoGameButtons.get(29).setString("AP : " + character.getCurrentActionPoint() + " / " + character.getActionPoint());
		
		// properties that only hero possess
		if (this.isHero == true) {
			GameFrame.allCharacterInfoGameButtons.get(4).setString("Strength : " +((Hero)character).getTotalStrength());
			GameFrame.allCharacterInfoGameButtons.get(5).setString("Agility : " + ((Hero)character).getTotalAgility());
			GameFrame.allCharacterInfoGameButtons.get(6).setString("Intelligence : " + ((Hero)character).getTotalIntelligence());
			
			GameFrame.allCharacterInfoGameButtons.get(9).setString("Level : " + ((Hero)character).getLevel());
			GameFrame.allCharacterInfoGameButtons.get(10).setString("Exp : " + ((Hero)character).getExperience());
			
			/*
			// item list
			GameFrame.allCharacterInfoGameButtons.get(11).
			GameFrame.allCharacterInfoGameButtons.get(12).
			GameFrame.allCharacterInfoGameButtons.get(13).
			GameFrame.allCharacterInfoGameButtons.get(14).
			GameFrame.allCharacterInfoGameButtons.get(15).
			GameFrame.allCharacterInfoGameButtons.get(16).
			
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
			GameFrame.allCharacterInfoGameButtons.get(25).setString("Kill : " +((Hero)character).getKill());
			GameFrame.allCharacterInfoGameButtons.get(26).setString("Death : " + ((Hero)character).getDeath());
			GameFrame.allCharacterInfoGameButtons.get(27).setString("Assist : " + ((Hero)character).getAssist());
			
			// money
			GameFrame.allCharacterInfoGameButtons.get(28).setString("Money : " +((Hero)character).getMoney());
		}


		
	}

	public boolean getIsOccupied(){
		return isOccupied;
	}
	
	public Character getCharacter(){
		return character;
	}

}
