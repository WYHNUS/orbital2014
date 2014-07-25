/*
 * GridRendererGraphicsImpl.h
 *
 *  Created on: 18 Jul, 2014
 *      Author: dingxiangfei
 */

#ifndef GRIDRENDERERGRAPHICSIMPL_H_
#define GRIDRENDERERGRAPHICSIMPL_H_
#include <vector>
#include <map>
#include <string>
#include <GLES2/gl2.h>
#include <mutex>

enum {
	SHADOW_MAP_TEXTURE_DIMENSION = 1024
};

class GridRendererGraphicsImpl {
	std::mutex mutex;
	typedef std::vector<float> Matrix4x4;

	int rows, columns, resolution;
	GLuint gridProgram, mapProgram, shadowProgram, shadowObjProgram;
	GLuint gridBuffer, gridIndexBuffer, gridBoxBuffer, gridBoxIndexBuffer, mapTexture, terrainNormalMap, terrainBuffer, frameBuf, renderBuf;
	Matrix4x4 model, view, projection, selectGridMatrix, lightProjection;
	std::vector<float> lightObserver;
	std::map<std::string, Matrix4x4> drawableModels, lightSrc, lightViews;
	std::map<std::string, GLuint> drawableModelHandlers, drawableModelSizes, drawableTextures, shadowMaps;
	std::map<std::string, std::vector<int>> drawableGridPositions, lightGridPosition;
	std::map<std::string, int> lightGridRange;
	std::map<std::string, bool> drawableVisible, lightOn;
	bool hasSelection, hasHighlights;
	std::vector<std::vector<int>> highlightedGrids;
	std::vector<int> orgGridIndex;
public:
	GridRendererGraphicsImpl();
	virtual ~GridRendererGraphicsImpl();
	void initialise(
			int columns,
			int rows,
			const char * gridvs,
			const char * gridfs,
			const char * mapvs,
			const char * mapfs,
			const char * shadowvs,
			const char * shadowfs,
			const char * shadowobjvs,
			const char * shadowobjfs,
			int resolution,
			GLuint gridBuffer,
			GLuint gridIndexBuffer,
			GLuint mapTexture,
			GLuint terrainBuffer,
			GLuint terrainNormalMap,
			float *lightProjection);
	void clearDrawable();
	void setDrawable(const std::string &name, GLuint textureHandler, GLuint modelHandler, GLuint modelSize, const Matrix4x4 &modelMatrix, const std::vector<int> &pos);
	void setDrawableVisible(const std::string &name, bool visible);

	void setModelMatrix(const float *mat);
	void setViewMatrix(const float *mat);
	void setProjectionMatrix(const float *mat);

	void setLight(const std::string &name, const std::vector<float> &lightSrc, bool lightOn, const std::vector<int> &pos, int range);
	void setLightOn(const std::string &name, bool lightOn);

	void setSelectGrid(bool hasSelectedGrid, const std::vector<int> &pos);
	void setHighlightedGrid(bool hasHighlightedGrid, const std::vector<std::vector<int>> &pos);

	void configureShadow(const std::string &name);
	void draw();

	void close();
};

#endif /* GRIDRENDERERGRAPHICSIMPL_H_ */
