package Agility;

public class Constants {

	public static volatile boolean debugMode = false;
	// Need to find all possible food ids
	public static volatile int foodID = 0;

	// Burthrope
	public static final int longBeamID = 66894;
	public static final int wallID = 66912;
	public static final int walkAcrossID = 66903;
	public static final int obstacleID = 66902;
	public static final int ropeID = 66904;
	public static final int barsID = 66897;
	public static final int jumpDownLedgeID = 66910;

	// GnomeVillage
	public static final int balanceLogID = 69526;
	public static final int netID = 69383;
	public static final int treeBranchID = 69508;
	public static final int balanceRopeID = 2312;
	public static final int climbDownTreeID = 69507;
	public static final int obstackleNetID = 69384;
	public static final int[] pipeIDs = { 69378, 69377 };

	// Barbarian Outpost
	public static final int ropeSwingID = 43526;
	public static final int ropeLadderID = 32015;
	public static final int logBalanceID = 43595;
	public static final int obstacleIDBARB = 20211;
	public static final int LedgeIDBARB = 2302;
	public static final int ladderID = 3205;
	public static final int crumblingWallID = 1948;

	// Wilderness
	public static final int obstaclePipe = 65362;
	public static final int ropeWild = 64696;

	// Tracking stuff
	public static volatile int startingAgilityLevel;
	public static volatile int startingAgilityExp;
	public static volatile int totalLaps = 0;
}
