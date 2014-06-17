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
class ResourceManager {
	std::map<std::string, int> textureHandlers, modelHandlers;
public:
	class Model {
		Model();
	public:
		void attachTexture();
		void testDraw();
	};
	explicit ResourceManager(const char * const path = "/sdcard/dotagrid/default.zip");
	virtual ~ResourceManager();
};

#endif /* RESOURCEMANAGER_H_ */
