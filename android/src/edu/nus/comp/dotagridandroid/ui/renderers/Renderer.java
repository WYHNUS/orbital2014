package edu.nus.comp.dotagridandroid.ui.renderers;
import java.util.*;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
public interface Renderer extends Closeable {
	void draw();
	void setMVP(float[] matrix);
	void setFrameBufferHandler (int framebuffer);
	void setTexture2D(Map<String, Texture2D> textures);
	void setAspectRatio(float ratio);
	void setGameLogicManager(GameLogicManager manager);
}
