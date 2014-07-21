package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;

import edu.nus.comp.dotagridandroid.appsupport.JsonConverter;

@SuppressWarnings("unchecked")
public class BasicGameMaster extends GameMaster {
	private int[] terrainType;
	private Map<Integer, Map<String, Object>> teamConfig;
	private Map<String, Object> stateData;
	private Map<String, Object> charsConfig;

	@Override
	public void serverNotify() {
	}

	@Override
	public void initialise() {
		teamConfig = new HashMap<>();
		stateData = new ConcurrentHashMap<>();
		state.addCharacter("MyHero", new Hero("MyHero", 1, 0, 200, 0, "strength",
				100,
				100,
				100,
				2,
				100,
				100,
				100,
				10000,
				1,
				100,
				100,
				100,
				100,
				100,
				100,
				100), false, true);
		state.addCharacter("MyHero2", new Hero("MyHero", 1, 0, 200, 0, "strength",
				100,
				100,
				100,
				2,
				100,
				100,
				100,
				10000,
				2,
				100,
				100,
				100,
				100,
				100,
				100,
				100), true, true);
		state.setCharacterPosition("MyHero", new int[]{0, 0});
		state.setCharacterPosition("MyHero2", new int[]{19,19});
		state.getCharacters().get("MyHero").setCharacterImage("MyHeroModel");	// actually this refers to an entry in objModels called MyHeroModel and a texture named MyHeroModel
		state.getCharacters().get("MyHero2").setCharacterImage("MyHeroModel");
		Item itm = new Item("TestItem", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		state.setItemInShop("TestItem", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		state.setItemInShop("TestItem2", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		state.setItemInShop("TestItem3", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		state.setItemInShop("TestItem4", itm);
		itm = new Item("TestItem-SE", 0, 0, 0, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		itm.setItemImage("DefaultButton");
		state.setItemInShop("TestItem5", itm);

		Map<String, Object> terrainConfig = null;
		terrainType = new int[state.getGridWidth() * state.getGridHeight()];
		Arrays.fill(terrainType, GameState.TERRAIN_TYPE_FLAT);
		try {
			terrainConfig = JsonConverter.JsonToMap(new JSONObject(resourceManager.getTerrainConfiguration()));
			Map<String, Object> terrain = (Map<String, Object>) terrainConfig.get("terrain");
			int c = 0;
			if (terrain.get("types") == null || "plain".equals(terrain.get("types")))
				Arrays.fill(terrainType, GameState.TERRAIN_TYPE_FLAT);
			else
				for (Number val : (List<Number>) terrain.get("types"))
					if (c < this.terrainType.length)
						this.terrainType[c++] = val.intValue();
					else
						break;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			charsConfig = JsonConverter.JsonToMap(new JSONObject(resourceManager.getCharacterConfiguration()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// terrain characters
		for (int i = 0; i < state.getGridWidth() * state.getGridHeight(); i++)
			switch (terrainType[i]) {
			case GameState.TERRAIN_TYPE_FOREST: {
				// TODO Change here
				Tree tree = new Tree();
				tree.setCharacterImage("Tree");	// TODO Change here!
				state.addCharacter("Tree" + i, tree, false, false);
				state.setCharacterPosition("Tree" + i, i % state.getGridWidth(), i / state.getGridWidth());
				break;
			}
			case GameState.TERRAIN_TYPE_RIVER: {
				break;
			}
			}
		
		// towers
		try {
			List<Map<String, Object>> towerConfig = (List<Map<String, Object>>) terrainConfig.get("towers");
			if (towerConfig != null)
				for (Map<String, Object> tower : towerConfig) {
					String name = (String) tower.get("name");
					Tower towerCharacter = new Tower (
							name,
							((Number) tower.get("bountyMoney")).intValue(),
							((Number) tower.get("startingHP")).intValue(),
							((Number) tower.get("startingMP")).intValue(),
							((Number) tower.get("startingPhysicalAttack")).doubleValue(),
							((Number) tower.get("startingPhysicalAttackArea")).intValue(),
							"max".equals(tower.get("startingPhysicalAttackSpeed")) ?
									GameCharacter.MAX_PHYSICAL_ATTACK_SPEED :
									((Number) tower.get("startingPhysicalAttackSpeed")).intValue(),
							((Number) tower.get("startingPhysicalDefence")).intValue(),
							((Number) tower.get("actionPoint")).intValue(),
							((Number) tower.get("team")).intValue()/*0*/);
					towerCharacter.setCharacterImage((String) tower.get("model"));
					towerCharacter.setSight(((Number) tower.get("sight")).intValue());
					state.addCharacter(name, towerCharacter, true, true);
					state.setCharacterPosition(name,
							((List<Number>) tower.get("position")).get(0).intValue(),
							((List<Number>) tower.get("position")).get(1).intValue());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// barracks
		try {
			for (Map<String, Object> barrack : (List<Map<String, Object>>) terrainConfig.get("barracks")) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// team
		try {
			for (Map<String, Object> team : (List<Map<String, Object>>) terrainConfig.get("teams")) {
				int teamNumber = ((Number) team.get("team")).intValue();
				teamConfig.put(teamNumber, team);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void applyRule(GameState stateMachine, String character,
			String actionName, Map<String, Object> options) {

		if (!character.equals(stateMachine.getCurrentCharacterName()))
			return;
		final int[]
				prevPos = stateMachine.getCharacterPositions().get(stateMachine.getCurrentCharacterName()),
				reqPos = stateMachine.getChosenGrid();
		switch (actionName) {
		case "ChooseGrid": {
			stateMachine.notifyUpdate(Collections.singletonMap("ChosenGrid", options.get("Coordinate")));
			break;
		}
		case "RequestAttackArea": {
			// TODO: calculate attack area
			List<List<Integer>> allowed = new ArrayList<>();
			final int gridWidth = stateMachine.getGridWidth(), gridHeight = stateMachine.getGridHeight();
			final int totalAttackArea = (Integer) stateMachine.getCharacterProperty(character, "totalPhysicalAttackArea")
					+ (Integer) stateMachine.getCharacterProperty(character, "totalItemAddPhysicalAttackArea");
			for (int i = -totalAttackArea; i <= totalAttackArea; i++)
				for (int j = -totalAttackArea + Math.abs(i); j <= totalAttackArea - Math.abs(i); j++)
					if ((i != 0 || j != 0) && prevPos[0] + i < gridWidth && prevPos[0] + i >= 0 && prevPos[1] + j < gridHeight && prevPos[1] + j >= 0)
						allowed.add(Arrays.asList(prevPos[0] + i, prevPos[1] + j));
			stateMachine.notifyUpdate(Collections.singletonMap("HighlightGrid", (Object) Collections.unmodifiableList(allowed)));
			return;
		}
		case "RequestMoveArea": {
			System.out.println("Requesting move area");
			List<List<Integer>> allowed = new ArrayList<>();
			if (reqPos[0] >= 0 && reqPos[0] < stateMachine.getGridWidth() && reqPos[1] >= 0 && reqPos[1] < stateMachine.getGridHeight()) {
				final double[] APmap = getLowestMoveAPConsumptionMap(stateMachine, prevPos, character);
				final int currentActionPoint = (Integer) stateMachine.getCharacterProperty(character, "currentActionPoint");
				int column = 0, row = 0;
				for (int i = 0; i < stateMachine.getGridWidth() * stateMachine.getGridHeight(); i++, column++) {
					if (column == stateMachine.getGridWidth()) {
						column = 0; row++;
					}
					if (APmap[i] <= currentActionPoint)
						allowed.add(Arrays.asList(column, row));
				}
				stateMachine.notifyUpdate(Collections.singletonMap("HighlightGrid", (Object) Collections.unmodifiableList(allowed)));
			}
			return;
		}
		case "RequestItemShop": {
			return;
		}
		case "GameAction": {
			// check if action is possible
			if (requestActionPossible(stateMachine, character, actionName, options)) {
				Map<String, Object> updates = new HashMap<>();
				switch ((String) options.get("Action")) {
				case "Attack":
					System.out.println("Game action is attack");
					// apply game rule
					// notify
					updates.put("Characters", Collections.singleton("ALL"));
					stateMachine.notifyUpdate(Collections.unmodifiableMap(updates));
					return;
				case "Move": {
					System.out.println("Game action is move");
					// calculate AP
					final double actionPointUsed = (double) stateMachine.getCharacterProperty(character, "APUsedInMovingOneGrid")
							* (Math.abs(reqPos[0] - prevPos[0]) + Math.abs(reqPos[1] - prevPos[1]));
					final int currentActionPoint = (Integer) stateMachine.getCharacterProperty(character, "currentActionPoint");
					final int newActionPoint = (int) (currentActionPoint - actionPointUsed);
					stateMachine.setCharacterProperty(character, "currentActionPoint", newActionPoint);
					// move
					stateMachine.setCharacterPosition(character, stateMachine.getChosenGrid());
					List<String> characters = Collections.singletonList(character);
					updates = Collections.singletonMap("Characters", (Object) characters);
					stateMachine.notifyUpdate(updates);
					return;
				}
				case "BuyItem": {
					break;
				}
				case "NextRound": {
					break;
				}
				case "BeginTurn":
					break;
				case "EndTurn":
					break;
				case "BeginRound": {
					final int roundCount = stateMachine.getRoundCount();
					Map<String, Object> thatTeamConfig;
					// spawn creeps
					// line creeps
					final Map<String, Object> charsConfig = stateMachine.getCharacterConfiguration();
					Set<Integer> teamsWithoutNeutral = new HashSet<Integer> (this.teamConfig.keySet());
					teamsWithoutNeutral.remove(0);
					for (Integer i : teamsWithoutNeutral) {
						thatTeamConfig = this.teamConfig.get(i);
						for (Map<String, Object> front : (List<Map<String, Object>>) thatTeamConfig.get("fronts")) {
							final int[] spawnPoint = new int[]{
									((List<Number>) front.get("creepSpawnPoint")).get(0).intValue(),
									((List<Number>) front.get("creepSpawnPoint")).get(1).intValue()
							};
							for (Map<String, Object> type : (List<Map<String, Object>>) front.get("creepSpawnType"))
								if (roundCount % ((Number) type.get("round")).intValue() == 0)
									for (int count = 0,
										end = Math.max(((Number) type.get("maximum")).intValue(),
												((Number) type.get("initial")).intValue() + roundCount / ((Number) type.get("increaseRound")).intValue()); count < end; count++) {
										String name;
										do {
											name = java.util.UUID.randomUUID().toString();
										} while (stateMachine.getCharacterType(name) < 0 || stateMachine.getCharacterType(name) == GameObject.GAMEOBJECT_TYPE_ROUNDFLAG);
										Map<String, Object> config = (Map<String, Object>) ((Map<String, Object>) charsConfig.get("creepConfiguration")).get(type.get("type"));
										LineCreep creep = new LineCreep();
										stateMachine.addCharacter(name, creep, true, true);
										stateMachine.setCharacterProperty(name, "characterImage", null);
										// specific rule
									}
						}
					}
					// neutral creeps
					thatTeamConfig = this.teamConfig.get(0);
					break;
				}
				case "EndRound": {
					for (Map.Entry<String, GameCharacter> entry : stateMachine.getCharacters().entrySet())
						if (entry.getValue().getRoundsToRevive() > 0) {
							stateMachine.setCharacterProperty(entry.getKey(), "roundsToRevive", entry.getValue().getRoundsToRevive() - 1);
							if (entry.getValue().getRoundsToRevive() == 0) {
								stateMachine.setCharacterProperty(entry.getKey(), "currentHP", entry.getValue().getMaxHP());
								stateMachine.setCharacterProperty(entry.getKey(), "currentMP", entry.getValue().getMaxMP());
								stateMachine.setCharacterProperty(entry.getKey(), "currentActionPoint", entry.getValue().getMaxActionPoint());
								List<Number> spawnPoint = (List<Number>)
										teamConfig.get(entry.getValue().getTeamNumber()).get("heroSpawnPoint");
								final int[] pos = {spawnPoint.get(0).intValue(), spawnPoint.get(1).intValue()};
								if (stateMachine.getCharacterAtPosition(pos) == null)
									stateMachine.setCharacterPosition(entry.getKey(), pos);
								else if (entry.getValue().getObjectType() == GameObject.GAMEOBJECT_TYPE_TREE) {
									stateMachine.setCharacterProperty(entry.getKey(), "roundsToRevive", 1);
								} else {
//									final int limit = Math.max(pos[0], Math.max(stateMachine.getGridWidth() - pos[0], Math.max(pos[1], stateMachine.getGridHeight() - pos[1])));
//									boolean searching = true;
//									for (int r = 1; r <= limit && searching; r++)
//										for (int i = Math.max(0, pos[0] - r), end = Math.min(stateMachine.getGridWidth(), pos[0] + r); i < end; i++)
//											if (pos[1] + Math.abs(i - pos[0]) - r >= 0 && pos[1] + Math.abs(i - pos[0]) - r < stateMachine.getGridHeight()) {
//												stateMachine.setCharacterPosition(entry.getKey(), i, pos[1] + Math.abs(i - pos[0]) - r);
//												searching = false;
//												break;
//											} else if (pos[1] - Math.abs(i - pos[0]) + r >= 0 && pos[1] - Math.abs(i - pos[0]) + r < stateMachine.getGridHeight()) {
//												stateMachine.setCharacterPosition(entry.getKey(), i, pos[1] - Math.abs(i - pos[0]) + r);
//												searching = false;
//												break;
//											}
									final int[] spawnPosition = getNearestFreeGrid(stateMachine, pos);
									if (spawnPoint == null)
										throw new RuntimeException("No spawn point found for " + entry.getKey());
									else
										stateMachine.setCharacterPosition(entry.getKey(), spawnPosition);
								}
							}
						} else if (entry.getValue().getRoundsToRevive() == 0) {
							stateMachine.setCharacterProperty(entry.getKey(), "currentHP", entry.getValue().getCurrentHP() + entry.getValue().getHPGainPerRound());
							stateMachine.setCharacterProperty(entry.getKey(), "currentMP", entry.getValue().getCurrentMP() + entry.getValue().getMPGainPerRound());
							stateMachine.setCharacterProperty(entry.getKey(), "currentActionPoint", entry.getValue().getMaxActionPoint());
						}
					break;
				}
				}
			}
			break;
		}
		case "Cancel":
			// nothing
			stateMachine.notifyUpdate(Collections.singletonMap("Cancel", (Object) "ALL"));
			return;
		}
	}

	@Override
	public boolean requestActionPossible(GameState stateMachine,
			String character, String actionName, Map<String, Object> options) {
		if (!character.equals(stateMachine.getCurrentCharacterName()))
			return false;
		final int[] targetGrid = stateMachine.getChosenGrid(), heroGrid = stateMachine.getCharacterPositions().get(character);
		switch (actionName) {
		case "GameAction":
			switch ((String) options.get("Action")) {
			case "Move" : {
				if (targetGrid[0] >= stateMachine.getGridWidth() || targetGrid[0] < 0 || targetGrid[1] >= stateMachine.getGridHeight() || targetGrid[1] < 0)
					return false;
				return true;
			}
			case "Attack": {
				if (targetGrid[0] >= stateMachine.getGridWidth() || targetGrid[0] < 0 || targetGrid[1] >= stateMachine.getGridHeight() || targetGrid[1] < 0)
					return false;
				Hero hero = (Hero) stateMachine.getCharacters().get(character);
				return stateMachine.getCharacterAtPosition(targetGrid) != null &&	// has hero, linecreep or tower
						hero.getTotalPhysicalAttackArea() + hero.getTotalItemAddPhysicalAttackArea()
						>= Math.abs(targetGrid[0] - heroGrid[0])
						+ Math.abs(targetGrid[1] - heroGrid[1]) &&	// within attack area
						hero.getCurrentActionPoint() > Hero.MIN_PHYSICAL_ATTACK_CONSUME_AP + (1 - hero.getTotalPhysicalAttackSpeed() / GameCharacter.MAX_PHYSICAL_ATTACK_SPEED)
							* Hero.PHYSICAL_ATTACK_CONSUME_AP;	// has enough action points
			}
			}
			break;
		}
		// TODO TEST
		return true;
	}
	
	private double[] getLowestMoveAPConsumptionMap (GameState stateMachine, int[] start, String character) {
		final float[] terrain = stateMachine.getTerrain();
		final int width = stateMachine.getGridWidth(), height = stateMachine.getGridHeight();
		final int[][] dirs = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		final double TERRAIN_CONST = 1;	// TODO terrain factor
		Queue<int[]> q = new LinkedList<>();
		double[] map = new double[width * height];
//		Arrays.fill(map, character.getMaxActionPoint() + 1);
		Arrays.fill(map, (Integer) stateMachine.getCharacterProperty(character, "currentActionPoint") + 1);
		final double APConsumptionPerGrid = (Double) stateMachine.getCharacterProperty(character, "APUsedInMovingOneGrid");
		map[start[0] + start[1] * width] = 0;
		q.add(start);
		while (!q.isEmpty()) {
			final int[] prevPos = q.remove();
			for (byte i = 0; i < 4; i++) 
				if (prevPos[0] + dirs[i][0] < width && prevPos[0] + dirs[i][0] >= 0 &&
						prevPos[1] + dirs[i][1] < height && prevPos[1] + dirs[i][1] >= 0 &&
						stateMachine.getCharacterAtPosition(prevPos[0] + dirs[i][0], prevPos[1] + dirs[i][1]) == null &&
						terrainType[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * stateMachine.getGridWidth()] != GameState.TERRAIN_TYPE_CLIFF) {
					final double APconsumed = APConsumptionPerGrid/*character.getAPUsedInMovingOneGrid()*/ +
							Math.max(0, terrain[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width] - terrain[prevPos[0] + prevPos[1] * width]) * TERRAIN_CONST;
					if (APconsumed < map[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width]) {
						map[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width] = APconsumed;
						q.add(new int[]{prevPos[0] + dirs[i][0], prevPos[1] + dirs[i][1]});
					}
				}
		}
		return map;
	}
	
	private int[] getNearestFreeGrid (GameState stateMachine, int[] pos) {
		final int limit = Math.max(pos[0], Math.max(stateMachine.getGridWidth() - pos[0], Math.max(pos[1], stateMachine.getGridHeight() - pos[1])));
		for (int r = 1; r <= limit; r++)
			for (int i = Math.max(0, pos[0] - r), end = Math.min(stateMachine.getGridWidth(), pos[0] + r); i < end; i++)
				if (pos[1] + Math.abs(i - pos[0]) - r >= 0 && pos[1] + Math.abs(i - pos[0]) - r < stateMachine.getGridHeight())
					return new int[] {i, pos[1] + Math.abs(i - pos[0]) - r};
				else if (pos[1] - Math.abs(i - pos[0]) + r >= 0 && pos[1] - Math.abs(i - pos[0]) + r < stateMachine.getGridHeight())
					return new int[] {i, pos[1] - Math.abs(i - pos[0]) + r};
		return null;
	}
}
