package edu.nus.comp.dotagrid.logic;

public class User {
	private Screen screen;
	
	Player player;
	
	int startingMoney = 300;
	int startingHealth = 100;
	
	public User(Screen screen) {
		this.screen = screen;
		this.screen.scene = 0; // sets scene to main menu
	}
	
	public void createPlayer(){
		this.player = new Player(this);
	}

}

