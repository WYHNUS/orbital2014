package edu.nus.comp.dotagrid.logic;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SellItem{

	JPanel panel = new JPanel();
	
	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JButton button3 = new JButton();
	JButton button4 = new JButton();
	JButton button5 = new JButton();
	JButton button6 = new JButton();
	
	JButton[] buttons = {button1, button2, button3, button4, button5, button6};
	
	public SellItem(){
		JFrame frame = new JFrame("SELL");
		
		GridFrame.popupJFrame = true;
		
		panel.setLayout(new GridLayout(3, 2));

		for(int i=0; i<GameFrame.MAX_ITEM_NUMBER; i++){
			if (((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[i] != null) {
				buttons[i] = new JButton(((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[i].getItemName() 
						+ ", sell for : " + 
						((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[i].getSellPrice(), 
						((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[i].getItemImage());
				
				buttons[i].addActionListener(new ClickItemListener(i));
				buttons[i].setActionCommand("" + i);
			}
		}
		
		// add all items onto the JPanel
		for (int i=0; i<6; i++) {
			panel.add(buttons[i]);
		}
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				GridFrame.popupJFrame = false;
			}
		});
		
		frame.add(panel);
		frame.pack();
		frame.setSize(600, 450);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}

	
	class ClickItemListener implements ActionListener {
		private int buttonIdex;

		public ClickItemListener(int index) {
			buttonIdex = index;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// click an item to sell
			int itemNumber = Integer.parseInt(e.getActionCommand());
			
			System.out.println("you have selled " 
					+ ((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).items[itemNumber].getItemName());

			// delete the selected item from player
			((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).removeItem(itemNumber);

			// reset money display for player
			GameFrame.allCharacterInfoGameButtons.get(29).setString("Money : " + 
					((Hero)GridFrame.gridButtonMap[Screen.user.player.getXPos()][Screen.user.player.getYPos()].getCharacter()).getMoney());
			
			// reselect the grid
			ItemShop.shouldUpdateItemInFo = true;
			GridFrame.invokeLeftClickEvent(GridFrame.getSelectedXCoordinatePos(), GridFrame.getSelectedYCoordinatePos());
			
			buttons[buttonIdex].setIcon(null);
			buttons[buttonIdex].setText(null);
			panel.revalidate();
			panel.repaint();
			
			// reset updateInfo boolean
			ItemShop.shouldUpdateItemInFo = false;
		}
		
	}
	
}
