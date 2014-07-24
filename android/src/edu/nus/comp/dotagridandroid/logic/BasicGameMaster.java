package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;

import edu.nus.comp.dotagridandroid.appsupport.JsonConverter;

@SuppressWarnings("unchecked")
public class BasicGameMaster extends GameMaster {
	private int[] terrainType;
	private Map<Integer, Map<String, Object>> teamConfig;
	private Map<Integer, Map<Integer, Set<String>>> teamStrength;
	private Map<Integer, Map<Integer, Map<String, Integer>>> teamCreepLevel;
	private Map<String, Object> stateData;
	private Map<String, Object> charsConfig;
	private Map<Integer, Map<String, Integer>> teamEnemyAttackPrioritised;
	private int priorityDecreaseRounds;

	@Override
	public void serverNotify() {
	}

	@Override
	public void initialise() {
		teamConfig = new HashMap<>();
		stateData = new ConcurrentHashMap<>();
		// TODO load character
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
			priorityDecreaseRounds = ((Number) terrainConfig.get("priorityDecreaseRounds")).intValue();
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
		teamEnemyAttackPrioritised = new HashMap<>();
		teamStrength = new HashMap<>();
		try {
			for (Map<String, Object> team : (List<Map<String, Object>>) terrainConfig.get("teams")) {
				final int teamNumber = ((Number) team.get("team")).intValue();
				teamConfig.put(teamNumber, team);
				teamEnemyAttackPrioritised.put(teamNumber, new HashMap<String, Integer>());
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
					Map<Integer, Set<String>> teamStrength = new HashMap<>();
					for (Map<String, Object> tower : (List<Map<String, Object>>) terrainConfig.get("towers"))
						if ((Integer) state.getCharacterProperty((String) tower.get("name"), "teamNumber") == teamNumber)
							if (teamStrength.containsKey(state.getCharacterProperty((String) tower.get("name"), "front")))
								teamStrength.get(state.getCharacterProperty((String) tower.get("name"), "front")).add((String) tower.get("name"));
							else
								teamStrength.put(
										(Integer) state.getCharacterProperty((String) tower.get("name"), "front"),
										new HashSet<String>(Arrays.asList((String) tower.get("name"))));
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
				teamCreepLevel.get(team).get(front).put(type, -1);
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
					teamStrength.put(team, new HashMap<Integer, Set<String>>());
				if (!teamStrength.get(team).containsKey(front))
					teamStrength.get(team).put(front, new HashSet<String>());
				teamStrength.get(team).get(front).add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Map.Entry<String, GameCharacter> characterObject : state.getCharacters().entrySet())
			characterObject.getValue().initialise();
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
					move (character, state.getChosenGrid());
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
				case "NextTurn": {
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
					for (Map.Entry<Integer, Map<String, Integer>> attackPriority : teamEnemyAttackPrioritised.entrySet())
						for (Map.Entry<String, Integer> entry : attackPriority.getValue().entrySet())
							entry.setValue(entry.getValue() - 1);
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
										final Map<String, Object> config = (Map<String, Object>) ((Map<String, Object>) charsConfig.get("creepConfiguration"))
												.get(type.get("type"));
										final Map<String, Object> configUpgraded = (Map<String, Object>) ((Map<String, Object>) charsConfig.get("creepConfiguration")).get(type.get("upgradedType"));
										final int level = teamCreepLevel.get(i).get(frontNumber).get(type.get("species"));
										LineCreep creep = new LineCreep();
										state.addCharacter(name, creep, true, true);
										state.setCharacterProperty(name, "characterImage", config.get("characterImage"));
										// specific rule
										if ("melee".equals(type.get("species")) && meleeUpgraded) {
											state.setCharacterProperty(name, "bountyMoney", configUpgraded.get("bountyMoney"));
											state.setCharacterProperty(name, "startingHP",
													((Number) configUpgraded.get("startingHP")).intValue() + ((Number) configUpgraded.get("startingHP-levelMultiplier")).intValue() * level);
											state.setCharacterProperty(name, "startingMP", configUpgraded.get("startingMP"));
											state.setCharacterProperty(name, "startingPhysicalAttack", configUpgraded.get("startingPhysicalAttack"));
											state.setCharacterProperty(name, "startingPhysicalAttackArea", configUpgraded.get("startingPhysicalAttackArea"));
											state.setCharacterProperty(name, "startingPhysicalAttackSpeed", configUpgraded.get("startingPhysicalAttackSpeed"));
											state.setCharacterProperty(name, "startingPhysicalDefence", configUpgraded.get("startingPhysicalDefence"));
											state.setCharacterProperty(name, "startingMagicResistance", configUpgraded.get("startingMagicResistance"));
											state.setCharacterProperty(name, "startingMovementSpeed", configUpgraded.get("startingMovementSpeed"));
											state.setCharacterProperty(name, "basicPhysicalAttack",
													((Number) configUpgraded.get("basicPhysicalAttack")).intValue()
													+ ((Number) configUpgraded.get("basicPhysicalAttack-levelMultiplier")).intValue() * level);
										} else if ("ranged".equals(type.get("species")) || "siege".equals(type.get("species")) && rangedUpgraded) {
											state.setCharacterProperty(name, "bountyMoney", configUpgraded.get("bountyMoney"));
											state.setCharacterProperty(name, "startingHP",
													((Number) configUpgraded.get("startingHP")).intValue() + ((Number) configUpgraded.get("startingHP-levelMultiplier")).intValue() * level);
											state.setCharacterProperty(name, "startingMP", configUpgraded.get("startingMP"));
											state.setCharacterProperty(name, "startingPhysicalAttack", configUpgraded.get("startingPhysicalAttack"));
											state.setCharacterProperty(name, "startingPhysicalAttackArea", configUpgraded.get("startingPhysicalAttackArea"));
											state.setCharacterProperty(name, "startingPhysicalAttackSpeed", configUpgraded.get("startingPhysicalAttackSpeed"));
											state.setCharacterProperty(name, "startingPhysicalDefence", configUpgraded.get("startingPhysicalDefence"));
											state.setCharacterProperty(name, "startingMagicResistance", configUpgraded.get("startingMagicResistance"));
											state.setCharacterProperty(name, "startingMovementSpeed", configUpgraded.get("startingMovementSpeed"));
											state.setCharacterProperty(name, "basicPhysicalAttack",
													((Number) configUpgraded.get("basicPhysicalAttack")).intValue()
													+ ((Number) configUpgraded.get("basicPhysicalAttack-levelMultiplier")).intValue() * level);
										} else {
											state.setCharacterProperty(name, "bountyMoney", config.get("bountyMoney"));
											state.setCharacterProperty(name, "startingHP",
													((Number) config.get("startingHP")).intValue() + ((Number) config.get("startingHP-levelMultiplier")).intValue() * level);
											state.setCharacterProperty(name, "startingMP", config.get("startingMP"));
											state.setCharacterProperty(name, "startingPhysicalAttack", config.get("startingPhysicalAttack"));
											state.setCharacterProperty(name, "startingPhysicalAttackArea", config.get("startingPhysicalAttackArea"));
											state.setCharacterProperty(name, "startingPhysicalAttackSpeed", config.get("startingPhysicalAttackSpeed"));
											state.setCharacterProperty(name, "startingPhysicalDefence", config.get("startingPhysicalDefence"));
											state.setCharacterProperty(name, "startingMagicResistance", config.get("startingMagicResistance"));
											state.setCharacterProperty(name, "startingMovementSpeed", config.get("startingMovementSpeed"));
											state.setCharacterProperty(name, "basicPhysicalAttack",
													((Number) config.get("basicPhysicalAttack")).intValue()
													+ ((Number) config.get("basicPhysicalAttack-levelMultiplier")).intValue() * level);
										}
										// IMPORTANT, must initialise
										creep.initialise();
										state.setCharacterProperty(name, "Automation-Checkpoints",
												new LinkedList<String>((List<String>) type.get("checkpoints")));
										state.setCharacterProperty(name, "Automation-Checkpoints-Radius",
												((Number) type.get("checkpoints-radius")).intValue());
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
				for (Map.Entry<String, GameCharacter> characterObject : state.getCharacters().entrySet())
					characterObject.getValue().updateProperties();
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
				final int[] targetGrid, attackerGrid = state.getCharacterPosition(character);
				if (options.containsKey("TargetGrid")) {
					final List<Number> pos = (List<Number>) options.get("TargetGrid");
					targetGrid = new int[] {pos.get(0).intValue(), pos.get(1).intValue()};
				} else
					targetGrid = state.getChosenGrid();
				final String targetCharacter = state.getCharacterAtPosition(targetGrid);
				if (targetCharacter == null || state.getCharacterType(targetCharacter) == GameObject.GAMEOBJECT_TYPE_TREE)
					return false;
				else if (!character.equals(state.getCurrentCharacterName()))
					return false;
				else if (targetGrid[0] >= state.getGridWidth() || targetGrid[0] < 0 || targetGrid[1] >= state.getGridHeight() || targetGrid[1] < 0)
					return false;
				else if ((Integer) state.getCharacterProperty(character, "teamNumber") == (Integer) state.getCharacterProperty(targetCharacter, "teamNumber"))
					return false;	// no friendly fire
				
				// structure protection
				switch (state.getCharacterType(targetCharacter)) {
				case GameObject.GAMEOBJECT_TYPE_TOWER: {
					final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
					final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
					final int order = (Integer) state.getCharacterProperty(targetCharacter, "order");
					Set<String> strength = teamStrength.get(team).get(front);
					for (String unit : strength)
						if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_TOWER
							&& (Boolean) state.getCharacterProperty(unit, "alive")
							&& (Integer) state.getCharacterProperty(unit, "order") < order)
							return false;
					break;
				}
				case GameObject.GAMEOBJECT_TYPE_BARRACK: {
					// only if the corresponding front has fallen
					final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
					final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
					Set<String> strength = teamStrength.get(team).get(front);
					for (String unit : strength)
						if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_TOWER
							&& (Boolean) state.getCharacterProperty(unit, "alive"))
							return false;
					break;
				}
				case GameObject.GAMEOBJECT_TYPE_ANCIENT: {
					final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
					final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
					Set<String> strength = teamStrength.get(team).get(front);
					for (String unit : strength)
						if ((Boolean) state.getCharacterProperty(unit, "alive"))
							return false;
					break;
				}
				}
				
				switch (state.getCharacterType(character)) {
				case GameObject.GAMEOBJECT_TYPE_HERO: {
					final Hero hero = (Hero) state.getCharacters().get(character);
					return state.getCharacterAtPosition(targetGrid) != null &&	// has hero, linecreep or tower
							hero.getTotalPhysicalAttackArea() + hero.getTotalItemAddPhysicalAttackArea()
							>= Math.abs(targetGrid[0] - attackerGrid[0])
							+ Math.abs(targetGrid[1] - attackerGrid[1]) &&	// within attack area
							hero.getCurrentActionPoint() > Hero.MIN_PHYSICAL_ATTACK_CONSUME_AP + (1 - hero.getTotalPhysicalAttackSpeed() / GameCharacter.MAX_PHYSICAL_ATTACK_SPEED)
								* Hero.PHYSICAL_ATTACK_CONSUME_AP;	// has enough action points
				}
				}
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
					final double APconsumed = APConsumptionPerGrid +
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
			for (int x = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth() - 1, pos[0] + r); x <= end; x++)
				if (pos[1] + Math.abs(x - pos[0]) - r >= 0
					&& state.getCharacterAtPosition(x, pos[1] + Math.abs(x - pos[0]) - r) == null)
					// below
					return new int[] {x, pos[1] + Math.abs(x - pos[0]) - r};
				else if (pos[1] - Math.abs(x - pos[0]) + r < state.getGridHeight()
						&& state.getCharacterAtPosition(x, pos[1] - Math.abs(x - pos[0]) + r) == null)
					// above
					return new int[] {x, pos[1] - Math.abs(x - pos[0]) + r};
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
			Set<String> strength = teamStrength.get(team).get(front);
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
			Set<String> strength = teamStrength.get(team).get(front);
			for (String unit : strength)
				if (state.getCharacterType(unit) == GameObject.GAMEOBJECT_TYPE_TOWER
					&& (Boolean) state.getCharacterProperty(unit, "alive"))
						return;
			break;
		}
		case GameObject.GAMEOBJECT_TYPE_ANCIENT: {
			final int team = (Integer) state.getCharacterProperty(targetCharacter, "teamNumber");
			final int front = (Integer) state.getCharacterProperty(targetCharacter, "front");
			Set<String> strength = teamStrength.get(team).get(front);
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
		
		characterDamageCheck (character, targetCharacter);
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
	
	private void move (final String character, final int... target) {
		final int[] source = state.getCharacterPosition(character);
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
	
	private void characterDamageCheck (String characterAttacker, String targetCharacter) {
		if (state.getCharacterType(targetCharacter) == GameObject.GAMEOBJECT_TYPE_HERO)
			teamEnemyAttackPrioritised.get(state.getCharacterProperty(targetCharacter, "teamNumber")).put(characterAttacker, priorityDecreaseRounds);
	}

	// move to checkpoint
	
	private void autoHero(String character) {
		
	}
	private void autoLineCreep(String character) {
		final int friendlyTeamNumber = state.getCharacters().get(character).getTeamNumber();
		final List<String> friendlyNames = new LinkedList<>();
		final Queue<Pair<Integer, Integer, String>> attackTargetPriorityQueue = new PriorityQueue<>(),
				possibleTargetPriorityQueue = new PriorityQueue<>();
		final int sight = (Integer) state.getCharacterProperty(character, "sight");
		while (((Integer) state.getCharacterProperty(character, "currentActionPoint")) > 0) {
			final int[] pos = state.getCharacterPosition(character);
			// find enemy
			for (int r = 1; r <= sight; r++)
				for (int x = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth() - 1, pos[0] + r); x <= end; x++) {
					if (pos[1] + Math.abs(x - pos[0]) - r >= 0) {
						final String target = state.getCharacterAtPosition(x, pos[1] + Math.abs(x - pos[0]) - r);
						final Map<String, Object> actionOption = new HashMap<>();
						actionOption.put("Action", "Attack");
						actionOption.put("TargetGrid", Arrays.asList(x, pos[1] + Math.abs(x - pos[0]) - r));
						if ((Integer) state.getCharacterProperty(target, "teamNumber") == friendlyTeamNumber)
							continue;
						// trees are exceptions
						Pair<Integer, Integer, String> entry;
						switch (state.getCharacterType(target)) {
						case GameObject.GAMEOBJECT_TYPE_HERO:
						case GameObject.GAMEOBJECT_TYPE_LINECREEP:
						case GameObject.GAMEOBJECT_TYPE_TOWER:
						case GameObject.GAMEOBJECT_TYPE_BARRACK:
						case GameObject.GAMEOBJECT_TYPE_ANCIENT:
							entry = teamEnemyAttackPrioritised.get(friendlyTeamNumber).containsKey(target)
							&& teamEnemyAttackPrioritised.get(friendlyTeamNumber).get(target) > 0 ?
									new Pair<Integer, Integer, String>(0, Math.abs(x - pos[0]) + Math.abs(r - x + pos[0]), target)
									: new Pair<Integer, Integer, String>(1, Math.abs(x - pos[0]) + Math.abs(r - x + pos[0]), target);
							if (requestActionPossible(character, "GameAction", actionOption))
								attackTargetPriorityQueue.add(entry);
							else
								possibleTargetPriorityQueue.add(entry);
							break;
						}
					}
					// possible duplicate
					if (Math.abs(x - pos[0]) < r && pos[1] - Math.abs(x - pos[0]) + r < state.getGridHeight()) {
						final String target = state.getCharacterAtPosition(x, pos[1] - Math.abs(x - pos[0]) + r);
						final Map<String, Object> actionOption = new HashMap<>();
						actionOption.put("Action", "Attack");
						actionOption.put("TargetGrid", Arrays.asList(x, pos[1] - Math.abs(x - pos[0]) + r));
						if ((Integer) state.getCharacterProperty(target, "teamNumber") == friendlyTeamNumber)
							continue;
						// same routine here
						Pair<Integer, Integer, String> entry;
						switch (state.getCharacterType(target)) {
						case GameObject.GAMEOBJECT_TYPE_HERO:
						case GameObject.GAMEOBJECT_TYPE_LINECREEP:
						case GameObject.GAMEOBJECT_TYPE_TOWER:
						case GameObject.GAMEOBJECT_TYPE_BARRACK:
						case GameObject.GAMEOBJECT_TYPE_ANCIENT:
							entry = teamEnemyAttackPrioritised.get(friendlyTeamNumber).containsKey(target)
							&& teamEnemyAttackPrioritised.get(friendlyTeamNumber).get(target) > 0 ?
									new Pair<Integer, Integer, String>(0, Math.abs(x - pos[0]) + Math.abs(r - x + pos[0]), target)
									: new Pair<Integer, Integer, String>(1, Math.abs(x - pos[0]) + Math.abs(r - x + pos[0]), target);
							if (requestActionPossible(character, "GameAction", actionOption))
								attackTargetPriorityQueue.add(entry);
							else
								possibleTargetPriorityQueue.add(entry);
							break;
						}
					}
				}
			for (Pair<Integer, Integer, String> targetCharacter : attackTargetPriorityQueue)
				while ((Integer) state.getCharacterProperty(character, "currentActionPoint") > 0)
					attack(character, targetCharacter.data);
			if ((Integer) state.getCharacterProperty(character, "currentActionPoint") > 0) {
				// move to nearest possible target
				final double[] apMap = getLowestMoveAPConsumptionMap(pos, character);
				final int currentAPPoint = (Integer) state.getCharacterProperty(character, "currentActionPoint");
				String targetCharacter;
				int[] targetPos;
				boolean engagingEnemy = false;
				while (!possibleTargetPriorityQueue.isEmpty()) {
					targetCharacter = possibleTargetPriorityQueue.poll().data;
					targetPos = state.getCharacterPosition(targetCharacter);
					if (targetPos == null)
						continue;
					engagingEnemy = true;
					boolean searching = true;
					final int limit = Math.max(pos[0], Math.max(state.getGridWidth() - pos[0], Math.max(pos[1], state.getGridHeight() - pos[1])));
					for (int r = 1; searching && r <= limit; r++)
						for (int x = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth() - 1, pos[0] + r); searching && x <= end; x++)
							if (pos[1] - Math.abs(x - pos[0]) + r >= 0
									&& apMap[x + state.getGridWidth() * (pos[1] - Math.abs(x - pos[0]) + r)] <= currentAPPoint) {
								move(character, x, pos[1] - Math.abs(x - pos[0]) + r);
								searching = false;
							} else if (pos[1] + Math.abs(x - pos[0]) - r < state.getGridHeight()
									&& apMap[x + state.getGridWidth() * (pos[1] + Math.abs(x - pos[0]) - r)] <= currentAPPoint) {
								move(character, x, pos[1] + Math.abs(x -pos[0]) + r);
								searching = false;
							}
				}
				if (!engagingEnemy) {
					// move to checkpoint
					final int[] characterPos = state.getCharacterPosition(character);
					final List<String> targets = (List<String>) state.getCharacterProperty(character, "Automation-Checkpoints");
					final int radius = (Integer) state.getCharacterProperty(character, "Automation-Checkpoints-Radius");
					while (true) {
						targetCharacter = targets.get(0);
						targetPos = state.getCharacterPosition(targetCharacter);
						if (targetPos == null || Math.abs(targetPos[0] - pos[0]) + Math.abs(targetPos[1] - pos[1]) <= radius) {
							targets.remove(0);
							continue;
						}
						final int limit = Math.max(targetPos[0], Math.max(state.getGridWidth() - targetPos[0], Math.max(targetPos[1], state.getGridHeight() - targetPos[1])));
						boolean searching = true;
						for (int r = 1; searching && r < limit; r++)
							for (int x = Math.max(0, targetPos[0] - r), end = Math.min(state.getGridWidth() - 1, targetPos[0] + r); searching && x <= end; x++)
								if (targetPos[1] - Math.abs(x - targetPos[0]) + r >= 0
										&& apMap[x + state.getGridWidth() * (targetPos[1] - Math.abs(x - targetPos[0]) + r)] <= currentAPPoint) {
									move(character, x, targetPos[1] - Math.abs(x - targetPos[0]) + r);
									searching = false;
								} else if (targetPos[1] + Math.abs(x - targetPos[0]) - r < state.getGridHeight()
										&& apMap[x + state.getGridWidth() * (targetPos[1] + Math.abs(x - targetPos[0]) - r)] <= currentAPPoint) {
									move(character, x, targetPos[1] + Math.abs(x - targetPos[0]) - r);
									searching = false;
								}
						break;
					}
					break;
				}
			}
		}
	}
	private boolean[] getFriendlySight(List<String> names) {
		final int width = state.getGridWidth(), height = state.getGridHeight();
		final boolean[] map = new boolean[width * height];
		for (String name : names) {
			SegmentNode<Double> shadow = null;
			final int[] pos = state.getCharacterPosition(name);
			if (pos != null) {
				final int sight = (Integer) state.getCharacterProperty(name, "sight");
				final double[] source = {pos[0] + .5, pos[1] + .5};
				map[pos[0] + pos[1] * width] = true;
				for (int radius = 1; radius <= sight; radius++) {
					if (pos[1] >= radius) {
						final int y = pos[1] - radius;
						for (int x = Math.max(0, pos[0] - radius); x < Math.min(width, pos[0] + radius); x++)
							if (Math.hypot(x - pos[0], y - pos[1]) <= sight) {
								map[x + y * width] |= shadow == null ? true :
									shadow.find(getAngle((y + .5 - source[1]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]),
											(x + .5 - source[0]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]))) == null;
								if (state.getCharacterAtPosition(x, pos[1] - radius) != null) {
									// obstacle - will add to shadowMap
									final List<Double> cornerAngles = Arrays.asList(
											getAngle((y - source[1]) / Math.hypot(x - source[0], y - source[1]), (x - source[0]) / Math.hypot(x - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x - source[0], y + 1 - source[1]), (x - source[0]) / Math.hypot(x - source[0], y + 1 - source[1])),
											getAngle((y - source[1]) / Math.hypot(x + 1 - source[0], y - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]))
									);
									if (shadow == null)
										shadow = new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles));
									else {
										boolean completeShadow = true;
										for (double angle : cornerAngles)
											if (shadow.find(angle) == null) {
												completeShadow = false;
												break;
											}
										if (completeShadow)
											shadow = shadow.insert(new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles)));
									}
								}
							}
					}
					
					if (pos[0] + radius < width) {
						final int x = pos[0] + radius;
						for (int y = Math.max(0, pos[1] - radius); y < Math.min(height, pos[1] + radius); y++)
							// same routine
							if (Math.hypot(x - pos[0], y - pos[1]) <= sight) {
								map[x + y * width] |= shadow == null ? true :
									shadow.find(getAngle((y + .5 - source[1]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]),
											(x + .5 - source[0]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]))) == null;
								if (state.getCharacterAtPosition(x, pos[1] - radius) != null) {
									// obstacle - will add to shadowMap
									final List<Double> cornerAngles = Arrays.asList(
											getAngle((y - source[1]) / Math.hypot(x - source[0], y - source[1]), (x - source[0]) / Math.hypot(x - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x - source[0], y + 1 - source[1]), (x - source[0]) / Math.hypot(x - source[0], y + 1 - source[1])),
											getAngle((y - source[1]) / Math.hypot(x + 1 - source[0], y - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]))
									);
									if (shadow == null)
										shadow = new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles));
									else {
										boolean completeShadow = true;
										for (double angle : cornerAngles)
											if (shadow.find(angle) == null) {
												completeShadow = false;
												break;
											}
										if (completeShadow)
											shadow = shadow.insert(new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles)));
									}
								}
							}
					}
					
					if (pos[1] + radius < height) {
						final int y = pos[1] + radius;
						for (int x = Math.min(width - 1, pos[0] + radius); x > Math.max(0, pos[0] - radius); x--)
							// same routine
							if (Math.hypot(x - pos[0], y - pos[1]) <= sight) {
								map[x + y * width] |= shadow == null ? true :
									shadow.find(getAngle((y + .5 - source[1]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]),
											(x + .5 - source[0]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]))) == null;
								if (state.getCharacterAtPosition(x, pos[1] - radius) != null) {
									// obstacle - will add to shadowMap
									final List<Double> cornerAngles = Arrays.asList(
											getAngle((y - source[1]) / Math.hypot(x - source[0], y - source[1]), (x - source[0]) / Math.hypot(x - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x - source[0], y + 1 - source[1]), (x - source[0]) / Math.hypot(x - source[0], y + 1 - source[1])),
											getAngle((y - source[1]) / Math.hypot(x + 1 - source[0], y - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]))
									);
									if (shadow == null)
										shadow = new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles));
									else {
										boolean completeShadow = true;
										for (double angle : cornerAngles)
											if (shadow.find(angle) == null) {
												completeShadow = false;
												break;
											}
										if (completeShadow)
											shadow = shadow.insert(new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles)));
									}
								}
							}
					}
					
					if (pos[0] >= radius) {
						final int x = pos[0] - radius;
						for (int y = Math.min(height - 1, pos[1] + radius); y > Math.max(0, pos[1] - radius); y--)
							// same routine
							if (Math.hypot(x - pos[0], y - pos[1]) <= sight) {
								map[x + y * width] |= shadow == null ? true :
									shadow.find(getAngle((y + .5 - source[1]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]),
											(x + .5 - source[0]) / Math.hypot(x + .5 - source[0], y + .5 - source[1]))) == null;
								if (state.getCharacterAtPosition(x, pos[1] - radius) != null) {
									// obstacle - will add to shadowMap
									final List<Double> cornerAngles = Arrays.asList(
											getAngle((y - source[1]) / Math.hypot(x - source[0], y - source[1]), (x - source[0]) / Math.hypot(x - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x - source[0], y + 1 - source[1]), (x - source[0]) / Math.hypot(x - source[0], y + 1 - source[1])),
											getAngle((y - source[1]) / Math.hypot(x + 1 - source[0], y - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y - source[1])),
											getAngle((y + 1 - source[1]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]), (x + 1 - source[0]) / Math.hypot(x + 1 - source[0], y + 1 - source[1]))
									);
									if (shadow == null)
										shadow = new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles));
									else {
										boolean completeShadow = true;
										for (double angle : cornerAngles)
											if (shadow.find(angle) == null) {
												completeShadow = false;
												break;
											}
										if (completeShadow)
											shadow = shadow.insert(new SegmentNode<Double>(Collections.min(cornerAngles), Collections.max(cornerAngles)));
									}
								}
							}
					}
				}
			}
		}
		return map;
	}
	private static double getAngle(double sinVal, double cosVal) {
		final double error = 1e-7;
		if (Math.abs(sinVal) < error || Math.abs(cosVal) < error)
			return 0;
		if (sinVal > 0)
			return Math.acos(cosVal);
		else
			return Math.PI * 2 - Math.acos(cosVal);
	}
	private void autoTower(String character) {
		final int friendlyTeamNumber = state.getCharacters().get(character).getTeamNumber();
		final Queue<String> attackQueue = new LinkedList<>();
		final Tower tower = (Tower) state.getCharacters().get(character);
		final int[] pos = state.getCharacterPosition(character);
		for (int r = 1; r <= tower.getTotalPhysicalAttackArea(); r++)
			for (int i = Math.max(0, pos[0] - r), end = Math.min(state.getGridWidth() - 1, pos[0] + r); i <= end; i++) {
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
	private static class SegmentNode <T extends Comparable<T>>  {
		private T start, end, max;
		private int depth;
		private SegmentNode<T> left, right, father;
		private SegmentNode (T start, T end) {
			if (start.compareTo(end) > 0)
				throw new RuntimeException("SegmentNode: start > end!");
			this.start = start; this.end = end; this.max = end;
		}
		private SegmentNode<T> insert(SegmentNode<T> node) {
			if (node == null)
				return this;
			SegmentNode<T> father = this;
			while (true)
				if (father.end.compareTo(node.end) < 0 && father.left != null)
					father = father.left;
				else if (father.end.compareTo(node.end) >= 0 && father.right != null)
					father = father.right;
				else
					break;
			if (father.end.compareTo(node.end) < 0)
				father.left = node;
			else
				father.right = node;
			return balance(father);
		}
		private SegmentNode<T> delete(SegmentNode<T> node) {
			if (left != node && right != node)
				return null;
			else if (left == node) {
				// delete left node
				if (node.left == null && node.right == null) {
					left = null;
					return balance(this);
				} else if (node.right == null) {
					SegmentNode<T> succ = node.left;
					while (succ.right != null)
						succ = succ.right;
					succ.father.delete (succ);
					succ.father = this;
					succ.left = node.left;
					succ.right = node.right;
					left = succ;
					return balance(succ);
				} else {
					SegmentNode<T> succ = node.right;
					while (succ.left != null)
						succ = succ.left;
					succ.father.delete(succ);
					succ.father = this;
					succ.left = node.left;
					succ.right = node.right;
					left = succ;
					return balance(succ);
				}
			} else {
				if (node.left == null && node.right == null) {
					right = null;
					return balance(this);
				} else if (node.right == null) {
					SegmentNode<T> succ = node.left;
					while (succ.right != null)
						succ = succ.right;
					succ.father.delete (succ);
					succ.father = this;
					succ.left = node.left;
					succ.right = node.right;
					right = succ;
					return balance(succ);
				} else {
					SegmentNode<T> succ = node.right;
					while (succ.left != null)
						succ = succ.left;
					succ.father.delete(succ);
					succ.father = this;
					succ.left = node.left;
					succ.right = node.right;
					right = succ;
					return balance(succ);
				}
			}
		}
		private static <T extends Comparable<T>> SegmentNode<T> balance(SegmentNode<T> node) {
			// balance node and its ancestors
			while (true) {
				if (node.father == null) {
					// take custody
					final int heavy = (node.left == null ? 0 : node.left.depth) - (node.right == null ? 0 : node.right.depth);
					if (heavy > 1) {
						// left heavy - rotate right
						if ((node.left.right == null ? 0 : node.left.right.depth) > (node.left.left == null ? 0 : node.left.left.depth)) {
							// rotate left first
							SegmentNode<T> left = node.left.right;
							node.left.right = left.left;
							left.left = node.left;
							node.left = left;
							node.left.father = node;
							node.left.left.father = node.left;
							if (node.left.left.right != null)
								node.left.left.right.father = node.left.left;
							node.left.left.depth = Math.max(
									node.left.left.left == null ? 0 : node.left.left.left.depth,
									node.left.left.right == null ? 0 : node.left.left.right.depth) + 1;
							node.left.left.max = node.left.left.right == null ? node.left.left.end : node.left.left.right.max;
						}
						SegmentNode<T> left = node.left;
						node.left = left.right;
						left.right = node;
						node = left;
						node.father = null;
						node.right.father = node;
						if (node.right.left != null)
							node.right.left.father = node.right;
						node.right.depth = Math.max(
								node.right.left == null ? 0 : node.right.left.depth,
								node.right.right == null ? 0 : node.right.right.depth) + 1;
					} else if (heavy < -1) {
						// right heavy
						if ((node.right.left == null ? 0 : node.right.left.depth) > (node.right.right == null ? 0 : node.right.right.depth)) {
							SegmentNode<T> right = node.right.left;
							node.right.left = right.right;
							right.right = node.right;
							node.right = right;
							node.right.father = node;
							node.right.right.father = node.right;
							if (node.right.right.left != null)
								node.right.right.left.father = node.right.right;
							node.right.right.depth = Math.max(
									node.right.right.left == null ? 0 : node.right.right.left.depth,
									node.right.right.right == null ? 0 : node.right.right.right.depth) + 1;
							node.right.right.max = node.right.right.right == null ? node.right.right.end : node.right.right.right.max;
						}
						SegmentNode<T> right = node.right;
						node.right = right.left;
						right.left = node;
						node = right;
						node.father = null;
						node.left.father = node;
						if (node.left.right != null)
							node.left.right.father = node.left;
						node.left.depth = Math.max(
								node.left.left == null ? 0 : node.left.left.depth,
								node.left.right == null ? 0 : node.left.right.depth) + 1;
					}
					node.max = node.right == null ? node.end : node.right.max;
					node.depth = Math.max(node.left == null ? 0 : node.left.depth, node.right == null ? 0 : node.right.depth) + 1;
					break;
				} else {
					// rotations
					final int heavy = (node.left == null ? 0 : node.left.depth) - (node.right == null ? 0 : node.right.depth);
					if (heavy > 1) {
						// left heavy - rotate right
						if ((node.left.right == null ? 0 : node.left.right.depth) > (node.left.left == null ? 0 : node.left.left.depth)) {
							// rotate left first
							SegmentNode<T> left = node.left.right;
							node.left.right = left.left;
							left.left = node.left;
							node.left = left;
							node.left.father = node;
							node.left.left.father = node.left;
							if (node.left.left.right != null)
								node.left.left.right.father = node.left.left;
							node.left.left.depth = Math.max(
									node.left.left.left == null ? 0 : node.left.left.left.depth,
									node.left.left.right == null ? 0 : node.left.left.right.depth) + 1;
							node.left.left.max = node.left.left.right == null ? node.left.left.end : node.left.left.right.max;
						}
						SegmentNode<T> left = node.left, father = node.father;
						node.left = left.right;
						left.right = node;
						if (father.left == node)
							father.left = left;
						else
							father.right = left;
						node = left;
						node.father = father;
						node.right.father = node;
						if (node.right.left != null)
							node.right.left.father = node.right;
						node.right.depth = Math.max(
								node.right.left == null ? 0 : node.right.left.depth,
								node.right.right == null ? 0 : node.right.right.depth) + 1;
					} else if (heavy < -1) {
						// right heavy
						if ((node.right.left == null ? 0 : node.right.left.depth) > (node.right.right == null ? 0 : node.right.right.depth)) {
							SegmentNode<T> right = node.right.left;
							node.right.left = right.right;
							right.right = node.right;
							node.right = right;
							node.right.father = node;
							node.right.right.father = node.right;
							if (node.right.right.left != null)
								node.right.right.left.father = node.right.right;
							node.right.right.depth = Math.max(
									node.right.right.left == null ? 0 : node.right.right.left.depth,
									node.right.right.right == null ? 0 : node.right.right.right.depth) + 1;
							node.right.right.max = node.right.right.right == null ? node.right.right.end : node.right.right.right.max;
						}
						SegmentNode<T> right = node.right, father = node.father;
						node.right = right.left;
						right.left = node;
						if (father.left == node)
							father.left = right;
						else
							father.right = right;
						node = right;
						node.father = father;
						node.left.father = node;
						if (node.left.right != null)
							node.left.right.father = node.left;
						node.left.depth = Math.max(
								node.left.left == null ? 0 : node.left.left.depth,
								node.left.right == null ? 0 : node.left.right.depth) + 1;
					}
					node.max = node.right == null ? node.end : node.right.max;
					node.depth = Math.max(node.left == null ? 0 : node.left.depth, node.right == null ? 0 : node.right.depth) + 1;
					node = node.father;
				}
			}
			return node;	// new root
		}
		private SegmentNode<T> find(T value) {
			SegmentNode<T> result = this;
			while (true)
				if (value.compareTo(result.end) <= 0 && value.compareTo(result.start) >= 0)
					return result;
				else if (result.left != null && value.compareTo(result.left.max) <= 0)
					result = left;
				else if (result.right != null && value.compareTo(result.right.max) <= 0)
					result = right;
				else
					return null;
		}
	}
	private static class Pair <A extends Comparable<A>, B extends Comparable<B>, C> implements Comparable<Pair<A, B, C>> {
		private A first;
		private B second;
		private C data;
		private Pair(A first, B second, C data) {
			this.first = first; this.second = second;
			this.data = data;
		}
		@Override
		public int compareTo(Pair<A, B, C> another) {
			int comp = first.compareTo(another.first);
			if(comp != 0)
				return comp;
			else
				return second.compareTo(another.second);
		}
	}
}
