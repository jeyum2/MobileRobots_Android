package kr.ac.uos.je.accessories;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OpenGLUtils {
	
	public static FloatBuffer arrayToFloatBuffer(float[] vertices){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		return vertexBuffer;
	}
	public static int calculateDistance(int x1, int y1, int x2, int y2){
		return (int)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
	}
}
