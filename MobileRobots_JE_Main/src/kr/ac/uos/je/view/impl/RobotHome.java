package kr.ac.uos.je.view.impl;

import java.nio.FloatBuffer;
import java.util.List;

import kr.ac.uos.je.accessories.OpenGLUtils;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.EObjectType.SubObject;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RobotHome implements DrawObject {
	private final EObjectType objectType;
	private EMapManager mMapManager;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private Texture goalTexture;

	public RobotHome(EMapManager mMapManager, EObjectType objectType) {
		this.mMapManager = mMapManager;
		this.objectType = objectType;
	}


	private float[] color;
	@Override
	public void draw(Application app) {
		
		if(color == null || objectType.isColorChanged()){
			color = objectType.getColor();
//			font.setColor(color[0], color[1], color[2], color[3]);
		}
		
		if(mMapManager.getMapStatus() == EMapManager.MapStatus.LoadingComplete && objectType.isVisible()){
			GL10 gl = app.getGraphics().getGL10();
			gl.glLoadIdentity();
			
			gl.glPushMatrix();
			font.setColor(Color.BLACK);
			font.draw(spriteBatch, objectType.name(), objectType.getVertices()[0]+goalTexture.getWidth(), objectType.getVertices()[1]+goalTexture.getHeight()*2);
			spriteBatch.draw(goalTexture, 
					objectType.getVertices()[0], objectType.getVertices()[1], 
					  0, 0,goalTexture.getWidth() , goalTexture.getHeight());		
			gl.glPopMatrix();
		}
		
	}

	@Override
	public void dispose() {
		font.dispose();
	}
	
	@Override
	public EObjectType getObjectType() {
		return this.objectType;
	}

	private float zoomRate;
	private static final int MIN_SCALE = 15;
	@Override
	public void update(Application app, SpriteBatch spriteBatch) {
		if(font == null){
			this.font = new BitmapFont();
		}
		if(goalTexture == null){
			this.goalTexture = new Texture(Gdx.files.internal("data/goal.png"));
		}
		if(this.spriteBatch == null){
			this.spriteBatch = spriteBatch;
		}
		float newZoomRate = EMapManager.MapManager.getZoomRate();
		if(newZoomRate < MIN_SCALE) newZoomRate = MIN_SCALE;
		if(zoomRate != newZoomRate){
			font.setScale(newZoomRate);
			this.zoomRate = newZoomRate;
		}
		
		
	}
}
