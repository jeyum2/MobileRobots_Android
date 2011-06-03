package kr.ac.uos.je.view;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uos.je.controller.RobotControllerManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.interfaces.MapManager;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import kr.ac.uos.je.view.impl.Area;
import kr.ac.uos.je.view.impl.Goals;
import kr.ac.uos.je.view.impl.Line;
import kr.ac.uos.je.view.impl.Path;
import kr.ac.uos.je.view.impl.Point;
import kr.ac.uos.je.view.impl.RobotPosition;
import kr.ac.uos.je.view.impl.Sensor;
import kr.ac.uos.je.view.interfaces.MapObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLU;

public class MobileRobotsMainRenderer implements ApplicationListener {

	private ResourceManager mResourceManager;
	private MapManager		mMapManager;
	private RobotControllerManager	mRobotManager;
	private List<MapObject> mapObjectList; 
	public MobileRobotsMainRenderer(ResourceManager resourceManager, MapManager mapManager, RobotControllerManager robotManager) {
		this.mResourceManager = resourceManager;
		this.mMapManager = mapManager;
		this.mRobotManager = robotManager;
		
	}
	@Override
	public void create() {
		initMapObject();
		initRobotObject();
		
	}

	private Path mPath;
	private RobotPosition mRobotPosition;
	private Sensor mSensor;
	private void initRobotObject() {
		mPath = new Path(mResourceManager, mMapManager, EObjectType.PATH);
		mRobotPosition = new RobotPosition(mResourceManager, mMapManager, EObjectType.ROBOT_POSITION);
		mSensor = new Sensor(mResourceManager, mMapManager, EObjectType.SENSORS);
	}
	private void initMapObject() {
		this.mapObjectList = new ArrayList<MapObject>();
		mapObjectList.add(new Area(mResourceManager, mMapManager, EObjectType.FORBIDDEN_AREA));
		mapObjectList.add(new Line(mResourceManager, mMapManager, EObjectType.MAP_LINE));
		mapObjectList.add(new Point(mResourceManager, mMapManager, EObjectType.MAP_POINT));
		mapObjectList.add(new Line(mResourceManager, mMapManager, EObjectType.FORBIDDEN_LINE));
		mapObjectList.add(new Goals(mResourceManager, mMapManager, EObjectType.GOALS));
	}
	@Override
	public void render() {
		drawMapObject();
		drawRobotObject();
	}


	
	private void drawRobotObject() {
		Application app =  Gdx.app;
		mPath.draw(app);
		mRobotPosition.draw(app);
		mSensor.draw(app);
		
	}
	private void drawMapObject() {
		for (MapObject map : mapObjectList) {
			if(map.getObjectType().isVisible())map.draw(Gdx.app);
		}
	}


	private int width;
	private int height;
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		GL10 gl = Gdx.graphics.getGL10();
		GLU glu = Gdx.graphics.getGLU(); 
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(gl, 0, 0, width, height);
		  //Projection style by [glOrtho or gluPerspective]
		  //Camera setting by gluLookAt
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {
		for (MapObject map : mapObjectList) {
			map.dispose();
		}
	}

}
