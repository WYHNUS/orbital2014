package edu.nus.comp.dotagrid.logic;

public class SummonCharacter extends Character {
	
	private int duration;
	
	public SummonCharacter(String name, int bountyExp, int bountyMoney, int sight, 
			int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance, int startingMovementSpeed,
			int actionPoint, int teamNumber, int duration) {
		
		super(name, bountyMoney, bountyExp, sight, startingHP, startingMP, 
				startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, 
				startingPhysicalDefence, startingMagicResistance, startingMovementSpeed, 
				actionPoint, teamNumber);
		
		this.duration = duration;
		this.setCharacterImage("summonCharacters", this.getName());
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
