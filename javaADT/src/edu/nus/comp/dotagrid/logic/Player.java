package edu.nus.comp.dotagrid.logic;

public class Player {
	private int XPos, YPos;

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

}
