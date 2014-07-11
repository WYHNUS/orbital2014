package edu.nus.comp.dotagrid.logic;

public class ItemDatabase {
	public static final int TOTAL_ITEM_NUMBER = 10;
	
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
		
		Item branches = new Item("branches", 53, 0, 0, false, 
				1.0, 1.0, 1.0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[0] = branches;	
		
		Item gauntlets = new Item("gauntlets", 150, 0, 0, false, 
				3.0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[1] = gauntlets;	
		
		Item slippers = new Item("slippers", 150, 0, 0, false, 
				0, 3.0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[2] = slippers;
		
		Item mantle = new Item("mantle", 150, 0, 0, false, 
				0, 0, 3.0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[3] = mantle;
		
		Item circlet = new Item("circlet", 185, 0, 0, false, 
				2.0, 2.0, 2.0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[4] = circlet;	
		
		Item beltOfStrength = new Item("beltOfStrength", 450, 0, 0, false, 
				6.0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[5] = beltOfStrength;	
		
		Item bootsOfElves = new Item("bootsOfElves", 450, 0, 0, false, 
				0, 6.0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[6] = bootsOfElves;
		
		Item robeOfTheMagi = new Item("robeOfTheMagi", 450, 0, 0, false, 
				0, 0, 6.0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0);
		itemDatabase[7] = robeOfTheMagi;
		
		Item clarity = new Item("clarity", 50, true);
		clarity.setDiscardAfterUse(true);
		clarity.setUsableTime(1);
		clarity.setCastingRange(3);
		itemDatabase[8] = clarity;
		
		Item flask = new Item("flask", 115, true);
		flask.setDiscardAfterUse(true);
		flask.setUsableTime(1);
		flask.setCastingRange(3);
		itemDatabase[9] = flask;
	}
}
