package edu.nus.comp.dotagridandroid.logic;

public class GameServer {
	public static final int MODE_SINGLEPLAYER = 0;
	public static final int MODE_MULTIPLAYER = 0x10;
	private String resourceName;
	private int mode;
	public GameServer(int mode) {
		// TODO set up
		resourceName = null;
		this.mode = mode;
	}
	public String getGameResourceName () {
		return resourceName;
	}
}
