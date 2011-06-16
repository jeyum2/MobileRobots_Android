package kr.ac.uos.je.view.impl;

import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
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
	@Override
	public void draw(Application app) {
//		if(color == null || objectType.isColorChanged()){
//			color = objectType.getColor();
//		}
		if(mMapManager.getMapStatus() == EMapManager.MapStatus.LoadingComplete && objectType.isVisible()){
			robotX = objectType.getVertices()[0];
			robotY = objectType.getVertices()[1];
			GL10 gl = app.getGraphics().getGL10();
			gl.glLoadIdentity();
			gl.glPushMatrix();
			
			spriteBatch.draw(robotTexture, 
					  robotX, robotY, 
					  0, 0,robotTexture.getWidth() , robotTexture.getHeight());		
			
			gl.glPopMatrix();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public EObjectType getObjectType() {
		return this.objectType;
	}

	private Texture robotTexture;
	private SpriteBatch spriteBatch;
	@Override
	public void update(Application app, SpriteBatch spriteBatch) {
		if(robotTexture == null){
			this.robotTexture = new Texture(Gdx.files.internal("data/robotcursor.png"));
		}
		if(this.spriteBatch == null){
			this.spriteBatch = spriteBatch;
		}
		
	}
}
