package kr.ac.uos.je;

import kr.ac.uos.je.controller.RobotControllerManager;
import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.exceptions.RobotControllerException;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MobileRobotsAndroidVerMainActivity extends AndroidApplication  implements AndroidAdaptor{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	EResourceManagerImpl.ResourceManger.initPreferences(getApplicationContext());
    	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;		
		initialize(new MobileRobotsFacade(EResourceManagerImpl.ResourceManger, this),config);
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
		EResourceManagerImpl.ResourceManger.saveResourceToPreference();
	}
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
		ipEditText.setOnKeyListener(new BackKeyListener());
		
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
					robotThread = new Thread(RobotControllerManager.initRobotClient(ip, MobileRobotsAndroidVerMainActivity.this));
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
		mProgressDialog.setOnKeyListener(new BackKeyListener());
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
	private class BackKeyListener implements View.OnKeyListener, DialogInterface.OnKeyListener{

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
	}
}

