package com.yuta.clocktime.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yuta.clocktime.R;
import com.yuta.clocktime.adapter.CityAdapter;
import com.yuta.clocktime.model.District;
import com.yuta.clocktime.util.ActivityCollector;

public class ChooseTimeZoneActivity extends BaseActivity implements OnItemClickListener{
	private static final String debug = "com.yuta.clocktime.activity.ChooseTimeZoneActivity";
	
	private ListView listView;
	private List<District> cityData = new ArrayList<District>();
	private CityAdapter adapter;
//	private LocalBroadcastManager localBroadcast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_timezone);
		cityData = Init.InitTimeZone();
		listView = (ListView)findViewById(R.id.listview);
//		localBroadcast = LocalBroadcastManager.getInstance(this);
		adapter  = new CityAdapter(cityData, this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	String preStr = "";
	List<District> preList = new ArrayList<District>();
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		District city = cityData.get(position);
		String tz = city.getTimezone();
		Intent broadcastIntent2 = new Intent("com.yuta.clocktime.ChooseTimeZoneActivity.TIMEZONE_CHANGED");
		broadcastIntent2.putExtra("time-zone", tz);
		broadcastIntent2.putExtra("city", city.getCity());
		sendBroadcast(broadcastIntent2);
		finish();
	}
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context, ChooseTimeZoneActivity.class);
		context.startActivity(intent);
	}
}
