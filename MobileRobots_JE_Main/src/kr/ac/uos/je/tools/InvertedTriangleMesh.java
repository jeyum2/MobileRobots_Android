package kr.ac.uos.je.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class InvertedTriangleMesh {
	
	private static InvertedTriangleMesh INSTANCE;
	private Mesh mesh; 
	
	private InvertedTriangleMesh(){
		mesh = new Mesh(true, 3, 3, 
	            new VertexAttribute(Usage.Position, 3, "a_position"),
	            new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		
		mesh.setVertices(new float[] {
	            -5.0f, 10.0f, 0, Color.toFloatBits(128, 0, 0, 255),
	            5.0f, 10.0f, 0, Color.toFloatBits(192, 0, 0, 255),
	            0.0f, 0.0f, 0, Color.toFloatBits(192, 0, 0, 255) });   
	    mesh.setIndices(new short[] { 0, 1, 2});
		
	}
	
	public static InvertedTriangleMesh getMesh(){
		if(INSTANCE == null) INSTANCE = new InvertedTriangleMesh();
		return INSTANCE;
	}
	public void dispose(){
		mesh.dispose();
	}
	public void render(){
		mesh.render(GL10.GL_TRIANGLES, 0, 3);
	}
	
}
