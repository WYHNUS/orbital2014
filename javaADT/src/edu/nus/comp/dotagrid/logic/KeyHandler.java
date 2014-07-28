package edu.nus.comp.dotagrid.logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
	public static boolean isPressed;

	@Override
	public void keyPressed(KeyEvent e) {
		
		isPressed = true;
		
		int keyCode = e.getKeyCode();
		
		System.out.println("You have pressed keyboard with key code = " + keyCode);
		
		switch (keyCode) {
			case 27:
				// key ESC
				MainMenu.confirmExit();
				break;
				
			case 32:
				// key SPACE
				// check if the game has started
				if (Screen.scene != 1) {
					Screen.startGame();
				} else {
					// end round
					GameFrame.allGameButtons.get(50).setNeededToHighlight(true);
					GameButtonActions.endRound();
				}
				break;
				
			case 65:
				// key A
				GameFrame.allGameButtons.get(33).setNeededToHighlight(true);
				GameButtonActions.readyToAttack();
				break;
				
			case 77:
				// key M
				GameFrame.allGameButtons.get(32).setNeededToHighlight(true);
				GameButtonActions.readyToMove();
				break;
				
			case 37:
				// key left
				GameFrame.allGameButtons.get(49).setNeededToHighlight(true);
				GameButtonActions.gameGridMoveLeft();
				break;
				
			case 38:
				// key up
				GameFrame.allGameButtons.get(48).setNeededToHighlight(true);
				GameButtonActions.gameGridMoveUp();
				break;
				
			case 39:
				// key right
				GameFrame.allGameButtons.get(51).setNeededToHighlight(true);
				GameButtonActions.gameGridMoveRight();
				break;
				
			case 40:
				// key down
				GameFrame.allGameButtons.get(52).setNeededToHighlight(true);
				GameButtonActions.gameGridMoveDown();
				break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
