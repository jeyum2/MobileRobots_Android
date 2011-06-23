package kr.ac.uos.je;

import kr.ac.uos.je.controller.ERobotMode;
import kr.ac.uos.je.controller.RobotCommuniator;
import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.controller.interfaces.IFacedeController;
import kr.ac.uos.je.exceptions.RobotControllerException;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MobileRobotsAndroidVerMainActivity extends AndroidApplication  implements AndroidAdaptor, View.OnKeyListener, DialogInterface.OnKeyListener{
	private IFacedeController mMobileRobotsFacade;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	EResourceManagerImpl.ResourceManger.initPreferences(getApplicationContext());
    	MobileRobotsFacade mMobileRobotsFacade = new MobileRobotsFacade(EResourceManagerImpl.ResourceManger, this);
		initialize(mMobileRobotsFacade,false);
		this.mMobileRobotsFacade = mMobileRobotsFacade;
    }	
	
	
	private AlertDialog quitDialog;
	private  void showQuitDialog(){
		quitDialog = new AlertDialog.Builder(MobileRobotsAndroidVerMainActivity.this)
		.setTitle("Quit?")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMobileRobotsFacade.dispose();
		try {
			RobotCommuniator.getRobotClient().disconnect();
		} catch (RobotControllerException e) {
			e.printStackTrace();
		}
		EResourceManagerImpl.ResourceManger.saveResourceToPreference();
	}
	
	/**
	 * Touch Input
	 */
	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//			
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//	    int act = event.getAction();
//
//	    Log.d("TOUCH", String.format("Simple down (%d, %d)", x,y));
//	    ETouchMode touchMode = ETouchMode.UP;
//		    
//	    	switch(act & MotionEvent.ACTION_MASK) {
//	        case MotionEvent.ACTION_DOWN:    //first finger touch(drag)
//	            touchMode = ETouchMode.FIRST_DOWN;
//	            break;
//	        case MotionEvent.ACTION_MOVE:
//	        	touchMode = ETouchMode.MOVE;
//	            break;
//	        case MotionEvent.ACTION_UP:    // 첫번째 손가락을 떼었을 경우
//	        case MotionEvent.ACTION_POINTER_UP:  // 두번째 손가락을 떼었을 경우
//	            break;
//	        case MotionEvent.ACTION_POINTER_DOWN:  
//	            break;
//	        case MotionEvent.ACTION_CANCEL:
//	        default : 
//	            break;
//	    	}
//		    //!!!!!!! important
//	    	EMultiTouchController.MultiTouchController.onTouchEvent(touchMode, x,y);
//		  return true;
//		}


	
	/**
	 * Helper Method - bind Java to Android by AndroidAdaptorImpl 
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
			case AndroidAdaptor.SHOW_LOGIN_DIALOG:
				showLoginDialog();
				break;
			case AndroidAdaptor.CONNECTION_ACCEPTED :
				Log.d("ROBOT", "Connection Accepted");
				if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
				mMobileRobotsFacade.setScreen(MobileRobotsFacade.EScreenStatus.MainScreen);
				break;
			case AndroidAdaptor.CONNECTION_FAIL :
				Toast.makeText(MobileRobotsAndroidVerMainActivity.this, EResourceManagerImpl.ResourceManger.getStringByStringName("ConnectionFail"), Toast.LENGTH_SHORT).show();
				if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
				showLoginDialog();
				break;
			}
		}
	};

	private Thread robotThread;
	private void showLoginDialog() {
		final SharedPreferences mPreference = getPreferences(0);
		
		final LinearLayout linear = (LinearLayout)View.inflate(MobileRobotsAndroidVerMainActivity.this, R.layout.login, null);
		final EditText ipEditText = (EditText) linear.findViewById(R.id.ServerAddressEditText);
		ipEditText.setText(mPreference.getString("ip", "172.16.164.130"));
		ipEditText.setOnKeyListener(this);
		
		new AlertDialog.Builder(MobileRobotsAndroidVerMainActivity.this)
		.setTitle("Login")
		.setView(linear)
		.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ip = ipEditText.getText().toString();
				SharedPreferences.Editor edit = mPreference.edit();
				edit.putString("ip", ip);
				edit.commit();
				
				try {
					robotThread = new Thread(RobotCommuniator.initRobotClient(ip, MobileRobotsAndroidVerMainActivity.this));
				} catch (RobotControllerException e) {
					Toast.makeText(MobileRobotsAndroidVerMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					mProgressDialog.dismiss();
					showLoginDialog();
					e.printStackTrace();
				}
				robotThread.start();
				Log.d("ROBOT","Thread status : "+ robotThread.isAlive());
				String loginProgressTitle = EResourceManagerImpl.ResourceManger.getStringByStringName("Progress_Title_Login"); 
				String loginProgressMessage = EResourceManagerImpl.ResourceManger.getStringByStringName("Progress_Message_Login");
				showProgressDialog(loginProgressTitle, loginProgressMessage);
			}
		})
		.setCancelable(false)
		.show();
	}
	private ProgressDialog mProgressDialog;
	private void showProgressDialog(String title, String message){
		if(mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
		mProgressDialog = new ProgressDialog(MobileRobotsAndroidVerMainActivity.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		mProgressDialog.setOnKeyListener(this);
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
	public ResourceManager getResourceManager() {
		return EResourceManagerImpl.ResourceManger;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(quitDialog == null || !quitDialog.isShowing()){
				showQuitDialog();
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(quitDialog == null || !quitDialog.isShowing()){
				showQuitDialog();
			}

			return false;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(quitDialog == null || !quitDialog.isShowing()){
				showQuitDialog();
			}

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	/**
	 * Menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.robotcontrolmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()){
			case R.id.Menu_DisplaySetting:
//				startActivity(new Intent(MobileRobotsAndroidVerMainActivity.this, DisplaySettingActivity.class));
				return true;
			case R.id.Menu_GetGoalList:
				showGoalListMenu();
				return true;
			case R.id.Menu_CenterOnRobot:
				Toast.makeText(getApplicationContext(),  ERobotMode.CenterOnRobotMode.toggleMode(), Toast.LENGTH_SHORT).show();
				return true;
			case R.id.Menu_ForceStop:
			try {
				RobotCommuniator.getRobotClient().forceStop();
				} catch (RobotControllerException e) {
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), "Force Stop", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.Menu_ManualDrive:
				Toast.makeText(getApplicationContext(), ERobotMode.ManualDriveMode.toggleMode(), Toast.LENGTH_SHORT).show();
				return true;
			case R.id.Menu_Preference:
//				showPreferenceList();
			default :
				break;
		}
	    return super.onOptionsItemSelected(item);
	}

	private void showGoalListMenu() {
		final String[] goalList = EObjectType.GOALS.getSubObjectNames(); 
		new AlertDialog.Builder(MobileRobotsAndroidVerMainActivity.this)
        .setTitle("Goal List")
        .setItems(goalList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	String goal = goalList[which];
            	Toast.makeText(getApplicationContext(), "Go to "+ goal, Toast.LENGTH_SHORT).show();
            	try {
					RobotCommuniator.getRobotClient().gotoGoal(goal);
				} catch (RobotControllerException e) {
					e.printStackTrace();
				}
            }
        })
        .create().show();
	}

	
}

