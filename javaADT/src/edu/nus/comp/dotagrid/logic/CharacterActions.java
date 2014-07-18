package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class CharacterActions {
	public static final int EXP_GAIN_RANGE = 10;
	public static final double ATTACK_VARIATION = 0.05;
	
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

		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		
		switch (castingHero.items[Player.invokedPlayerItemIndex].getItemName()) {
		
			case "clarity" : 
				if (!increaseMP(75)) return;
				break;
				
			case "flask" :
				if (!increaseHP(400)) return;
				break;
		
		}
		
		// decrease item usable times by 1
		((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).items[Player.invokedPlayerItemIndex].setUsableTime(
				((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).items[Player.invokedPlayerItemIndex].getUsableTime() - 1);

		// check if the item should be discarded
		if (((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).items[Player.invokedPlayerItemIndex].getUsableTime() <= 0) {
			System.out.println("discardble = " + ((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).items[Player.invokedPlayerItemIndex].isDiscardAfterUse());
			if (((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).items[Player.invokedPlayerItemIndex].isDiscardAfterUse()) {
				// discard the item
				System.out.println("discard!");
				((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).removeItem(Player.invokedPlayerItemIndex);
			}
		}
		
	}


	private boolean increaseHP(int increaseNumber) {
		// if [toXPos][toYPos] is friendly unit, increase the unit's HP by increaseNumber
		if (checkIfToPosIsFriendlyUnit()){
			GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentHP(
					increaseNumber + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP());
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "You need to use the item on a friendly unit!");
			return false;
		}
	}

	private boolean increaseMP(int increaseNumber) {
		// if [toXPos][toYPos] is friendly unit, increase the unit's MP by increaseNumber
		if (checkIfToPosIsFriendlyUnit()){
			GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentMP(
					increaseNumber + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentMP());
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "You need to use the item on a friendly unit!");
			return false;
		}
	}


	private boolean checkIfToPosIsFriendlyUnit() {
		// check if [toXPos][toYPos] is friendly unit
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() != null) {
			return (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTeamNumber() == 
					GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTeamNumber());
		} else {
			return false;
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
		
		// get attack variation
		Random rdm = new Random();
		double attackVariation = rdm.nextDouble() * 2.0 * ATTACK_VARIATION - ATTACK_VARIATION + 1.0;
		
		// check for critical strike
		boolean isCriticalStrike = false;
		
		if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCriticalStrikeChance() > 0) {
			isCriticalStrike = (rdm.nextDouble() > GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCriticalStrikeChance());
		} 
		
		// set final attack for this character
		double attack = attackVariation * Character.getActualDamage(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTotalPhysicalAttack(), 
				GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTotalPhysicalDefence());
		
		if (isCriticalStrike) {
			System.out.println("critical attack!");
			attack *= GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCriticalStrikeMultiplier();
		}
		
		// check if the attacked target is building or tower
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() instanceof Tower) {
			// check if the protection has been invoked
			if (!((Tower)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getProtectionPosList().isEmpty()) {
				System.out.println("Attacked Tower has been protected!");
				// set attack to 1
				attack = 1;
			}
		}
		
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() instanceof Building) {
			// check if the protection has been invoked
			if (!((Building)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getProtectionPosList().isEmpty()) {
				System.out.println("Attacked Building has been protected!");
				// set attack to 1
				attack = 1;
			} else {
				System.out.println(GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getName() + " is not protected!");
			}
		}
		
		// get the AP required for one physical attack
		int usedAP = calculateAttackUsedAP();
				
		// perform attack action
		GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentHP((int) (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP() 
				- attack));
								
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
							
			}
			
			// check if nearby has non-friendly hero
			ArrayList<Hero> addExpHero = new ArrayList<Hero>();
			for (int i=-EXP_GAIN_RANGE; i<=EXP_GAIN_RANGE; i++) {
				for (int j=-EXP_GAIN_RANGE; j<=EXP_GAIN_RANGE; j++) {
					// check if within range
					if (Math.abs(i) + Math.abs(j) <= EXP_GAIN_RANGE) {
						// check if within grid frame
						if (withinBoundary(toXPos+i, toYPos+j)) {
							// check if has character
							if (GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter() != null) {
								// check if the character is non-friendly hero
								if ((GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter() instanceof Hero) &&
										(GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter().getTeamNumber() == 
										GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTeamNumber())) {
									// add the hero into the temp hero arraylist for future sharing of experience
									addExpHero.add((Hero) GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter());
								}
							}
						}
					}
				}
			}
			
			if (!addExpHero.isEmpty()){
				double sharedExp = (1.0 * GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyExp()) / addExpHero.size();
				// add exp to each hero in the arraylist
				for (Hero element : addExpHero) {
					element.setExperience((int) (element.getExperience() + sharedExp));
				}
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
							
				// reset hero's MP
				((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).setCurrentMP(
						((Hero)GridFrame.gridButtonMap[toXPos][toYPos].getCharacter()).getmaxMP());
				
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
