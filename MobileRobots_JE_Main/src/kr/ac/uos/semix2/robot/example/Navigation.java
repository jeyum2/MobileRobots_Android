package kr.ac.uos.semix2.robot.example;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.Parameter;
import kr.ac.uos.semix2.robot.ParameterBuilder;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class Navigation {

	public static void main(String[] args) {
		RobotClient client = RobotClientFactory.newRobotClient();
		if (!client.connect()) {
			System.out.println("connection failed.");
			return;
		}

		Command updateCommand = client.getCommand("update");
		Command getGoalsCommand = client.getCommand("getGoals");
		Command goalNameCommand = client.getCommand("goalName");
		Command pathPlannerStatusCommand = client.getCommand("pathPlannerStatus");
		Command gotoGoalCommand = client.getCommand("gotoGoal");

		if (updateCommand == null || getGoalsCommand == null || goalNameCommand == null || pathPlannerStatusCommand == null || gotoGoalCommand == null) {
			System.out.println("Command not found");
			client.disconnect();
			return;
		}
		
		System.out.println(updateCommand);
		System.out.println(getGoalsCommand);
		System.out.println(goalNameCommand);
		System.out.println(pathPlannerStatusCommand);
		System.out.println(gotoGoalCommand);
		
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
		client.request(updateCommand, 5000);
		
		client.addDataPacketHandler(getGoalsCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				List<String> goalList = new LinkedList<String>();
				DataPacketIterator iter = packet.getDataPacketIterator();
				while(iter.hasNext(1)) {
					String goal = iter.nextString();
					if (goal.length() > 0) {
						goalList.add(goal);
					}
				}
				
				StringBuilder builder = new StringBuilder();
				builder.append("Goal List:\n");
				for (String goal : goalList) {
					builder.append("\t").append(goal).append("\n");
				}
				System.out.println(builder);
			}
		});
		client.request(getGoalsCommand);

		client.addDataPacketHandler(goalNameCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				String currentGoal = iter.nextString();
				System.out.println("Current goal: " + currentGoal);
			}
		});
		client.request(goalNameCommand, 5000);
		
		client.addDataPacketHandler(pathPlannerStatusCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				String status = iter.nextString();
				System.out.println("Path planner status: " + status);
			}
		});
		client.request(pathPlannerStatusCommand, 5000);
		
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.println("=> Enter a goal name: ");
			String goal = in.nextLine();
			if (goal.length() == 0) {
				break;
			}
			if (goal.equals("?")) {
				client.request(getGoalsCommand);
			} else {
				ParameterBuilder builder = new ParameterBuilder();
				builder.appendString(goal);
				Parameter parameter = builder.toParameter();
				client.request(gotoGoalCommand, parameter);
			}
		}
		
		client.disconnect();
	}
}
