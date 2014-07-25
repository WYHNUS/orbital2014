package edu.nus.comp.dotagrid.logic;


public class HeroDatabase {
	
	public static int totalHeroNumber = 1;
	
	public static Hero[] heroDatabase = new Hero[totalHeroNumber];
	
	public HeroDatabase(){
		/* 
		 * String heroName, 
		 * int BountyMoney
		 * int bountyExp
		 * int sight
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
		heroDatabase[0] = getHeroFurion();
	}
	
	public static Hero getHeroFurion(){
		Hero furion = new Hero("furion", "intelligence", 
				150, 10, 31, 3, 0.7, 3.52, 20, 100, 1,
				19, 18, 21, 1.8, 1.9, 2.9, 295);	
		furion.setCharacterImage("Heroes", "furion");
		furion.addSkill(SkillDatabase.getSkillSprout());
		furion.addSkill(SkillDatabase.getSkillTeleportation());
		furion.addSkill(SkillDatabase.getSkillNatureCall());
		furion.addSkill(SkillDatabase.getSkillWrathOfNature());
		furion.addSkill(SkillDatabase.getAttributeBonus());
		
		return furion;
	}

}
