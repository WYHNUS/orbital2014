package edu.nus.comp.dotagrid.logic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{
	
	private int handXPos = 0;
	private int handYPos = 0;

	@Override
	public void mouseClicked(MouseEvent e) {
		handXPos = e.getXOnScreen();
		handYPos = e.getYOnScreen();
		
		if(e.getButton() == MouseEvent.BUTTON1) {
            // left click
			GameFrame.invokeLeftClickEvent(handXPos, handYPos);
			GridFrame.invokeLeftClickEvent(handXPos, handYPos);
			
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            // right click
        	GameFrame.invokeRightClickEvent(handXPos, handYPos);
        }
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
