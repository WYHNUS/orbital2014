package edu.nus.comp.dotagridandroid.ui.renderers;

import java.nio.*;
import java.util.Arrays;
import java.util.Map;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class NormalGenerator implements Renderer {
	public static final int RESOLUTION = 10;
	private final int rows, columns, width, height;
	private final float[] terrain;
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	private float ratio;
	private GraphicsResponder responder;
	
	// resource
	private GenericProgram normalProgram;
	private int frameBuf, renderBuf, textureHandler;
	private FloatBuffer pos, normal;
	
	private final float[] identity = IdentityMatrix4x4();
	private boolean renderReady = false;
	private Thread computeTask;
	public NormalGenerator (int columns, int rows, float[] terrain, int width, int height) {
		this.rows = rows;
		this.columns = columns;
		this.terrain = terrain;
		this.width = width;
		this.height = height;
		normalProgram = new GenericProgram(CommonShaders.VS_IDENTITY_VARYING_COLOR, CommonShaders.FS_IDENTITY_VARYING_COLOR);
		// framebuffer object
		int[] buf = new int[1];
		glGenFramebuffers(1, buf, 0);
		frameBuf = buf[0];
		glGenRenderbuffers(1, buf, 0);
		renderBuf = buf[0];
		glGenTextures(1, buf, 0);
		textureHandler = buf[0];
		computeTask = new Thread() {
			@Override
			public void run() {
				computeTexture();
			}
		};
		computeTask.start();
	}
	@Override
	public void setVertexBufferManager(VertexBufferManager manager) {
		this.vBufMan = manager;
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {}

	private void computeTexture() {
		pos = BufferUtils.createFloatBuffer(rows * RESOLUTION * columns * RESOLUTION * 24);
		normal = BufferUtils.createFloatBuffer(rows * columns * RESOLUTION * RESOLUTION * 24);
		int offset;
		final int arrWidth = columns * RESOLUTION + 1;
		for (int i = 0; i < rows; i++) {
			offset = i * arrWidth * RESOLUTION;
			for (int j = 0; j < columns; j++) {
				for (int s = 0; s < RESOLUTION; s++)
					for (int t = 0; t < RESOLUTION; t++) {
						final int
							bottomLeft = offset + s * arrWidth + t,
							bottomRight = bottomLeft + 1,
							topLeft = offset + (s + 1) * arrWidth + t,
							topRight = topLeft + 1;
						pos.put(terrain, 4 * bottomLeft, 4)
						.put(terrain, 4 * bottomRight, 4)
						.put(terrain, 4 * topLeft, 4)
						.put(terrain, 4 * topLeft, 4)
						.put(terrain, 4 * topRight, 4)
						.put(terrain, 4 * bottomRight, 4);
						// lower
						float[] n = NormalisedVector3(Vector3CrossProduct (
								new float[]{
										terrain[4 * bottomRight] - terrain[4 * bottomLeft],
										terrain[4 * bottomRight + 1] - terrain[4 * bottomLeft + 1],
										terrain[4 * bottomRight + 2] - terrain[4 * bottomLeft + 2]
										},
								new float[]{
										terrain[4 * topLeft] - terrain[4 * bottomLeft],
										terrain[4 * topLeft + 1] - terrain[4 * bottomLeft + 1],
										terrain[4 * topLeft + 2] - terrain[4 * bottomLeft + 2]
										}
								));
						n = new float[] {(n[0] + 1)/2, (n[1] + 1)/2, (n[2] + 1)/2};
						normal.put(n).put(1).put(n).put(1).put(n).put(1);
						// upper
						n = NormalisedVector3(Vector3CrossProduct (
								new float[]{
										terrain[4 * topLeft] - terrain[4 * topRight],
										terrain[4 * topLeft + 1] - terrain[4 * topRight + 1],
										terrain[4 * topLeft + 2] - terrain[4 * topRight + 2]
										},
								new float[]{
										terrain[4 * bottomRight] - terrain[4 * topRight],
										terrain[4 * bottomRight + 1] - terrain[4 * topRight + 1],
										terrain[4 * bottomRight + 2] - terrain[4 * topRight + 2]
										}
								));
						n = new float[] {(n[0] + 1)/2, (n[1] + 1)/2, (n[2] + 1)/2};
						normal.put(n).put(1).put(n).put(1).put(n).put(1);
					}
				offset += RESOLUTION;
			}
		}
	}
	@Override
	public void setRenderReady() {
		// prepare normal texture
		glBindTexture(GL_TEXTURE_2D, textureHandler);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, renderBuf);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuf);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureHandler, 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuf);
		glViewport(0, 0, width, height);	// match texture size
		glClearColor(0, 0, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// end
		// row by row
		glUseProgram(normalProgram.getProgramId());
		// note, do not use vbufman
		// the array is too large and it is used once
		final int
			vPosition = glGetAttribLocation(normalProgram.getProgramId(), "vPosition"),
			vColor = glGetAttribLocation(normalProgram.getProgramId(), "vColor"),
			mModel = glGetUniformLocation(normalProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(normalProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(normalProgram.getProgramId(), "projection");
		glUniformMatrix4fv(mModel, 1, false, identity, 0);
		glUniformMatrix4fv(mView, 1, false, identity, 0);
		glUniformMatrix4fv(mProjection, 1, false, identity, 0);
		try {
			computeTask.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glVertexAttribPointer(vColor, 4, GL_FLOAT, false, 0, normal.position(0));
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, pos.position(0));
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(vColor);
		glDrawArrays(GL_TRIANGLES, 0, rows * columns * RESOLUTION * RESOLUTION * 6);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(vColor);
		pos = normal = null;
		renderReady = true;
	}
	
	public int getNormalTexture() {
		return textureHandler;
	}

	@Override
	public boolean getReadyState() {
		return renderReady;
	}

	@Override
	public void draw() {
		
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		normalProgram.close();
		glDeleteFramebuffers(1, new int[]{frameBuf}, 0);
		glDeleteRenderbuffers(1, new int[]{renderBuf}, 0);
		glDeleteTextures(1, new int[]{textureHandler}, 0);
	}
}