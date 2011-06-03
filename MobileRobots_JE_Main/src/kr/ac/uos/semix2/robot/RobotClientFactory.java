package kr.ac.uos.semix2.robot;

import kr.ac.uos.semix2.impl.robot.RobotClientImpl;

public class RobotClientFactory {
	
	private RobotClientFactory() {
		//
	}
	
	public static final RobotClient newRobotClient() {
		return new RobotClientImpl();
	}
}
