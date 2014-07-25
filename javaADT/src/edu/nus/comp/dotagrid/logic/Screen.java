package edu.nus.comp.dotagrid.logic;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {

	Thread thread = new Thread(this);

	Frame frame;
	public static User user;
	

	private int fps = 0;

	// control the states of the game
	public int scene;

	private boolean running = false;
	private boolean isFrameInitialized = false;
	
	private GridFrame newGameGridFrame;
	private GameFrame newGameFrame;


	// constructor
	public Screen(Frame frame) {
		this.frame = frame;
		
		this.frame.addKeyListener(new KeyHandler(this));
		this.frame.addMouseListener(new MouseHandler());
			
		thread.start();
	}
	

	public void paintComponent(Graphics g) {
		
		g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
		super.paintComponent(g);
		
		if (scene == 0) {
			
			// load game
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
			
			g.drawImage(new ImageIcon(getClass().getResource("/edu/nus/comp/dotagrid/res/Loading Image/" + "Furion" + ".jpg")).getImage(), 0, 0, this.frame.getWidth(), this.frame.getHeight(), null);
	
			new BuildingDatabase();
			new SkillDatabase();
			new NeutralCreepDatabase();

		} else if (scene == 1) {
			// start game!				
			g.setColor(Color.BLACK);	
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

			if (isFrameInitialized == false) {	
				System.out.println("START THE GAME!!!!!!!!!");			
				// draw game frame
				newGameFrame = new GameFrame(g, frame);
				
				// draw game grid 
				newGameGridFrame = new GridFrame(g, this);

				// initialize towers
				TowerDatabase.initializeAllTowers();
				BuildingDatabase.initializeAllBuildings();

				new HeroDatabase();
				
				// initialize user
				user = new User();
				
				// select player's hero's grid button
				GridFrame.invokeLeftClickEvent((int)((0.5 + user.playerStartingXPos - GridFrame.getCurrentGridXPos()) * GridFrame.getGridWidth() + GameFrame.FRAME_BORDER_HEIGHT), 
						(int)((0.5 + user.playerStartingYPos - GridFrame.getCurrentGridYPos()) * GridFrame.getGridHeight() + GameFrame.FRAME_BORDER_WIDTH));
				
				isFrameInitialized = true;
			} else {
				newGameFrame.updateGameFrame(g);
				newGameGridFrame.updateGridFrame(g);
			}

		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
		}

		// FPS at the bottom -> paint then clear the rectangle
		g.drawString(fps + "", 10, 10);

	} // end override method paintComponent


	// first time
	public void loadGame() {
		scene = 0; // sets scene to main menu
		running = true;
	}

	public void startGame() {
		this.scene = 1; // enter game!
		MouseHandler.isClicked = true; // repaint
	}

	@Override
	public void run() {
		long lastFrame = System.currentTimeMillis();
		int frames = 0;

		loadGame();
		
		// game loop
		while (running) {
			if (MouseHandler.isClicked) {
				repaint();
				MouseHandler.isClicked = false;
			} else {
				frames++;

				if (System.currentTimeMillis() - 1000 >= lastFrame) {
					// has passed at least 1 second
					fps = frames;
					frames = 0;
					lastFrame = System.currentTimeMillis();
				}

				try {
					// to control the repaint rate
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		System.exit(0);
	} // end method run
	
	public class KeyTyped {
		public void keyESC() {
			MainMenu.confirmExit();
		}

		public void keySPACE() {
			startGame();
		}
	} // end inner class KeyTyped

}
