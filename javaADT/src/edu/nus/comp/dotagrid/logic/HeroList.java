package edu.nus.comp.dotagrid.logic;

public class HeroList {
	
	public static int totalHeroNumber = 1;
	
	public static Hero[] heroList = new Hero[totalHeroNumber];
	
	public HeroList(){
		Hero fur = new Hero("fur", 150, 10, 10, 1, 10, 2, 20, 10);	
		heroList[0] = fur;
		
	}

}
