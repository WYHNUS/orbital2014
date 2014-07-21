package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import edu.nus.comp.dotagridandroid.appsupport.*;

public class ExtendedGameMaster extends GameMaster {
	@Override
	public void initialise() {
	}
	@Override
	public void applyRule(String character, String actionName, Map<String, Object> options) {
		if ("SetProperty".equals(actionName)) {
			// server special
		} else {
			ExtensionEngine ee = state.getExtensionEngine();
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
	public boolean requestActionPossible(String character, String actionName, Map<String, Object> options) {
		return true;
	}
	@Override
	public void serverNotify() {
		// TODO Auto-generated method stub
		
	}
}
