package team104;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.TerrainTile;

public abstract class BasePlayer extends StaticStuff{
	public BasePlayer(RobotController rc){

	}

	public abstract void run();
	
	public MapLocation  getNearestCapturable(){
		return getNearestCapturable(new MapSet());
	}
	public MapLocation getNearestCapturable(MapSet set){
		MapLocation cur = myRC.getLocation();
		MapLocation[] nodes = myRC.senseCapturablePowerNodes();
		MapLocation closeNode = nodes[0];
		double min = closeNode.distanceSquaredTo(myRC.getLocation());
		for(MapLocation loc : nodes){
			if(!set.contains(loc) && loc.distanceSquaredTo(cur)<min){
				min = loc.distanceSquaredTo(cur);
				closeNode = loc;
			}
		}
		return closeNode;
	}
	
	public void senseHelper(MapLocation loc, Direction dir, int radius){
		boolean done = false;
		int i=radius;
		while(!done&&i>0){
			MapLocation cur = loc.add(dir, i);
			if(myRC.senseTerrainTile(cur)!=TerrainTile.OFF_MAP)
				done=true;
			else{
				switch(dir){
				case NORTH:
					tEdge = cur.y;
					break;
				case SOUTH:
					bEdge = cur.y;
					break;
				case EAST:
					rEdge = cur.x;
					break;
				case WEST:
					lEdge = cur.x;
					break;
				}
			}
			i--;
		}
		System.out.println("SAMPLE: "+lEdge+" DIR: "+dir);
	}
	public void senseEdges(int radius){
		MapLocation loc = myRC.getLocation();
		//NORTH,EAST,WEST,SOUTH
		senseHelper(loc,Direction.NORTH,radius);
		senseHelper(loc,Direction.EAST,radius);
		senseHelper(loc,Direction.WEST,radius);
		senseHelper(loc,Direction.SOUTH,radius);
	}

}
