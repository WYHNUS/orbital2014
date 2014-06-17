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
class ResourceManager {
	static void png_read_zip_image(png_structp, png_bytep, png_size_t);
public:
	class Model {
		Model();
	public:
		void attachTexture();
	};
	explicit ResourceManager(const char * const path = "/sdcard/dotagrid/default.zip");
	virtual ~ResourceManager();
};

#endif /* RESOURCEMANAGER_H_ */
