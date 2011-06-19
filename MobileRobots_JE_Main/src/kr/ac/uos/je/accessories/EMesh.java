package kr.ac.uos.je.accessories;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public enum EMesh {

	InvertedTriangleMesh(
			new Mesh(true, 3, 3, new VertexAttribute(Usage.Position, 3, "a_position")),
			new float[] {
				-5.0f, 10.0f, 0,
	            5.0f, 10.0f, 0, 
	            0.0f, 0.0f, 0 },
            new short[] { 0, 1, 2},
			GL10.GL_TRIANGLES);
	
	private Mesh mesh; 
	private final int drawType;
	private EMesh(Mesh mesh, float[] vertices, short[] indices, int drawType){
		this.drawType = drawType;
		this.mesh = mesh;
		this.mesh.setVertices(vertices);
		this.mesh.setIndices(indices);
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
