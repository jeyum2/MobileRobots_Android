package kr.ac.uos.semix2.impl.robot;

public class CheckSumUtil {

	private CheckSumUtil() {
		//
	}
	
	public static final int getCheckSum(byte[] packet, int length) {
		int c = 0;
		int i = 3;
		int n = length - 4;
		while(n > 3) {
			c += ((packet[i] & 0xff) << 8) | (packet[i+1] & 0xff);
			c = c & 0xffff;
			n -= 2;
			i += 2;
		}
		if (n > 0) {
			c = c ^ (packet[i] & 0xff);
		}
		return c;
	}
}
