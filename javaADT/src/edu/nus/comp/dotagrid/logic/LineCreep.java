package edu.nus.comp.dotagrid.logic;

public class LineCreep extends Character{
	
	public static int lineCreepActionPoint = 100;
	public static int lineCreepStartingSight = 6;
	
	public static int levelSentinel = 0;
	public static int levelScourge = 0;

	public LineCreep(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP, 
			double startingPhysicalAttack, double basicPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance,
			int startingMovementSpeed, int teamNumber) {
		
		super(name, bountyMoney, bountyExp, lineCreepStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, lineCreepActionPoint, teamNumber);
		
		// set basic physical attack
		this.setBasicPhysicalAttack(basicPhysicalAttack);
		
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack() + this.getBasicPhysicalAttack());
		
		// set image
		this.setCharacterImage("creeps", this.getName());		
	}
	
}
