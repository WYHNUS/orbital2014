package edu.nus.comp.dotagridandroid.math;

public class RenderMaths {
	public static final float PI = (float) Math.PI;
	public static final float ERROR = 1e-7f;
	
	public static float[] IdentityMatrix4x4 () {
		return new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	}
	public static float[] FlatMatrix4x4Multiplication (float[] a, float[] b) {
		if (a.length != 16 || b.length != 16)
			throw new RuntimeException ("Wrong matrix size");
		return new float[] {
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
				a[12]*b[3] + a[13]*b[7] + a[14]*b[11] + a[15]*b[15],
		};
	}
	public static float[] FlatMatrix4x4Multiplication (float[] a, float[] b, float[] ...fs) {
		final int len = fs.length;
		float[] result = fs[len - 1];
		for (int i = len - 2; i >= 0; i--)
			result = FlatMatrix4x4Multiplication(fs[i], result);
		return FlatMatrix4x4Multiplication(a, FlatMatrix4x4Multiplication(b, result));
	}
	public static float[] FlatMatrix4x4ScalarMultiplication (float a, float[] b) {
		if (b.length != 16)
			throw new RuntimeException ("Wrong matrix size");
		return new float[] {
				a*b[0], a*b[1], a*b[2], a*b[3],
				a*b[4], a*b[5], a*b[6], a*b[7],
				a*b[8], a*b[9], a*b[10], a*b[11],
				a*b[12], a*b[13], a*b[14], a*b[15]
		};
	}
	public static float[] FlatMatrix4x4Vector4Multiplication(float[] a, float[] b) {
		if (a.length != 16 || b.length != 4)
			throw new RuntimeException ("Wrong sizes");
		return new float[] {
				a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3],
				a[4] * b[0] + a[5] * b[1] + a[6] * b[2] + a[7] * b[3],
				a[8] * b[0] + a[9] * b[1] + a[10] * b[2] + a[11] * b[3],
				a[12] * b[0] + a[13] * b[1] + a[14] * b[2] + a[15] * b[3],
		};
	}
	public static float Vector4InnerProduct (float[] a, float[] b) {
		if (a.length != 4 || b.length != 4)
			throw new RuntimeException ("Wrong vector size");
		return a[0]*b[0] + a[1]*b[1] + a[2]*b[2] + a[3]*b[3];
	}
	public static float[] FlatRotationMatrix4x4 (float angleRadians, float x, float y, float z) {
		final float len = (float) Math.abs(Math.sqrt(x*x + y*y + z*z));
		if (len < ERROR)
			return IdentityMatrix4x4();
		final float sin_theta = (float) Math.sin(angleRadians), one_minus_cos_theta = (float) (1 - Math.cos(angleRadians));
		final float nx = x / len, ny = y / len, nz = z / len;
		final float nx2 = nx*nx, ny2 = ny*ny, nz2 = nz*nz;
		return new float[] {
				1 - one_minus_cos_theta * (ny2 + nz2), -nz * sin_theta + one_minus_cos_theta * nx * ny, ny * sin_theta + one_minus_cos_theta * nx * nz, 0,
				nz * sin_theta + one_minus_cos_theta * nx * ny, 1 - one_minus_cos_theta * (nx2 + nz2), -nx * sin_theta + one_minus_cos_theta * ny * nz, 0,
				-ny * sin_theta + one_minus_cos_theta * nx * nz, nx * sin_theta + one_minus_cos_theta * ny * nz, 1 - one_minus_cos_theta * (nx2 + ny2), 0,
				0, 0, 0, 1
		};
	}
	public static float[] FlatTranslationMatrix4x4 (float x, float y, float z) {
		return new float[] {1,0,0,x,0,1,0,y,0,0,1,z,0,0,0,1};
	}
	public static float[] FlatScalingMatrix4x4 (float x, float y, float z) {
		return new float[] {x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1};
	}
	// Note: Projection Matrix Inverts Z Depth
	public static float[] FlatPerspectiveMatrix4x4 (float near, float far, float left, float right, float top, float bottom) {
		return new float[] {
				2 * near / (right - left), 0, (right + left) / (right - left), 0,
				0, 2 * near / (top - bottom), (top + bottom) / (top - bottom), 0,
				0, 0, (near + far) / (near - far), 2 * near * far / (near - far),
				0, 0, -1, 0
		};
	}
	public static float FlatMatrix4x4Determinant (float[] a) {
		if (a.length != 16)
			throw new RuntimeException ("Wrong matrix size");
		return a[0]*a[5]*a[10]*a[15]+a[0]*a[6]*a[11]*a[13]+a[0]*a[7]*a[9]*a[14]-a[0]*a[7]*a[10]*a[13]-a[0]*a[6]*a[9]*a[15]-a[0]*a[5]*a[11]*a[14]
				-a[1]*a[4]*a[10]*a[15]-a[1]*a[6]*a[11]*a[12]-a[1]*a[7]*a[8]*a[14]+a[1]*a[7]*a[10]*a[12]+a[1]*a[6]*a[8]*a[15]+a[1]*a[4]*a[11]*a[14]
				+a[2]*a[4]*a[9]*a[15]+a[2]*a[5]*a[11]*a[12]+a[2]*a[7]*a[8]*a[13]-a[2]*a[7]*a[9]*a[12]-a[2]*a[5]*a[8]*a[15]-a[2]*a[4]*a[11]*a[13]
				-a[3]*a[4]*a[9]*a[14]-a[3]*a[5]*a[10]*a[12]-a[3]*a[6]*a[8]*a[13]+a[3]*a[6]*a[9]*a[12]+a[3]*a[5]*a[8]*a[14]+a[3]*a[4]*a[10]*a[13];
	}
	public static float[] FlatInverseMatrix4x4 (float[] a) {
		// TODO: Check formulae
		final float det = FlatMatrix4x4Determinant (a);
		if (det != 0)
			return new float[] {
				(a[5]*a[10]*a[15]+a[6]*a[11]*a[13]+a[7]*a[9]*a[14]-a[7]*a[10]*a[13]-a[6]*a[9]*a[15]-a[5]*a[11]*a[14])/det,
				(a[3]*a[10]*a[13]+a[2]*a[9]*a[15]+a[1]*a[11]*a[14]-a[1]*a[10]*a[15]-a[2]*a[11]*a[13]-a[3]*a[9]*a[14])/det,
				(a[1]*a[6]*a[15]+a[2]*a[7]*a[13]+a[3]*a[5]*a[14]-a[3]*a[6]*a[13]-a[2]*a[5]*a[15]-a[1]*a[7]*a[13])/det,
				(a[3]*a[6]*a[9]+a[2]*a[5]*a[11]+a[1]*a[7]*a[10]-a[1]*a[6]*a[11]-a[2]*a[7]*a[9]-a[3]*a[5]*a[10])/det,
				(a[7]*a[10]*a[12]+a[6]*a[8]*a[15]+a[4]*a[11]*a[14]-a[4]*a[10]*a[15]-a[6]*a[11]*a[12]-a[7]*a[8]*a[14])/det,
				(a[0]*a[10]*a[15]+a[2]*a[11]*a[12]+a[3]*a[8]*a[14]-a[3]*a[10]*a[12]-a[2]*a[8]*a[15]-a[0]*a[11]*a[14])/det,
				(a[3]*a[6]*a[12]+a[2]*a[4]*a[15]+a[0]*a[7]*a[14]-a[0]*a[6]*a[15]-a[2]*a[7]*a[12]-a[3]*a[4]*a[14])/det,
				(a[0]*a[6]*a[11]+a[2]*a[7]*a[8]+a[3]*a[4]*a[10]-a[3]*a[6]*a[8]-a[2]*a[4]*a[11]-a[0]*a[7]*a[10])/det,
				(a[4]*a[9]*a[15]+a[5]*a[11]*a[12]+a[7]*a[8]*a[13]-a[7]*a[9]*a[12]-a[5]*a[8]*a[15]-a[4]*a[11]*a[13])/det,
				(a[3]*a[9]*a[12]+a[1]*a[8]*a[15]+a[0]*a[11]*a[13]-a[0]*a[9]*a[15]-a[1]*a[11]*a[12]-a[3]*a[8]*a[13])/det,
				(a[0]*a[5]*a[15]+a[1]*a[7]*a[12]+a[3]*a[4]*a[13]-a[3]*a[5]*a[12]-a[1]*a[4]*a[15]-a[0]*a[7]*a[13])/det,
				(a[3]*a[5]*a[8]+a[1]*a[4]*a[11]+a[0]*a[7]*a[9]-a[0]*a[5]*a[11]-a[1]*a[7]*a[8]-a[3]*a[4]*a[9])/det,
				(a[6]*a[9]*a[12]+a[5]*a[8]*a[14]+a[4]*a[10]*a[13]-a[4]*a[9]*a[14]-a[5]*a[10]*a[12]-a[6]*a[8]*a[13])/det,
				(a[0]*a[9]*a[14]+a[1]*a[10]*a[12]+a[2]*a[8]*a[13]-a[2]*a[9]*a[12]-a[1]*a[8]*a[14]-a[0]*a[10]*a[13])/det,
				(a[2]*a[5]*a[12]+a[1]*a[4]*a[14]+a[0]*a[6]*a[13]-a[0]*a[5]*a[14]-a[1]*a[6]*a[12]-a[2]*a[4]*a[13])/det,
				(a[0]*a[5]*a[10]+a[1]*a[6]*a[8]+a[2]*a[4]*a[9]-a[2]*a[5]*a[8]-a[1]*a[4]*a[10]-a[0]*a[6]*a[9])/det
		};
		else
			throw new RuntimeException ("Matrix not invertible");
	}
	public static float[] FlatMatrix4x4Transpose (float[] a) {
		return new float[] {
				a[0], a[4], a[8], a[12],
				a[1], a[5], a[9], a[13],
				a[2], a[6], a[10], a[14],
				a[3], a[7], a[11], a[15]
		};
	}
	public static float FlatMatrix3x3Determinant (float[] a) {
		if (a.length != 9)
			throw new RuntimeException ("Wrong matrix size");
		return a[0]*a[4]*a[8]+a[1]*a[5]*a[6]+a[2]*a[3]*a[7]-a[2]*a[4]*a[6]-a[1]*a[3]*a[8]-a[0]*a[5]*a[7];
	}
	public static float FlatMatrix2x2Determinant (float[] a) {
		if (a.length != 4)
			throw new RuntimeException ("Wrong matrix size");
		return a[0] * a[3] - a[1] * a[2];
	}
	public static float[] Vector3CrossProduct(float[] a, float[] b) {
		if (a.length != 3 || b.length != 3)
			throw new RuntimeException ("Wrong vector size");
		return new float[] {
				a[1] * b[2] - a[2] * b[1],
				a[2] * b[0] - a[0] * b[2],
				a[0] * b[1] - a[1] * b[0]
		};
	}
	public static float[] NormalisedVector3(float[] a) {
		if (a.length != 3)
			throw new RuntimeException("Wrong vector size");
		final float len = (float) Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2]);
		if (Math.abs(len) > ERROR)
			return new float[] {a[0]/len,a[1]/len,a[2]/len};
		else
			return new float[] {0,0,0};
	}
	public static float[] Vector3Addition(float[] a, float[] b, float ka, float kb) {
		if (a.length != 3 || b.length != 3)
			throw new RuntimeException("Wrong vector size");
		return new float[]{a[0]*ka + b[0]*kb,a[1]*ka + b[1]*kb,a[2]*ka+b[2]*kb};
	}
}
