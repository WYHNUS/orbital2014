package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;

public class WorldMap {

	public final int COLUMN_NUMBER = 200;
	public final int ROW_NUMBER = 200;

	public WorldMap(Graphics g, Frame frame) {
		// construct world map
		double gridWidth = (frame.getWidth() - 250) / (1440 / 1100.0) / Screen.gridRowNumberInScreen;
		double gridHeight = frame.getHeight() / (900 / 700.0) / Screen.gridColNumberInScreen;

		// background
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		// grid
		g.setColor(Color.GRAY);
		for (int x = 0; x < Screen.gridRowNumberInScreen; x++) {
			for (int y = 0; y < Screen.gridColNumberInScreen; y++) {
				g.drawRect(25 + (x * (int) gridWidth),
						25 + (y * (int) gridHeight), (int) gridWidth,
						(int) gridHeight);
			}
		}
		
		/*******************************************
		 * things need to implement:
		 * 
		 * 1. how to repaint map after scaling / moving up/down/left/right
		 * 
		 * 2. how to connect mouse event with Character 
		 * 
		 * *****************************************************/
		
		// image of the character selected
		int startingYPos = (int) gridHeight * Screen.gridColNumberInScreen + 25 + 25;
		g.drawRect(25, startingYPos, 100, 100);
		
		// stuff displayed at the bottom of the screen
		
		// Name, HP , MP
		g.drawRect(25 + 25 + 100, startingYPos, 150, 100 / 3);
		g.drawString("Name: ", 25 + 25+ 100 + 15, startingYPos + 15);

		g.drawRect(25 + 25 + 100, startingYPos + 100 / 3, 150, 100 / 3);
		g.drawString("HP: ", 25 + 25 + 100 + 15, startingYPos + 100 / 3 + 15);

		g.drawRect(25 + 25 + 100, startingYPos + 100 / 3 * 2, 150, 100 / 3);
		g.drawString("MP: ", 25 + 25 + 100 + 15, startingYPos + 200 / 3 + 15);
		
		
		// Strength, Agility, Intelligence
		g.drawRect(25 + 25 * 2 + 100 + 150, startingYPos, 150, 100 / 3);
		g.drawString("Strength: ", 25 + 25 * 2 + 100 + 150 + 15, startingYPos + 15);

		g.drawRect(25 + 25 * 2 + 100 + 150, startingYPos + 100 / 3, 150, 100 / 3);
		g.drawString("Agility: ", 25 + 25 * 2 + 100 + 150 + 15, startingYPos + 100 / 3 + 15);

		g.drawRect(25 + 25 * 2 + 100 + 150, startingYPos + 100 / 3 * 2, 150, 100 / 3);
		g.drawString("Intelligence: ", 25 + 25 * 2 + 100 + 150 + 15, startingYPos + 200 / 3 + 15);
		
		// Attack, Defense
		g.drawRect(25 + 25 * 3 + 100 + 150 + 150, startingYPos, 100, 100 / 2);
		g.drawString("Attack: ", 25 + 25 * 3 + 100 + 150 + 150 + 15, startingYPos + 15);

		g.drawRect(25 + 25 * 3 + 100 + 150 + 150, startingYPos + 100 / 2, 100, 100 / 2);
		g.drawString("Defense: ", 25 + 25 * 3 + 100 + 150 + 150 + 15, startingYPos + 100 / 2 + 15);
		
		// Level, Experience
		g.drawRect(25 + 25 * 4 + 100 + 150 + 150 + 100, startingYPos, 100, 100 / 3);
		g.drawString("Level: ", 25 + 25 * 4 + 100 + 150 + 150 + 100 + 15, startingYPos + 15);
		
		g.drawRect(25 + 25 * 4 + 100 + 150 + 150 + 100, startingYPos + 100 / 3, 100, 200 / 3);
		g.drawString("Experience: ", 25 + 25 * 4 + 100 + 150 + 150 + 100 + 15, startingYPos + 100 / 3 + 15);
		
		// Item List
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2, startingYPos + 50, 50, 50);
		
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2 + 50, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2 + 50, startingYPos + 50, 50, 50);
		
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2 + 50 * 2, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 5 + 100 * 3 + 150 * 2 + 50 * 2, startingYPos + 50, 50, 50);
		
		// Skill List
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3, startingYPos + 50, 50, 50);
		
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3 + 50, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3 + 50, startingYPos + 50, 50, 50);
		
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3 + 50 * 2, startingYPos, 50, 50);
		g.drawRect(25 + 25 * 6 + 100 * 3 + 150 * 3 + 50 * 2, startingYPos + 50, 50, 50);
		
		
		
		// stuff displayed at the right hand side of the screen
		int startingXPos = (int) gridWidth * Screen.gridRowNumberInScreen + 25 + 25;
		
		// Version Info
		g.drawRect(startingXPos, 25, 100, 50);
		g.drawString("Version 1.0", startingXPos + 15, 25 + 30);
		
		// turn count
		g.drawRect(startingXPos + 100, 25, 100, 50);
		g.drawString("Turn: ", startingXPos + 100 + 15, 25 + 30);
		
		// KDA
		g.drawRect(startingXPos, 25 * 2 + 50, 200/3, 25);
		g.drawString("K: ", startingXPos + 13, 25 * 2 + 50 + 17);
		
		g.drawRect(startingXPos + 200/3, 25 * 2 + 50, 200/3, 25);
		g.drawString("D: ", startingXPos + 200/3 + 13, 25 * 2 + 50 + 17);
		
		g.drawRect(startingXPos + 2 * 200/3, 25 * 2 + 50, 200/3, 25);
		g.drawString("A: ", startingXPos + 2 * 200/3 + 13, 25 * 2 + 50 + 17);

		// Money & Action Points
		g.drawRect(startingXPos, 25 * 2 + 50 + 50, 100, 50);
		g.drawString("Money: ", startingXPos + 15, 25 * 2 + 50 + 15 + 50);
		
		g.drawRect(startingXPos + 100, 25 * 2 + 50 + 50, 100, 50);
		g.drawString("ActionPoint: ", startingXPos + 100 + 15, 25 * 2 + 50 + 15 + 50);
		
		// Action List
		g.drawRect(startingXPos, 25 * 3 + 50 + 50 + 50, 50, 50);
		
		g.drawRect(startingXPos + 50, 25 * 3 + 50 + 50 + 50, 50, 50);
		
		g.drawRect(startingXPos + 100, 25 * 3 + 50 + 50 + 50, 50, 50);
		
		g.drawRect(startingXPos + 150, 25 * 3 + 50 + 50 + 50, 50, 50);
		
		g.drawRect(startingXPos, 25 * 3 + 50 + 50 + 50 * 2, 50, 50);
		
		g.drawRect(startingXPos + 50, 25 * 3 + 50 + 50 + 50 * 2, 50, 50);
		
		g.drawRect(startingXPos + 100, 25 * 3 + 50 + 50 + 50 * 2, 50, 50);
		
		g.drawRect(startingXPos + 150, 25 * 3 + 50 + 50 + 50 * 2, 50, 50);

		g.drawRect(startingXPos, 25 * 3 + 50 + 50 + 50 * 3, 50, 50);
		
		g.drawRect(startingXPos + 50, 25 * 3 + 50 + 50 + 50 * 3, 50, 50);
		
		g.drawRect(startingXPos + 100, 25 * 3 + 50 + 50 + 50 * 3, 50, 50);
		
		g.drawRect(startingXPos + 150, 25 * 3 + 50 + 50 + 50 * 3, 50, 50);
		
		g.drawRect(startingXPos, 25 * 3 + 50 + 50 + 50 * 4, 50, 50);
		
		g.drawRect(startingXPos + 50, 25 * 3 + 50 + 50 + 50 * 4, 50, 50);
		
		g.drawRect(startingXPos + 100, 25 * 3 + 50 + 50 + 50 * 4, 50, 50);
		
		g.drawRect(startingXPos + 150, 25 * 3 + 50 + 50 + 50 * 4, 50, 50);
		
			
		// move WorldMap buttons:
		
		// Up button
		g.drawRect(startingXPos + (200 - 40) / 2, 25 * 4 + 50 + 50 + 50 * 5 + 42, 40, 40);
		
		// Game Icon
		g.drawRect(startingXPos + (200 - 40) / 2, 25 * 4 + 50 + 50 + 50 * 5 + 40 + 42, 40, 40);
		
		// Left button
		g.drawRect(startingXPos + (200 - 40) / 2 - 40, 25 * 4 + 50 + 50 + 50 * 5 + 40 + 42, 40, 40);
		
		// Right button
		g.drawRect(startingXPos + (200 - 40) / 2 + 40, 25 * 4 + 50 + 50 + 50 * 5 + 40 + 42, 40, 40);
		
		// Down button
		g.drawRect(startingXPos + (200 - 40) / 2, 25 * 4 + 50 + 50 + 50 * 5 + 40 * 2 + 42, 40, 40);
		
	}

}
