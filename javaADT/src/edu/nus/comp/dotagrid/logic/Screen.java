package edu.nus.comp.dotagrid.logic;

import java.awt.*;
import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {

	Thread thread = new Thread(this);

	Frame frame;
	User user;
	

	private int fps = 0;

	// control the states of the game
	public int scene;

	private boolean running = false;


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
			
			g.setColor(Color.BLACK);		

		} else if (scene == 1) {
			// start game!	
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
			
			// draw game frame
			new GameFrame(g, frame);
			
			// game grid 
			new GridFrame(g, this);

		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
		}

		// FPS at the bottom -> paint then clear the rectangle
		g.drawString(fps + "", 10, 10);

	} // end override method paintComponent


	// first time
	public void loadGame() {
		user = new User(this);

		running = true;
	}

	public void startGame(User user) {
		user.createPlayer();

		this.scene = 1; // enter game!
	}

	@Override
	public void run() {
		long lastFrame = System.currentTimeMillis();
		int frames = 0;

		loadGame();

		// game loop
		while (running) {
			repaint();

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

		System.exit(0);
	} // end method run
	
	public class KeyTyped {
		public void keyESC() {
			running = false;
		}

		public void keySPACE() {
			startGame(user);
		}
	} // end inner class KeyTyped

}
