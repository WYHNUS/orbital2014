package edu.nus.comp.dotagrid.math;

public class RenderMaths {
	public static final float[] IdentityMatrix4x4 = new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
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
	public static float[] FlatVector4InnerProduct (float[] a, float[] b) {
		if (a.length != 4 || b.length != 4)
			throw new RuntimeException ("Wrong vector size");
		return new float[] {
				a[0]*b[0],
				a[1]*b[1],
				a[2]*b[2],
				a[3]*b[3]
		};
	}
	public static float[] FlatRotationMatrix4x4 (float angleRadians) {
		throw new UnsupportedOperationException();
	}
}
