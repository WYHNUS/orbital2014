package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;

import android.content.Context;
import edu.nus.comp.dotagridandroid.Closeable;
import edu.nus.comp.dotagridandroid.appsupport.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class GameLogicManager implements Closeable {
	private Map<String, Object> gameSetting = new ConcurrentHashMap<>();
	private Map<String, GameState>gameStates = new ConcurrentHashMap<>();
	private GameState currentState;
	private Context context;
	private SoundEngine se;

	public GameLogicManager(Context context) {
		se = AppNativeAPI.createSoundEngine();
		se.prepareBufferQueuePlayer();
		this.context = context;
		gameSetting.put("DISPLAY_ANTI_ALIAS_SAMPLINGS", 4);
		GameState current = new GameState(null);
		final int width = 100, height = 100;
		current.setGridHeight(height);
		current.setGridWidth(width);
		final float[] terrain = new float[width * height];
		Random r = new Random();
//		for (int i = 0; i < width * height; i++)
//			terrain[i] = r.nextFloat();
		for (int i = 0; i < width; i++)
			terrain[height / 2 * width + i] = r. nextFloat();
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
//		current.setTerrain(new float[]{
//				0,0,0,0,
//				0,0,0,0,
//				1,1,1,1,
//				0,0,0,0
//		});
//		current.setTerrain(new float[]{
//				0,0,0,
//				1,1,1,
//				0,0,0
//		});
//		current.setTerrain(new float[width * height]);
		current.setTerrain(terrain);
		gameStates.put("Current", current);	// dummy
	}

	public Object getGameSetting(String key) {
		return gameSetting.get(key);
	}

	public GameState getGameState(String key) {
		return gameStates.get(key);
	}
	
	public void setCurrentGameState(String key) {
		if (currentState != null && currentState.isInitialised())
			currentState.close();
		if (gameStates.containsKey(key)) {
			currentState = gameStates.get(key);
			currentState.setContext(context);
		} else
			currentState = null;
	}
	
	public GameState getCurrentGameState() {
		return currentState;
	}
	
	@Override
	public void close() {
		// app will close, save states
		for (GameState state : gameStates.values())
			state.close();
		se.close();
	}

	public static class GameStateUpdateDelegate {
		public void updateState() {
		}
	}

	public void processEvent(ControlEvent e) {
		if ((e.type & ControlEvent.TYPE_INTERPRETED) == 0)
			return;
		if (e.extendedType.equals("APPLICATION")) {
			// application data
		}
		if (currentState != null)
			currentState.processEvent(e);
	}
}
