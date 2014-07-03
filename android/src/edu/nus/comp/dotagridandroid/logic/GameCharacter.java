package edu.nus.comp.dotagridandroid.logic;

public class GameCharacter extends GameObject {
	
	// character compose of : LineCreeps, hero, tower and other buildings
	
	private String characterImage;
	
	private String name;
	
	private int bountyMoney;
	private int bountyExp;
	
	private boolean isAlive = false;
	
	private int startingHP, startingMP;
	private int maxHP, maxMP;
	private int currentHP, currentMP;
	
	private double HPGainPerRound, MPGainPerRound;
	
	private int startingMovementSpeed, totalMovementSpeed;
	
	private double startingPhysicalAttack, basicPhysicalAttack, totalPhysicalAttack;
	private int startingPhysicalAttackArea, totalPhysicalAttackArea;
	private double startingPhysicalAttackSpeed, totalPhysicalAttackSpeed;
	
	private double startingPhysicalDefence, basicPhysicalDefence, totalPhysicalDefence;
	private double startingMagicResistance, totalMagicResistance;
	
	private int maxActionPoint;
	private int currentActionPoint;
	
	/* 0 represents neutral 
	 * 1 represents Sentinel 
	 * 2 represents Scourge
	 * */
	private int teamNumber;

	public static final int MAX_MOVEMENT_SPEED = 522;
	public static final double MIN_MOVEMENT_CONSUME_AP = 2.0;
	public static final double MOVEMENT_CONSUME_AP = 20.0;

	public static final double MAX_PHYSICAL_ATTACK_SPEED = 4.54;
	public static final double MIN_PHYSICAL_ATTACK_CONSUME_AP = 20.0;
	public static final double PHYSICAL_ATTACK_CONSUME_AP = 38.0;
	
	
	public GameCharacter(String name, int bountyMoney, int startingHP, int startingMP, 
					double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
					double startingPhysicalDefence, double startingMagicResistance, int startingMovementSpeed, int maxActionPoint, int teamNumber)
	{
		super(GAMEOBJECT_TYPE_CHARACTER);
		this.setName(name);
		this.setBountyMoney(bountyMoney);
		this.setTeamNumber(teamNumber);
		
		this.setStartingHP(startingHP);
		this.setStartingMP(startingMP);
		this.setMaxHP(this.getStartingHP());
		this.setMaxMP(this.getStartingMP());
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());
		
		this.setStartingPhysicalAttack(startingPhysicalAttack);
		this.setBasicPhysicalAttack(this.getStartingPhysicalAttack());
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack());
		
		this.setStartingPhysicalAttackArea(startingPhysicalAttackArea);
		this.setTotalPhysicalAttackArea(this.getStartingPhysicalAttackArea());
		
		this.setStartingPhysicalAttackSpeed(startingPhysicalAttackSpeed);
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed());
		
		this.setStartingPhysicalDefence(startingPhysicalDefence);
		this.setBasicPhysicalDefence(this.getStartingPhysicalDefence());
		this.setTotalPhysicalDefence(this.getStartingPhysicalDefence());
		
		this.setStartingMagicResistance(startingMagicResistance);
		this.setTotalMagicResistance(this.getStartingMagicResistance());
		
		this.setStartingMovementSpeed(startingMovementSpeed);
		this.setTotalMovementSpeed(this.getStartingMovementSpeed());
		
		this.setMaxActionPoint(maxActionPoint);
		this.setCurrentActionPoint(this.getMaxActionPoint());
		
		this.setAlive(true);
	}
	
	public GameCharacter (GameCharacter that) {
		super(that);
		this.setName(that.name);
		this.setBountyMoney(that.bountyMoney);
		this.setTeamNumber(that.teamNumber);
		
		this.setStartingHP(that.startingHP);
		this.setStartingMP(that.startingMP);
		this.setMaxHP(this.getStartingHP());
		this.setMaxMP(this.getStartingMP());
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());
		
		this.setStartingPhysicalAttack(that.startingPhysicalAttack);
		this.setBasicPhysicalAttack(this.getStartingPhysicalAttack());
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack());
		
		this.setStartingPhysicalAttackArea(that.startingPhysicalAttackArea);
		this.setTotalPhysicalAttackArea(this.getStartingPhysicalAttackArea());
		
		this.setStartingPhysicalAttackSpeed(that.startingPhysicalAttackSpeed);
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed());
		
		this.setStartingPhysicalDefence(that.startingPhysicalDefence);
		this.setBasicPhysicalDefence(this.getStartingPhysicalDefence());
		this.setTotalPhysicalDefence(this.getStartingPhysicalDefence());
		
		this.setStartingMagicResistance(that.startingMagicResistance);
		this.setTotalMagicResistance(this.getStartingMagicResistance());
		
		this.setStartingMovementSpeed(that.startingMovementSpeed);
		this.setTotalMovementSpeed(this.getStartingMovementSpeed());
		
		this.setMaxActionPoint(that.maxActionPoint);
		this.setCurrentActionPoint(this.getMaxActionPoint());
		
		this.setAlive(true);
	}

	
	public String getCharacterImage() {
		return characterImage;
	}


	public void setCharacterImage(String modelName) {
		this.characterImage = modelName;
	}
	
	

	// accessor and mutator for HP and MP
	
	public int getStartingHP() {
		return startingHP;
	}
	
	public void setStartingHP(int startingHP) {
		// not possible for startingHP to go below 0
		if (startingHP < 0) {
			System.out.println("Error: not possible for starting HP to go below 0");
		} else {
			this.startingHP = startingHP;
		}
	}
	
	public int getStartingMP() {
		return startingMP;
	}
	
	public void setStartingMP(int startingMP) {
		// not possible for startingMP to go below 0
		if (startingMP < 0) {
			System.out.println("Error: not possible for starting MP to go below 0");
		} else {
			this.startingMP = startingMP;
		}
	}

	public int getCurrentHP() {
		return currentHP;
	}


	public void setCurrentHP(int currentHP) {
		// if currentHP goes below 0, the character is dead
		if (currentHP < 0) {
			this.currentHP = 0;
			this.setAlive(false);
		} else if (currentHP > this.getmaxHP()) {
			// current HP cannnot go beyond max HP value
			this.currentHP = this.getmaxHP();
		} else {
			this.currentHP = currentHP;
		}
	}


	public int getCurrentMP() {
		return currentMP;
	}


	public void setCurrentMP(int currentMP) {
		// minimum currentMP is 0 
		if (currentMP < 0) {
			this.currentMP = 0;
		} else if (currentMP > this.getmaxMP()) {
			// current MP cannnot go beyond max MP value
			this.currentMP = this.getmaxMP();
		} else {
			this.currentMP = currentMP;
		}
	}



	public int getmaxHP() {
		return maxHP;
	}


	public void setMaxHP(int maxHP) {
		// maxHP cannot goes below startingHP
		if (maxHP <= startingHP) {
			this.maxHP = startingHP;
		} else {
			this.maxHP = maxHP;
		}
	}


	public int getmaxMP() {
		return maxMP;
	}


	public void setMaxMP(int maxMP) {
		// minimum maxMP is startingMP 
		if (maxMP <= startingMP) {
			this.maxMP = startingMP;
		} else {
			this.maxMP = maxMP;
		}
	}

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
	
	// accessor and mutator for physical attack -/speed/area
	
	public double getStartingPhysicalAttack() {
		return startingPhysicalAttack;
	}


	public void setStartingPhysicalAttack(double physicalAttack) {
		// minimum physical attack is 0
		if (physicalAttack <= 0) {
			this.startingPhysicalAttack = 0;
		} else {
			this.startingPhysicalAttack = physicalAttack;
		}
	}

	public double getBasicPhysicalAttack() {
		return basicPhysicalAttack;
	}


	public void setBasicPhysicalAttack(double basicPhysicalAttack) {
		this.basicPhysicalAttack = basicPhysicalAttack;
	}

	public double getTotalPhysicalAttack() {
		return totalPhysicalAttack;
	}


	public void setTotalPhysicalAttack(double totalPhysicalAttack) {
		// minimum physical attack is 0
		if (totalPhysicalAttack <= 0) {
			this.totalPhysicalAttack = 0;
		} else {
			this.totalPhysicalAttack = totalPhysicalAttack;
		}
	}


	public int getStartingPhysicalAttackArea() {
		return startingPhysicalAttackArea;
	}


	public void setStartingPhysicalAttackArea(int physicalAttackArea) {
		// minimum physical attack area is 1
		if (physicalAttackArea <= 1) {
			this.startingPhysicalAttackArea = 1;
		} else {
			this.startingPhysicalAttackArea = physicalAttackArea;
		}
	}
	
	public int getTotalPhysicalAttackArea() {
		return totalPhysicalAttackArea;
	}


	public void setTotalPhysicalAttackArea(int totalPhysicalAttackArea) {
		// minimum physical attack is 0
		if (totalPhysicalAttackArea <= 1) {
			this.totalPhysicalAttackArea = 1;
		} else {
			this.totalPhysicalAttackArea = totalPhysicalAttackArea;
		}
	}

	
	public double getStartingPhysicalAttackSpeed() {
		return startingPhysicalAttackSpeed;
	}


	public void setStartingPhysicalAttackSpeed(double startingphysicalAttackSpeed) {
		this.startingPhysicalAttackSpeed = startingphysicalAttackSpeed;
	}
	
	public double getTotalPhysicalAttackSpeed() {
		return totalPhysicalAttackSpeed;
	}


	public void setTotalPhysicalAttackSpeed(double totalPhysicalAttackSpeed) {
		this.totalPhysicalAttackSpeed = totalPhysicalAttackSpeed;
	}


	
	// accessor and mutator for ActionPoint
	public int getMaxActionPoint() {
		return maxActionPoint;
	}


	public void setMaxActionPoint(int actionPoint) {
		// minimum AP is 0
		if(actionPoint <= 0) {
			System.out.println("Maximum Action Point cannnot go below 0!");
		} else {
			this.maxActionPoint = actionPoint;
		}
	}
	
	public int getCurrentActionPoint() {
		return currentActionPoint;
	}


	public void setCurrentActionPoint(int currentActionPoint) {
		// minimum current Action Point is 0
		if(currentActionPoint <= 0) {
			this.currentActionPoint = 0;
		} else {
			this.currentActionPoint = currentActionPoint;
		}
	}



	
	// accessor and mutator for Physical Defence
	
	public double getStartingPhysicalDefence() {
		return startingPhysicalDefence;
	}


	public void setStartingPhysicalDefence(double startingPhysicalDefence) {
		// there is no upper or lower boundary for physical Defence
		this.startingPhysicalDefence = startingPhysicalDefence;
	}


	public double getBasicPhysicalDefence() {
		return basicPhysicalDefence;
	}


	public void setBasicPhysicalDefence(double basicPhysicalDefence) {
		this.basicPhysicalDefence = basicPhysicalDefence;
	}
	
	
	public double getTotalPhysicalDefence() {
		return totalPhysicalDefence;
	}


	public void setTotalPhysicalDefence(double totalPhysicalDefence) {
		// there is no upper or lower boundary for physical Defence
		this.totalPhysicalDefence = totalPhysicalDefence;
	}


	
	// accessor and mutator for magic resistance
	
	public double getStartingMagicResistance() {
		return startingMagicResistance;
	}


	public void setStartingMagicResistance(double startingMagicResistance) {
		// there is no upper or lower boundary for magic resistance
		this.startingMagicResistance = startingMagicResistance;
	}
	
	public double getTotalMagicResistance() {
		return totalMagicResistance;
	}


	public void setTotalMagicResistance(double totalMagicResistance) {
		// there is no upper or lower boundary for magic resistance
		this.totalMagicResistance = totalMagicResistance;
	}


	
	// accessor and mutator for movement speed
	
	public int getStartingMovementSpeed() {
		return startingMovementSpeed;
	}


	public void setStartingMovementSpeed(int startingMovementSpeed) {
		// cannot be higher than maximum movement speed
		if (startingMovementSpeed >= MAX_MOVEMENT_SPEED){
			this.startingMovementSpeed = MAX_MOVEMENT_SPEED;
		} else {
			this.startingMovementSpeed = startingMovementSpeed;
		}
	}

	public int getTotalMovementSpeed() {
		return totalMovementSpeed;
	}


	public void setTotalMovementSpeed(int totalMovementSpeed) {
		// cannot be higher than maximum movement speed
		if (totalMovementSpeed >= MAX_MOVEMENT_SPEED){
			this.totalMovementSpeed = MAX_MOVEMENT_SPEED;
		} else {
			this.totalMovementSpeed = totalMovementSpeed;
		}
	}
	
	public int getNumberOfMovableGrid(){
		return (int) (currentActionPoint / getAPUsedInMovingOneGrid());
	}
	
	public double getAPUsedInMovingOneGrid(){
		return MIN_MOVEMENT_CONSUME_AP + (1 - 1.0 * totalMovementSpeed / MAX_MOVEMENT_SPEED) * MOVEMENT_CONSUME_AP;
	}
	
	public double getAPUsedWhenAttack(){
		return MIN_PHYSICAL_ATTACK_CONSUME_AP + (1 - 1.0 * totalPhysicalAttackSpeed / MAX_PHYSICAL_ATTACK_SPEED) * PHYSICAL_ATTACK_CONSUME_AP;
	}

	
	// accessor and mutator for other properties
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isAlive() {
		return isAlive;
	}


	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public static int getActualDamage(double physicalAttack, double physicalDefence){
		return (int)((1 - physicalDefence * 0.06 / (1 + 0.06 * physicalDefence) ) * physicalAttack);	
	}


	public int getBountyMoney() {
		return bountyMoney;
	}


	public void setBountyMoney(int bountyMoney) {
		if (bountyMoney < 0){
			System.out.println("Error: not possible for starting HP to go below 0");
		} else {
			this.bountyMoney = bountyMoney;
		}
	}	public int getTeamNumber() {
		return teamNumber;
	}

	public int getBountyExp() {
		return bountyExp;
	}


	public void setBountyExp(int bountyExp) {
		if (bountyExp < 0){
			System.out.println("Error: not possible for bountyExp to go below 0");
		} else {
			this.bountyExp = bountyExp;
		}
	}


	public void setTeamNumber(int teamNumber) {
		if (teamNumber != 0 && teamNumber != 1 && teamNumber != 2) {
			System.out.println("Invalid teamNumber !");
		} else {
			this.teamNumber = teamNumber;
		}
	}
}
