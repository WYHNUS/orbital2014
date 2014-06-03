package edu.nus.comp.dotagrid.logic;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;


public class Hero extends Character{
	
	private String textureFile = "";
	private Image texture;
	
	private String name;
	
	private int hp, mp;
	private int physicalAttack, physicalAttackArea;
	private double physicalAttackSpeed;
	
	private int strength, agility, intelligence;
	private double strengthGrowth, agilityGrowth, intelligenceGrowth;
	
	private int actionPoint;
	private double physicalDefense, magicResistance;
	
	private int money;
	private int level;
	private int experience;

	private ArrayList<Skill> skills;
	private ArrayList<Item> items;
	
	
	public Hero(int hp, int mp, int physicalAttack, int physicalAttackArea,
			double physicalAttackSpeed, double physicalDefense,
			double magicResistance, int actionPoint) {
		super(hp, mp, physicalAttack, physicalAttackArea, physicalAttackSpeed,
				physicalDefense, magicResistance, actionPoint);
		// TODO Auto-generated constructor stub
	}
	
	public Hero getTextureFile(String str) {
		this.textureFile = str;
		
		this.texture = new ImageIcon("res/tower/" + this.textureFile + ".gif").getImage();
		
		return null;
	}

}
