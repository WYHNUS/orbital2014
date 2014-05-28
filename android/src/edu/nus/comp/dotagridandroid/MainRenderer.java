package edu.nus.comp.dotagridandroid;

import java.util.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.*;
import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.ui.renderers.*;

public class MainRenderer implements GLSurfaceView.Renderer {
	private Context context;
	private VertexBufferManager vBufMan;
	private CommonShapes cs;
	private Renderer r;
	private int width, height;
	final static int gridWidth = 10, gridHeight = 10;
	private Map<String, Texture2D> texture2d = new HashMap<>();
	private boolean firstRun = true;
	public MainRenderer (Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
	     gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	     // TODO: set MVP
	     r.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glEnable(GL_BLEND);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_TEXTURE);
		gl.glDepthFunc(GL_LESS);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if (firstRun) {
			// initialise
			vBufMan = new VertexBufferManager();
			cs = new CommonShapes(vBufMan);
			// TODO: different resolution of maps
			// TODO: change resource name
			// TODO: allow decoding from network, stream, files etc
			texture2d.put("GridMapBackground", new Texture2D(BitmapFactory.decodeResource(context.getResources(), R.drawable.reimu_original)));
			r = new GridRenderer(vBufMan, gridHeight, gridWidth);
			r.setTexture2D(texture2d);
			firstRun = false;
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// black background
		glClearColor(0f, 0f, 0f, 1f);
	}

	protected void setProcessingTranslation (float x, float y, float z) {
		
	}
	
	protected void setScaling (float scale) {
		
	}
	
	protected void setProcessingPerspective (float x, float y, float z) {
		
	}
}