package edu.nus.comp.dotagridandroid.ui.renderers;

public class CommonShaders {
	public static final String VS_IDENTITY
		= "attribute vec4 vPosition;"
		+ "uniform mat4 mMVP;"
		+ "void main() {"
			+ "gl_Position = mMVP * vPosition;"
		+ "}";
	public static final String FS_IDENTITY
		= "precision mediump float;"
		+ "uniform vec4 vColor;"
		+ "void main () {"
			+ "gl_FragColor = vColor;"
		+ "}";
	
	public static final String VS_IDENTITY_TEXTURED
		= "attribute vec4 vPosition;"
		+ "attribute vec2 textureCoord;"
		+ "uniform mat4 mMVP;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main() {"
			+ "gl_Position = mMVP * vPosition;"
			+ "autoTextureCoord = textureCoord;"
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
