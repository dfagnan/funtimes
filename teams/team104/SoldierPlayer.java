package team104;

import battlecode.common.MapLocation;
import battlecode.common.Message;
import battlecode.common.PowerNode;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotLevel;
import battlecode.common.RobotType;

public class SoldierPlayer extends BasePlayer{

	public SoldierPlayer(RobotController rc) {
		super(rc);
		senseEdges();
	}

	public void senseEdges(){
		super.senseEdges(3);
	}

	@Override
	public void run() {
		while(true){
			try{

				
				Message msg = myRC.getNextMessage();
				if(msg!=null){
					System.out.println("RECEIVED<<<<<<<<<<");
					System.out.println(msg.locations[0]);
					if(!myRC.isAttackActive() && myRC.canAttackSquare(msg.locations[0]))
						myRC.attackSquare(msg.locations[0], RobotLevel.ON_GROUND) ;
					nav.setDestination(msg.locations[0],RobotType.SOLDIER.attackRadiusMinSquared);
					nav.bugNavigate(true);
					myRC.yield();
				}
				
				//CHECK ENEMY
				Robot[] robots = myRC.senseNearbyGameObjects(Robot.class);
				for(Robot r : robots){
					RobotInfo info = myRC.senseRobotInfo(r);
					if(info.team!=myRC.getTeam()){
						if(!myRC.isAttackActive() && myRC.canAttackSquare(info.location))
							myRC.attackSquare(info.location, info.type != RobotType.SCOUT 
									? RobotLevel.ON_GROUND : RobotLevel.IN_AIR);
						nav.setDestination(info.location,Math.sqrt(RobotType.SOLDIER.attackRadiusMaxSquared));
						nav.bugNavigate(true);
						myRC.yield();
					}
				}
				
				MapLocation cur = myRC.getLocation();
				MapLocation[] archons = myRC.senseAlliedArchons();
				if(archons.length>1){
					MapLocation closeArchon = archons[0];
					double min = closeArchon.distanceSquaredTo(cur);
					for(MapLocation loc : archons){
						if(loc.distanceSquaredTo(cur)<=min){
							min = loc.distanceSquaredTo(cur);
							nav.setDestination(loc,1);
						}
					}
				}
				
				if(!myRC.isMovementActive()){
					nav.bugNavigate(true);
				}
				myRC.yield();
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
		
	}


}
