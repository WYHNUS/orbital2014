package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;


public class GameButtonActions {
	public static boolean readyToAct = false;
	public static boolean readyToMove = false;
	public static boolean readyToAttack = false;
	public static boolean readyToCastSpell = false;
	public static boolean readyToUpgradeSkill = false;
	public static boolean readyToUseItem = false;
	
	public static String heroDamageLog = "<html>";
	
	private static int moveRowNumberOfGrid = (int) (GridFrame.getGridRowNumberInScreen() / 2.0);
	private static int moveHeightNumberOfGrid = (int) (GridFrame.getGridColNumberInScreen() / 2.0);
	
	private static double zoomFactor = 2.0;

	public GameButtonActions(int actionNumber) {
		/**
		 * Each actionNumber corresponds to a fixed actionEvent
		 * 
		 * actionNumber 1 : game grid screen moves up by 1/2 of the height of screen's grids
		 * 
		 * actionNumber 2 : game grid screen moves down by 1/2 of the height of screen's grids
		 * 
		 * actionNumber 3 : game grid screen moves left by 1/2 of the width of screen's grids
		 * 
		 * actionNumber 4 : game grid screen moves right by 1/2 of the width of screen's grids
		 * 
		 * actionNumber 5 : zoom in game grid screen by reducing size of a factor of 2
		 * 
		 * actionNumber 6 : zoom out game grid screen by increasing size of a factor of 2
		 * 
		 * actionNumber 7 : end this round, start next round: 
		 * 						1. reset all AP 
		 * 						2. check if need to spawn new wave of creeps
		 * 
		 * actionNumber 8 : invoke ready to movement event
		 * 
		 * actionNumber 9 : invoke ready to attack event
		 * 
		 * actionNumber 10 : 
		 * 
		 * actionNumber 11 : 
		 * 
		 * actionNumber 12 : pop up a item shop menu
		 * 
		 * actionNumber 13 : pop up a sell item menu
		 * 
		 * actionNumber 20 - 27 : invoke ready to cast spell event
		 * 
		 * actionNumber 28 - 33 : invoke use item event
		 * 
		 */

		switch (actionNumber) {
									
			case 0:
				break;
				
			case 1:
				gameGridMoveUp();
				break;
	
			case 2:
				gameGridMoveDown();
				break;
	
			case 3:
				gameGridMoveLeft();
				break;
	
			case 4:
				gameGridMoveRight();
				break;
				
			case 5:
				zoomInGameGrid();
				break;
				
			case 6:
				zoomOutGameGrid();
				break;
				
			case 7 :
				endRound();
				break;
				
			case 8:
				readyToMove();
				break;
				
			case 9 :
				readyToAttack();
				break;
				
			case 10 :
				upgradeSkill();
				break;
			
			case 11 : 
				break;
			
			case 12 : 
				showItemShop();
				break;
				
			case 13 :
				sellItem();
				break;
						
				
				
			case 20 :
				playerHeroCastSpell(0);
				break;
				
			case 21 :
				playerHeroCastSpell(1);
				break;
				
			case 22 :
				playerHeroCastSpell(2);
				break;
				
			case 23 :
				playerHeroCastSpell(3);
				break;
				
			case 24 :
				playerHeroCastSpell(4);
				break;
				
			case 25 :
				playerHeroCastSpell(5);
				break;
				
			case 26 :
				playerHeroCastSpell(6);
				break;
				
			case 27 :
				playerHeroCastSpell(7);
				break;
				
				
			case 28 :
				playerHeroUseItem(0);
				break;
				
			case 29 :
				playerHeroUseItem(1);
				break;
				
			case 30 :
				playerHeroUseItem(2);
				break;
				
			case 31 :
				playerHeroUseItem(3);
				break;
				
			case 32 :
				playerHeroUseItem(4);
				break;
				
			case 33 :
				playerHeroUseItem(5);
				break;
		}
	}

	

	private void playerHeroUseItem(int playerItemIndex) {
		// based on playerItemIndex, use corresponding item
		
		// check if the selected grid is player's hero!
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer()) {
					
			// check if hero's item list is empty
			if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[playerItemIndex] != null) {
				// check if the item is usable
				if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[playerItemIndex].isUsable()) {
					// check if usable time is non-zero
					if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[playerItemIndex].getUsableTime() > 0) {
						// highlight usable range
						((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[playerItemIndex].
							invokeItemAction(Screen.user.player.getXPos(), Screen.user.player.getYPos());
						
						// get ready to use item
						readyToAct = true;
						readyToUseItem = true;
						
						Player.invokedPlayerItemIndex = playerItemIndex;
					} else {
						System.out.println("Not enough usable times!");
					}
				} else {
					System.out.println("item not usable!");
				}
			}
		}
	}



	private void upgradeSkill() {
		// check if selected character is player's hero
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer()) {
			// check if player's hero has positive unused skill point
			if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getUnusedSkillCount() > 0) {
				readyToUpgradeSkill = true;
			} else {
				JOptionPane.showMessageDialog(null, "You don't have any unused skill point!");
			}
		}
	}



	private void playerHeroCastSpell(int playerSkillIndex) {
		// based on playerSkillIndex, determine which player's skill to cast
		
		// check if the selected grid is player's hero!
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer()) {
			
			// check if hero's skill list is empty
			if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex] != null) {

				// if not empty, check if the action is to cast spell or upgrade skill
				if (readyToUpgradeSkill == true) {
					System.out.println("ready to upgrade skill!");
					// upgrade skill !
					((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].
						setSkillLevel(((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].getSkillLevel()
								+ 1);
					// deduct unused skill count
					((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).
						setUnusedSkillCount(((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getUnusedSkillCount() - 1);

					readyToUpgradeSkill = false;
					
					// display a panel to inform the user skill has been upgraded
					Skill tempSkill = new Skill(((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex]);
					JOptionPane.showMessageDialog(null, "You have upgrade the skill : " + tempSkill.getSkillName() + " from level " 
							+ (tempSkill.getSkillLevel() - 1) + " to level " + tempSkill.getSkillLevel() + " !");
					
					if (!((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].isCastable()) {
						// select player's hero's grid button
						System.out.println("reselect player's hero!");
						GridFrame.invokeLeftClickEvent((int)((0.5 + Screen.user.player.getXPos() - GridFrame.getCurrentGridXPos()) * GridFrame.getGridWidth() + GameFrame.FRAME_BORDER_HEIGHT), 
								(int)((0.5 + Screen.user.player.getYPos() - GridFrame.getCurrentGridYPos()) * GridFrame.getGridHeight() + GameFrame.FRAME_BORDER_WIDTH));
					}
					
				} else {
					
					// check if the skill is usable
					if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].isCastable()) {
						// cast spell if skill level is not 0!
						if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].getSkillLevel() > 0) {
							((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].
									invokeSkillAction(Screen.user.player.getXPos(), Screen.user.player.getYPos());
							
							readyToAct = true;
							readyToCastSpell  = true;
							
							Skill.invokedSkillType = ((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).skills[playerSkillIndex].getSkillType();
							Player.invokedPlayerSkillIndex = playerSkillIndex;
						} else {
							JOptionPane.showMessageDialog(null, "You have not leanrt this skill!");
						}
					} else {
						System.out.println("A non-castable skill has been selected!");
					}
				}
			}
			
		}
	}


	private void sellItem() {
		// only invoke if player's hero is within item shop range
		int playerXPos = Screen.user.player.getXPos();
		int playerYPos = Screen.user.player.getYPos();
		if ((playerXPos >= ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[0] && 
				playerXPos < ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[0] + ItemShop.SENTINEL_ITEM_SHOP_X_OFFSET &&
				playerYPos >= ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[1] && 
				playerYPos < ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[1] + ItemShop.SENTINEL_ITEM_SHOP_Y_OFFSET) 
				||
				(playerXPos >= ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[0] && 
				playerXPos < ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[0] + ItemShop.SCOURGE_ITEM_SHOP_X_OFFSET &&
				playerYPos >= ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[1] && 
				playerYPos < ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[1] + ItemShop.SCOURGE_ITEM_SHOP_Y_OFFSET)) {
			// sell an item from player's bought item list
			System.out.println("show sell item interface!");
			new SellItem();
		} else {
			System.out.println("need to move closer to the fountain in order to invoke this action!");
			JOptionPane.showMessageDialog(null, "You need to move closer to the fountain to invoke this action!");
		}
	}


	private void showItemShop() {
		// only invoke if player's hero is within item shop range
		int playerXPos = Screen.user.player.getXPos();
		int playerYPos = Screen.user.player.getYPos();
		if ((playerXPos >= ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[0] && 
				playerXPos < ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[0] + ItemShop.SENTINEL_ITEM_SHOP_X_OFFSET &&
				playerYPos >= ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[1] && 
				playerYPos < ItemShop.SENTINEL_ITEM_SHOP_AREA_POS[1] + ItemShop.SENTINEL_ITEM_SHOP_Y_OFFSET) 
				||
				(playerXPos >= ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[0] && 
				playerXPos < ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[0] + ItemShop.SCOURGE_ITEM_SHOP_X_OFFSET &&
				playerYPos >= ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[1] && 
				playerYPos < ItemShop.SCOURGE_ITEM_SHOP_AREA_POS[1] + ItemShop.SCOURGE_ITEM_SHOP_Y_OFFSET)) {
			// create a new frame to show item shop list
			System.out.println("show item shop interface!");
			new ItemShop();
		} else {
			System.out.println("need to move closer to the fountain in order to invoke this action!");
			JOptionPane.showMessageDialog(null, "You need to move closer to the fountain to invoke this action!");
		}
	}


	public static void endRound() {
		GameFrame.turn++;
		GameFrame.allCharacterInfoGameButtons.get(25).setString("Turn : " + GameFrame.turn);

		System.out.println();
		System.out.println("Turn Number :  " + GameFrame.turn);
		
		// AI's turn
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				if (GridFrame.gridButtonMap[x][y].getCharacter() != null){
					// only non-player controlled character need AI to perform action
					if (!(GridFrame.gridButtonMap[x][y].getIsPlayer() && GridFrame.gridButtonMap[x][y].getCharacter() instanceof Hero)){
						// check if the character is summon character
						if (GridFrame.gridButtonMap[x][y].getCharacter() instanceof SummonCharacter) {
							// AI action!
							new AICharacter(x, y);
							
							if (GridFrame.gridButtonMap[x][y].getCharacter() != null) {
								// reduce the current duration for summon character
								((SummonCharacter)GridFrame.gridButtonMap[x][y].getCharacter()).setCurrentDuration(
										((SummonCharacter)GridFrame.gridButtonMap[x][y].getCharacter()).getCurrentDuration() - 1);
								
								// check if the duration is below 0
								if (((SummonCharacter)GridFrame.gridButtonMap[x][y].getCharacter()).getCurrentDuration() <= 0) {
									// discard the summon character
									GridFrame.gridButtonMap[x][y] = new GridButton(1);
								}
							}
						} else {
							// AI action!
							new AICharacter(x, y);
						}
					}
				}
			}
		}

		// reset all attributes
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				if (GridFrame.gridButtonMap[x][y].getCharacter() != null){
					
					// reset AP for all existing characters
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentActionPoint(GridFrame.gridButtonMap[x][y].getCharacter().getMaxActionPoint());
					
					// reset HP and MP for all existing characters
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentHP((int)(GridFrame.gridButtonMap[x][y].getCharacter().getCurrentHP() + GridFrame.gridButtonMap[x][y].getCharacter().getHPGainPerRound()));
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentMP((int)(GridFrame.gridButtonMap[x][y].getCharacter().getCurrentMP() + GridFrame.gridButtonMap[x][y].getCharacter().getMPGainPerRound()));
					
					// reset attack priority
					GridFrame.gridButtonMap[x][y].getCharacter().setCurrentAttackPriority(GridFrame.gridButtonMap[x][y].getCharacter().getStartingAttackPriority());
					
					// if character is Tower or Building, check and update protectionPosList
					if (GridFrame.gridButtonMap[x][y].getCharacter() instanceof Tower || GridFrame.gridButtonMap[x][y].getCharacter() instanceof Building)
					{
						ArrayList<int[]> protectedPos;
						if (GridFrame.gridButtonMap[x][y].getCharacter() instanceof Tower){
							protectedPos = ((Tower)GridFrame.gridButtonMap[x][y].getCharacter()).getProtectionPosList();
						} else if (GridFrame.gridButtonMap[x][y].getCharacter() instanceof Building){
							protectedPos = ((Building)GridFrame.gridButtonMap[x][y].getCharacter()).getProtectionPosList();
						} else {
							protectedPos = new ArrayList<int[]>();
						}
						
						for (Iterator<int[]> iterator = protectedPos.iterator(); iterator.hasNext();) {
							int[] element = iterator.next();
							// check if there is character
							if (GridFrame.gridButtonMap[element[0]][element[1]].getCharacter() != null) {
								// check if the character is Tower
								if (! (GridFrame.gridButtonMap[element[0]][element[1]].getCharacter() instanceof Tower)) { 
									// remove element from arraylist
									System.out.println("x = " + element[0] + "    y = " + element[1]);
									System.out.println("Remove Protection because character is not tower!");
									iterator.remove();
								}
							} else {
								// remove element from arraylist
								System.out.println("x = " + element[0] + "    y = " + element[1]);
								System.out.println("Remove Protection because there is no existing character!");
								iterator.remove();
							}
						}
						
						if (protectedPos.isEmpty()) {
							// not protected
							GridFrame.gridButtonMap[x][y].getCharacter().setAttackable(true);
						} else {
							// protected
							GridFrame.gridButtonMap[x][y].getCharacter().setAttackable(false);
						}
					}
					
					// if character is hero, reset all hero's skills' current cooldown round
					if (GridFrame.gridButtonMap[x][y].getIsHero()) {
						// add money to hero's account
						Hero.addPerTurnMoney((Hero)GridFrame.gridButtonMap[x][y].getCharacter());
						for (int i=0; i<GameFrame.MAX_SKILL_NUMBER; i++){
							// check if skill exists
							if (((Hero)GridFrame.gridButtonMap[x][y].getCharacter()).skills[i] != null){
								((Hero)GridFrame.gridButtonMap[x][y].getCharacter()).skills[i].setCurrentCoolDownRound(((Hero)GridFrame.gridButtonMap[x][y].getCharacter()).skills[i].getCurrentCoolDownRound() - 1);
							}
						}
					}
						
				}
			}
		}	// reset attributes ended
		

		// update reviveQueue
		for (Iterator<Pair<Hero, Integer>> iterator = Hero.reviveQueue.iterator(); iterator.hasNext();) {
			Pair<Hero, Integer> element = iterator.next();
			element.setSecond(element.getSecond() - 1);
			Hero.addPerTurnMoney(element.getFirst());
			if (element.getSecond() <= 0) {
				// hero revive from its original spawning position
				GridFrame.gridButtonMap[element.getFirst().getHeroSpawningXPos()][element.getFirst().getHeroSpawningYPos()]
						= new GridButton(element.getFirst());
				
				// check if the revived hero is player's hero
				if (element.getFirst().getHeroSpawningXPos() == Screen.user.playerStartingXPos
						&& element.getFirst().getHeroSpawningYPos() == Screen.user.playerStartingYPos) {
					GridFrame.gridButtonMap[element.getFirst().getHeroSpawningXPos()][element.getFirst().getHeroSpawningYPos()].setIsPlayer(true);
				}
				
				iterator.remove();
			}
		}
		
		// revive trees
		Tree.treeRevive();

		// spawn a new wave of creeps
		LineCreepSpawnPoint.spawnNewWave();
		NeutralCreepSpawnPoint.spawnNewWave();
		
		
		heroDamageLog += "</html>";
		if (heroDamageLog.length() >= 14) 
				JOptionPane.showMessageDialog(null, heroDamageLog);
		
		heroDamageLog = "<html>";
		// reselect the grid
		GridFrame.invokeLeftClickEvent(GridFrame.getSelectedXCoordinatePos(), GridFrame.getSelectedYCoordinatePos());
		System.out.println("End Round!");
	}	

	
	public static void readyToAttack() {
		// get ready for player's hero to perform physical attack if player's character is selected
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToAttack = true;
		} else {
			String str = "You need to select your own hero to execute this action!";
			JOptionPane.showMessageDialog(null, str);
		}
	}


	public static void readyToMove() {
		// get ready for player's hero to move
		if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsPlayer() == true){
			readyToAct = true;
			readyToMove = true;
		} else {
			String str = "You need to select your own hero to execute this action!";
			JOptionPane.showMessageDialog(null, str);
		}
	}


	private void zoomOutGameGrid() {
		// zoom out game grid screen
		GridFrame.setGridColNumberInScreen((int) (GridFrame.getGridColNumberInScreen() * zoomFactor));
		GridFrame.setGridRowNumberInScreen((int) (GridFrame.getGridRowNumberInScreen() * zoomFactor));
		
		// check the boundary conditions:
		
		// x-axis position has gone beyond the maximum column number
		if(GridFrame.getCurrentGridXPos() + GridFrame.getGridColNumberInScreen() >= GridFrame.COLUMN_NUMBER) {
			GridFrame.isMaxMapShown = true;
			GridFrame.setCurrentGridXPos((int) (GridFrame.COLUMN_NUMBER - GridFrame.getGridColNumberInScreen()));
		}
		
		// y-axis position has gone beyond the maximum row number
		if(GridFrame.getCurrentGridYPos() + GridFrame.getGridRowNumberInScreen() >= GridFrame.ROW_NUMBER) {
			GridFrame.isMaxMapShown = true;
			GridFrame.setCurrentGridYPos((int) (GridFrame.ROW_NUMBER - GridFrame.getGridRowNumberInScreen()));
		}
		
		System.out.println("zoomOutGameGrid has been invoked!");
	}


	private void zoomInGameGrid() {
		if (GridFrame.isMaxMapShown) {
			// hard code to adjust the screen display
			GridFrame.setGridColNumberInScreen(80);
			GridFrame.setGridRowNumberInScreen(56);
			GridFrame.isMaxMapShown = false;
		} else {
			// zoom in game grid screen
			GridFrame.setGridColNumberInScreen((int) (GridFrame.getGridColNumberInScreen() / zoomFactor));
			GridFrame.setGridRowNumberInScreen((int) (GridFrame.getGridRowNumberInScreen() / zoomFactor));
		}
		System.out.println("zoomInGameGrid has been invoked!");
	}


	public static void gameGridMoveRight() {
		// change currentGridXPos
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() + moveHeightNumberOfGrid);
		System.out.println("gameGridMoveRight has been invoked!");
	}

	public static void gameGridMoveLeft() {
		// change currentGridXPos
		GridFrame.setCurrentGridXPos(GridFrame.getCurrentGridXPos() - moveHeightNumberOfGrid);
		System.out.println("gameGridMoveLeft has been invoked!");
	}

	public static void gameGridMoveDown() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() + moveRowNumberOfGrid);
		System.out.println("gameGridMoveDown has been invoked!");
	}

	public static void gameGridMoveUp() {
		// change currentGridYPos
		GridFrame.setCurrentGridYPos(GridFrame.getCurrentGridYPos() - moveRowNumberOfGrid);
		System.out.println("gameGridMoveUp has been invoked!");
	}

}
