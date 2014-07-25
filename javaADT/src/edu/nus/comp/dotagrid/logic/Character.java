package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Character {
	
	// character compose of : LineCreeps, hero, tower and other buildings
	private Image characterImage;
	
	private String name;
	
	private int bountyMoney;
	private int bountyExp;
	
	private int sight;
	
	private boolean isAlive = false;
	private boolean isAttackable = true; // check if the character can be attacked
	
	private int startingHP, startingMP;
	private int maxHP, maxMP;
	private int currentHP, currentMP;	
	
	private double HPGainPerRound, MPGainPerRound;
	
	private int startingMovementSpeed, totalMovementSpeed;
	
	private double startingPhysicalAttack, basicPhysicalAttack, totalPhysicalAttack;
	private int startingPhysicalAttackArea, totalPhysicalAttackArea;
	private double startingPhysicalAttackSpeed, totalPhysicalAttackSpeed;
	
	private double criticalStrikeChance;
	private double criticalStrikeMultiplier;
	
	private double startingPhysicalDefence, basicPhysicalDefence, totalPhysicalDefence;
	private double startingMagicResistance, totalMagicResistance;
	
	private int maxActionPoint;
	private int currentActionPoint;
	
	// attack priority from 0 - 10 : larger value means less likely to be attacked
	/* 
	 * LineCreep : 3
	 * SummonCharacter / NeutralCreep : 4
	 * Hero : 5
	 * Tower : 7
	 * Building : 9
	 */
	private int startingAttackPriority = 10;
	private int currentAttackPriority = 10;


	/* 0 represents neutral 
	 * 1 represents Sentinel 
	 * 2 represents Scourge
	 * */
	private int teamNumber;

	public static final int MAX_MOVEMENT_SPEED = 522;
	public static final double MIN_MOVEMENT_CONSUME_AP = 2.0;
	public static final double MOVEMENT_CONSUME_AP = 20.0;

	public static final double MAX_PHYSICAL_ATTACK_SPEED = 4.54;
	public static final double MIN_PHYSICAL_ATTACK_CONSUME_AP = 10.0;
	public static final double PHYSICAL_ATTACK_CONSUME_AP = 30.0;
	
	
	public Character(String name, int bountyMoney, int bountyExp, int sight, int startingHP, int startingMP, 
					double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
					double startingPhysicalDefence, double startingMagicResistance, int startingMovementSpeed, 
					int actionPoint, int startingAttackPriority, int teamNumber)
	{
		
		this.setName(name);
		this.setBountyMoney(bountyMoney);
		this.setBountyExp(bountyExp);
		
		this.setSight(sight);
		this.setTeamNumber(teamNumber);
		
		this.setStartingHP(startingHP);
		this.setStartingMP(startingMP);
		this.setmaxHP(this.getStartingHP());
		this.setmaxMP(this.getStartingMP());
		this.setCurrentHP(this.getmaxHP());
		this.setCurrentMP(this.getmaxMP());
		
		this.setStartingPhysicalAttack(startingPhysicalAttack);
		this.setBasicPhysicalAttack(0);
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
		
		this.setMaxActionPoint(actionPoint);
		this.setCurrentActionPoint(this.getMaxActionPoint());
		
		this.setStartingAttackPriority(startingAttackPriority);
		this.setCurrentAttackPriority(startingAttackPriority);
		
		this.setAlive(true);
	}

	
	public Image getCharacterImage() {
		return characterImage;
	}


	public void setCharacterImage(String characterType, String characterName) {
		System.out.println("/edu/nus/comp/dotagrid/res/" + characterType + "/" + characterName + ".jpg");
		this.characterImage = new ImageIcon(getClass().getResource("/edu/nus/comp/dotagrid/res/" + characterType + "/" + characterName + ".jpg")).getImage();
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
		}  else if (currentMP > this.getmaxMP()) {
			// current MP cannnot go beyond max MP value
			this.currentMP = this.getmaxMP();
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
		// minimum physical attack area is 0
		if (physicalAttackArea <= 0) {
			this.startingPhysicalAttackArea = 0;
		} else {
			this.startingPhysicalAttackArea = physicalAttackArea;
		}
	}
	
	public int getTotalPhysicalAttackArea() {
		return totalPhysicalAttackArea;
	}


	public void setTotalPhysicalAttackArea(int totalPhysicalAttackArea) {
		// minimum physical attack area is 0
		if (totalPhysicalAttackArea <= 0) {
			this.totalPhysicalAttackArea = 0;
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
			this.maxActionPoint = 0;
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
		return (int) (currentActionPoint / APUsedInMovingOneGrid());
	}
	
	public double APUsedInMovingOneGrid(){
		return MIN_MOVEMENT_CONSUME_AP + (1 - 1.0 * totalMovementSpeed / MAX_MOVEMENT_SPEED) * MOVEMENT_CONSUME_AP;
	}
	
	public double APUsedWhenAttack(){
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

	public static int getMangicalDamage(double magicalAttack, double magicalResis){
		return (int)(magicalAttack * (100 - magicalResis));
	}

	public int getBountyMoney() {
		return bountyMoney;
	}


	public void setBountyMoney(int bountyMoney) {
		if (bountyMoney < 0){
			System.out.println("Error: not possible for bountyMoney to go below 0");
		} else {
			this.bountyMoney = bountyMoney;
		}
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

	public int getTeamNumber() {
		return this.teamNumber;
	}


	public void setTeamNumber(int teamNumber) {
		if (teamNumber != 0 && teamNumber != 1 && teamNumber != 2) {
			System.out.println("Invalid teamNumber !");
		} else {
			this.teamNumber = teamNumber;
		}
	}
	
	public int getStartingAttackPriority() {
		return startingAttackPriority;
	}


	public void setStartingAttackPriority(int startingAttackPriority) {
		if (startingAttackPriority < 0 || startingAttackPriority > 10) {
			System.out.println("Out of attackPriority boundary!");
		} else {
			this.startingAttackPriority = startingAttackPriority;
		}
	}


	public int getCurrentAttackPriority() {
		return currentAttackPriority;
	}


	public void setCurrentAttackPriority(int currentAttackPriority) {
		if (currentAttackPriority < 0 || currentAttackPriority > 10) {
			System.out.println("Out of attackPriority boundary!");
		} else {
			this.currentAttackPriority = currentAttackPriority;
		}
	}


	public int getSight() {
		return sight;
	}


	public void setSight(int sight) {
		this.sight = sight;
	}


	public double getCriticalStrikeMultiplier() {
		return criticalStrikeMultiplier;
	}


	public void setCriticalStrikeMultiplier(double criticalStrikeMultiplier) {
		this.criticalStrikeMultiplier = criticalStrikeMultiplier;
	}


	public double getCriticalStrikeChance() {
		return criticalStrikeChance;
	}


	public void setCriticalStrikeChance(double criticalStrikeChance) {
		this.criticalStrikeChance = criticalStrikeChance;
	}


	public boolean isAttackable() {
		return isAttackable;
	}


	public void setAttackable(boolean isAttackable) {
		this.isAttackable = isAttackable;
	}


}
