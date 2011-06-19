package kr.ac.uos.je.accessories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public enum EMesh {

	InvertedTriangleMesh(GL10.GL_TRIANGLES);
	
	private Mesh mesh; 
	private final int drawType;
	private EMesh(int drawType){
		this.drawType = drawType;
		
		mesh = new Mesh(true, 3, 3, 
	            new VertexAttribute(Usage.Position, 3, "a_position")
//	            ,new VertexAttribute(Usage.ColorPacked, 4, "a_color")
	            );
		
		mesh.setVertices(new float[] {
	            -5.0f, 10.0f, 0,
//	            Color.toFloatBits(128, 0, 0, 255),
	            5.0f, 10.0f, 0, 
//	            Color.toFloatBits(192, 0, 0, 255),
	            0.0f, 0.0f, 0 
//	            ,Color.toFloatBits(192, 0, 0, 255)
	            });   
	    mesh.setIndices(new short[] { 0, 1, 2});
		
	}
	
	
	public static void dispose(){
		for (EMesh e : EMesh.values()) {
			e.disposeMesh();
		}
	}
	
	public void disposeMesh(){
		mesh.dispose();
		
	}
	public void render(){
		mesh.render(drawType, 0, 3);
	}
	
}
