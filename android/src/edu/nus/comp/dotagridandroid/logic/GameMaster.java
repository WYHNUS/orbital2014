package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import edu.nus.comp.dotagridandroid.ui.event.*;
public class GameMaster {
	public void serverNotify() {
		//
	}
	
	public void applyRule(final GameState stateMachine, final String character, final String actionName, final Map<String, Object> options) {
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
				final double[] APmap = getLowestMoveAPConsumptionMap(stateMachine, prevPos, reqPos, character);
				final int currentActionPoint = (Integer) stateMachine.getCharacterProperty(character, "currentActionPoint");
				int column = 0, row = 0;
				for (int i = 0; i < stateMachine.getGridWidth() * stateMachine.getGridHeight(); i++) {
					column++;
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

	public boolean requestActionPossible(final GameState stateMachine, final String character, final String actionName, final Map<String, Object> options) {
		if (!character.equals(stateMachine.getCurrentCharacterName()))
			return false;
		final int[] targetGrid = stateMachine.getChosenGrid(), heroGrid = stateMachine.getCharacterPositions().get(character);
		final Map<String, GameCharacter> chars = stateMachine.getCharacters();
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
	
	private double[] getLowestMoveAPConsumptionMap (GameState stateMachine, int[] start, int[] end, String character) {
		final float[] terrain = stateMachine.getTerrain();
		final int width = stateMachine.getGridWidth(), height = stateMachine.getGridHeight();
		final int[][] dirs = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		final double TERRAIN_CONST = 1;	// TODO terrain factor
		Queue<int[]> q = new LinkedList<>();
		double[] map = new double[width * height];
//		Arrays.fill(map, character.getMaxActionPoint() + 1);
		Arrays.fill(map, (Integer) stateMachine.getCharacterProperty(character, "maxActionPoint") + 1);
		final int APConsumptionPerGrid = (Integer) stateMachine.getCharacterProperty(character, "APUsedInMovingOneGrid");
		map[start[0] + start[1] * width] = 0;
		q.add(start);
		while (!q.isEmpty()) {
			final int[] prevPos = q.remove();
			for (byte i = 0; i < 4; i++) 
				if (prevPos[0] + dirs[i][0] < width && prevPos[0] + dirs[i][0] >= 0 &&
						prevPos[1] + dirs[i][1] < height && prevPos[1] + dirs[i][1] >= 0) {
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
}
