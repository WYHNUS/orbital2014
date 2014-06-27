package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import edu.nus.comp.dotagridandroid.appsupport.ExtensionEngine;
public class ExtendedGameMaster extends GameMaster {
	@Override
	public void applyRule(GameState stateMachine, String character, String actionName, Map<String, Object> options) {
		if (actionName.equals("SetProperty")) {
			//
		} else {
			ExtensionEngine ee = stateMachine.getExtensionEngine();
		}
	}
}
