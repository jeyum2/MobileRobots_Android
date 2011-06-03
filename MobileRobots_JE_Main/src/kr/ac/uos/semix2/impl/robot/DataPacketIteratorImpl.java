package kr.ac.uos.semix2.impl.robot;

import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketIterator;

public class DataPacketIteratorImpl implements DataPacketIterator {
	private final byte[]			_packet;
	private int						_readIndex;

	public DataPacketIteratorImpl(byte[] packet) {
		_packet 	= packet.clone();
		_readIndex	= DataPacket.HEADER_LENGTH;
	}
	
	public boolean hasNext(int readCount) {
		return (_readIndex + readCount <= _packet.length - DataPacket.FOOTER_LENGTH);
	}

	public int nextByte() {
		byte value = 0;
		if (hasNext(1)) {
			value = _packet[_readIndex];
			_readIndex++;
		}
		return value;
	}
	
	public int nextByte2() {
		int value = 0;
		if (hasNext(2)) {
			byte value1 = _packet[_readIndex];
			byte value2 = _packet[_readIndex + 1];
			if ((value2 & 0x80) != 0) {
				value = (0xff << 24) | (0xff << 16) | ((value2 & 0xff) << 8) | (value1 & 0xff);
			} else {
				value = ((value2 & 0xff) << 8) | (value1 & 0xff);
			}
			_readIndex += 2;
		}
		return value;
	}
	
	public int nextUByte2() {
		int value = 0;
		if (hasNext(2)) {
			byte value1 = _packet[_readIndex];
			byte value2 = _packet[_readIndex + 1];
			value = (value1 & 0xff) | ((value2 & 0xff) << 8);
			_readIndex += 2;
		}
		return value;
	}
	
	public int nextByte4() {
		int value = 0;
		if (hasNext(4)) {
			byte value1 = _packet[_readIndex];
			byte value2 = _packet[_readIndex + 1];
			byte value3 = _packet[_readIndex + 2];
			byte value4 = _packet[_readIndex + 3];
			value = (value1 & 0xff) | ((value2 & 0xff) << 8) | ((value3 & 0xff) << 16) | ((value4 & 0xff) << 24);
			_readIndex += 4;
		}
		return value;
	}
	
	public long nextUByte4() {
		long value = 0;
		if (hasNext(4)) {
			byte value1 = _packet[_readIndex];
			byte value2 = _packet[_readIndex + 1];
			byte value3 = _packet[_readIndex + 2];
			byte value4 = _packet[_readIndex + 3];
			value = (value1 & 0xff) | ((value2 & 0xff) << 8) | ((value3 & 0xff) << 16) | ((value4 & 0xff) << 24);
			_readIndex += 4;
		}
		return value;
	}
	
	public double nextDouble() {
		return Double.valueOf(nextString());
	}
	
	public String nextString() {
		int stringLength = 0;
		while(hasNext(stringLength) && _packet[_readIndex + stringLength] != 0) {
			stringLength++;
		}
		byte[] stringBuffer = new byte[stringLength];
		
		
		int i = 0;
		while(hasNext(1) && _packet[_readIndex] != 0) {
			stringBuffer[i] = _packet[_readIndex];
			i++;
			_readIndex++;
		}
		if (_packet[_readIndex] == 0) {
			_readIndex++;
		}
		return new String(stringBuffer, 0, i);
	}
}
