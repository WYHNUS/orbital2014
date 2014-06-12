package edu.nus.comp.dotagridandroid.logic;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.Closeable;
import edu.nus.comp.dotagridandroid.ui.event.*;

public class GameState implements Closeable {
	private int gridWidth, gridHeight;
	private float[] terrain;
	private boolean initialised;
	private Thread initialisationProcess;
	private SceneRenderer currentSceneRenderer;
	// game rule object
	private GameMaster gameMaster;
	public GameState() {
	}
	
	public void initialise() {
		if (initialised)
			return;
		initialisationProcess = new Thread() {
			@Override
			public void run() {
				gameMaster = new GameMaster();
				initialised = true;
			}
		};
		initialisationProcess.start();
	}
	
	public boolean isInitialised() {
		try {initialisationProcess.join();} catch (Exception e) {e.printStackTrace();}
		return initialised;
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
		case "TestButton":
			System.out.println("Test Button Pressed");
		case "":
		}
		/// Use Hard code game rules
	}
}
