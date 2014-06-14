package edu.nus.comp.dotagrid.logic;

public class Player {
	private Hero hero;
	private int XPos, YPos;

	// constructor
	public Player(int XPos, int YPos, Hero hero){
		this.hero = new Hero(hero);
		this.XPos = XPos;
		this.YPos = YPos;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}
	
	public int getXPos() {
		return XPos;
	}

	public void setXPos(int xPos) {
		XPos = xPos;
	}

	public int getYPos() {
		return YPos;
	}

	public void setYPos(int yPos) {
		YPos = yPos;
	}

}
