package edu.nus.comp.dotagridandroid.logic;
import java.util.*;

import edu.nus.comp.dotagridandroid.appsupport.ResourceManager;
import edu.nus.comp.dotagridandroid.ui.event.*;

@SuppressWarnings("unchecked")
public abstract class GameMaster {
	GameState state;
	ResourceManager resourceManager;
	
	public abstract void serverNotify();
	
	public abstract void initialise();
	
	public abstract void applyRule(String character, String actionName, Map<String, Object> options);

	public abstract boolean requestActionPossible(final GameState stateMachine, final String character, final String actionName, final Map<String, Object> options);

	public void setResource(ResourceManager resMan) {
		this.resourceManager = resMan;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
}
