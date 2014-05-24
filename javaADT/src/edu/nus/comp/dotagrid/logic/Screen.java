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

	public boolean running = false;

	// constructor
	public Screen(Frame frame) {
		this.frame = frame;

		this.frame.addKeyListener(new KeyHandler(this));
		this.frame.addMouseListener(new MouseHandler(this));

		thread.start();
	}

	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

		if (scene == 0) {
			// main menu
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

			Button start = new Button("START A NEW GAME");
			Button help = new Button("HELP");
			Button acknowledgement = new Button("ACKNOWLEDGEMENT");

			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					scene = 1;
				}
			});

			help.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					scene = 2;
				}
			});

			acknowledgement.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					scene = 3;
				}
			});

			frame.add(start);
			frame.add(help);
			frame.add(acknowledgement);

		} else if (scene == 1) {
			// game menu
			// background
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

		} else if (scene == 2) {
			// help list

		} else if (scene == 3) {
			// acknowledgement list

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

		this.scene = 1; // level 1
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

			if (hand != 0) {
				// place tower
				hand = 0;
			}

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
