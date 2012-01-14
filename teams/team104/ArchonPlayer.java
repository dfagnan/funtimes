package team104;

import battlecode.common.*;

public class ArchonPlayer extends BasePlayer{

	public static final int RADSQ = RobotType.ARCHON.sensorRadiusSquared;
	public ArchonPlayer(RobotController myRC) {
		super(myRC);
		senseEdges();
	}
	
	public void senseEdges(){
		super.senseEdges(6);
	}
	
	




	public void run(){
		try {
			System.out.println("Flux: "+myRC.getFlux());
			if(myRC.senseNearbyGameObjects(Robot.class).length>1)
				myRC.suicide();
			MapLocation cur = myRC.getLocation();
			nav.setDestination(cur.add(Direction.NORTH_WEST, 12));
			while(!nav.bugNavigate(false)){
				myRC.yield();
			}
			

		} catch (GameActionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		senseEdges();
		while(true){
			try{
				myRC.yield();
			}
			catch(Exception e){
				e.printStackTrace();
				myRC.yield();
			}
		}
	}
}
