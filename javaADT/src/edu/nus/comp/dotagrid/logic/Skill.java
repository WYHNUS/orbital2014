package edu.nus.comp.dotagrid.logic;

import javax.swing.ImageIcon;

public class Skill {
	/*
	 * Skill type : 
	 * 
	 * 1 : teleport
	 * 
	 */
	
	private ImageIcon skillImage;
	
	private int skillType;
	private String skilName;
	
	private int usedMP;
	private int usedActionPoint;
	
	private int castRange;
	
	// coolDownRounds records the minimum rounds, if a hero has casted this spell, needed to end before using this skill again
	// currentRound records the rounds needed to end before using this skill again
	private int coolDownRounds, currentRound;
	
	
	// constructor
	public Skill(int skillType, String skilName, int[] attributes) {
		this.skillType = skillType;
		this.skilName = skilName;
		
		switch(this.skillType) {
		
			case 1 :
				// usedMP, usedActionPoint, castRange, coolDownRounds
				this.usedMP = attributes[0];
				this.usedActionPoint = attributes[1];
				this.castRange = attributes[2];
				this.coolDownRounds = attributes[3];
				this.setCurrentRound(0);
				break;
				
			case 2 :
				
				break;
		}
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



	public int getCurrentRound() {
		return currentRound;
	}



	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}



	public String getSkilName() {
		return skilName;
	}



	public void setSkilName(String skilName) {
		this.skilName = skilName;
	}
}
