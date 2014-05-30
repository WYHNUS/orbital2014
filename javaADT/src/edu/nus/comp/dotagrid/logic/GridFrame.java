package edu.nus.comp.dotagrid.logic;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class GridFrame {
	
	
	WorldMap worldMap;
	WorldMapFile worldMapFile;
	
	
	public int[][] map = new int[ROW_NUMBER][COLUMN_NUMBER];
	
	public GridButton[][] gridMap = new GridButton[ROW_NUMBER][COLUMN_NUMBER];
	
	// store terrain Images
	public Image[] terrain = new Image[100];
	
	
	public static final int ROW_NUMBER = 200;
	public static final int COLUMN_NUMBER = 200;
	
	private static int gridRowNumberInScreen = 14;
	private static int gridColNumberInScreen = 20;
		
	// default starting position: left bottom corner of the map
	private static int currentGridXPos = 0;
	private static int currentGridYPos = Screen.ROW_NUMBER - gridRowNumberInScreen - 1;
	
	private double gridWidth = 1.0;
	private double gridHeight = 1.0;
	
	
	
	// constructor
	public GridFrame(Graphics g, Screen screen) {
		
		double gameFrameGridWidth = (screen.frame.getWidth() - 2.0 * GameFrame.FRAME_BORDER_WIDTH) / GameFrame.GRID_COL_NUMBER_IN_SCREEN;
		double gameFrameGridHeight = (screen.frame.getHeight() - 2.0 * GameFrame.FRAME_BORDER_HEIGHT) / GameFrame.GRID_ROW_NUMBER_IN_SCREEN;	
		
		gridWidth = GameFrame.FRAME_COL_NUMBER_OCCUPIED * gameFrameGridWidth / gridColNumberInScreen;
		gridHeight = GameFrame.FRAME_ROW_NUMBER_OCCUPIED * gameFrameGridHeight / gridRowNumberInScreen;
		
		worldMapFile = new WorldMapFile();
		
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				int z = x + (y * 10);
				terrain[z] = new ImageIcon("res/WorldMap/terrian" + "/terrian" + z + ".png").getImage();
			}
		}		
		
		this.worldMap = worldMapFile.getWorldMap();
		this.map = this.worldMap.map;
		
		displayGridOnScreen(g);
		
		initializeGridButtonsOnScreen();
			
	}

	private void initializeGridButtonsOnScreen() {
		// initialize grid buttons that can be displayed on screen
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) { 
				gridMap[x][y] = new GridButton();
			}
		}	
	}

	private void displayGridOnScreen(Graphics g) {
		
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) { 
				
				g.drawImage(terrain[map[x + currentGridXPos][y + currentGridYPos]],
						(int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
						(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
						(int) gridHeight, null);
				
				g.drawRect((int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
						(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
						(int) gridHeight);

			}
		}
		
	}
	
	
	
	public static int getCurrentGridXPos(){
		return currentGridXPos;
	}
	
	public static void setCurrentGridXPos(int changedGridXPos){
		if (changedGridXPos <= 0) {
			// changedGridXPos cannot be less than 0
			currentGridXPos = 0;
		} else if (changedGridXPos >= (COLUMN_NUMBER - gridColNumberInScreen - 1)) {
			// changedGridXPos cannot be larger than COLUMN_NUMBER - gridColNumberInScreen - 1
			currentGridXPos = COLUMN_NUMBER - gridColNumberInScreen - 1;
		} else {
			currentGridXPos = changedGridXPos;
		}
	}
	
	
	public static int getCurrentGridYPos(){
		return currentGridYPos;
	}
	
	public static void setCurrentGridYPos(int changedGridYPos){
		if (changedGridYPos <= 0) {
			// changedGridYPos cannot be less than 0
			currentGridYPos = 0;
		} else if (changedGridYPos >= (ROW_NUMBER - gridRowNumberInScreen - 1)) {
			// changedGridYPos cannot be larger than ROW_NUMBER - gridRowNumberInScreen - 1
			currentGridYPos = ROW_NUMBER - gridRowNumberInScreen - 1;
		} else {
			currentGridYPos = changedGridYPos;
		}
	}
	
	
}
