package edu.nus.comp.dotagrid.logic;

public class Player {
	private Hero hero;
	private int money;
	
	public Player(int money, Hero hero){
		this.hero = new Hero(hero);
		this.money = money;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		if (money <= 0) {
			System.out.println("player's money cannot be less than 0!");
		} else {
			this.money = money;
		}
	}
}
