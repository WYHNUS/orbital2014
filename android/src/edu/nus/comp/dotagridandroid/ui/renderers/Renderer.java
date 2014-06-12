package edu.nus.comp.dotagridandroid.ui.renderers;
import java.util.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
public interface Renderer extends Closeable {
	void setVertexBufferManager (VertexBufferManager manager);
	void setFrameBufferHandler (int framebuffer);
	void setTexture2D(Map<String, Texture2D> textures);
	void setAspectRatio(float ratio);
	void setGameLogicManager(GameLogicManager manager);
	void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer);
	void setMVP(float[] model, float[] view, float[] projection);
	void setRenderReady();
	void notifyUpdate(Map<String, Object> updates);
	boolean getReadyState();
	void draw();
	boolean passEvent(ControlEvent e);
	void close();
}
