package edu.nus.comp.dotagrid.logic;

public class ItemDatabase {
	public static final int TOTAL_ITEM_NUMBER = 8;
	
	public Item[] itemDatabase = new Item[TOTAL_ITEM_NUMBER];
	
	public ItemDatabase(){
		/*
		 * String itemName, 
		 * int cost, 
		 * int requiredMPPerUse, 
		 * int requiredHPPerUse, 
		 * boolean isReusable, 
		 * 
		 * double addStrength, 
		 * double addAgility, 
		 * double addIntelligence, 
		 * int addHP, 
		 * int addMP, 
		 * double addHPGainPerRound, 
		 * double addMPGainPerRound, 
		 * 
		 * double addPhysicalDefence, 
		 * double addMagicResistance,
		 * double addPhysicalAttack, 
		 * double addPhysicalAttackSpeed, 
		 * int addPhysicalAttackArea, 
		 * int addMovementSpeed)
		 */
		
		Item branches = new Item("branches", 53, 0, 0, true, 
				1, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[0] = branches;	
		
		Item gauntlets = new Item("gauntlets", 150, 0, 0, true, 
				3, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[1] = gauntlets;	
		
		Item slippers = new Item("slippers", 150, 0, 0, true, 
				0, 3, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[2] = slippers;
		
		Item mantle = new Item("mantle", 150, 0, 0, true, 
				0, 0, 3, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[3] = mantle;
		
		Item circlet = new Item("circlet", 185, 0, 0, true, 
				2, 2, 2, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[4] = circlet;	
		
		Item beltOfStrength = new Item("beltOfStrength", 450, 0, 0, true, 
				6, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[5] = beltOfStrength;	
		
		Item bootsOfElves = new Item("bootsOfElves", 450, 0, 0, true, 
				0, 6, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[6] = bootsOfElves;
		
		Item robeOfTheMagi = new Item("robeOfTheMagi", 450, 0, 0, true, 
				0, 0, 6, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[7] = robeOfTheMagi;
	}
}
