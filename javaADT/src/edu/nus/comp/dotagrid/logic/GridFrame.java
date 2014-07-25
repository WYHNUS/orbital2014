package edu.nus.comp.dotagrid.logic;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class GridFrame {
	
	WorldMap worldMap;
	WorldMapFile worldMapFile;

	// a public boolean to check if any JFrame has popped up
	public static boolean popupJFrame = false;
	
	public static final int ROW_NUMBER = 100;
	public static final int COLUMN_NUMBER = 100;
	
	public static final int minRowNumberInScreen = 7;
	public static final int minColNumberInScreen = 10;
	
	private static int gridRowNumberInScreen = 14;
	private static int gridColNumberInScreen = 20;
		
	// default starting position: left bottom corner of the map
	private static int currentGridXPos = 0;
	private static int currentGridYPos = ROW_NUMBER - gridRowNumberInScreen - 1;
	
	public static double gameFrameGridWidth = 1.0;
	public static double gameFrameGridHeight = 1.0;
	
	public static double gameFrameWidth = 1.0;
	public static double gameFrameHeight = 1.0;
	
	private static double gridWidth = 1.0;
	private static double gridHeight = 1.0;
	
	// grid position that is to be highlighted
	private static int selectedXCoodinatePos = -1;
	private static int selectedYCoodinatePos = -1;
	
	private static int selectedXPos = -1;
	private static int selectedYPos = -1;
	
	private static int previouslySelectedXPos = -1;
	private static int previouslySelectedYPos = -1;
	
	// store terrain Images
	public static Image[] terrain = new Image[10];
	
	public static int[][] map = new int[ROW_NUMBER][COLUMN_NUMBER];
	public static boolean[][] treeMap = new boolean[ROW_NUMBER][COLUMN_NUMBER];
	public static int[][] highlightedMap = new int[ROW_NUMBER][COLUMN_NUMBER];
	public static int[][] sightMap = new int[ROW_NUMBER][COLUMN_NUMBER];
	public static int[][] attackRangeMap = new int[ROW_NUMBER][COLUMN_NUMBER];
	
	public static GridButton[][] gridButtonMap = new GridButton[ROW_NUMBER][COLUMN_NUMBER];
	
	
	
	// constructor
	public GridFrame(Graphics g, Screen screen) {
		
		gameFrameGridWidth = (screen.frame.getWidth() - 2.0 * GameFrame.FRAME_BORDER_WIDTH) / GameFrame.GRID_COL_NUMBER_IN_SCREEN;
		gameFrameGridHeight = (screen.frame.getHeight() - 2.0 * GameFrame.FRAME_BORDER_HEIGHT) / GameFrame.GRID_ROW_NUMBER_IN_SCREEN;	
		
		gameFrameWidth = GameFrame.FRAME_COL_NUMBER_OCCUPIED * gameFrameGridWidth;
		gameFrameHeight = GameFrame.FRAME_ROW_NUMBER_OCCUPIED * gameFrameGridHeight;
		
		gridWidth = gameFrameWidth / gridColNumberInScreen;
		gridHeight = gameFrameHeight / gridRowNumberInScreen;
		
		worldMapFile = new WorldMapFile();
		
		for (int i = 0; i < 10; i++)
			if (getClass().getResource("/edu/nus/comp/dotagrid/res/WorldMap/terrian" + "/terrian" + i + ".png") != null) {
				terrain[i] = new ImageIcon(getClass().getResource("/edu/nus/comp/dotagrid/res/WorldMap/terrian" + "/terrian" + i + ".png")).getImage();
			} else
				terrain[i] = new ImageIcon().getImage();
		
		this.worldMap = worldMapFile.getWorldMap();
		map = this.worldMap.map;
		
		displayGridOnScreen(g);
		
		initializeAllMapsOnScreen();
		
		displayGridButtonsOnScreen(g);
	}

	private void initializeAllMapsOnScreen() {
		// initialize grid buttons that can be displayed on screen
		for (int x=0; x<ROW_NUMBER; x++) {
			for (int y=0; y<COLUMN_NUMBER; y++) { 
				gridButtonMap[x][y] = new GridButton(map[x][y]);
				// initialize all maps
				highlightedMap[x][y] = -1;
				attackRangeMap[x][y] = -1;
				sightMap[x][y] = 1;
				// initialize tree map
				if (map[x][y] == 4) {
					treeMap[x][y] = true;
				}
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
	
	private void displayGridButtonsOnScreen(Graphics g) {
		
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) { 
				
				if (gridButtonMap[x + currentGridXPos][y + currentGridYPos].getCharacter() != null
						&& sightMap[x + currentGridXPos][y + currentGridYPos] != -1) {
					g.drawImage(gridButtonMap[x + currentGridXPos][y + currentGridYPos].getCharacter().getCharacterImage(),
							(int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
							(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
							(int) gridHeight, null);
				}

			}
		}
		
	}
	
	
	// display all highlighted grid on screen if there is any
	private void displayHighlightGrid(Graphics g) {
		
		// prepare to draw transparent grids
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f); 
		g2d.setComposite(ac);
		
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) {
				
				// condition to highlight a button
				if (highlightedMap[x + currentGridXPos][y + currentGridYPos] == 1){
					g2d.setColor(Color.BLACK);
					g2d.fillRect((int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
							(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
							(int) gridHeight);	
				}
				
				// condition to highlight a button within attackable range
				if (attackRangeMap[x + currentGridXPos][y + currentGridYPos] == 1){
					g2d.setColor(Color.BLUE);
					g2d.setStroke(new BasicStroke(10));
					g2d.drawRect((int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
							(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
							(int) gridHeight);
				}				
				
			}
		}
		
		// disable g2d
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); 
		g2d.dispose();
	}

	
	public static void updateSightMap() {
		// reset sight map first
		for (int x=0; x<ROW_NUMBER-1; x++) {
			for (int y=0; y<COLUMN_NUMBER-1; y++) {
				sightMap[x][y] = -1;
			}
		}
		
		// update sight map
		for (int x=0; x<ROW_NUMBER-1; x++) {
			for (int y=0; y<COLUMN_NUMBER-1; y++) {
						
				// only occupied grid has character
				if (gridButtonMap[x][y].getCharacter() != null) {
					// only friendly character's sight can be shared
					if (gridButtonMap[x][y].getCharacter().getTeamNumber() == Player.getTeamNumber()){
						if (gridButtonMap[x][y].getIsPlayer())
							System.out.println("update sight map!");
						// update selected character's sight
						new CharacterActions(3, -1, -1, x, y);
					}
				}					
			}
		}	
	}
	
	
	// display sight map
	private void displaySightMap(Graphics g) {
		// prepare to draw transparent grids
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f); 
		g2d.setComposite(ac);
		
		// cover up everything not within friendly units' sight with color black
		g2d.setColor(Color.BLACK);
		
		// display sight map
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) { 
				if (sightMap[x + currentGridXPos][y + currentGridYPos] == -1) {					
					g2d.fillRect((int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
							(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
							(int) gridHeight);
				}
			}
		}

	}
	

	private void repaintRect(Graphics g) {		
		// repaint frames for each rectangle (just to look nicer)
		for (int x=0; x<gridColNumberInScreen; x++) {
			for (int y=0; y<gridRowNumberInScreen; y++) { 
				g.setColor(Color.GRAY);
				g.drawRect((int)(GameFrame.FRAME_BORDER_WIDTH + x * gridWidth),
						(int)(GameFrame.FRAME_BORDER_HEIGHT + y * gridHeight), (int) gridWidth,
						(int) gridHeight);
			}
		}
		
	}
	
	
	// method to update grid frame
	public void updateGridFrame(Graphics g){
		gridWidth = GameFrame.FRAME_COL_NUMBER_OCCUPIED * gameFrameGridWidth / gridColNumberInScreen;
		gridHeight = GameFrame.FRAME_ROW_NUMBER_OCCUPIED * gameFrameGridHeight / gridRowNumberInScreen;
		
		displayGridOnScreen(g);
		displayGridButtonsOnScreen(g);	
		displaySightMap(g);
		repaintRect(g);
		displayHighlightGrid(g);
		
	}


	// method to check and invoke grid frame event
	public static void invokeLeftClickEvent(int handXPos, int handYPos){
		MouseHandler.isClicked = true;
		setSelectedXCoodinatePos(handXPos);
		setSelectedYCoodinatePos(handYPos);
		
		boolean isWithinGridFrame = (handXPos > GameFrame.FRAME_BORDER_WIDTH && handXPos < (GameFrame.FRAME_BORDER_WIDTH + gameFrameWidth)
				&& handYPos > GameFrame.FRAME_BORDER_HEIGHT && handYPos < (GameFrame.FRAME_BORDER_HEIGHT + gameFrameHeight));
		
		// check if the selected position is within grid frame
		if (isWithinGridFrame || (ItemShop.shouldUpdateItemInFo == true)) {	
			// reset all character info!
			resetAllCharacterInfo();			
			
			if (popupJFrame == false) {
				// set the coordinates for the selected position
				previouslySelectedXPos = selectedXPos;
				previouslySelectedYPos = selectedYPos;			
				selectedXPos = currentGridXPos + (int) ((handXPos - GameFrame.FRAME_BORDER_WIDTH) / gridWidth);
				selectedYPos = currentGridYPos + (int) ((handYPos - GameFrame.FRAME_BORDER_HEIGHT) / gridHeight);
			}
			
			gridButtonMap[selectedXPos][selectedYPos].actionPerformed();
		}		

		GridFrame.updateSightMap();
	}
	
	
	public static void resetAllCharacterInfo() {
		System.out.println("reset all character information!");
		// clear all previously highlighted grids and prepare for new round of highlighting XD
		// clear sight map for further refresh of information
		for (int x=0; x<ROW_NUMBER; x++) {
			for (int y=0; y<COLUMN_NUMBER; y++) { 
				highlightedMap[x][y] = -1;
				attackRangeMap[x][y] = -1;
			}
		}	
				
		// reset all character images and displayed strings, number 25 is the last skill's index
		for (int i=0; i<25; i++) {
			GameFrame.allCharacterInfoGameButtons.get(i).setIsReadyToDrawImage(false);
			GameFrame.allCharacterInfoGameButtons.get(i).setString("");
		}
	}

	// method to get and change the selected highlighted position
	public static int getSelectedXPos(){
		return selectedXPos;
	}
	
	public static void setSelectedXPos(int changedSelectedXPos){
		selectedXPos = changedSelectedXPos;
	}
	
	public static int getSelectedYPos(){
		return selectedYPos;
	}
	
	public static void setSelectedYPos(int changedSelectedYPos){
		selectedYPos = changedSelectedYPos;
	}
	
	public static int getPreviouslySelectedXPos(){
		return previouslySelectedXPos;
	}
	
	public static void setPreviouslySelectedXPos(int changedSelectedXPos){
		previouslySelectedXPos = changedSelectedXPos;
	}
	
	public static int getPreviouslySelectedYPos(){
		return previouslySelectedYPos;
	}
	
	public static void setPreviouslySelectedYPos(int changedSelectedYPos){
		previouslySelectedYPos = changedSelectedYPos;
	}
	
	
	
	/* 
	 * Methods to change the display size of the game grid frame
	 * 
	 */
	
	
	public static int getGridRowNumberInScreen(){
		return gridRowNumberInScreen;
	}
	
	public static void setGridRowNumberInScreen(int changedGridRowNumberInScreen){
		if (changedGridRowNumberInScreen <= minRowNumberInScreen) {
			gridRowNumberInScreen = minRowNumberInScreen;
		} else if (changedGridRowNumberInScreen >= ROW_NUMBER) {
			gridRowNumberInScreen = ROW_NUMBER;
		} else {
			gridRowNumberInScreen = changedGridRowNumberInScreen;
		}
	}
	
	public static int getGridColNumberInScreen(){
		return gridColNumberInScreen;
	}
	
	public static void setGridColNumberInScreen(int changedGridColNumberInScreen){
		if (changedGridColNumberInScreen <= minColNumberInScreen) {
			gridColNumberInScreen = minColNumberInScreen;
		} else if (changedGridColNumberInScreen >= COLUMN_NUMBER) {
			gridColNumberInScreen = COLUMN_NUMBER;
		} else {
			gridColNumberInScreen = changedGridColNumberInScreen;
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

	public static int getSelectedXCoodinatePos() {
		return selectedXCoodinatePos;
	}

	public static void setSelectedXCoodinatePos(int selectedXCoodinatePos) {
		GridFrame.selectedXCoodinatePos = selectedXCoodinatePos;
	}

	public static int getSelectedYCoodinatePos() {
		return selectedYCoodinatePos;
	}

	public static void setSelectedYCoodinatePos(int selectedYCoodinatePos) {
		GridFrame.selectedYCoodinatePos = selectedYCoodinatePos;
	}
	

	public static double getGridWidth() {
		return gridWidth;
	}

	public static void setGridWidth(double gridWidth) {
		GridFrame.gridWidth = gridWidth;
	}

	public static double getGridHeight() {
		return gridHeight;
	}

	public static void setGridHeight(double gridHeight) {
		GridFrame.gridHeight = gridHeight;
	}

}
