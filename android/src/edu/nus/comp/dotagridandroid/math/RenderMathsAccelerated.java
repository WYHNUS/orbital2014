package edu.nus.comp.dotagridandroid.math;
import android.support.v8.renderscript.*;
public class RenderMathsAccelerated {
	public static final float PI = (float) Math.PI;
	public static final float ERROR = 1e-7f;
	
	public static float[] IdentityMatrix4x4 () {
		return new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	}
	public static float[] FlatMatrix4x4Multiplication (float[] a, float[] b) {
		Matrix4f matA = new Matrix4f(a), matB = new Matrix4f(b);
		matB.multiply(matA);
		return matB.getArray().clone();
	}
	public static float[] FlatMatrix4x4Multiplication (float[] a, float[] b, float[] ...fs) {
		final int len = fs.length;
		Matrix4f matResult = new Matrix4f (fs[len - 1]);
		for (int i = len - 2; i >= 0; i--)
			matResult.multiply(new Matrix4f (fs[i]));
		matResult.multiply(new Matrix4f(b));
		matResult.multiply(new Matrix4f(a));
		return matResult.getArray().clone();
	}
	public static float[] FlatMatrix4x4ScalarMultiplication (float a, float[] b) {
		Matrix4f matA = new Matrix4f(b);
		matA.scale(a, a, a);
		return matA.getArray().clone();
	}
	public static float Vector4InnerProduct (float[] a, float[] b) {
		if (a.length != 4 || b.length != 4)
			throw new RuntimeException ("Wrong vector size");
		return a[0]*b[0] + a[1]*b[1] + a[2]*b[2] + a[3]*b[3];
	}
	public static float[] FlatTranslationMatrix4x4 (float x, float y, float z) {
		return new float[] {1,0,0,x,0,1,0,y,0,0,1,z,0,0,0,1};
	}
	public static float[] FlatScalingMatrix4x4 (float x, float y, float z) {
		return new float[] {x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1};
	}
	public static float[] FlatPerspectiveMatrix4x4 (float near, float far, float left, float right, float top, float bottom) {
		Matrix4f matA = new Matrix4f();
		matA.loadFrustum(left, right, bottom, top, near, far);
		matA.transpose();
		return matA.getArray().clone();
//		return edu.nus.comp.dotagridandroid.math.RenderMaths.FlatPerspectiveMatrix4x4(near, far, left, right, top, bottom);
	}
	public static float[] FlatRotationMatrix4x4 (float angleRadians, float x, float y, float z) {
		Matrix4f matA = new Matrix4f();
		matA.loadRotate(angleRadians / PI * 180, x, y, z);
		matA.transpose();
		return matA.getArray().clone();
	}
	public static float[] FlatInverseMatrix4x4 (float[] a) {
		Matrix4f matA = new Matrix4f(a);
		if (matA.inverse())
			return matA.getArray().clone();
		else
			throw new RuntimeException ("Matrix not invertible");
	}
	public static float[] FlatMatrix4x4Transpose (float[] a) {
		Matrix4f matA = new Matrix4f(a);
		matA.transpose();
		return matA.getArray().clone();
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
	public static float[] NormalisedVector3(float[] a) {
		if (a.length != 3)
			throw new RuntimeException("Wrong vector size");
		final float len = (float) Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2]);
		if (Math.abs(len) < ERROR)
			return new float[] {0,0,0};
		else
			return new float[] {a[0]/len,a[1]/len,a[2]/len};
	}
}
