package edu.nus.comp.dotagridandroid.ui.renderers;
import java.util.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
public interface Renderer extends Closeable {
	void draw();
	void setFrameBufferHandler (int framebuffer);
	void setTexture2D(Map<String, Texture2D> textures);
	void setAspectRatio(float ratio);
	void setGameLogicManager(GameLogicManager manager);
	void passEvent(ControlEvent e);
	void setGraphicsResponder(MainRenderer mainRenderer);
}
