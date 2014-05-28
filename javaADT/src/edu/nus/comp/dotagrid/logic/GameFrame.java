package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


public class GameFrame{
	
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

	
	
	// main game grid frame
	GameButton mainGame;
	
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
	public final int MAX_ITEM_NUMBER = 6;
	
	// skill list
	GameButton[] skills;
	public final int MAX_SKILL_NUMBER = 8;
	
	// version info
	GameButton versionID;
	
	// turns counter
	GameButton turnCounter;
	
	// KDA
	GameButton[] KDA;
	
	// Money & Action Points
	GameButton money;
	GameButton actionPoints;
	
	// Action List 
	GameButton[] actionList;
	public final int MAX_ACTION_NUMBER = 16;
	
	// move mainGame buttons
	GameButton[] directions;
	public final int DIRECTION_NUMBER = 5;
	
	

	public GameFrame(Graphics g, int frameWidth, int frameHeight) {
		
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		gridWidth = (frameWidth - (2.0 * FRAME_BORDER_WIDTH)) / GRID_COL_NUMBER_IN_SCREEN;
		gridHeight = (frameHeight - (2.0 * FRAME_BORDER_HEIGHT)) / GRID_ROW_NUMBER_IN_SCREEN;
		
		// construct game frame

		// Initialize game buttons
		initializeAllButtons();
		
		// draw game frame
		displayAllButtons(g);
		
	}

	
	private void displayAllButtons(Graphics g) {
		g.setColor(defaultColor);
		
		// draw main game frame
		mainGame.drawRect(g);
		
		// stuff displayed at the bottom of the screen
		
		characterIcon.drawRect(g);
		
		characterName.drawRect(g);
		characterName.drawString(g);
		
		
		characterHP.drawRect(g);
		characterHP.drawString(g);
		
		characterMP.drawRect(g);
		characterMP.drawString(g);
		
		characterStrength.drawRect(g);
		characterStrength.drawString(g);

		characterAgility.drawRect(g);
		characterAgility.drawString(g);

		characterIntelligence.drawRect(g);
		characterIntelligence.drawString(g);
		
		characterAttack.drawRect(g);
		characterAttack.drawString(g);
		
		characterDefense.drawRect(g);
		characterDefense.drawString(g);
		
		characterLevel.drawRect(g);
		characterLevel.drawString(g);
		
		characterExperience.drawRect(g);
		characterExperience.drawString(g);
		
		// display item list
		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
			items[i].drawRect(g);
		}
		
		// display skill list
		for (int i=0; i<MAX_SKILL_NUMBER; i++) {
			skills[i].drawRect(g);
		}
		
		
		// stuff displayed at the right hand side of the screen
		
		versionID.drawRect(g);
		versionID.drawString(g);
		
		turnCounter.drawRect(g);

		KDA[0].drawRect(g);
		KDA[1].drawRect(g);
		KDA[2].drawRect(g);
		
		money.drawRect(g);
		actionPoints.drawRect(g);
		
		// display Action List 
		for(int i=0; i<MAX_ACTION_NUMBER; i++){
			actionList[i].drawRect(g);
		}
		
		// display move mainGame buttons
		for(int i=0; i<DIRECTION_NUMBER; i++){
			directions[i].drawRect(g);
		}
		
		
	}

	
	
	private void initializeAllButtons() {
		int startingYPos = FRAME_BORDER_HEIGHT + (int) (gridHeight * (FRAME_ROW_NUMBER_OCCUPIED + 1));
		int startingXPos = FRAME_BORDER_WIDTH + (int) (gridWidth * (FRAME_COL_NUMBER_OCCUPIED + 1));	
		
		// main game frame
		mainGame = new GameButton("main game", null, FRAME_BORDER_WIDTH, FRAME_BORDER_HEIGHT, (int) (FRAME_COL_NUMBER_OCCUPIED * gridWidth), (int) (FRAME_ROW_NUMBER_OCCUPIED * gridHeight));
		
		// image of the character selected
		characterIcon = new GameButton("characterIcon", null, FRAME_BORDER_WIDTH, startingYPos, (int) (2 * gridWidth), (int) (2 * gridHeight));
		
		// Name, HP , MP
		characterName = new GameButton("Name: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterHP = new GameButton("HP: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterMP = new GameButton("MP: ", null, FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		
		// Strength, Agility, Intelligence
		characterStrength = new GameButton("Strength: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterAgility = new GameButton("Agility: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterIntelligence = new GameButton("Intelligence: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));

		// Attack, Defense
		characterAttack = new GameButton("Attack: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));
		characterDefense = new GameButton("Defense: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 2), (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));

		// Level, Experience
		characterLevel = new GameButton("Level: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		characterExperience = new GameButton("Experience: ", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (2.5 * gridWidth), (int) (4.0 * gridHeight / 3));

		
		// Item List (numbers are marked from top to bottom first, then left to right)
		items = new GameButton[MAX_ITEM_NUMBER];
		
		items[0] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[1] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		items[2] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[3] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		items[4] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);
		items[5] = new GameButton("item", null, FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
			
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
					


		// Version Info
		versionID = new GameButton("C-DOTA 1.0", null, startingXPos, FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		
		// turn count
		turnCounter = new GameButton("turnCounter", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		
		// KDA
		KDA = new GameButton[3];
		KDA[0] = new GameButton("Kill", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		KDA[1] = new GameButton("Death", null, startingXPos + (int) (4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		KDA[2] = new GameButton("Assist", null, startingXPos + (int) (2 * 4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));

		// Money & Action Points
		money = new GameButton("Money: ", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);
		actionPoints = new GameButton("Action Points: ", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);

		// Action List 
		// (draw from left to right first, then top to bottom)
		actionList = new GameButton[MAX_ACTION_NUMBER];
		
		actionList[0] = new GameButton("action", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);	
		actionList[1] = new GameButton("action", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[2] = new GameButton("action", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[3] = new GameButton("action", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[4] = new GameButton("action", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[5] = new GameButton("action", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[6] = new GameButton("action", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[7] = new GameButton("action", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[8] = new GameButton("action", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[9] = new GameButton("action", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[10] = new GameButton("action", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[11] = new GameButton("action", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[12] = new GameButton("action", null, startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[13] = new GameButton("action", null, startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[14] = new GameButton("action", null, startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		actionList[15] = new GameButton("action", null, startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
			

		// move mainGame buttons
		directions = new GameButton[DIRECTION_NUMBER];
		
		// Up button
		directions[0] = new GameButton("up", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (4 * gridHeight), (int) gridWidth, (int) gridHeight);	
		
		// Left button
		directions[1] = new GameButton("left", null, startingXPos + (int) (0.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
			
		// Game Icon
		directions[2] = new GameButton("game icon", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);	
		
		// Right button
		directions[3] = new GameButton("right", null, startingXPos + (int) (2.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		// Down button
		directions[4] = new GameButton("down", null, startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (2 * gridHeight), (int) gridWidth, (int) gridHeight);
		
	}

}
