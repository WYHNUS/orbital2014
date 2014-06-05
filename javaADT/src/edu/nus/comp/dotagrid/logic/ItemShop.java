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
		
		for (int i=0; i<ItemDatabase.totalItemNumber; i++) {
			
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
		
		System.out.println("ItemName : " + itemDtabase.itemDatabase[itemNumber].getItemName());
	}
	
}
