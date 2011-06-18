package kr.ac.uos.je.view.impl;

import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RobotPosition implements DrawObject {
	private final EObjectType objectType;
	private EMapManager mMapManager;

	public RobotPosition(EMapManager mMapManager, EObjectType forbiddenArea) {
		this.mMapManager = mMapManager;
		this.objectType = forbiddenArea;
	}


//	private float[] color;
	private float robotX;
	private float robotY;
	private float robotTh;
	private float robotSize;
	@Override
	public void draw(Application app) {
//		if(color == null || objectType.isColorChanged()){
//			color = objectType.getColor();
//		}
		if(mMapManager.getMapStatus() == EMapManager.MapStatus.LoadingComplete && objectType.isVisible()){
			robotX = objectType.getVertices()[0];
			robotY = objectType.getVertices()[1];
			robotTh = objectType.getVertices()[2];
			GL10 gl = app.getGraphics().getGL10();
			gl.glLoadIdentity();
			gl.glPushMatrix();
			
			gl.glTranslatef(robotX, robotY, 0.0f);
			gl.glScalef(robotSize, robotSize, robotSize);
			gl.glRotatef(robotTh+90, 0, 0, 1.0f);
			Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
			robotTexture.bind();
		    robotMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
//			spriteBatch.draw(robotTexture, 
//					  robotX+robotTexture.getWidth(), robotY, 
//					  0, 0,robotTexture.getWidth() , robotTexture.getHeight());		
			
			gl.glPopMatrix();
		}
	}

	@Override
	public void dispose() {
		robotMesh.dispose();
		robotTexture.dispose();
	}
	@Override
	public EObjectType getObjectType() {
		return this.objectType;
	}

	private Texture robotTexture;
	private SpriteBatch spriteBatch;
	private Mesh robotMesh;
	@Override
	public void update(Application app, SpriteBatch spriteBatch) {
		robotSize = EMapManager.MapManager.getObjectSize();
		if(robotTexture == null){
			this.robotTexture = new Texture(Gdx.files.internal("data/robotcursor.png"));
		}
		if(this.spriteBatch == null){
			this.spriteBatch = spriteBatch;
		}
		if(this.robotMesh == null){
			
			robotMesh = new Mesh(true, 4, 4, 
                    new VertexAttribute(Usage.Position, 3, "a_position"),
                    new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
					new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

            robotMesh.setVertices(new float[] {
                    -0.5f, -0.5f, 0, Color.toFloatBits(255, 255, 255, 255), 0, 0,
                    0.5f, -0.5f, 0, Color.toFloatBits(255, 255, 255, 255),  1, 0,
                    -0.5f, 0.5f, 0, Color.toFloatBits(255, 255, 255, 255),  0, 1,
                    0.5f, 0.5f, 0, Color.toFloatBits(255, 255, 255, 255), 1, 1 });   
            robotMesh.setIndices(new short[] { 0, 1, 2, 3});
            
		}
		
	}
}
