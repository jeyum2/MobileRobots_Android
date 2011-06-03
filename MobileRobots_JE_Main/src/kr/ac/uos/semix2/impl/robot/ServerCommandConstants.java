package kr.ac.uos.semix2.impl.robot;

public class ServerCommandConstants {
	public static final int SHUTDOWN 					= 1; 	// Closes the connection.  
	public static final int INTRODUCTION 				= 2;	// Introduces the server to the client.  
	public static final int UDP_INTRODUCTION 			= 3; 	// Udp introduction of the server to the client.  
	public static final int UDP_CONFIRMATION 			= 4;	// Confirmation Udp was received from client.  
	public static final int CONNECTED 					= 5;	// Server accepts clients connection.  
	public static final int REJECTED 					= 6;	// Server rejects clients connection, has a byte2, then a string.... these reasons (1 = bad username password, string then is empty, 2 = rejecting connection because using central server, string then is central server IP).  
	public static final int TCP_ONLY 					= 7;	// Server tells client to only send TCP.  
	public static final int LIST 						= 129;	// Map of the string names for a type to a number along with a long description of the data type.  
	public static final int LISTSINGLE 					= 130;	// Map of a single type to a number (for late additions to server) along with its description.  
	public static final int LISTARGRET 					= 131;	// Map of the number to their arguments and returns descriptions.  
	public static final int LISTARGRETSINGLE 			= 132;	// Map of a single type to a number (for late additions to server) along with its argument and return descriptions.  
	public static final int LISTGROUPANDFLAGS 			= 133;	// Map of the number to their command groups and data flags.  
	public static final int LISTGROUPANDFLAGSSINGLE 	= 134;	// Map of a single type to a number (for late additions to server) along with its command group and data flags.
	
	public ServerCommandConstants() {
		//
	}
}
