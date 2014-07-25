/*
 * GridRendererGraphicsImpl.cpp
 *
 *  Created on: 18 Jul, 2014
 *      Author: dingxiangfei
 */

#include <GridRendererGraphicsImpl.h>
#include <RenderMath.h>
#include <cmath>
#include <android/log.h>


const float gridBox[] {
	-1, -1, -1, 1,	//0
	-1, 1, -1, 1,	//1
	1, 1, -1, 1,	//2
	1, -1, -1, 1,	//3
	-1, -1, 1, 1,	//4
	-1, 1, 1, 1,	//5
	1, 1, 1, 1,		//6
	1, -1, 1, 1		//7
};
const unsigned short gridBoxIndex[] {
	0, 1, 3, 1, 3, 2,
	3, 2, 6, 6, 3, 7,
	3, 7, 0, 7, 0, 4,
	0, 4, 1, 4, 1, 5,
	1, 5, 2, 5, 2, 6,
	6, 5, 7, 5, 7, 4
};

GLuint makeProgram(const char * vsSrc, const char * fsSrc) {
	GLuint vs = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vs, 1, &vsSrc, NULL);
	glCompileShader(vs);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "%d, vs %d", vs, glGetError());
	GLuint fs = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fs, 1, &fsSrc, NULL);
	glCompileShader(fs);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "%d, fs %d", fs, glGetError());
	GLuint program = glCreateProgram();
	glAttachShader(program, vs);
	glAttachShader(program, fs);
	glLinkProgram(program);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "%d, lnk %d", program, glGetError());
	return program;
}

GridRendererGraphicsImpl::GridRendererGraphicsImpl() : lightObserver({0, 0, 0}), hasSelection(false), hasHighlights(false) {
}

GridRendererGraphicsImpl::~GridRendererGraphicsImpl() {
}

void GridRendererGraphicsImpl::initialise(
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
		float *lightProjection) {
	this->rows = rows;
	this->columns = columns;
	this->resolution = resolution;
	this->mapTexture = mapTexture;
	this->terrainBuffer = terrainBuffer;
	this->terrainNormalMap = terrainNormalMap;
	this->gridBuffer = gridBuffer;
	this->gridIndexBuffer = gridIndexBuffer;
	this->lightProjection.insert(this->lightProjection.begin(), lightProjection, lightProjection + 16);
	selectGridMatrix = {1.0f / columns, 0, 0, 1.0f / columns, 0, 1.0f / rows, 0, 1.0f / rows, 0, 0, 1, 0, 0, 0, 0, 1};
	gridProgram = makeProgram(gridvs, gridfs);
	mapProgram = makeProgram(mapvs, mapfs);
	shadowProgram = makeProgram(shadowvs, shadowfs);
	shadowObjProgram = makeProgram(shadowobjvs, shadowobjfs);

	GLuint buf[1];
	glGenFramebuffers(1, buf);
	frameBuf = buf[0];
	glGenRenderbuffers(1, buf);
	renderBuf = buf[0];
	glBindRenderbuffer(GL_RENDERBUFFER, renderBuf);
	glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, SHADOW_MAP_TEXTURE_DIMENSION, SHADOW_MAP_TEXTURE_DIMENSION);
	glGenBuffers(1, buf);
	gridBoxBuffer = buf[0];
	glGenBuffers(1, buf);
	gridBoxIndexBuffer = buf[0];
	glBindBuffer(GL_ARRAY_BUFFER, gridBoxBuffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof (gridBox), gridBox, GL_STATIC_DRAW);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gridBoxIndexBuffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof (gridBoxIndex), gridBoxIndex, GL_STATIC_DRAW);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
}

void GridRendererGraphicsImpl::setModelMatrix (const float * mat) {
	std::lock_guard<std::mutex> guard(mutex);
	model.insert(model.begin(), mat, mat + 16);
}

void GridRendererGraphicsImpl::setViewMatrix (const float * mat) {
	std::lock_guard<std::mutex> guard(mutex);
	view.insert(view.begin(), mat, mat + 16);
}

void GridRendererGraphicsImpl::setProjectionMatrix (const float * mat) {
	std::lock_guard<std::mutex> guard(mutex);
	projection.insert(projection.begin(), mat, mat + 16);
}

void GridRendererGraphicsImpl::configureShadow(const std::string &name) {
	std::lock_guard<std::mutex> guard(mutex);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "ConfigureShadow Begin");
	if (shadowMaps.find(name) == shadowMaps.end()) {
		// generate texture
		GLuint v[1];
		glGenTextures(1, v);
		shadowMaps[name] = v[0];
		// configure
		glBindTexture(GL_TEXTURE_2D, v[0]);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, SHADOW_MAP_TEXTURE_DIMENSION, SHADOW_MAP_TEXTURE_DIMENSION, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	if (lightSrc.find(name) == lightSrc.end() || lightGridPosition.find(name) == lightGridPosition.end())
		return;
	const std::vector<float> &lightConfig = lightSrc[name];
	const Matrix4x4 &lightView = FlatTranslationMatrix4x4(-lightConfig[0], -lightConfig[1], -lightConfig[2]);
	lightViews[name] = lightView;
	glBindFramebuffer(GL_FRAMEBUFFER, frameBuf);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, shadowMaps[name], 0);
	glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuf);
	glViewport(0, 0, SHADOW_MAP_TEXTURE_DIMENSION, SHADOW_MAP_TEXTURE_DIMENSION);
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glUseProgram(shadowProgram);
	const int
		vPosition = glGetAttribLocation(shadowProgram, "vPosition"),
		mModel = glGetUniformLocation(shadowProgram, "model"),
		mView = glGetUniformLocation(shadowProgram, "view"),
		mProjection = glGetUniformLocation(shadowProgram, "projection");
	glUniformMatrix4fv(mModel, 1, false, &model[0]);
	glUniformMatrix4fv(mView, 1, false, &lightView[0]);
	glUniformMatrix4fv(mProjection, 1, false, &lightProjection[0]);
	glBindBuffer(GL_ARRAY_BUFFER, terrainBuffer);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, sizeof (float) * 8, 0);
	glEnableVertexAttribArray(vPosition);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, rows * resolution * (columns * resolution + 1) * 2);
	glDisableVertexAttribArray(vPosition);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	// drawables
	for (auto entry : drawableModelHandlers)
		if (drawableVisible[entry.first]
		        && drawableGridPositions.find(entry.first) != drawableGridPositions.end()
		        && drawableModels.find(entry.first) != drawableModels.end()
		        && drawableModelSizes.find(entry.first) != drawableModelSizes.end()
				&& pow(lightGridPosition[name][0] - drawableGridPositions[entry.first][0], 2)
						+ pow(lightGridPosition[name][1] - drawableGridPositions[entry.first][1], 2) <= pow(lightGridRange[name], 2)) {
			glUniformMatrix4fv(mModel, 1, false, &FlatMatrix4x4Multiplication(model, drawableModels[entry.first])[0]);
			glBindBuffer(GL_ARRAY_BUFFER, entry.second);
			glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, sizeof(float) * 10, 0);
			glEnableVertexAttribArray(vPosition);
			glDrawArrays(GL_TRIANGLES, 0, drawableModelSizes[entry.first]);
			glDisableVertexAttribArray(vPosition);
		}
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "ConfigureShadow Success");
}

void GridRendererGraphicsImpl::setDrawable(const std::string &name, GLuint textureHandler, GLuint modelHandler, GLuint modelSize, const Matrix4x4 &modelMatrix, const std::vector<int> &pos) {
	mutex.lock();
	drawableModelHandlers[name] = modelHandler;
	drawableModelSizes[name] = modelSize;
	drawableModels[name] = modelMatrix;
	drawableGridPositions[name] = pos;
	drawableTextures[name] = textureHandler;
	mutex.unlock();
}

void GridRendererGraphicsImpl::setDrawableVisible(const std::string &name, bool visible) {
	mutex.lock();
	drawableVisible[name] = visible;
	mutex.unlock();
}

void GridRendererGraphicsImpl::setLight(const std::string &name, const std::vector<float> &lightSrc, bool lightOn, const std::vector<int> &pos, int range) {
	mutex.lock();
	this->lightSrc[name] = lightSrc;
	this->lightOn[name] = lightOn;
	this->lightGridPosition[name] = pos;
	this->lightGridRange[name] = range;
	mutex.unlock();
}

void GridRendererGraphicsImpl::setLightOn(const std::string &name, bool lightOn) {
	mutex.lock();
	this->lightOn[name] = lightOn;
	mutex.unlock();
}

void GridRendererGraphicsImpl::setSelectGrid(bool hasSelectedGrid, const std::vector<int> &pos) {
	mutex.lock();
	hasSelection = hasSelectedGrid;
	if (hasSelectedGrid)
		orgGridIndex = pos;
	mutex.unlock();
}

void GridRendererGraphicsImpl::setHighlightedGrid(bool hasHighlightedGrid, const std::vector<std::vector<int>> &pos) {
	mutex.lock();
	hasHighlights = hasHighlightedGrid;
	if (hasHighlightedGrid)
		highlightedGrids = pos;
	mutex.unlock();
}

void GridRendererGraphicsImpl::draw() {
	std::lock_guard<std::mutex> guard(mutex);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "Draw begin");
	glGetError();
	const Matrix4x4 &matMVP = FlatMatrix4x4Multiplication(projection, view, model);
	std::map<std::string, bool> lightOnScreen;
	for (const auto &entry : lightOn)
		if (entry.second) {
			const Matrix4x4 &centrePoint = FlatMatrix4x4Vector4Multiplication(
					matMVP,
					std::vector<float>({
						(lightGridPosition[entry.first][0] + .5f) * 2 / columns - 1,
						(lightGridPosition[entry.first][1] + .5f) * 2 / rows - 1, 1, 1}));
			lightOnScreen[entry.first] = centrePoint[0] / centrePoint[3] <= 1 && centrePoint[0] / centrePoint[3] >= -1 &&
					centrePoint[1] / centrePoint[3] <= 1 && centrePoint[1] / centrePoint[3] >= -1 &&
					centrePoint[2] / centrePoint[3] <= 1 && centrePoint[2] / centrePoint[3] >= -1;
		} else
			lightOnScreen[entry.first] = false;
	// map
	int vPosition = glGetAttribLocation(mapProgram, "vPosition"),
		mModel = glGetUniformLocation(mapProgram, "model"),
		mView = glGetUniformLocation(mapProgram, "view"),
		mProjection = glGetUniformLocation(mapProgram, "projection"),
		cameraPosition = glGetUniformLocation(mapProgram, "camera"),
		textureLocation = glGetUniformLocation(mapProgram, "texture"),
		normalLocation = glGetUniformLocation(mapProgram, "normal"),
		shadowLocation = glGetUniformLocation(mapProgram, "shadow"),
		textureCoord = glGetAttribLocation(mapProgram, "textureCoord"),
		normalCoord = glGetAttribLocation(mapProgram, "normalCoord"),
		source = glGetUniformLocation(mapProgram, "light.source"),
		color = glGetUniformLocation(mapProgram, "light.color"),
		specular = glGetUniformLocation(mapProgram, "light.specular"),
		attenuation = glGetUniformLocation(mapProgram, "light.attenuation"),
		sight = glGetUniformLocation(mapProgram, "light.sight"),
		mLight = glGetUniformLocation(mapProgram, "lightTransform");
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	bool firstTime = true;
	glBlendFunc(GL_ONE, GL_ONE);
	glUseProgram(mapProgram);
	glUniformMatrix4fv(mModel, 1, false, &model[0]);
	glUniformMatrix4fv(mView, 1, false, &view[0]);
	glUniformMatrix4fv(mProjection, 1, false, &projection[0]);
	// texture and normal
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, terrainNormalMap);
	glUniform1i(normalLocation, 0);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, mapTexture);
	glUniform1i(textureLocation, 1);	// changed from 1
	glUniform1i(shadowLocation, 2);
	// vertex attributes
	glBindBuffer(GL_ARRAY_BUFFER, terrainBuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, sizeof (float) * 8, 0);
	glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, sizeof (float) * 8, (void*) (4 * sizeof (float)));
	glVertexAttribPointer(normalCoord, 2, GL_FLOAT, false, sizeof (float) * 8, (void*) (6 * sizeof (float)));
	glEnableVertexAttribArray(vPosition);
	glEnableVertexAttribArray(textureCoord);
	glEnableVertexAttribArray(normalCoord);
	for (const auto &entry : lightSrc)
		if (lightOn[entry.first] && lightOnScreen[entry.first] && lightViews.find(entry.first) != lightViews.end()) {
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, shadowMaps[entry.first]);
			__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "%s", entry.first.c_str());
			const std::vector<float> &config = entry.second;
			glUniformMatrix4fv(mLight, 1, false, &FlatMatrix4x4Multiplication(lightProjection, lightViews[entry.first])[0]);
			glUniform3f(cameraPosition, lightObserver[0] + config[0], lightObserver[1] + config[1], lightObserver[2] + config[2]);
			glUniform3f(source, config[0], config[1], config[2]);
			glUniform3f(color, config[3], config[4], config[5]);
			glUniform1f(specular, config[6]);
			glUniform1f(attenuation, config[7]);
			glUniform1f(sight, config[8]);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, rows * resolution * (columns * resolution + 1) * 2);
			if (firstTime) {
				glBlendFunc(GL_ONE, GL_ONE);
				firstTime = false;
			}
		}
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "2");
	glDisableVertexAttribArray(vPosition);
	glDisableVertexAttribArray(textureCoord);
	glDisableVertexAttribArray(normalCoord);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	// drawables
	glUseProgram(shadowObjProgram);
	vPosition = glGetAttribLocation(shadowObjProgram, "vPosition");
	mModel = glGetUniformLocation(shadowObjProgram, "model");
	mView = glGetUniformLocation(shadowObjProgram, "view");
	mProjection = glGetUniformLocation(shadowObjProgram, "projection");
	cameraPosition = glGetUniformLocation(shadowObjProgram, "camera");
	textureLocation = glGetUniformLocation(shadowObjProgram, "texture");
	int vNormal = glGetAttribLocation(shadowObjProgram, "normal");
	shadowLocation = glGetUniformLocation(shadowObjProgram, "shadow");
	textureCoord = glGetAttribLocation(shadowObjProgram, "textureCoord");
	source = glGetUniformLocation(shadowObjProgram, "light.source");
	color = glGetUniformLocation(shadowObjProgram, "light.color");
	specular = glGetUniformLocation(shadowObjProgram, "light.specular");
	attenuation = glGetUniformLocation(shadowObjProgram, "light.attenuation");
	sight = glGetUniformLocation(shadowObjProgram, "light.sight");
	mLight = glGetUniformLocation(shadowObjProgram, "lightTransform");
	glUniformMatrix4fv(mView, 1, false, &view[0]);
	glUniformMatrix4fv(mProjection, 1, false, &projection[0]);
	for (const auto &entry : drawableModelHandlers)
		if (drawableVisible[entry.first]
			&& drawableModels.find(entry.first) != drawableModels.end()
			&& drawableGridPositions.find(entry.first) != drawableGridPositions.end()) {
			const Matrix4x4 &centrePoint = FlatMatrix4x4Vector4Multiplication(
					matMVP,
					std::vector<float>({
						(drawableGridPositions[entry.first][0] + .5f) * 2 / columns - 1,
						(drawableGridPositions[entry.first][1] + .5f) * 2 / rows - 1, 1, 1}));
			if (centrePoint[0] / centrePoint[3] > 1 || centrePoint[0] / centrePoint[3] < -1 ||
					centrePoint[1] / centrePoint[3] > 1 || centrePoint[1] / centrePoint[3] < -1 ||
					centrePoint[2] / centrePoint[3] > 1 || centrePoint[2] / centrePoint[3] < -1)
				continue;

			firstTime = true;
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			glUniformMatrix4fv(mModel, 1, false, &FlatMatrix4x4Multiplication(model, drawableModels[entry.first])[0]);
			glBindBuffer(GL_ARRAY_BUFFER, entry.second);
			glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, sizeof(float) * 10, 0);
			glVertexAttribPointer(textureCoord, 2, GL_FLOAT, false, sizeof(float) * 10, (void*) (sizeof(float) * 4));
			glVertexAttribPointer(vNormal, 4, GL_FLOAT, false, sizeof(float) * 10, (void*) (sizeof(float) * 6));
			glEnableVertexAttribArray(vPosition);
			glEnableVertexAttribArray(vNormal);
			glEnableVertexAttribArray(textureCoord);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, drawableTextures[entry.first]);
			glUniform1i(textureLocation, 0);
			glUniform1i(shadowLocation, 1);
			for (const auto &lightEntry : lightSrc)
				if (lightOn[lightEntry.first] && lightOnScreen[lightEntry.first]
				   && lightGridPosition.find(lightEntry.first) != lightGridPosition.end()
				   && lightGridRange.find(lightEntry.first) != lightGridRange.end()
				   && lightViews.find(lightEntry.first) != lightViews.end()
				   && pow(lightGridPosition[lightEntry.first][0] - drawableGridPositions[entry.first][0], 2)
				   + pow(lightGridPosition[lightEntry.first][1] - drawableGridPositions[entry.first][1], 2) <= pow(lightGridRange[lightEntry.first], 2)) {
					glActiveTexture(GL_TEXTURE1);
					glBindTexture(GL_TEXTURE_2D, shadowMaps[lightEntry.first]);
					const std::vector<float> &config = lightEntry.second;
					glUniformMatrix4fv(mLight, 1, false, &FlatMatrix4x4Multiplication(lightProjection, lightViews[lightEntry.first])[0]);
					glUniform3f(cameraPosition, lightObserver[0] + config[0], lightObserver[1] + config[1], lightObserver[2] + config[2]);
					glUniform3f(source, config[0], config[1], config[2]);
					glUniform3f(color, config[3], config[4], config[5]);
					glUniform1f(specular, config[6]);
					glUniform1f(attenuation, config[7]);
					glUniform1f(sight, config[8]);
					glDrawArrays(GL_TRIANGLES, 0, drawableModelSizes[entry.first]);
					if (firstTime) {
						glBlendFunc(GL_ONE, GL_ONE);
						firstTime = false;
					}
				}
			glDisableVertexAttribArray(vPosition);
			glDisableVertexAttribArray(vNormal);
			glDisableVertexAttribArray(textureCoord);
		}
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "3");
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	// grid program
	vPosition = glGetAttribLocation(gridProgram, "vPosition");
	mModel = glGetUniformLocation(gridProgram, "model");
	mView = glGetUniformLocation(gridProgram, "view");
	mProjection = glGetUniformLocation(gridProgram, "projection");
	int vColor = glGetUniformLocation(gridProgram, "vColor");
	glUseProgram(gridProgram);
	glUniformMatrix4fv(mModel, 1, false, &model[0]);
	glUniformMatrix4fv(mView, 1, false, &view[0]);
	glUniformMatrix4fv(mProjection, 1, false, &projection[0]);
	glUniform4f(vColor, 1, 0, 0, 1);
	glBindBuffer(GL_ARRAY_BUFFER, gridBuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gridIndexBuffer);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, 0);
	glEnableVertexAttribArray(vPosition);
	glDrawElements(GL_LINES, 2 * (columns + rows + 2), GL_UNSIGNED_SHORT, 0);
	glDisableVertexAttribArray(vPosition);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


	Matrix4x4 mat;
	vPosition = glGetAttribLocation(gridProgram, "vPosition");
	mModel = glGetUniformLocation(gridProgram, "model"),
	mView = glGetUniformLocation(gridProgram, "view"),
	mProjection = glGetUniformLocation(gridProgram, "projection"),
	vColor = glGetUniformLocation(gridProgram, "vColor");
	glUseProgram(gridProgram);
	glUniformMatrix4fv(mView, 1, false, &view[0]);
	glUniformMatrix4fv(mProjection, 1, false, &projection[0]);
	glBindBuffer(GL_ARRAY_BUFFER, gridBoxBuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gridBoxIndexBuffer);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, false, 0, 0);
	glEnableVertexAttribArray(vPosition);
	if (hasSelection) {
		mat = FlatMatrix4x4Multiplication(
				model,
				FlatTranslationMatrix4x4(2.0f/columns * orgGridIndex[0]-1, 2.0f/rows * orgGridIndex[1]-1, 0),
				selectGridMatrix);
		glUniformMatrix4fv(mModel, 1, false, &mat[0]);
		glUniform4f(vColor, 1, 0, 0, .2f);
		glDrawElements(GL_TRIANGLES, sizeof(gridBoxIndex) / sizeof(unsigned short), GL_UNSIGNED_SHORT, 0);
	}
	if (hasHighlights) {
		glUniform4f(vColor, 0, 0, 1, .2f);
		for (const auto& idx : highlightedGrids) {
			mat = FlatMatrix4x4Multiplication(
					model,
					FlatTranslationMatrix4x4(2.0f/columns * idx[0]-1, 2.0f/rows * idx[1]-1, 0),
					selectGridMatrix);
			glUniformMatrix4fv(mModel, 1, false, &mat[0]);
			glDrawElements(GL_TRIANGLES, sizeof(gridBoxIndex) / sizeof(unsigned short), GL_UNSIGNED_SHORT, 0);
		}
	}
	glDisableVertexAttribArray(vPosition);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "Draw success");
}


void GridRendererGraphicsImpl::clearDrawable() {
	std::lock_guard<std::mutex> guard(mutex);
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "Clear begin");
	drawableGridPositions.clear();
	drawableModelHandlers.clear();
	drawableModelSizes.clear();
	drawableTextures.clear();
	drawableVisible.clear();
	lightGridPosition.clear();
	lightGridRange.clear();
	lightObserver.clear();
	lightOn.clear();
	lightSrc.clear();
	lightViews.clear();
	__android_log_print(ANDROID_LOG_DEBUG, "Graphics", "Clear success");
}

void GridRendererGraphicsImpl::close() {
}
