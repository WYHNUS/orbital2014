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
		current.setGridHeight(5);
		current.setGridWidth(5);
		current.setTerrain(new float[]{
			0, 0, 0, 0, 0,
			1, 1, 1, 1, 1,
			2, 2, 2, 2, 2,
			3, 3, 3, 3, 3,
			4, 4, 4, 4, 4,
		});
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
