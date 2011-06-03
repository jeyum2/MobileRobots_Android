package kr.ac.uos.semix2.robot;

public interface RobotClient {
	public static final String 		DEFAULT_HOST 	= "localhost";
	public static final int 		DEFAULT_PORT 	= 7272; 
	
	public void setUser(String user);
	public void setPassword(String password);
	public void setServerKey(String serverKey);
	public void setHost(String host);
	public void setPort(int port);
	
	public boolean connect();
	public void disconnect();

	public byte[] getClientKey();
	public Command getCommand(String name);
	public Command getCommand(int commandId);
	public Command[] getAllCommands();
	
	public void addDataPacketHandler(Command command, DataPacketHandler handler);
	public void addDataPacketHandler(int commandId, DataPacketHandler handler);
	public void removeDataPacketHandler(Command command, DataPacketHandler handler);
	public void removeDataPacketHandler(int commandId, DataPacketHandler handler);
	
	public boolean request(Command command);
	public boolean request(int commandId);
	public boolean request(Command command, int mSec);
	public boolean request(int commandId, int mSec);
	public boolean request(Command command, Parameter parameter);
	public boolean request(int commandId, Parameter parameter);
	public boolean request(Command command, Parameter parameter, int mSec);
	public boolean request(int commandId, Parameter parameter, int mSec);
	public boolean stop(Command command);
	public boolean stop(int commandId);
}
