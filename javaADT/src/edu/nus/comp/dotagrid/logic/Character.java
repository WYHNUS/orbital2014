package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Character {
	
	// character compose of : LineCreeps, hero, tower and other buildings
	
	private Image characterImage;
	
	private int hp, mp;
	private int physicalAttack, physicalAttackArea;
	private double physicalAttackSpeed;
	
	private int actionPoint;
	private double physicalDefense, magicResistance;

	
	public Character(int hp, int mp, int physicalAttack, int physicalAttackArea, double physicalAttackSpeed, double physicalDefense, double magicResistance, int actionPoint){
		this.setHp(hp);
		this.setMp(mp);
		this.setPhysicalAttack(physicalAttack);
		this.setPhysicalAttackArea(physicalAttackArea);
		this.setPhysicalAttackSpeed(physicalAttackSpeed);
		this.setPhysicalDefense(physicalDefense);
		this.setMagicResistance(magicResistance);
		this.setActionPoint(actionPoint);
	}

	
	public Image getCharacterImage() {
		return characterImage;
	}


	public void setCharacterImage(String characterType, String characterName) {
		this.characterImage = new ImageIcon("res/" + characterType + "/" + characterName + ".jpg").getImage();
	}
	

	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getMp() {
		return mp;
	}


	public void setMp(int mp) {
		this.mp = mp;
	}


	public int getPhysicalAttack() {
		return physicalAttack;
	}


	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}


	public int getPhysicalAttackArea() {
		return physicalAttackArea;
	}


	public void setPhysicalAttackArea(int physicalAttackArea) {
		this.physicalAttackArea = physicalAttackArea;
	}


	public double getPhysicalAttackSpeed() {
		return physicalAttackSpeed;
	}


	public void setPhysicalAttackSpeed(double physicalAttackSpeed2) {
		this.physicalAttackSpeed = physicalAttackSpeed2;
	}


	public int getActionPoint() {
		return actionPoint;
	}


	public void setActionPoint(int actionPoint) {
		this.actionPoint = actionPoint;
	}


	public double getPhysicalDefense() {
		return physicalDefense;
	}


	public void setPhysicalDefense(double physicalDefense) {
		this.physicalDefense = physicalDefense;
	}


	public double getMagicResistance() {
		return magicResistance;
	}


	public void setMagicResistance(double magicResistance) {
		this.magicResistance = magicResistance;
	}


	
}
