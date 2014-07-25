package edu.nus.comp.dotagridandroid.ui.renderers.scenes;
import java.util.Map;
import edu.nus.comp.dotagridandroid.ui.renderers.*;

public interface SceneRenderer extends Renderer {
	void onTransferToView (Map<String, Object> configuration);
	void setMainSceneRenderer(MainSceneRenderer renderer);
}
