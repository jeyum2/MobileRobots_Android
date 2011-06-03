package kr.ac.uos.semix2.impl.robot;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;

public class TCPPacketReader implements Runnable {
	private final InputStream				_in;
	private final DataPacketHandler			_handler;
	private final byte[]					_readBuffer;
	private final Logger					_logger;
	
	public TCPPacketReader(InputStream in, DataPacketHandler handler) {
		_in 			= in;
		_handler		= handler;
		_readBuffer		= new byte[32020];
		_logger			= Logger.getLogger(TCPPacketReader.class.getName());
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			DataPacket packet = null;
			try {
				packet = readPacket();
			} catch(IOException ignore) {}
			if (packet != null) {
				_handler.handleDataPacket(packet);
			} 
		}
	}
	
	private DataPacket readPacket() throws IOException {
		/*
		 * SYNC1, SYNC2 
		 */
		
		if (_in.read() != 0xF) {
			return null;
		}
		if (_in.read() != 0xC) {
			return null;
		}
		
		/*
		 * Packet length
		 */
		
		byte length1 = (byte)_in.read();
		byte length2 = (byte)_in.read();

		int length = (length1 & 0xff) | ((length2 & 0xff) << 8);
		if (length < DataPacket.HEADER_LENGTH + DataPacket.FOOTER_LENGTH) {
			_logger.log(Level.WARNING, "err length");
			return null; // bad_packet
		}
		
		/*
		 * Command
		 */
		
		byte command1 = (byte)_in.read();
		byte command2 = (byte)_in.read();
		
		/*
		 * Read packet bytes
		 */
		
		_readBuffer[0] = 0xF;
		_readBuffer[1] = 0xC;
		_readBuffer[2] = length1;
		_readBuffer[3] = length2;
		_readBuffer[4] = command1;
		_readBuffer[5] = command2;

		int readLength = length - (DataPacket.HEADER_LENGTH + DataPacket.FOOTER_LENGTH);
		if (readLength > 0) {
			int readIndex = 0;
			while(readIndex < readLength) {
				int n = _in.read(_readBuffer, DataPacket.HEADER_LENGTH + readIndex, readLength - readIndex);
				if (n <= 0) {
					_logger.log(Level.WARNING, "failed read");
					return null;
				}
				readIndex += n;
			}
		}
		
		/*
		 * CheckSum
		 */
		
		byte checkSum1 = (byte)_in.read();
		byte checkSum2 = (byte)_in.read();

		int checkSum = ((checkSum1 & 0xff) << 8) | (checkSum2 & 0xff);
		if (checkSum != CheckSumUtil.getCheckSum(_readBuffer, length)) {
			_logger.log(Level.WARNING, "err checksum");
			return null;
		}
		
		return new DataPacketImpl(_readBuffer, length);
	}
}
