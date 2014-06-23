package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer.GraphicsResponder;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import edu.nus.comp.dotagridandroid.ui.event.EventData;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class ButtonRenderer implements Renderer {
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	private GenericProgram buttonProgram;
	private boolean pressed = false;
	
	private GLResourceManager vBufMan;
	private Map<String, Texture2D> textures;
	private String textureName = "DefaultButton";
	private GameLogicManager manager;
	private String tapEventName, longPressEventName;
	private Map<String, Object> tapExtendedData, longPressExtendedData;
	private float ratio;
	
	private boolean tapEnabled = false, longPressEnabled = false;
	public ButtonRenderer () {
		model = view = projection = IdentityMatrix4x4();
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		vBufMan = manager;
		buttonProgram = manager.getProgram("identityTexturedWithTone");
		if (buttonProgram == null) {
			buttonProgram = new GenericProgram(CommonShaders.VS_IDENTITY_TEXTURED, CommonShaders.FS_IDENTITY_TEXTURED_TONED);
			manager.setProgram("identityTexturedWithTone", buttonProgram);
		}
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}

	@Override
	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
		this.manager = manager;
	}

	@Override
	public void setGraphicsResponder(GraphicsResponder mainRenderer) {
	}
	
	public void setTapRespondName(String eventName) {
		if (eventName != null)
			this.tapEventName = eventName;
	}
	
	public void setTapRespondData(Map<String, Object> data) {
		tapExtendedData = data; 
	}
	
	public void setLongPressRespondName(String eventName) {
		if (eventName != null)
			this.longPressEventName = eventName;
	}
	
	public void setLongPressRespondData(Map<String, Object> data) {
		longPressExtendedData = data;
	}
	
	public void setButtonTexture(String name) {
		if (textures.containsKey(name))
			textureName = name;
	}
	
	public void setTapEnabled(boolean enabled) {
		this.tapEnabled = enabled;
	}
	
	public void setLongPressEnabled(boolean enabled) {
		this.longPressEnabled = enabled;
	}

	@Override
	public void setMVP(float[] model, float[] view, float[] projection) {
		if (model != null)
			this.model = model;
		if (view != null)
			this.view = view;
		if (projection != null)
			this.projection = projection;
	}

	@Override
	public void setRenderReady() {}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {}

	@Override
	public boolean getReadyState() {
		return true;
	}

	@Override
	public void draw() {
		final int
			vPosition = glGetAttribLocation(buttonProgram.getProgramId(), "vPosition"),
			mModel = glGetUniformLocation(buttonProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(buttonProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(buttonProgram.getProgramId(), "projection"),
			textureLocation = glGetUniformLocation(buttonProgram.getProgramId(), "texture"),
			textureCoord = glGetAttribLocation(buttonProgram.getProgramId(), "textureCoord"),
			textureTone = glGetUniformLocation(buttonProgram.getProgramId(), "textureColorTone");
		final int
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			vTOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTextureYInverted"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex");
		glUseProgram(buttonProgram.getProgramId());
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get(textureName).getTexture());
		glUniform1i(textureLocation, 0);
		if (tapEnabled)
			glUniform4f(textureTone, 0, 0, 0, 0);
		else
			glUniform4f(textureTone, -.79f, -.28f, -.93f, 0);	// 21% red, 72% green, 7% blue
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, 0, vTOffset);
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(textureCoord);
		glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		glDisableVertexAttribArray(vPosition);
		glDisableVertexAttribArray(textureCoord);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		final boolean hit;
		final float[] mvp = FlatMatrix4x4Multiplication(projection, view, model);
		final float[] pos = FlatMatrix4x4Vector4Multiplication(FlatInverseMatrix4x4(mvp), new float[]{e.data.x[0], e.data.y[0], -1, 1});
		hit = pos[0] >= -1 && pos[0] <= 1 && pos[1] >= -1 && pos[1] <= 1;
		if (e.type == ControlEvent.TYPE_DOWN) {
			pressed = true;
			return hit && e.data.pointerCount == 1;
		} else if (e.type == ControlEvent.TYPE_DRAG) {
			if (Math.abs(e.data.deltaX) < ControlEvent.TAP_DRIFT_LIMIT / ratio && Math.abs(e.data.deltaY) < ControlEvent.TAP_DRIFT_LIMIT * ratio)
				return pressed && (tapEnabled || longPressEnabled);
		} else if (e.type == ControlEvent.TYPE_CLEAR) {
			if (hit && e.data.pointerCount == 1 && pressed) {
				if (longPressEnabled && e.data.eventTime - e.data.startTime > ControlEvent.TAP_LONG_TIME_LIMIT) {
					ControlEvent newevt = new ControlEvent(ControlEvent.TYPE_CLICK | ControlEvent.TYPE_INTERPRETED, new EventData(e.data));
					newevt.extendedType = longPressEventName;
					newevt.emitter = this;
					newevt.data.extendedData = longPressExtendedData;
					manager.processEvent(newevt);
				} else if (tapEnabled) {
					ControlEvent newevt = new ControlEvent(ControlEvent.TYPE_CLICK | ControlEvent.TYPE_INTERPRETED, new EventData(e.data));
					newevt.extendedType = tapEventName;
					newevt.emitter = this;
					newevt.data.extendedData = tapExtendedData;
					manager.processEvent(newevt);
				}
			}
			return false;
		}
		pressed = false;
		return false;
	}

	@Override
	public void close() {}

}
