package edu.nus.comp.dotagridandroid.logic;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;
import edu.nus.comp.dotagridandroid.Closeable;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import edu.nus.comp.dotagridandroid.ui.event.*;
import edu.nus.comp.dotagridandroid.appsupport.*;

public class GameState implements Closeable {
	private int gridWidth, gridHeight;
	private String packagePath;
	private float[] terrain;
	private boolean initialised = false;
	private SceneRenderer currentSceneRenderer;
	private List<String> roundOrder;
	private Map<String, GameCharacter> chars;
	private Map<String, GameObject> objs;
	private Map<String, int[]> objPositions;
	private Map<GridPointIndex, String> posReverseLookup;
	private Map<String, FloatBuffer[]> objModels;
	private Map<String, Texture2D> objTextures, objThumbnail;
	private Map<String, Item> itemShop;
	private ResourceManager resMan;
	private ExtensionEngine extensionEngine;
	// game rule object
	private GameMaster gameMaster;
	private GameServer server;
	private String playerCharacter, currentCharacter;
	private int[] chosenGrid;
	private boolean ownRound;
	public GameState(String packagePath) {
		this.packagePath = packagePath;
	}
	public void attachServer(GameServer server) {
		this.server = server;
	}
	public GameServer getServer() {
		return server;
	}
	public void initialise(String playerCharacter) {
		this.playerCharacter = playerCharacter;
		currentCharacter = playerCharacter;
		if (initialised || server == null)
			return;
		// TODO change this part
		resMan = AppNativeAPI.createResourceManager(packagePath);
		if (resMan.isExtensionEnabled()) {
			gameMaster = new ExtendedGameMaster();
			extensionEngine = AppNativeAPI.createExtensionEngine();
			extensionEngine.loadScript(resMan.getAllScript());
			extensionEngine.execute();
		} else
			gameMaster = new GameMaster();	// TODO extended or basic?
		chars = new ConcurrentHashMap<>();
		roundOrder = new ArrayList<>();
		objs = new ConcurrentHashMap<>();
		objPositions = new ConcurrentHashMap<>();
		objModels = new ConcurrentHashMap<>();
		objTextures = new ConcurrentHashMap<>();
		objThumbnail = new ConcurrentHashMap<>();
		posReverseLookup = new ConcurrentHashMap<>();
		itemShop = new ConcurrentHashMap<>();
		chosenGrid = new int[2];
		// TODO load characters
		chars.put("MyHero", new Hero("MyHero", 1, 0, "strength",
				100,
				100,
				100,
				2,
				100,
				100,
				100,
				100,
				1,
				100,
				100,
				100,
				100,
				100,
				100,
				100));
		chars.put("MyHero2", new Hero("MyHero", 1, 0, "strength",
				100,
				100,
				100,
				2,
				100,
				100,
				100,
				100,
				1,
				100,
				100,
				100,
				100,
				100,
				100,
				100));
		setCharacterPositions("MyHero", new int[]{0, 0});
		setCharacterPositions("MyHero2", new int[]{19,19});
		// TODO load character models
		chars.get("MyHero").setCharacterImage("MyHeroModel");	// actually this refers to an entry in objModels called MyHeroModel and a texture named MyHeroModel
		chars.get("MyHero2").setCharacterImage("MyHeroModel");
		Item itm = new Item("TestItem", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		itemShop.put("TestItem", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		itemShop.put("TestItem2", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		itemShop.put("TestItem3", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		itemShop.put("TestItem4", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		itemShop.put("TestItem5", itm);
		// end
		chosenGrid = objPositions.get(playerCharacter).clone();
		initialised = true;
	}
	
	@Override
	public void close() {
		if (!initialised)
			return;
		// release resources
		resMan.close();
		gameMaster = null;
		chars = null;
		objs = null;
		objPositions = null;
		objModels = null;
		for (Texture2D tex : objTextures.values())
			tex.close();
		for (Texture2D tex : objThumbnail.values())
			tex.close();
		objThumbnail = objTextures = null;
		itemShop = null;
		initialised = false;
	}
	
	public void startTimer() {
		if (!initialised)
			return;
	}
	
	public void stopTimer() {
		if (!initialised)
			return;
	}
	
	public void resetTimer() {
		if (!initialised)
			return;
	}

	//terrain
	
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
	
	// characters
	public String getCurrentCharacterName() {
		return currentCharacter;
	}
	
	public String getPlayerCharacterName() {
		return playerCharacter;
	}
	
	public Map<String, GameCharacter> getCharacters() {
		return Collections.unmodifiableMap(chars);
	}
	
	public Map<String, int[]> getCharacterPositions() {
		return Collections.unmodifiableMap(objPositions);
	}
	
	// For Extension Engine Use
	public Object getCharacterExtendedProperty(String type, GameCharacter character, String name) {
		return null;
	}
	
	public void setCharacterExtendedProperty(String type, GameCharacter character, String name, Object value) {
		
	}
	
	public ExtensionEngine getExtensionEngine() {
		return extensionEngine;
	}
	
	public Object getCharacterProperty(String charName, String name) {
		// call getter method through reflection
		if (name == null || name.length() == 0 || !chars.containsKey(charName))
			return null;
		// Method
		final String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		final String booleanMethodName = "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		final Method[] methods;
		// TODO add LineCreeps
		final GameCharacter character = chars.get(charName);
		if (character instanceof Hero) {
			// Hero class
			methods = Hero.class.getMethods();
		} else {
			// Generic Character class
			methods = GameCharacter.class.getMethods();
		}
		for (Method m : methods) {
			if (!methodName.equals(m.getName()))
				continue;
			if (m.getGenericParameterTypes().length != 0)
				continue;
			try {
				m.setAccessible(true);
				return m.invoke(character);
			} catch (Exception e) {
				System.out.println("Property getter of '" + name + "' is not available.");
				System.out.println(e.getCause().getMessage());
			}
			if (!booleanMethodName.equals(m.getName()))
				continue;
			if (m.getReturnType() != boolean.class)
				continue;
			// is boolean method
			try {
				m.setAccessible(true);
				return m.invoke(character);
			} catch (Exception e) {
				System.out.println("Property getter of '" + name + "' is not available.");
				System.out.println(e.getCause().getMessage());
			}
		}
		// extended
		return character.getExtendedProperty(name);
	}
	
	public void setCharacterProperty(String charName, String name, Object value) {
		if (name == null || name.length() == 0 || !chars.containsKey(charName) || value == null)
			return;
		final String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		final Method[] methods;
		boolean setterSuccess = false;
		// TODO add LineCreeps
		final GameCharacter character = chars.get(charName);
		if (character instanceof Hero) {
			// Hero class
			methods = Hero.class.getMethods();
		} else {
			// Generic Character class
			methods = GameCharacter.class.getMethods();
		}
		for (Method m : methods) {
			if (!methodName.equals(m.getName()))
				continue;
			if (m.getGenericReturnType() != void.class)
				continue;
			final Type[] params = m.getGenericParameterTypes();
			if (!value.getClass().isAssignableFrom(params[0].getClass()))
				continue;
			try {
				m.setAccessible(true);
				m.invoke(character, value);
				setterSuccess = true;
				break;
			} catch (Exception e) {
				System.out.println("Property getter of '" + name + "' is not available.");
				System.out.println(e.getCause().getMessage());
				break;
			}
		}
		if (!setterSuccess && value instanceof Number)
			for (Method m : methods) {
				if (!methodName.equals(m.getName()))
					continue;
				if (m.getGenericReturnType() != void.class)
					continue;
				final Type[] params = m.getGenericParameterTypes();
				if (params.length != 1)
					continue;
				Object numericValue;
				if (params[0] == double.class || params[0] == Double.class)
					numericValue = ((Number) value).doubleValue();
				else if (params[0] == float.class || params[0] == Float.class)
					numericValue = ((Number) value).floatValue();
				else if (params[0] == int.class || params[0] == Integer.class)
					numericValue = ((Number) value).intValue();
				else if (params[0] == short.class || params[0] == Short.class)
					numericValue = ((Number) value).shortValue();
				else if (params[0] == byte.class || params[0] == Byte.class)
					numericValue = ((Number) value).byteValue();
				else if (params[0] == long.class || params[0] == Long.class)
					numericValue = ((Number) value).longValue();
				else
					continue;
				try {
					m.setAccessible(true);
					m.invoke(character, numericValue);
					setterSuccess = true;
					break;
				} catch (Exception e) {
					System.out.println("Property getter of '" + name + "' is not available.");
					System.out.println(e.getCause().getMessage());
					break;
				}
			}
		if (!setterSuccess) {
			// extended
			character.setExtendedProperty(name, value);
			if (!resMan.isExtensionEnabled())
				System.out.println("Property '" + name + "' is unknown. Looks like you are writing a custom property. Are you sure?");
		}
	}
	
	protected void setCharacterPositions(String name, int[] position) {
		if (chars.containsKey(name)) {
			if (position != null && position.length == 2) {
				final GridPointIndex key = new GridPointIndex(position);
				if (posReverseLookup.containsKey(key))
					return;	// failed
				if (objPositions.get(name) != null && posReverseLookup.containsKey(new GridPointIndex(objPositions.get(name))))
					posReverseLookup.remove(new GridPointIndex(objPositions.get(name)));
				posReverseLookup.put(key, name);
				objPositions.put(name, position.clone());
			} else {
				final GridPointIndex key = new GridPointIndex(objPositions.remove(name));
				posReverseLookup.remove(key);
			}
		}
	}
	
	public String getCharacterAtPosition (int[] position) {
		return posReverseLookup.get(new GridPointIndex(position));
	}
	
	public void addAutoCharacter(String name, GameCharacter character) {
		chars.put(name, character);
		roundOrder.add(name);
	}
	
	// character actions
	public void turnNextRound () {
		int idx = roundOrder.indexOf(currentCharacter);
		if (idx == roundOrder.size())
			idx = 0;
		else
			idx++;
		while (!chars.get(roundOrder.get(idx)).isAlive()) {
			roundOrder.remove(idx);
			if (idx == roundOrder.size())
				idx = 0;
		}
		currentCharacter = roundOrder.get(idx);
		gameMaster.applyRule(this, currentCharacter, "GameAction", Collections.singletonMap("BeginRound", null));
	}
		
	// models and resources for rendering
	
	public int getCharacterModel(String name) {
		return resMan.getModel(name);
	}
	
	public int getCharacterModelSize(String name) {
		return resMan.getModelSize(name);
	}
	
	public Texture2D getModelTexture(String name) {
		return new Texture2D(resMan.getTexture(name), resMan.getTextureWidth(name), resMan.getTextureHeight(name));
	}
	
	public Texture2D getModelThumbnail(String name) {
		return objThumbnail.get(name);
	}
	
	// items
	public Map<String, Item> getItemsInShop() {
		return Collections.unmodifiableMap(itemShop);
	}
	
	// interface interactions
	public Map<String, Boolean> areActionPossible (Map<String, Map<String, Object>> actions) {
		Map<String, Boolean> possible = new HashMap<>();
		for (Map.Entry<String, Map<String,Object>> action : actions.entrySet())
			possible.put(action.getKey(), gameMaster.requestActionPossible(this, playerCharacter, action.getKey(), action.getValue()));
		return Collections.unmodifiableMap(possible);
	}
	
	protected int[] getChosenGrid () {
		return chosenGrid.clone();
	}
	
	public void setCurrentSceneRenderer (SceneRenderer renderer) {
		currentSceneRenderer = renderer;
	}
	
	public void notifyUpdate (Map<String, Object> updates) {
		if (currentSceneRenderer != null)
			currentSceneRenderer.notifyUpdate(updates);
	}

	public void processEvent(ControlEvent e) {
		// TODO apply rules
		switch (e.extendedType) {
		// interface
		case "ChooseGrid":
			this.chosenGrid = (int[]) e.data.extendedData.get("Coordinates");
			gameMaster.applyRule(this, playerCharacter, "ChooseGrid", e.data.extendedData);
			break;
		case "RequestAttackArea":
			gameMaster.applyRule(this, playerCharacter, "RequestAttackArea", null);
			break;
		case "RequestMoveArea":
			gameMaster.applyRule(this, playerCharacter, "RequestMoveArea", null);
			break;
		case "RequestActionDetail":
			break;
		case "RequestItemShop":
			currentSceneRenderer.notifyUpdate(Collections.singletonMap("Dialog", (Object) "ItemShop")); 
			break;
		case "RequestActionList":
			break;
		case "Cancel":
			this.chosenGrid = new int[] {-1,-1};
			gameMaster.applyRule(this, playerCharacter, "Cancel", null);	// bounce back
			break;
		// game action
		case "GameAction":
			// send to game master
			gameMaster.applyRule(this, playerCharacter, "GameAction", e.data.extendedData);
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
		case "TestButton":
			System.out.println("Test Button Pressed");
			break;
		}
		/// Use Hard code game rules
	}
	public boolean isInitialised() {
		return initialised;
	}
}
