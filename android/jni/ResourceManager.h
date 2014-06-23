/*
 * ResourceManager.h
 *
 *  Created on: 16 Jun, 2014
 *      Author: apple
 */

#ifndef RESOURCEMANAGER_H_
#define RESOURCEMANAGER_H_
#include <zip.h>
#include <png.h>
#include <map>
#include <string>
#include <GLES2/gl2.h>
class ResourceManager {
	std::map<std::string, GLuint> textureHandlers, modelHandlers;
	std::map<std::string, unsigned int> modelSizes;
public:
	explicit ResourceManager(const char * const path = "/sdcard/dotagrid/default.zip");
	virtual ~ResourceManager();
	GLuint getTextureHandler(const std::string&);
	GLuint getModelHandler(const std::string&);
	GLuint getModelSize(const std::string&);
};

#endif /* RESOURCEMANAGER_H_ */
