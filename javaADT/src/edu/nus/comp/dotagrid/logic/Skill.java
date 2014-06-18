package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

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
	private String skillName;
	
	private int usedMP;
	private int usedActionPoint;
	
	private int castRange;
	
	// coolDownRounds records the minimum rounds, if a hero has casted this spell, needed to end before using this skill again
	// currentRound records the rounds needed to end before using this skill again
	private int coolDownRounds, currentRound;
	
	
	// constructor
	public Skill(int skillType, String skilName, int[] attributes) {
		this.skillType = skillType;
		this.skillName = skilName;
		this.setImage();
		
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
	
	
	
	public Skill(Skill skill) {
		
		this.setSkillType(skill.getSkillType());
		this.setSkillName(skill.getSkillName());
		this.setImage();

		this.setUsedMP(skill.getUsedMP());
		this.setUsedActionPoint(skill.getUsedActionPoint());
		this.setCastRange(skill.getCastRange());
		
		this.setCoolDownRounds(skill.getCoolDownRounds());
		this.setCurrentRound(skill.getCurrentRound());
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
		this.skillImage = new ImageIcon("res/skills/" + skillName + ".gif");
	}



	public void invokeSkillAction() {
		// TODO Auto-generated method stub
		
	}
}
