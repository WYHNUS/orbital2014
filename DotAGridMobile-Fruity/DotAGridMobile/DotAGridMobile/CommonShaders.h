//
//  CommonShaders.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

// ported from edu.nus.comp.dotagridandroid.ui.renderers.CommonShaders
const char * const VS_IDENTITY =
"attribute vec4 vPosition; \
uniform mat4 model, view, projection;\
void main() {\
    gl_Position = vPosition * model * view * projection;\
}";
const char * const VS_IDENTITY_VARYING_COLOR =
"attribute vec4 vPosition, vColor;\
uniform mat4 model, view, projection;\
varying vec4 autoColor;\
void main () {\
    gl_Position = vPosition * model * view * projection;\
    autoColor = vColor;\
}";
const char * const FS_IDENTITY =
"precision mediump float;\
uniform vec4 vColor;\
void main () {\
    gl_FragColor = vColor;\
}";
const char * const FS_IDENTITY_VARYING_COLOR =
"precision mediump float;\
varying vec4 autoColor;\
void main () {\
    gl_FragColor = autoColor;\
}";
// textured
const char * const VS_IDENTITY_TEXTURED =
"attribute vec4 vPosition;\
attribute vec2 textureCoord;\
uniform mat4 model, view, projection;\
varying vec2 autoTextureCoord;\
void main() {\
    gl_Position = vPosition * model * view * projection;\
    autoTextureCoord = textureCoord;\
}";
const char * const VS_IDENTITY_TEXTURED_SCALED_OFFSET =
"attribute vec4 vPosition;\
attribute vec2 textureCoord;\
uniform vec2 textureCoordOffset, textureScale;\
uniform mat4 model, view, projection;\
varying vec2 autoTextureCoord;\
void main () {\
    gl_Position = vPosition * model * view * projection;\
    autoTextureCoord = (textureCoord + textureCoordOffset) * textureScale;\
}";
const char * const VS_IDENTITY_TEXTURED_TRANSFORMED =
"attribute vec4 vPosition;\
attribute vec2 textureCoord;\
uniform mat3 textureMat;\
uniform mat4 model, view, projection;\
varying vec2 autoTextureCoord;\
void main () {\
    gl_Position = vPosition * model * view * projection;\
    vec3 transformedTextureCoord = vec3(textureCoord, 1.0) * textureMat;\
    autoTextureCoord = transformedTextureCoord.xy / transformedTextureCoord.z;\
}";
const char * const FS_IDENTITY_TEXTURED =
"precision mediump float;\
uniform sampler2D texture;\
varying vec2 autoTextureCoord;\
void main () {\
    gl_FragColor = texture2D (texture, autoTextureCoord);\
}";
const char * const FS_IDENTITY_TEXTURED_TONED =
"precision mediump float;\
uniform sampler2D texture;\
uniform vec4 textureColorTone;\
varying vec2 autoTextureCoord;\
void main () {\
    vec4 color = texture2D (texture, autoTextureCoord);\
    gl_FragColor = color * textureColorTone + color;\
}";
// shadow and light maps
const char * const VS_IDENTITY_SPECIAL_LIGHTING =
"attribute vec4 vPosition;\
attribute vec2 textureCoord, normalCoord;\
uniform mat4 model, view, projection, lightTransform;\
varying vec3 position;\
varying vec2 autoTextureCoord, autoNormalCoord, autoShadowCoord;\
varying float actualDepth;\
void main () {\
gl_Position = vPosition * model * view * projection;\
    vec4 pos = vPosition * model;\
    position = pos.xyz / pos.w;\
    autoTextureCoord = textureCoord;\
    autoNormalCoord = normalCoord;\
    vec4 lightPos = pos * lightTransform;\
    autoShadowCoord = vec2(lightPos.x / lightPos.w + 1.0, lightPos.y / lightPos.w + 1.0) / 2.0;\
    actualDepth = lightPos.z / lightPos.w;\
}";
const char * const VS_IDENTITY_SPECIAL_LIGHTING_UNIFORMNORMAL =
"attribute vec4 vPosition, normal;\
attribute vec2 textureCoord;\
uniform mat4 model, view, projection, lightTransform;\
varying vec3 position;\
varying vec2 autoTextureCoord, autoShadowCoord;\
varying vec4 autoNormal;\
varying float actualDepth;\
void main () {\
    gl_Position = vPosition * model * view * projection;\
    vec4 pos = vPosition * model;\
    position = pos.xyz / pos.w;\
    autoTextureCoord = textureCoord;\
    autoNormal = normal;\
    vec4 lightPos = pos * lightTransform;\
    autoShadowCoord = vec2(lightPos.x / lightPos.w + 1.0, lightPos.y / lightPos.w + 1.0) / 2.0;\
    actualDepth = lightPos.z / lightPos.w;\
}";
const char * const FS_IDENTITY_SPECIAL_LIGHTING =
"precision mediump float;\
uniform sampler2D texture, normal, shadow;\
struct lightsrc {\
    vec3 source, color;\
    float specular, attenuation, layerFactor, layerCount;\
};\
uniform lightsrc light;\
uniform vec3 camera;\
varying vec2 autoTextureCoord, autoNormalCoord, autoShadowCoord;\
varying vec3 position;\
varying float actualDepth;\
const float shadowDecay = 2.0;\
void main () {\
    vec4 materialColor = texture2D (texture, autoTextureCoord);\
    vec3 normalVec = normalize (texture2D (normal, autoNormalCoord).rgb * 2.0 - 1.0);\
    vec3 observer = camera - position;\
    float observerDistance = length(observer);\
    vec3 normalObserver = normalize (observer);\
    vec3 inbound = normalize (position - light.source);\
    float lightDistance = length (position - light.source);\
    vec3 outbound = inbound - 2.0 * dot (inbound, normalVec) * normalVec;\
    float atn = max(0.0, dot(normalVec, -inbound)) / (1.0 + light.attenuation * pow (observerDistance + lightDistance, 2.0));\
    vec3 lightColor = light.color * atn * (1.0 + pow (dot (normalObserver, outbound), light.specular));\
    vec3 finalColor = lightColor * materialColor.rgb;\
    vec3 shadowValue = texture2D (shadow, autoShadowCoord).xyz;\
    float depth = (shadowValue.x + shadowValue.y + shadowValue.z / 256.0) * 2.0 - 1.0;\
    float shadowFactor = exp(shadowDecay * min(depth - actualDepth, 0.0));\
    gl_FragColor = vec4(light.layerCount * shadowFactor * finalColor, light.layerFactor);\
}";
const char * const FS_IDENTITY_SPECIAL_LIGHTING_UNIFORMNORMAL =
"precision mediump float;\
uniform sampler2D texture, shadow;\
struct lightsrc {\
    vec3 source, color;\
    float specular, attenuation, layerFactor, layerCount;\
};\
uniform lightsrc light;\
uniform vec3 camera;\
varying vec2 autoTextureCoord, autoShadowCoord;\
varying vec3 position;\
varying vec4 autoNormal;\
varying float actualDepth;\
const float shadowDecay = 2.0;\
void main () {\
    vec4 materialColor = texture2D (texture, autoTextureCoord);\
    vec3 normalVec = normalize (autoNormal.xyz);\
    vec3 observer = camera - position;\
    float observerDistance = length(observer);\
    vec3 normalObserver = normalize (observer);\
    vec3 inbound = normalize (position - light.source);\
    float lightDistance = length (position - light.source);\
    vec3 outbound = inbound - 2.0 * dot (inbound, normalVec) * normalVec;\
    float atn = max(0.0, dot(normalVec, -inbound)) / (1.0 + light.attenuation * pow (observerDistance + lightDistance, 2.0));\
    vec3 lightColor = light.color * atn * (1.0 + pow (dot (normalObserver, outbound), light.specular));\
    vec3 finalColor = lightColor * materialColor.rgb;\
    vec3 shadowValue = texture2D (shadow, autoShadowCoord).xyz;\
    float depth = (shadowValue.x + shadowValue.y + shadowValue.z / 256.0) * 2.0 - 1.0;\
    float shadowFactor = exp(shadowDecay * min(depth - actualDepth, 0.0));\
    gl_FragColor = vec4(light.layerCount * shadowFactor * finalColor, light.layerFactor);\
}";
const char * const VS_IDENTITY_SPECIAL_SHADOW =
"attribute vec4 vPosition;\
uniform mat4 model, view, projection;\
varying vec4 pos;\
void main () {\
    pos = vPosition * model * view * projection;\
    gl_Position = vPosition * model * view * projection;\
}";
const char * const FS_IDENTITY_SPECIAL_SHADOW =
"precision mediump float;\
varying vec4 pos;\
void main () {\
    float z = max(0.0, (pos.z / pos.w + 1.0) / 2.0);\
    vec4 c = vec4(min(1.0, floor(z)), fract(floor(z * 256.0) / 256.0), fract(z * 256.0), 1.0);\
    gl_FragColor = c;\
}";
