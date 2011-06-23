package kr.ac.uos.je.screens;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import kr.ac.uos.je.accessories.OpenGLUtils;
import kr.ac.uos.je.controller.ERobotMode;
import kr.ac.uos.je.controller.RobotCommuniator;
import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.exceptions.RobotControllerException;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.view.ManualDriveRenderer;
import kr.ac.uos.je.view.impl.Area;
import kr.ac.uos.je.view.impl.Goal;
import kr.ac.uos.je.view.impl.Line;
import kr.ac.uos.je.view.impl.Path;
import kr.ac.uos.je.view.impl.Point;
import kr.ac.uos.je.view.impl.RobotHome;
import kr.ac.uos.je.view.impl.RobotPosition;
import kr.ac.uos.je.view.impl.Sensor;
import kr.ac.uos.je.view.interfaces.DrawObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class MainScreen implements Screen {
	
	private EMapManager		mMapManager;
	private List<DrawObject> objectList;
	private AndroidAdaptor mAndroidAdaptor;
	private ManualDriveRenderer manualDriveRenderer;
	public MainScreen(Application app, AndroidAdaptor mAndroidAdaptor) {
		this.mAndroidAdaptor = mAndroidAdaptor;
		objectSpriteBatch = new SpriteBatch();
		menuSpriteBatch = new SpriteBatch();
		manualDriveRenderer = new ManualDriveRenderer();
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
		
		objectList.add(new Line(mMapManager, EObjectType.MAP_LINE));
		objectList.add(new Point(mMapManager, EObjectType.MAP_POINT));
		objectList.add(new Area(mMapManager, EObjectType.FORBIDDEN_AREA));
		objectList.add(new Line(mMapManager, EObjectType.FORBIDDEN_LINE));
		
		objectList.add(new RobotHome(mMapManager, EObjectType.ROBOT_HOME));
		objectList.add(new Goal(mMapManager, EObjectType.GOALS));
		
		
		objectList.add(new RobotPosition(mMapManager, EObjectType.ROBOT_POSITION));
		objectList.add(new Path(mMapManager, EObjectType.PATH));
		objectList.add(new Sensor(mMapManager, EObjectType.SENSORS));
	}

	private int screenWidth;
	private int screenHeight;
	private float zoomRate;
	@Override
	public void update(Application app) {
		this.screenWidth = app.getGraphics().getWidth();
		this.screenHeight = app.getGraphics().getHeight();
		
		if(ERobotMode.CenterOnRobotMode.isEnable() || ERobotMode.ManualDriveMode.isEnable()){
			centerX = (int) EObjectType.ROBOT_POSITION.getVertices()[0];
			centerY = (int) EObjectType.ROBOT_POSITION.getVertices()[1];
		}
		
		this.zoomRate = EMapManager.MapManager.getZoomRate();
		if(ERobotMode.ManualDriveMode.isEnable()){
			this.zoomRate = 10;
		}
		int width = (int)(screenWidth*zoomRate);
		int height = (int) (screenHeight*zoomRate);
		objectSpriteBatch.getProjectionMatrix().setToOrtho2D(centerX-width/2, centerY-height/2, width, height);
		menuSpriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, app.getGraphics().getWidth(), app.getGraphics().getHeight());
		
		
		inputHandling(app.getInput());
	}
	private int previousX1;
	private int previousX2;
	private int previousY1;
	private int previousY2;
	private int manualDirectionX;
	private int manualDirectionY;
	private enum ETouchMode{UP, TOUCHED_FIRST_FINGER, TOUCHED_SECOND_FINGER };
	private ETouchMode currentMode;
	private void inputHandling(Input input) {
		
		if(!ERobotMode.ManualDriveMode.isEnable() && !ERobotMode.CenterOnRobotMode.isEnable()){
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
//					if(Math.abs(dDistance)>2){
						mAndroidAdaptor.printLog("TOUCH_TEST", "Zoom");
						mMapManager.setZoomRate(dDistance);
						previousX1 = x1;
						previousX2 = x2;
						previousY1 = y1;
						previousY2 = y2;
//					}
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
		}else if(ERobotMode.ManualDriveMode.isEnable()){
			//UP -> Stop
			if(!input.isTouched()){
				try {
					RobotCommuniator.getRobotClient().forceStop();
				} catch (RobotControllerException e) {
					e.printStackTrace();
				}
				manualDirectionX = -1;
				manualDirectionY = -1;
			}else{
				manualDirectionX = input.getX(0);
//				manualDirectionY = Gdx.graphics.getHeight() - input.getY(0);
				manualDirectionY = input.getY(0);
				int screenCenterX = Gdx.graphics.getWidth()/2;
				int screenCenterY = Gdx.graphics.getHeight()/2;
				double length = Math.sqrt((manualDirectionX-screenCenterX)^2 +(manualDirectionY-screenCenterY)^2); 
				double angle = Math.acos((manualDirectionX-screenCenterX)/length);
				System.out.println("("Length : "+length);
//				if(length < THROTTLE_MIN_BOUNDARY){
//					System.out.println("In");
//					manualDirectionX = -1;
//					manualDirectionY = -1;
//				}
//				if(length > THROTTLE_MAX_BOUNDARY){
//					System.out.println("Out");
//					manualDirectionX = (int) (screenCenterX+Math.cos(angle)*THROTTLE_MAX_BOUNDARY);
//					manualDirectionY = (int) (screenCenterX+Math.sin(angle)*THROTTLE_MAX_BOUNDARY);
//				}
					
			}
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
		drawRobotController();

	}
	private static final int THROTTLE_MAX_BOUNDARY = 200;
	private static final int THROTTLE_MIN_BOUNDARY = 50;
	private void drawRobotController() {
		if(ERobotMode.ManualDriveMode.isEnable()){
			menuSpriteBatch.begin();
			manualDriveRenderer.update(Gdx.app, menuSpriteBatch, manualDirectionX, manualDirectionY,THROTTLE_MIN_BOUNDARY, THROTTLE_MAX_BOUNDARY);
			manualDriveRenderer.draw(Gdx.app);
			menuSpriteBatch.end();
		}
		
	}
	private SpriteBatch objectSpriteBatch;
	private SpriteBatch menuSpriteBatch;
	private void drawMapObject() {
		objectSpriteBatch.setColor(Color.WHITE);
		objectSpriteBatch.begin();
		for (DrawObject map : objectList) {
			if(map.getObjectType().isVisible()){
				map.update(Gdx.app, objectSpriteBatch);
				map.draw(Gdx.app);
			}
		}
		objectSpriteBatch.end();
		
	}
	

	@Override
	public void dispose() {
		for (DrawObject map : objectList) {
			map.dispose();
		}
		objectSpriteBatch.dispose();
		menuSpriteBatch.dispose();
		manualDriveRenderer.dispose();
		//TODO Dispose RbotObject
	}
}
