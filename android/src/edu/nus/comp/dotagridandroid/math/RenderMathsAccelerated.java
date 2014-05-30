package edu.nus.comp.dotagridandroid.math;
import android.support.v8.renderscript.*;
public class RenderMathsAccelerated {
	public static float[] IdentityMatrix4x4 () {
		return new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	}
	public static float[] FlatMatrix4x4Multiplication (float[] a, float[] b) {
		Matrix4f matA = new Matrix4f(a), matB = new Matrix4f(b);
		matA.multiply(matB);
		return matA.getArray().clone();
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
		return matA.getArray().clone();
	}
	public static float[] FlatRotationMatrix4x4 (float angleRadians, float x, float y, float z) {
		Matrix4f matA = new Matrix4f();
		matA.loadRotate(angleRadians, x, y, z);
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
}
