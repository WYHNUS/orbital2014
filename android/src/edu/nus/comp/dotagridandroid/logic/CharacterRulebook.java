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
				int[] target = stateMachine.getChosenGrid(), source = stateMachine.getCharacterPosition(character);
				if (stateMachine.getCharacterAtPosition(target) == null) {
					// move
					double[][] map = CharacterRulebook.getLowestMoveAPConsumptionMap(stateMachine, source, target, character);
					if (map[target[0]][target[1]] < (Integer) stateMachine.getCharacterProperty(character, "currentActionPoint")) {
						stateMachine.setCharacterPosition(character, target);
						stateMachine.setCharacterProperty(character, "currentActionPoint",
								(Integer) stateMachine.getCharacterProperty(character, "currentActionPoint") - map[target[0]][target[1]]
								);
					}
				}
			}
			case "BuyItem":
			case "SellItem":
			case "Skill":
				break;
			case "Attack": {
				String targetCharacter = stateMachine.getCharacterAtPosition(stateMachine.getChosenGrid());
				if (targetCharacter == null)
					return;
				int APused = (Integer) stateMachine.getCharacterProperty(character, "APUsedWhenAttack");
				int damage = GameCharacter.getActualDamage(
						(Integer) stateMachine.getCharacterProperty(targetCharacter, "totalPhysicalAttack"),
						(Integer) stateMachine.getCharacterProperty(targetCharacter, "totalPhysicalDefence"));
				stateMachine.setCharacterProperty(character, "currentActionPoint",
						(Integer) stateMachine.getCharacterProperty(character, "currentActionPoint") - APused);
				stateMachine.setCharacterProperty(targetCharacter, "currentHP",
						(Integer) stateMachine.getCharacterProperty(targetCharacter, "currentHP") - damage);
				if (!(Boolean) stateMachine.getCharacterProperty(targetCharacter, "alive")) {
					// TODO check game ended
					
					if (stateMachine.getCharacterType(character) == GameObject.GAMEOBJECT_TYPE_HERO) {
						stateMachine.setCharacterProperty(character, "money",
								(Integer) stateMachine.getCharacterProperty(character, "money") +
								(Integer) stateMachine.getCharacterProperty(targetCharacter, "bountyMoney")
								);
						stateMachine.setCharacterProperty(character, "experience",
								(Integer) stateMachine.getCharacterProperty(character, "experience") +
								(Integer) stateMachine.getCharacterProperty(targetCharacter, "bountyExp")
								);
					}
					// TODO reset character position
					stateMachine.setCharacterPosition(targetCharacter, null);
				}
				break;
			}
			}
			break;
		}
		}
	}
	public static double[][] getLowestMoveAPConsumptionMap (GameState stateMachine, int[] start, int[] end, String character) {
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
