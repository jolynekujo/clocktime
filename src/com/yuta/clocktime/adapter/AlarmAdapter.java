package com.yuta.clocktime.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yuta.clocktime.R;
import com.yuta.clocktime.model.AlarmClock;

public class AlarmAdapter extends BaseAdapter{
	private static final String debug = "com.yuta.clocktime.adapter.AlarmAdapter";
	
	private List<AlarmClock> alarmData = new ArrayList<AlarmClock>();
	private List<Boolean> states = new ArrayList<Boolean>();
	private int layoutId;
	private ViewHolder mHolder;
	private Context context;
	
	private boolean isOn = false;;
	
	public AlarmAdapter(List<AlarmClock> data, List<Boolean> states, int layoutId, Context context){
		alarmData = data;
		this.layoutId = layoutId;
		this.context = context;
		this.states = states;
	}
	
	@Override
	public int getCount() {
		return alarmData.size();
	}

	@Override
	public Object getItem(int position) {
		return alarmData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final AlarmClock alarm = alarmData.get(position);
		
		if(convertView==null){
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(layoutId, null);
			mHolder.mTime = (TextView)view.findViewById(R.id.id_textview_time);
			mHolder.mLabel = (TextView)view.findViewById(R.id.id_textview_label);
			mHolder.mFrequency = (TextView)view.findViewById(R.id.id_textview_freq);
			mHolder.mCheckBox = (CheckBox)view.findViewById(R.id.id_checkbox_alarm);
			view.setTag(mHolder);
		}else{
			view = convertView;
			mHolder = (ViewHolder) view.getTag();
		}
		mHolder.mTime.setText(alarm.getAlarmTime());
		mHolder.mLabel.setText(alarm.getLabel());
		String selectedDay = new String();
		if(alarm.getFrequency().size()==7){
			selectedDay = "每天";
		}else if(alarm.getFrequency().size()==0){
			selectedDay = "仅一次";
		}else{
			selectedDay = "周";
			for(String str: alarm.getFrequency()){
				
				selectedDay += str;
			}
		}
		mHolder.mFrequency.setText(selectedDay);
		mHolder.mCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.id_checkbox_alarm:
//					if((CheckBox)v.is)
				}
			}
		});
		return view;
	}

	class ViewHolder{
		TextView mTime;
		TextView mLabel;
		TextView mFrequency;
		CheckBox mCheckBox;
	}

}
