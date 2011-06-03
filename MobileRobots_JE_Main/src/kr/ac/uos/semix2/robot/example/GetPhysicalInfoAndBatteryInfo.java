package kr.ac.uos.semix2.robot.example;

import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class GetPhysicalInfoAndBatteryInfo {

	public static void main(String[] args) {
		RobotClient client = RobotClientFactory.newRobotClient();
		if (!client.connect()) {
			System.out.println("connection failed.");
			return;
		}
		
		Command physicalInfoCommand = client.getCommand("physicalInfo");
		Command batteryInfoCommand = client.getCommand("batteryInfo");
		if (physicalInfoCommand == null || batteryInfoCommand == null) {
			System.out.println("Command not fount");
			client.disconnect();
			return;
		}
		
		System.out.println(physicalInfoCommand);
		System.out.println(batteryInfoCommand);
		
		client.addDataPacketHandler(physicalInfoCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				String robotType 		= iter.nextString();
				String robotSubtype 	= iter.nextString();
				int width 				= iter.nextByte2();
				int lengthFront 		= iter.nextByte2();
				int lengthRear 			= iter.nextByte2();
				
				StringBuilder builder = new StringBuilder();
				builder.append("Type: ").append(robotType);
				builder.append(" Subtype: ").append(robotSubtype);
				builder.append(" Width: ").append(width);
				builder.append(" LengthFront: ").append(lengthFront);
				builder.append(" LengthRear: ").append(lengthRear);
				System.out.println(builder);
			}
		});
		client.request(physicalInfoCommand);

		client.addDataPacketHandler(batteryInfoCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				double lowBattery 		= iter.nextDouble();
				double shutdownBattery 	= iter.nextDouble();

				StringBuilder builder = new StringBuilder();
				builder.append("Low battery voltage: ").append(lowBattery);
				builder.append(" Shutdown battery voltage: ").append(shutdownBattery);
				System.out.println(builder);
			}
		});
		client.request(batteryInfoCommand);
		
		try {
			Thread.sleep(50);
		} catch(InterruptedException ignore) {}
		client.disconnect();
	}
}
