package edu.nus.comp.dotagrid.logic;

import java.util.Random;

public class User {
	public int playerStartingXPos = 7;
	public int playerStartingYPos = 91;
	
	public static final int SENTINEL_PLAYER_STARTING_X_POS = 7;
	public static final int SENTINEL_PLAYER_STARTING_Y_POS = 91;
	
	public static final int SCOURGE_PLAYER_STARTING_X_POS = 91;
	public static final int SCOURGE_PLAYER_STARTING_Y_POS = 7;
	
	public Player player;
	
	public User() {
		System.out.println("initialize user!");
		
		// randomly select a hero from hero database for player to control
		Random random = new Random();
		Hero character = HeroDatabase.heroDatabase[random.nextInt(HeroDatabase.totalHeroNumber)];
		character.setTeamNumber(1);
		
		// set player's hero's starting position
		character.setHeroSpawningXPos(playerStartingXPos);
		character.setHeroSpawningYPos(playerStartingYPos);

		// create player!
		GridFrame.gridButtonMap[playerStartingXPos][playerStartingYPos] = new GridButton(character);
		GridFrame.gridButtonMap[playerStartingXPos][playerStartingYPos].setIsPlayer(true);
		createPlayer();
		Player.setTeamNumber(character.getTeamNumber());
	}
	
	public void createPlayer(){
		System.out.println("Create a new player!");
		this.player = new Player(playerStartingXPos, playerStartingYPos);
	}

}

