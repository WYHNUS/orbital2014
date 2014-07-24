package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Skill {
	
	public static int invokedSkillType = -1;
	
	/*
	 * Skill type : 
	 * 
	 * 1 : teleport
	 * 
	 * 2 : summon creatures
	 * 
	 * 3 : hit a maximum number of enemies in sight
	 * 
	 * 4 : summon creatures by destroying trees
	 * 
	 * 5 : attribute bonus
	 */
	
	private ImageIcon skillImage;
	
	private SummonCharacter skillCharacter;
	private int summonRange;
	
	private int skillType;
	private boolean castable = true;
	private String skillName;
	
	private int usedMP;
	private int usedActionPoint;
	
	private int skillLevel;
	private int castRange;
	
	private int magicalDamage;
	private int numberOfChara;
	
	// coolDownRounds records the minimum rounds, if a hero has casted this spell, needed to end before using this skill again
	// currentRound records the rounds needed to end before using this skill again
	private int coolDownRounds, currentCoolDownRound;
	
	// attributes contributed by skill
	private double addStrength, addAgility, addIntelligence;
	private int addHP, addMP;
	private double addHPGainPerRound, addMPGainPerRound;
	private double addPhysicalDefence, addMagicResistance;
	private double addPhysicalAttack, addPhysicalAttackSpeed;
	private int addPhysicalAttackArea;
	private int addMovementSpeed;
	
	
	// constructor
	public Skill(int skillType, String skilName, Object[] attributes) {
		this.skillType = skillType;
		this.skillName = skilName;
		this.setImage();
		this.skillLevel = 0;
		
		switch(this.skillType) {
		
			case 1 :
				// usedMP, usedActionPoint, castRange, coolDownRounds
				this.usedMP = (int) attributes[0];
				this.usedActionPoint = (int) attributes[1];
				this.castRange = (int) attributes[2];
				this.coolDownRounds = (int) attributes[3];
				this.setCurrentCoolDownRound(0);
				break;
				
			case 2 :
				// usedMP, usedActionPoint, castRange, coolDownRounds, summonCharacter
				this.usedMP = (int) attributes[0];
				this.usedActionPoint = (int) attributes[1];
				this.castRange = (int) attributes[2];
				this.coolDownRounds = (int) attributes[3];
				this.setCurrentCoolDownRound(0);
				
				this.skillCharacter = (SummonCharacter) attributes[4];
				this.summonRange = (int) attributes[5];
				break;
				
			case 3 :
				// usedMP, usedActionPoint, castRange, coolDownRounds,  magicalDanmage, maximumHitUnits
				this.usedMP = (int) attributes[0];
				this.usedActionPoint = (int) attributes[1];
				this.castRange = (int) attributes[2];
				this.coolDownRounds = (int) attributes[3];
				this.setCurrentCoolDownRound(0);
				
				this.magicalDamage = (int) attributes[4];
				this.numberOfChara = (int) attributes[5];
				break;
				
			case 4 :
				// usedMP, usedActionPoint, castRange, coolDownRounds, summonCharacter
				this.usedMP = (int) attributes[0];
				this.usedActionPoint = (int) attributes[1];
				this.castRange = (int) attributes[2];
				this.coolDownRounds = (int) attributes[3];
				this.setCurrentCoolDownRound(0);
				
				this.skillCharacter = (SummonCharacter) attributes[4];
				this.summonRange = (int) attributes[5];
				this.numberOfChara = (int) attributes[6];
				break;
				
			case 5 :
				// addStrength, addAgility, addIntelligence, addHP, addMP, addHPGainPerRound, addMPGainPerRound
				// addPhysicalDefence, addMagicResistance, addPhysicalAttack, addPhysicalAttackSpeed, addPhysicalAttackArea
				// addMovementSpeed
				this.addStrength = (double) attributes[0];
				this.addAgility = (double) attributes[1];
				this.addIntelligence = (double) attributes[2];
				this.addHP = (int) attributes[3];
				this.addMP = (int) attributes[4];
				this.addHPGainPerRound = (double) attributes[5];
				this.addMPGainPerRound = (double) attributes[6];
				this.addPhysicalDefence = (double) attributes[7];
				this.addMagicResistance = (double) attributes[8];
				this.addPhysicalAttack = (double) attributes[9];
				this.addPhysicalAttackSpeed = (double) attributes[10];
				this.addPhysicalAttackArea = (int) attributes[11];
				this.addMovementSpeed = (int) attributes[12];
				
				this.setCastable(false);
				break;
		}

	}
	
	
	public Skill(Skill skill) {
		
		this.setSkillType(skill.getSkillType());
		this.setSkillName(skill.getSkillName());
		this.setImage();
		
		this.setSkillCharacter(skill.getSkillCharacter());
		this.setSummonRange(skill.getSummonRange());
		
		this.setMagicalDamage(skill.getMagicalDamage());
		this.setNumberOfChara(skill.getNumberOfChara());

		this.setUsedMP(skill.getUsedMP());
		this.setUsedActionPoint(skill.getUsedActionPoint());
		this.setCastRange(skill.getCastRange());
		
		this.setCoolDownRounds(skill.getCoolDownRounds());
		this.setCurrentCoolDownRound(skill.getCurrentCoolDownRound());
		
		this.setAddAgility(skill.getAddAgility());
		this.setAddIntelligence(skill.getAddIntelligence());
		this.setAddStrength(skill.getAddStrength());
		this.setAddHP(skill.getAddHP());
		this.setAddMP(skill.getAddMP());
		this.setAddHPGainPerRound(skill.getAddHPGainPerRound());
		this.setAddMPGainPerRound(skill.getAddMPGainPerRound());
		this.setAddPhysicalDefence(skill.getAddPhysicalDefence());
		this.setAddMagicResistance(skill.getAddMagicResistance());
		this.setAddPhysicalAttack(skill.getAddPhysicalAttack());
		this.setAddPhysicalAttackSpeed(skill.getAddPhysicalAttackSpeed());
		this.setAddPhysicalAttackArea(skill.getAddPhysicalAttackArea());
		this.setAddMovementSpeed(skill.getAddMovementSpeed());
		
		this.setCastable(skill.isCastable());
	}


	public int getSkillType() {
		return skillType;
	}


	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}


	public int getUsedMP() {
		return usedMP;
	}


	public void setUsedMP(int usedMP) {
		this.usedMP = usedMP;
	}


	public int getUsedActionPoint() {
		return usedActionPoint;
	}


	public void setUsedActionPoint(int usedActionPoint) {
		this.usedActionPoint = usedActionPoint;
	}


	public int getCastRange() {
		return castRange;
	}


	public void setCastRange(int castRange) {
		this.castRange = castRange;
	}


	public int getCoolDownRounds() {
		return coolDownRounds;
	}


	public void setCoolDownRounds(int coolDownRounds) {
		this.coolDownRounds = coolDownRounds;
	}



	public int getCurrentCoolDownRound() {
		return currentCoolDownRound;
	}



	public void setCurrentCoolDownRound(int currentRound) {
		if (currentRound <= 0) {
			this.currentCoolDownRound = 0;
		} else {
			this.currentCoolDownRound = currentRound;
		}
	}



	public String getSkillName() {
		return skillName;
	}



	public void setSkillName(String skilName) {
		this.skillName = skilName;
	}



	public ImageIcon getImage() {
		return skillImage;
	}

	public void setImage() {
		this.skillImage = new ImageIcon("res/Skills/" + skillName + ".gif");
	}

	
	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}





	public void invokeSkillAction(int heroXPos, int heroYPos) {
		
		// reset attack map and highlighted map
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				GridFrame.highlightedMap[x][y] = -1;
				GridFrame.attackRangeMap[x][y] = -1;
			}
		}	
					
		// change highlighted map
		for(int x=heroXPos-this.getCastRange(); x<heroXPos+this.getCastRange()+1; x++){
			for(int y=heroYPos-this.getCastRange(); y<heroYPos+this.getCastRange()+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of attackable grid
						if (Math.abs(heroXPos - x) + Math.abs(heroYPos - y) <= this.getCastRange()) {
							GridFrame.highlightedMap[x][y] = 1;
						}
					}
				}
			}
		}

	}



	public SummonCharacter getSkillCharacter() {
		return skillCharacter;
	}



	public void setSkillCharacter(SummonCharacter skillCharacter) {
		this.skillCharacter = skillCharacter;
	}



	public int getSummonRange() {
		return summonRange;
	}



	public void setSummonRange(int summonRange) {
		this.summonRange = summonRange;
	}



	public int getMagicalDamage() {
		return magicalDamage;
	}



	public void setMagicalDamage(int magicalDamage) {
		this.magicalDamage = magicalDamage;
	}



	public int getNumberOfChara() {
		return numberOfChara;
	}



	public void setNumberOfChara(int numberOfChara) {
		this.numberOfChara = numberOfChara;
	}


	public boolean isCastable() {
		return castable;
	}


	public void setCastable(boolean castable) {
		this.castable = castable;
	}


	public double getAddStrength() {
		return addStrength;
	}


	public void setAddStrength(double addStrength) {
		this.addStrength = addStrength;
	}


	public double getAddAgility() {
		return addAgility;
	}


	public void setAddAgility(double addAgility) {
		this.addAgility = addAgility;
	}


	public double getAddIntelligence() {
		return addIntelligence;
	}


	public void setAddIntelligence(double addIntelligence) {
		this.addIntelligence = addIntelligence;
	}


	public int getAddHP() {
		return addHP;
	}


	public void setAddHP(int addHP) {
		this.addHP = addHP;
	}


	public int getAddMP() {
		return addMP;
	}


	public void setAddMP(int addMP) {
		this.addMP = addMP;
	}


	public double getAddHPGainPerRound() {
		return addHPGainPerRound;
	}


	public void setAddHPGainPerRound(double addHPGainPerRound) {
		this.addHPGainPerRound = addHPGainPerRound;
	}


	public double getAddMPGainPerRound() {
		return addMPGainPerRound;
	}


	public void setAddMPGainPerRound(double addMPGainPerRound) {
		this.addMPGainPerRound = addMPGainPerRound;
	}


	public double getAddPhysicalDefence() {
		return addPhysicalDefence;
	}


	public void setAddPhysicalDefence(double addPhysicalDefence) {
		this.addPhysicalDefence = addPhysicalDefence;
	}


	public double getAddMagicResistance() {
		return addMagicResistance;
	}


	public void setAddMagicResistance(double addMagicResistance) {
		this.addMagicResistance = addMagicResistance;
	}


	public double getAddPhysicalAttack() {
		return addPhysicalAttack;
	}


	public void setAddPhysicalAttack(double addPhysicalAttack) {
		this.addPhysicalAttack = addPhysicalAttack;
	}


	public double getAddPhysicalAttackSpeed() {
		return addPhysicalAttackSpeed;
	}


	public void setAddPhysicalAttackSpeed(double addPhysicalAttackSpeed) {
		this.addPhysicalAttackSpeed = addPhysicalAttackSpeed;
	}


	public int getAddPhysicalAttackArea() {
		return addPhysicalAttackArea;
	}


	public void setAddPhysicalAttackArea(int addPhysicalAttackArea) {
		this.addPhysicalAttackArea = addPhysicalAttackArea;
	}


	public int getAddMovementSpeed() {
		return addMovementSpeed;
	}


	public void setAddMovementSpeed(int addMovementSpeed) {
		this.addMovementSpeed = addMovementSpeed;
	}
}
