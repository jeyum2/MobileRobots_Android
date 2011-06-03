package kr.ac.uos.semix2.robot.example;

import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.Parameter;
import kr.ac.uos.semix2.robot.ParameterBuilder;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class GetCurrentSensor {

	public static void main(String[] args) {
		RobotClient client = RobotClientFactory.newRobotClient();
		if (!client.connect()) {
			System.out.println("connection failed.");
			return;
		}
		System.out.println("Connected");
		
		Command getSensorCurrentCommand = client.getCommand("getSensorCurrent");
		if (getSensorCurrentCommand == null) {
			System.out.println("Command not fount");
			client.disconnect();
			return;
		}
		
		System.out.println(getSensorCurrentCommand);
		
		client.addDataPacketHandler(getSensorCurrentCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				int numReadings = iter.nextByte2();
				String sensorName = iter.nextString();
				System.out.println("Current [" + sensorName + "] " + numReadings);
				for (int i=0; i<numReadings; i++) {
					int x = iter.nextByte4();
					int y = iter.nextByte4();
					System.out.println(String.format("\tx: %7d, y: %7d", x, y));
				}
				
			}
		});

		/*
		 * Sensor List:
		 * 		forbidden
		 * 		bumpers
		 * 		irs
		 * 		multiRobot
		 * 		sonar
		 * 		laser
		 */
		String sensorName = "sonar";

		ParameterBuilder builder = new ParameterBuilder();
		builder.appendString(sensorName);
		Parameter parameter = builder.toParameter();
		
		client.request(getSensorCurrentCommand, parameter, 1000);
		
		try {
			Thread.sleep(5000);
		} catch(InterruptedException ignore) {}

		client.stop(getSensorCurrentCommand);
		client.disconnect();
	}
}
