package kr.ac.uos.je.screens;

import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;

import com.badlogic.gdx.Application;

public class MainScreen implements Screen {

	public MainScreen(Application app, AndroidAdaptor mAndroidAdaptor) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Application app) {
		// TODO Auto-generated method stub

	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
/**
 * 
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
		initTest();
		initMapObject();
		initRobotObject();
		
	}

	

	private SpriteBatch testBatch;
	private BitmapFont	font;
	private void initTest() {
		testBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
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
		mapObjectList.add(new Line(mResourceManager, mMapManager, EObjectType.MAP_LINE));
		mapObjectList.add(new Point(mResourceManager, mMapManager, EObjectType.MAP_POINT));
		mapObjectList.add(new Area(mResourceManager, mMapManager, EObjectType.FORBIDDEN_AREA));
		mapObjectList.add(new Line(mResourceManager, mMapManager, EObjectType.FORBIDDEN_LINE));
//		mapObjectList.add(new Goals(mResourceManager, mMapManager, EObjectType.GOALS));
	}
	@Override
	public void render() {
//		camera.update();
//		camera.projection.setToOrtho2D(0, 0, width*zoomRate, height*zoomRate);
//		camera.apply(Gdx.gl10);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		drawMapObject();
//		drawRobotObject();
	}
	
	

	private void drawRobotObject() {
		Application app =  Gdx.app;
		mPath.draw(app);
		mRobotPosition.draw(app);
		mSensor.draw(app);
		
	}
	private void drawMapObject() {
		testBatch.begin();
		font.setScale(10.0f, 10.0f);
		font.draw(testBatch, "Hello World!", 100,300);
		
		for (MapObject map : mapObjectList) {
			if(map.getObjectType().isVisible())map.draw(Gdx.app);
		}
			testBatch.end();
	}


	private int width;
	private int height;
//	private OrthographicCamera camera;
	private int zoomRate = 100; 
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
//		camera = new OrthographicCamera(width*zoomRate, height*zoomRate);
		testBatch.getProjectionMatrix().setToOrtho2D(-width*zoomRate/2, -height*zoomRate/2, width*zoomRate, height*zoomRate);
//		camera.projection.setToOrtho2D(0, 0, width*zoomRate, height*zoomRate);
		
//		System.out.println("resize");
//		GL10 gl = Gdx.graphics.getGL10();
//		GLU glu = Gdx.graphics.getGLU(); 
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glLoadIdentity();
//		glu.gluOrtho2D(gl, 0, 10000, 0, 10000);
//		  //Projection style by [glOrtho or gluPerspective]
//		  //Camera setting by gluLookAt
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//		gl.glLoadIdentity();
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

 */
}
