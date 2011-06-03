package kr.ac.uos.semix2.robot;

public interface DataPacketIterator {
	public boolean hasNext(int readCount);
	public int nextByte();
	public int nextByte2();
	public int nextUByte2();
	public int nextByte4();
	public long nextUByte4();
	public double nextDouble();
	public String nextString();
}
