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
	public static final int MAX_LIGHT_SOURCE = 16;
	public static final String VS_IDENTITY_SPECIAL_LIGHTING
		= "attribute vec4 vPosition;"
		+ "attribute vec2 textureCoord, normalCoord;"
		+ "uniform mat4 model, view, projection;"
		+ "varying vec3 position;"
		+ "varying vec2 autoTextureCoord, autoNormalCoord;"
		+ "void main () {"
			+ "gl_Position = vPosition * model * view * projection;"
			+ "vec4 pos = vPosition * model;"
			+ "position = pos.xyz / pos.w;"
			+ "autoTextureCoord = textureCoord;"
			+ "autoNormalCoord = normalCoord;"
		+ "}";
	public static final String FS_IDENTITY_SPECIAL_LIGHTING
		= "precision mediump float;"
		+ "uniform sampler2D texture, normal;"
		+ "struct lightsrc {"
			+ "vec3 source, color;"
			+ "float specular, attenuation;"
		+ "};"
		+ "uniform lightsrc lights[" + MAX_LIGHT_SOURCE + "];"
		+ "uniform vec3 camera;"
		+ "varying vec2 autoTextureCoord, autoNormalCoord;"
		+ "varying vec3 position;"
		+ "void main () {"
			+ "int i;"
			+ "vec4 materialColor = texture2D (texture, autoTextureCoord);"
			+ "vec3 lightColor = vec3(0.0,0.0,0.0);"
			+ "vec3 normalVec = normalize (texture2D (normal, autoNormalCoord).rgb * 2.0 - vec3(1.0,1.0,1.0));"
			+ "vec3 observer = camera - position;"
			+ "float observerDistance = length(observer);"
			+ "vec3 normalObserver = normalize (observer);"
			+ "for (i = 0; i < " + MAX_LIGHT_SOURCE + "; i++) {"
				+ "vec3 inbound = normalize (position - lights[i].source);"
				+ "vec3 outbound = inbound - 2.0 * dot (inbound, normalVec) * normalVec;"
				+ "float atn = (max(0.0, dot(normalVec, -inbound)) / (1.0 + lights[i].attenuation * observerDistance * observerDistance));"
				+ "lightColor = lightColor + lights[i].color * atn * (1.0 + pow (dot (normalObserver, outbound), lights[i].specular));"
			+ "}"
			+ "vec3 finalColor = lightColor * materialColor.rgb;"
			+ "gl_FragColor = vec4(finalColor, materialColor.a);"
		+ "}";
}
