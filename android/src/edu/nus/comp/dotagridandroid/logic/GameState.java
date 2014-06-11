package edu.nus.comp.dotagridandroid.logic;
import edu.nus.comp.dotagridandroid.ui.renderers.scenes.SceneRenderer;
import edu.nus.comp.dotagridandroid.ui.renderers.Closeable;

public class GameState implements Closeable {
	private int gridWidth, gridHeight;
	private float[] terrain;
	private boolean initialised;
	private final Thread initialisationProcess;
	private SceneRenderer currentSceneRenderer;
	public GameState() {
		initialisationProcess = new Thread() {
			@Override
			public void run() {
				initialised = true;
			}
		};
	}
	
	public void initialise() {
		if (initialised)
			return;
		initialisationProcess.start();
	}
	
	public boolean isInitialised() {
		try {initialisationProcess.join();} catch (Exception e) {e.printStackTrace();}
		return initialised;
	}
	
	@Override
	public void close() {
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
	
	// interface interactions
	public void setCurrentSceneRenderer (SceneRenderer renderer) {
		currentSceneRenderer = renderer;
	}
	public void attachUpdateDelegate (String notifyEvent) {
		if (currentSceneRenderer != null)
			;
	}
}
