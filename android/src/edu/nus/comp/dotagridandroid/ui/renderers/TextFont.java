package edu.nus.comp.dotagridandroid.ui.renderers;

public class TextFont {
	private final Texture2D textureHandler;
	private final float characterRatio;
	public TextFont (Texture2D textureHandler, float characterRatio) {
		this.textureHandler = textureHandler;
		this.characterRatio = characterRatio;
	}
	public float getCharacterSizeRatio() {
		return 0;
	}
	public int getTexture() {
		return 0;
	}
}
