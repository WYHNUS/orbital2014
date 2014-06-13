package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;

import android.R.string;

public class Hero extends Character{
	
	private String mainAttribute;
	private double basicMainAttribute;
	private double totalMainAttribute;
	
	
	// when display in the game frame, the format will be <basic attribute> + " + " + <attributes obtained from items>
	
	// starting attribute is given when constructing the hero
	private double startingStrength, startingAgility, startingIntelligence;
	private double strengthGrowth, agilityGrowth, intelligenceGrowth;
	
	// basic attribute is calculated by taking  (startingAttributes + growth * level + attributes obtained from skills)
	private double basicStrength, basicAgility, basicIntelligence;
	
	// total attribute is calculated by taking basic attribute + attributes obtained from items
	private double totalStrength, totalAgility, totalIntelligence;
	
	private double HPGainPerRound, MPGainPerRound;

	private int level;
	private int experience;

	public Skill[] skills;
	
	// items and total attributes added from items
	public Map<String,Item> items = new ConcurrentHashMap<>();
	private double totalItemAddStrength, totalItemAddAgility, totalItemAddIntelligence;
	private int totalItemAddHP, totalItemAddMP;
	private double totalItemAddHPGainPerRound, totalItemAddMPGainPerRound;
	private double totalItemAddPhysicalDefence, totalItemAddMagicResistance;
	private double totalItemAddPhysicalAttack, totalItemAddPhysicalAttackSpeed;
	private int totalItemAddPhysicalAttackArea;
	private int totalItemAddMovementSpeed;
	


	private int kill, death, assist;
	
	// define a list of constants for calculation
	public static final int STRENGTH_ADD_HP_RATIO = 19;
	public static final int INTELLIGENCE_ADD_MP_RATIO = 13;
	
	public static final double STRENGTH_ADD_HP_PER_ROUND = 3.0;
	public static final double INTELLIGENCE_ADD_MP_PER_ROUND = 4.0;
	public static final double AGILITY_ADD_PHYSICAL_DEFENCE_RATIO = 1 / 7.0;
	
	public static final double AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO = 1.0;
	public static final double MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO = 1.0;

	public static final int MAX_ITEM_NUMBER = 6;
	public static final int MAX_SKILL_NUMBER = 8;
	public final int MAX_ACTION_NUMBER = 16;
	public final int DIRECTION_NUMBER = 5;
	
	// constructor
	public Hero(String heroName, String mainAttribute, int startingHP, int startingMP, 
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance, int actionPoint,
			int startingStrength, int startingAgility, int startingIntelligence, 
			double strengthGrowth, double agilityGrowth, double intelligenceGrowth, int movementSpeed) 
	{
		
		super(heroName, startingHP, startingMP, startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, 
				startingPhysicalDefence, startingMagicResistance, movementSpeed, actionPoint);
		
		// initialize attributes specific to heros
		this.setMainAttribute(mainAttribute);
	
		this.setLevel(0);
		this.setExperience(0);

		this.setStrengthGrowth(strengthGrowth);
		this.setAgilityGrowth(agilityGrowth);
		this.setIntelligenceGrowth(intelligenceGrowth);	
		
		this.setStartingStrength(startingStrength);
		this.setStartingAgility(startingAgility);
		this.setStartingIntelligence(startingIntelligence);

		this.setStartingHP(startingHP + startingStrength * STRENGTH_ADD_HP_RATIO);
		this.setStartingMP(startingMP + startingIntelligence * INTELLIGENCE_ADD_MP_RATIO);
		
		this.setBasicStrength(this.getStartingStrength());
		this.setBasicAgility(this.getStartingAgility());
		this.setBasicIntelligence(this.getStartingIntelligence());
		
		this.setTotalStrength(this.getStartingStrength());
		this.setTotalAgility(this.getStartingAgility());
		this.setTotalIntelligence(this.getStartingIntelligence());
		
		this.setBasicMainAttribute();
		this.setTotalMainAttribute();
		
		this.setmaxHP((int) (this.getStartingHP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO));
		this.setmaxMP((int) (this.getStartingMP() + this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_RATIO));
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());
		
		this.setHPGainPerRound(this.getTotalStrength() * STRENGTH_ADD_HP_PER_ROUND);
		this.setMPGainPerRound(this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND);

		this.setTotalPhysicalDefence(this.getBasicPhysicalDefence() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO);		
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO);
		
		this.setTotalPhysicalAttack(this.getBasicPhysicalAttack() + this.getTotalMainAttribute() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO);
	}
	


	public Hero(Hero hero) {
		super(hero.getName(), hero.getStartingHP(), hero.getStartingMP(), hero.getStartingPhysicalAttack(), hero.getStartingPhysicalAttackArea(), 
				hero.getStartingPhysicalAttackSpeed(), hero.getStartingPhysicalDefence(), hero.getStartingMagicResistance(), 
				hero.getStartingMovementSpeed(), hero.getActionPoint());
		
		this.setMainAttribute(hero.getMainAttribute());
		
		this.setLevel(hero.getLevel());
		this.setExperience(hero.getExperience());

		this.setStrengthGrowth(hero.getStrengthGrowth());
		this.setAgilityGrowth(hero.getAgilityGrowth());
		this.setIntelligenceGrowth(hero.getIntelligenceGrowth());	
			
		this.setStartingStrength(hero.getStartingStrength());
		this.setStartingAgility(hero.getStartingAgility());
		this.setStartingIntelligence(hero.getStartingIntelligence());
	
		this.setStartingHP(hero.getStartingHP());
		this.setStartingMP(hero.getStartingMP());
		
		this.setBasicStrength(hero.getBasicStrength());
		this.setBasicAgility(hero.getBasicAgility());
		this.setBasicIntelligence(hero.getBasicIntelligence());
		
		this.setTotalStrength(hero.getTotalStrength());
		this.setTotalAgility(hero.getTotalAgility());
		this.setTotalIntelligence(hero.getTotalIntelligence());
		
		this.setBasicMainAttribute();
		this.setTotalMainAttribute();
		
		this.setmaxHP(hero.getmaxHP());
		this.setmaxMP(hero.getmaxMP());
		this.setCurrentHP(hero.getCurrentHP());
		this.setCurrentMP(hero.getCurrentMP());
		
		this.setHPGainPerRound(hero.getHPGainPerRound());
		this.setMPGainPerRound(hero.getMPGainPerRound());
		
		this.setBasicPhysicalDefence(hero.getBasicPhysicalDefence());
		this.setTotalPhysicalDefence(hero.getTotalPhysicalDefence());		
		this.setTotalPhysicalAttackSpeed(hero.getTotalPhysicalAttackSpeed());
		
		this.setBasicPhysicalAttack(hero.getBasicPhysicalAttack());
		this.setTotalPhysicalAttack(hero.getTotalPhysicalAttack());
		
		for (Map.Entry<String, Item> item : hero.items.entrySet())
			items.put(item.getKey(), item.getValue());
		
		/*
		this.skills = new Skill[GameFrame.MAX_SKILL_NUMBER];
		for (int i=0; i<GameFrame.MAX_SKILL_NUMBER; i++){
			if (hero.skills[i] != null){
				this.skills[i] = new Skill(hero.skills[i]);
			}
		}
		*/
	}


	public void updateHeroAttributeInfo(){
		this.setBasicStrength(this.getStartingStrength() + this.getLevel() * this.getStrengthGrowth());
		this.setBasicAgility(this.getStartingAgility() + this.getLevel() * this.getAgilityGrowth());
		this.setBasicIntelligence(this.getStartingIntelligence() + this.getLevel() * this.getIntelligenceGrowth());
		
		this.setTotalStrength(this.getBasicStrength() + this.getTotalItemAddStrength());
		this.setTotalAgility(this.getBasicAgility() + this.getTotalItemAddAgility());
		this.setTotalIntelligence(this.getBasicIntelligence() + this.getTotalItemAddIntelligence());
		
		this.setBasicMainAttribute();
		this.setTotalMainAttribute();
		
		this.setmaxHP((int) (this.getStartingHP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO + this.getTotalItemAddHP()));
		this.setmaxMP((int) (this.getStartingMP() + this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_RATIO + this.getTotalItemAddMP()));
		
		this.setHPGainPerRound(this.getTotalStrength() * STRENGTH_ADD_HP_PER_ROUND + this.getTotalItemAddHPGainPerRound());
		this.setMPGainPerRound(this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND + this.getTotalItemAddMPGainPerRound());
		
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO
				+ this.getTotalItemAddPhysicalAttackSpeed());
		this.setTotalPhysicalAttackArea(this.getStartingPhysicalAttackArea() + this.getTotalItemAddPhysicalAttackArea());
		this.setTotalMagicResistance(this.getStartingMagicResistance() + this.getTotalItemAddMagicResistance());
		this.setTotalMovementSpeed(this.getStartingMovementSpeed() + this.getTotalItemAddMovementSpeed());
		
		this.setBasicPhysicalDefence(this.getStartingPhysicalDefence() + this.getBasicAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO);	
		this.setTotalPhysicalDefence(this.getBasicPhysicalDefence() + this.getTotalItemAddPhysicalDefence());
		
		this.setBasicPhysicalAttack(this.getStartingPhysicalAttack() + this.getBasicMainAttribute() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO);
		this.setTotalPhysicalAttack(this.getBasicPhysicalAttack() + this.getTotalItemAddPhysicalAttack());
	}
	
	
	
	// accessor and mutator for three primary attributes: strength, agility, intelligence

	public String getMainAttribute() {
		return mainAttribute;
	}

	public void setMainAttribute(String mainAttribute) {
		if (mainAttribute.equalsIgnoreCase("strength") || 
			mainAttribute.equalsIgnoreCase("agility") || 
			mainAttribute.equalsIgnoreCase("intelligence")) {
			this.mainAttribute = mainAttribute.toLowerCase(Locale.US);
		} else {
			System.out.println("Invalid Main Attribute Type!");
		}		
	}
	
	public double getBasicMainAttribute() {
		return totalMainAttribute;
	}


	public void setBasicMainAttribute() {
		if (this.getMainAttribute().equalsIgnoreCase("strength")) {
			this.basicMainAttribute = this.getBasicStrength();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("agility")){
			this.basicMainAttribute = this.getBasicAgility();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("intelligence")){
			this.basicMainAttribute = this.getBasicIntelligence();
			
		} else {
			System.out.println("Error occurs when trying to set total main attribute!");
		}
	}
	
	public double getTotalMainAttribute() {
		return totalMainAttribute;
	}


	public void setTotalMainAttribute() {
		if (this.getMainAttribute().equalsIgnoreCase("strength")) {
			this.totalMainAttribute = this.getTotalStrength();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("agility")){
			this.totalMainAttribute = this.getTotalAgility();
			
		} else if (this.getMainAttribute().equalsIgnoreCase("intelligence")){
			this.totalMainAttribute = this.getTotalIntelligence();
			
		} else {
			System.out.println("Error occurs when trying to set total main attribute!");
		}
	}

	
	public double getStartingStrength() {
		return startingStrength;
	}


	public void setStartingStrength(double startingStrength) {
		// minimum value is 0
		if (startingStrength <= 0) {
			System.out.println("startingStrength must be positive!");
		} else {
			this.startingStrength = startingStrength;
		}
	}


	public double getStartingAgility() {
		return startingAgility;
	}


	public void setStartingAgility(double startingAgility) {
		// minimum value is 0
		if (startingAgility <= 0) {
			System.out.println("startingAgility must be positive!");
		} else {
			this.startingAgility = startingAgility;
		}
	}


	public double getStartingIntelligence() {
		return startingIntelligence;
	}


	public void setStartingIntelligence(double startingIntelligence) {
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

	public double getBasicStrength() {
		return basicStrength;
	}


	public void setBasicStrength(double basicStrength) {
		this.basicStrength = basicStrength;
	}


	public double getBasicAgility() {
		return basicAgility;
	}


	public void setBasicAgility(double basicAgility) {
		this.basicAgility = basicAgility;
	}


	public double getBasicIntelligence() {
		return basicIntelligence;
	}


	public void setBasicIntelligence(double basicIntelligence) {
		this.basicIntelligence = basicIntelligence;
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



	// KDA
	
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

	
	// attributes obtained from items
	
	public double getTotalItemAddStrength() {
		this.totalItemAddStrength = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddStrength += items[i].getAddStrength();
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddStrength += item.getValue().getAddStrength();
		return this.totalItemAddStrength;
	}


	public double getTotalItemAddAgility() {
		this.totalItemAddAgility = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddAgility += items[i].getAddAgility();
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddAgility += item.getValue().getAddAgility();
		return this.totalItemAddAgility;
	}


	public double getTotalItemAddIntelligence() {
		this.totalItemAddIntelligence = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddIntelligence += items[i].getAddIntelligence();
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddIntelligence += item.getValue().getAddIntelligence();
		return this.totalItemAddIntelligence;
	}


	public int getTotalItemAddHP() {
		this.totalItemAddHP = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddHP += items[i].getAddHP();
//				this.totalItemAddHP += items[i].getAddStrength() * STRENGTH_ADD_HP_RATIO;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddHP +=
				item.getValue().getAddHP()
				+ item.getValue().getAddStrength() * STRENGTH_ADD_HP_RATIO;
		return this.totalItemAddHP;
	}

	public int getTotalItemAddMP() {
		this.totalItemAddMP = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddMP += items[i].getAddMP();
//				this.totalItemAddMP += items[i].getAddIntelligence() * INTELLIGENCE_ADD_MP_RATIO;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddMP +=
				item.getValue().getAddMP()
				+ item.getValue().getAddIntelligence() * INTELLIGENCE_ADD_MP_RATIO;
		return this.totalItemAddMP;
	}
	

	public double getTotalItemAddHPGainPerRound() {
		this.totalItemAddHPGainPerRound = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddHPGainPerRound += items[i].getAddHPGainPerRound();
//				this.totalItemAddHPGainPerRound += items[i].getAddStrength() * STRENGTH_ADD_HP_PER_ROUND;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddHPGainPerRound +=
				item.getValue().getAddHPGainPerRound()
				+ item.getValue().getAddStrength() * STRENGTH_ADD_HP_PER_ROUND;
		return this.totalItemAddHPGainPerRound;
	}

	
	public double getTotalItemAddMPGainPerRound() {
		this.totalItemAddMPGainPerRound = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddMPGainPerRound += items[i].getAddMPGainPerRound();
//				this.totalItemAddMPGainPerRound += items[i].getAddIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddMPGainPerRound +=
				item.getValue().getAddMPGainPerRound()
				+ item.getValue().getAddIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND;
		return this.totalItemAddMPGainPerRound;
	}

	
	public double getTotalItemAddPhysicalDefence() {
		this.totalItemAddPhysicalDefence = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddPhysicalDefence += items[i].getAddPhysicalDefence();
//				this.totalItemAddPhysicalDefence += items[i].getAddAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddPhysicalDefence +=
				item.getValue().getAddPhysicalDefence()
				+ item.getValue().getAddAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO;
		return this.totalItemAddPhysicalDefence;
	}

	
	public double getTotalItemAddMagicResistance() {
		this.totalItemAddMagicResistance = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddMagicResistance += items[i].getAddMagicResistance();
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddMagicResistance += item.getValue().getAddMagicResistance();
		return this.totalItemAddMagicResistance;
	}


	public double getTotalItemAddPhysicalAttack() {
		this.totalItemAddPhysicalAttack = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddPhysicalAttack += items[i].getAddPhysicalAttack();
//				
//				if (this.getMainAttribute().equalsIgnoreCase("strength")) {
//					this.totalItemAddPhysicalAttack += items[i].getAddStrength() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;					
//				} else if (this.getMainAttribute().equalsIgnoreCase("agility")){
//					this.totalItemAddPhysicalAttack += items[i].getAddAgility()  * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
//				} else {
//					this.totalItemAddPhysicalAttack += items[i].getAddIntelligence() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
//				}
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet()) {
			this.totalItemAddPhysicalAttack += item.getValue().getAddPhysicalAttack();
			if (this.getMainAttribute().equals("strength")) {
				this.totalItemAddPhysicalAttack += item.getValue().getAddStrength() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
			} else if (this.getMainAttribute().equals("agility")) {
				this.totalItemAddPhysicalAttack += item.getValue().getAddAgility() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
			} else if (this.getMainAttribute().equals("intelligence")) {
				this.totalItemAddPhysicalAttack += item.getValue().getAddIntelligence() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
			}
		}
		return this.totalItemAddPhysicalAttack;
	}

	public double getTotalItemAddPhysicalAttackSpeed() {
		this.totalItemAddPhysicalAttackSpeed = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null) {
//				this.totalItemAddPhysicalAttackSpeed += items[i].getAddPhysicalAttackSpeed();
//				this.totalItemAddPhysicalAttackSpeed += items[i].getAddAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO;
//			}
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddPhysicalAttackSpeed +=
				item.getValue().getAddPhysicalAttackSpeed()
				+ item.getValue().getAddAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO;
		return this.totalItemAddPhysicalAttackSpeed;
	}
	

	public int getTotalItemAddPhysicalAttackArea() {
		this.totalItemAddPhysicalAttackArea = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddPhysicalAttackArea += items[i].getAddPhysicalAttackArea();
//		}
		for (Map.Entry<String, Item> item: items.entrySet())
			this.totalItemAddPhysicalAttackArea += item.getValue().getAddPhysicalAttackArea();
		return this.totalItemAddPhysicalAttackArea;
	}
	

	public int getTotalItemAddMovementSpeed() {
		this.totalItemAddMovementSpeed = 0;
//		for (int i=0; i<MAX_ITEM_NUMBER; i++) {
//			if (this.items[i] != null)
//				this.totalItemAddMovementSpeed += items[i].getAddMovementSpeed();
//		}
		for (Map.Entry<String, Item> item : items.entrySet())
			this.totalItemAddMovementSpeed += item.getValue().getAddMovementSpeed();
		return this.totalItemAddMovementSpeed;
	}
	
	
	// add in an item
	public void addItem(String name, Item item){
		if (items.containsKey(name) || items.size() >= MAX_ITEM_NUMBER || item == null)
			return;
		items.put(name, item);
	}
	
	// sell an item
	public void removeItem(String name){
		// add the selling price to player's money
//		Screen.user.player.setMoney(Screen.user.player.getMoney() + Screen.user.player.getHero().items[itemNumber].getSellPrice()); 
		// delete the item
		items.remove(name);
//		GameFrame.allCharacterInfoGameButtons.get(11 + itemNumber).setImage(null);
	}

}
