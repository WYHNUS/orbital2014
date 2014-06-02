package edu.nus.comp.dotagridandroid.ui.renderers;

public class TextFont {
	public static final byte CHARACTER_MAP_LENGTH = 95;
	private final Texture2D textureHandler;
	private final float characterRatio;
	public TextFont (Texture2D textureHandler) {
		this.textureHandler = textureHandler;
		this.characterRatio = textureHandler.getHeight() * CHARACTER_MAP_LENGTH / textureHandler.getWidth();
	}
	public float getCharacterSizeRatio() {
		return 0;
	}
	public int getTexture() {
		return textureHandler.getTexture();
	}
	public float[] getCharacterOffset(char ch) {
		return new float[] {ch - 32,0};
	}
}
