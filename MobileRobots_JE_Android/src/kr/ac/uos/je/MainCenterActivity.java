package kr.ac.uos.je;

import kr.ac.uos.je.controller.RobotControllerManager;
import kr.ac.uos.je.exceptions.RobotControllerException;
import kr.ac.uos.je.view.MobileRobotsMainRenderer;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainCenterActivity extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		EResourceManagerImpl.ResourceManger.initPreferences(getApplicationContext());
		try {
			initialize(new MobileRobotsMainRenderer(EResourceManagerImpl.ResourceManger, EMapManagerImpl.MapManager, RobotControllerManager.getRobotClient()), false);
		} catch (RobotControllerException e) {
			Toast.makeText(MainCenterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			startActivity(new Intent(MainCenterActivity.this, MobileRobotsAndroidVerMainActivity.class));
			finish();
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EResourceManagerImpl.ResourceManger.saveResourceToPreference();
	}

}
