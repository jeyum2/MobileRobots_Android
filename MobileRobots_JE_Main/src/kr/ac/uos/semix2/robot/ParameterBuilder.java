package kr.ac.uos.semix2.robot;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uos.semix2.impl.robot.ParameterImpl;

public class ParameterBuilder {
	private final List<Byte>		_parameter;

	public ParameterBuilder() {
		_parameter = new ArrayList<Byte>();
	}
	
	public void appendByte(int value) {
		_parameter.add((byte)(value & 0xff));
	}
	
	public void appendByte2(int value) {
		appendUByte2(value);
	}
	
	public void appendUByte2(int value) {
		_parameter.add((byte)(value & 0xff));
		_parameter.add((byte)((value >> 8) & 0xff));
	}
	
	public void appendByte4(int value) {
		appendUByte4(value);
	}
	
	public void appendUByte4(long value) {
		_parameter.add((byte)(value & 0xff));
		_parameter.add((byte)((value >> 8) & 0xff));
		_parameter.add((byte)((value >> 16) & 0xff));
		_parameter.add((byte)((value >> 24) & 0xff));
	}
	
	public void appendDouble(double value) {
		appendString(String.format("%g", value));
	}

	public void appendString(String value) {
		if (value == null) {
			value = "";
		}
		for (byte b : value.getBytes()) {
			appendByte(b);
		}
		appendByte(0);
	}
	
	public Parameter toParameter() {
		byte[] bytes = new byte[_parameter.size()];
		for (int i=0, n=_parameter.size(); i<n; i++) {
			bytes[i] = _parameter.get(i);
		}
		return new ParameterImpl(bytes);
	}
}
