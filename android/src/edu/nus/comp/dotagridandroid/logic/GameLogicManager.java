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
//				.1f,.2f,.1f,.3f,.1f,.1f,.2f,.2f,.5f,.7f,.3f,.1f,0,.4f,.9f,1,
//				.5f,.2f,.3f,.2f,.1f,.1f,0,.9f,.2f,.3f,1,.3f,.2f,.1f,.6f,.7f,
//				.4f,.2f,.8f,.6f,.3f,.2f,.1f,.6f,.6f,.3f,0,0,.1f,.4f,.8f,.6f,
//				.5f,.2f,.1f,.1f,.1f,.4f,.8f,.6f,.9f,.9f,.3f,.7f,.6f,.8f,0,1,
//				.2f,.1f,.1f,.6f,.5f,.9f,0,.1f,.2f,.6f,.9f,1,.2f,.2f,.6f,.3f,
//				.1f,.1f,.1f,.1f,.1f,.1f,.3f,.2f,.9f,.8f,.2f,0,0,.1f,.2f,.2f,
//				.1f,.2f,.2f,.1f,0,0,.6f,.9f,.8f,.4f,.5f,.1f,.7f,.7f,.5f,.1f,
//				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
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
