package kr.ac.uos.semix2.impl.robot;

public class ClientCommandConstants {
	public static final int SHUTDOWN 			= 1;   	// Closes the connection.  
	public static final int INTRODUCTION 		= 2; 	// Introduces the client to the server.  
	public static final int UDP_INTRODUCTION 	= 3; 	// Udp introduction of the client to the server.  
	public static final int UDP_CONFIRMATION 	= 4; 	// Confirmation Udp was received from server.  
	public static final int TCP_ONLY 			= 5; 	// Client tells server to only send TCP.  
	public static final int LIST 				= 128; 	// Lists the types that can be handled.  
	public static final int REQUEST 			= 129; 	// Requests packet of a certain type.  
	public static final int REQUESTSTOP 		= 130;	// Requests that the server stop sending the given type.
	
	private ClientCommandConstants() {
		//
	}
}
