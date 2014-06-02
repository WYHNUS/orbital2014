package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class GameStatusRenderer implements Renderer {
	private GameLogicManager manager;
	private float ratio;
	
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
	public void setGraphicsResponder(MainRenderer mainRenderer) {
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	@Override
	public void setRenderReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		return false;
	}
	
	@Override
	public void close() {
	}

}
