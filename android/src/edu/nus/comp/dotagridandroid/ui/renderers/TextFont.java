package edu.nus.comp.dotagridandroid.ui.renderers;

public class TextFont {
	public static final byte CHARACTER_MAP_LENGTH = 95;
	private final Texture2D textureHandler;
	private final float characterRatio;
	public TextFont (Texture2D textureHandler) {
		this.textureHandler = textureHandler;
		this.characterRatio = textureHandler.getWidth() / (float) CHARACTER_MAP_LENGTH / textureHandler.getHeight();
	}
	public float getCharacterSizeRatio() {
		return characterRatio;
	}
	public int getTexture() {
		return textureHandler.getTexture();
	}
	public float[] getCharacterOffset(char ch) {
		return new float[] {ch - 32,0};
	}
}
