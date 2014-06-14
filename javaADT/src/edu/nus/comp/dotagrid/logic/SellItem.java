package edu.nus.comp.dotagrid.logic;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SellItem implements ActionListener {
	
	public SellItem(){
		JFrame frame = new JFrame("SELL");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 4));

		for(int i=0; i<GameFrame.MAX_ITEM_NUMBER; i++){
			if (Screen.user.player.getHero().items[i] != null) {
				JButton button = new JButton(Screen.user.player.getHero().items[i].getItemName() + ", sell for : " + 
						Screen.user.player.getHero().items[i].getSellPrice(), Screen.user.player.getHero().items[i].getItemImage());
				
				button.addActionListener(this);
				button.setActionCommand("" + i);
				
				panel.add(button);
			}
		}
		
		
		
		frame.add(panel);
		frame.pack();
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// click an item to sell
		int itemNumber = Integer.parseInt(e.getActionCommand());
		
		System.out.println("you have selled " + Screen.user.player.getHero().items[itemNumber].getItemName());
		
		// delete the selected item from player
		Screen.user.player.getHero().removeItem(itemNumber);
		
		// reset money display for player
		GameFrame.allCharacterInfoGameButtons.get(29).setString("Money : " + 
				((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getMoney());
		
		// reselect the grid
		ItemShop.shouldUpdateItemInFo = true;
		GridFrame.invokeEvent(GridFrame.getSelectedXCoodinatePos(), GridFrame.getSelectedYCoodinatePos());
	}
	
}
