package team104;

import battlecode.common.PowerNode;
import battlecode.common.RobotController;

public abstract class StaticStuff {
	public static RobotController myRC;
	public static Navigator nav;
	public static PowerNode base;
	
	public static int tEdge = -1;
	public static int bEdge = -1;
	public static int lEdge = -1;
	public static int rEdge = -1;
	public static boolean isMoving;
	public static boolean isAttacking;
	
	
	public static void init(RobotController rc){
		myRC = rc;
		base = myRC.sensePowerCore();	
		nav = new Navigator(rc);
		
	
	}
	
	
	

	
	
}
