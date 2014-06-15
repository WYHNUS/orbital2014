package edu.nus.comp.dotagrid.logic;


public class HeroDatabase {
	
	public static int totalHeroNumber = 1;
	public static int heroStartingBountyMoney = 250;
	public static int heroStartingBountyExp = 100;
	public static int heroStartingMoney = 875;
	
	public Hero[] heroDatabase = new Hero[totalHeroNumber];
	
	public HeroDatabase(){
		
		/* 
		 * String heroName, 
		 * int BountyMoney
		 * int bountyExp
		 * 
		 * int heroStartingMoney
		 * String mainAttribute
		 * 
		 * int startingHP, 
		 * int startingMP, 
		 * int startingPhysicalAttack, 
		 * int startingPhysicalAttackArea, 
		 * double startingPhysicalAttackSpeed, 
		 * double startingPhysicalDefense, 
		 * double startingMagicResistance, 
		 * int actionPoint,
		 * int teamNumber
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
		
		Hero fur = new Hero("fur", heroStartingBountyMoney, heroStartingBountyExp, heroStartingMoney, "intelligence", 
							150, 10, 30000, 3, 0.7, 3.52, 20, 100, 1,
							19, 18, 21, 1.8, 1.9, 2.9, 295);	
		fur.setCharacterImage("heroes", "fur");
		heroDatabase[0] = fur;
		
	}

}
