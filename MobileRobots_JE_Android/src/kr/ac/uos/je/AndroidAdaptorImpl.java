package kr.ac.uos.je;

import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.model.interfaces.MapManager;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import android.os.Handler;
import android.util.Log;

public class AndroidAdaptorImpl implements AndroidAdaptor {

	private Handler mHandler;
	public AndroidAdaptorImpl(Handler handler) {
		this.mHandler = handler;
	}
	@Override
	public void sendEmptyMessage(int msg) {
		mHandler.sendEmptyMessage(msg);
	}
	@Override
	public void printLog(String tag, String msg) {
		Log.d(tag, msg);
		
	}
	@Override
	public MapManager getMapManager() {
		return EMapManagerImpl.MapManager;
	}
	@Override
	public ResourceManager getResourceManager() {
		return EResourceManagerImpl.ResourceManger;
	}
	
}
