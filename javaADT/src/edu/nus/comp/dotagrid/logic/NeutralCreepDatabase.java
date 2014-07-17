package edu.nus.comp.dotagrid.logic;

import java.util.Queue;
import java.util.Random;

public class NeutralCreepDatabase {
	public static int smallNCWaveNumber = 5;
	public static int mediumNCWaveNumber = 5;
	public static int largeNCWaveNumber = 5;
	public static int ancientNCWaveNumber = 4;
	
	private static Random selection = new Random();
	
	public NeutralCreepDatabase() {}
	
	public static void createSmallNCWave(Queue<NeutralCreep> NCList){
		NCList.clear();
		smallNCWave(selection.nextInt(smallNCWaveNumber), NCList);
	}
	
	public static void createMediumNCWave(Queue<NeutralCreep> NCList){
		NCList.clear();
		mediumNCWave(selection.nextInt(mediumNCWaveNumber), NCList);
	}
	
	public static void createLargeNCWave(Queue<NeutralCreep> NCList){
		NCList.clear();
		largeNCWave(selection.nextInt(largeNCWaveNumber), NCList);
	}
	
	public static void createAncientNCWave(Queue<NeutralCreep> NCList){
		NCList.clear();
		ancientNCWave(selection.nextInt(ancientNCWaveNumber), NCList);
	}
	
	
	// create NC waves
	
	private static void ancientNCWave(int waveNumber, Queue<NeutralCreep> NCList) {
		// add the selected NC wave to NCList
		
		switch (waveNumber) {
			case 0 :
				// Black Dragon family
				NCList.add(NeutralCreepDatabase.getBlackDragon());
				NCList.add(NeutralCreepDatabase.getBlackDrake());
				NCList.add(NeutralCreepDatabase.getBlackDrake());
				break;
				
			case 1 :
				// Dragonspawn family
				NCList.add(NeutralCreepDatabase.getBlueDragonspawnOverseer());
				NCList.add(NeutralCreepDatabase.getBlueDragonspawnSorcerer());
				NCList.add(NeutralCreepDatabase.getBlueDragonspawnSorcerer());
				break;
				
			case 2 :
				// Jungle Stalker family
				NCList.add(NeutralCreepDatabase.getElderJungleStalker());
				NCList.add(NeutralCreepDatabase.getJungleStalker());
				NCList.add(NeutralCreepDatabase.getJungleStalker());
				break;
				
			case 3 :
				// Golem family
				NCList.add(NeutralCreepDatabase.getGraniteGolem());
				NCList.add(NeutralCreepDatabase.getRockGolem());
				NCList.add(NeutralCreepDatabase.getRockGolem());
				break;
		}
	}
	

	private static void largeNCWave(int waveNumber, Queue<NeutralCreep> NCList) {
		// add the selected NC wave to NCList
		
		switch (waveNumber) {
			case 0 :
				// Troll family
				NCList.add(NeutralCreepDatabase.getDarkTrollWarlord());
				NCList.add(NeutralCreepDatabase.getDarkTroll());
				NCList.add(NeutralCreepDatabase.getDarkTroll());
				break;
				
			case 1 :
				// Satyr family
				NCList.add(NeutralCreepDatabase.getSatyrHellcaller());
				NCList.add(NeutralCreepDatabase.getSatyrSoulstealer());
				NCList.add(NeutralCreepDatabase.getSatyrTrickster());
				break;
				
			case 2 :
				// Wildkin family
				NCList.add(NeutralCreepDatabase.getEnragedWildkin());
				NCList.add(NeutralCreepDatabase.getWildkin());
				NCList.add(NeutralCreepDatabase.getWildkin());
				break;
				
			case 3 :
				// Centaur family
				NCList.add(NeutralCreepDatabase.getCentaurKhan());
				NCList.add(NeutralCreepDatabase.getCentaurOutrunner());
				break;
				
			case 4 :
				// Polar Bear family
				NCList.add(NeutralCreepDatabase.getPolarFurbolgChampion());
				NCList.add(NeutralCreepDatabase.getPolarFurbolgUrsaWarrior());
				break;
				
		}
	}
	
	private static void mediumNCWave(int waveNumber, Queue<NeutralCreep> NCList) {
		// add the selected NC wave to NCList
		
		switch (waveNumber) {
			case 0 :
				// Wolf family
				NCList.add(NeutralCreepDatabase.getAlphaWolf());
				NCList.add(NeutralCreepDatabase.getGiantWolf());
				NCList.add(NeutralCreepDatabase.getGiantWolf());
				break;
				
			case 1 :
				// Ogre family
				NCList.add(NeutralCreepDatabase.getOgreMagi());
				NCList.add(NeutralCreepDatabase.getOgreMauler());
				NCList.add(NeutralCreepDatabase.getOgreMauler());
				break;
				
			case 2 :
				// Mud Golem
				NCList.add(NeutralCreepDatabase.getMudGolem());
				NCList.add(NeutralCreepDatabase.getMudGolem());
				break;
				
			case 3 :
				// Centaur family
				NCList.add(NeutralCreepDatabase.getCentaurKhan());
				NCList.add(NeutralCreepDatabase.getCentaurOutrunner());
				break;
				
			case 4 :
				// Satyr family
				NCList.add(NeutralCreepDatabase.getSatyrSoulstealer());
				NCList.add(NeutralCreepDatabase.getSatyrTrickster());
				NCList.add(NeutralCreepDatabase.getSatyrSoulstealer());
				NCList.add(NeutralCreepDatabase.getSatyrTrickster());
				break;
		}
	}

	private static void smallNCWave(int waveNumber, Queue<NeutralCreep> NCList) {
		// add the selected NC wave to NCList
		
		switch (waveNumber) {
			case 0 :
				// Kobold family
				NCList.add(NeutralCreepDatabase.getKoboldTaskmaster());
				NCList.add(NeutralCreepDatabase.getKoboldTunneler());
				NCList.add(NeutralCreepDatabase.getKobold());
				NCList.add(NeutralCreepDatabase.getKobold());
				NCList.add(NeutralCreepDatabase.getKobold());
				break;
				
			case 1 :
				// Forest Troll family
				NCList.add(NeutralCreepDatabase.getForestTrollBerserker());
				NCList.add(NeutralCreepDatabase.getForestTrollBerserker());
				NCList.add(NeutralCreepDatabase.getForestTrollHighPriest());
				break;
				
			case 2 :
				// Kobold and Troll
				NCList.add(NeutralCreepDatabase.getForestTrollBerserker());
				NCList.add(NeutralCreepDatabase.getForestTrollBerserker());
				NCList.add(NeutralCreepDatabase.getKoboldTaskmaster());
				break;
				
			case 3 :
				// Gnoll Assassin
				NCList.add(NeutralCreepDatabase.getGnollAssassin());
				NCList.add(NeutralCreepDatabase.getGnollAssassin());
				NCList.add(NeutralCreepDatabase.getGnollAssassin());
				break;
				
			case 4 :
				// Ghost and Fel Beast
				NCList.add(NeutralCreepDatabase.getGhost());
				NCList.add(NeutralCreepDatabase.getFelBeast());
				NCList.add(NeutralCreepDatabase.getFelBeast());
				break;
		}
		
	}

	
	
	// create NC database!
	
	/*
	 * String name, 
	 * int bountyMoney, 
	 * int bountyExp, 
	 * int startingHP, 
	 * int startingMP, 
	 * double HPGainPerRound, 
	 * double MPGainPerRound,
	 * 
	 * double startingPhysicalAttack, 
	 * int startingPhysicalAttackArea, 
	 * double startingPhysicalAttackSpeed, 
	 * double startingPhysicalDefence, 
	 * double startingMagicResistance,
	 * int startingMovementSpeed, 
	 * int startingAP
	 * 
	 */
	
	public static NeutralCreep getAlphaWolf(){
		return new NeutralCreep("AlphaWolf", 41, 88, 600, 0, 5, 0,
				32, 1, 2.30, 3, 0, 350, 100);
	}
	
	public static NeutralCreep getBlackDragon(){
		return new NeutralCreep("BlackDragon", 200, 155, 2000, 0, 20, 0,
				85, 3, 1.50, 6, 90, 300, 100);
	}
	
	public static NeutralCreep getBlackDrake(){
		return new NeutralCreep("BlackDrake", 51, 62, 950, 0, 5, 0,
				53, 3, 0.80, 2, 90, 350, 100);
	}
	
	public static NeutralCreep getBlueDragonspawnOverseer(){
		return new NeutralCreep("BlueDragonspawnOverseer", 90, 119, 1200, 400, 5, 5,
				63, 1, 2.30, 1, 90, 300, 100);
	}
	
	public static NeutralCreep getBlueDragonspawnSorcerer(){
		return new NeutralCreep("BlueDragonspawnSorcerer", 80, 62, 675, 400, 5, 5,
				35, 1, 2.30, 0, 90, 270, 100);
	}
	
	public static NeutralCreep getCentaurKhan(){
		return new NeutralCreep("CentaurKhan", 82, 119, 1100, 200, 10, 5,
				52, 1, 1.20, 4, 0, 320, 100);
	}
	
	public static NeutralCreep getCentaurOutrunner(){
		return new NeutralCreep("CentaurOutrunner", 22, 41, 550, 0, 5, 0,
				20, 1, 2.30, 2, 0, 350, 100);
	}
	
	public static NeutralCreep getDarkTroll(){
		return new NeutralCreep("DarkTroll", 30, 62, 500, 0, 5, 0,
				26, 3, 2.30, 0, 0, 270, 100);
	}
	
	public static NeutralCreep getDarkTrollWarlord(){
		return new NeutralCreep("DarkTrollWarlord", 58, 155, 1100, 550, 5, 5,
				52, 3, 2.30, 1, 0, 320, 100);
	}
	
	public static NeutralCreep getElderJungleStalker(){
		return new NeutralCreep("ElderJungleStalker", 161, 155, 2000, 0, 5, 0,
				70, 1, 2.30, 4, 90, 320, 100);
	}
	
	public static NeutralCreep getEnragedWildkin(){
		return new NeutralCreep("EnragedWildkin", 77, 119, 950, 400, 5, 5,
				53, 1, 2.30, 4, 0, 320, 100);
	}
	
	public static NeutralCreep getFelBeast(){
		return new NeutralCreep("FelBeast", 22, 41, 400, 0, 5, 0,
				15, 1, 1.50, 1, 0, 350, 100);
	}
	
	public static NeutralCreep getForestTrollBerserker(){
		return new NeutralCreep("ForestTrollBerserker", 25, 41, 500, 0, 5, 0,
				33, 3, 1.20, 1, 0, 270, 100);
	}
	
	public static NeutralCreep getForestTrollHighPriest(){
		return new NeutralCreep("ForestTrollHighPriest", 23, 41, 450, 500, 5, 5,
				28, 3, 0.8, 0, 0, 290, 100);
	}
	
	public static NeutralCreep getGhost(){
		return new NeutralCreep("Ghost", 35, 62, 500, 400, 5, 5,
				33, 3, 3.10, 1, 0, 320, 100);
	}
	
	public static NeutralCreep getGiantWolf(){
		return new NeutralCreep("GiantWolf", 29, 62, 500, 0, 5, 0,
				23, 1, 2.30, 1, 0, 350, 100);
	}
	
	public static NeutralCreep getGnollAssassin(){
		return new NeutralCreep("GnollAssassin", 30, 41, 370, 0, 5, 0,
				32, 3, 1.20, 1, 0, 270, 100);
	}
	
	public static NeutralCreep getGraniteGolem(){
		return new NeutralCreep("GraniteGolem", 116, 155, 2000, 600, 15, 5,
				83, 1, 2.30, 8, 100, 270, 100);
	}
	
	public static NeutralCreep getJungleStalker(){
		return new NeutralCreep("JungleStalker", 61, 119, 1300, 400, 5, 5,
				55, 1, 2.30, 2, 90, 320, 100);
	}
	
	public static NeutralCreep getKobold(){
		return new NeutralCreep("Kobold", 8, 25, 240, 0, 5, 0,
				11, 1, 2.30, 0, 0, 270, 100);
	}
	
	public static NeutralCreep getKoboldTaskmaster(){
		return new NeutralCreep("KoboldTaskmaster", 35, 41, 400, 0, 5, 0,
				26, 1, 2.30, 0, 0, 330, 100);
	}
	
	public static NeutralCreep getKoboldTunneler(){
		return new NeutralCreep("KoboldTunneler", 18, 25, 325, 0, 5, 0,
				15, 1, 2.30, 1, 0, 270, 100);
	}
	
	public static NeutralCreep getMudGolem(){
		return new NeutralCreep("MudGolem", 58, 119, 800, 400, 5, 5,
				31, 1, 2.30, 4, 100, 270, 100);
	}
	
	public static NeutralCreep getOgreMagi(){
		return new NeutralCreep("OgreMagi", 46, 62, 600, 400, 5, 5,
				26, 1, 2.30, 0, 0, 270, 100);
	}
	
	public static NeutralCreep getOgreMauler(){
		return new NeutralCreep("OgreMauler", 40, 41, 850, 0, 5, 0,
				26, 1, 2.30, 1, 0, 270, 100);
	}
	
	public static NeutralCreep getPolarFurbolgChampion(){
		return new NeutralCreep("PolarFurbolgChampion", 65, 88, 950, 0, 5, 0,
				42, 1, 2.30, 5, 0, 320, 100);
	}
	
	public static NeutralCreep getPolarFurbolgUrsaWarrior(){
		return new NeutralCreep("PolarFurbolgUrsaWarrior", 84, 119, 1100, 300, 10, 5,
				53, 1, 2.30, 4, 0, 320, 100);
	}
	
	public static NeutralCreep getRockGolem(){
		return new NeutralCreep("RockGolem", 58, 119, 800, 400, 5, 5,
				31, 1, 2.30, 4, 100, 270, 100);
	}
	
	public static NeutralCreep getRoshan(){
		return new NeutralCreep("Roshan", 1450, 1184, 9000, 2000, 200, 5,
				195, 1, 1.20, 5, 90, 270, 100);
	}
	
	public static NeutralCreep getSatyrHellcaller(){
		return new NeutralCreep("SatyrHellcaller", 111, 155, 1100, 300, 10, 5,
				52, 1, 2.30, 0, 0, 290, 100);
	}
	
	public static NeutralCreep getSatyrSoulstealer(){
		return new NeutralCreep("SatyrSoulstealer", 35, 88, 600, 400, 5, 5,
				26, 1, 2.30, 1, 0, 270, 100);
	}
	
	public static NeutralCreep getSatyrTrickster(){
		return new NeutralCreep("SatyrTrickster", 16, 41, 240, 200, 5, 5,
				8, 6, 0.8, 0, 0, 300, 100);
	}
	
	public static NeutralCreep getWildkin(){
		return new NeutralCreep("Wildkin", 18, 25, 350, 0, 5, 0,
				23, 1, 2.30, 2, 0, 300, 100);
	}
}
