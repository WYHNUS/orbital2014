package edu.nus.comp.dotagrid.logic;

import javax.swing.JFrame;

public class Frame extends JFrame{
	
	public Frame() {
		new JFrame();
		
		this.setTitle("C-DOTA");
		// shut down the program when closing the window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setVisible(true);
		
		Screen screen = new Screen(this);
		// add screen to JFrame
		this.add(screen);
	}
	
	public static void main(String[] args) {
		new Frame();
	}
	
}
