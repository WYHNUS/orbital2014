package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class GameButton {
	
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
		g.drawString(string, xPos + 15, yPos + 20);
	}
	
	public void drawImage(Graphics g) {
		g.drawImage(image, xPos, yPos, width, height, null);
	}
	
}
