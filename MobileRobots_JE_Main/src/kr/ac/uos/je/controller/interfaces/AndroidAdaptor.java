package kr.ac.uos.je.controller.interfaces;

import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.interfaces.ResourceManager;

public interface AndroidAdaptor {
	public static final int SHOW_LOGIN_DIALOG = 11;
	public static final int CONNECTION_ACCEPTED = 21;
	public static final int CONNECTION_FAIL = 22;
	public void sendEmptyMessage(int msg);
	public void printLog(String tag, String msg);
	public ResourceManager getResourceManager();

}
