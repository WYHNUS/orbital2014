package edu.nus.comp.dotagrid.logic;

public class SummonCharacter extends Character {
	
	private int duration;
	private int currentDuration;
	
	public static final int LINE_CREEP_ATTACK_PRIORITY = 4;
	
	public SummonCharacter(String name, int bountyExp, int bountyMoney, int sight, 
			int startingHP, int startingMP,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance, int startingMovementSpeed,
			int actionPoint, int teamNumber, int duration) {
		
		super(name, bountyMoney, bountyExp, sight, startingHP, startingMP, 
				startingPhysicalAttack, startingPhysicalAttackArea, startingPhysicalAttackSpeed, 
				startingPhysicalDefence, startingMagicResistance, startingMovementSpeed, 
				actionPoint, LINE_CREEP_ATTACK_PRIORITY, teamNumber);
		
		this.duration = duration;
		this.setCurrentDuration(duration);
		this.setCharacterImage("summonCharacters", this.getName());
	}

	public SummonCharacter(SummonCharacter summonChara) {

		super(summonChara.getName(), summonChara.getBountyMoney(), summonChara.getBountyExp(), summonChara.getSight(),
				summonChara.getStartingHP(), summonChara.getStartingMP(), summonChara.getStartingPhysicalAttack(),
				summonChara.getStartingPhysicalAttackArea(), summonChara.getStartingPhysicalAttackSpeed(),
				summonChara.getStartingPhysicalDefence(), summonChara.getStartingMagicResistance(), summonChara.getStartingMovementSpeed(),
				summonChara.getMaxActionPoint(), LINE_CREEP_ATTACK_PRIORITY, summonChara.getTeamNumber());
		
		this.setCurrentAttackPriority(summonChara.getCurrentAttackPriority());
		this.setDuration(summonChara.getDuration());
		this.setCurrentDuration(summonChara.getCurrentDuration());
		this.setCharacterImage("summonCharacters", this.getName());
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCurrentDuration() {
		return currentDuration;
	}

	public void setCurrentDuration(int currentDuration) {
		this.currentDuration = currentDuration;
	}
	
}
