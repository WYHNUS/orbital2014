package edu.nus.comp.dotagridandroid.logic;

public class Tree extends GameCharacter {

	public Tree() {
		super("Tree", 0, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0, 0, 0, Integer.MAX_VALUE, 0, 1, GameCharacter.TEAM_NEUTURAL);
		setObjectType(GAMEOBJECT_TYPE_TREE);
	}

}
