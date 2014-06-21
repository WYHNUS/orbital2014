package edu.nus.comp.dotagrid.logic;

public class Pair <H, I> {
	
	private H first;
	private I second;
	
	public Pair (H first, I second){
		this.first = first;
		this.second = second;
	}
	
	public H getFirst(){ return this.first;}
	
	public I getSecond(){return this.second;}
	
	public void setFirst(H first) {this.first = first;}
	
	public void setSecond(I second) {this.second = second;}
	
}
