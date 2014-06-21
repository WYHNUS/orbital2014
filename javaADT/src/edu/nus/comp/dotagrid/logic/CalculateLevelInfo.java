package edu.nus.comp.dotagrid.logic;

public class CalculateLevelInfo {
	
	private int level;

	public CalculateLevelInfo(int experience) {
		/*level - Experience
		 * 1 : 200
		 * 2 : 500
		 * 3 : 900
		 * 4 : 1400
		 * 5 : 2000
		 * 6 : 2700
		 * 7 : 3500
		 * 8 : 4400
		 * 9 : 5400
		 * 10 : 6500
		 * 11 : 7700
		 * 12 : 9000
		 * 13 : 10400
		 * 14 : 11900
		 * 15 : 13500
		 * 16 : 15200
		 * 17 : 17000
		 * 18 : 18900
		 * 19 : 20900
		 * 20 : 23000
		 * 21 : 25200
		 * 22 : 27500
		 * 23 : 29900
		 * 24 : 32400
		 * 25 : +
		 * */
		
		if (experience >= 0 && experience < 200) {
			level = 1;
		} 
		
		if (experience >= 200 && experience < 500) {
			level = 2;
		}
		
		if (experience >=500 && experience < 900) {
			level = 3;
		}
		
		if (experience >= 900 && experience < 1400) {
			level = 4;
		} 
		
		if (experience >= 1400 && experience < 2000) {
			level = 5;
		}
		
		if (experience >= 2000 && experience < 2700) {
			level = 6;
		}
		
		if (experience >= 2700 && experience < 3500) {
			level = 7;
		} 
		
		if (experience >= 3500 && experience < 4400) {
			level = 8;
		} 
		
		if (experience >= 4400 && experience < 5400) {
			level = 9;
		} 
		
		if (experience >= 5400 && experience < 6500) {
			level = 10;
		} 
		
		if (experience >= 6500 && experience < 7700) {
			level = 11;
		} 
		
		if (experience >= 7700 && experience < 9000) {
			level = 12;
		} 
		
		if (experience >= 9000 && experience < 10400) {
			level = 13;
		} 
		
		if (experience >= 10400 && experience < 11900) {
			level = 14;
		} 
		
		if (experience >= 11900 && experience < 13500) {
			level = 15;
		} 
		
		if (experience >= 13500 && experience < 15200) {
			level = 16;
		} 
		
		if (experience >= 15200 && experience < 17000) {
			level = 17;
		} 
		
		if (experience >= 17000 && experience < 18900) {
			level = 18;
		} 
		
		if (experience >= 18900 && experience < 20900) {
			level = 19;
		} 
		
		if (experience >= 20900 && experience < 23000) {
			level = 20;
		} 
		
		if (experience >= 23000 && experience < 25200) {
			level = 21;
		} 
		
		if (experience >= 25200 && experience < 27500) {
			level = 22;
		} 
		
		if (experience >= 27500 && experience < 29900) {
			level = 23;
		} 

		if (experience >= 29900 && experience < 32400) {
			level = 24;
		} 
		
		if (experience >= 32400) {
			level = 25;
		} 
	}

	
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
	public static int calculateBountyExp(int level){
		/*
		 * level 1 - 5 : 
		 * bounty exp : 100 / 120 / 160 / 220 / 300
		 * 
		 * level 6 - 25 : 
		 * each level increase 100
		 * 
		 * */
		int bountyExp = 0;
		
		if (level == 1) {
			bountyExp = 100;
		}
		
		if (level == 2) {
			bountyExp =  120;
		}
		
		if (level == 3) {
			bountyExp =  160;
		}
		
		if (level == 4) {
			bountyExp =  220;
		}
		
		if (level >= 5 && level <= 25) {
			bountyExp =  300 + 100 * (level - 5);
		}
		
		return bountyExp;
	}
	
	
	
	public static int calculateDeathCount(int level){
		// when a hero is killed, he will revive after (2 * level) turns
		int deathcountConstant = 2;
		return deathcountConstant * level;
	}

}
