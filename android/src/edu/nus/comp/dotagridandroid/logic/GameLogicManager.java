package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;

import android.content.Context;
import edu.nus.comp.dotagridandroid.Closeable;
import edu.nus.comp.dotagridandroid.appsupport.*;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;

public class GameLogicManager implements Closeable {
	private Map<String, Object> gameSetting = new ConcurrentHashMap<>();
	private Map<String, GameState>gameStates = new ConcurrentHashMap<>();
	private GameState currentState;
	private Context context;
	private SoundEngine se;
	private GameServer currentServer;
	private SceneRenderer applicationSceneRenderer;

	public GameLogicManager(Context context) {
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

	public List<String> getGameList() {
		return Collections.emptyList();
	}
	
	public void initiateSoundEngine() {
		se = AppNativeAPI.createSoundEngine(context);
		se.prepareBufferQueuePlayer();
		// prepare common asset sounds
		prepareAssetSound("glass.ogg", "click");	// click
	}
	
	public void prepareAssetSound(String file, String name) {
		se.prepareAssetPlayer(file, name);
	}
	
	public void playAssetSound(String name) {
		se.setAssetPlayerStop(name);
		se.setAssetPlayerPlayState(name, true);
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
			// TODO set up server according to saved state
			currentServer = new GameServer(GameServer.MODE_SINGLEPLAYER);
			currentState.attachServer(currentServer);
		} else
			currentState = null;
	}
	
	public GameState getCurrentGameState() {
		return currentState;
	}
	
	public void saveGame() {
		// TODO save game states
	}
	
	@Override
	public void close() {
		// app will close, save states
		for (GameState state : gameStates.values())
			state.close();
		if (se != null)
			se.close();
	}
	
	public void setApplicationSceneRenderer(SceneRenderer scene) {
		applicationSceneRenderer = scene;
	}

	public void processEvent(ControlEvent e) {
		if ((e.type & ControlEvent.TYPE_INTERPRETED) == 0)
			return;
		if ("APPLICATION".equals(e.extendedType)) {
			// application data
			applicationSceneRenderer.notifyUpdate(Collections.singletonMap("APPLICATION", (Object) e.data.extendedData));
		} else if (currentState != null)
			currentState.processEvent(e);
	}
}
