package kr.ac.uos.je;

import java.util.ArrayList;
import java.util.List;
import android.R.integer;

import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.EObjectType.AdditionalMapObject;
import kr.ac.uos.je.model.interfaces.MapManager;
import kr.ac.uos.je.utils.EMapFactory;
import kr.ac.uos.semix2.robot.DataPacket;

public enum EMapManagerImpl implements MapManager{
	MapManager;


	private MapStatus mMapStatus;
	private EMapManagerImpl() {
		mMapStatus = MapStatus.Empty;
		mapLineList = new ArrayList<Integer>();
		mapPointList = new ArrayList<Integer>();
		forbiddenLineList = new ArrayList<Integer>();
		forbiddenAreaList = new ArrayList<Integer>();
	}
	@Override
	public MapStatus getMapStatus() {
		return mMapStatus;
	}

	@Override
	public void setMapStatus(MapStatus mapStatus) {
		this.mMapStatus = mapStatus;
		if(mapStatus == MapStatus.LoadingComplete){
			createMap();
		}
		
	}

	private void createMap() {
		mMapStatus = MapStatus.LoadingComplete;
		EObjectType.MAP_LINE.setVertices(EMapFactory.integerListToFloatArray(mapLineList));
		EObjectType.MAP_POINT.setVertices(EMapFactory.integerListToFloatArray(mapPointList));
		EObjectType.FORBIDDEN_LINE.setVertices(EMapFactory.integerListToFloatArray(forbiddenLineList));
		EObjectType.FORBIDDEN_AREA.setVertices(EMapFactory.integerListToFloatArray(forbiddenAreaList));
	}
	@Override
	public void updateMap(DataPacket packet) {
		EMapFactory.MapFactory.updateMap(this, packet);
	}

	
	private List<Integer> mapLineList;
	@Override
	public void addMapLine(int x1, int y1, int z1, int x2, int y2, int z2) {
		mapLineList.add(x1);
		mapLineList.add(y1);
		mapLineList.add(z1);
		mapLineList.add(x2);
		mapLineList.add(y2);
		mapLineList.add(z2);
	}
	private List<Integer> mapPointList;
	@Override
	public void addMapPoint(int x, int y, int z) {
		mapPointList.add(x);
		mapPointList.add(y);
		mapPointList.add(z);
	}
	private int[] minPos;
	public int[] getMinPos() {
		return minPos;
	}
	@Override
	public void setMinPos(int x, int y) {
		minPos = new int[]{x,y};
	}
	
	private int resolution;
	public int getResolution() {
		return resolution;
	}
	@Override
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	
	@Override
	public void setRobotHome(int x, int y, String description, String iconName,
			String name) {
		EObjectType.ROBOT_HOME.setVertices(new float[]{x,y});
		EObjectType.ROBOT_HOME.setDescription(description);
		EObjectType.ROBOT_HOME.setIconName(iconName);
		EObjectType.ROBOT_HOME.setObjectName(name);
		
	}
	@Override
	public void addGoal(int x, int y, String description, String iconName,
			String name, boolean withHeading) {
		EObjectType.ROBOT_HOME.addAdditionalMapObject(new AdditionalMapObject(x, y, description, iconName, name, withHeading));
		
	}
	private List<Integer> forbiddenLineList;
	@Override
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
	@Override
	public void addForbiddenArea(int x1, int y1, int x2, int y2,
			String description, String iconName, String name) {
		forbiddenAreaList.add(x1);
		forbiddenAreaList.add(y1);
		forbiddenAreaList.add(0);
		forbiddenAreaList.add(x2);
		forbiddenAreaList.add(y2);
		forbiddenAreaList.add(0);
	}
	
}
