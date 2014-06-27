/*
 * ResourceManager.cpp
 *
 *  Created on: 16 Jun, 2014
 *      Author: apple
 */
#include "ResourceManager.h"
#include <cstdlib>
#include <android/log.h>
#include <sstream>
#include <exception>
#include <GLES2/gl2.h>

void readPNG(zip_file *zf, const int textureHandler, unsigned int &textureWidth, unsigned int &textureHeight) {
	png_byte header[8];
	// read header
	zip_fread(zf, header, 8);
	if (png_sig_cmp(header, 0, 8)) {
		__android_log_write(ANDROID_LOG_DEBUG, "ResourceManager", "Invalid PNG signature");
		return;
	}
	png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	png_infop info_ptr = png_create_info_struct(png_ptr);
	png_infop end_info_ptr = png_create_info_struct(png_ptr);
	if (setjmp(png_jmpbuf(png_ptr))) {
		__android_log_write(ANDROID_LOG_DEBUG, "ResourceManager", "PNG error");
		return;
	}
	png_set_read_fn(png_ptr, (png_voidp) zf, [] (png_structp pngStruct, png_bytep data, png_size_t length) {
//		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Loading ...%d", length);
		zip_file *zf = static_cast<zip_file*>(png_get_io_ptr(pngStruct));
		zip_fread(zf, data, length);
	});
	// skip
	png_set_sig_bytes(png_ptr, 8);
	// load info
	png_read_info(png_ptr, info_ptr);
	int bit_depth, color_type;
	png_uint_32 width, height;
	png_get_IHDR(png_ptr, info_ptr, &width, &height, &bit_depth, &color_type, NULL, NULL, NULL);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Width %d, Height %d, BitDepth %d", width, height, bit_depth);
	textureWidth = width;
	textureHeight = height;
	// adjust image
	// transparency -> alpha
	if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS))
		png_set_tRNS_to_alpha(png_ptr);
	// grayscale -> 8 bit
	if (color_type == PNG_COLOR_TYPE_GRAY && bit_depth < 8)
		png_set_expand_gray_1_2_4_to_8(png_ptr);
	// palette -> rgb
	if (color_type == PNG_COLOR_TYPE_PALETTE)
		png_set_palette_to_rgb(png_ptr);
	// rgb -> rgba
	if (color_type == PNG_COLOR_TYPE_PALETTE || color_type == PNG_COLOR_TYPE_RGB)
		png_set_add_alpha(png_ptr, 0xff, PNG_FILLER_AFTER);
	// packing
	if (bit_depth < 8)
		png_set_packing(png_ptr);
	else if (bit_depth == 16)
		png_set_scale_16(png_ptr);
	png_read_update_info(png_ptr, info_ptr);
	color_type = png_get_color_type(png_ptr, info_ptr);
	// read
	int row_bytes = png_get_rowbytes(png_ptr, info_ptr);
	png_byte * image = new png_byte[row_bytes * height];
	png_bytepp row_pointers = new png_bytep[height];
	for (int i = 0; i < height; i++)
		row_pointers[i] = image + i * row_bytes;
	png_read_image(png_ptr, row_pointers);
	png_destroy_read_struct(&png_ptr, &info_ptr, &end_info_ptr);
	delete[] row_pointers;
	int textureFormat;
	switch (color_type) {
	case PNG_COLOR_TYPE_GRAY:
		textureFormat = GL_LUMINANCE;
		break;
	case PNG_COLOR_TYPE_RGB_ALPHA:
		textureFormat = GL_RGBA;
		break;
	case PNG_COLOR_TYPE_GRAY_ALPHA:
		textureFormat = GL_LUMINANCE_ALPHA;
	}
	glGetError();
	glBindTexture(GL_TEXTURE_2D, textureHandler);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glBindTexture:%d", glGetError());
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glTexParameteri(min_filter,mipmap_linear):%d", glGetError());
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glTexParameteri(mag_filter,linear):%d", glGetError());
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, textureFormat, GL_UNSIGNED_BYTE, image);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glTexImage2D:%d", glGetError());
	glGenerateMipmap(GL_TEXTURE_2D);
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glGenerateMipmap:%d", glGetError());
	delete[] image;
}

std::string trim (const std::string& str) {
	int i = 0, j = str.length() - 1;
	while (i < j && (str[i] == ' ' || str[i] == '\t'))
		i++;
	while (i < j && (str[j] == ' ' || str[j] == '\t'))
		j--;
	if (i <= j)
		return str.substr(i, j - i + 1);
	else
		return std::string();
}

void readOBJ(zip_file *zf, size_t fileSize, GLuint bufferHandler, unsigned int &size) {
	// read obj file
	float * vAttribute = NULL;
	char * content = new char[fileSize];
	zip_fread(zf, content, fileSize);
	std::string *contentStr = new std::string(content);
	delete[] content;
	std::stringstream *ss = new std::stringstream(*contentStr);
	std::vector<std::vector<float>> *vertexPosition = new std::vector<std::vector<float>>(),
		*texturePosition = new std::vector<std::vector<float>>(),
		*normalVector = new std::vector<std::vector<float>>();
	std::vector<std::vector<int>> *faces = new std::vector<std::vector<int>>();
	delete contentStr;
	size_t offset = 0;
	try {
		while (!ss->eof()) {
			std::string str;
			char c;
			float x, y, z, w;
			std::vector<float> v;
			*ss >> str;
			if (str.size() == 0)
				continue;
			switch (str[0]) {
			case 'v':
				if (str.length() == 1) {
					// vertex
					*ss >> x >> y >> z;
					// optional w
//					do {*ss >> c;} while (c == ' ' || c == '\t');
//					if ((c >= '0' && c <= '9') || c == '.') {
//						ss->putback(c);
//						*ss >> w;
//					} else {
//						w = 1;
//						if (c == '\n' || c == '\r')
//							ss->putback(c);
//						std::getline(*ss, str);
//					}
					v.push_back(x); v.push_back(y); v.push_back(z); v.push_back(1);
					vertexPosition->push_back(v);
				} else if (str[1] == 't') {
					// texture, accept x and y only
					*ss >> x >> y;
					v.push_back(x); v.push_back(y);
					texturePosition->push_back(v);
				} else if (str[1] == 'n') {
					// normal
					*ss >> x >> y >> z;
					v.push_back(x); v.push_back(y); v.push_back(z);
					normalVector->push_back(v);
				} else {
					std::getline(*ss, str);
					continue;
				}
				break;
			case 'f': {
				std::vector<int> f;
				// accept triangles only
				for (int i = 0; i < 3; i++) {
					*ss >> str;
					int t = str.find_first_of('/');
					int no = atoi(str.substr(0, t).c_str());
					if (no <= 0)
						throw std::string("Invalid format");
					f.push_back(no);
					str = str.substr(t + 1, str.length() - t - 1);
					t = str.find_first_of('/');
					no = atoi(str.substr(0, t).c_str());
					if (no <= 0)
						throw std::string("Invalid format");
					f.push_back(no);
					str = str.substr(t + 1, str.length() - t - 1);
					no = atoi(str.c_str());
					if (no <= 0)
						throw std::string("Invalid format");
					f.push_back(no);
				}
				faces->push_back(f);
				break;
			}
			default:
				std::getline(*ss, str);
			}
		}
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Vertex no=%d Texture no=%d Normal no=%d Face no=%d",
				vertexPosition->size(),
				texturePosition->size(),
				normalVector->size(),
				faces->size());
		vAttribute = new float[faces->size() * 30];
		for (int i = 0; i < faces->size(); i++)
			for (int j = 0; j < 3; j++) {
				// vertex
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j * 3] - 1)[0];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j * 3] - 1)[1];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j * 3] - 1)[2];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j * 3] - 1)[3];
				// texture
				vAttribute[offset++] = texturePosition->at(faces->at(i)[j * 3 + 1] - 1)[0];
				vAttribute[offset++] = texturePosition->at(faces->at(i)[j * 3 + 1] - 1)[1];
				// normal
				vAttribute[offset++] = normalVector->at(faces->at(i)[j * 3 + 2] - 1)[0];
				vAttribute[offset++] = normalVector->at(faces->at(i)[j * 3 + 2] - 1)[1];
				vAttribute[offset++] = normalVector->at(faces->at(i)[j * 3 + 2] - 1)[2];
				vAttribute[offset++] = 0;
			}
		size = faces->size() * 3;	// every face has three vertices, of cause
		glGetError();
		glBindBuffer(GL_ARRAY_BUFFER, bufferHandler);
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glBindBuffer %d", glGetError());
		glBufferData(GL_ARRAY_BUFFER, sizeof(float) * faces->size() * 30, vAttribute, GL_STATIC_DRAW);
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "glBufferData %d", glGetError());
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		delete[] vAttribute;
	} catch (std::string& e) {
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "readOBJ Exception: %s", e.c_str());
		if (vAttribute)
			delete[] vAttribute;
	} catch (std::exception& e) {
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "readOBJ Exception: %s", e.what());
		if (vAttribute)
			delete[] vAttribute;
	}
	delete vertexPosition, texturePosition, normalVector, faces;
}

ResourceManager::ResourceManager(const char * const pathToPkg) : useExtensionEngine(false) {
	__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "read from package located @ %s", pathToPkg);
	int err = 0;
	zip *z = zip_open(pathToPkg, 0, &err);
	if (err) {
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Package failed to open");
		throw "Package not found";
	}
	char const * file;
	char * content;
	std::string contentStr;
	std::stringstream *ss;
	struct zip_stat st;
	zip_stat_init(&st);
	zip_file *zf;
	// index
	// terrain data and map data
	// script
	file = "scriptIndex";
	zip_stat(z, file, 0, &st);
	content = new char[st.size + 1];
	zf = zip_fopen(z, file, 0);
	if (zf) {
		zip_fread(zf, content, st.size);
		zip_fclose(zf);
		content[st.size] = 0;
		contentStr = std::string(content);
		delete[] content;
		ss = new std::stringstream(contentStr);
		while (!ss->eof()) {
			std::string scriptFile;
			*ss >> scriptFile;
			__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load script %s", scriptFile.c_str());
			if (scriptFile.size() > 0) {
				zip_stat(z, scriptFile.c_str(), 0, &st);
				zf = zip_fopen(z, scriptFile.c_str(), 0);
				if (zf) {
					content = new char[st.size + 1];
					zip_fread(zf, content, st.size);
					zip_fclose(zf);
					scripts.push_back(std::unique_ptr<char[]>(content));
					useExtensionEngine = true;
				} else
					__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load %s failed", scriptFile.c_str());
			}
		}
		delete ss;
	} else
		delete[] content;
	// model
	file = "modelIndex";
	zip_stat(z, file, 0, &st);
	content = new char[st.size + 1];
	zf = zip_fopen(z, file, 0);
	if (zf) {
		zip_fread(zf, content, st.size);
		zip_fclose(zf);
		content[st.size] = 0;
		contentStr = std::string(content);
		ss = new std::stringstream(contentStr);
		while (!ss->eof()) {
			std::string modelName, modelFile;
			*ss >> modelName >> modelFile;
			__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load model %s from %s", modelName.c_str(), modelFile.c_str());
			if (modelName.size() > 0 && modelFile.size() > 0) {
				zip_stat(z, modelFile.c_str(), 0, &st);
				zf = zip_fopen(z, modelFile.c_str(), 0);
				if (zf) {
					GLuint buf[1];
					glGenBuffers(1, buf);
					modelHandlers[modelName] = buf[0];
					readOBJ(zf, st.size, modelHandlers[modelName], modelSizes[modelName]);
					zip_fclose(zf);
				} else
					__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load %s failed", modelName.c_str());
			}
		}
		delete ss;
	}
	delete[] content;
	// texture index
	file = "textureIndex";
	zip_stat(z, file, 0, &st);
	content = new char[st.size + 1];
	zf = zip_fopen(z, file, 0);
	if (zf) {
		zip_fread(zf, content, st.size);
		zip_fclose(zf);
		content[st.size] = 0;	// null terminate
		contentStr = std::string(content);
		ss = new std::stringstream(contentStr);
		while (!ss->eof()) {
			std::string textureName, textureFile;
			*ss >> textureName >> textureFile;
			__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load texture %s from %s", textureName.c_str(), textureFile.c_str());
			if (textureName.size() > 0 && textureFile.size() > 0) {
				zf = zip_fopen(z, textureFile.c_str(), 0);
				if (zf) {
					GLuint tex[1];
					glGenTextures(1, tex);
					textureHandlers[textureName] = tex[0];
					__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Texture GLuint: %d", textureHandlers[textureName]);
					readPNG(zf, textureHandlers[textureName], textureWidths[textureName], textureHeights[textureName]);
					zip_fclose(zf);
				} else
					__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "Load %s failed", textureFile.c_str());
			}
		}
		delete ss;
	}
	delete[] content;
	// close zip
	zip_close(z);
}

ResourceManager::~ResourceManager() {
	GLuint *buf = new GLuint[textureHandlers.size()];
	int c = 0;
	for (const auto &itr : textureHandlers)
		buf[c++] = itr.second;
	glDeleteTextures(c, buf);
	delete[] buf;
	buf = new GLuint[modelHandlers.size()];
	c = 0;
	for (const auto &itr : modelHandlers)
		buf[c++] = itr.second;
	glDeleteBuffers(c, buf);
	delete[] buf;
}

GLuint ResourceManager::getTextureHandler(const std::string &name) {
	const auto &itr = textureHandlers.find(name);
	if (itr != textureHandlers.end())
		return itr->second;
	else
		return 0;
}

unsigned int ResourceManager::getTextureWidth(const std::string &name) {
	const auto &itr = textureWidths.find(name);
	if (itr != textureWidths.end())
		return itr->second;
	else
		return 0;
}

unsigned int ResourceManager::getTextureHeight(const std::string &name) {
	const auto &itr = textureHeights.find(name);
	if (itr != textureHeights.end())
		return itr->second;
	else
		return 0;
}

GLuint ResourceManager::getModelHandler(const std::string &name) {
	const auto &itr = modelHandlers.find(name);
	if (itr != modelHandlers.end())
		return itr->second;
	else
		return 0;
}

unsigned int ResourceManager::getModelSize(const std::string &name) {
	const auto &itr = modelSizes.find(name);
	if (itr != modelSizes.end())
		return itr->second;
	else
		return 0;
}

std::string ResourceManager::getAllScript() {
	std::string ret;
	for (auto& script_ptr : scripts)
		ret += script_ptr.get();
	return ret;
}

bool ResourceManager::isExtensionEnabled() {
	return useExtensionEngine;
}
