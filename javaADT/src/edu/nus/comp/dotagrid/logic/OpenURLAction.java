package edu.nus.comp.dotagrid.logic;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenURLAction implements ActionListener {
	private URI uri;
	
	// constructor
	public OpenURLAction(String url) {
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		open(uri);
	}

	private void open(URI uri2) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) { 
				/* TODO: error handling */ 
				System.out.println(e.getMessage());
			}
		} else { 
			/* TODO: error handling */ 
			System.out.println("Desktop not supported!");
		}
	}

}
