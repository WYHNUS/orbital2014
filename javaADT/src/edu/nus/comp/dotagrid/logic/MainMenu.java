package edu.nus.comp.dotagrid.logic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				helpList();
			}
		});

		acknowledgement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				acknowledgementList();
			}
		});
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirmExit();
			}
		});

		
		JPanel pane = new JPanel();
		
		pane.add(start);
		pane.add(help);
		pane.add(acknowledgement);
		pane.add(exit);

		return pane;
	}

	protected void startGameMenu() {
		new Frame();
	}

	protected void helpList() {
		// TODO Auto-generated method stub
		
	}

	protected void acknowledgementList() {
		// TODO Auto-generated method stub
		
	}
	
	protected void confirmExit() {
		JFrame frame = new JFrame("Exit");
		final JLabel label = new JLabel("Are You Sure To Exit ?");
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
				
			}
		});
		
		frame.setLayout(new BorderLayout());
		frame.add("Center", label);
		frame.add("North", yesButton);
		frame.add("South", noButton);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		frame.pack();
		frame.setSize(200, 400);
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
		frame.setVisible(true);
	}
}