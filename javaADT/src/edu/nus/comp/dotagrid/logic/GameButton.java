package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class GameButton{
	
	private String string;
	private Image image;
	
	private boolean isClicked;
	private boolean isReadyToDrawImage;
	
	private int actionNumber;
	
	private int xPos = 0;
	private int yPos = 0; 
	private int width = 0;
	private int height = 0;
	
	
	// constructor
	public GameButton(String string, Image image, int xPos, int yPos, int width, int height) {
		this.string = string;
		this.image = image;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
		isClicked = false;
		isReadyToDrawImage = false;
	}
	
	
	// accessor and mutator
	public int getActionNumber() {
		return this.actionNumber;
	}
	
	public void setActionNumber(int actionNumber) {
		this.actionNumber = actionNumber;
	}
	
	public String getString(){
		return this.string;
	}
	
	public void setString(String string){
		this.string = string;
	}
	
	public void setImage(Image image){
		this.image = image;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	public void setColor(Graphics g, Color color) {
		g.setColor(color);
	}
	
	public boolean getIsReadyToDrawImage(){
		return isReadyToDrawImage;
	}
	
	public void setIsReadyToDrawImage(boolean state){
		this.isReadyToDrawImage = state;
	}
	
	
	// other methods
	public void fillRect(Graphics g){
		g.fillRect(xPos, yPos, width, height);
	}
	
	
	public void drawRect(Graphics g) {
		g.drawRect(xPos, yPos, width, height);
	}	
	
	public void drawString(Graphics g) {
		int xPosAllign = 15;
		int yPosAllign = 20;
		g.drawString(string, xPos + xPosAllign, yPos + yPosAllign);
	}
	
	public void drawImage(Graphics g) {
		g.drawImage(image, xPos, yPos, width, height, null);
	}
	
	
	public boolean checkEvent(int handXPos, int handYPos) {		
		// within x-axis position
		if (handXPos > xPos && handXPos < (xPos + width)) {
			// within y-axis position
			if(handYPos > yPos && handYPos < (yPos + height)) {
				// do the mouse event
				isClicked = true;
			} 
		}
		
		return isClicked;
	}
	
	public void resetBoolean(){
		this.isClicked = false;
	}
	
	
	public void actionPerformed(int actionNumber) {
		System.out.println(actionNumber + " game button action is invoked!");
		new GameButtonActions(actionNumber);
	}

	
}
