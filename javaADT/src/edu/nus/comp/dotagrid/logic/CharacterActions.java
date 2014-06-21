package edu.nus.comp.dotagrid.logic;

import javax.swing.JOptionPane;

public class CharacterActions {
	private int toXPos;
	private int toYPos;
	private int fromXPos;
	private int fromYPos;

	public CharacterActions(int actionNumber, int fromXPos, int fromYPos, int toXPos,
			int toYPos) {
		this.toXPos = toXPos;
		this.toYPos = toYPos;
		this.fromXPos = fromXPos;
		this.fromYPos = fromYPos;
			
		/*
		 * Each actionNumber corresponds to a fixed actionEvent
		 * 
		 * actionNumber 1 : move
		 * 
		 * actionNumber 2 : attack
		 * 
		 * actionNumber 3 : update sight map
		 * 
		 * */
		
		switch(actionNumber){
			case 1 :
				move();
				break;
				
			case 2 :
				attack();
				break;
				
			case 3 :
				updateSightMap();
				break;
				
			case 4 :
				castSpell();
				break;
		}
		
	}

	
	private void castSpell() {
		// casting hero is at [fromXPos][fromYPos] position, target position is at [toXPos][toYPos]
		// actions are different depending on skill type
		
		switch (Skill.invokedSkillType) {
		
			case 1 :
				// teleport
				teleport();
				break;
				
		}
		
	}


	private void teleport() {
		
		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
		

		// hero can only invoke the skill if the skill's current count down is 0;
		if (castingSkill.getCurrentCoolDownRound() == 0) {
				
			// hero must have enough mana in order to cast this spell
			if (castingSkill.getUsedMP() < castingHero.getCurrentMP()) {
					
				// hero must have enough AP
				if (castingSkill.getUsedActionPoint() < castingHero.getCurrentActionPoint()) {
				
					// can only teleport to non-occupied and movable grid
					if (GridFrame.gridButtonMap[toXPos][toYPos].getIsMovable() == true 
							&& GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == false) {
							
						// perform move action
						GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(GridFrame.gridButtonMap[fromXPos][fromYPos]); 
						GridFrame.gridButtonMap[fromXPos][fromYPos] = new GridButton(1);
															
						// reduce character's AP 
						GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentActionPoint(
								GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentActionPoint() - castingSkill.getUsedActionPoint());
									
						// reduce character's MP
						GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentMP(
								GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentMP() - castingSkill.getUsedMP());
							
						// reset current cooldown
						((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).skills[Player.invokedPlayerSkillIndex].setCurrentCoolDownRound(castingSkill.getCoolDownRounds());
							
						// if hero is player, change player's position
						if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer() == true) {
							Screen.user.player.setXPos(toXPos);
							Screen.user.player.setYPos(toYPos);
						}
									
					} else {
						JOptionPane.showMessageDialog(null, "Unable to Move to that position!");
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
	}


	private void updateSightMap() {
		// update sight map centered at position (toXPos, toYPos)
		int sightRange = GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getSight();
		
		for(int x=toXPos-sightRange; x<toXPos+sightRange+1; x++){
			for(int y=toYPos-sightRange; y<toYPos+sightRange+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of sight grid
						if (Math.abs(toXPos - x) + Math.abs(toYPos - y) <= sightRange) {
							GridFrame.sightMap[x][y] = 1;
						}
					}
				}
			}
		}
		
	}

	private void attack() {
		// get the AP required for one physical attack
		int usedAP = calculateAttackUsedAP();
				
		// perform attack action
		GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentHP(GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP() 
				- Character.getActualDamage(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTotalPhysicalAttack(), GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTotalPhysicalDefence()));
								
		// reduce character's AP 
		GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentActionPoint(
				GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP);
						
		// check if the attacked target is dead
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().isAlive() == false) {
				
			// check if should end the game
			GameEnded.isGameEnded();
					
			// check if any barracks are destroyed
			BuildingDatabase.isBarracksDestroyed();
						
			// reset AI isAttack
			AICharacter.isAttack = false;
						
			// if the attacker is hero, add bounty money and bounty Exp into hero's account
			if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsHero() == true) {
							
				// add money
				((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).setMoney(
						((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getMoney()
						+ GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyMoney());
							
				// add Experience
				((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).setExperience(
						((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getExperience()
						+ GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyExp());
							
			}
						
			// if the hero is dead
			if (GridFrame.gridButtonMap[toXPos][toYPos].getIsHero() == true) {
				// deduct dead hero's money
				((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).setMoney(
						((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getMoney() 
						- GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyMoney());
							
				// check if the dead hero is player's hero
				if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer() == true) {
					Screen.user.player.setXPos(Screen.user.playerStartingXPos);
					Screen.user.player.setYPos(Screen.user.playerStartingYPos);
				}
							
				// update reviveQueue
				Pair<Hero, Integer> dead = new Pair<Hero, Integer>((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter(), 
						CalculateLevelInfo.calculateDeathCount(((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getLevel()));
				Hero.reviveQueue.add(dead);
				
			}	
						
			// character is dead, reset the grid which the dead character was at 
			GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(1);
		}
			
		// attack action ended
		GameButtonActions.readyToAttack = false;
	}

	
	private void move() {				
		// can only move on non-occupied and movable grid
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == false) {

			// get the AP required for such movement
			int usedAP = calculateMovementUsedAP(fromXPos, fromYPos, toXPos, toYPos);
			
				// can only move if character has enough AP
				if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP >= 0){
								
					// perform move action
					GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(GridFrame.gridButtonMap[fromXPos][fromYPos]); 
					GridFrame.gridButtonMap[fromXPos][fromYPos] = new GridButton(1);
								
					// reduce character's AP 
					GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentActionPoint(
							GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentActionPoint() - usedAP);
					

					// if hero is player, change player's position
					if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer() == true) {
						Screen.user.player.setXPos(toXPos);
						Screen.user.player.setYPos(toYPos);
					}
								
				}
				
		}			
		
		// move action ended
		GameButtonActions.readyToMove = false;
	}
	
	

	
	
	private int calculateAttackUsedAP() {
		// calculate AP used when performing attack action
		return (int)(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedWhenAttack());
	}


	private int calculateMovementUsedAP(int previouslySelectedXPos, int previouslySelectedYPos, int selectedXPos, int selectedYPos) {
		// calculate AP used by moving from (previouslySelectedXPos, previouslySelectedYPos) to (selectedXPos, selectedYPos)
		FindPath tempPath = new FindPath(GridFrame.gridButtonMap[previouslySelectedXPos][previouslySelectedYPos].getCharacter().getNumberOfMovableGrid());
		
		int numberOfGridsMoved = tempPath.findShortestPath(previouslySelectedXPos, previouslySelectedYPos, selectedXPos, selectedYPos,
				GridFrame.gridButtonMap[previouslySelectedXPos][previouslySelectedYPos].getCharacter().getNumberOfMovableGrid());
		if (numberOfGridsMoved == -1) {
			return -1;
		} else{
			return (int)(numberOfGridsMoved * GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedInMovingOneGrid());
		}
	}


}
