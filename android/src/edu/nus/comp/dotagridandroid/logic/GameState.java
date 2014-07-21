package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

import org.json.*;

import android.R.integer;
import edu.nus.comp.dotagridandroid.Closeable;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import edu.nus.comp.dotagridandroid.ui.event.*;
import edu.nus.comp.dotagridandroid.appsupport.*;

@SuppressWarnings("unchecked")
public class GameState implements Closeable {
	public static final int TERRAIN_TYPE_FLAT = 1;
	public static final int TERRAIN_TYPE_MOUNTAIN = 0;
	public static final int TERRAIN_TYPE_RIVER = 3;
	public static final int TERRAIN_TYPE_FOREST = 4;
	public static final int TERRAIN_TYPE_FENCE = 5;
	public static final int TERRAIN_TYPE_CLIFF = 6;
	
	private int gridWidth, gridHeight;
	private String packagePath;
	private float[] terrain;
	private int[] terrainType;
	private boolean initialised = false;
	private SceneRenderer currentSceneRenderer;
	private Queue<String> roundOrder;
	private int roundCount = 0;
	private Map<String, GameCharacter> chars;
	private Set<String> charsTurned;
	private Map<String, GameObject> objs;
	private Map<String, int[]> objPositions;
	private Map<GridPointIndex, String> posReverseLookup;
	private Map<String, Item> itemShop;
	private Map<String, Texture2D> objTextures, objThumbnail;
	private Map<String, Object> charsConfig;
	private ResourceManager resMan;
	private ExtensionEngine extensionEngine;
	// game rule object
	private GameMaster gameMaster;
	private GameServer server;
	private String playerCharacter, currentCharacter;
	private int[] chosenGrid;
	private Thread automatonThread = null;
	private String roundFlagName;
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
		chars = new ConcurrentHashMap<>();
		charsTurned = Collections.synchronizedSet(new HashSet<String>());
		roundOrder = new ConcurrentLinkedQueue<>();
		objs = new ConcurrentHashMap<>();
		objPositions = new ConcurrentHashMap<>();
		objTextures = new ConcurrentHashMap<>();
		objThumbnail = new ConcurrentHashMap<>();
		posReverseLookup = new ConcurrentHashMap<>();
		itemShop = new ConcurrentHashMap<>();
		resMan = AppNativeAPI.createResourceManager(packagePath);
		if (resMan.isExtensionEnabled()) {
			gameMaster = new ExtendedGameMaster();
			gameMaster.setResource(resMan);
			gameMaster.setState(this);
			extensionEngine = AppNativeAPI.createExtensionEngine();
			extensionEngine.loadScript(resMan.getAllScript());
			extensionEngine.execute();
			extensionEngine.attachGameState(this);
		} else {
			gameMaster = new BasicGameMaster();	// TODO extended or basic?
			gameMaster.setResource(resMan);
			gameMaster.setState(this);
		}
		chosenGrid = new int[2];
		// TODO load characters
		// TODO round order
		do {
			roundFlagName = java.util.UUID.randomUUID().toString();
		} while (chars.containsKey(roundFlagName));
		roundOrder.addAll(Arrays.asList("MyHero", "MyHero2", roundFlagName));
		// terrain
		Map<String, Object> terrainConfig = null;
		try {
			terrainConfig = JsonConverter.JsonToMap(new JSONObject(resMan.getTerrainConfiguration()));
			Map<String, Object> terrain = (Map<String, Object>) terrainConfig.get("terrain");
			gridWidth = ((Number) terrainConfig.get("width")).intValue();
			gridHeight = ((Number) terrainConfig.get("height")).intValue();
			switch ((String) terrain.get("type")) {
			case "fixed": {
				int c = 0;
				this.terrain = new float[gridWidth * gridHeight];
				this.terrainType = new int[gridWidth * gridHeight];
				for (Number val : (List<Number>) terrain.get("heights"))
					if (c < this.terrain.length)
						this.terrain[c++] = val.floatValue();
					else
						break;
				break;
			}
			case "random": {
				Random r = new Random();
				gridWidth = r.nextInt(300);
				gridHeight = r.nextInt(300);
				this.terrain = new float[gridWidth * gridHeight];
				for (int i = 0; i < gridWidth * gridHeight; i++)
					this.terrain[i] = r.nextFloat();
				break;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			terrain = new float[gridWidth * gridHeight];
		}
		// end
		gameMaster.initialise();
		chosenGrid = objPositions.get(playerCharacter).clone();
		initialised = true;
	}
	
	@Override
	public void close() {
		if (!initialised)
			return;
		while (automatonThread != null && automatonThread.isAlive())
			;
		// release resources
		if (resMan.isExtensionEnabled() && extensionEngine != null)
			extensionEngine.close();
		resMan.close();
		resMan = null;
		extensionEngine = null;
		gameMaster = null;
		chars = null;
		objs = null;
		objPositions = null;
		for (Texture2D tex : objTextures.values())
			tex.close();
		for (Texture2D tex : objThumbnail.values())
			tex.close();
		objThumbnail = objTextures = null;
		itemShop = null;
		initialised = false;
	}
	
	public boolean isExtensionEnabled() {
		return resMan.isExtensionEnabled();
	}

	//terrain
	
	public String getTerrainConfiguration () {
		return resMan.getTerrainConfiguration();
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
		return terrain.clone();
	}
	
	public float getTerrainHeight(int... pos) {
		return terrain[pos[0] + pos[1] * gridWidth];
	}
	
//	public int getTerrainType(int... pos) {
//		return terrainType[pos[0] + pos[1] * gridWidth];
//	}

//	public void setTerrain(float[] terrain) {
//		this.terrain = terrain;
//	}
	
	// characters
	public Map<String, Object> getCharacterConfiguration () {
		return charsConfig;
	}
	
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
	
	public ExtensionEngine getExtensionEngine() {
		return extensionEngine;
	}
	
	public Object getCharacterProperty(String charName, String name) {
		// call getter method through reflection
		if (name == null || name.length() == 0 || !chars.containsKey(charName) || roundFlagName.equals(charName))
			return null;
		// Method
		final String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		final String booleanMethodName = "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		// TODO add LineCreeps
		final GameCharacter character = chars.get(charName);
		try {
			return character.getClass().getMethod(methodName, new Class[]{}).invoke(character);
		} catch (Exception e) {
			System.out.println("Property getter of '" + name + "' is not available.");
		}
		try {
			return character.getClass().getMethod(booleanMethodName, new Class[]{}).invoke(character);
		} catch (Exception e) {
			System.out.println("Boolean property getter of '" + name + "' is not available.");
		}
		// extended
		return character.getExtendedProperty(name);
	}
	
	public synchronized void setCharacterProperty(String charName, String name, Object value) {
		if (name == null || name.length() == 0 || !chars.containsKey(charName) || value == null)
			return;
		final String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
		final Method[] methods;
		boolean setterSuccess = false;
		// TODO add LineCreeps
		final GameCharacter character = chars.get(charName);
		methods = character.getClass().getMethods();
		for (Method m : methods) {
			if (!methodName.equals(m.getName()))
				continue;
			if (m.getGenericReturnType() != void.class)
				continue;
			final Type[] params = m.getGenericParameterTypes();
			if (value instanceof Integer && params[0] == Integer.TYPE)
				;
			else if (value instanceof Double && params[0] == Double.TYPE)
				;
			else if (value instanceof Float && params[0] == Float.TYPE)
				;
			else if (value instanceof Long && params[0] == Long.TYPE)
				;
			else if (value instanceof Short && params[0] == Short.TYPE)
				;
			else if (value instanceof Boolean && params[0] == Boolean.TYPE)
				;
			else if (!params[0].getClass().isAssignableFrom(value.getClass()))
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
		else if (!setterSuccess && value instanceof List)
			for (Method m : methods) {
				if (!methodName.equals(m.getName()))
					continue;
				if (m.getGenericReturnType() != void.class)
					continue;
				final Type[] params = m.getGenericParameterTypes();
				if (params.length != 1)
					continue;
				Object numericValue;
				int c = 0;
				if (params[0] == double[].class) {
					double[] val = new double[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.doubleValue();
					numericValue = val;
				} else if (params[0] == float[].class) {
					float[] val = new float[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.floatValue();
					numericValue = val;
				} else if (params[0] == int[].class) {
					int[] val = new int[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.intValue();
					numericValue = val;
				} else if (params[0] == short[].class) {
					short[] val = new short[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.shortValue();
					numericValue = val;
				} else if (params[0] == byte[].class) {
					byte[] val = new byte[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.byteValue();
					numericValue = val;
				} else if (params[0] == long[].class) {
					long[] val = new long[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.longValue();
					numericValue = val;
				} else if (params[0] == Double[].class) {
					Double[] val = new Double[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.doubleValue();
					numericValue = val;
				} else if (params[0] == Float[].class) {
					Float[] val = new Float[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.floatValue();
					numericValue = val;
				} else if (params[0] == Integer[].class) {
					Integer[] val = new Integer[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.intValue();
					numericValue = val;
				} else if (params[0] == Short[].class) {
					Short[] val = new Short[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.shortValue();
					numericValue = val;
				} else if (params[0] == Byte[].class) {
					Byte[] val = new Byte[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.byteValue();
					numericValue = val;
				} else if (params[0] == Long[].class) {
					Long[] val = new Long[((List) value).size()];
					for (Number e : (List<Number>) value)
						val[c++] = e.longValue();
					numericValue = val;
				} else if (params[0] == Object[].class) {
					numericValue = ((List) value).toArray(new Object[((List) value).size()]);
				} else
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
	
	public int[] getCharacterPosition(String name) {
		return objPositions.get(name);
	}
	
	public synchronized void setCharacterPosition(String name, int... position) {
		if (chars.containsKey(name)) {
			if (position != null) {
				if (position[0] >= getGridWidth() || position[0] < 0 || position[1] >= getGridHeight() || position[1] < 0)
					return;	// failed
				final GridPointIndex key = new GridPointIndex(position);
				if (posReverseLookup.containsKey(key))
					return;	// failed
				if (objPositions.get(name) != null && posReverseLookup.containsKey(new GridPointIndex(objPositions.get(name))))
					posReverseLookup.remove(new GridPointIndex(objPositions.get(name)));
				posReverseLookup.put(key, name);
				objPositions.put(name, position.clone());
			} else
				posReverseLookup.remove(new GridPointIndex(objPositions.remove(name)));
		} else if (objPositions.containsKey(name))
			posReverseLookup.remove(new GridPointIndex(objPositions.remove(name)));
	}
	
	public String getCharacterAtPosition (int... position) {
		return posReverseLookup.get(new GridPointIndex(position));
	}
	
	public void addCharacter(String name, GameCharacter character, boolean automated, boolean controlled) {
		if (chars.containsKey(name) || roundFlagName.equals(name))
			return;
		chars.put(name, character);
		setCharacterProperty(name, "automated", automated);
		if (controlled)
			roundOrder.add(name);
	}
	
	public GameCharacter removeCharacter(String name) {
		roundOrder.remove(name);
		return chars.remove(name);
	}
	
	public int getCharacterType (String name) {
		if (roundFlagName.equals(name))
			return GameObject.GAMEOBJECT_TYPE_ROUNDFLAG;
		else if (chars.containsKey(name))
			return chars.get(name).getObjectType();
		else
			return -1;
	}
	
	// character actions
	public void nextTurn () {
		// end round routine
		gameMaster.applyRule(this, null, "GameAction", Collections.singletonMap("EndTurn", (Object) currentCharacter));
		if (!currentCharacter.equals(roundOrder.peek()))
			throw new RuntimeException("Round Order is corrupted");
		roundOrder.add(roundOrder.poll());
		charsTurned.add(currentCharacter);
		while (true)
			if (roundFlagName.equals(roundOrder.peek())) {
				gameMaster.applyRule(this, null, "GameAction", Collections.singletonMap("EndRound", null));
				roundOrder.add(roundOrder.poll());
				roundCount++;
				gameMaster.applyRule(this, null, "GameAction", Collections.singletonMap("BeginRound", null));
			} else if (chars.get(roundOrder.peek()).isAlive())
				break;
			else
				roundOrder.add(roundOrder.poll());
		currentCharacter = roundOrder.peek();
		charsTurned.add(currentCharacter);
		if (chars.get(currentCharacter).isAutomated()) {
			Thread t = new Thread() {
				@Override
				public void run() {
					GameState stateMachine = GameState.this;
					gameMaster.applyRule(stateMachine, currentCharacter, "GameAction", Collections.singletonMap("BeginTurn", null));
					String character = currentCharacter;
					GameCharacterAutomaton.autoAction(stateMachine, currentCharacter);
					// force nextRound
					if (character.equals(currentCharacter))
						nextTurn();
				}
			};
			t.start();
			automatonThread = t;
		} else {
			gameMaster.applyRule(this, currentCharacter, "GameAction", Collections.singletonMap("BeginTurn", null));
			automatonThread = null;
		}
	}
		
	public int getRoundCount() {
		return roundCount;
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
	public void setItemInShop (String name, Item item) {
		itemShop.put(name, item);
	}
	
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
	
	public int[] getChosenGrid () {
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
