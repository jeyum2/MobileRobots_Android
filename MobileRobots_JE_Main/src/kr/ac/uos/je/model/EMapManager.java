package kr.ac.uos.je.model;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.model.EObjectType.SubObject;
import kr.ac.uos.je.tools.EMapFactory;
import kr.ac.uos.semix2.robot.DataPacket;

public enum EMapManager {
	MapManager;
	
	public enum MapStatus{Empty, LoadingComplete, SomethingChanged};


	private MapStatus mMapStatus;
	private EMapManager() {
		mMapStatus = MapStatus.Empty;
		mapLineList = new ArrayList<Integer>();
		mapPointList = new ArrayList<Integer>();
		forbiddenLineList = new ArrayList<Integer>();
		forbiddenAreaList = new ArrayList<Integer>();
	}
	public MapStatus getMapStatus() {
		return mMapStatus;
	}

	public void setMapStatus(MapStatus mapStatus) {
		this.mMapStatus = mapStatus;
		if(mapStatus == MapStatus.LoadingComplete){
			createMap();
		}
	}

	private int mapWidth;
	private int mapHeight;
	private float MAX_ZOOM_RATE = -1;
	private void createMap() {
		EObjectType.GOALS.sortSubObjectByname();
		
		EObjectType.MAP_LINE.setVertices(EMapFactory.integerListToFloatArray(mapLineList));
		EObjectType.MAP_POINT.setVertices(EMapFactory.integerListToFloatArray(mapPointList));
		EObjectType.FORBIDDEN_LINE.setVertices(EMapFactory.integerListToFloatArray(forbiddenLineList));
		EObjectType.FORBIDDEN_AREA.setVertices(EMapFactory.integerListToFloatArray(forbiddenAreaList));
		
		setDefaultZoomRate();
	}
	
//	private int normMapSize;
//	private int normScreenSize;
	private void setDefaultZoomRate(){
		this.mapWidth = Math.abs(maxPos[0] - minPos[0]);
		this.mapHeight = Math.abs(maxPos[1] - minPos[1]);

		
		
		if(isScreenDataInitilized){
			System.out.println("ScreenData Initialized");
			//TODO map < screenHeight case!! 
			int heightScale = (mapHeight / screenHeight);
			int widthScale = (mapWidth / screenWidth);
			this.zoomRate = (widthScale > heightScale)? widthScale * 1.5f : heightScale * 1.5f;
			this.MAX_ZOOM_RATE  = this.zoomRate;
		}else{
			this.zoomRate = 100;
		}
	}
	public void updateMap(DataPacket packet) {
		EMapFactory.MapFactory.updateMap(this, packet);
	}

	
	private List<Integer> mapLineList;
	public void addMapLine(int x1, int y1, int z1, int x2, int y2, int z2) {
		mapLineList.add(x1);
		mapLineList.add(y1);
		mapLineList.add(z1);
		mapLineList.add(x2);
		mapLineList.add(y2);
		mapLineList.add(z2);
	}
	private List<Integer> mapPointList;
	public void addMapPoint(int x, int y, int z) {
		mapPointList.add(x);
		mapPointList.add(y);
		mapPointList.add(z);
	}
	private int[] minPos;
	public int[] getMinPos() {
		return minPos.clone();
	}
	public void setMinPos(int x, int y) {
		minPos = new int[]{x,y};
	}
	private int[] maxPos;
	public int[] getMaxPos() {
		return maxPos.clone();
	}
	public void setMaxPos(int x, int y) {
		maxPos = new int[]{x,y};
	}
	
	private int resolution;
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	
	public void setRobotHome(int x, int y, String description, String iconName,
			String name) {
		EObjectType.ROBOT_HOME.setVertices(new float[]{x,y});
		EObjectType.ROBOT_HOME.setDescription(description);
		EObjectType.ROBOT_HOME.setIconName(iconName);
		EObjectType.ROBOT_HOME.setObjectName(name);
		
	}
	public void addGoal(int x, int y, String description, String iconName,
			String name, boolean withHeading) {
		EObjectType.GOALS.addSubObject(new SubObject(x, y, description, iconName, name, withHeading));
		
	}
	
	private List<Integer> forbiddenLineList;
	public void addForbiddenLine(int x1, int y1, int x2, int y2,
			String description, String iconName, String name) {
		forbiddenLineList.add(x1);
		forbiddenLineList.add(y1);
		forbiddenLineList.add(0);
		forbiddenLineList.add(x2);
		forbiddenLineList.add(y2);
		forbiddenLineList.add(0);
	}
	private List<Integer> forbiddenAreaList;
	public void addForbiddenArea(int x1, int y1, int x2, int y2,
			String description, String iconName, String name) {
		forbiddenAreaList.add(x1);
		forbiddenAreaList.add(y1);
		forbiddenAreaList.add(0);
		
		forbiddenAreaList.add(x2);
		forbiddenAreaList.add(y1);
		forbiddenAreaList.add(0);
		
		forbiddenAreaList.add(x2);
		forbiddenAreaList.add(y2);
		forbiddenAreaList.add(0);
		
		forbiddenAreaList.add(x1);
		forbiddenAreaList.add(y2);
		forbiddenAreaList.add(0);
	}
	
	private float zoomRate;
	private static final float MIN_ZOOM_RATE = 5;
	public void setZoomRate(float zoomRate){
		zoomRate += this.zoomRate;
		
		if(MAX_ZOOM_RATE > 0 && zoomRate > MAX_ZOOM_RATE){
			zoomRate = MAX_ZOOM_RATE;
		}
		
		if(zoomRate <= MIN_ZOOM_RATE){
			this.zoomRate = MIN_ZOOM_RATE;
		}else{
			this.zoomRate = zoomRate;
		}
		
	}
	public float getZoomRate() {
		return this.zoomRate;
	}
	private int screenWidth;
	private int screenHeight;
	private boolean isScreenDataInitilized = false;
	public void setScreenSize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.isScreenDataInitilized = true;
	}
	public String[] getGoalList(){
		EObjectType.GOALS.getObjectName();
//		for ( iterable_element : iterable) {
//			
//		}
		return null;
	}
	/**
	 * TODO Size calculate
	 */
	public static final float ROBOT_SIZE = 1000;
	public float getObjectSize() {
		return ROBOT_SIZE;
	}
}
