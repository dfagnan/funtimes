package team104;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public abstract class StaticStuff {
	public static RobotController myRC;
	public static TangentNavigator nav;
	public static MapLocation cur;
	public static Direction dir;
	public static MapLocation base;
	public static int tEdge = -1;
	public static int bEdge = -1;
	public static int lEdge = -1;
	public static int rEdge = -1;
	public static boolean isMoving;
	public static boolean isAttacking;
	
	
	public static void init(RobotController rc){
		myRC = rc;
		base = myRC.sensePowerCore().getLocation();
		nav = new TangentNavigator(rc);
		
	
	}
	
	public static void getProperties(){
		isMoving = myRC.isMovementActive();
		isAttacking = myRC.isAttackActive();
		cur = myRC.getLocation();
		dir = myRC.getDirection();
		
	}
	
	
}
