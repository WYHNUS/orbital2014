package edu.nus.comp.dotagridandroid.logic;

import java.util.Map;

public class LinecreepRulebook {
	public static void applyRule(GameState stateMachine, String character, String actionName, Map<String, Object> options) {
		switch (actionName) {
		case "GameAction": {
			switch ((String) options.get("Action")) {
			case "Move":
			case "BuyItem":
			case "SellItem":
			case "Skill":
			case "Attack":
				break;
			}
			break;
		}
		}
	}
}
