package edu.nus.comp.dotagrid.ui.renderers;

public class CommonShapes {
	private VertexBufferManager vBufMan;
	public CommonShapes (VertexBufferManager man) {
		vBufMan = man;
		// Square
		vBufMan.setVertexBuffer("GenericFullSquare", new float[]{1,1,0,1,1,-1,0,1,-1,-1,0,1,-1,1,0,1});
		vBufMan.setIndexBuffer("GenericFullSquareIndex", new int[]{0,1,3,2});
		vBufMan.setVertexBuffer("GenericFullSquareTexture", new float[]{1,0,1,1,0,1,0,0});
	}
}
