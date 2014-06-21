package edu.nus.comp.dotagrid.logic;


public class SkillDatabase {
	public static int TOTAL_SKILL_NUMBER = 1;
	
	public static Skill[] skillDatabase = new Skill[TOTAL_SKILL_NUMBER]; 
	
	public SkillDatabase(){
		
		{
			// 1 : teleport
			int skillType = 1;
			
			// usedMP, usedActionPoint, castRange, coolDownRounds
			
			{
				int[] attributes = {50, 80, 200, 10};
				Skill teleportation = new Skill(skillType, "Teleportation", attributes);
				skillDatabase[0] = teleportation;
			}
			
		} // end skills for case 1
		
	}
}
