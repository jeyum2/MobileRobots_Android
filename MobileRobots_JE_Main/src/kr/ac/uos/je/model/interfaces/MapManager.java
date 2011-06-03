package kr.ac.uos.je.model.interfaces;

import kr.ac.uos.semix2.robot.DataPacket;

public interface MapManager {
	

	public enum MapStatus{Empty, LoadingComplete, SomethingChanged};

	public MapStatus getMapStatus();
	public void setMapStatus(MapStatus mapStatus);

	public void updateMap(DataPacket packet);

	public void addMapLine(int x1, int y1, int z1, int x2, int y2, int z2);

	public void addMapPoint(int x, int y, int z);

	public void setMinPos(int x, int y);

	public void setResolution(int resolution);

	public void setRobotHome(int x, int y, String description, String iconName,
			String name);

	public void addGoal(int x, int y, String description, String iconName,
			String name, boolean withHeading);

	public void addForbiddenLine(int x1, int y1, int x2,
			int y2, String description, String iconName, String name);
	public void addForbiddenArea(int x1, int y1, int x2,
			int y2, String description, String iconName, String name);

}
