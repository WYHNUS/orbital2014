package edu.nus.comp.dotagrid.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class GameButton {
	
	private Graphics g;
	private String string;
	private Image image;
	
	private int xPos;
	private int yPos; 
	private int width;
	private int height;
	
	
	public GameButton(Graphics g, String string, Image image, int xPos, int yPos, int width, int height) {
		this.g = g; 
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
	
	
	public void drawRect() {
		g.drawRect(xPos, yPos, width, height);
	}
	
	public void fillRect(){
		g.fillRect(xPos, yPos, width, height);
	}
	
	public void setColor(Color color) {
		g.setColor(color);
	}
	
	public void drawString() {
		g.drawString(string, xPos + 15, yPos + 20);
	}
	
	public void drawImage() {
		g.drawImage(image, xPos, yPos, width, height, null);
	}
	
}
