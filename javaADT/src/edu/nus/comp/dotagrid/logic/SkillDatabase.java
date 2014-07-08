package edu.nus.comp.dotagrid.logic;


public class SkillDatabase {
	public static int TOTAL_SKILL_NUMBER = 2;
	
	public static Skill[] skillDatabase = new Skill[TOTAL_SKILL_NUMBER]; 
	
	public SkillDatabase(){
		
		{
			// 1 : teleport
			int skillType = 1;
			
			// usedMP, usedActionPoint, castRange, coolDownRounds
			
			{
				Object[] attributes = {50, 80, 200, 10};
				Skill teleportation = new Skill(skillType, "Teleportation", attributes);
				skillDatabase[0] = teleportation;
			}
			
		} // end skills for case 1
		
		
		{
			// 2 : summon creatures
			int skillType = 2;
			
			// usedMP, usedActionPoint, castRange, coolDownRounds, summonCharacter, range
			
			{
				SummonCharacter summonTree = new SummonCharacter("Tree", 5, 10, 1, 500, 0, 0, 0, 0, 
						1.0, 0, -99999, 0, 0, 2);
				Object[] attributes = {100, 50, 6, 5, summonTree, 2};
				Skill sprout = new Skill(skillType, "Sprout", attributes);
				skillDatabase[1] = sprout;
			}
			
		} // end skills for case 1
		
	}
}
