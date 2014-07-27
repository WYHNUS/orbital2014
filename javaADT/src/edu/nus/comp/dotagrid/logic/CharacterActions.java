package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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
				summonStationaryCreatures();
				break;
				
			case 3 :
				// hit a maximum number of enemies in sight
				attackAllEnemies();
				break;
				
			case 4 :
				// summon creatures by destroying trees
				convertTreeToCreatures();
				break;
		}
		
	}


	private void convertTreeToCreatures() {
		// converting trees to creatures
		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
		int counter = 0;
		boolean isCasted = false;

		// convert nearest tree to creature until search end or enough number of creatures has been created
		Queue<int[]> positionQueue = new LinkedList<int[]>();
		int[] startingPos = {toXPos, toYPos};
		positionQueue.add(startingPos);
		ArrayList<int[]> checkedPosition = new ArrayList<int[]>();
		while (!positionQueue.isEmpty()) {
			int xPos = positionQueue.peek()[0];
			int yPos = positionQueue.peek()[1];
			int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
			for (int i=0; i<4; i++) {
				int checkXPos = xPos + dirs[i][0];
				int checkYPos = yPos + dirs[i][1];
				if (!AICharacter.isChecked(checkedPosition, positionQueue, checkXPos, checkYPos)) {
					if ((Math.abs(checkXPos - toXPos) + Math.abs(checkYPos - toYPos)) <= castingSkill.getSummonRange()) {
						int[] newPos = {checkXPos, checkYPos};
						positionQueue.add(newPos);
					}
				}
			}
			// check if should replace the tree
			if (GridFrame.gridButtonMap[xPos][yPos].getCharacter() != null) {
				if (GridFrame.gridButtonMap[xPos][yPos].getCharacter() instanceof Tree) {
					isCasted = true;
					// destroy tree in map
					GridFrame.map[xPos][yPos] = 1;
					if (counter < castingSkill.getNumberOfChara()){
						// destroy tree and display summoned character
						SummonCharacter chara = new SummonCharacter(castingSkill.getSkillCharacter());
						chara.setTeamNumber(castingHero.getTeamNumber());
						GridFrame.gridButtonMap[xPos][yPos] = new GridButton(chara);
						// summon creature is controlled by player
						GridFrame.gridButtonMap[xPos][yPos].setIsPlayer(true);
						counter++;
						System.out.println("summon character " + chara.getName() + " !!!");
					} else {
						GridFrame.gridButtonMap[xPos][yPos] = new GridButton(1);
					}
				}
			}
			checkedPosition.add(positionQueue.poll());
		}
		
		if (isCasted) {
			resetAttributes(castingSkill);
		}
		
		// casting action ended
		GameButtonActions.readyToCastSpell = false;	
	}


	private void attackAllEnemies() {
		// hit a maximum number of enemies in sight
		Hero castingHero = new Hero(((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()));
		int teamNumber = castingHero.getTeamNumber();
		Skill castingSkill = new Skill(castingHero.skills[Player.invokedPlayerSkillIndex]);
		int magicalDamage = castingSkill.getMagicalDamage();
		int counter = 0;
		
		String skillDamageLog = "<html>";
		
		// check through the map for enemies
		for (int y=0; y<GridFrame.COLUMN_NUMBER; y++){
			for (int x=0; x<GridFrame.ROW_NUMBER; x++){
				// check if the grid has character and is within sight map
				if (GridFrame.gridButtonMap[y][x].getCharacter() != null &&
						GridFrame.sightMap[y][x] == 1) {
					// check if the character is enemy and attackable
					if (GridFrame.gridButtonMap[y][x].getCharacter().getTeamNumber() != teamNumber &&
							GridFrame.gridButtonMap[y][x].getCharacter().getTotalMagicResistance() != 100 &&
							GridFrame.gridButtonMap[y][x].getCharacter().isAttackable()) {
						// counter cannot go beyond maximum attack units
						if (counter <= castingSkill.getNumberOfChara()) {
							// attack!
							int actualDamage = Character.getMangicalDamage(magicalDamage, GridFrame.gridButtonMap[y][x].getCharacter().getTotalMagicResistance());
							GridFrame.gridButtonMap[y][x].getCharacter().setCurrentHP((int) (GridFrame.gridButtonMap[y][x].getCharacter().getCurrentHP() 
									- actualDamage));
							
							skillDamageLog = skillDamageLog + castingSkill.getSkillName() + " has cause " + actualDamage + " damage to " 
									+ GridFrame.gridButtonMap[y][x].getCharacter().getName() + "<br>";
							
							// check if the attacked target is dead
							if (GridFrame.gridButtonMap[y][x].getCharacter().isAlive() == false) {
								
								// if the attacker is hero, add bounty money and bounty Exp into hero's account
								checkToAddMoneyAndExp(y, x);
									
								// if the hero is dead
								checkIfHeroIsDead(y, x);
											
								// character is dead, reset the grid which the dead character was at 
								GridFrame.gridButtonMap[y][x] = new GridButton(1);
							}
							
							counter++;
						}
					}
				}
			}
		}
		
		System.out.println(skillDamageLog);
		if (skillDamageLog.length() > 6) {
			skillDamageLog += "</html>";
			JOptionPane.showMessageDialog(null, skillDamageLog);
		} else {
			JOptionPane.showMessageDialog(null, "You have none 0 damage!");
		}

		resetAttributes(castingSkill);
		
		// casting action ended
		GameButtonActions.readyToCastSpell = false;	
	}

	private void summonStationaryCreatures() {
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
							if (GridFrame.gridButtonMap[toXPos+i][toYPos+j].getCharacter() == null) {
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
			resetAttributes(castingSkill);
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
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsMovable()
				&& GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() == null) {

			resetAttributes(castingSkill);
			
			// perform move action
			GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(GridFrame.gridButtonMap[fromXPos][fromYPos]); 
			GridFrame.gridButtonMap[fromXPos][fromYPos] = new GridButton(1);

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



	private void resetAttributes(Skill castingSkill) {
		// reset hero's attributes
		// reduce character's AP 
		GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentActionPoint(
				GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - castingSkill.getUsedActionPoint());

		// reduce character's MP
		GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentMP(
				GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentMP() - castingSkill.getUsedMP());

		// reset current cooldown
		((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).skills[Player.invokedPlayerSkillIndex].setCurrentCoolDownRound(castingSkill.getCoolDownRounds());		
	}


	private void updateSightMap() {
		// update sight map centered at position (toXPos, toYPos)
		int sightRange = GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getSight();
		ArrayList<Pair<double[], double[]>> blockedAngleList = new ArrayList<Pair<double[], double[]>>(); 

		// check and add all in sight blocking element into the list 
		for(int x=toXPos-sightRange; x<=toXPos+sightRange; x++) {
			for(int y=toYPos-sightRange; y<=toYPos+sightRange; y++) {
				// x and y need to be within the grid frame 
				if (x >= 0 && x < GridFrame.COLUMN_NUMBER && y>=0 && y < GridFrame.ROW_NUMBER) {
					// x + y need to be within the number of sight grid
					if (Math.abs(toXPos - x) + Math.abs(toYPos - y) <= sightRange) {
						if (GridFrame.gridButtonMap[x][y].isBlockSight()) {
							Pair<double[], double[]> blockingElementAngles = calculateAngles(toXPos+0.5, toYPos+0.5, x, y);
							blockedAngleList.add(blockingElementAngles);
						}
					}
				}
			}
		}

		
		// reset sight map for any visible element
		for(int x=toXPos-sightRange; x<=toXPos+sightRange; x++) {
			for(int y=toYPos-sightRange; y<=toYPos+sightRange; y++) {
				// x and y need to be within the grid frame 
				if (x >= 0 && x < GridFrame.COLUMN_NUMBER && y>=0 && y < GridFrame.ROW_NUMBER) {
					// x + y need to be within the number of sight grid
					if (Math.abs(toXPos - x) + Math.abs(toYPos - y) <= sightRange) {
						
						// check if the grid has been blocked by any element in the list
						boolean isBlocked = checkIsBlocked(blockedAngleList, x, y, toXPos, toYPos);
						
						// only set grid to visible if it is not blocked by any other grid
						if (!isBlocked) {
							GridFrame.sightMap[x][y] = 1;
						}
					}
				}
			}
		} // end for loop
	}

	public static boolean checkIsBlocked(
			ArrayList<Pair<double[], double[]>> blockedAngleList, 
			int checkedXPos, int checkedYPos, int startingXPos, int startingYPos) {

		// check if the selected grid can be set to visible 
		Pair<double[], double[]> selectedGridAngles = calculateAngles(startingXPos+0.5, startingYPos+0.5, checkedXPos, checkedYPos);
		double selectedGridAverageAngle; 
		
		// special condition : selected grid is directly at the right of the starting grid
		if (checkedYPos == startingYPos && checkedXPos > startingXPos) {
			selectedGridAverageAngle = 0;
		} else {
			selectedGridAverageAngle = (selectedGridAngles.getFirst()[0] + selectedGridAngles.getFirst()[3]) / 2.0;
		}
		
		boolean isBlocked = false;
		boolean needCheck = false;
		
		for (Pair<double[], double[]> element : blockedAngleList) {
			
			// boolean is used to check if the selected grid is within blocked angle's check range
			if (element.getSecond()[0] > startingXPos && element.getSecond()[1] > startingYPos) {
				// 1st quadrant
				if (checkedXPos >= element.getSecond()[0] && checkedYPos >= element.getSecond()[1]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] < startingXPos && element.getSecond()[1] > startingYPos) {
				// 2nd quadrant
				if (checkedXPos <= element.getSecond()[0] && checkedYPos >= element.getSecond()[1]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] < startingXPos && element.getSecond()[1] < startingYPos) {
				// 3rd quadrant
				if (checkedXPos <= element.getSecond()[0] && checkedYPos <= element.getSecond()[1]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] > startingXPos && element.getSecond()[1] < startingYPos) {
				// 4th quadrant
				if (checkedXPos >= element.getSecond()[0] && checkedYPos <= element.getSecond()[1]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] > startingXPos && element.getSecond()[1] == startingYPos) {
				// directly on the right
				if (checkedXPos >= element.getSecond()[0]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] < startingXPos && element.getSecond()[1] == startingYPos) {
				// directly on the left
				if (checkedXPos <= element.getSecond()[0]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] == startingXPos && element.getSecond()[1] > startingYPos) {
				// directly on the top
				if (checkedYPos >= element.getSecond()[1]) {
					needCheck = true;
				}
				
			} else if (element.getSecond()[0] == startingXPos && element.getSecond()[1] < startingYPos) {
				// directly on the bottom
				if (checkedYPos <= element.getSecond()[1]) {
					needCheck = true;
				}
			} 
			
			if (checkedXPos == element.getSecond()[0] && checkedYPos == element.getSecond()[1]) {
				needCheck = false;
			}
			
			
			if (needCheck) {
				// special case : grid is directly on the right of the starting grid
				if (element.getSecond()[0] > startingXPos && element.getSecond()[1] == startingYPos) {
					if (selectedGridAverageAngle <= element.getFirst()[1] || selectedGridAverageAngle >= element.getFirst()[2]) {
						// within range! remove from the sight map
						isBlocked = true;
					}
				}
				// check if the average angle of the selected grid is within the blocking element angle's range
				else if (selectedGridAverageAngle >= element.getFirst()[0] && selectedGridAverageAngle <= element.getFirst()[3]) {
					// within range! remove from the sight map
					isBlocked = true;
				}
			}
			
		}
		
		return isBlocked;
	}


	public static Pair<double[], double[]> calculateAngles(double startingX, double startingY, double blockingX, double blockingY) {
		// calculate both the maximum and minimum angle from (startingX, startingY) to grid at (blockingX, blockingY)
		
		double angle1 = getAngle(startingX, startingY, blockingX, blockingY);
		double angle2 = getAngle(startingX, startingY, blockingX+1, blockingY);
		double angle3 = getAngle(startingX, startingY, blockingX, blockingY+1);
		double angle4 = getAngle(startingX, startingY, blockingX+1, blockingY+1);
		
		double[] minAndMaxAngle = {angle1, angle2, angle3, angle4} ;
		Arrays.sort(minAndMaxAngle);
		
		double[] blockingPos = {blockingX, blockingY};
				
		return new Pair<double[], double[]>(minAndMaxAngle, blockingPos);
	}


	private static double getAngle(double startingX, double startingY, double blockingX, double blockingY) {
		// calculate angle between (startingX, startingY) and (blockingX, blockingY)

		// angle modifier changes the coordinate of the angle from (-PI/2, PI/2) to (0, 2PI), first quadrant no need to modify
		double angleModifier = 0;
		if (blockingX < startingX ) {
			// second and third quadrant, modify by PI
			angleModifier = Math.PI;
		} else if (blockingX > startingX && blockingY < startingY) {
			// fourth quadrant, modify by 2PI
			angleModifier = 2.0 * Math.PI;
		}
		
		return (Math.atan((blockingY - startingY) / (blockingX - startingX)) + angleModifier);
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
		
		// get the AP required for one physical attack
		int usedAP = calculateAttackUsedAP();

		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer()) {
			System.out.println("hero current hp = " + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP());
			System.out.println("attack = " + attack);
			System.out.println("set current hp = " + (int) (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP() - attack));
			System.out.println("hero max hp = " + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getmaxHP());
		}
		
		// perform attack action
		GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentHP((int) (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentHP() 
				- attack));
		
		// check if the attacker is hero's unit, if yes, show attacker's damage done
		if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsPlayer()) 
			JOptionPane.showMessageDialog(null, "You have done " + String.format("%.2f", attack) + " damage to " + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getName());
								
		// reduce character's AP 
		GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentActionPoint(
				GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP);
		
		// check if the target is player's character
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer()) {
			if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() instanceof Hero) 
				GameButtonActions.heroDamageLog += "player's hero : ";
			else
				GameButtonActions.heroDamageLog += "player's summoned creature : ";
			
			GameButtonActions.heroDamageLog = GameButtonActions.heroDamageLog + GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getName()
					+ " has taken " + String.format("%.2f", attack) + " damage from " 
					+ GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getName() + "<br>";
		}
				
		// check if the target character is hero
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() instanceof Hero) {
			// if yes, set attacker's attack priority to 1
			GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().setCurrentAttackPriority(1);
		}
		
		// check if the attacked target is dead
		if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().isAlive() == false) {
				
			// check if should end the game
			GameEnded.isGameEnded();
					
			// check if any barracks are destroyed
			BuildingDatabase.isBarracksDestroyed();
						
			// reset AI isAttack
			AICharacter.isAttack = false;
						
			// if the attacker is hero, add bounty money and bounty Exp into hero's account
			checkToAddMoneyAndExp(toXPos, toYPos);
			
			// hard code!!!!!!!!
			if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsPlayer() && GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter() instanceof SummonCharacter) {						
				// add money to player
				((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).setMoney(
						((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getMoney()
						+ GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getBountyMoney());							
			}
							
			// if the hero is dead
			checkIfHeroIsDead(toXPos, toYPos);
						
			// character is dead, reset the grid which the dead character was at 
			GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(1);
		}
			
		// attack action ended
		GameButtonActions.readyToAttack = false;
	}

	
	private void checkToAddMoneyAndExp(int xPos, int yPos) {
		// check and add money and Exp to hero if the attacker is a hero
		
		if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsHero()) {						
			// add money
			((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).setMoney(
					((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getMoney()
					+ GridFrame.gridButtonMap[xPos][yPos].getCharacter().getBountyMoney());							
		}

		// add exp!
		// check if nearby has non-friendly hero
		ArrayList<Hero> addExpHero = new ArrayList<Hero>();
		for (int i=-EXP_GAIN_RANGE; i<=EXP_GAIN_RANGE; i++) {
			for (int j=-EXP_GAIN_RANGE; j<=EXP_GAIN_RANGE; j++) {
				// check if within range
				if (Math.abs(i) + Math.abs(j) <= EXP_GAIN_RANGE) {
					// check if within grid frame
					if (withinBoundary(xPos+i, yPos+j)) {
						// check if has character
						if (GridFrame.gridButtonMap[xPos+i][yPos+j].getCharacter() != null) {
							// check if the character is non-friendly hero
							if ((GridFrame.gridButtonMap[xPos+i][yPos+j].getCharacter() instanceof Hero) &&
									(GridFrame.gridButtonMap[xPos+i][yPos+j].getCharacter().getTeamNumber() == 
									GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getTeamNumber())) {
								// add the hero into the temp hero arraylist for future sharing of experience
								addExpHero.add((Hero) GridFrame.gridButtonMap[xPos+i][yPos+j].getCharacter());
							}
						}
					}
				}
			}
		}
		
		if (!addExpHero.isEmpty()){
			double sharedExp = (1.0 * GridFrame.gridButtonMap[xPos][yPos].getCharacter().getBountyExp()) / addExpHero.size();
			// add exp to each hero in the arraylist
			for (Hero element : addExpHero) {
				element.setExperience((int) (element.getExperience() + sharedExp));
			}
		}
		
	}


	private void checkIfHeroIsDead(int xPos, int yPos) {
		// do actions if the attacked hero is dead
		
		if (GridFrame.gridButtonMap[xPos][yPos].getIsHero()) {
			// deduct dead hero's money
			((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).setMoney(
					((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).getMoney() 
					- GridFrame.gridButtonMap[xPos][yPos].getCharacter().getBountyMoney());
			
			// reset hero's HP
			((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).setCurrentHP(
					((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).getmaxHP());
			
			// set player's alive status
			((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).setAlive(true);
						
			// reset hero's MP
			((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).setCurrentMP(
					((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).getmaxMP());
			
			// add hero's death
			((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).setDeath(
					((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).getDeath() + 1);
			
			// check if the dead hero is player's hero
			if (GridFrame.gridButtonMap[xPos][yPos].getIsPlayer() == true) {
				JOptionPane.showMessageDialog(null, "You have been slayed.");
				Screen.user.player.setXPos(Screen.user.playerStartingXPos);
				Screen.user.player.setYPos(Screen.user.playerStartingYPos);
			}
			
			// check if the attacker if a hero
			if (GridFrame.gridButtonMap[fromXPos][fromYPos].getIsHero()) {
				// add kill
				((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).setKill(
						((Hero)GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter()).getKill() + 1);				
			}
			
			// update reviveQueue
			Pair<Hero, Integer> dead = new Pair<Hero, Integer>((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter(), 
					CalculateLevelInfo.calculateDeathCount(((Hero)GridFrame.gridButtonMap[xPos][yPos].getCharacter()).getLevel()));
			Hero.reviveQueue.add(dead);
			
		}	
	}


	private void move() {				
		// can only move on non-occupied and movable grid
		if (GridFrame.gridButtonMap[toXPos][toYPos].getIsMovable()
				&& GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() == null) {

			// get the AP required for such movement
			int usedAP = calculateMovementUsedAP(fromXPos, fromYPos, toXPos, toYPos);
			
				// can only move if character has enough AP
				if (GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().getCurrentActionPoint() - usedAP >= 0){
					System.out.println("move!");
					System.out.println("Character move from position (" + fromXPos + ", " + fromYPos + ") to position (" + toXPos + ", " + toYPos + ")");
					
					// perform move action
					GridFrame.gridButtonMap[toXPos][toYPos] = new GridButton(GridFrame.gridButtonMap[fromXPos][fromYPos]); 
					GridFrame.gridButtonMap[fromXPos][fromYPos] = new GridButton(1);
								
					// reduce character's AP 
					GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().setCurrentActionPoint(
							GridFrame.gridButtonMap[toXPos][toYPos].getCharacter().getCurrentActionPoint() - usedAP);
					

					// if hero is player, change player's position
					if (GridFrame.gridButtonMap[toXPos][toYPos].getIsPlayer()) {
						System.out.println("Player has moved!");
						// check if the character is player's hero
						if (GridFrame.gridButtonMap[toXPos][toYPos].getCharacter() instanceof Hero) {
							System.out.println("Reset player's hero's position!");
							Screen.user.player.setXPos(toXPos);
							Screen.user.player.setYPos(toYPos);
						}
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
		
		
		/*
		int[][] tempPathMap = tempPath.getPath();
		
		for (int i=0; i<tempPathMap.length; i++) {
			for (int j=0; j<tempPathMap.length; j++) {
				System.out.printf("%3s", tempPathMap[j][i]);
			}
			System.out.println();
		}
		*/
		
		if (numberOfGridsMoved > GridFrame.gridButtonMap[previouslySelectedXPos][previouslySelectedYPos].getCharacter().getNumberOfMovableGrid()
				|| numberOfGridsMoved == -1) {
			return -1;
		} else{
			return (int)(numberOfGridsMoved * GridFrame.gridButtonMap[fromXPos][fromYPos].getCharacter().APUsedInMovingOneGrid());
		}
	}


}
