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
					
		// can only attack on non-friendly occupied grid
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == true) {
			
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
					// if the attacker is hero, add bounty money into hero's account
					System.out.println("before attack = " + ((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getMoney());
					System.out.println("bounty money = " + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyMoney());
					if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsHero() == true) {
						((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).setMoney(
								((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getMoney()
								+ GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyMoney());
					}
					
					System.out.println("after attack = " + ((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getMoney());
					
					
					// character is dead, reset the grid which the dead character was at 
					GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(1);
					
				}
			
			} else {			
				JOptionPane.showMessageDialog(null, "not enough action point to attack!");
			}
		}
		
		// attack action ended
		GameButtonActions.readyToAttack = false;
	}

	
	private void move() {
		// get the AP required for such movement
		int usedAP = calculateMovementUsedAP(fromXPos, fromYPos, toXPos, toYPos);
					
		// can only move on non-occupied and movable grid
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsMovable() == true 
				&& GridFrame.gridButtonMap[toXPos][toYPos].getIsOccupied() == false) {
						
			// can only move if character has enough AP
			if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP >= 0){
							
				// perform move action
				resetGridButton(GridFrame.gridButtonMap[fromXPos][fromYPos]); 
				GridFrame.gridButtonMap[fromXPos][fromYPos] = new GridButton(1);
							
				// reduce hero's AP 
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
		int numberOfGridsMoved = Math.abs(previouslySelectedXPos - selectedXPos) + Math.abs(previouslySelectedYPos - selectedYPos);
		
		System.out.println("fromXPos = " + fromXPos);
		System.out.println("fromYPos = " + fromYPos);

		return (int)(numberOfGridsMoved * GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedInMovingOneGrid());
	}


	private void resetGridButton(GridButton resultGridButton){
		GridFrame.gridButtonMap[toXPos][toYPos].setIsMovable(resultGridButton.getIsMovable());
		GridFrame.gridButtonMap[toXPos][toYPos].setIsOccupied(resultGridButton.getIsOccupied());
		GridFrame.gridButtonMap[toXPos][toYPos].setIsPlayer(resultGridButton.getIsPlayer());
		GridFrame.gridButtonMap[toXPos][toYPos].setIsHero(resultGridButton.getIsHero());
		GridFrame.gridButtonMap[toXPos][toYPos].setCharacter(resultGridButton.getCharacter());
	}
	

}
