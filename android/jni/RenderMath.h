/*
 * RenderMath.h
 *
 *  Created on: 19 Jul, 2014
 *      Author: dingxiangfei
 */

#ifndef RENDERMATH_H_
#define RENDERMATH_H_
#include <vector>
#include <android/log.h>
inline std::vector<float> FlatMatrix4x4Multiplication(const std::vector<float> &a, const std::vector<float> &b) {
	return std::vector<float>({
		a[0]*b[0] + a[1]*b[4] + a[2]*b[8] + a[3]*b[12],
		a[0]*b[1] + a[1]*b[5] + a[2]*b[9] + a[3]*b[13],
		a[0]*b[2] + a[1]*b[6] + a[2]*b[10] + a[3]*b[14],
		a[0]*b[3] + a[1]*b[7] + a[2]*b[11] + a[3]*b[15],
		a[4]*b[0] + a[5]*b[4] + a[6]*b[8] + a[7]*b[12],
		a[4]*b[1] + a[5]*b[5] + a[6]*b[9] + a[7]*b[13],
		a[4]*b[2] + a[5]*b[6] + a[6]*b[10] + a[7]*b[14],
		a[4]*b[3] + a[5]*b[7] + a[6]*b[11] + a[7]*b[15],
		a[8]*b[0] + a[9]*b[4] + a[10]*b[8] + a[11]*b[12],
		a[8]*b[1] + a[9]*b[5] + a[10]*b[9] + a[11]*b[13],
		a[8]*b[2] + a[9]*b[6] + a[10]*b[10] + a[11]*b[14],
		a[8]*b[3] + a[9]*b[7] + a[10]*b[11] + a[11]*b[15],
		a[12]*b[0] + a[13]*b[4] + a[14]*b[8] + a[15]*b[12],
		a[12]*b[1] + a[13]*b[5] + a[14]*b[9] + a[15]*b[13],
		a[12]*b[2] + a[13]*b[6] + a[14]*b[10] + a[15]*b[14],
		a[12]*b[3] + a[13]*b[7] + a[14]*b[11] + a[15]*b[15]
	});
}

template <class ... Args>
inline std::vector<float> FlatMatrix4x4Multiplication(const std::vector<float> &a, const std::vector<float> &b, Args ... tail) {
	return FlatMatrix4x4Multiplication(a, FlatMatrix4x4Multiplication(b, tail...));
}

inline std::vector<float> FlatMatrix4x4Vector4Multiplication(const std::vector<float> &a, const std::vector<float> &b) {
	return std::vector<float>({
		a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3],
		a[4] * b[0] + a[5] * b[1] + a[6] * b[2] + a[7] * b[3],
		a[8] * b[0] + a[9] * b[1] + a[10] * b[2] + a[11] * b[3],
		a[12] * b[0] + a[13] * b[1] + a[14] * b[2] + a[15] * b[3]
	});
}

inline std::vector<float> FlatTranslationMatrix4x4 (float x, float y, float z) {
	return std::vector<float>({1,0,0,x,0,1,0,y,0,0,1,z,0,0,0,1});
}

#endif /* RENDERMATH_H_ */
