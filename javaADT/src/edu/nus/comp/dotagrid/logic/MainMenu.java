package edu.nus.comp.dotagrid.logic;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainMenu {
	
	public Component createComponents(){

		final JButton start = new JButton("START A NEW GAME");
		final JButton help = new JButton("HELP");
		final JButton acknowledgement = new JButton("ACKNOWLEDGEMENT");
		final JButton exit = new JButton("EXIT");

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startGameMenu();
			}
		});

		help.addActionListener(new OpenURLAction("http://wangyanhao1993.wix.com/nus-c-dota#!game-help/c1p9k"));

		acknowledgement.addActionListener(new OpenURLAction("http://wangyanhao1993.wix.com/nus-c-dota#!acknowledgement/c18wc"));
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirmExit();
			}
		});

		
		JPanel pane = new JPanel();
		
		pane.setLayout(new GridLayout(4, 1));
		
		pane.add(start);
		pane.add(help);
		pane.add(acknowledgement);
		pane.add(exit);

		return pane;
	}

	protected void startGameMenu() {
		new Frame();
	}

	
	public static void confirmExit() {
		final JFrame frame = new JFrame("Exit");
		final JLabel label = new JLabel("Are You Sure To Exit ?", JLabel.CENTER);
		JButton yesButton = new JButton("YES");
		JButton noButton = new JButton("NO");
		
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		
		frame.setLayout(new BorderLayout());
		frame.add("Center", label);
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));
		
		pane.add(yesButton);
		pane.add(noButton);
		
		frame.add("South", pane);
		
		frame.pack();
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("C-DOTA");
		
		MainMenu app = new MainMenu();
		Component contents = app.createComponents();
		
		frame.getContentPane().add(contents);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		frame.pack();
		frame.setSize(500, 700);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
