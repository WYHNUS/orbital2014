package edu.nus.comp.dotagridandroid.logic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CharacterRulebook {
	public static void applyRule(GameState stateMachine, String character, String actionName, Map<String, Object> options) {
		switch (actionName) {
		case "GameAction": {
			switch ((String) options.get("Action")) {
			case "Move": {
			}
			case "BuyItem":
			case "SellItem":
			case "Skill":
				break;
			case "Attack": {
				String targetCharacter = stateMachine.getCharacterAtPosition(stateMachine.getChosenGrid());
				if (targetCharacter == null)
					return;
				
				break;
			}
			}
			break;
		}
		}
	}
	public static double[][] getLowestMoveAPConsumptionMap (GameState stateMachine, int[] start, String character) {
		final float[] terrain = stateMachine.getTerrain();
		final int width = stateMachine.getGridWidth(), height = stateMachine.getGridHeight();
		final int[][] dirs = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		final double TERRAIN_CONST = 1;	// TODO terrain factor
		Queue<int[]> q = new LinkedList<>();
		double[][] map = new double[width][height];
		for (int i = 0; i < height; i++)
			Arrays.fill(map[i], (Integer) stateMachine.getCharacterProperty(character, "maxActionPoint") + 1);
		final int APConsumptionPerGrid = (Integer) stateMachine.getCharacterProperty(character, "APUsedInMovingOneGrid");
		map[start[0]][start[1]] = 0;
		q.add(start);
		while (!q.isEmpty()) {
			final int[] prevPos = q.remove();
			for (byte i = 0; i < 4; i++) 
				if (prevPos[0] + dirs[i][0] < width && prevPos[0] + dirs[i][0] >= 0 &&
						prevPos[1] + dirs[i][1] < height && prevPos[1] + dirs[i][1] >= 0) {
					final double APconsumed = APConsumptionPerGrid/*character.getAPUsedInMovingOneGrid()*/ +
							Math.max(0, terrain[prevPos[0] + dirs[i][0] + (prevPos[1] + dirs[i][1]) * width] - terrain[prevPos[0] + prevPos[1] * width]) * TERRAIN_CONST;
					if (APconsumed < map[prevPos[0] + dirs[i][0]][prevPos[1] + dirs[i][1]]) {
						map[prevPos[0] + dirs[i][0]][prevPos[1] + dirs[i][1]] = APconsumed;
						q.add(new int[]{prevPos[0] + dirs[i][0], prevPos[1] + dirs[i][1]});
					}
				}
		}
		return map;
	}
}
