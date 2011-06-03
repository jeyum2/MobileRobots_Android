package kr.ac.uos.semix2.robot.example;

import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class PrintAllCommands {

	public static void main(String[] args) {
		RobotClient client = RobotClientFactory.newRobotClient();
		//client.setHost("172.16.165.190");
		if (!client.connect()) {
			System.out.println("connection failed.");
			return;
		}
		printCommands(client);
		client.disconnect();
	}
	
	private static void printCommands(RobotClient client) {
		System.out.println("<HTML><HEAD></HEAD><BODY>");
		
		for (Command command : client.getAllCommands()) {
			System.out.println("<TABLE border=1 width=600>");
			System.out.println("<TR><TD>Command</TD><TD>" + command.getName() + " [" + command.getId() + "]" + "</TD></TR>");
			System.out.println("<TR><TD>Description</TD><TD>" + command.getDescription() + "</TD></TR>");
			System.out.println("<TR><TD>Arguments</TD><TD>" + command.getArgumentsDescription() + "</TD></TR>");
			System.out.println("<TR><TD>Returns</TD><TD>" + command.getReturnValueDescription() + "</TD></TR>");
			System.out.println("</TABLE>");
			System.out.println("<BR/>");
		}
		
		System.out.println("</BODY></HTML>");
		
		
		/*
		System.out.println(String.format("%5s | %30s | %35s | %s", "ID", "GroupName", "CommandName", "Description"));
		System.out.println("-------------------------------------------------------------------------------------------------------------------------");
		for (Command command : client.getAllCommands()) {
			System.out.println(String.format("%5d | %30s | %35s | %s", command.getId(), command.getGroupName(), command.getName(), command.getDescription().replace("\n", " ")));
		}
		*/
	}
}
