package kr.ac.uos.je.view.impl;

import java.nio.FloatBuffer;
import java.util.Set;

import kr.ac.uos.je.accessories.EMesh;
import kr.ac.uos.je.accessories.OpenGLUtils;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.EObjectType.SubObject;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sensor implements DrawObject {
	private final EObjectType objectType;
	private EMapManager mMapManager;

	public Sensor(EMapManager mMapManager, EObjectType objectType) {
		this.mMapManager = mMapManager;
		this.objectType = objectType;
	}


	private float[] color;
	@Override
	public void draw(Application app) {
		if(mMapManager.getMapStatus() == EMapManager.MapStatus.LoadingComplete){
			if(color == null || objectType.isColorChanged()){
				color = objectType.getColor();
			}
		
			if(objectType.isVisible()){

				GL10 gl = app.getGraphics().getGL10();
				Set<SubObject> sensorSet = EObjectType.SENSORS.getSubObjects();
				gl.glLoadIdentity();
				gl.glPushMatrix();{
					for(SubObject sensor : sensorSet){
						float[] pointVertices = sensor.getVertices();
						System.out.println(sensor.getName());
						if(pointVertices != null && pointVertices.length > 2){
							gl.glPushMatrix();
							FloatBuffer pointVertexBuffer = OpenGLUtils.arrayToFloatBuffer(pointVertices);
							if(sensor.getName().equalsIgnoreCase("Sonar")){
								gl.glPointSize(5.0f);
								gl.glColor4f(0.0f,0.0f,1.0f,1.0f);
							}else{
								gl.glPointSize(2.0f);
								gl.glColor4f(color[0],color[1],color[2],color[3]);
							}
							//Point to our vertex buffer
							gl.glVertexPointer(3, GL10.GL_FLOAT, 0, pointVertexBuffer);
							//Enable vertex buffer
							gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
							
							gl.glDrawArrays(GL10.GL_POINTS, 0, pointVertices.length / 3);
							//Disable the client state before leaving
							gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
							gl.glPointSize(1.0f);
							gl.glPopMatrix();
						}
					}
				}gl.glPopMatrix();
			}
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
	@Override
	public void update(Application app, SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
		
	}
	
}
