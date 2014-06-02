package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class TextRenderer implements Renderer {

	private MainRenderer responder;
	private float ratio;

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		return false;
	}

	@Override
	public void setGraphicsResponder(MainRenderer mainRenderer) {
		this.responder = mainRenderer;
	}
}
