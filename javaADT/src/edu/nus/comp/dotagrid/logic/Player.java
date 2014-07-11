package edu.nus.comp.dotagrid.logic;

public class Player {
	private int XPos, YPos;
	private static int teamNumber;
	public static int invokedPlayerSkillIndex;
	public static int invokedPlayerItemIndex;

	// constructor
	public Player(int XPos, int YPos){
		this.XPos = XPos;
		this.YPos = YPos;
	}
	
	public int getXPos() {
		return XPos;
	}

	public void setXPos(int xPos) {
		XPos = xPos;
	}

	public int getYPos() {
		return YPos;
	}

	public void setYPos(int yPos) {
		YPos = yPos;
	}

	public static int getTeamNumber() {
		return teamNumber;
	}

	public static void setTeamNumber(int teamNumber) {
		Player.teamNumber = teamNumber;
	}

}
