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
		 * */
		
		switch(actionNumber){
			case 1 :
				move();
				break;
				
			case 2 :
				attack();
				break;
		}
		
	}

	private void attack() {
		// get the AP required for one physical attack
		int usedAP = calculateAttackUsedAP();
					
		// can only attack on occupied grid
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == true) {
			
			// can only attack non-friendly units
			if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTeamNumber() != GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTeamNumber()) {
			
				// can only attack if character has enough AP
				if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP >= 0){
									
					// perform attack action
					GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentHP(GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP() 
							- Character.getActualDamage(GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTotalPhysicalAttack(), GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getTotalPhysicalDefence()));
								
					// reduce character's AP 
					GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentActionPoint(
							GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP);
					
					// check if the attacked target is dead
					if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().isAlive() == false) {
						
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
						
						
						// character is dead, reset the grid which the dead character was at 
						GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(1);
						
					}
				
				} else {			
					JOptionPane.showMessageDialog(null, "not enough action point to attack!");
				}
				
			} else {
				JOptionPane.showMessageDialog(null, "Unable to attack friendly units!");
			}
			
		} else {
			JOptionPane.showMessageDialog(null, "Need to attack a existing character!");
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
