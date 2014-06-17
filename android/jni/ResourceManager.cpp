/*
 * ResourceManager.cpp
 *
 *  Created on: 16 Jun, 2014
 *      Author: apple
 */
#include <cstdlib>
#include <android/log.h>
#include "ResourceManager.h"
#include <sstream>
#include <vector>
#include <exception>
#include <GLES2/gl2.h>

void readPNG(zip_file *zf, int textureHandler) {
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
	glBindTexture(GL_TEXTURE_2D, textureHandler);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, textureFormat, GL_UNSIGNED_BYTE, image);
	glGenerateMipmap(GL_TEXTURE_2D);
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
size_t readOBJ(zip_file *zf, size_t fileSize, float *&vAttribute) {
	// read obj file
	if (vAttribute)
		return 0;
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
			str = trim(str);
			switch (str[0]) {
			case 'v':
				if (str.length() == 1) {
					// vertex
					*ss >> x >> y >> z;
					// optional w
					do {*ss >> c;} while (c == ' ' || c == '\t');
					if ((c >= '0' && c <= '9') || c == '.') {
						ss->putback(c);
						*ss >> w;
					} else {
						w = 1;
						if (c == '\n' || c == '\r')
							ss->putback(c);
						std::getline(*ss, str);
					}
					v.push_back(x); v.push_back(y); v.push_back(z); v.push_back(w);
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
						throw "Invalid format";
					f.push_back(no);
					str = str.substr(t + 1, str.length() - t - 1);
					t = str.find_first_of('/');
					no = atoi(str.substr(0, t).c_str());
					if (no <= 0)
						throw "Invalid format";
					f.push_back(no);
					str = str.substr(t + 1, str.length() - t - 1);
					no = atoi(str.c_str());
					if (no <= 0)
						throw "Invalid format";
					f.push_back(no);
				}
				break;
			}
			default:
				std::getline(*ss, str);
			}
		}
		vAttribute = new float[faces->size() * 10];
		for (int i = 0; i < faces->size(); i++)
			for (int j = 0; j < 3; j++) {
				// vertex
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j])[0];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j])[1];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j])[2];
				vAttribute[offset++] = vertexPosition->at(faces->at(i)[j])[3];
				// texture
				vAttribute[offset++] = texturePosition->at(faces->at(i)[j + 1])[0];
				vAttribute[offset++] = texturePosition->at(faces->at(i)[j + 1])[1];
				// normal
				vAttribute[offset++] = normalVector->at(faces->at(i)[j + 2])[0];
				vAttribute[offset++] = normalVector->at(faces->at(i)[j + 2])[1];
				vAttribute[offset++] = normalVector->at(faces->at(i)[j + 2])[2];
				vAttribute[offset++] = 0;
			}
	} catch (std::string& e) {
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "readOBJ: %s", e.c_str());
		if (vAttribute)
			delete[] vAttribute;
	} catch (std::exception& e) {
		__android_log_print(ANDROID_LOG_DEBUG, "ResourceManager", "readOBJ: %s", e.what());
		if (vAttribute)
			delete[] vAttribute;
	}
	delete vertexPosition, texturePosition, normalVector, faces;
	return offset;
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
	std::string textureName;
	int offset = 0;
	zip_stat(z, file, 0, &st);
	zf = zip_fopen(z, file, 0);
	zip_fclose(zf);
	// close zip
	zip_close(z);
}

ResourceManager::~ResourceManager() {
}

