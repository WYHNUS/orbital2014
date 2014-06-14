package edu.nus.comp.dotagridandroid.logic;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.*;
import android.content.Context;
import android.graphics.*;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import edu.nus.comp.dotagridandroid.ui.event.*;

public class GameState implements Closeable {
	private int gridWidth, gridHeight;
	private float[] terrain;
	private Context context;
	private boolean initialised = false, initialising = false;
	private Thread initialisationProcess;
	private SceneRenderer currentSceneRenderer;
	private Map<String, Character> chars;
	private Map<String, Object> objs;
	private Map<String, int[]> objPositions;
	private Map<String, FloatBuffer[]> objModels;
	private Map<String, Texture2D> objTextures;
	// game rule object
	private GameMaster gameMaster;
	private String playerCharacter;
	public GameState() {
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public void initialise() {
		if (initialised || initialising)
			return;
		chars = new ConcurrentHashMap<>();
		objs = new ConcurrentHashMap<>();
		objPositions = new ConcurrentHashMap<>();
		objModels = new ConcurrentHashMap<>();
		objTextures = new ConcurrentHashMap<>();
		initialisationProcess = new Thread() {
			@Override
			public void run() {
				initialising = true;
				gameMaster = new GameMaster();
				// TODO load characters
				chars.put("MyHero", new Hero("MyHero", "strength",
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100));
				chars.put("MyHero2", new Hero("MyHero", "strength",
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100,
						100));
				objPositions.put("MyHero", new int[]{0, 0});
				objPositions.put("MyHero2", new int[]{19,19});
				// TODO load character models
				chars.get("MyHero").setCharacterImage("MyHeroModel");	// actually this refers to an entry in objModels called MyHeroModel and a texture named MyHeroModel
				chars.get("MyHero2").setCharacterImage("MyHeroModel");
				objModels.put("MyHeroModel", new FloatBuffer[]{
						// 0: vertex
						BufferUtils.createFloatBuffer(36 * 4).put(new float[]{
								-1,1,1,1, -1,-1,1,1, 1,-1,1,1, 1,-1,1,1, 1,1,1,1, -1,1,1,1,
								1,1,1,1, 1,-1,1,1, 1,-1,-1,1, 1,-1,-1,1, 1,1,-1,1, 1,1,1,1,
								1,1,-1,1, 1,-1,-1,1, -1,-1,-1,1, -1,-1,-1,1, -1,1,-1,1, 1,1,-1,1,
								-1,1,-1,1, -1,-1,-1,1, -1,-1,1,1, -1,-1,1,1, -1,1,1,1, -1,1,-1,1,
								-1,1,-1,1, -1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,-1,1, -1,1,-1,1,
								-1,-1,1,1, -1,-1,-1,1, 1,-1,-1,1, 1,-1,-1,1, 1,-1,1,1, -1,-1,1,1
						}),
						// 1: texture
						BufferUtils.createFloatBuffer(36 * 2).put(new float[]{
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
								0,0, 0,1, 1,1, 1,1, 1,0, 0,0,
						}),
						// 2: normal - vec4 - important!
						BufferUtils.createFloatBuffer(36 * 4).put(new float[]{
								0,0,1,0, 0,0,1,0, 0,0,1,0, 0,0,1,0, 0,0,1,0, 0,0,1,0,
								1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0,
								0,0,-1,0, 0,0,-1,0, 0,0,-1,0, 0,0,-1,0, 0,0,-1,0, 0,0,-1,0,
								-1,0,0,0, -1,0,0,0, -1,0,0,0, -1,0,0,0, -1,0,0,0, -1,0,0,0,
								0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0,
								0,-1,0,0, 0,-1,0,0, 0,-1,0,0, 0,-1,0,0, 0,-1,0,0, 0,-1,0,0
						})
				});
				initialised = true;
				initialising = false;
			}
		};
		initialisationProcess.start();
		// load resources
		System.gc();
		Bitmap tempBitmap = BitmapFactory.decodeResource(context.getResources(), edu.nus.comp.dotagridandroid.R.drawable.reimu_original);
		Texture2D tex = new Texture2D (tempBitmap);
		tempBitmap.recycle();
		objTextures.put("MyHeroModel", tex);
		objTextures.put("GridMapBackground", tex);
		try {initialisationProcess.join();} catch (Exception e) {e.printStackTrace();}
		initialising = false;
	}
	
	@Override
	public void close() {
		// release resources
		gameMaster = null;
		chars = null;
		objs = null;
		objPositions = null;
		objModels = null;
		objTextures = null;
		initialised = false;
	}
	
	public boolean isInitialised() {
		try {initialisationProcess.join();} catch (Exception e) {e.printStackTrace();}
		return initialised;
	}
	
	public void startTimer() {
		if (!initialised)
			return;
	}
	
	public void stopTimer() {
		if (!initialised)
			return;
	}

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
		return isInitialised() ? terrain : null;
	}

	public void setTerrain(float[] terrain) {
		this.terrain = terrain;
	}
	
	// characters
	public void setPlayerCharacter(String name) {
		if (chars.containsKey(name))
			playerCharacter = name;
	}
	public String getPlayerCharacter() {
		return playerCharacter;
	}
	
	public Map<String, Character> getCharacters() {
		return Collections.unmodifiableMap(chars);
	}
	
	public Map<String, int[]> getCharacterPositions() {
		return Collections.unmodifiableMap(objPositions);
	}
	
	public void setCharacterPositions(String name, int[] position) {
		if (chars.containsKey(name) && position != null && position.length == 2)
			objPositions.put(name, position.clone());
	}
	
	public FloatBuffer[] getCharacterModel(String name) {
		return objModels.get(name).clone();
	}
	
	public Texture2D getModelTexture(String name) {
		return objTextures.get(name);
	}
	
	// interface interactions
	public void setCurrentSceneRenderer (SceneRenderer renderer) {
		currentSceneRenderer = renderer;
	}
	public void notifyAction (ControlEvent e) {
		if (currentSceneRenderer != null)
			;
	}

	public void processEvent(ControlEvent e) {
		// TODO apply rules
		switch (e.extendedType) {
		// interface
		case "ChooseGrid":
			gameMaster.applyRule(this, "ChooseGrid", e.data.extendedData);
			currentSceneRenderer.passEvent(e);
			break;
		case "RequestItemList":
			break;
		case "RequestActionList":
			break;
		case "Cancel":
			notifyAction(e);	// bounce back
			break;
		// game action
		case "GameAction":
			// send to game master
			break;
		case "GamePause":
			// pause game
			break;
		case "GameResume":
			break;
		case "GameSave":
			break;
		case "GameExit":
			break;
		}
		/// Use Hard code game rules
	}
}
