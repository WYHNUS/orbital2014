package edu.nus.comp.dotagrid.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Item {
	private ImageIcon itemImage;
	
	private String itemName;
	private int cost;
	private int sellPrice;
	
	private int requiredMPPerUse, requiredHPPerUse;
	
	private boolean isUsable;
	private boolean discardAfterUse;
	private int usableTime = 1; // default 1
	private int castingRange;
	
	private double addStrength, addAgility, addIntelligence;
	
	private int addHP, addMP;
	private double addHPGainPerRound, addMPGainPerRound;
	
	private double addPhysicalDefence, addMagicResistance;
	
	private double addPhysicalAttack, addPhysicalAttackSpeed;
	private int addPhysicalAttackArea;
	
	private int addMovementSpeed;
	
	
	// constructor	

	public Item() {
		// default java constructor
	}

	public Item(String itemName, int cost, boolean isUsable){
		this.setItemName(itemName);
		this.setItemImage();
		this.setCost(cost);
		this.setSellPrice((int) (cost/2.0));

		this.setUsable(isUsable);
	}

	public Item(String itemName, int cost, int requiredMPPerUse, int requiredHPPerUse, boolean isUsable,
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
		
		this.setUsable(isUsable);
		
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
	
	
	public Item(Item item) {
		this.setItemName(item.getItemName());
		this.setItemImage();
		this.setCost(item.getCost());
		this.setSellPrice(item.getSellPrice());
		
		this.setRequiredMPPerUse(item.getRequiredMPPerUse());
		this.setRequiredHPPerUse(item.getRequiredHPPerUse());
		
		this.setUsable(item.isUsable());
		this.setUsableTime(item.getUsableTime());
		this.setDiscardAfterUse(item.isDiscardAfterUse());
		this.setCastingRange(item.getCastingRange());
		
		this.setAddStrength(item.getAddStrength());
		this.setAddAgility(item.getAddAgility());
		this.setAddIntelligence(item.getAddIntelligence());
		
		this.setAddHP(item.getAddHP());
		this.setAddMP(item.getAddMP());
		
		this.setAddHPGainPerRound(item.getAddHPGainPerRound());
		this.setAddMPGainPerRound(item.getAddMPGainPerRound());
		
		this.setAddPhysicalDefence(item.getAddPhysicalDefence());
		this.setAddMagicResistance(item.getAddMagicResistance());
		
		this.setAddPhysicalAttack(item.getAddPhysicalAttack());
		this.setAddPhysicalAttackSpeed(item.getAddPhysicalAttackSpeed());
		this.setAddPhysicalAttackArea(item.getAddPhysicalAttackArea());
		
		this.setAddMovementSpeed(item.getAddMovementSpeed());
	}
	

	public ImageIcon getItemImage() {
		return itemImage;
	}

	public void setItemImage() {
		this.itemImage = new ImageIcon(getClass().getResource("/edu/nus/comp/dotagrid/res/Items/" + itemName + ".jpg"));
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

	public boolean isUsable() {
		return isUsable;
	}

	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}

	public boolean isDiscardAfterUse() {
		return discardAfterUse;
	}


	public void setDiscardAfterUse(boolean discardAfterUse) {
		this.discardAfterUse = discardAfterUse;
	}


	public int getUsableTime() {
		return usableTime;
	}

	public void setUsableTime(int usableTime) {
		this.usableTime = usableTime;
	}

	public int getCastingRange() {
		return castingRange;
	}

	public void setCastingRange(int castingRange) {
		this.castingRange = castingRange;
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
	
	
	public void invokeItemAction(int heroXPos, int heroYPos) {
		
		// reset attack map and highlighted map
		for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
			for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
				GridFrame.highlightedMap[x][y] = false;
				GridFrame.attackRangeMap[x][y] = false;
			}
		}	
					
		// change highlighted map
		for(int x=heroXPos-this.getCastingRange(); x<heroXPos+this.getCastingRange()+1; x++){
			for(int y=heroYPos-this.getCastingRange(); y<heroYPos+this.getCastingRange()+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of attackable grid
						if (Math.abs(heroXPos - x) + Math.abs(heroYPos - y) <= this.getCastingRange()) {
							GridFrame.highlightedMap[x][y] = true;
						}
					}
				}
			}
		}

	}


}
