package edu.nus.comp.dotagrid.logic;

import java.util.*;



public class Hero extends Character{
	
	private String mainAttribute;
	private double totalMainAttrubute;
	
	private int startingStrength, startingAgility, startingIntelligence;
	private double strengthGrowth, agilityGrowth, intelligenceGrowth;
	private double totalStrength, totalAgility, totalIntelligence;
	
	private double HPGainPerRound, MPGainPerRound;
	
	private int movementSpeed;
	
	private int money;
	private int level;
	private int experience;

	private ArrayList<Skill> skills;
	private ArrayList<Item> items;
	
	private int kill, death, assist;
	
	// define a list of constants for calculation
	public final int STRENGTH_ADD_HP_RATIO = 19;
	public final int INTELLIGENCE_ADD_MP_RATIO = 13;
	
	public final double STRENGTH_ADD_HP_PER_ROUND = 3.0;
	public final double INTELLIGENCE_ADD_MP_PER_ROUND = 4.0;
	public final double AGILITY_ADD_PHYSICAL_DEFENCE_RATIO = 1 / 7.0;
	
	public final double AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO = 1.0;
	public final double MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO = 1.0;

	public final int MAX_MOVEMENT_SPEED = 522;
	
	
	// constructor
	public Hero(String heroName, String mainAttribute, int startingHP, int startingMP, 
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefense, double startingMagicResistance, int actionPoint,
			int startingStrength, int startingAgility, int startingIntelligence, 
			double strengthGrowth, double agilityGrowth, double intelligenceGrowth, int movementSpeed) {
		
		super(heroName, startingHP, startingMP, startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, 
				startingPhysicalDefense, startingMagicResistance, actionPoint);
		
		// initialize attributes specific to heros
		this.setMainAttribute(mainAttribute);
		
		this.setStartingStrength(startingStrength);
		this.setStartingAgility(startingAgility);
		this.setStartingIntelligence(startingIntelligence);

		this.setStrengthGrowth(strengthGrowth);
		this.setAgilityGrowth(agilityGrowth);
		this.setIntelligenceGrowth(intelligenceGrowth);	
		
		this.setStartingHP(startingHP + startingStrength * STRENGTH_ADD_HP_RATIO);
		this.setStartingMP(startingMP + startingIntelligence * INTELLIGENCE_ADD_MP_RATIO);
		
		this.setLevel(0);
		this.setExperience(0);
		
		this.setTotalStrength(this.getStartingStrength() + this.getLevel() * this.getStrengthGrowth());
		this.setTotalAgility(this.getStartingAgility() + this.getLevel() * this.getAgilityGrowth());
		this.setTotalIntelligence(this.getStartingIntelligence() + this.getLevel() * this.getIntelligenceGrowth());
		
		this.setTotalMainAttrubute();
		
		this.setmaxHP((int) (this.getStartingHP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO));
		this.setmaxMP((int) (this.getStartingMP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO));
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());
		
		this.setHPGainPerRound(this.getTotalStrength() * STRENGTH_ADD_HP_PER_ROUND);
		this.setMPGainPerRound(this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND);
		
		this.setTotalPhysicalDefense(this.getStartingPhysicalDefense() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO);		
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO);
		
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack() + this.getTotalMainAttrubute() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO);
		
	}
	
	
	
	
	// accessor and mutator for three primary attributes: strength, agility, intelligence

	public String getMainAttribute() {
		return mainAttribute;
	}

	public void setMainAttribute(String mainAttribute) {
		if (mainAttribute.equalsIgnoreCase("strength") || 
			mainAttribute.equalsIgnoreCase("agility") || 
			mainAttribute.equalsIgnoreCase("intelligence")) {
			
			this.mainAttribute = mainAttribute;
		} else {
			System.out.println("Invalid Main Attribute Type!");
		}		
	}
	
	public double getTotalMainAttrubute() {
		return totalMainAttrubute;
	}


	public void setTotalMainAttrubute() {
		if (this.getMainAttribute().equalsIgnoreCase("strength")) {
			this.totalMainAttrubute = this.getTotalStrength();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("agility")){
			this.totalMainAttrubute = this.getTotalAgility();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("intelligence")){
			this.totalMainAttrubute = this.getTotalIntelligence();
			
		} else {
			System.out.println("Error occurs when trying to set total main attribute!");
		}
	}

	
	public int getStartingStrength() {
		return startingStrength;
	}


	public void setStartingStrength(int startingStrength) {
		// minimum value is 0
		if (startingStrength <= 0) {
			System.out.println("startingStrength must be positive!");
		} else {
			this.startingStrength = startingStrength;
		}
	}


	public int getStartingAgility() {
		return startingAgility;
	}


	public void setStartingAgility(int startingAgility) {
		// minimum value is 0
		if (startingAgility <= 0) {
			System.out.println("startingAgility must be positive!");
		} else {
			this.startingAgility = startingAgility;
		}
	}


	public int getStartingIntelligence() {
		return startingIntelligence;
	}


	public void setStartingIntelligence(int startingIntelligence) {
		// minimum value is 0
		if (startingIntelligence <= 0) {
			System.out.println("startingIntelligence must be positive!");
		} else {
			this.startingIntelligence = startingIntelligence;
		}
	}


	public double getStrengthGrowth() {
		return strengthGrowth;
	}


	public void setStrengthGrowth(double strengthGrowth) {
		// minimum value is 0
		if (strengthGrowth <= 0) {
			System.out.println("strengthGrowth must be positive!");
		} else {
			this.strengthGrowth = strengthGrowth;
		}
	}


	public double getAgilityGrowth() {
		return agilityGrowth;
	}


	public void setAgilityGrowth(double agilityGrowth) {
		// minimum value is 0
		if (agilityGrowth <= 0) {
			System.out.println("agilityGrowth must be positive!");
		} else {
			this.agilityGrowth = agilityGrowth;
		}
	}


	public double getIntelligenceGrowth() {
		return intelligenceGrowth;
	}


	public void setIntelligenceGrowth(double intelligenceGrowth) {
		// minimum value is 0
		if (intelligenceGrowth <= 0) {
			System.out.println("intelligenceGrowth must be positive!");
		} else {
			this.intelligenceGrowth = intelligenceGrowth;
		}
	}


	public double getTotalStrength() {
		return totalStrength;
	}


	public void setTotalStrength(double totalStrength) {
		// minimum value is startingStrength
		if (totalStrength <= startingStrength){
			this.totalStrength = startingStrength;
		} else {
			this.totalStrength = totalStrength;
		}
	}


	public double getTotalAgility() {
		return totalAgility;
	}


	public void setTotalAgility(double totalAgility) {
		// minimum value is startingAgility
		if (totalAgility <= startingAgility){
			this.totalAgility = startingAgility;
		} else {
			this.totalAgility = totalAgility;
		}
	}


	public double getTotalIntelligence() {
		return totalIntelligence;
	}


	public void setTotalIntelligence(double totalIntelligence) {
		// minimum value is startingIntelligence
		if (totalIntelligence <= startingIntelligence){
			this.totalIntelligence = startingIntelligence;
		} else {
			this.totalIntelligence = totalIntelligence;
		}
	}


	// accessor and mutator for HP and MP Gain Per Round
	
	public double getHPGainPerRound() {
		return HPGainPerRound;
	}


	public void setHPGainPerRound(double HPGainPerRound) {
		// there is no upper/lower boundary for HPGainPerRound
		this.HPGainPerRound = HPGainPerRound;
	}


	public double getMPGainPerRound() {
		return MPGainPerRound;
	}


	public void setMPGainPerRound(double MPGainPerRound) {
		this.MPGainPerRound = MPGainPerRound;
	}


	
	// accessor and mutator for movement speed
	
	public int getMovementSpeed() {
		return movementSpeed;
	}


	public void setMovementSpeed(int movementSpeed) {
		// cannot be higher than maximum movement speed
		if (movementSpeed >= MAX_MOVEMENT_SPEED){
			this.movementSpeed = MAX_MOVEMENT_SPEED;
		} else {
			this.movementSpeed = movementSpeed;
		}
	}



	// accessor and mutator for level and experience

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}




	public int getKill() {
		return kill;
	}




	public void setKill(int kill) {
		this.kill = kill;
	}




	public int getDeath() {
		return death;
	}




	public void setDeath(int death) {
		this.death = death;
	}




	public int getAssist() {
		return assist;
	}




	public void setAssist(int assist) {
		this.assist = assist;
	}




	public int getMoney() {
		return money;
	}




	public void setMoney(int money) {
		this.money = money;
	}





}
