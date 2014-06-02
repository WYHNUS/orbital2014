package edu.nus.comp.dotagridandroid.logic;

import java.util.*;
import java.util.concurrent.*;

public class GameLogicManager {
	private Map<String, Object> gameSetting;

	public GameLogicManager() {
		gameSetting = new ConcurrentHashMap<>();
		gameSetting.put("DISPLAY_ANTI_ALIAS_SAMPLINGS", 4);
	}

	public Object getGameSetting(String key) {
		return gameSetting.get(key);
	}

}
