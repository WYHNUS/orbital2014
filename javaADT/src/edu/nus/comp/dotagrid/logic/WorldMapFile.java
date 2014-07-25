package edu.nus.comp.dotagrid.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class WorldMapFile {
	InputStreamReader reader;
	
	Scanner scanner;
	
	WorldMap worldMap = new WorldMap();
	
	public WorldMap getWorldMap(){
		try{
			reader = new InputStreamReader(getClass().getResourceAsStream("/edu/nus/comp/dotagrid/res/WorldMap/Map/worldMap.dota"));
			
			scanner = new Scanner(reader);
			
			worldMap.map = new int[GridFrame.ROW_NUMBER][GridFrame.COLUMN_NUMBER];
			
			int x = 0;
			int y = 0;
			
			while (scanner.hasNext()) {
				worldMap.map[x][y] = scanner.nextInt();
				
				if (x < GridFrame.ROW_NUMBER - 1) {
					x++;
				} else {
					y++;
					
					x = 0;
				}
			}
			
			return worldMap;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
