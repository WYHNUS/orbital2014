package edu.nus.comp.dotagridandroid.logic;
import java.util.*;
import java.util.concurrent.*;

public class Hero extends GameCharacter{
	
	public static ArrayList<Pair<Hero, Integer>> reviveQueue = new ArrayList<>();
	
	private String mainAttribute;
	private double basicMainAttribute;
	private double totalMainAttribute;
	
	private int heroSpawningXPos = -1;
	private int heroSpawningYPos = -1;
	
	// when display in the game frame, the format will be <basic attribute> + " + " + <attributes obtained from items>
	
	// starting attribute is given when constructing the hero
	private double startingStrength, startingAgility, startingIntelligence;
	private double strengthGrowth, agilityGrowth, intelligenceGrowth;
	
	// basic attribute is calculated by taking  (startingAttributes + growth * level + attributes obtained from skills)
	private double basicStrength, basicAgility, basicIntelligence;
	
	// total attribute is calculated by taking basic attribute + attributes obtained from items
	private double totalStrength, totalAgility, totalIntelligence;
	
	private int unusedSkillCount = 1;

	private int level;
	private int experience;

	public Map<String, Skill> skills = new ConcurrentHashMap<>();
	
	// items and total attributes added from items
	public Map<String, Item> items = new ConcurrentHashMap<>();
	private double totalItemAddStrength, totalItemAddAgility, totalItemAddIntelligence;
	private int totalItemAddHP, totalItemAddMP;
	private double totalItemAddHPGainPerRound, totalItemAddMPGainPerRound;
	private double totalItemAddPhysicalDefence, totalItemAddMagicResistance;
	private double totalItemAddPhysicalAttack, totalItemAddPhysicalAttackSpeed;
	private int totalItemAddPhysicalAttackArea;
	private int totalItemAddMovementSpeed;
	
	private int kill, death, assist;
	private int money;
	
	// define a list of constants for calculation
	public static final int STRENGTH_ADD_HP_RATIO = 19;
	public static final int INTELLIGENCE_ADD_MP_RATIO = 13;

	public static final double STRENGTH_ADD_HP_PER_ROUND = 0.3;
	public static final double INTELLIGENCE_ADD_MP_PER_ROUND = 0.4;
	public static final double AGILITY_ADD_PHYSICAL_DEFENCE_RATIO = 0.02;
	
	public static final double AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO = 0.01;
	public static final double MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO = 1.0;

	public static double HeroHPGainPerRound = 1.0;
	public static double HeroMPGainPerRound = 2.5;

	public static final int MAX_ITEM_NUMBER = 6;
	public static final int MAX_SKILL_NUMBER = 8;
	
	// constructor
	public Hero(String heroName, int bountyMoney, int startingmoney, String mainAttribute, int startingHP, int startingMP, 
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance, int actionPoint, int teamNumber,
			int startingStrength, int startingAgility, int startingIntelligence, 
			double strengthGrowth, double agilityGrowth, double intelligenceGrowth, int movementSpeed) 
	{
		super(heroName, bountyMoney, startingHP, startingMP, startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, 
				startingPhysicalDefence, startingMagicResistance, movementSpeed, actionPoint, teamNumber);
		setObjectType(GAMEOBJECT_TYPE_HERO);
		
		// initialize attributes specific to heros
		this.setMainAttribute(mainAttribute);
	
		this.setLevel(0);
		this.setExperience(0);
		
		this.setMoney(startingmoney);
		this.setKill(0);
		this.setAssist(0);
		this.setDeath(0);

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
		
		this.setBasicMainAttribute(null);
		this.setTotalMainAttribute(null);
		
		this.setMaxHP((int) (this.getStartingHP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO));
		this.setMaxMP((int) (this.getStartingMP() + this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_RATIO));
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());

		this.setHPGainPerRound(this.getTotalStrength() * STRENGTH_ADD_HP_PER_ROUND + HeroHPGainPerRound);
		this.setMPGainPerRound(this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND + HeroMPGainPerRound);

		this.setTotalPhysicalDefence(this.getBasicPhysicalDefence() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO);		
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed() + this.getTotalAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO);
		
		this.setTotalPhysicalAttack(this.getBasicPhysicalAttack() + this.getTotalMainAttribute() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO);
		
	}
	


	public Hero(Hero that) {
//		super(hero.getName(), hero.getBountyMoney(), hero.getStartingHP(), hero.getStartingMP(), hero.getStartingPhysicalAttack(), hero.getStartingPhysicalAttackArea(), 
//				hero.getStartingPhysicalAttackSpeed(), hero.getStartingPhysicalDefence(), hero.getStartingMagicResistance(), 
//				hero.getStartingMovementSpeed(), hero.getMaxActionPoint(), hero.getTeamNumber());
		super(that);
		
		this.setMainAttribute(that.getMainAttribute());
		
		this.setLevel(that.getLevel());
		this.setExperience(that.getExperience());
		
		this.setMoney(that.getMoney());
		this.setKill(that.getKill());
		this.setAssist(that.getAssist());
		this.setDeath(that.getDeath());

		this.setStrengthGrowth(that.getStrengthGrowth());
		this.setAgilityGrowth(that.getAgilityGrowth());
		this.setIntelligenceGrowth(that.getIntelligenceGrowth());	
			
		this.setStartingStrength(that.getStartingStrength());
		this.setStartingAgility(that.getStartingAgility());
		this.setStartingIntelligence(that.getStartingIntelligence());
	
		this.setStartingHP(that.getStartingHP());
		this.setStartingMP(that.getStartingMP());
		
		this.setBasicStrength(that.getBasicStrength());
		this.setBasicAgility(that.getBasicAgility());
		this.setBasicIntelligence(that.getBasicIntelligence());
		
		this.setTotalStrength(that.getTotalStrength());
		this.setTotalAgility(that.getTotalAgility());
		this.setTotalIntelligence(that.getTotalIntelligence());
		
		this.setBasicMainAttribute(null);
		this.setTotalMainAttribute(null);
		
		this.setMaxHP(that.getmaxHP());
		this.setMaxMP(that.getmaxMP());
		this.setCurrentHP(that.getCurrentHP());
		this.setCurrentMP(that.getCurrentMP());
		
		this.setHPGainPerRound(that.getHPGainPerRound());
		this.setMPGainPerRound(that.getMPGainPerRound());
		
		this.setBasicPhysicalDefence(that.getBasicPhysicalDefence());
		this.setTotalPhysicalDefence(that.getTotalPhysicalDefence());		
		this.setTotalPhysicalAttackSpeed(that.getTotalPhysicalAttackSpeed());
		
		this.setBasicPhysicalAttack(that.getBasicPhysicalAttack());
		this.setTotalPhysicalAttack(that.getTotalPhysicalAttack());
		
		this.setLevel(that.getLevel());
		this.setExperience(that.getExperience());
		
		this.setUnusedSkillCount(that.getUnusedSkillCount());
		
		this.setBountyExp(CalculateLevelInfo.calculateBountyExp(this.getLevel()));
		
		this.setMoney(that.getMoney());
		
		this.setCurrentActionPoint(that.getCurrentActionPoint());

		this.setHeroSpawningXPos(that.getHeroSpawningXPos());
		this.setHeroSpawningYPos(that.getHeroSpawningYPos());
		
		for (Map.Entry<String, Item> entry : that.items.entrySet())
			this.items.put(entry.getKey(), new Item(entry.getValue()));
		
		for (Map.Entry<String, Skill> entry : that.skills.entrySet())
			this.skills.put(entry.getKey(), new Skill(entry.getValue()));
		
	}


	public void updateHeroAttributeInfo(){
		this.setBasicStrength(this.getStartingStrength() + this.getLevel() * this.getStrengthGrowth());
		this.setBasicAgility(this.getStartingAgility() + this.getLevel() * this.getAgilityGrowth());
		this.setBasicIntelligence(this.getStartingIntelligence() + this.getLevel() * this.getIntelligenceGrowth());
		
		this.setTotalStrength(this.getBasicStrength() + this.getTotalItemAddStrength());
		this.setTotalAgility(this.getBasicAgility() + this.getTotalItemAddAgility());
		this.setTotalIntelligence(this.getBasicIntelligence() + this.getTotalItemAddIntelligence());
		
		this.setBasicMainAttribute(null);
		this.setTotalMainAttribute(null);
		
		this.setMaxHP((int) (this.getStartingHP() + this.getTotalStrength() * STRENGTH_ADD_HP_RATIO + this.getTotalItemAddHP()));
		this.setMaxMP((int) (this.getStartingMP() + this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_RATIO + this.getTotalItemAddMP()));

		this.setHPGainPerRound(HeroHPGainPerRound + this.getTotalStrength() * STRENGTH_ADD_HP_PER_ROUND + this.getTotalItemAddHPGainPerRound());
		this.setMPGainPerRound(HeroMPGainPerRound + this.getTotalIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND + this.getTotalItemAddMPGainPerRound());
		
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
	
	// accessor and mutator for hero spawning position
	
	public int getHeroSpawningXPos() {
		return heroSpawningXPos;
	}

	public void setHeroSpawningXPos(int heroSpawningXPos) {
		this.heroSpawningXPos = heroSpawningXPos;
	}

	public int getHeroSpawningYPos() {
		return heroSpawningYPos;
	}

	public void setHeroSpawningYPos(int heroSpawningYPos) {
		this.heroSpawningYPos = heroSpawningYPos;
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


	public void setBasicMainAttribute(Object nothing) {	// TODO Not complying to bean rules
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

	// TODO not complying to bean rule
	public void setTotalMainAttribute(Object nothing) {
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



	// KDA & Money
	
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
		if (money <= 0) {
			this.money = 0;
		} else {
			this.money = money;
		}
	}
	
	
	// attributes obtained from items
	
	public double getTotalItemAddStrength() {
		this.totalItemAddStrength = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddStrength += entry.getValue().getAddStrength();
		return this.totalItemAddStrength;
	}


	public double getTotalItemAddAgility() {
		this.totalItemAddAgility = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddAgility += entry.getValue().getAddAgility();
		return this.totalItemAddAgility;
	}


	public double getTotalItemAddIntelligence() {
		this.totalItemAddIntelligence = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddIntelligence += entry.getValue().getAddIntelligence();
		return this.totalItemAddIntelligence;
	}


	public int getTotalItemAddHP() {
		this.totalItemAddHP = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddHP += entry.getValue().getAddHP() + entry.getValue().getAddStrength() * STRENGTH_ADD_HP_RATIO;
		return this.totalItemAddHP;
	}

	public int getTotalItemAddMP() {
		this.totalItemAddMP = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddMP += entry.getValue().getAddMP() + entry.getValue().getAddIntelligence() * INTELLIGENCE_ADD_MP_RATIO;
		return this.totalItemAddMP;
	}
	

	public double getTotalItemAddHPGainPerRound() {
		this.totalItemAddHPGainPerRound = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddHPGainPerRound += entry.getValue().getAddHPGainPerRound() + entry.getValue().getAddStrength() * STRENGTH_ADD_HP_PER_ROUND;
		return this.totalItemAddHPGainPerRound;
	}

	
	public double getTotalItemAddMPGainPerRound() {
		this.totalItemAddMPGainPerRound = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddMPGainPerRound += entry.getValue().getAddMPGainPerRound() + entry.getValue().getAddIntelligence() * INTELLIGENCE_ADD_MP_PER_ROUND;
		return this.totalItemAddMPGainPerRound;
	}

	
	public double getTotalItemAddPhysicalDefence() {
		this.totalItemAddPhysicalDefence = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddPhysicalDefence += entry.getValue().getAddPhysicalDefence() + entry.getValue().getAddAgility() * AGILITY_ADD_PHYSICAL_DEFENCE_RATIO;
		return this.totalItemAddPhysicalDefence;
	}

	
	public double getTotalItemAddMagicResistance() {
		this.totalItemAddMagicResistance = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddMagicResistance += entry.getValue().getAddMagicResistance();
		return this.totalItemAddMagicResistance;
	}


	public double getTotalItemAddPhysicalAttack() {
		this.totalItemAddPhysicalAttack = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			switch (this.getMainAttribute()) {
			case "strength":
				this.totalItemAddPhysicalAttack += entry.getValue().getAddStrength() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
				break;
			case "agility":
				this.totalItemAddPhysicalAttack += entry.getValue().getAddAgility() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
				break;
			case "intelligence":
				this.totalItemAddPhysicalAttack += entry.getValue().getAddIntelligence() * MAIN_ATTRIBUTE_ADD_PHYSICAL_ATTACK_RATIO;
				break;
			}
		return this.totalItemAddPhysicalAttack;
	}

	public double getTotalItemAddPhysicalAttackSpeed() {
		this.totalItemAddPhysicalAttackSpeed = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddPhysicalAttackSpeed += entry.getValue().getAddPhysicalAttackSpeed() + entry.getValue().getAddAgility() * AGILITY_ADD_PHYSICAL_ATTACK_SPEED_RATIO;
		return this.totalItemAddPhysicalAttackSpeed;
	}
	

	public int getTotalItemAddPhysicalAttackArea() {
		this.totalItemAddPhysicalAttackArea = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddPhysicalAttackArea += entry.getValue().getAddPhysicalAttackArea();
		return this.totalItemAddPhysicalAttackArea;
	}
	

	public int getTotalItemAddMovementSpeed() {
		this.totalItemAddMovementSpeed = 0;
		for (Map.Entry<String, Item> entry : items.entrySet())
			this.totalItemAddMovementSpeed += entry.getValue().getAddMovementSpeed();
		return this.totalItemAddMovementSpeed;
	}
	
	
	// add in an item
	public void addItem(String name, Item item){
		if (items.size() < MAX_ITEM_NUMBER && name != null && !items.containsKey(name) && item != null)
			items.put(name, item);
	}
	
	// sell an item
	public void removeItem(String name){
		if (items.containsKey(name))
			this.money += items.remove(name).getSellPrice();
	}public int getUnusedSkillCount() {
		return unusedSkillCount;
	}



	public void setUnusedSkillCount(int unusedSkillCount) {
		if (unusedSkillCount < 0) {
			System.out.println("unusedSkillCount cannnot go below 0!");
		} else {
			this.unusedSkillCount = unusedSkillCount;
		}
	}

}
