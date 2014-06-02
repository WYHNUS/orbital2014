package edu.nus.comp.dotagridandroid.ui.renderers;

import java.util.Map;

import static android.opengl.GLES20.*;
import edu.nus.comp.dotagridandroid.MainRenderer;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;
import static edu.nus.comp.dotagridandroid.math.RenderMaths.*;

public class TextRenderer implements Renderer {
	// parent resources
	private GenericProgram textProgram;
	private MainRenderer responder;
	private VertexBufferManager vBufMan;
	private Map<String, Texture2D> textures;
	// render params
	private float[] model = IdentityMatrix4x4(), view = IdentityMatrix4x4(), projection = IdentityMatrix4x4();
	// text attrs
	private TextFont font;
	private String text;
	private float[] textColour;
	
	public TextRenderer () {
		textProgram = new GenericProgram(
				CommonShaders.VS_IDENTITY_TEXTURED_OFFSET,
				CommonShaders.FS_IDENTITY_TEXTURED_TONED);
	}
	
	public void setMVP(float[] model, float[] view, float[] projection) {
		if (model != null)
			this.model = model;
		if (view != null)
			this.view = view;
		if (projection != null)
			this.projection = projection;
		responder.updateGraphics();
	}

	@Override
	public void close() {
	}
	
	public void setText (String text) {this.text = text;}
	public void setTextColour (float[] colour) {textColour = colour;}
	public void setTextFont (TextFont font) {this.font = font;}

	@Override
	public void draw() {
		if (text == null || text.length() == 0)
			return;
		int
			vPosition = glGetAttribLocation(textProgram.getProgramId(), "vPosition"),
			vTexture = glGetAttribLocation(textProgram.getProgramId(), "textureCoord"),
			mModel = glGetUniformLocation(textProgram.getProgramId(), "model"),
			mView = glGetUniformLocation(textProgram.getProgramId(), "view"),
			mProjection = glGetUniformLocation(textProgram.getProgramId(), "projection"),
			charMapOffset = glGetUniformLocation(textProgram.getProgramId(), "textureCoordOffset"),
			positionOffset = glGetUniformLocation(textProgram.getProgramId(), "positionOffset"),
			textureColourTone = glGetUniformLocation(textProgram.getProgramId(), "textureColourTone"),
			texture = glGetUniformLocation(textProgram.getProgramId(), "texture");
		int
			vOffset = vBufMan.getVertexBufferOffset("GenericFullSquare"),
			iOffset = vBufMan.getIndexBufferOffset("GenericFullSquareIndex"),
			vTexOffset = vBufMan.getIndexBufferOffset("GenericFullSquareTexture");
		glUseProgram(textProgram.getProgramId());
		glUniformMatrix4fv(mModel, 1, false, model, 0);
		glUniformMatrix4fv(mView, 1, false, view, 0);
		glUniformMatrix4fv(mProjection, 1, false, projection, 0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textures.get("TextFontMap").getTexture());
		glUniform1i(texture, 0);
		glUniform4fv(textureColourTone, 1, textColour, 0);
		glEnableVertexAttribArray(vPosition);
		glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, vOffset);
		glEnableVertexAttribArray(vTexture);
		glVertexAttribPointer(vTexture, 4, GL_FLOAT, false, 0, vTexOffset);
		glBindBuffer(GL_ARRAY_BUFFER, vBufMan.getVertexBuffer());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vBufMan.getIndexBuffer());
		for (int i = 0, len = text.length(); i < len; i++) {
			glUniform2fv(charMapOffset, 1, new float[]{}, 0);
			glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, iOffset);
		}
		glDisableVertexAttribArray(vTexture);
		glDisableVertexAttribArray(vPosition);
	}

	@Override
	public void setFrameBufferHandler(int framebuffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTexture2D(Map<String, Texture2D> textures) {
		this.textures = textures;
	}

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
	public void setGraphicsResponder(MainRenderer mainRenderer) {
		this.responder = mainRenderer;
	}
}
