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
	public static final int RESOLUTION = 16;
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
	private Buffer texBuf;
	
	private final float[] identity = IdentityMatrix4x4();
	private boolean renderReady = false;
	private Thread computeTask;
	public NormalGenerator (int columns, int rows, float[] terrain, int width, int height) {
		this.rows = rows;
		this.columns = columns;
		this.terrain = terrain;
		this.width = width;
		this.height = height;
		normalProgram = new GenericProgram(CommonShaders.VS_IDENTITY, CommonShaders.FS_IDENTITY);
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
//				computeTexture();
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

	public void computeTexture() {
		pos = BufferUtils.createFloatBuffer(rows * columns * RESOLUTION * RESOLUTION * 24);
		normal = BufferUtils.createFloatBuffer(rows * columns * RESOLUTION * RESOLUTION * 24);
		int offset;
		final int arrWidth = columns * RESOLUTION;
		for (int i = 0; i < rows; i++) {
			offset = i * columns * RESOLUTION * RESOLUTION;
			for (int j = 0; j < columns; j++) {
				for (int s = 0; s < RESOLUTION; s++)
					for (int t = 0; t < RESOLUTION; t++) {
						pos.put(terrain, 4 * (offset + s * arrWidth + t), 4)
						.put(terrain, 4 * (offset + (s + 1) * arrWidth + t), 4)
						.put(terrain, 4 * (offset + s * arrWidth + t + 1), 4)
						.put(terrain, 4 * (offset + s * arrWidth + t + 1), 4)
						.put(terrain, 4 * (offset + (s + 1) * arrWidth + t), 4)
						.put(terrain, 4 * (offset + (s + 1) * arrWidth + t + 1), 4);
						// lower
						float[] n = NormalisedVector3(Vector3CrossProduct (
								new float[]{
										terrain[4 * (offset + s * arrWidth + t + 1)] - terrain[4 * (offset + s * arrWidth + t)],
										terrain[4 * (offset + s * arrWidth + t + 1) + 1] - terrain[4 * (offset + s * arrWidth + t) + 1],
										terrain[4 * (offset + s * arrWidth + t + 1) + 2] - terrain[4 * (offset + s * arrWidth + t) + 2]
										},
								new float[]{
										terrain[4 * (offset + (s + 1) * arrWidth + t)] - terrain[4 * (offset + s * arrWidth + t)],
										terrain[4 * (offset + (s + 1) * arrWidth + t) + 1] - terrain[4 * (offset + s * arrWidth + t) + 1],
										terrain[4 * (offset + (s + 1) * arrWidth + t) + 2] - terrain[4 * (offset + s * arrWidth + t) + 2]
										}
								));
						normal.put(n).put(0).put(n).put(0).put(n).put(0);
						// upper
						n = NormalisedVector3(Vector3CrossProduct (
								new float[]{
										terrain[4 * (offset + (s + 1) * arrWidth + t)] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1)],
										terrain[4 * (offset + (s + 1) * arrWidth + t) + 1] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1) + 1],
										terrain[4 * (offset + (s + 1) * arrWidth + t) + 2] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1) + 2]
										},
								new float[]{
										terrain[4 * (offset + s * arrWidth + t + 1)] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1)],
										terrain[4 * (offset + s * arrWidth + t + 1) + 1] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1) + 1],
										terrain[4 * (offset + s * arrWidth + t + 1) + 2] - terrain[4 * (offset + (s + 1) * arrWidth + t + 1) + 2]
										}
								));
						normal.put(n).put(0).put(n).put(0).put(n).put(0);
					}
				offset += RESOLUTION;
			}
		}
	}
	@Override
	public void setRenderReady() {
		// prepare normal texture
		glBindTexture(GL_TEXTURE_2D, textureHandler);
		int[] buf = new int[width * height];
		Arrays.fill(buf, 0xff0000ff);
		texBuf = BufferUtils.createIntBuffer(width * height).put(buf);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texBuf.position(0));
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D, 0);
//		glBindRenderbuffer(GL_RENDERBUFFER, renderBuf);
//		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height);
		glViewport(0, 0, width, height);	// match texture size
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuf);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureHandler, 0);
//		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuf);
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(1, 1, 1, 1);
		// end
		// row by row
		glUseProgram(normalProgram.getProgramId());
		// note, do not use vbufman
		// the array is too large and it is used once
		final int
			vPosition = glGetAttribLocation(normalProgram.getProgramId(), "vPosition"),
//			vColor = glGetAttribLocation(normalProgram.getProgramId(), "vColor"),
			vColor = glGetUniformLocation(normalProgram.getProgramId(), "vColor"),
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
//		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, pos.position(0));
//		glVertexAttribPointer(vColor, 4, GL_FLOAT, false, 0, normal.position(0));
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, BufferUtils.createFloatBuffer(12).put(new float[]{-1,-1,0,1,0,1,0,1,1,-1,0,1}).position(0));
		glUniform4f(vColor, 1, 1, 1, 1);
		glDrawArrays(GL_TRIANGLES, 0, 3);
//		glDrawArrays(GL_TRIANGLES, 0, rows * columns * RESOLUTION * RESOLUTION * 6);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		pos = normal = null;
		renderReady = true;
		responder.updateGraphics();
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
