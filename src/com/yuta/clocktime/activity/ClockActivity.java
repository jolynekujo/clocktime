package com.yuta.clocktime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.yuta.clocktime.R;
import com.yuta.clocktime.util.ActivityCollector;

public class ClockActivity extends BaseActivity{
	private static final String debug = "com.yuta.clocktime.activity.ClockActivity";
	private SecondFragment secondFragment;
	private OneFragment oneFragment;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);
		secondFragment = (SecondFragment)getFragmentManager().findFragmentById(R.id.alarm_fragment);
		oneFragment = (OneFragment)getFragmentManager().findFragmentById(R.id.clock_fragment);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityCollector.finishAll();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    } 
}
