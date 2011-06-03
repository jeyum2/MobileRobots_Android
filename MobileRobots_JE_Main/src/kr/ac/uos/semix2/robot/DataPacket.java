package kr.ac.uos.semix2.robot;

public interface DataPacket {
	public static final int HEADER_LENGTH 	= 6;
	public static final int FOOTER_LENGTH 	= 2;

	public int getCommandId();
	public byte[] getBytes();
	public DataPacketIterator getDataPacketIterator();
}
