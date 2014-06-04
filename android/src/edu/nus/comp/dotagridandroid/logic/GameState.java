package edu.nus.comp.dotagridandroid.logic;

public class GameState {
	private int gridWidth, gridHeight;
	private float[] terrain;

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public float[] getTerrain() {
		return terrain;
	}

	public void setTerrain(float[] terrain) {
		this.terrain = terrain;
	}
}
