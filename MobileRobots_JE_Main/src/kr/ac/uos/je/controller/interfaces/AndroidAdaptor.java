package kr.ac.uos.je.controller.interfaces;

import kr.ac.uos.je.model.interfaces.MapManager;
import kr.ac.uos.je.model.interfaces.ResourceManager;

public interface AndroidAdaptor {
	public static final int CONNECTION_COMPLETE = 1;
	public void sendEmptyMessage(int msg);
	public void printLog(String tag, String msg);
	public MapManager getMapManager();
	public ResourceManager getResourceManager();

}
