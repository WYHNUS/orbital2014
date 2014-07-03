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
#include <vector>
#include <string>
#include <GLES2/gl2.h>
class ResourceManager {
	std::map<std::string, GLuint> textureHandlers, modelHandlers;
	std::map<std::string, unsigned int> modelSizes, textureWidths, textureHeights;
	std::vector<std::string> scripts;
	bool useExtensionEngine;
public:
	explicit ResourceManager(const char * const path = "/sdcard/dotagrid/default.zip");
	virtual ~ResourceManager();
	GLuint getTextureHandler(const std::string&);
	GLuint getModelHandler(const std::string&);
	GLuint getModelSize(const std::string&);
	unsigned int getTextureWidth(const std::string&);
	unsigned int getTextureHeight(const std::string&);
	std::string getAllScript();
	bool isExtensionEnabled();
};

#endif /* RESOURCEMANAGER_H_ */
