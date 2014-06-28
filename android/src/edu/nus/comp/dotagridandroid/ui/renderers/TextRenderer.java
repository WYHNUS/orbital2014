package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.*;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class TextRenderer implements Renderer {
	private static final float[] FONTTRANSFORMATION = FlatMatrix4x4Multiplication(
			FlatScalingMatrix4x4(.5f, .5f, 1),
			FlatTranslationMatrix4x4(1, -1, 0));
	// parent resources
	private GenericProgram textProgram;
	private MainRenderer.GraphicsResponder responder;
	private GLResourceManager vBufMan;
	// render params
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	// text attrs
	private TextFont font;
	private List<String> text = Collections.emptyList();
	private float[] textColour = {1,-1,-1,0};
	private boolean yAligned = false;
	private float xExtreme, yExtreme;
	public TextRenderer () {
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
	public void close() {
	}
	
	public void setText (String text) {
		xExtreme = yExtreme = 0;
		if (text != null && text.length() > 0) {
			this.text = Collections.singletonList(text);
			responder.updateGraphics();
			if (yAligned) {
				xExtreme = text.length() * font.getCharacterSizeRatio();
				yExtreme = 1;
			} else {
				xExtreme = text.length();
				yExtreme = 1 / font.getCharacterSizeRatio();
			}
		}
	}
	public void setTexts (String... text) {
		xExtreme = yExtreme = 0;
		this.text = Arrays.asList(text);
		int c = 0;
		if (yAligned) {
			for (String line : text)
				if (line != null) {
					xExtreme = Math.max(xExtreme, line.length() * font.getCharacterSizeRatio());
					c++;
				}
			yExtreme = c;
		} else {
			for (String line : text)
				if (line != null) {
					xExtreme = Math.max(xExtreme, line.length());
					c++;
				}
			yExtreme = c / font.getCharacterSizeRatio();
		}
		responder.updateGraphics();
	}
	public void setTextColour (float[] colour) {textColour = colour;}
	public void setTextFont (TextFont font) {this.font = font;}
	public void setYAligned (boolean value) {this.yAligned = value;}
	public float getXExtreme () {return xExtreme;}
	public float getYExtreme () {return yExtreme;}

	@Override
	public void draw() {
		if (text == null)
			return;
		final int
			vPosition = glGetAttribLocation(textProgram.getProgramId(), "vPosition"),
			vTexture = glGetAttribLocation(textProgram.getProgramId(), "textureCoord"),
			mModel = glGetUniformLocation(textProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(textProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(textProgram.getProgramId(), "projection"),
			charMapOffset = glGetUniformLocation(textProgram.getProgramId(), "textureCoordOffset"),
			textureScale = glGetUniformLocation(textProgram.getProgramId(), "textureScale"),
			textureColourTone = glGetUniformLocation(textProgram.getProgramId(), "textureColorTone"),
			texture = glGetUniformLocation(textProgram.getProgramId(), "texture");
		final int
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex"),
			vTexOffset = vBufMan.getVertexBufferOffset("GenericFullSquareTextureYInverted");
		glUseProgram(textProgram.getProgramId());
		final float[] scale = {1/(float) TextFont.CHARACTER_MAP_LENGTH,1};
		glUniform2fv(textureScale, 1, scale, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, font.getTexture());
		glUniform1i(texture, 0);
		glUniform4fv(textureColourTone, 1, textColour, 0);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glVertexAttribPointer(vTexture, 2, GL_FLOAT, false, 0, vTexOffset);
		glEnableVertexAttribArray(vPosition);
		glEnableVertexAttribArray(vTexture);
		byte lines = 0;
		for (String str : text) {
			for (int i = 0, len = str.length(); i < len; i++) {
				glUniformMatrix4fv(mModel, 1, false, FlatMatrix4x4Multiplication(
						model,
						yAligned ? FlatScalingMatrix4x4(font.getCharacterSizeRatio(), 1, 1) : FlatScalingMatrix4x4(1,1/font.getCharacterSizeRatio(),1),
						FlatTranslationMatrix4x4(i, -lines, 0),
						FONTTRANSFORMATION), 0);
				glUniform2fv(charMapOffset, 1, font.getCharacterOffset(str.charAt(i)), 0);
				glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
			}
			lines++;
		}
		glDisableVertexAttribArray(vTexture);
		glDisableVertexAttribArray(vPosition);
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {}

	@Override
	public void setAspectRatio(float ratio) {}

	@Override
	public void setGameLogicManager(GameLogicManager manager) {
	}

	@Override
	public boolean passEvent(ControlEvent e) {
		return false;
	}

	@Override
	public void setGraphicsResponder(MainRenderer.GraphicsResponder mainRenderer) {
		this.responder = mainRenderer;
	}

	@Override
	public void setRenderReady() {
	}

	@Override
	public void setGLResourceManager(GLResourceManager manager) {
		vBufMan = manager;
		textProgram = vBufMan.getProgram("textProgram");
		if (textProgram == null) {
			textProgram = new GenericProgram(
					CommonShaders.VS_IDENTITY_TEXTURED_SCALED_OFFSET,
					CommonShaders.FS_IDENTITY_TEXTURED_TONED);
			vBufMan.setProgram("textProgram", textProgram);
		}
	}
	
	@Override
	public boolean getReadyState() {return true;}
	
	@Override
	public void notifyUpdate(Map<String, Object> updates) {}	// nothing
}
