package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;
import edu.nus.comp.dotagridandroid.appsupport.JsonConverter;

@SuppressWarnings("unchecked")
public class BasicGameMaster extends GameMaster {
	private int[] terrainType;
	private Map<Integer, Map<String, Object>> teamConfig;
	private Map<Integer, Map<Integer, List<String>>> teamStrength;
	private Map<Integer, Map<Integer, Map<String, Integer>>> teamCreepLevel;
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
				100), false, false);
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
				100), false, true);
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
					final String name = (String) tower.get("name");
					Tower towerCharacter = new Tower();
					state.addCharacter(name, towerCharacter, true, true);
					state.setCharacterPosition(name,
							((List<Number>) tower.get("position")).get(0).intValue(),
							((List<Number>) tower.get("position")).get(1).intValue());
					state.setCharacterProperty(name, "name", name);
					state.setCharacterProperty(name, "bountyMoney", tower.get("bountyMoney"));
					state.setCharacterProperty(name, "startingHP", tower.get("startingHP"));
					state.setCharacterProperty(name, "maxHP", tower.get("startingHP"));
					state.setCharacterProperty(name, "currentHP", tower.get("startingHP"));
					state.setCharacterProperty(name, "startingMP", tower.get("startingMP"));
					state.setCharacterProperty(name, "maxMP", tower.get("startingMP"));
					state.setCharacterProperty(name, "currentMP", tower.get("startingMP"));
					state.setCharacterProperty(name, "startingPhysicalAttack", tower.get("startingPhysicalAttack"));
					state.setCharacterProperty(name, "startingPhysicalAttackArea", tower.get("startingPhysicalAttackArea"));
					state.setCharacterProperty(name, "startingPhysicalAttackSpeed",
							"max".equals(tower.get("startingPhysicalAttackSpeed")) ?
							GameCharacter.MAX_PHYSICAL_ATTACK_SPEED :
							tower.get("startingPhysicalAttackSpeed"));
					state.setCharacterProperty(name, "startingPhysicalDefence", tower.get("startingPhysicalDefence"));
					state.setCharacterProperty(name, "maxActionPoint", tower.get("actionPoint"));
					state.setCharacterProperty(name, "teamNumber", tower.get("team"));
					state.setCharacterProperty(name, "front", tower.get("front"));
					state.setCharacterProperty(name, "characterImage", tower.get("model"));
					state.setCharacterProperty(name, "sight", tower.get("sight"));
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// team & ancients
		teamStrength = new HashMap<>();
		try {
			for (Map<String, Object> team : (List<Map<String, Object>>) terrainConfig.get("teams")) {
				final int teamNumber = ((Number) team.get("team")).intValue();
				teamConfig.put(teamNumber, team);
				final Map<String, Object> ancientConfig = (Map<String, Object>) team.get("ancient");
				if (ancientConfig != null) {
					final String name = (String) ancientConfig.get("name");
					Ancient ancient = new Ancient();
					state.addCharacter(name, ancient, false, false);
					state.setCharacterProperty(name, "bountyMoney", ancientConfig.get("bountyMoney"));
					state.setCharacterProperty(name, "startingHP", ancientConfig.get("startingHP"));
					state.setCharacterProperty(name, "startingPhysicalDefence", ancientConfig.get("startingPhysicalDefence"));
					state.setCharacterProperty(name, "characterImage", ancientConfig.get("model"));
					state.setCharacterPosition(name,
							((List<Number>) ancientConfig.get("position")).get(0).intValue(),
							((List<Number>) ancientConfig.get("position")).get(0).intValue());
				}
				// record team strength (tower)
				if (teamNumber > 0) {
					Map<Integer, List<String>> teamStrength = new HashMap<>();
					for (Map<String, Object> tower : (List<Map<String, Object>>) terrainConfig.get("towers"))
						if ((Integer) state.getCharacterProperty((String) tower.get("name"), "teamNumber") == teamNumber)
							if (teamStrength.containsKey(state.getCharacterProperty((String) tower.get("name"), "front")))
								teamStrength.get(state.getCharacterProperty((String) tower.get("name"), "front")).add((String) tower.get("name"));
							else
								teamStrength.put(
										(Integer) state.getCharacterProperty((String) tower.get("name"), "front"),
										new ArrayList<String>(Arrays.asList((String) tower.get("name"))));
					this.teamStrength.put(teamNumber, teamStrength);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// barracks
		teamCreepLevel = new HashMap<>();
		try {
			for (Map<String, Object> barrack : (List<Map<String, Object>>) terrainConfig.get("barracks")) {
				final String name = (String) barrack.get("name");
				final int team = ((Number) barrack.get("team")).intValue(), front = ((Number) barrack.get("front")).intValue();
				final String type = (String) barrack.get("type");
				final String model = (String) barrack.get("model");
				if (!teamCreepLevel.containsKey(team))
					teamCreepLevel.put(team, new HashMap<Integer, Map<String, Integer>>());
				if (!teamCreepLevel.get(team).containsKey(front))
					teamCreepLevel.get(team).put(front, new HashMap<String, Integer>());
				teamCreepLevel.get(team).get(front).put(type, 0);
				Barrack obj = new Barrack();
				state.addCharacter(name, obj, false, false);
				// load barrack data
				Map<String, Object> barrackSetting = (Map<String, Object>) charsConfig.get("barrackConfiguration");
				for (Map.Entry<String, Object> setting : ((Map<String, Object>) barrackSetting.get(type)).entrySet())
					state.setCharacterProperty(name, setting.getKey(), setting.getValue());
				state.setCharacterProperty(name, "maxHP", ((Map<String, Object>) barrackSetting.get(type)).get("startingHP"));
				state.setCharacterProperty(name, "currentHP", ((Map<String, Object>) barrackSetting.get(type)).get("startingHP"));
				state.setCharacterProperty(name, "teamNumber", team);
				state.setCharacterProperty(name, "front", front);
				state.setCharacterProperty(name, "characterImage", model);
				state.setCharacterProperty(name, "type", type);
				state.setCharacterPosition(name,
						((List<Number>) barrack.get("position")).get(0).intValue(),
						((List<Number>) barrack.get("position")).get(1).intValue());
				// count into strength
				if (!teamStrength.containsKey(team))
					teamStrength.put(team, new HashMap<Integer, List<String>>());
				if (!teamStrength.get(team).containsKey(front))
					teamStrength.get(team).put(front, new ArrayList<String>());
				teamStrength.get(team).get(front).add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void applyRule(String character, String actionName, Map<String, Object> options) {

		final int[]
				prevPos = state.getCharacterPosition(state.getCurrentCharacterName()),
				reqPos = state.getChosenGrid();
		switch (actionName) {
		case "ChooseGrid": {
			if (!character.equals(state.getCurrentCharacterName()))
				return;
			state.notifyUpdate(Collections.singletonMap("ChosenGrid", options.get("Coordinate")));
			break;
		}
		case "RequestAttackArea": {
			if (!character.equals(state.getCurrentCharacterName()))
				return;
			// TODO: calculate attack area
			List<List<Integer>> allowed = new ArrayList<>();
			final int gridWidth = state.getGridWidth(), gridHeight = state.getGridHeight();
			final int totalAttackArea = (Integer) state.getCharacterProperty(character, "totalPhysicalAttackArea")
					+ (Integer) state.getCharacterProperty(character, "totalItemAddPhysicalAttackArea");
			for (int i = -totalAttackArea; i <= totalAttackArea; i++)
				for (int j = -totalAttackArea + Math.abs(i); j <= totalAttackArea - Math.abs(i); j++)
					if ((i != 0 || j != 0) && prevPos[0] + i < gridWidth && prevPos[0] + i >= 0 && prevPos[1] + j < gridHeight && prevPos[1] + j >= 0)
						allowed.add(Arrays.asList(prevPos[0] + i, prevPos[1] + j));
			state.notifyUpdate(Collections.singletonMap("HighlightGrid", (Object) Collections.unmodifiableList(allowed)));
			return;
		}
		case "RequestMoveArea": {
			if (!character.equals(state.getCurrentCharacterName()))
				return;
			System.out.println("Requesting move area");
			List<List<Integer>> allowed = new ArrayList<>();
			if (reqPos[0] >= 0 && reqPos[0] < state.getGridWidth() && reqPos[1] >= 0 && reqPos[1] < state.getGridHeight()) {
				final double[] APmap = getLowestMoveAPConsumptionMap(prevPos, character);
				final int currentActionPoint = (Integer) state.getCharacterProperty(character, "currentActionPoint");
				int column = 0, row = 0;
				for (int i = 0; i < state.getGridWidth() * state.getGridHeight(); i++, column++) {
					if (column == state.getGridWidth()) {
						column = 0; row++;
					}
					if (APmap[i] <= currentActionPoint)
						allowed.add(Arrays.asList(column, row));
				}
				state.notifyUpdate(Collections.singletonMap("HighlightGrid", (Object) Collections.unmodifiableList(allowed)));
			}
			return;
		}
		case "RequestItemShop": {
			if (!character.equals(state.getCurrentCharacterName()))
				return;
			return;
		}
		case "GameAction": {
			// check if action is possible
			if (requestActionPossible(character, actionName, options)) {
				Map<String, Object> updates = new HashMap<>();
				switch ((String) options.get("Action")) {
				case "Automation": {
					if (!character.equals(state.getCurrentCharacterName()))
						return;
					System.out.println("Automation " + character);
					switch (state.getCharacters().get(character).getObjectType()) {
					case GameObject.GAMEOBJECT_TYPE_HERO:
						System.out.println("Hero automation not implemented");
						autoHero(character);
						break;
					case GameObject.GAMEOBJECT_TYPE_LINECREEP:
						System.out.println("Linecreep automation not implemented");
						autoLineCreep(character);
						break;
					case GameObject.GAMEOBJECT_TYPE_TOWER:
						System.out.println("Tower automation not implemented");
						autoTower(character);
						break;
					}
					return;
				}
				case "Attack":
					if (!character.equals(state.getCurrentCharacterName()))
						return;
					System.out.println("Game action is attack");
					// apply game rule
					attack(character, state.getCharacterAtPosition(state.getChosenGrid()));
					// notify
					updates.put("Characters", Collections.singleton("ALL"));
					state.notifyUpdate(Collections.unmodifiableMap(updates));
					return;
				case "Move": {
					if (!character.equals(state.getCurrentCharacterName()))
						return;
					System.out.println("Game action is move");
					move (character);
					List<String> characters = Collections.singletonList(character);
					updates = Collections.singletonMap("Characters", (Object) characters);
					state.notifyUpdate(updates);
					return;
				}
				case "BuyItem": {
					if (!character.equals(state.getCurrentCharacterName()))
						return;
					break;
				}
				case "NextRound": {
					if (!character.equals(state.getCurrentCharacterName()))
						return;
					state.nextTurn();
					break;
				}
				case "BeginTurn":
					break;
				case "EndTurn":
					break;
				case "BeginRound": {
					final int roundCount = state.getRoundCount();
					Map<String, Object> thisTeamConfig;
					// spawn creeps
					Set<Integer> teamsWithoutNeutral = new HashSet<Integer> (this.teamConfig.keySet());
					teamsWithoutNeutral.remove(0);
					for (int i : teamsWithoutNeutral) {
						thisTeamConfig = this.teamConfig.get(i);
						int frontNumber = 0;
						// check if barrack destroyed
						
						// spawning
						for (Map<String, Object> front : (List<Map<String, Object>>) thisTeamConfig.get("fronts")) {
							// level up line creeps
							if (roundCount % ((Number) front.get("creepLevelUpRound")).intValue() == 0)
								for (Map.Entry<String, Integer> species : teamCreepLevel.get(i).get(frontNumber).entrySet())
									species.setValue(Math.min(((Number) front.get("creepMaximumLevel")).intValue(), species.getValue() + 1));
							final int[] spawnPoint = new int[]{
									((List<Number>) front.get("creepSpawnPoint")).get(0).intValue(),
									((List<Number>) front.get("creepSpawnPoint")).get(1).intValue()
							};
							boolean meleeUpgraded = true, rangedUpgraded = true;
							for (int otherTeam : teamsWithoutNeutral) {
								boolean barrackSurvived = false;
								if (otherTeam != i) {
									for (String unit : teamStrength.get(otherTeam).get(frontNumber))
										if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_BARRACK) {
											if ("melee".equals(state.getCharacterProperty(unit, "type")))
												meleeUpgraded &= !(Boolean) state.getCharacterProperty(unit, "alive");
											else if ("ranged".equals(state.getCharacterProperty(unit, "type")))
												rangedUpgraded &= !(Boolean) state.getCharacterProperty(unit, "alive");
											barrackSurvived |= (Boolean) state.getCharacterProperty(unit, "alive");
										}
									if (!barrackSurvived) {
										// maximum level bonus
										for (int thisTeamFront : teamCreepLevel.get(i).keySet())
											for (Map.Entry<String, Integer> entry : teamCreepLevel.get(i).get(thisTeamFront).entrySet())
												entry.setValue(((Number)
														((List<Map<String, Object>>) thisTeamConfig.get("fronts"))
															.get(thisTeamFront)
																.get("creepMaximumLevel")).intValue());
										break;
									}
								}
							}
							for (Map<String, Object> type : (List<Map<String, Object>>) front.get("creepSpawnType"))
								if (roundCount % ((Number) type.get("round")).intValue() == 0)
									for (int count = 0,
										end = Math.min(((Number) type.get("maximum")).intValue(),
												((Number) type.get("initial")).intValue() + roundCount / ((Number) type.get("increaseRound")).intValue()); count < end; count++) {
										String name;
										do {
											name = java.util.UUID.randomUUID().toString();
										} while (state.getCharacterType(name) > 0 || state.getCharacterType(name) == GameObject.GAMEOBJECT_TYPE_ROUNDFLAG);
										Map<String, Object> config = (Map<String, Object>) ((Map<String, Object>) charsConfig.get("creepConfiguration"))
												.get(type.get("type"));
										LineCreep creep = new LineCreep();
										state.addCharacter(name, creep, true, true);
										state.setCharacterProperty(name, "characterImage", config.get("characterImage"));
										// specific rule
										if ("melee".equals(type.get("species")) && meleeUpgraded) {
											//
										} else if ("ranged".equals(type.get("species")) || "siege".equals(type.get("species")) && rangedUpgraded) {
											//
										} else switch ((String) type.get("species")) {
										case "melee":
											state.setCharacterProperty(name, "bountyMoney", 0);
										case "ranged":
										case "siege":
										}
										state.setCharacterProperty(name, "level", teamCreepLevel.get(i).get(frontNumber).get(type.get("species")));
									}
							frontNumber++;
						}
					}
					// neutral creeps
					thisTeamConfig = this.teamConfig.get(0);
					break;
				}
				case "EndRound": {
					for (Map.Entry<String, GameCharacter> entry : state.getCharacters().entrySet())
						if (entry.getValue().getRoundsToRevive() > 0) {
							state.setCharacterProperty(entry.getKey(), "roundsToRevive", entry.getValue().getRoundsToRevive() - 1);
							if (entry.getValue().getRoundsToRevive() == 0) {
								state.setCharacterProperty(entry.getKey(), "currentHP", entry.getValue().getMaxHP());
								state.setCharacterProperty(entry.getKey(), "currentMP", entry.getValue().getMaxMP());
								state.setCharacterProperty(entry.getKey(), "currentActionPoint", entry.getValue().getMaxActionPoint());
								List<Number> spawnPoint = (List<Number>)
										teamConfig.get(entry.getValue().getTeamNumber()).get("heroSpawnPoint");
								final int[] pos = {spawnPoint.get(0).intValue(), spawnPoint.get(1).intValue()};
								if (state.getCharacterAtPosition(pos) == null)
									state.setCharacterPosition(entry.getKey(), pos);
								else if (entry.getValue().getObjectType() == GameObject.GAMEOBJECT_TYPE_TREE) {
									state.setCharacterProperty(entry.getKey(), "roundsToRevive", 1);
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
									final int[] spawnPosition = getNearestFreeGrid(pos);
									if (spawnPoint == null)
										throw new RuntimeException("No spawn point found for " + entry.getKey());
									else
										state.setCharacterPosition(entry.getKey(), spawnPosition);
								}
							}
						} else if (entry.getValue().getRoundsToRevive() == 0) {
							state.setCharacterProperty(entry.getKey(), "currentHP", entry.getValue().getCurrentHP() + entry.getValue().getHPGainPerRound());
							state.setCharacterProperty(entry.getKey(), "currentMP", entry.getValue().getCurrentMP() + entry.getValue().getMPGainPerRound());
							state.setCharacterProperty(entry.getKey(), "currentActionPoint", entry.getValue().getMaxActionPoint());
						}
					break;
				}
				}
			}
			break;
		}
		case "Cancel":
			// nothing
			state.notifyUpdate(Collections.singletonMap("Cancel", (Object) "ALL"));
			return;
		}
	}

	@Override
	public boolean requestActionPossible(String character, String actionName, Map<String, Object> options) {
		switch (actionName) {
		case "GameAction":
			switch ((String) options.get("Action")) {
			case "Move" : {
				final int[] targetGrid = state.getChosenGrid(), heroGrid = state.getCharacterPosition(character);
				if (!character.equals(state.getCurrentCharacterName()))
					return false;
				if (targetGrid[0] >= state.getGridWidth() || targetGrid[0] < 0 || targetGrid[1] >= state.getGridHeight() || targetGrid[1] < 0)
					return false;
				return true;
			}
			case "Attack": {
				final int[] targetGrid = state.getChosenGrid(), heroGrid = state.getCharacterPosition(character);
				if (!character.equals(state.getCurrentCharacterName()))
					return false;
				if (targetGrid[0] >= state.getGridWidth() || targetGrid[0] < 0 || targetGrid[1] >= state.getGridHeight() || targetGrid[1] < 0)
					return false;
				Hero hero = (Hero) state.getCharacters().get(character);
				return state.getCharacterAtPosition(targetGrid) != null &&	// has hero, linecreep or tower
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
	
	// utilities
	
	private double[] getLowestMoveAPConsumptionMap (int[] start, String character) {
		final float[] terrain = state.getTerrain();
		final int width = state.getGridWidth(), height = state.getGridHeight();
		final int[][] dirs = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		final double TERRAIN_CONST = 1;	// TODO terrain factor
		Queue<int[]> q = new LinkedList<>();
		double[] map = new double[width * height];
//		Arrays.fill(map, character.getMaxActionPoint() + 1);
		Arrays.fill(map, (Integer) state.getCharacterProperty(character, "currentActionPoint") + 1);
		final double APConsumptionPerGrid = (Double) state.getCharacterProperty(character, "APUsedInMovingOneGrid");
		map[start[0] + start[1] * width] = 0;
		q.add(start);
		while (!q.isEmpty()) {
			final int[] prevPos = q.remove();
			for (byte i = 0; i < 4; i++) 
				if (prevPos[0] + dirs[i][0] < width && prevPos[0] + dirs[i][0] >= 0 &&
						prevPos[1] + dirs[i][1] < height && prevPos[1] + dirs[i][1] >= 0 &&
						state.getCharacterAtPosition(prevPos[0] + dirs[i][0], prevPos[1] + dirs[i][1]) == null &&
						terrainType[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * state.getGridWidth()] != GameState.TERRAIN_TYPE_CLIFF) {
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
	
	private int[] getNearestFreeGrid (int[] pos) {
		final int limit = Math.max(pos[0], Math.max(state.getGridWidth() - pos[0], Math.max(pos[1], state.getGridHeight() - pos[1])));
		for (int r = 1; r <= limit; r++)
			for (int i = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth(), pos[0] + r); i < end; i++)
				if (pos[1] + Math.abs(i - pos[0]) - r >= 0
					&& state.getCharacterAtPosition(i, pos[1] + Math.abs(i - pos[0]) - r) == null)
					// below
					return new int[] {i, pos[1] + Math.abs(i - pos[0]) - r};
				else if (pos[1] - Math.abs(i - pos[0]) + r < state.getGridHeight()
						&& state.getCharacterAtPosition(i, pos[1] - Math.abs(i - pos[0]) + r) == null)
					// above
					return new int[] {i, pos[1] - Math.abs(i - pos[0]) + r};
		return null;
	}
	
	private void attack (String character, String targetCharacter) {
		if ("OutOfBound".equals(targetCharacter) || targetCharacter == null)
			return;
		
		// TODO structure protection
		switch (state.getCharacterType(targetCharacter)) {
		case GameObject.GAMEOBJECT_TYPE_TOWER: {
			final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
			final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
			final int order = (Integer) state.getCharacterProperty(targetCharacter, "order");
			List<String> strength = teamStrength.get(team).get(front);
			for (String unit : strength)
				if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_TOWER
						&& (Boolean) state.getCharacterProperty(unit, "alive")
						&& (Integer) state.getCharacterProperty(unit, "order") < order)
					return;
			break;
		}
		case GameObject.GAMEOBJECT_TYPE_BARRACK: {
			// only if the corresponding front has fallen
			final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
			final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
			List<String> strength = teamStrength.get(team).get(front);
			for (String unit : strength)
				if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_TOWER
					&& (Boolean) state.getCharacterProperty(unit, "alive"))
						return;
			break;
		}
		case GameObject.GAMEOBJECT_TYPE_ANCIENT: {
			final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
			final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
			List<String> strength = teamStrength.get(team).get(front);
			for (String unit : strength)
				if ((Boolean) state.getCharacterProperty(unit, "alive"))
					return;
			break;
		}
		}

		int APused = (Integer) state.getCharacterProperty(character, "APUsedWhenAttack");
		int damage = GameCharacter.getActualDamage(
				(Integer) state.getCharacterProperty(targetCharacter, "totalPhysicalAttack"),
				(Integer) state.getCharacterProperty(targetCharacter, "totalPhysicalDefence"));
		
		state.setCharacterProperty(character, "currentActionPoint",
				(Integer) state.getCharacterProperty(character, "currentActionPoint") - APused);
		state.setCharacterProperty(targetCharacter, "currentHP",
				(Integer) state.getCharacterProperty(targetCharacter, "currentHP") - damage);
		
		characterDamageCheck (targetCharacter);
		if (!(Boolean) state.getCharacterProperty(targetCharacter, "alive")) {
			// TODO check game ended
			//
			switch (state.getCharacterType(character)) {
			case GameObject.GAMEOBJECT_TYPE_HERO:
				state.setCharacterProperty(character, "money",
						(Integer) state.getCharacterProperty(character, "money") +
						(Integer) state.getCharacterProperty(targetCharacter, "bountyMoney")
						);
				state.setCharacterProperty(character, "experience",
						(Integer) state.getCharacterProperty(character, "experience") +
						(Integer) state.getCharacterProperty(targetCharacter, "bountyExp")
						);
			}
			// TODO reset character position
			state.setCharacterPosition(targetCharacter, null);
		}
	}
	
	private void move (String character) {
		final int[] target = state.getChosenGrid(), source = state.getCharacterPosition(character);
		if (state.getCharacterAtPosition(target) == null) {
			// move
			double[] map = getLowestMoveAPConsumptionMap(source, character);
			if (map[target[0] + target[1] * state.getGridWidth()] < (Integer) state.getCharacterProperty(character, "currentActionPoint")) {
				state.setCharacterPosition(character, target);
				state.setCharacterProperty(character, "currentActionPoint",
						(Integer) state.getCharacterProperty(character, "currentActionPoint") - map[target[0] + target[1] * state.getGridWidth()]
						);
			}
		}
	}
	
	private void characterDamageCheck (String targetCharacter) {
		
	}
	
	private void autoHero(String character) {
		
	}
	private void autoLineCreep(String character) {
		
	}
	private void autoTower(String character) {
		final int friendlyTeamNumber = state.getCharacters().get(character).getTeamNumber();
		final Queue<String> attackQueue = new LinkedList<>();
		final Tower tower = (Tower) state.getCharacters().get(character);
		final int[] pos = state.getCharacterPosition(character);
		for (int r = 1; r <= tower.getTotalPhysicalAttackArea(); r++)
			for (int i = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth(), pos[0] + r); i < end; i++) {
				if (pos[1] + Math.abs(i - pos[0]) - r >= 0) {
					final String target = state.getCharacterAtPosition(i, pos[1] + Math.abs(i - pos[0]) - r);
					if (state.getCharacterType(target) > 0
							&& state.getCharacters().get(target).getTeamNumber() != friendlyTeamNumber)
						attackQueue.add(target);
				}
				if (pos[1] - Math.abs(i - pos[0]) + r < state.getGridHeight()) {
					final String target = state.getCharacterAtPosition(i, pos[1] - Math.abs(i - pos[0]) + r);
					if (state.getCharacterType(target) > 0
							&& state.getCharacters().get(target).getTeamNumber() != friendlyTeamNumber)
						attackQueue.add(target);
				}
			}
		while (tower.getCurrentActionPoint() > 0) {
			final String target = attackQueue.poll();
			final GameCharacter targetCharacter = state.getCharacters().get(target);
			while (targetCharacter.isAlive() && tower.getCurrentActionPoint() > 0)
				attack(character, target);
		}
	}
}
