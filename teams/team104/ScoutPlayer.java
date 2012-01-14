package team104;

import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Message;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotLevel;
import battlecode.common.RobotType;

public class ScoutPlayer extends BasePlayer{

	public ScoutPlayer(RobotController rc) {
		super(rc);
		
	}

	@Override
	public void run() {
		while(true){
			try{
				MapLocation cur = myRC.getLocation();
				
				Message msg = myRC.getNextMessage();
				if(msg!=null){
					System.out.println("RECEIVED<<<<<<<<<<");
					System.out.println(msg.locations[0]);
					if(!myRC.isAttackActive() && myRC.canAttackSquare(msg.locations[0]))
						myRC.attackSquare(msg.locations[0], RobotLevel.ON_GROUND) ;
					nav.setDestination(msg.locations[0],RobotType.SOLDIER.attackRadiusMinSquared);
					nav.bugNavigate(true);
				}
				
				
				//CHECK ENEMY
				Robot[] robots = myRC.senseNearbyGameObjects(Robot.class);
				double minHP = 100000;
				boolean heal = false;
				boolean movetoHeal = false;
				for(Robot r : robots){
					RobotInfo info = myRC.senseRobotInfo(r);
					if(info.team==myRC.getTeam() && info.energon != info.type.maxEnergon 
							&& myRC.getFlux()>=GameConstants.REGEN_COST){
						if(info.location.distanceSquaredTo(cur)<=Math.sqrt(RobotType.SCOUT.attackRadiusMinSquared))
								heal = true;
						if(info.energon < minHP){
							movetoHeal = true;
							minHP = info.energon;
							nav.setDestination(info.location, Math.sqrt(RobotType.SCOUT.attackRadiusMinSquared));
						}
					}
					
				}
				if(heal) myRC.regenerate();
				if(movetoHeal) {
					nav.bugNavigate(true);
					myRC.yield();
				}

				
				
				MapLocation[] archons = myRC.senseAlliedArchons();
				if(archons.length>1){
					MapLocation closeArchon = archons[0];
					double min = closeArchon.distanceSquaredTo(cur);
					for(MapLocation loc : archons){
						if(loc.distanceSquaredTo(cur)<=min){
							min = loc.distanceSquaredTo(cur);
							nav.setDestination(loc);
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
