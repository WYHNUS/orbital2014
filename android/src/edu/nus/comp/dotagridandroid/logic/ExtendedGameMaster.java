package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import org.json.JSONException;

import edu.nus.comp.dotagridandroid.appsupport.ExtensionEngine;
import edu.nus.comp.dotagridandroid.appsupport.JsonConverter;
public class ExtendedGameMaster extends GameMaster {
	@Override
	public void applyRule(GameState stateMachine, String character, String actionName, Map<String, Object> options) {
		if ("SetProperty".equals(actionName)) {
			// server special
		} else {
			ExtensionEngine ee = stateMachine.getExtensionEngine();
			String optionParam = null;
			try {
				optionParam = JsonConverter.MapToJson(options).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (optionParam == null)
					ee.applyRule(character, actionName, "{}");
				else
					ee.applyRule(character, actionName, optionParam);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public boolean requestActionPossible(GameState stateMachine, String character, String actionName, Map<String, Object> options) {
		return true;
	}
}
