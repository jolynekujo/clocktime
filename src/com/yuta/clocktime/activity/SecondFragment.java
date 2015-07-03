package com.yuta.clocktime.activity;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yuta.clocktime.R;
import com.yuta.clocktime.model.AlarmClock;
import com.yuta.clocktime.receiver.AlarmReceiver;
import com.yuta.clocktime.receiver.CycleReceiver;
import com.yuta.clocktime.service.AlarmService;

public class SecondFragment<Gson> extends Fragment implements OnItemClickListener, OnItemLongClickListener, OnClickListener{
	private static final String debug = "com.yuta.clocktime.activity.SecondFragment";
	
	private View view;
	private ListView mShowAlarm;
	private TextView mAddAlarm;
	private AlarmAdapter adapter;
	private ArrayList<AlarmClock> mData = new ArrayList<AlarmClock>();
	private Intent mIntent;
	//intent的数量
	private int count = 0;
	//开关状态
	private ArrayList<Boolean> switchState = new ArrayList<Boolean>();
	//指向service的intent
	private ArrayList<Intent> intentList = new ArrayList<Intent>();
	
	private SharedPreferences mShared;
	private Editor mEditor;
	
	
	public SecondFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShared = getActivity().getSharedPreferences("datasaved", Activity.MODE_PRIVATE);
		mEditor = mShared.edit();
		if(mShared.contains("mData-size")){
			getData();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new AlarmClock();
		mIntent = new Intent(getActivity(), AlarmEditActivity.class);
		view = inflater.inflate(R.layout.alarmclock_layout, null);
		mShowAlarm = (ListView)view.findViewById(R.id.id_listview_alarm);
		mAddAlarm = (TextView)view.findViewById(R.id.id_textview_addalarm);
		adapter = new AlarmAdapter(mData, R.layout.alarm_clock_item, getActivity());
		
		mShowAlarm.setAdapter(adapter);
		mShowAlarm.setOnItemClickListener(this);
		mShowAlarm.setOnItemLongClickListener(this);
		mShowAlarm.setLongClickable(true);
		mAddAlarm.setOnClickListener(this);
		adapter.notifyDataSetChanged();
		return view;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		mData.get(position);
		mIntent.putExtra("key", "modify");
		mIntent.putExtra("count", count);
		mIntent.putExtra("alarm-clock-"+count+"-modify", mData.get(position));
		mIntent.putExtra("position", position);
		mIntent.putExtra("switch-state", switchState.get(position));
		
		startActivityForResult(mIntent, 2);
		count++;
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
		}
		
	};

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final AlarmClock alarmClock = mData.get(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("确定要删除该闹铃?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mData.remove(alarmClock);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return false;
	}

	@Override
	public void onClick(View v) {
		Calendar mCalendar = Calendar.getInstance();
		Date mDate = mCalendar.getTime();
		Calendar now = Calendar.getInstance();
		now.setTime(mDate);
		int mHour = now.get(Calendar.HOUR_OF_DAY);
		int mMinute = now.get(Calendar.MINUTE);
		DecimalFormat df = new DecimalFormat("00");
		String mTime = df.format(mHour)+":"+df.format(mMinute);
		List<String> mRepeat = new ArrayList<String>();
		String mLabel = "闹钟";
		int mVolume = 50;
		Uri mRingtone = RingtoneManager.getActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_RINGTONE);
		new AlarmClock(mTime, mLabel, mRepeat, mRingtone, mVolume);
		boolean mSwitchState = true;
		mIntent.putExtra("switch-state", mSwitchState);
		mIntent.putExtra("alarm-clock-"+count+"-add", new AlarmClock(mTime, mLabel, mRepeat, mRingtone, mVolume));
		mIntent.putExtra("count", count);
		mIntent.putExtra("key", "add");
		
		startActivityForResult(mIntent, 1);
		count++;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case 1:
			if(resultCode==Activity.RESULT_OK){
				AlarmClock mAlarmClock = data.getParcelableExtra("alarm-data"); 
				switchState.add(data.getBooleanExtra("return-state", true));
				mData.add(mAlarmClock);
				int pos = mData.size()-1;
				adapter.notifyDataSetChanged();
				
				
				//开启定时服务
				Intent i = new Intent(getActivity(), AlarmService.class);
				intentList.add(i);
				i.putExtra("alarmclock-info", mAlarmClock);
				i.putExtra("position", pos);
				getActivity().startService(i);
				
			}else if(resultCode==Activity.RESULT_FIRST_USER){
				
			}
			break;
		case 2:	
			if(resultCode==Activity.RESULT_OK){
				AlarmClock mAlarmClock = data.getParcelableExtra("alarm-data");
				Log.d(debug, mAlarmClock.getRing().toString()+"");
				boolean mReturnState = data.getBooleanExtra("return-state", false);
				int position = data.getIntExtra("position-back", -1);
				//更新界面
				mData.set(position, mAlarmClock);
				adapter.notifyDataSetChanged();
				//如果开关开启，停止之前的服务，重新开启数据修改后的服务
				if(mReturnState){	
					//should stop trigger task in service
					Intent i = new Intent(getActivity(), AlarmService.class);
					getActivity().stopService(i);
					
					Intent replaceIntent = new Intent(getActivity(), AlarmService.class);
					replaceIntent.putExtra("alarmclock-info", mAlarmClock);
					replaceIntent.putExtra("position", position);
					i.replaceExtras(replaceIntent);
					getActivity().startService(i);
				}
			}else if(resultCode==Activity.RESULT_FIRST_USER){
				int mP = data.getIntExtra("position-back", -1);
				boolean b = switchState.get(mP);
				if(b){
					Intent s1 = new Intent(AlarmReceiver.START_ALARM);
					Intent s2 = new Intent(CycleReceiver.START_CYCLE);
					//stop trigger task in service
					AlarmManager ma = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
					PendingIntent pi = PendingIntent.getBroadcast(getActivity(), mP, s1, 0);
					PendingIntent pi2 = PendingIntent.getBroadcast(getActivity(), mP, s2, 0);
					ma.cancel(pi);
					ma.cancel(pi2);
//					getActivity().stopService(i);
				}
				mData.remove(mP);
				switchState.remove(mP);
				intentList.remove(mP);
				boolean z = false;
				for(int i=0; i<switchState.size(); i++){
					if(switchState.get(i)){
						z = true;
						break;
					}
				}
				setStatusBarIcon(getActivity(), z);
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}
	
	//通知栏显示闹钟标志
	public static void setStatusBarIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}

	
	private void saveData(){
		for(int i=0; i<mData.size(); i++){
			AlarmClock mAC = mData.get(i);
			mEditor.putString("alarmtime"+i, mAC.getAlarmTime());
			mEditor.putString("label"+i, mAC.getLabel());
			mEditor.putInt("volume"+i, mAC.getVolume());
			mEditor.putString("ringtone"+i, mAC.getRing().toString());
			mEditor.putInt("frequency"+i, mAC.getFrequency().size());
			for(int j=0; j<mAC.getFrequency().size(); j++){
				mEditor.putString("fre"+i+":"+j, mAC.getFrequency().get(j));
			}
		}
		for(int i=0; i<intentList.size(); i++){
			Intent mI = intentList.get(i);
			mEditor.putString("intent"+i, mI.toUri(i));
		}
		for(int i=0; i<switchState.size(); i++){
			mEditor.putBoolean("state"+i, switchState.get(i));
		}
		mEditor.putInt("mData-size", mData.size());
		mEditor.putInt("intentList-size", intentList.size());
		mEditor.putInt("switchState-size", switchState.size());
		mEditor.commit();
	}
	
	private void getData(){
		int mDataSize = mShared.getInt("mData-size", 0);
		int intentListSize = mShared.getInt("intentList-size", 0);
		int switchStateSize = mShared.getInt("switchState-size", 0);
		for(int i=0; i<mDataSize; i++){
			String at = mShared.getString("alarmtime"+i, "");
			String l = mShared.getString("label"+i, "");
			int v = mShared.getInt("volume"+i, 0);
			Uri r = Uri.parse(mShared.getString("ringtone"+i, ""));
			int fs = mShared.getInt("frequency"+i, 0);
			List<String> ls = new ArrayList<String>();
			for(int j=0; j<fs; j++){
				String s = mShared.getString("fre"+i+":"+j, "");
				ls.add(s);
			}
			mData.add(new AlarmClock(at, l, ls, r, v));
		}
		for(int i=0; i<intentListSize; i++){
			Intent mI = new Intent();
			try {
				mI = Intent.parseUri(mShared.getString("intent"+i, ""), i);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			intentList.add(mI);
		}
		for(int i=0; i<switchStateSize; i++){
			boolean mS = mShared.getBoolean("state"+i, false);
			switchState.add(mS);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saveData();
	}
	
	class AlarmAdapter extends BaseAdapter{
		
		private List<AlarmClock> alarmData = new ArrayList<AlarmClock>();
		private int layoutId;
		private ViewHolder mHolder;
		private Context context;
		
		
		public AlarmAdapter(List<AlarmClock> data, int layoutId, Context context){
			alarmData = data;
			this.layoutId = layoutId;
			this.context = context;
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
			return position;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			Log.d(debug, position+"");
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
				String[] space = new String[]{"","","","","","",""};
				for(String str: alarm.getFrequency()){
					if(str.equals("一")){
						space[0] = "一";
					}
					if(str.equals("二")){
						space[1] = "二";
					}
					if(str.equals("三")){
						space[2] = "三";
					}
					if(str.equals("四")){
						space[3] = "四";
					}
					if(str.equals("五")){
						space[4] = "五";
					}
					if(str.equals("六")){
						space[5] = "六";
					}
					if(str.equals("日")){
						space[6] = "日";
					}
				}
				selectedDay += space[0]+space[1]+space[2]+space[3]+space[4]+space[5]+space[6];
			}
			mHolder.mFrequency.setText(selectedDay);
			mHolder.mCheckBox.setChecked(switchState.get(position));
			mHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.d(debug, position+"");
					Log.d(debug, isChecked?"true":"false");
					if(switchState.get(position)){
						Intent s1 = new Intent(AlarmReceiver.START_ALARM);
						Intent s2 = new Intent(CycleReceiver.START_CYCLE);
						//stop trigger task in service
						AlarmManager ma = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
						PendingIntent pi = PendingIntent.getBroadcast(getActivity(), position, s1, 0);
						PendingIntent pi2 = PendingIntent.getBroadcast(getActivity(), position, s2, 0);
						ma.cancel(pi);
						ma.cancel(pi2);
//						getActivity().stopService(new Intent(getActivity(), AlarmService.class));
						
						switchState.set(position, Boolean.valueOf(false));
						NotificationManager m = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
						m.cancel(position);
						boolean x = false;
						for(int i=0; i<switchState.size(); i++){
							if(switchState.get(i)){
								x = true;
								break;
							}
						}
						setStatusBarIcon(getActivity(), x);
					}else{
						Intent newIntent = new Intent(getActivity(), AlarmService.class);
						newIntent.putExtra("alarmclock-info", mData.get(position));
						newIntent.putExtra("position", position);
						getActivity().startService(newIntent);
						switchState.set(position, Boolean.valueOf(true));
						setStatusBarIcon(getActivity(), true);
						
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
	
	
}
