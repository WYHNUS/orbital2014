package edu.nus.comp.dotagrid.logic;

public class User {
	private Screen screen;
	public int startingMoney = 853;
	
	public Player player;
	
	public User(Screen screen) {
		this.screen = screen;
		this.screen.scene = 0; // sets scene to main menu
	}
	
	public void createPlayer(Hero hero){
		this.player = new Player(startingMoney, hero);
	}

}

