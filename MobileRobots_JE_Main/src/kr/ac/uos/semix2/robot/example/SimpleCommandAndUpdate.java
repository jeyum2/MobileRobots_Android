package kr.ac.uos.semix2.robot.example;

import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.Parameter;
import kr.ac.uos.semix2.robot.ParameterBuilder;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class SimpleCommandAndUpdate {

	public static void main(String[] args) {
		RobotClient client = RobotClientFactory.newRobotClient();
		if (!client.connect()) {
			System.out.println("connection failed.");
			return;
		}

		Command updateCommand = client.getCommand("update");
		Command ratioDriveCommand = client.getCommand("ratioDrive");
		if (updateCommand == null || ratioDriveCommand == null) {
			System.out.println("Command not found");
			client.disconnect();
			return;
		}
		
		System.out.println(updateCommand);
		System.out.println(ratioDriveCommand);
		
		client.addDataPacketHandler(updateCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				String status 		= iter.nextString();
				String mode 		= iter.nextString();
				double voltage 		= iter.nextByte2() / 10.0;
				int x 				= iter.nextByte4();
				int y 				= iter.nextByte4();
				int head 			= iter.nextByte2();
				int velocity 		= iter.nextByte2();
				int rotateVelocity 	= iter.nextByte2();
				
				System.out.println(String.format("x: %6d| y: %6d| head: %6d| vel: %6d| rotVel: %6d| volts: %6.1f| mode: %15s| status: %20s|", x, y, head, velocity, rotateVelocity, voltage, mode, status));
			}
		});
		client.request(updateCommand, 200);

		ParameterBuilder builder = new ParameterBuilder();
		builder.appendDouble(20); 		// translationValue
		builder.appendDouble(20);		// rotationValue
		builder.appendDouble(100);		// scalingFactor
		Parameter parameter = builder.toParameter();
		client.request(ratioDriveCommand, parameter, 100);

		try {
			Thread.sleep(5000);
		} catch(InterruptedException ignore) {}
		
//		builder = new ParameterBuilder();
//		builder.appendDouble(0); 		// translationValue
//		builder.appendDouble(0);		// rotationValue
//		builder.appendDouble(100);		// scalingFactor
//		client.request(ratioDriveCommand, parameter);
		
		client.stop(ratioDriveCommand);
		
		try {
			Thread.sleep(5000);
		} catch(InterruptedException ignore) {}

		client.disconnect();
	}
}
