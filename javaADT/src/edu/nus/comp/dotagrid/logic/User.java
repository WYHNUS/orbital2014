package edu.nus.comp.dotagrid.logic;

public class User {
	private Screen screen;
	public int playerStartingXPos = 7;
	public int playerStartingYPos = 91;
	
	public Player player;
	
	public User(Screen screen) {
		this.screen = screen;
		this.screen.scene = 0; // sets scene to main menu
	}
	
	public void createPlayer(){
		this.player = new Player(playerStartingXPos, playerStartingYPos);
	}

}

