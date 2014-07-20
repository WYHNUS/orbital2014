package edu.nus.comp.dotagrid.logic;

import javax.swing.JOptionPane;


public class GridButtonActions {
	
	private int toXPos;
	private int toYPos;
	private int fromXPos;
	private int fromYPos;

	// constructor
	public GridButtonActions(int toXPos, int toYPos, int fromXPos, int fromYPos) {
		this.toXPos = toXPos;
		this.toYPos = toYPos;
		this.fromXPos = fromXPos;
		this.fromYPos = fromYPos;
	}

	public void updateWhenNoActionInvoked() {
		
		// display the selected position's character icon on the characterIcon if within player's sight
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() && GridFrame.sightMap[toXPos][toYPos] == 1) {
			
			// change allCharacterInfoGameButtons in game frame to the selected character's info
			displayCharacterInfoOnGameFrame(GridFrame.gridButtonMap[toXPos][toYPos].getCharacter());
			
			// highlight attackable grids when attack is invoked 
			FindPath.highlightMovableGrids(toXPos, toYPos, GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getNumberOfMovableGrid());
			displayAttackableGrids();
			
		} else {
			// highlight the selected position if no character is on the grid
			GridFrame.highlightedMap[toXPos][toYPos] = 1;
			
			// display world map icon
			GameFrame.allCharacterInfoGameButtons.get(0).setImage(GridFrame.terrain[GridFrame.map[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()]]);
			GameFrame.allCharacterInfoGameButtons.get(0).setIsReadyToDrawImage(true);
		}	
	}

	public void updateWhenSomeActionsInvoked() {
		// only one of the readyToMove / readyToAttack / readyToCastSpell can be true at each time interval (use if{} else if{} to save time)

		// check to execute move action
		if (GameButtonActions.readyToMove) {
			
			// check if within movable grids
			if (isWithinMovableRange()) {
				// move!
				new CharacterActions(1, fromXPos, fromYPos, toXPos, toYPos);
				
			} else {
				JOptionPane.showMessageDialog(null, "Out Of Movable Range!");
			}

			// move action ended
			GameButtonActions.readyToMove = false;
			
			// player's action ended
			GameButtonActions.readyToAct = false;
			
			// reselect player's hero position
			GridFrame.invokeLeftClickEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
		}
		
		
		// check to execute attack action
		else if (GameButtonActions.readyToAttack) {
			boolean isWithinAttackRange = calculateWithinAttackRange();
			
			// check if within attackable range
			if (isWithinAttackRange && GridFrame.sightMap[toXPos][toYPos] == 1) {
				// can only attack on occupied grid
				if (GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == true) {
					// can only attack non-friendly units
					if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTeamNumber() != GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTeamNumber()) {
						// can only attack if character has enough AP
						if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - (int)(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedWhenAttack()) >= 0){
							// attack!
							new CharacterActions(2, fromXPos, fromYPos, toXPos, toYPos);} 
						
						else {			
							JOptionPane.showMessageDialog(null, "not enough action point to attack!");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Unable to attack friendly units!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Need to attack a existing character!");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Out Of Attack Range!");
			}

			// attack action ended
			GameButtonActions.readyToAttack = false;
			
			// player's action ended
			GameButtonActions.readyToAct = false;
			
			// select position which has been attacked
			GridFrame.invokeLeftClickEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
		}
		
		// check to execute cast spell (hero's skill) action
		else if (GameButtonActions.readyToCastSpell) {
			boolean isWithinCastingRange = calculateWithinSkillRange();
			
			Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
			Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
			
			// hero can only invoke the skill if the skill's current count down is 0;
			if (castingSkill.getCurrentCoolDownRound() == 0) {
					
				// hero must have enough mana in order to cast this spell
				if (castingSkill.getUsedMP() < castingHero.getCurrentMP()) {
						
					// hero must have enough AP
					if (castingSkill.getUsedActionPoint() < castingHero.getCurrentActionPoint()) {
						
						if (isWithinCastingRange) {
							// cast this spell !
							new CharacterActions(4, fromXPos, fromYPos, toXPos, toYPos);
						} else {
							JOptionPane.showMessageDialog(null, "Out Of Skill Casting Range!");
						}
						
					} else {
						JOptionPane.showMessageDialog(null, "You don't have enought Action Point to cast this spell!");
					}
						
				} else {
					JOptionPane.showMessageDialog(null, "You don't have enought mana to cast this spell!");
				}
					
			} else {
				JOptionPane.showMessageDialog(null, "You have to wait another " + castingSkill.getCurrentCoolDownRound() + " rounds to cast this spell!");
			}	

			
			// casting action ended
			GameButtonActions.readyToCastSpell = false;
			
			// player's action ended
			GameButtonActions.readyToAct = false;
			
			// select position which the spell has casted on
			GridFrame.invokeLeftClickEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
		}
		
		// check to execute use item action
		else if (GameButtonActions.readyToUseItem) {
			boolean isWithinCastingRange = calculateWithinItemCastingRange();
			
			if (isWithinCastingRange) {
				// use this item !
				new CharacterActions(5, fromXPos, fromYPos, toXPos, toYPos);
			} else {
				JOptionPane.showMessageDialog(null, "Out Of Item Casting Range!");
			}
			
			// casting action ended
			GameButtonActions.readyToUseItem = false;
						
			// player's action ended
			GameButtonActions.readyToAct = false;
		}
	}
	
	
	private boolean calculateWithinItemCastingRange() {
		// calculate if the selected grid is within attack range
		return (Math.abs(toXPos - fromXPos) + Math.abs(toYPos - fromYPos)) <= ((Hero)(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter())).items[Player.invokedPlayerItemIndex].getCastingRange();
	}

	private boolean calculateWithinSkillRange() {
		// calculate if the selected grid is within attack range
		return (Math.abs(toXPos - fromXPos) + Math.abs(toYPos - fromYPos)) <= ((Hero)(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter())).skills[Player.invokedPlayerSkillIndex].getCastRange();	
	}

	private boolean isWithinMovableRange() {
		// calculate if the selected grid is within movable range
		FindPath.highlightMovableGrids(fromXPos, fromYPos, GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getNumberOfMovableGrid());
		
		return GridFrame.highlightedMap[toXPos][toYPos] == 1;
	}

	private boolean calculateWithinAttackRange() {
		// calculate if the selected grid is within attack range
		return (Math.abs(toXPos - fromXPos) + Math.abs(toYPos - fromYPos) <= GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTotalPhysicalAttackArea());
	}
	

	
	private void displayAttackableGrids() {
		// highlight attackable grids
		int physicalAttackArea = GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTotalPhysicalAttackArea();

		
		for(int x=toXPos-physicalAttackArea; x<toXPos+physicalAttackArea+1; x++){
			for(int y=toYPos-physicalAttackArea; y<toYPos+physicalAttackArea+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of attackable grid
						if (Math.abs(toXPos - x) + Math.abs(toYPos - y) <= physicalAttackArea) {
							GridFrame.attackRangeMap[x][y] = 1;
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
		 * 8: characterDefence
		 
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

		GameFrame.allCharacterInfoGameButtons.get(7).setString("Attack : " + character.getStartingPhysicalAttack() 
				+ " + " + character.getBasicPhysicalAttack());
		GameFrame.allCharacterInfoGameButtons.get(8).setString("Defence : " + String.format("%.2f", character.getTotalPhysicalDefence()));
		
		//GameFrame.allCharacterInfoGameButtons.get(30).setString("AP : " + character.getCurrentActionPoint() + " / " + character.getMaxActionPoint());
		
		// properties that only hero possess
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsHero() == true) {
			
			// update character's information
			((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).updateHeroAttributeInfo();
			Hero heroCharacter = new Hero((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter());
			updateInfo(heroCharacter);
		}

	}
	
	
	private void updateInfo(Hero hero) {
		
		GameFrame.allCharacterInfoGameButtons.get(2).setString("HP : " + hero.getCurrentHP() + " / " + hero.getmaxHP());
		GameFrame.allCharacterInfoGameButtons.get(3).setString("MP : " + hero.getCurrentMP() + " / " + hero.getmaxMP());
		
		GameFrame.allCharacterInfoGameButtons.get(4).setString("Strength : " + hero.getStartingStrength()
				+ " + " + hero.getTotalItemAddStrength());
		GameFrame.allCharacterInfoGameButtons.get(5).setString("Agility : " + hero.getStartingAgility()
				+ " + " + hero.getTotalItemAddAgility());
		GameFrame.allCharacterInfoGameButtons.get(6).setString("Intelligence : " + hero.getStartingIntelligence()
				+ " + " + hero.getTotalItemAddIntelligence());
		
		GameFrame.allCharacterInfoGameButtons.get(7).setString("Attack : " + hero.getStartingPhysicalAttack() 
				+ " + " + hero.getTotalItemAddPhysicalAttack());
		GameFrame.allCharacterInfoGameButtons.get(8).setString("Defence : " + String.format("%.2f", hero.getStartingPhysicalDefence())
				+ " + " + String.format("%.2f", hero.getTotalItemAddPhysicalDefence()));
		
		GameFrame.allCharacterInfoGameButtons.get(9).setString("Level : " + hero.getLevel());
		GameFrame.allCharacterInfoGameButtons.get(10).setString("Exp : " + hero.getExperience());
		
		
		// item list
		GameFrame.allCharacterInfoGameButtons.get(11).setIsReadyToDrawImage(true);
		GameFrame.allCharacterInfoGameButtons.get(12).setIsReadyToDrawImage(true);
		GameFrame.allCharacterInfoGameButtons.get(13).setIsReadyToDrawImage(true);
		GameFrame.allCharacterInfoGameButtons.get(14).setIsReadyToDrawImage(true);
		GameFrame.allCharacterInfoGameButtons.get(15).setIsReadyToDrawImage(true);
		GameFrame.allCharacterInfoGameButtons.get(16).setIsReadyToDrawImage(true);
		
		
		// skill list
		for (int i=17; i<=24; i++) {
			GameFrame.allCharacterInfoGameButtons.get(i).setIsReadyToDrawImage(true);
		}
		
		// KDA
		GameFrame.allCharacterInfoGameButtons.get(26).setString("Kill : " + hero.getKill());
		GameFrame.allCharacterInfoGameButtons.get(27).setString("Death : " + hero.getDeath());
		GameFrame.allCharacterInfoGameButtons.get(28).setString("Assist : " + hero.getAssist());
		
		// money (remark: player should only be able to see his own amount of money)
		GameFrame.allCharacterInfoGameButtons.get(29).setString("Money : " + 
				((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getMoney());
	}
	
}
