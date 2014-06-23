package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import edu.nus.comp.dotagridandroid.ui.event.*;
public class GameMaster {
	public void applyRule(GameState stateMachine, String actionName, Map<String, Object> options) {
		final String playerCharName = stateMachine.getPlayerCharacterName(); 
		final Character playerChar = stateMachine.getCharacters().get(playerCharName);
		final int[] prevPos = stateMachine.getCharacterPositions().get(stateMachine.getPlayerCharacterName());
		switch (actionName) {
		case "ChooseGrid": {
			stateMachine.notifyUpdate(Collections.singletonMap("ChosenGrid", options.get("Coordinate")));
			break;
		}
		case "RequestAttackArea": {
			Map<String, Object> updates = new HashMap<>();
			// TODO: calculate attack area
			List<List<Integer>> allowed = new ArrayList<>();
			final int gridWidth = stateMachine.getGridWidth(), gridHeight = stateMachine.getGridHeight();
			final int totalAttackArea = playerChar.getTotalPhysicalAttackArea() + ((Hero) playerChar).getTotalItemAddPhysicalAttackArea();
			for (int i = -totalAttackArea; i <= totalAttackArea; i++)
				for (int j = -totalAttackArea + Math.abs(i); j <= totalAttackArea - Math.abs(i); j++)
					if ((i != 0 || j != 0) && prevPos[0] + i < gridWidth && prevPos[0] + i >= 0 && prevPos[1] + j < gridHeight && prevPos[1] + j >= 0)
						allowed.add(Arrays.asList(prevPos[0] + i, prevPos[1] + j));
			updates.put("HighlightGrid", Collections.unmodifiableList(allowed));
			stateMachine.notifyUpdate(Collections.unmodifiableMap(updates));
			return;
		}
		case "RequestMoveArea": {
			System.out.println("Requesting move area");
			return;
		}
		case "RequestItemShop": {
			return;
		}
		case "GameAction": {
			// check if action is possible
			if (requestActionPossible(stateMachine, actionName, options)) {
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
					final int[] reqPos = stateMachine.getChosenGrid();
//					// calculate AP
					final double actionPointUsed = playerChar.APUsedInMovingOneGrid() * (Math.abs(reqPos[0] - prevPos[0]) + Math.abs(reqPos[1] - prevPos[1]));
					final double currentActionPoint = playerChar.getCurrentActionPoint() - actionPointUsed;
					playerChar.setCurrentActionPoint((int) currentActionPoint);
					// move
					stateMachine.setCharacterPositions(playerCharName, stateMachine.getChosenGrid());
					List<String> characters = Collections.singletonList(playerCharName);
					updates = Collections.singletonMap("Characters", (Object) characters);
					stateMachine.notifyUpdate(updates);
					return;
				}
				case "BuyItem": {
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

	public boolean requestActionPossible(GameState stateMachine, String actionName, Map<String, Object> options) {
		final int[] targetGrid = stateMachine.getChosenGrid(), heroGrid = stateMachine.getCharacterPositions().get(stateMachine.getPlayerCharacterName());
		final Map<String, Character> chars = stateMachine.getCharacters();
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
				Hero hero = (Hero) stateMachine.getCharacters().get(stateMachine.getPlayerCharacterName());
				return stateMachine.getCharacterAtPosition(targetGrid) != null &&	// has hero, linecreep or tower
						hero.getTotalPhysicalAttackArea() + hero.getTotalItemAddPhysicalAttackArea()
						>= Math.abs(targetGrid[0] - heroGrid[0])
						+ Math.abs(targetGrid[1] - heroGrid[1]) &&	// within attack area
						hero.getCurrentActionPoint() > Hero.MIN_PHYSICAL_ATTACK_CONSUME_AP + (1 - hero.getTotalPhysicalAttackSpeed() / Character.MAX_PHYSICAL_ATTACK_SPEED)
							* Hero.PHYSICAL_ATTACK_CONSUME_AP;	// has enough action points
			}
			}
			break;
		}
		// TODO TEST
		return true;
	}
	
	private double findLowestMoveAPConsumption (int[] start, int[] end, float[] terrain, int width, int height, Character character) {
		final int[][] dirs = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		final double TERRAIN_CONST = 1;	// TODO terrain factor
		Queue<int[]> q = new LinkedList<>();
		double[] map = new double[width * height];
		Arrays.fill(map, character.getMaxActionPoint() + 1);
		map[start[0] + start[1] * width] = 0;
		q.add(start);
		while (!q.isEmpty()) {
			final int[] prevPos = q.remove();
			for (byte i = 0; i < 4; i++) 
				if (prevPos[0] + dirs[i][0] < width && prevPos[0] + dirs[i][0] >= 0 &&
						prevPos[1] + dirs[i][1] < height && prevPos[1] + dirs[i][1] >= 0) {
					final double APconsumed = character.APUsedInMovingOneGrid() +
							Math.max(0, terrain[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width] - terrain[prevPos[0] + prevPos[1] * width]) * TERRAIN_CONST;
					if (APconsumed < map[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width]) {
						map[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width] = APconsumed;
						q.add(new int[]{prevPos[0] + dirs[i][0], prevPos[1] + dirs[i][1]});
					}
				}
		}
		return map[end[0] + end[1] * width];
	}
}
