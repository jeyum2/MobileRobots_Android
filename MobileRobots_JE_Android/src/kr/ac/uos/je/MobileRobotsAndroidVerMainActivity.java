package kr.ac.uos.je;

import kr.ac.uos.je.controller.RobotControllerManager;
import kr.ac.uos.je.exceptions.RobotControllerException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MobileRobotsAndroidVerMainActivity extends Activity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        openLoginDialog();
        
        //splash 
        startActivity(new Intent(this, SplashActivity.class));
        
    }
    
    public static final int  CONNECTION_ACCEPTED = 100;  
    private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(msg.what == CONNECTION_ACCEPTED) {
				startActivity(new Intent(MobileRobotsAndroidVerMainActivity.this, MainCenterActivity.class));
				finish();
			}
		}
	};
	
	private AlertDialog loginDialog;
	private SharedPreferences mPreference;
	private void openLoginDialog() {
		mPreference = getPreferences(0);
		
		final LinearLayout linear = (LinearLayout)View.inflate(MobileRobotsAndroidVerMainActivity.this, R.layout.login, null);
		final EditText ipEditText = (EditText) linear.findViewById(R.id.ServerAddressEditText);
		ipEditText.setText(mPreference.getString("ip", "172.16.164.108"));
		
		loginDialog = new AlertDialog.Builder(MobileRobotsAndroidVerMainActivity.this)
		.setTitle("Login")
		.setView(linear)
		.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ip = ipEditText.getText().toString();
				saveAddress(ip);
				
				try {
					robotThread = new Thread(RobotControllerManager.initRobotClient(ip, new AndroidAdaptorImpl(mHandler)));
				} catch (RobotControllerException e) {
					Toast.makeText(MobileRobotsAndroidVerMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					mProgressDialog.dismiss();
					openLoginDialog();
					e.printStackTrace();
				}
				robotThread.start();
				Log.d("ROBOT","Thread status : "+ robotThread.isAlive());
				
				showProgressDialog();
			}
		})
		.setCancelable(false)
		.show();
	}
	
	
	private Thread robotThread;
	private ProgressDialog mProgressDialog;
	private void showProgressDialog(){
		mProgressDialog = new ProgressDialog(MobileRobotsAndroidVerMainActivity.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle("Log in...");
		mProgressDialog.setMessage("Wait please");
		mProgressDialog.show();
	}
	private void saveAddress(String ip) {
		SharedPreferences.Editor edit = mPreference.edit();
		edit.putString("ip", ip);
		edit.commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(MobileRobotsAndroidVerMainActivity.this)
			.setTitle("Quit?")
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}