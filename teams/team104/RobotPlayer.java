package team104;

import battlecode.common.RobotController;

public class RobotPlayer {

	public static void run(RobotController myRC) {

		StaticStuff.init(myRC);//defines myRC and base location for later use
		switch(myRC.getType()) {
		case ARCHON:
			new ArchonPlayer(myRC).run();
			break;
		}

	}



	
	
}
