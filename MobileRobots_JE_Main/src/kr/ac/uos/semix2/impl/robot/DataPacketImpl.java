package kr.ac.uos.semix2.impl.robot;

import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.Parameter;

public class DataPacketImpl implements DataPacket {
	private final byte[]			_packet;
	
	public DataPacketImpl(byte[] packet) {
		this(packet, packet.length);
	}
	
	public DataPacketImpl(byte[] packet, int length) {
		
		/**
		 * _packet = Arrays.copyOf(packet, length);
		 * Arrays.copyof method doesn't supported at android sdk
		 */
		
		_packet = new byte[length];
		for (int i = 0; i < length; i++) {
			_packet[i] = packet[i];
		}

	}
	
	public DataPacketImpl(int commandId) {
		this(commandId, null);
	}
	
	public DataPacketImpl(int commandId, Parameter parameter) {
		int length = DataPacket.HEADER_LENGTH + ((parameter != null) ? parameter.getLength() : 0) + DataPacket.FOOTER_LENGTH;
		_packet = new byte[length];
		_packet[0] = 0xF;
		_packet[1] = 0xC;
		_packet[2] = (byte)(length & 0xff);
		_packet[3] = (byte)((length >> 8) & 0xff);
		_packet[4] = (byte)(commandId & 0xff);
		_packet[5] = (byte)((commandId >> 8) & 0xff);
		if (parameter != null) {
			byte[] paramBytes = parameter.getBytes();
			for (int i=0, j=6, n=paramBytes.length; i<n; i++, j++) {
				_packet[j] = paramBytes[i];
			}
		}
		int checkSum = CheckSumUtil.getCheckSum(_packet, _packet.length);
		_packet[length - 2] = (byte)((checkSum >> 8) & 0xff);
		_packet[length - 1] = (byte)(checkSum & 0xff);
	}
	
	public int getCommandId() {
		return  (_packet[4] & 0xff) | ((_packet[5] & 0xff) << 8);
	}
	
	public int getLength() {
		return _packet.length;
	}
	
	public byte[] getBytes() {
		return _packet.clone();
	}
	
	public DataPacketIterator getDataPacketIterator() {
		return new DataPacketIteratorImpl(_packet);
	}
}
