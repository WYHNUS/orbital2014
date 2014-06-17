package edu.nus.comp.dotagrid.logic;

public class LineCreep extends Character{
	
	public static int lineCreepActionPoint = 100;
	public static int lineCreepStartingSight = 6;
	
	private int level;

	public LineCreep(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP, 
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance,
			int startingMovementSpeed, int teamNumber) {
		
		super(name, bountyMoney, bountyExp, lineCreepStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, lineCreepActionPoint, teamNumber);
		
		// set image
		this.setCharacterImage("creeps", this.getName());
		
		// set level
		this.setLevel(1);
		
	}
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		// starting level is the minimum level (level 1)
		if (level <= 1) {
			this.level = 1;
		} else {
			this.level = level;
		}
	}

}
