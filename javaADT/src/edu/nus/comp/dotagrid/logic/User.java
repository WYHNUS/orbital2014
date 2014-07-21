package edu.nus.comp.dotagrid.logic;

import java.util.Random;

public class User {
	public int playerStartingXPos = 7;
	public int playerStartingYPos = 91;
	
	public Player player;
	
	public User() {
		// randomly select a hero from hero database for player to control
		Random random = new Random();
		Hero character = new HeroDatabase().heroDatabase[random.nextInt(HeroDatabase.totalHeroNumber)];
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
		this.player = new Player(playerStartingXPos, playerStartingYPos);
	}

}

