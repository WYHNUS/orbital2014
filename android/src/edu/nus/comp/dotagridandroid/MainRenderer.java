package edu.nus.comp.dotagridandroid;

import java.util.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.*;
import android.opengl.*;
import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.ui.event.*;
import edu.nus.comp.dotagridandroid.ui.renderers.*;
import edu.nus.comp.dotagridandroid.appsupport.AppNativeAPI;
import edu.nus.comp.dotagridandroid.logic.*;

public class MainRenderer implements GLSurfaceView.Renderer, Closeable {
	private Context context;
	private MainSurfaceView view;
	private VertexBufferManager vBufMan;
	private CommonShapes cs;
	private Renderer r;
	private int width, height;
	private Map<String, Texture2D> texture2d = new HashMap<>();
	private GameLogicManager manager;
	private boolean closed;
	public MainRenderer (Context context, MainSurfaceView view) {
		this.manager = ((Main) context).getGameLogicManager();
		this.context = context;
		this.view = view;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (!closed && r.getReadyState()) {
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			glViewport(0, 0, width, height);
//			glClearColor(.4f, .6f, .9f, 1);
			glClearColor(0,0,0,1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//			AppNativeAPI.testGL();
			r.draw();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		r = new MainSceneRenderer();
		System.gc();
		this.width = width;
		this.height = height;
		glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		vBufMan = new VertexBufferManager();
		cs = new CommonShapes(vBufMan);
		// TODO: different resolution of maps
		// TODO: change resource name
		// TODO: allow decoding from network, stream, files etc (to be done in GameState)
		Bitmap image;
		texture2d.put("GridMapBackground", new Texture2D(image = BitmapFactory.decodeResource(context.getResources(), R.drawable.reimu_original)));
		image.recycle();
		System.gc();
		texture2d.put("DefaultTextFontMap", new Texture2D(image = BitmapFactory.decodeResource(context.getResources(), R.drawable.textmap)));
		image.recycle();
		System.gc();
		texture2d.put("StatusControlBackground", texture2d.get("GridMapBackground"));//new Texture2D(image = BitmapFactory.decodeResource(context.getResources(), R.drawable.dota2bg)));
//		image.recycle();
		System.gc();
		texture2d.put("DefaultButton", new Texture2D(image = BitmapFactory.decodeResource(context.getResources(), R.drawable.button)));
		image.recycle();
		System.gc();
		r.setVertexBufferManager(vBufMan);
		r.setTexture2D(Collections.unmodifiableMap(texture2d));
		r.setGameLogicManager(manager);
		r.setGraphicsResponder(new GraphicsResponder());
		r.setAspectRatio(ratio);
		r.setRenderReady();
		closed = false;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE);
//		glEnable(GL_STENCIL_TEST);
//		glDepthFunc(GL_LESS);
		glDepthFunc(GL_LEQUAL);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		glStencilFunc(GL_ALWAYS, 1, 1);
//		glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
	}
	
	public void passEvent (ControlEvent event) {
		if (r != null) {
			// normalise
			event.data.deltaX /= width / 2;
			event.data.deltaY /= -height / 2;
			for (int i = event.data.pointerCount - 1; i >= 0; i--) {
				event.data.x[i] = event.data.x[i] / width * 2 - 1;
				event.data.y[i] = 1 - event.data.y[i] / height * 2;
			}
			r.passEvent(event);
		}
	}

	@Override
	public void close() {
		if (closed)
			return;
		this.closed = true;
		if (r != null)
			r.close();
		if (vBufMan != null)
			vBufMan.close();
		for (Texture2D t : texture2d.values())
			t.close();
	}
	
	public class GraphicsResponder {
		public void updateGraphics() {
			view.requestRender();
		}
	}

}