package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;

public class GameFrame {
	
	public static final int FRAME_BORDER_WIDTH = 20;
	public static final int FRAME_BORDER_HEIGHT = 20;
	
	// total grid space in the game frame
	public static final int GRID_ROW_NUMBER_IN_SCREEN = 17;
	public static final int GRID_COL_NUMBER_IN_SCREEN = 25;
	
	// space to draw the main game frame 
	public static final int FRAME_ROW_NUMBER_OCCUPIED = 14;
	public static final int FRAME_COL_NUMBER_OCCUPIED = 20;

	public GameFrame(Graphics g, Frame frame) {
		// construct game frame
		double gridWidth = (frame.getWidth() - 2 * FRAME_BORDER_WIDTH) / GRID_COL_NUMBER_IN_SCREEN;
		double gridHeight = (frame.getHeight() - 2 * FRAME_BORDER_HEIGHT) / GRID_ROW_NUMBER_IN_SCREEN;

		// background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		// grid frame
		g.setColor(Color.GRAY);
		g.drawRect(FRAME_BORDER_WIDTH, FRAME_BORDER_HEIGHT, (int) (FRAME_COL_NUMBER_OCCUPIED * gridWidth), (int) (FRAME_ROW_NUMBER_OCCUPIED * gridHeight));
		
		/*******************************************
		 * things need to implement:
		 * 
		 * 1. how to repaint map after scaling / moving up/down/left/right
		 * 
		 * 2. how to connect mouse event with Character 
		 * 
		 * *****************************************************/

		int startingYPos = FRAME_BORDER_HEIGHT + (int) (gridHeight * (FRAME_ROW_NUMBER_OCCUPIED + 1));
		
		// image of the character selected
		g.drawRect(FRAME_BORDER_WIDTH, startingYPos, (int) (2 * gridWidth), (int) (2 * gridHeight));
		
		// stuff displayed at the bottom of the screen
		
		// Name, HP , MP
		g.drawRect(FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("Name: ", FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth) + 15, startingYPos + 20);

		g.drawRect(FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("HP: ", FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth) + 15, startingYPos + (int) (2 * gridHeight / 3) + 20);

		g.drawRect(FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("MP: ", FRAME_BORDER_WIDTH + (int) (2 * gridWidth + 0.5 * gridWidth) + 15, startingYPos + (int) (4.0 * gridHeight / 3) + 20);
		
		
		// Strength, Agility, Intelligence
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos, (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("Strength: ", FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth) + 15, startingYPos + 20);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("Agility: ", FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth) + 15, startingYPos + (int) (2.0 * gridHeight / 3) + 20);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth), startingYPos + (int) (4.0 * gridHeight / 3), (int) (3.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("Intelligence: ", FRAME_BORDER_WIDTH + (int) ((2+3.5) * gridWidth + (2*0.5) * gridWidth) + 15, startingYPos + (int) (4.0 * gridHeight / 3) + 20);
		
		// Attack, Defense
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));
		g.drawString("Attack: ", FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth) + 15, startingYPos + 25);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 2), (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 2));
		g.drawString("Defense: ", FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5) * gridWidth + (3*0.5) * gridWidth) + 15, startingYPos + (int) (2.0 * gridHeight / 2) + 25);
		
		// Level, Experience
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos, (int) (2.5 * gridWidth), (int) (2.0 * gridHeight / 3));
		g.drawString("Level: ", FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth) + 15, startingYPos + 20);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth), startingYPos + (int) (2.0 * gridHeight / 3), (int) (2.5 * gridWidth), (int) (4.0 * gridHeight / 3));
		g.drawString("Experience: ", FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5) * gridWidth + (4*0.5) * gridWidth) + 15, startingYPos + (int) (2 * gridHeight / 2) + 15);
		
		// Item List
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
	
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5) * gridWidth + (5*0.5) * gridWidth + 2 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
	
		// Skill List
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
	
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 2 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 2 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		
		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 3 * gridWidth), startingYPos, (int) gridWidth, (int) gridHeight);

		g.drawRect(FRAME_BORDER_WIDTH + (int) ((2+3.5+3.5+2.5+2.5+3) * gridWidth + (6*0.5) * gridWidth + 3 * gridWidth), startingYPos + (int) gridHeight, (int) gridWidth, (int) gridHeight);
		
		
		
		// stuff displayed at the right hand side of the screen
		int startingXPos = FRAME_BORDER_WIDTH + (int) (gridWidth * (FRAME_COL_NUMBER_OCCUPIED + 1));
		
		// Version Info
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		g.drawString("Version 1.0", startingXPos + 15, FRAME_BORDER_HEIGHT + 25);
		
		// turn count
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT, (int) (2 * gridWidth), (int) gridHeight);
		g.drawString("Turn : ", startingXPos + (int) (2 * gridWidth) + 15, FRAME_BORDER_HEIGHT + 25);
		
		// KDA
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		g.drawString("K: ", startingXPos + 15, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight) + 15);
		
		g.drawRect(startingXPos + (int) (4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		g.drawString("D: ", startingXPos + (int) (4.0 / 3 * gridWidth) + 15, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight) + 15);
		
		g.drawRect(startingXPos + (int) (2 * 4.0 / 3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight), (int) (4.0 / 3 * gridWidth), (int) (0.5 * gridHeight));
		g.drawString("A: ", startingXPos + (int) (2 * 4.0 / 3 * gridWidth) + 15, FRAME_BORDER_HEIGHT + (int) (gridHeight + 0.5 * gridHeight) + 15);

		// Money & Action Points
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);
		g.drawString("Money: ", startingXPos + 15, FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight) + 25);
		
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight), (int) (2 * gridWidth), (int) gridHeight);
		g.drawString("Action Points: ", startingXPos + (int) (2 * gridWidth) + 15, FRAME_BORDER_HEIGHT + (int) (2.5 * gridHeight) + 25);
		
		// Action List 
		// (draw from left to right first, then top to bottom)
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 1.0 * gridHeight), (int) gridWidth, (int) gridHeight);

		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 2.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) gridWidth, FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (2 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		g.drawRect(startingXPos + (int) (3 * gridWidth), FRAME_BORDER_HEIGHT + (int) (4.0 * gridHeight + 3.0 * gridHeight), (int) gridWidth, (int) gridHeight);
		
			
		// move WorldMap buttons:
		
		// Up button
		g.drawRect(startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (4 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		// Game Icon
		g.drawRect(startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		// Left button
		g.drawRect(startingXPos + (int) (0.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		// Right button
		g.drawRect(startingXPos + (int) (2.5 * gridWidth), startingYPos - (int) (3 * gridHeight), (int) gridWidth, (int) gridHeight);
		
		// Down button
		g.drawRect(startingXPos + (int) (1.5 * gridWidth), startingYPos - (int) (2 * gridHeight), (int) gridWidth, (int) gridHeight);
		
	}

}
