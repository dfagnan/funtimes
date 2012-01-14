package team104;

import team104.Util.ArchonState;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Message;
import battlecode.common.PowerNode;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotLevel;
import battlecode.common.RobotType;

public class ArchonPlayer extends BasePlayer{
	public boolean waitingTower = false;
	public int numNodes = 0;
	public ArchonState state;
	public Message toBroadcast;
	public static final int RADSQ = RobotType.ARCHON.sensorRadiusSquared;
	public ArchonPlayer(RobotController myRC) {
		super(myRC);
		senseEdges();
		setup();
		
	}

	public void setup(){
		Message[] msg = myRC.getAllMessages();
		if(msg.length>0){
			state = ArchonState.FOLLOW;
		}
		else{
			state = ArchonState.NODE;
			nav.setDestination(getNearestCapturable(),1.5);
		}
		
	}
	public void senseEdges(){
		super.senseEdges(6);
	}






	public void run(){
		try {

			while(true){

				
				switch(state){
				case NODE:
					if(toBroadcast!=null){
						if(myRC.getFlux()>GameConstants.BROADCAST_FIXED_COST+GameConstants.BROADCAST_COST_PER_BYTE*3)
							myRC.broadcast(toBroadcast);
					}
					MapLocation cur = myRC.getLocation();
					//CHECK ENEMY
					Robot[] robots = myRC.senseNearbyGameObjects(Robot.class);
					for(Robot r : robots){
						RobotInfo info = myRC.senseRobotInfo(r);
						if(info.team!=myRC.getTeam()){
							if(!myRC.isAttackActive() && myRC.canAttackSquare(info.location))
								myRC.attackSquare(info.location, info.type != RobotType.SCOUT 
										? RobotLevel.ON_GROUND : RobotLevel.IN_AIR);
							nav.setDestination(info.location.add(info.location.directionTo(cur),6));
							nav.bugNavigate(true);
							toBroadcast = broadcastLocation(info.location);
							if(!myRC.hasBroadcasted() && myRC.getFlux()>GameConstants.BROADCAST_FIXED_COST+GameConstants.BROADCAST_COST_PER_BYTE*3)
								myRC.broadcast(toBroadcast);
							System.out.println("ATTTTTTTTTTACK");
							myRC.yield();
						}
					}

			
					if(!myRC.isMovementActive() && !waitingTower){
						int soldiers = 0;
						int scouts = 0;
						robots = myRC.senseNearbyGameObjects(Robot.class);
						for(Robot r : robots){
							RobotInfo info = myRC.senseRobotInfo(r);
							if(info.type==RobotType.SOLDIER)
								soldiers++;
							else if(info.type==RobotType.SCOUT)
								scouts++;
						}
						if(soldiers>=2&& (double)(scouts+1.0)/soldiers<0.25){
							//BUILD SCOUT
							if(myRC.getFlux()>RobotType.SCOUT.spawnCost && myRC.canMove(myRC.getDirection()))
								myRC.spawn(RobotType.SCOUT);
						}
						else{
							//BUILD SOLIDER
							if(myRC.getFlux()>RobotType.SOLDIER.spawnCost && myRC.canMove(myRC.getDirection()))
								myRC.spawn(RobotType.SOLDIER);
						}

					}


					for(Robot r : robots){
						RobotInfo info = myRC.senseRobotInfo(r);
						if(cur.distanceSquaredTo(info.location)<=2 && info.type != RobotType.TOWER 
								&& info.flux<20 && myRC.getFlux()>0){
							myRC.transferFlux(info.location, info.type != RobotType.SCOUT ? RobotLevel.ON_GROUND 
									: RobotLevel.IN_AIR , Math.min(myRC.getFlux(),20-info.flux));
							System.out.println("TRANSFERRED: "+info.type);
						}
					}
					if(nav.bugNavigate(true)){
						if(myRC.getFlux()<200){
							waitingTower=true;
							MapLocation node = getNearestCapturable();
							if(cur.distanceSquaredTo(node)>2)
								waitingTower = false;
							nav.setDestination(node,1.5);
							myRC.yield();
						}
						else{
							myRC.spawn(RobotType.TOWER);
							waitingTower=false;
							nav.setDestination(getNearestCapturable(),1.5);
							myRC.yield();
						}
					}
					else myRC.yield();
					break;
				case FOLLOW:
					myRC.yield();
					
				}
				
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


	

	public Message broadcastLocation(MapLocation closeNode) throws GameActionException{
		Message msg = new Message();
		MapLocation[] locs = new MapLocation[1];
		locs[0]=closeNode;
		msg.locations=locs;
		return msg;
	}
}
