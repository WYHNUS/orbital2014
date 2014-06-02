package edu.nus.comp.dotagridandroid.ui.renderers;

public class CommonShaders {
	public static final String VS_IDENTITY
		= "attribute vec4 vPosition;"
		+ "uniform mat4 model, view, projection;"
		+ "void main() {"
			+ "gl_Position = vPosition * model * view * projection;"
		+ "}";
	public static final String FS_IDENTITY
		= "precision mediump float;"
		+ "uniform vec4 vColor;"
		+ "void main () {"
			+ "gl_FragColor = vColor;"
		+ "}";
	// textured
	public static final String VS_IDENTITY_TEXTURED
		= "attribute vec4 vPosition;"
		+ "attribute vec2 textureCoord;"
		+ "uniform mat4 model, view, projection;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main() {"
			+ "gl_Position = vPosition * model * view * projection;"
			+ "autoTextureCoord = textureCoord;"
		+ "}";
	public static final String VS_IDENTITY_TEXTURED_SCALED_OFFSET
		= "attribute vec4 vPosition;"
		+ "attribute vec2 textureCoord;"
		+ "uniform vec2 textureCoordOffset, textureScale;"
		+ "uniform mat4 model, view, projection;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main () {"
			+ "vec4 pos = vPosition;"
			+ "gl_Position = pos * model * view * projection;"
			+ "autoTextureCoord = textureScale * (textureCoord + textureCoordOffset);"
		+ "}";
	public static final String FS_IDENTITY_TEXTURED
		= "precision mediump float;"
		+ "uniform sampler2D texture;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main () {"
			+ "gl_FragColor = texture2D (texture, autoTextureCoord);"
		+ "}";
	public static final String FS_IDENTITY_TEXTURED_TONED
		= "precision mediump float;"
		+ "uniform sampler2D texture;"
		+ "uniform vec4 textureColorTone;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main () {"
			+ "vec4 color = texture2D (texture, autoTextureCoord);"
			+ "gl_FragColor = color * textureColorTone + color;"
		+ "}";
	
}
