package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;


public class NeutralCreep extends Character{
	public static final int neutralCreepTeamNumber = 0;
	public static int neutralCreepStartingSight = 6;
	
	public static final int NEUTRAL_CREEP_ATTACK_PRIORITY = 4;
	
	private ArrayList<int[]> AItargetPos;
	
	private boolean moveBack = false;
	
	public NeutralCreep(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP, double HPGainPerRound, double MPGainPerRound,
			double startingPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance,
			int startingMovementSpeed, int startingAP) {
		
		super(name, bountyMoney, bountyExp, neutralCreepStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed, startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, startingAP, NEUTRAL_CREEP_ATTACK_PRIORITY, neutralCreepTeamNumber);
		
		// set regeneration rate
		this.setHPGainPerRound(HPGainPerRound);
		this.setMPGainPerRound(MPGainPerRound);
		
		// set image
		this.setCharacterImage("NeutralCreeps", this.getName());		
		
	}

	public boolean isMoveBack() {
		return moveBack;
	}

	public void setMoveBack(boolean moveBack) {
		this.moveBack = moveBack;
	}

	public ArrayList<int[]> getAItargetPos() {
		return AItargetPos;
	}

	public void setAItargetPos(ArrayList<int[]> aItargetPos) {
		AItargetPos = aItargetPos;
	}
	
}
