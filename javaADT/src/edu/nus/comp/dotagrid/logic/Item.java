package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Item {
	private ImageIcon itemImage;
	
	private String itemName;
	private int cost;
	private int sellPrice;
	
	private int requiredMPPerUse, requiredHPPerUse;
	
	private boolean isReusable;
	private int usableTime;
	
	private double addStrength, addAgility, addIntelligence;
	
	private int addHP, addMP;
	private double addHPGainPerRound, addMPGainPerRound;
	
	private double addPhysicalDefence, addMagicResistance;
	
	private double addPhysicalAttack, addPhysicalAttackSpeed;
	private int addPhysicalAttackArea;
	
	private int addMovementSpeed;
	
	
	// constructor
	public Item(String itemName, int cost, int requiredMPPerUse, int requiredHPPerUse, boolean isReusable,
				double addStrength, double addAgility, double addIntelligence, int addHP, int addMP, 
				double addHPGainPerRound, double addMPGainPerRound, double addPhysicalDefence, double addMagicResistance,
				double addPhysicalAttack, double addPhysicalAttackSpeed, int addPhysicalAttackArea, int addMovementSpeed)
	{
		
		this.setItemName(itemName);
		this.setItemImage();
		this.setCost(cost);
		this.setSellPrice((int) (cost/2.0));
		
		this.setRequiredMPPerUse(requiredMPPerUse);
		this.setRequiredHPPerUse(requiredHPPerUse);
		
		this.setReusable(isReusable);
		
		this.setAddStrength(addStrength);
		this.setAddAgility(addAgility);
		this.setAddIntelligence(addIntelligence);
		
		this.setAddHP(addHP);
		this.setAddMP(addMP);
		
		this.setAddHPGainPerRound(addHPGainPerRound);
		this.setAddMPGainPerRound(addMPGainPerRound);
		
		this.setAddPhysicalDefence(addPhysicalDefence);
		this.setAddMagicResistance(addMagicResistance);
		
		this.setAddPhysicalAttack(addPhysicalAttack);
		this.setAddPhysicalAttackSpeed(addPhysicalAttackSpeed);
		this.setAddPhysicalAttackArea(addPhysicalAttackArea);
		
		this.setAddMovementSpeed(addMovementSpeed);
			
	}
	
	public ImageIcon getItemImage() {
		return itemImage;
	}

	public void setItemImage() {
		this.itemImage = new ImageIcon("res/items/" + "/" + itemName + ".png");
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}


	public int getRequiredMPPerUse() {
		return requiredMPPerUse;
	}

	public void setRequiredMPPerUse(int requiredMPPerUse) {
		this.requiredMPPerUse = requiredMPPerUse;
	}

	public int getRequiredHPPerUse() {
		return requiredHPPerUse;
	}

	public void setRequiredHPPerUse(int requiredHPPerUse) {
		this.requiredHPPerUse = requiredHPPerUse;
	}

	public boolean isReusable() {
		return isReusable;
	}

	public void setReusable(boolean isReusable) {
		this.isReusable = isReusable;
	}

	public int getUsableTime() {
		return usableTime;
	}

	public void setUsableTime(int usableTime) {
		this.usableTime = usableTime;
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
