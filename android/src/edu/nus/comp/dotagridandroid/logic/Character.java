package edu.nus.comp.dotagridandroid.logic;


public class Character {
	
	// character compose of : LineCreeps, hero, tower and other buildings
	
	private String characterImage;
	
	private String name;
	
	private boolean isAlive = false;
	
	private int startingHP, startingMP;
	private int maxHP, maxMP;
	private int currentHP, currentMP;	
	
	private int startingMovementSpeed, totalMovementSpeed;
	
	private double startingPhysicalAttack, basicPhysicalAttack, totalPhysicalAttack;
	private int startingPhysicalAttackArea, totalPhysicalAttackArea;
	private double startingPhysicalAttackSpeed, totalPhysicalAttackSpeed;
	
	private double startingPhysicalDefence, basicPhysicalDefence, totalPhysicalDefence;
	private double startingMagicResistance, totalMagicResistance;
	
	private int actionPoint;
	private int currentActionPoint;

	public static final int MAX_MOVEMENT_SPEED = 522;
	public static final int MIN_MOVEMENT_CONSUME_AP = 2;
	public static final int MOVEMENT_CONSUME_AP = 20;
	
	
	public Character(String name, int startingHP, int startingMP, 
					double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
					double startingPhysicalDefence, double startingMagicResistance, int startingMovementSpeed, int actionPoint)
	{
		
		this.setName(name);
		
		this.setStartingHP(startingHP);
		this.setStartingMP(startingMP);
		this.setmaxHP(this.getStartingHP());
		this.setmaxMP(this.getStartingMP());
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
		
		this.setActionPoint(actionPoint);
		this.setCurrentActionPoint(this.getActionPoint());
		
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
		if (startingHP <= 0) {
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
		if (startingMP <= 0) {
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
		if (currentHP <= 0) {
			this.currentHP = 0;
			this.setAlive(false);
		} else {
			this.currentHP = currentHP;
		}
	}


	public int getCurrentMP() {
		return currentMP;
	}


	public void setCurrentMP(int currentMP) {
		// minimum currentMP is 0 
		if (currentMP <= 0) {
			this.currentMP = 0;
		} else {
			this.currentMP = currentMP;
		}
	}



	public int getmaxHP() {
		return maxHP;
	}


	public void setmaxHP(int maxHP) {
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


	public void setmaxMP(int maxMP) {
		// minimum maxMP is startingMP 
		if (maxMP <= startingMP) {
			this.maxMP = startingMP;
		} else {
			this.maxMP = maxMP;
		}
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
	public int getActionPoint() {
		return actionPoint;
	}


	public void setActionPoint(int actionPoint) {
		// minimum AP is 0
		if(actionPoint <= 0) {
			System.out.println("Maximum Action Point cannnot go below 0!");
		} else {
			this.actionPoint = actionPoint;
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
		return (int) (currentActionPoint / APUsedInMovingOneGrid());
	}
	
	public double APUsedInMovingOneGrid(){
		return MIN_MOVEMENT_CONSUME_AP + (1 - 1.0 * totalMovementSpeed / MAX_MOVEMENT_SPEED) * MOVEMENT_CONSUME_AP;
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
	
}
