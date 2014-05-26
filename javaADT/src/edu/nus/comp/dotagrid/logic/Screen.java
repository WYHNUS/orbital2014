package edu.nus.comp.dotagrid.logic;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {

	Thread thread = new Thread(this);

	Frame frame;
	User user;

	private int fps = 0;

	// control the states of the game
	public int scene;

	public int hand = 0;
	public int handXPos = 0;
	public int handYPos = 0;
	
	public double gridWidth = 1.0;
	public double gridHeight = 1.0;
	
	public static final int gridRowNumberInScreen = 20;
	public static final int gridColNumberInScreen = 14;

	public boolean running = false;

	// constructor
	public Screen(Frame frame) {
		this.frame = frame;

		this.frame.addKeyListener(new KeyHandler(this));
		this.frame.addMouseListener(new MouseHandler(this));
		
		gridWidth = this.frame.getWidth() / ((1440 - 250) / 1100.0) / gridRowNumberInScreen;
		gridHeight = this.frame.getHeight() / (900 / 700.0) / gridColNumberInScreen;

		thread.start();
	}

	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

		if (scene == 0) {
			// load game
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

		} else if (scene == 1) {
			// start game
			new WorldMap(g, this.frame);

		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
		}

		// FPS at the bottom -> paint then clear the rectangle
		g.drawString(fps + "", 10, 10);

	} // end method paintComponent

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

	public class MouseHeld {
		boolean mouseDown = false;

		public void mouseMoved(MouseEvent e) {
			handXPos = e.getXOnScreen();
			handYPos = e.getYOnScreen();
		}

		public void updateMouse(MouseEvent e) {
			if (scene == 1) {
				if (mouseDown && hand == 0) {

				}
			}
		}

		public void mouseDown(MouseEvent e) {
			mouseDown = true;

			if (false) {}

			updateMouse(e);
		}
	} // end inner class MouseHeld

	
	public class KeyTyped {
		public void keyESC() {
			running = false;
		}

		public void keySPACE() {
			startGame(user);
		}
	} // end inner class KeyTyped

}
