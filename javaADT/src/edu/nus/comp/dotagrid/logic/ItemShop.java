package edu.nus.comp.dotagrid.logic;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ItemShop implements ActionListener{
	
	ItemDatabase itemDtabase; 
	
	public ItemShop(){
		JFrame frame = new JFrame("SHOP");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 4));
		
		itemDtabase = new ItemDatabase();
		
		for (int i=0; i<ItemDatabase.TOTAL_ITEM_NUMBER; i++) {
			
			JButton button = new JButton(itemDtabase.itemDatabase[i].getItemName(), itemDtabase.itemDatabase[i].getItemImage());

			button.addActionListener(this);
			button.setActionCommand("" + i);

			panel.add(button);
		}
		
		
		
		frame.add(panel);
		frame.pack();
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int itemNumber = Integer.parseInt(e.getActionCommand());
		
		// condition for buying a item : player's item list still has vacancy
		if (Screen.user.player.getHero().items.length <= ItemDatabase.TOTAL_ITEM_NUMBER){
			
			// condition for buying a item : player's hero has enough money
			if (Screen.user.player.getMoney() - itemDtabase.itemDatabase[itemNumber].getCost() >= 0){
				
				// add an item to player and deduce the required amount of money
				Screen.user.player.getHero().addItem(itemDtabase.itemDatabase[itemNumber]);
				Screen.user.player.setMoney(Screen.user.player.getMoney() - itemDtabase.itemDatabase[itemNumber].getCost()); 
				
				// reset money display for player
				GameFrame.allCharacterInfoGameButtons.get(29).setString("Money : " + Screen.user.player.getMoney());
				
				System.out.println("Player has bought an item : " + itemDtabase.itemDatabase[itemNumber].getItemName());
				
			} else {
				System.out.println("not enough money!");
			}
		} else {
			System.out.println("item list is full!");
		}
	}
	
}
