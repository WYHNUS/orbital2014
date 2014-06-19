package edu.nus.comp.dotagridandroid.ui.renderers;

public class CommonShapes {
	private GLResourceManager vBufMan;
	public CommonShapes (GLResourceManager man) {
		vBufMan = man;
		// Square
		vBufMan.setVertexBuffer("GenericFullSquare", new float[]{-1,1,0,1,-1,-1,0,1,1,-1,0,1,1,1,0,1});
		vBufMan.setIndexBuffer("GenericFullSquareIndex", new int[]{0,1,3,2});
		vBufMan.setVertexBuffer("GenericFullSquareTextureYInverted", new float[]{0,0,0,1,1,1,1,0});
		vBufMan.setVertexBuffer("GenericFullSquareTexture", new float[]{0,1,0,0,1,0,1,1});
	}
}
