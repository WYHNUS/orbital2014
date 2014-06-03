package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Character {
	
	// character compose of : LineCreeps, hero, tower and other buildings
	
	private Image characterImage;
	
	private String name;
	
	private boolean isAlive = false;
	
	private int startingHP, startingMP;
	private int maxHP, maxMP;
	private int currentHP, currentMP;
	
	private int startingPhysicalAttack, totalPhysicalAttack;
	private int startingPhysicalAttackArea, totalPhysicalAttackArea;
	private double startingPhysicalAttackSpeed, totalPhysicalAttackSpeed;
	
	private double startingPhysicalDefense, totalPhysicalDefense;
	private double startingMagicResistance, totalMagicResistance;
	
	private int actionPoint;
	private int currentActionPoint;

	
	public Character(String name, int startingHP, int startingMP, int startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, double startingPhysicalDefense, double startingMagicResistance, int actionPoint){
		this.setName(name);
		
		this.setStartingHP(startingHP);
		this.setStartingMP(startingMP);
		this.setmaxHP(this.getStartingHP());
		this.setmaxMP(this.getStartingMP());
		this.setCurrentHP(this.getStartingHP());
		this.setCurrentMP(this.getStartingMP());
		
		this.setStartingPhysicalAttack(startingPhysicalAttack);
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack());
		
		this.setStartingPhysicalAttackArea(startingPhysicalAttackArea);
		this.setTotalPhysicalAttackArea(this.getStartingPhysicalAttackArea());
		
		this.setStartingPhysicalAttackSpeed(startingPhysicalAttackSpeed);
		this.setTotalPhysicalAttackSpeed(this.getStartingPhysicalAttackSpeed());
		
		this.setStartingPhysicalDefense(startingPhysicalDefense);
		this.setTotalPhysicalDefense(this.getStartingPhysicalDefense());
		
		this.setStartingMagicResistance(startingMagicResistance);
		this.setTotalMagicResistance(this.getStartingMagicResistance());
		
		this.setActionPoint(actionPoint);
		this.setCurrentActionPoint(this.getActionPoint());
		
		this.setAlive(true);
	}

	
	public Image getCharacterImage() {
		return characterImage;
	}


	public void setCharacterImage(String characterType, String characterName) {
		this.characterImage = new ImageIcon("res/" + characterType + "/" + characterName + ".jpg").getImage();
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
	
	public int getStartingPhysicalAttack() {
		return startingPhysicalAttack;
	}


	public void setStartingPhysicalAttack(int physicalAttack) {
		// minimum physical attack is 0
		if (physicalAttack <= 0) {
			this.startingPhysicalAttack = 0;
		} else {
			this.startingPhysicalAttack = physicalAttack;
		}
	}
	
	public int getTotalPhysicalAttack() {
		return totalPhysicalAttack;
	}


	public void setTotalPhysicalAttack(int totalPhysicalAttack) {
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



	
	// accessor and mutator for Physical Defense
	
	public double getStartingPhysicalDefense() {
		return startingPhysicalDefense;
	}


	public void setStartingPhysicalDefense(double startingPhysicalDefense) {
		// there is no upper or lower boundary for physical defense
		this.startingPhysicalDefense = startingPhysicalDefense;
	}
	
	
	public double getTotalPhysicalDefense() {
		return totalPhysicalDefense;
	}


	public void setTotalPhysicalDefense(double totalPhysicalDefense) {
		// there is no upper or lower boundary for physical defense
		this.totalPhysicalDefense = totalPhysicalDefense;
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
