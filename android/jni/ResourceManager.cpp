/*
 * ResourceManager.cpp
 *
 *  Created on: 16 Jun, 2014
 *      Author: apple
 */
#include <android/log.h>
#include "ResourceManager.h"

// accesory method
void ResourceManager::png_read_zip_image(png_structp png, png_bytep data, png_size_t size) {

}

ResourceManager::ResourceManager(const char * const pathToPkg) {
	int err = 0;
	zip *z = zip_open(pathToPkg, 0, &err);
	if (err) {
		throw "Package not found";
	}
	// index
	const char * const file = "modelIndex";
	struct zip_stat st;
	zip_stat_init(&st);
	zip_stat(z, file, 0, &st);
	zip_file *zf = zip_fopen(z, file, 0);
	char * content = new char[st.size];
	zip_fread(zf, content, st.size);
	zip_fclose(zf);
	// terrain data and map data
	// model
	// texture
	zip_stat(z, file, 0, &st);
	zf = zip_fopen(z, file, 0);
	png_byte header[8];
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
	png_set_read_fn(png_ptr, NULL, NULL);	// TODO
	png_set_sig_bytes(png_ptr, 8);
	png_read_info(png_ptr, info_ptr);
	int bit_depth, color_type;
	png_uint_32 width, height;
	png_get_IHDR(png_ptr, info_ptr, &width, &height, &bit_depth, &color_type, NULL, NULL, NULL);
	png_read_update_info(png_ptr, info_ptr);
	int row_bytes = png_get_rowbytes(png_ptr, info_ptr);
	png_byte * image = new png_byte[row_bytes * height];
	png_bytepp row_pointers = new png_bytep[height];
	for (int i = 0; i < height; i++)
		row_pointers[i] = image + i * row_bytes;
	png_read_image(png_ptr, row_pointers);
	png_destroy_read_struct(&png_ptr, &info_ptr, &end_info_ptr);
	zip_fclose(zf);
	// close zip
	zip_close(z);
}

ResourceManager::~ResourceManager() {
}

