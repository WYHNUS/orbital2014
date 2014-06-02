package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class GameStatusRenderer implements Renderer {
	private GameLogicManager manager;
	private float ratio;

	@Override
	public void close() {
	}

	@Override
	public void draw() {
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {this.manager = manager;}

	@Override
	public boolean passEvent(ControlEvent e) {
		return false;
	}

	@Override
	public void setGraphicsResponder(MainRenderer mainRenderer) {
	}

}
