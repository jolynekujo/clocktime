package com.yuta.clocktime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yuta.clocktime.activity.SecondFragment;
import com.yuta.clocktime.model.AlarmClock;
import com.yuta.clocktime.service.AlarmService;

public class CycleReceiver extends BroadcastReceiver{
	private static final String debug = "com.yuta.clocktime.receiver.CycleReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmClock mAlarmClock = intent.getParcelableExtra("alarm-data-service");
		Log.d(debug, intent==null?"0":"1");
		boolean isRepeat = intent.getBooleanExtra("isRepeat", false);
		int activedCount = intent.getIntExtra("actived-count", 0);
		if(isRepeat){
			Intent returnIntent = new Intent(context, AlarmService.class);
			returnIntent.putExtra("alarmclock-info", mAlarmClock);
			Log.d(debug, returnIntent==null?"0":"1");
			context.startService(returnIntent);
		}else{
			if(activedCount-1==0){
				SecondFragment.setStatusBarIcon(context, false);
			}
			Log.d(debug, "AlarmService stop");
			//将对应的switch设置为关
		}
	}

}
