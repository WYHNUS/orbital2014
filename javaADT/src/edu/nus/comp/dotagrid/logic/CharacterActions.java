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
		 * actionNumber 4 : use skill
		 * 
		 * actionNumber 5 : use item
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
				
			case 5 : 
				useItem();
				break;
		}
		
	}

	
	private void useItem() {
		// casting hero is at [fromXPos][fromYPos] position, target position is at [toXPos][toYPos]
		// actions are different depending on item type
		
	}


	private void castSpell() {
		// casting hero is at [fromXPos][fromYPos] position, target position is at [toXPos][toYPos]
		// actions are different depending on skill type
		
		switch (Skill.invokedSkillType) {
		
			case 1 :
				// teleport
				teleport();
				break;
				
			case 2 :
				// summon creatures
				summonCreatures();
				break;
		}
		
	}


	private void summonCreatures() {
		// summon creatures centered at selected grid position
		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		int teamNumber = castingHero.getTeamNumber();
		Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
		int castingRange = castingSkill.getSummonRange();
		boolean isCasted = false;
		
		for (int i=-castingRange; i<=castingRange; i++) {
			for (int j=-castingRange; j<=castingRange; j++) {
				// create summon character condition
				if (Math.abs(i) + Math.abs(j) == castingRange) {
					// check if summon character can be added to the grid
					if (withinBoundary(toXPos+i, toYPos+j)) {
						if (GridFrame.gridButtonMap[toXPos+i][toYPos+j].getIsMovable()) {
							// only add summon creature on movable grid
							isCasted = true;
							if (!GridFrame.gridButtonMap[toXPos+i][toYPos+j].getIsOccupied()) {
								// add creature!
								GridFrame.gridButtonMap[toXPos+i][toYPos+j] = new GridButton(new SummonCharacter(castingSkill.getSkillCharacter()));
								GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter().setTeamNumber(teamNumber);
							}
						}
					}
				}
			}
		}
		
		// check if the spell has been successfully casted!
		if (isCasted) {
			// reduce character's AP 
			GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentActionPoint(
					GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - castingSkill.getUsedActionPoint());
												
			// reduce character's MP
			GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentMP(
					GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentMP() - castingSkill.getUsedMP());
										
			// reset current cooldown
			((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).skills[Player.invokedPlayerSkillIndex].setCurrentCoolDownRound(castingSkill.getCoolDownRounds());		
		}
		
		// casting action ended
		GameButtonActions.readyToCastSpell = false;	
	}


	private boolean withinBoundary(int i, int j) {
		// check if i and j are inside the gridButtonMap range
		return (i>=0 && i<GridFrame.COLUMN_NUMBER && j>=0 && j<GridFrame.ROW_NUMBER);
	}


	private void teleport() {
		
		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
	
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
		System.out.println("attack!");
		
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
				
				// reset hero's HP
				((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).setCurrentHP(
						((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getmaxHP());
							
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
					System.out.println("move!");
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
								
				} else {
					System.out.println("Not enough AP!");
				}	
				
		} else {
			System.out.println("Non-movable or already occupied grid!");
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
		
		int numberOfGridsMoved = tempPath.findShortestPath(previouslySelectedXPos, previouslySelectedYPos, selectedXPos, selectedYPos);
		
		if (numberOfGridsMoved > GridFrame.gridButtonMap[previouslySelectedXPos][previouslySelectedYPos].getCharacter().getNumberOfMovableGrid()
				|| numberOfGridsMoved == -1) {
			return -1;
		} else{
			return (int)(numberOfGridsMoved * GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedInMovingOneGrid());
		}
	}


}
