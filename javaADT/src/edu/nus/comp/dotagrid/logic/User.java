package edu.nus.comp.dotagrid.logic;

public class User {
	private Screen screen;
	
	Player player;
	
	public User(Screen screen) {
		this.screen = screen;
		this.screen.scene = 0; // sets scene to main menu
	}
	
	public void createPlayer(Hero hero){
		this.player = new Player(hero);
	}

}

