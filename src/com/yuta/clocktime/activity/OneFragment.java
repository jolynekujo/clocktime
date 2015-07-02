package com.yuta.clocktime.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuta.clocktime.R;
import com.yuta.clocktime.widiget.ClockView;

@SuppressLint("SimpleDateFormat")
@Deprecated
public class OneFragment extends Fragment implements OnClickListener{
	private TextView showTime;
	private TextView showCity;
	private TextView changeTZ;
	private ClockView mClock;
	//current timezone
	private static TimeZone mTimezone;
	
	static{
		mTimezone = TimeZone.getDefault();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.clock_layout, container);
		showTime = (TextView)view.findViewById(R.id.time);
		showCity = (TextView)view.findViewById(R.id.timezone);
		changeTZ = (TextView)view.findViewById(R.id.change_timezone);
		mClock = (ClockView)view.findViewById(R.id.clock1);
		String[] values = getCurrentTime().split(":");
		updateUi(values[0]+":"+values[1], TimeZone.getDefault().getDisplayName().toString());
		changeTZ.setOnClickListener(this);
		return view;
	}

	private void updateUi(String time, String timezone) {
		showTime.setText(time);
		showCity.setText(timezone);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.change_timezone:
			ChooseTimeZoneActivity.actionStart(getActivity());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.yuta.clocktime.ChooseTimeZoneActivity.TIMEZONE_CHANGED");
		getActivity().registerReceiver(receiver, filter);
		
		IntentFilter timeFilter = new IntentFilter();
		timeFilter.addAction(Intent.ACTION_TIME_CHANGED);
		timeFilter.addAction(Intent.ACTION_TIME_TICK);
		timeFilter.addAction("com.yuta.clocktime.OneFragment.TIMECHANGED");
		
		getActivity().registerReceiver(timeReceiver, timeFilter);
	}
	
	private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String[] values = getCurrentTime().split(":");
			showTime.setText(values[0]+":"+values[1]);
//			mClock.setTime(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
		}
	};
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("timezonechanged", action);
//			if(action.equals(Intent.ACTION_TIME_TICK)){
//				showTime.setText(getCurrentTime());
//			}
			if(action.equals("com.yuta.clocktime.ChooseTimeZoneActivity.TIMEZONE_CHANGED")){
				String tz = intent.getStringExtra("time-zone");
				String city = intent.getStringExtra("city");
				TimeZone timeZone = TimeZone.getTimeZone(tz);
				mTimezone = timeZone;
				Intent i = new Intent("com.yuta.clocktime.OneFragment.TIMECHANGED");
				getActivity().sendBroadcast(i);
				showCity.setText(city);
			}
		}
	};
	
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
		getActivity().unregisterReceiver(timeReceiver);
		mTimezone = TimeZone.getDefault();
	}
	
	private String getCurrentTime(){
		TimeZone defTimeZone = mTimezone;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(defTimeZone);
		String time = sdf.format(new Date());
		return time;
	}
	
	
}
