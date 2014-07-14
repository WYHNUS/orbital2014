package edu.nus.comp.dotagridandroid.ui.renderers.scenes;

import edu.nus.comp.dotagridandroid.ui.renderers.*;

public interface SceneRenderer extends Renderer {
	void onTransferToView (SceneConfiguration configuration);
	void setMainSceneRenderer(MainSceneRenderer renderer);
}
