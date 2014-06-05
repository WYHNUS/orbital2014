package edu.nus.comp.dotagrid.logic;

public class Player {
	private Hero hero;
	
	public Player(Hero hero){
		this.hero = new Hero(hero);
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}
}
