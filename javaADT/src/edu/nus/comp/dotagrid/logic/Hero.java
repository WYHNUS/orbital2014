package edu.nus.comp.dotagrid.logic;
import java.util.*;

public class Hero implements Drawable, Character {
	public int hp, mp;
	public int attack, attackArea, attackSpeed;
	public int strength, agility, intelligence;
	public int strengthGrowth, agilityGrowth, intelligenceGrowth;
	public int speed, actionPoint;
	public int defense, magicResist;
	public int money;
	public int level;
	public String name;
	public ArrayList<Skill> skills;
	public ArrayList<Item> items;
	
	@Override
	public void flipClock () {
		;
	}
	
	@Override
	public void draw(DrawDelegate delegate) {
		// TODO Auto-generated method stub
	}
}
