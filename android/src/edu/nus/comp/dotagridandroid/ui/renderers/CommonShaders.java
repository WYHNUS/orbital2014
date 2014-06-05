package edu.nus.comp.dotagridandroid.ui.renderers;

public class CommonShaders {
	public static final String VS_IDENTITY
		= "attribute vec4 vPosition;"
		+ "uniform mat4 model, view, projection;"
		+ "void main() {"
			+ "gl_Position = vPosition * model * view * projection;"
		+ "}";
	public static final String VS_IDENTITY_VARYING_COLOR
		= "attribute vec4 vPosition, vColor;"
		+ "uniform mat4 model, view, projection;"
		+ "varying vec4 autoColor;"
		+ "void main () {"
			+ "gl_Position = vPosition * model * view * projection;"
			+ "autoColor = vColor;"
		+ "}";
	public static final String FS_IDENTITY
		= "precision mediump float;"
		+ "uniform vec4 vColor;"
		+ "void main () {"
			+ "gl_FragColor = vColor;"
		+ "}";
	public static final String FS_IDENTITY_VARYING_COLOR
		= "precision mediump float;"
		+ "varying vec4 autoColor;"
		+ "void main () {"
			+ "gl_FragColor = autoColor;"
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
			+ "gl_Position = vPosition * model * view * projection;"
			+ "autoTextureCoord = (textureCoord + textureCoordOffset) * textureScale;"
		+ "}";
	public static final String VS_IDENTITY_TEXTURED_TRANSFORMED
		= "attribute vec4 vPosition;"
		+ "attribute vec2 textureCoord;"
		+ "uniform mat3 textureMat;"
		+ "uniform mat4 model, view, projection;"
		+ "varying vec2 autoTextureCoord;"
		+ "void main () {"
			+ "gl_Position = vPosition * model * view * projection;"
			+ "vec3 transformedTextureCoord = vec3(textureCoord, 1.0) * textureMat;"
			+ "autoTextureCoord = transformedTextureCoord.xy / transformedTextureCoord.z;"
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
	// shadow and light maps
	public static final String FS_IDENTITY_SPECIAL_LIGHTING
		= "precision mediump float;"
		+ "uniform sampler2D texture, normal;"
		+ "struct lightsrc {"
			+ "vec3 source;"
			+ "vec4 color;"
		+ "};"
		+ "uniform lightsrc lights[128];"
		+ "uniform vec3 camera;"
		+ "void main () {"
			+ ""
		+ "}";
}
