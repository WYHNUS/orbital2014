package edu.nus.comp.dotagrid.logic;

public class Tree extends Character{

	public Tree() {
		super("Tree", 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		// Tree has all zero attribute
		this.setAttackable(false);
		this.setCharacterImage("WorldMap/terrian/", "Tree");
	}

}
