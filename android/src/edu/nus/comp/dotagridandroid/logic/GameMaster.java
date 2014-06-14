package edu.nus.comp.dotagridandroid.logic;
import java.util.*;
import edu.nus.comp.dotagridandroid.ui.event.*;
public class GameMaster {
	public void applyRule(GameState stateMachine, String actionName, Map<String, Object> options) {
		switch (actionName) {
		case "ChooseGrid": {
			// TODO store action verb
			// assume "Move"
			final String playerCharName = stateMachine.getPlayerCharacter(); 
			final Character playerChar = stateMachine.getCharacters().get(playerCharName);
			final int[] reqPos = (int[]) options.get("Coordinate");
			final int[] prevPos = stateMachine.getCharacterPositions().get(stateMachine.getPlayerCharacter());
			// calculate AP
			final double actionPointUsed = playerChar.APUsedInMovingOneGrid() * (Math.abs(reqPos[0] - prevPos[0]) + Math.abs(reqPos[1] - prevPos[1]));
			//final double currentActionPoint = 
			// move
			stateMachine.setCharacterPositions(playerCharName, (int[]) options.get("Coordinate"));
			ControlEvent e = new ControlEvent(ControlEvent.TYPE_INTERPRETED, null);
			stateMachine.notifyAction(null);
		}
		}
	}
	// Hard coded rules go below
}
