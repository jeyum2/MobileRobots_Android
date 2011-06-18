package kr.ac.uos.je.screens;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uos.je.controller.RobotCommuniator;
import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.tools.OpenGLUtils;
import kr.ac.uos.je.view.impl.Area;
import kr.ac.uos.je.view.impl.Goals;
import kr.ac.uos.je.view.impl.Line;
import kr.ac.uos.je.view.impl.Path;
import kr.ac.uos.je.view.impl.Point;
import kr.ac.uos.je.view.impl.RobotPosition;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreen implements Screen {
	private EMapManager		mMapManager;
	private RobotCommuniator	mRobotManager;
	private List<DrawObject> objectList;
	private AndroidAdaptor mAndroidAdaptor;
	
	public MainScreen(Application app, AndroidAdaptor mAndroidAdaptor) {
		this.mAndroidAdaptor = mAndroidAdaptor;
		spriteBatch = new SpriteBatch();
		this.mMapManager = EMapManager.MapManager;
		currentMode = ETouchMode.UP;
		initMapObject();
		this.centerX = 0;
		this.centerY = 0;
//		initRobotObject();
	}
	private void initMapObject() {
		//Be careful spriteBatch, texture drawing has some bug.
		this.objectList = new ArrayList<DrawObject>();
		
		objectList.add(new Path(mMapManager, EObjectType.PATH));
		objectList.add(new RobotPosition(mMapManager, EObjectType.ROBOT_POSITION));
		
		
		objectList.add(new Line(mMapManager, EObjectType.MAP_LINE));
		objectList.add(new Point(mMapManager, EObjectType.MAP_POINT));
		objectList.add(new Area(mMapManager, EObjectType.FORBIDDEN_AREA));
		objectList.add(new Line(mMapManager, EObjectType.FORBIDDEN_LINE));
		objectList.add(new Goals(mMapManager, EObjectType.GOALS));
		
		
		
	}
	/**
	 * private Path mPath;
	private RobotPosition mRobotPosition;
	private Sensor mSensor;
	private void initRobotObject() {
		mPath = new Path(mResourceManager, mMapManager, EObjectType.PATH);
		mRobotPosition = new RobotPosition(mResourceManager, mMapManager, EObjectType.ROBOT_POSITION);
		mSensor = new Sensor(mResourceManager, mMapManager, EObjectType.SENSORS);
	}
	 */

	private int screenWidth;
	private int screenHeight;
	private float zoomRate;
	@Override
	public void update(Application app) {
		
		this.screenWidth = app.getGraphics().getWidth();
		this.screenHeight = app.getGraphics().getHeight();
		this.zoomRate = EMapManager.MapManager.getZoomRate();
		int width = (int)(screenWidth*zoomRate);
//		if(width <= screenWidth) width = screenWidth;
		int height = (int) (screenHeight*zoomRate);
//		if(height <= screenHeight) height = screenHeight;
		spriteBatch.getProjectionMatrix().setToOrtho2D(centerX-width/2, centerY-height/2, width, height);
		
		
		
		inputHandling(app.getInput());
	}
	private enum ECurrentTouchMode{ NONE, MAP_SCROLL, MANUAL_DRIVE};
	private int previousX1;
	private int previousX2;
	private int previousY1;
	private int previousY2;
	private enum ETouchMode{UP, TOUCHED_FIRST_FINGER, TOUCHED_SECOND_FINGER };
	private ETouchMode currentMode;
	private void inputHandling(Input input) {
		switch (currentMode) {
		case UP:
			if(input.isTouched(1)){
				mAndroidAdaptor.printLog("TOUCH_TEST", "UP->DOUBLE TOUCH");
				currentMode = ETouchMode.TOUCHED_SECOND_FINGER;
				previousX1 = input.getX(0);
				previousY1 = input.getY(0);
				previousX2 = input.getX(1);
				previousY2 = input.getY(1);
			}else if(input.isTouched(0)){
				mAndroidAdaptor.printLog("TOUCH_TEST", "UP->SINGLE TOUCH");
				currentMode = ETouchMode.TOUCHED_FIRST_FINGER;
				previousX1 = input.getX();
				previousY1 = input.getY();
				previousX2 = -1;
				previousY2 = -1;
			}
			break;
		case TOUCHED_FIRST_FINGER:
			if(input.isTouched(1)){
				mAndroidAdaptor.printLog("TOUCH_TEST", "SINGLE->DOUBLE TOUCH");
				currentMode = ETouchMode.TOUCHED_SECOND_FINGER;
				previousX2 = input.getX(1);
				previousY2 = input.getY(1);
			}else if(input.isTouched(0)){
				mAndroidAdaptor.printLog("TOUCH_TEST", "SINGLE DRAG");
				int x = input.getX();
				int y = input.getY();
				int dx = previousX1 - x;
				int dy = previousY1 - y;
				if(Math.abs(dx) > 5 || Math.abs(dy)> 5){ // because of human's vibration
					mAndroidAdaptor.printLog("TOUCH_TEST", "DRAG -> over threshold ");
					touchDrag(dx, -dy); //beacuse of coordinate system dy is inversed 
					previousX1 = x;
					previousY1 = y;
				}
			}else if(!input.isTouched()){
				currentMode = ETouchMode.UP;
				previousX1 = -1;
				previousX2 = -1;
				previousY1 = -1;
				previousY2 = -1;
			}
			break;
		case TOUCHED_SECOND_FINGER:
			if(input.isTouched(1)){
				mAndroidAdaptor.printLog("TOUCH_TEST", "ZOOM OR ROLL");
				int x1 = input.getX(0);
				int x2 = input.getX(1);
				int y1 = input.getY(0);
				int y2 = input.getY(1);
				int previousDistance = OpenGLUtils.calculateDistance(previousX1, previousY1, previousX2, previousY2);
				int currentDistance = OpenGLUtils.calculateDistance(input.getX(0), input.getY(0), input.getX(1), input.getY(1));
				int dDistance = (previousDistance - currentDistance)/5; 
//				if(Math.abs(dDistance)>2){
					mAndroidAdaptor.printLog("TOUCH_TEST", "Zoom");
					mMapManager.setZoomRate(dDistance);
					previousX1 = x1;
					previousX2 = x2;
					previousY1 = y1;
					previousY2 = y2;
//				}
				//TODO ROLL
			}else{
				mAndroidAdaptor.printLog("TOUCH_TEST", "DOUBLE -> RELEASE");
				//whatever finger released, change status to UP
				currentMode = ETouchMode.UP;
				previousX1 = -1;
				previousY1 = -1;
				previousX2 = -1;
				previousY2 = -1;
			}
			break;
		default:
			break;
		}
	}
	private int centerX;
	private int centerY;
	private void touchDrag(int dx, int dy) {
		centerX += dx*zoomRate;
		centerY += dy*zoomRate;
//		mAndroidAdaptor.printLog("TOUCH",String.format("Drag dx:%d, dy:%d",dx,dy));
		
	}

	@Override
	public void render(Application app) {
		GL10 gl = app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		drawMapObject();
//		drawRobotObject();

	}
	private SpriteBatch spriteBatch;
	private void drawMapObject() {
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.begin();
		for (DrawObject map : objectList) {
			if(map.getObjectType().isVisible()){
				map.update(Gdx.app, spriteBatch);
				map.draw(Gdx.app);
			}
		}
		spriteBatch.end();
	}


	@Override
	public void dispose() {
		for (DrawObject map : objectList) {
			map.dispose();
		}
		spriteBatch.dispose();
		//TODO Dispose RbotObject
		

	}
/**
 * 
	private void drawRobotObject() {
		Application app =  Gdx.app;
		mPath.draw(app);
		mRobotPosition.draw(app);
		mSensor.draw(app);
		
	}
	

 */
}
