package edu.nus.comp.dotagrid.logic;

import javax.swing.JOptionPane;

public class GameEnded {
	public static void isGameEnded() {
		// check if sentinel base has been destroyed
		 if (GridFrame.gridButtonMap[BuildingDatabase.sentinelBasePosition[0]][BuildingDatabase.sentinelBasePosition[1]]
				.getCharacter().isAlive() == false) {
			 // end game and announce sentinel win
			 announceWinner(1);
		 }

			// check if sentinel base has been destroyed
		if (GridFrame.gridButtonMap[BuildingDatabase.scourgeBasePosition[0]][BuildingDatabase.scourgeBasePosition[1]]
				.getCharacter().isAlive() == false) {
			 // end game and announce scourge win
			 announceWinner(2);
		}
	}
	
	public static void announceWinner(int winningTeamNumber){
		// check if player is in winner's team
		if (Player.getTeamNumber() != winningTeamNumber) {
			 System.out.println("Game ended! Player has won!");
			// announce player has win the game
			JOptionPane.showMessageDialog(null, "You have WON the GAME!!!!!");
			System.exit(1);
		} else {
			System.out.println("Game ended! Player has lost!");
			// announce player has lost the game
			JOptionPane.showMessageDialog(null, "You have LOST the GAME...");
			System.exit(1);
		}
	}
}
