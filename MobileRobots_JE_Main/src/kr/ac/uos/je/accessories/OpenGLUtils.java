package kr.ac.uos.je.accessories;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.badlogic.gdx.math.Circle;

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
	public static float[] makeCircleArray(int numOfVertex){
		float[] circleArray = new float[numOfVertex*3];
		for (int i = 0; i < numOfVertex; i++) {
			double angle = 2* Math.PI * i / numOfVertex;
			float x = (float) Math.cos(angle);
			float y = (float) Math.sin(angle);
			circleArray[i*3] = x;
			circleArray[i*3+1] = y;
			circleArray[i*3+2] = 0.0f;
		}
		return circleArray;
	}
}
