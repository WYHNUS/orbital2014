package edu.nus.comp.dotagrid.logic;

public class AICharacter {
	
	public static boolean isAttack = false;
	
	private int teamNumber;
	private int startingXPos;
	private int startingYPos;
	
	public AICharacter(int XPos, int YPos) {
		startingXPos = XPos;
		startingYPos = YPos;
		teamNumber = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTeamNumber();
		isAttack = false;

		if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof LineCreep){
			// line creep AI
			
		}
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Tower){
			// tower AI
			do {
				// attack when have enough action point
				AIAttack();
			} while ((GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getCurrentActionPoint() -
					GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().APUsedWhenAttack() >= 0)
					&& (isAttack == true));
		}
		
		else if (GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter() instanceof Hero){
			// comp Hero AI
			
		}
	}
	
	public void AIAttack(){
		// attack method : attack any enemy in sight
		int attackRange = GridFrame.gridButtonMap[startingXPos][startingYPos].getCharacter().getTotalPhysicalAttackArea();
		
		for(int x=startingXPos-attackRange; x<startingXPos+attackRange+1; x++){
			for(int y=startingYPos-attackRange; y<startingYPos+attackRange+1; y++){
				// x and y need to be within the grid frame 
				if (x >= 0 && x <= GridFrame.COLUMN_NUMBER-1){
					if (y>=0 && y <= GridFrame.ROW_NUMBER-1) {
						// x + y need to be within the number of attackable grid
						if (Math.abs(startingXPos - x) + Math.abs(startingYPos - y) <= attackRange) {
							// check if any character is within attackable grid
							if (GridFrame.gridButtonMap[x][y].getCharacter() != null) {
								// check if the character is non-friendly unit
								if (GridFrame.gridButtonMap[x][y].getCharacter().getTeamNumber() != teamNumber) {
									// attack ENEMY!
									isAttack = true;
									new CharacterActions(2, startingXPos, startingYPos, x, y);
								}
							}
						}
					}
				}
			}
		}
	}
	
}
