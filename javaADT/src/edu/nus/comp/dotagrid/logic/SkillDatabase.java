package edu.nus.comp.dotagrid.logic;


public class SkillDatabase {
	
	public SkillDatabase(){
	}

	public static Skill getSkillTeleportation(){
		// 1 : teleport
		int skillType = 1;

		// usedMP, usedActionPoint, castRange, coolDownRounds
		Object[] attributes = {50, 80, 200, 10};
		Skill teleportation = new Skill(skillType, "Teleportation", attributes);
		
		return teleportation;
	}

	public static Skill getSkillSprout(){
		// 2 : summon creatures
		int skillType = 2;

		// usedMP, usedActionPoint, castRange, coolDownRounds, summonCharacter, range
		SummonCharacter summonTree = new SummonCharacter("Tree", 5, 10, 1, 500, 0, 0, 0, 0, 
				1.0, 0, -99999, 0, 0, 2);
		Object[] attributes = {100, 50, 6, 5, summonTree, 2};
		Skill sprout = new Skill(skillType, "Sprout", attributes);
		
		return sprout;
	}
	
	public static Skill getSkillWrathOfNature(){
		// 3 : hit a maximum number of enemies in sight
		int skillType = 3;
		
		// usedMP, usedActionPoint, castRange, coolDownRounds,  magicalDanmage, maximumHitUnits
		Object[] attributes = {175, 30, 200, 20, 140, 16};
		Skill wrathOfNature = new Skill(skillType, "Wrath Of Nature", attributes);
		
		return wrathOfNature;
	}
}
