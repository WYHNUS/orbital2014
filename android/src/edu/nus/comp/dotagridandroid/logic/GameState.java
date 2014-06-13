package edu.nus.comp.dotagridandroid.logic;
import java.util.*;
import java.util.concurrent.*;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.Closeable;
import edu.nus.comp.dotagridandroid.ui.event.*;

public class GameState implements Closeable {
	private int gridWidth, gridHeight;
	private float[] terrain;
	private boolean initialised = false, initialising = false;
	private Thread initialisationProcess;
	private SceneRenderer currentSceneRenderer;
	private Map<String, Character> chars;
	private Map<String, Object> objs;
	// game rule object
	private GameMaster gameMaster;
	public GameState() {
	}
	
	public void initialise() {
		if (initialised || initialising)
			return;
		initialisationProcess = new Thread() {
			@Override
			public void run() {
				initialising = true;
				gameMaster = new GameMaster();
				initialised = true;
				initialising = false;
			}
		};
		initialisationProcess.start();
	}
	
	public boolean isInitialised() {
		try {initialisationProcess.join();} catch (Exception e) {e.printStackTrace();}
		return initialised;
	}
	
	public void startTimer() {
		if (!initialised)
			return;
	}
	
	public void stopTimer() {
		if (!initialised)
			return;
	}
	
	@Override
	public void close() {
		// release resources
		gameMaster = null;
		initialised = false;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public float[] getTerrain() {
		return terrain;
	}

	public void setTerrain(float[] terrain) {
		this.terrain = terrain;
	}
	
	// characters
	public void getMainCharacter() {
	}
	
	public void getCharacters() {
	}
	
	public void getCharacterPosition() {
	}
	
	// interface interactions
	public void setCurrentSceneRenderer (SceneRenderer renderer) {
		currentSceneRenderer = renderer;
	}
	public void notifyAction (ControlEvent e) {
		if (currentSceneRenderer != null)
			;
	}

	public void processEvent(ControlEvent e) {
		// TODO apply rules
		switch (e.extendedType) {
		// interface
		case "ChooseGrid":
			break;
		case "RequestItemList":
			break;
		case "RequestActionList":
			break;
		case "InterfaceCancel":
			notifyAction(e);	// bounce back
			break;
		// game action
		case "GameAction":
			// send to game master
			break;
		case "GamePause":
			// pause game
			break;
		case "GameResume":
			break;
		case "GameSave":
			break;
		case "GameExit":
			break;
		}
		/// Use Hard code game rules
	}
}
