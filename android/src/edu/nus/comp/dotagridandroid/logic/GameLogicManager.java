package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;

import edu.nus.comp.dotagridandroid.ui.renderers.Closeable;

public class GameLogicManager implements Closeable {
	private Map<String, Object> gameSetting = new ConcurrentHashMap<>();
	private Map<String, GameState>gameStates = new ConcurrentHashMap<>();

	public GameLogicManager() {
		gameSetting.put("DISPLAY_ANTI_ALIAS_SAMPLINGS", 4);
		GameState current = new GameState();
		final int width = 20, height = 20;
		current.setGridHeight(height);
		current.setGridWidth(width);
		final float[] terrain = new float[width * height];
		Random r = new Random();
		for (int i = 0; i < width * height; i++)
			terrain[i] = r.nextFloat();
//		current.setTerrain(new float[]{
//				0, 0, 0, 0, 0, 0, 0, 0,
//				0, 0, 0, 0, 0, 0, 0, 0,
//				0, 0, 0, 0, 0, 0, 0, 0,
//				0, 0, 0, 0, 0, 0, 0, 0,
//				1, 1, 0, 1, 1, 0, 1, 1,
//				0, 0, 0, 0, 0, 0, 0, 0,
//				0, 0, 0, 0, 0, 0, 0, 0,
//				0, 0, 0, 0, 0, 0, 0, 0
//		});
//		current.setTerrain(new float[20 * 20]);
		current.setTerrain(terrain);
		gameStates.put("Current", current);	// dummy
	}

	public Object getGameSetting(String key) {
		return gameSetting.get(key);
	}

	public GameState getGameState(String key) {
		return gameStates.get(key);
	}
	
	@Override
	public void close() {
		// app will close, save states
	}

}
