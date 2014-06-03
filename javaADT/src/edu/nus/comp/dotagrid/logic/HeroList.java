package edu.nus.comp.dotagrid.logic;

public class HeroList {
	
	public static int totalHeroNumber = 1;
	
	public static Hero[] heroList = new Hero[totalHeroNumber];
	
	public HeroList(){
		
		/* 
		 * String heroName, 
		 * int startingHP, 
		 * int startingMP, 
		 * int startingPhysicalAttack, 
		 * int startingPhysicalAttackArea, 
		 * double startingPhysicalAttackSpeed, 
		 * double startingPhysicalDefense, 
		 * double startingMagicResistance, 
		 * int actionPoint,
		 * 
		 * int strength, 
		 * int agility, 
		 * int intelligence, 
		 * double strengthGrowth, 
		 * double agilityGrowth, 
		 * double intelligenceGrowth,
		 * int movementSpeed
		 * 
		 * */
		
		Hero fur = new Hero("fur", "intelligence", 150, 10, 10, 3, 1.7, 3.52, 20, 100,
							19, 18, 21, 1.8, 1.9, 2.9, 295);	
		heroList[0] = fur;
		
	}

}
