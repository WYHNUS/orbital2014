package edu.nus.comp.dotagridandroid;

import java.util.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.*;
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
	public MainRenderer (Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
	     glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	     glClearColor(0, 0, 0, 1);
	     // TODO: set MVP
	     r.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		glViewport(0, 0, width, height);
		float ratio = (float) width / height;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE);
		glDepthFunc(GL_LESS);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//gl.glClearColor(1, 0, 0, 1);
		vBufMan = new VertexBufferManager();
		cs = new CommonShapes(vBufMan);
		// TODO: different resolution of maps
		// TODO: change resource name
		// TODO: allow decoding from network, stream, files etc
		Bitmap image;
		texture2d.put("GridMapBackground", new Texture2D(image = BitmapFactory.decodeResource(context.getResources(), R.drawable.reimu_original)));
		image.recycle();
		r = new GridRenderer(vBufMan, gridHeight, gridWidth);
		r.setTexture2D(texture2d);
	}

	protected void setProcessingTranslation (float x, float y, float z) {
		
	}
	
	protected void setScaling (float scale) {
		
	}
	
	protected void setProcessingPerspective (float x, float y, float z) {
		
	}
}