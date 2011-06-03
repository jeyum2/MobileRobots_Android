package kr.ac.uos.je;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{
	 //after 2000ms(2sec) finish
	private static final int LOADING_TIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		initialize();
	}

	private void initialize() {
		Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, LOADING_TIME); 
	}
	
}
