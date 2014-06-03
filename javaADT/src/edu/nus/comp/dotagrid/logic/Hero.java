package edu.nus.comp.dotagrid.logic;

import java.util.*;



public class Hero extends Character{
	
	private int startingStrength, agility, intelligence;
	private double strengthGrowth, agilityGrowth, intelligenceGrowth;
	
	private int money;
	private int level;
	private int experience;

	private ArrayList<Skill> skills;
	private ArrayList<Item> items;
	
	
	public Hero(String heroName, int startingHP, int startingMP, int startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, double startingPhysicalDefense, double startingMagicResistance, int actionPoint,
			int strength, int agility, int intelligence, double strengthGrowth, double agilityGrowth, double intelligenceGrowth,
			int level, int experience) {
		super(heroName, startingHP, startingMP, startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, startingPhysicalDefense, startingMagicResistance, actionPoint);
		
		// initialize attributes specific to heros
		
	}

}
