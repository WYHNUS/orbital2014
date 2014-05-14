package edu.nus.comp.dotagrid.logic;
import java.util.*;
public abstract class WorldMap {
	private int width, height;
	public WorldMap(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public abstract Cell getCell(int x, int y);
	public abstract List<Cell> findPath (Cell start, Cell end);
}
