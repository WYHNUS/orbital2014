package edu.nus.comp.dotagrid.logic;

import java.util.ArrayList;

public class NeutralCreep extends Character{
	public static int nertralCreepStartingSight = 6;
	
	public NeutralCreep(String name, int bountyMoney, int bountyExp, int startingHP, int startingMP, double HPGainPerRound, double MPGainPerRound,
			double startingPhysicalAttack, double basicPhysicalAttack, int startingPhysicalAttackArea, double startingPhysicalAttackSpeed, 
			double startingPhysicalDefence, double startingMagicResistance,
			int startingMovementSpeed, int startingAP) {
		
		super(name, bountyMoney, bountyExp, nertralCreepStartingSight, startingHP, startingMP, startingPhysicalAttack,
				startingPhysicalAttackArea, startingPhysicalAttackSpeed,
				startingPhysicalDefence, startingMagicResistance,
				startingMovementSpeed, startingAP, 0);
		
		// set basic physical attack
		this.setBasicPhysicalAttack(basicPhysicalAttack);
		this.setTotalPhysicalAttack(this.getStartingPhysicalAttack() + this.getBasicPhysicalAttack());
		
		// set regeneration rate
		this.setHPGainPerRound(HPGainPerRound);
		this.setMPGainPerRound(MPGainPerRound);
		
		// set image
		this.setCharacterImage("NeutralCreeps", this.getName());		
		
	}
	
}
