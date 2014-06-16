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
			// TODO store and action verb
			// assume "Move"
//			final String playerCharName = stateMachine.getPlayerCharacter(); 
//			final Character playerChar = stateMachine.getCharacters().get(playerCharName);
//			final int[] reqPos = (int[]) options.get("Coordinates");
//			final int[] prevPos = stateMachine.getCharacterPositions().get(stateMachine.getPlayerCharacter());
//			// calculate AP
//			final double actionPointUsed = playerChar.APUsedInMovingOneGrid() * (Math.abs(reqPos[0] - prevPos[0]) + Math.abs(reqPos[1] - prevPos[1]));
//			final double currentActionPoint = playerChar.getCurrentActionPoint() - actionPointUsed;
//			if (currentActionPoint < 0)
//				return;
//			playerChar.setCurrentActionPoint((int) currentActionPoint);
			// move
//			stateMachine.setCharacterPositions(playerCharName, (int[]) options.get("Coordinate"));
//			Set<String> characters = Collections.singleton(playerCharName);
//			Map<String, Object> updates = Collections.singletonMap("Characters", (Object) characters);
//			stateMachine.notifyUpdate(updates);
//			Map<String, Object> updates = Collections.singletonMap("ChosenGrid", options.get("Coordinates"));
//			stateMachine.notifyUpdate(updates);
			break;
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
				case "RequestAttackArea": {
					System.out.println("Requesting Attack Area");
					// TODO: calculate attack area
					List<int[]> allowed = new ArrayList<>();
					final int gridWidth = stateMachine.getGridWidth(), gridHeight = stateMachine.getGridHeight();
					final int totalAttackArea = playerChar.getTotalPhysicalAttackArea() + ((Hero) playerChar).getTotalItemAddPhysicalAttackArea();
					for (int i = -totalAttackArea; i <= totalAttackArea; i++)
						for (int j = -totalAttackArea + Math.abs(i); j <= totalAttackArea - Math.abs(i); j++)
							if ((i != 0 || j != 0) && prevPos[0] + i < gridWidth && prevPos[0] + i >= 0 && prevPos[1] + j < gridHeight && prevPos[1] + j >= 0)
								allowed.add(new int[]{prevPos[0] + i, prevPos[1] + j});
					updates.put("HighlightGrid", allowed.toArray(new int[2][allowed.size()]));
					stateMachine.notifyUpdate(Collections.unmodifiableMap(updates));
					return;
				}
				case "Move": {
					System.out.println("Game action is move");
					final int[] reqPos = stateMachine.getChosenGrid();
//					// calculate AP
					final double actionPointUsed = playerChar.APUsedInMovingOneGrid() * (Math.abs(reqPos[0] - prevPos[0]) + Math.abs(reqPos[1] - prevPos[1]));
					final double currentActionPoint = playerChar.getCurrentActionPoint() - actionPointUsed;
//					if (currentActionPoint < 0)
//						return;
					playerChar.setCurrentActionPoint((int) currentActionPoint);
					// move
					stateMachine.setCharacterPositions(playerCharName, stateMachine.getChosenGrid());
					Set<String> characters = Collections.singleton(playerCharName);
					updates = Collections.singletonMap("Characters", (Object) characters);
					stateMachine.notifyUpdate(updates);
					return;
				}
				case "RequestMoveArea": {
					System.out.println("Requesting move area");
				}
				}
				
			}
			break;
		}
		}
	}
	// Hard coded rules go below

	public boolean requestActionPossible(GameState stateMachine, String actionName, Map<String, Object> options) {
		final int[] targetGrid = stateMachine.getChosenGrid();
		switch (actionName) {
		case "GameAction":
			switch ((String) options.get("Action")) {
			case "Attack":
			}
			break;
		}
		// TODO TEST
		return true;
	}
}
