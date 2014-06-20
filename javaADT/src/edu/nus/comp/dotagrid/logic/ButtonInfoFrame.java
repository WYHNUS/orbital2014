package edu.nus.comp.dotagrid.logic;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonInfoFrame {

	public ButtonInfoFrame(int gameButtonIndex) {
		// pop up a new JFrame to show the button's information
		JFrame frame = new JFrame("information");
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		
		GridFrame.popupJFrame = true;
		
		switch (gameButtonIndex) {
		
		/*
		 * 0 : characterIcon
		 * 1 : characterName
		 * 2 : characterHP
		 * 3 : characterMP
		 * 4 : characterStrength
		 * 5 : characterAgility
		 * 6 : characterIntelligence
		 * 7 : characterAttack
		 * 8 : characterDefense
		 * 9 : characterLevel
		 * 10 : characterExperience
		 * 
		 * 11 - 16 : item list 
		 * 17 - 24 : skill list
		 * 
		 * 25 : versionID
		 * 26 : turnCounter
		 * 27 : Kill
		 * 28 : Death
		 * 29 : Assist
		 * 
		 * 30 : money
		 * 31 : actionPoints
		 * 
		 * 32 : action move
		 * 33 : action attack
		 * 34 : 
		 * 35 : 
		 * 36 : action item shop
		 * 37 : action sell item
		 * 38 - 47 : 
		 * 
		 * 48 : move up
		 * 49 : move left
		 * 50 : end round
		 * 51 : move right
		 * 52 : move down
		 * 
		 * 53 : zoom in
		 * 54 : zoom out
		 */
		
		case 17: case 18: case 19: case 20: case 21: case 22: case 23: case 24 :
			// show selected character's skill's information if there are any
			// only hero can have skill!
			if (GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getIsHero()) {
				// check if a skill actually exist
				if (((Hero)GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getCharacter()).
						skills[gameButtonIndex-17] != null) {
					String skillName = ((Hero)GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getCharacter()).skills[gameButtonIndex-17].getSkillName();
					int skillLevel = ((Hero)GridFrame.gridButtonMap[GridFrame.getSelectedXPos()][GridFrame.getSelectedYPos()].getCharacter()).skills[gameButtonIndex-17].getSkillLevel();
					label.setText("<html>" + skillName + "<br> Level : " + skillLevel + "</html>");
				}
			}
		
		} // end switch
		
		
		panel.add(label);
		frame.add(panel);
		
		frame.pack();
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
