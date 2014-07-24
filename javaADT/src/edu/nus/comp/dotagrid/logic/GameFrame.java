package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;


public class GameFrame{
	
	public static int turn = 1;
	
	public int frameWidth;
	public int frameHeight;
	
	Color defaultColor = Color.GRAY;
	
	public static final int FRAME_BORDER_WIDTH = 20;
	public static final int FRAME_BORDER_HEIGHT = 20;
	
	// total grid space in the game frame
	public static final int GRID_ROW_NUMBER_IN_SCREEN = 17;
	public static final int GRID_COL_NUMBER_IN_SCREEN = 25;
	
	// space to draw the main game frame 
	public static final int FRAME_ROW_NUMBER_OCCUPIED = 14;
	public static final int FRAME_COL_NUMBER_OCCUPIED = 20;
	
	public double gridWidth = 1.0;
	public double gridHeight = 1.0;

	
	
	// an ArrayList of GameButton which store all the GameButtons on the GameFrame
	private static ArrayList<GameButton> allGameButtons = new ArrayList<GameButton>();
	
	// an ArrayList of GameButton which store all the GameButtons that displays character's information
	public static ArrayList<GameButton> allCharacterInfoGameButtons = new ArrayList<GameButton>();
	
	// image of the character selected
	GameButton characterIcon;
	
	// Name, HP , MP
	GameButton characterName;
	GameButton characterHP;
	GameButton characterMP;
	
	// Strength, Agility, Intelligence
	GameButton characterStrength;
	GameButton characterAgility;
	GameButton characterIntelligence;
	
	// Attack, Defense
	GameButton characterAttack;
	GameButton characterDefense;
	
	// Level, Experience
	GameButton characterLevel;
	GameButton characterExperience;
	
	// item list
	GameButton[] items;
	public static final int MAX_ITEM_NUMBER = 6;
	
	// skill list
	GameButton[] skills;
	public static final int MAX_SKILL_NUMBER = 8;
	
	// version info
	GameButton versionID;
	
	// turns counter
	GameButton turnCounter;
	
	// KDA
	GameButton Kill;
	GameButton Death;
	GameButton Assist;
	
	// Money & Action Points
	GameButton money;
	GameButton actionPoints;
	
	// Action List 
	GameButton[] actionList;
	public final int MAX_ACTION_NUMBER = 16;
	
	// move mainGame buttons
	GameButton[] directions;
	public final int DIRECTION_NUMBER = 5;
	
	// zoom in button and zoom out button
	GameButton zoomIn;
	GameButton zoomOut;
	

	// constructor
	public GameFrame(Graphics g, Frame frame) {
		this.frameWidth = frame.getWidth();
		this.frameHeight = frame.getHeight();
		
		gridWidth = (frameWidth - (2.0 * FRAME_BORDER_WIDTH)) / GRID_COL_NUMBER_IN_SCREEN;
		gridHeight = (frameHeight - (2.0 * FRAME_BORDER_HEIGHT)) / GRID_ROW_NUMBER_IN_SCREEN;
		
		// construct game frame

		// Initialize game buttons
		initializeAllButtons();
		
		// draw game frame
		displayAllButtons(g);
		drawAllReadyImage(g);
		
	}
	
	
	public void updateGameFrame(Graphics g){
		// draw game frame
		drawAllReadyImage(g);
		displayAllButtons(g);
		
	}

	
	private void displayAllButtons(Graphics g) {
		g.setColor(defaultColor);
		
		//display allGameButtons
		for (int i=0; i<allGameButtons.size(); i++) {
			allGameButtons.get(i).drawString(g);
		}
	}
	
	
	private void drawAllReadyImage(Graphics g) {
		//display all ready images
		System.out.println("display all ready game button images!");
		for (int i=0; i<allGameButtons.size(); i++) {
			// check if image is ready to draw
			if (allGameButtons.get(i).getIsReadyToDrawImage()){
				allGameButtons.get(i).drawImage(g);
			}
		}
	}

	
	
	private void initializeAllButtons() {
		int startingYPos = FRAME_BORDER_HEIGHT + (int) (gridHeight * (FRAME_ROW_NUMBER_OCCUPIED + 1));
		int startingXPos = FRAME_BORDER_WIDTH + (int) (gridWidth * (FRAME_COL_NUMBER_OCCUPIED + 1));	
		
		// image of the character selected
		characterIcon = new GameButton("characterIcon", null, FRAME_BORDER_WIDTH, startingYPos, (int) (2 * gridWidth), (int) (2 * gridHeight));
		allGameButtons.add(characterIcon);
		allCharacterInfoGameButtons.add(characterIcon);
		
		// Name, HP , MP
		characterName = new GameButton("Name: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterHP = new GameButton("HP: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterMP = new GameButton("MP: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		
		allGameButtons.add(characterName);
		allGameButtons.add(characterHP);
		allGameButtons.add(characterMP);
		
		allCharacterInfoGameButtons.add(characterName);
		allCharacterInfoGameButtons.add(characterHP);
		allCharacterInfoGameButtons.add(characterMP);
		
		
		// Strength, Agility, Intelligence
		characterStrength = new GameButton("Strength: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterAgility = new GameButton("Agility: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterIntelligence = new GameButton("Intelligence: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));

		allGameButtons.add(characterStrength);
		allGameButtons.add(characterAgility);
		allGameButtons.add(characterIntelligence);
		
		allCharacterInfoGameButtons.add(characterStrength);
		allCharacterInfoGameButtons.add(characterAgility);
		allCharacterInfoGameButtons.add(characterIntelligence);
		
		// Attack, Defense
		characterAttack = new GameButton("Attack: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));
		characterDefense = new GameButton("Defense: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 2), (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));
		
		allGameButtons.add(characterAttack);
		allGameButtons.add(characterDefense);
		
		allCharacterInfoGameButtons.add(characterAttack);
		allCharacterInfoGameButtons.add(characterDefense);
		
		// Level, Experience
		characterLevel = new GameButton("Level: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (gridHeight));
		characterExperience = new GameButton("Experience: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos + (int) (gridHeight), (int) (2.5 * gridWidth), (int) (2.0 * gridHeight));

		allGameButtons.add(characterLevel);
		allGameButtons.add(characterExperience);
		
		allCharacterInfoGameButtons.add(characterLevel);
		allCharacterInfoGameButtons.add(characterExperience);
		
		// Item List (numbers are marked from top to bottom first, then left to right)
		items = new GameButton[MAX_ITEM_NUMBER];
		
		items[0] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[1] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		items[2] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[3] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		items[4] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[5] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
			
		items[0].setActionNumber(28);
		items[1].setActionNumber(29);
		items[2].setActionNumber(30);
		items[3].setActionNumber(31);
		items[4].setActionNumber(32);
		items[5].setActionNumber(33);
		
		allGameButtons.add(items[0]);
		allGameButtons.add(items[1]);
		allGameButtons.add(items[2]);
		allGameButtons.add(items[3]);
		allGameButtons.add(items[4]);
		allGameButtons.add(items[5]);
		
		allCharacterInfoGameButtons.add(items[0]);
		allCharacterInfoGameButtons.add(items[1]);
		allCharacterInfoGameButtons.add(items[2]);
		allCharacterInfoGameButtons.add(items[3]);
		allCharacterInfoGameButtons.add(items[4]);
		allCharacterInfoGameButtons.add(items[5]);
		
		// Skill List (numbers are marked from top to bottom first, then left to right)
		skills = new GameButton[MAX_SKILL_NUMBER];
		
		skills[0] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		skills[1] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		skills[2] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		skills[3] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		skills[4] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 2 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		skills[5] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 2 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		skills[6] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 3 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		skills[7] = new GameButton("skill", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 3 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
				
		skills[0].setActionNumber(20);
		skills[1].setActionNumber(21);
		skills[2].setActionNumber(22);
		skills[3].setActionNumber(23);
		skills[4].setActionNumber(24);
		skills[5].setActionNumber(25);
		skills[6].setActionNumber(26);
		skills[7].setActionNumber(27);
		
		allGameButtons.add(skills[0]);
		allGameButtons.add(skills[1]);
		allGameButtons.add(skills[2]);
		allGameButtons.add(skills[3]);
		allGameButtons.add(skills[4]);
		allGameButtons.add(skills[5]);
		allGameButtons.add(skills[6]);
		allGameButtons.add(skills[7]);
		
		allCharacterInfoGameButtons.add(skills[0]);
		allCharacterInfoGameButtons.add(skills[1]);
		allCharacterInfoGameButtons.add(skills[2]);
		allCharacterInfoGameButtons.add(skills[3]);
		allCharacterInfoGameButtons.add(skills[4]);
		allCharacterInfoGameButtons.add(skills[5]);
		allCharacterInfoGameButtons.add(skills[6]);
		allCharacterInfoGameButtons.add(skills[7]);


		// Version Info
		versionID = new GameButton("C-DOTA 1.0", null, startingXPos, FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		allGameButtons.add(versionID);
		
		// turn count
		turnCounter = new GameButton("Turn : 1", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		allGameButtons.add(turnCounter);
		allCharacterInfoGameButtons.add(turnCounter);
		
		// KDA
		Kill = new GameButton("Kill : 0", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		Death = new GameButton("Death : 0", null, startingXPos + (int) (4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		Assist = new GameButton("Assist : 0", null, startingXPos + (int) (2 * 4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));

		allGameButtons.add(Kill);
		allGameButtons.add(Death);
		allGameButtons.add(Assist);
		
		allCharacterInfoGameButtons.add(Kill);
		allCharacterInfoGameButtons.add(Death);
		allCharacterInfoGameButtons.add(Assist);
		
		// Money & Action Points
		money = new GameButton("Money: ", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);
		actionPoints = new GameButton("AP : 100 / 100", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);

		allGameButtons.add(money);
		allGameButtons.add(actionPoints);
		
		allCharacterInfoGameButtons.add(money);
		allCharacterInfoGameButtons.add(actionPoints);
		
		// Action List 
		// (draw from left to right first, then top to bottom)
		actionList = new GameButton[MAX_ACTION_NUMBER];
		
		actionList[0] = new GameButton("move", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);	
		actionList[1] = new GameButton("attack", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[2] = new GameButton("+skill", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[3] = new GameButton("", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[4] = new GameButton("shop", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[5] = new GameButton("sell", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[6] = new GameButton("", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[7] = new GameButton("", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[8] = new GameButton("", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[9] = new GameButton("", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[10] = new GameButton("", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[11] = new GameButton("", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[12] = new GameButton("", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[13] = new GameButton("", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[14] = new GameButton("", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[15] = new GameButton("", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
			
		actionList[0].setActionNumber(8);
		actionList[1].setActionNumber(9);
		actionList[2].setActionNumber(10);
		actionList[4].setActionNumber(12);
		actionList[5].setActionNumber(13);

		allGameButtons.add(actionList[0]);
		allGameButtons.add(actionList[1]);
		allGameButtons.add(actionList[2]);
		allGameButtons.add(actionList[3]);
		allGameButtons.add(actionList[4]);
		allGameButtons.add(actionList[5]);
		allGameButtons.add(actionList[6]);
		allGameButtons.add(actionList[7]);
		allGameButtons.add(actionList[8]);
		allGameButtons.add(actionList[9]);
		allGameButtons.add(actionList[10]);
		allGameButtons.add(actionList[11]);
		allGameButtons.add(actionList[12]);
		allGameButtons.add(actionList[13]);
		allGameButtons.add(actionList[14]);
		allGameButtons.add(actionList[15]);
		
		// move mainGame buttons
		directions = new GameButton[DIRECTION_NUMBER];
		
		// Up button
		directions[0] = new GameButton("up", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (4 * gridHeight), (int) gridWidth, (int) gridHeight);	
		directions[0].setActionNumber(1);
		directions[0].setImage("move up");
		
		// Left button
		directions[1] = new GameButton("left", null, startingXPos + (int) (0.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		directions[1].setActionNumber(3);
		directions[1].setImage("move left");
		
		// End Round Button
		directions[2] = new GameButton("END", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);	
		directions[2].setActionNumber(7);
		directions[2].setImage("end round");
		
		// Right button
		directions[3] = new GameButton("right", null, startingXPos + (int) (2.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		directions[3].setActionNumber(4);
		directions[3].setImage("move right");
		
		// Down button
		directions[4] = new GameButton("down", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (2 * gridHeight), (int) gridWidth, (int) gridHeight);
		directions[4].setActionNumber(2);
		directions[4].setImage("move down");
		
		allGameButtons.add(directions[0]);
		allGameButtons.add(directions[1]);
		allGameButtons.add(directions[2]);
		allGameButtons.add(directions[3]);
		allGameButtons.add(directions[4]);
		
		// zoom in button and zoom out button
		zoomIn = new GameButton("zoomIn", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (10.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		zoomOut = new GameButton("zoomOut", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (10.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		zoomIn.setActionNumber(5);
		zoomIn.setImage("zoom in");
		zoomOut.setActionNumber(6);
		zoomOut.setImage("zoom out");
		
		allGameButtons.add(zoomIn);
		allGameButtons.add(zoomOut);
		
	}

	
	public static void invokeLeftClickEvent(int handXPos, int handYPos){
		MouseHandler.isClicked = true;
		// check through allGameButtons for the correct button that has been pressed
		for (int i=0; i<allGameButtons.size(); i++) {
			if (allGameButtons.get(i).checkEvent(handXPos, handYPos)) {
				allGameButtons.get(i).resetBoolean();
				allGameButtons.get(i).actionPerformed(allGameButtons.get(i).getActionNumber());
				break;
			}
		}
	}

	
	public static void invokeRightClickEvent(int handXPos, int handYPos){
		
		// check through allGameButtons for the correct button that has been pressed
		for (int i=0; i<allGameButtons.size(); i++) {
			if (allGameButtons.get(i).checkEvent(handXPos, handYPos)) {
				allGameButtons.get(i).resetBoolean();
				// pop-out a menu to show the selected game button's detailed information
				new ButtonInfoFrame(i);
				break;
			}
		}
	}
}
