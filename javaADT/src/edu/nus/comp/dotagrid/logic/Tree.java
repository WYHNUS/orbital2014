package edu.nus.comp.dotagrid.logic;

public class Tree extends Character{
	
	public static final int TREE_REVIVE_TIME = 100;

	public Tree() {
		super("Tree", 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3);
		// Tree has all zero attribute
		this.setAttackable(false);
		this.setCharacterImage("WorldMap/terrian", "Tree");
	}
	
	public static void treeRevive(){
		if (GameFrame.turn % TREE_REVIVE_TIME == 0) {
			// travel through world map searching for places needed to revive trees
			for (int x=0; x<GridFrame.ROW_NUMBER; x++) {
				for (int y=0; y<GridFrame.COLUMN_NUMBER; y++) { 
					if (GridFrame.treeMap[x][y]) {
						if (GridFrame.map[x][y] != 4) {
							// revive trees if no character occupies this grid
							if (GridFrame.gridButtonMap[x][y].getCharacter() == null) {
								// revive!
								GridFrame.map[x][y] = 4;
								GridFrame.gridButtonMap[x][y] = new GridButton(4);
							} 
						}
					}
				}
			}
		}
	}
}
