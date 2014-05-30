package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameButton extends Component{
	
	private String string;
	private Image image;
	
	private int xPos = 0;
	private int yPos = 0; 
	private int width = 0;
	private int height = 0;
	
	
	public GameButton(String string, Image image, int xPos, int yPos, int width, int height) {
		this.string = string;
		this.image = image;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
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
	
	public void setColor(Graphics g, Color color) {
		g.setColor(color);
	}
	
	
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
		boolean isClicked = false;
		
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
	
	
	public void actionPerformed(){
		System.out.println(string + " has been invoked!");
	}


	
}
