package edu.nus.comp.dotagrid.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class WorldMapFile {
	FileInputStream file;
	InputStreamReader reader;
	
	Scanner scanner;
	
	WorldMap worldMap = new WorldMap();
	
	public WorldMap getWorldMap(){
		try{
			file = new FileInputStream("res/WorldMap/Map/Map.dota");
			reader = new InputStreamReader(file);
			
			scanner = new Scanner(reader);
			
			worldMap.map = new int[Screen.ROW_NUMBER][Screen.COLUMN_NUMBER];
			
			int x = 0;
			int y = 0;
			
			while (scanner.hasNext()) {
				worldMap.map[x][y] = scanner.nextInt();
				
				if (x < Screen.ROW_NUMBER - 1) {
					x++;
				} else {
					y++;
					
					x = 0;
				}
			}
			
			return worldMap;
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
