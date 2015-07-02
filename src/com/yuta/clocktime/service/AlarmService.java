package com.yuta.clocktime.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yuta.clocktime.R;
import com.yuta.clocktime.activity.SecondFragment;
import com.yuta.clocktime.model.AlarmClock;
import com.yuta.clocktime.receiver.AlarmReceiver;
import com.yuta.clocktime.receiver.CycleReceiver;
import com.yuta.clocktime.util.MyApplication;

public class AlarmService extends Service{
	private static final String debug = "com.yuta.clocktime.service.AlarmService";
	
	private AlarmClock alarmClock = new AlarmClock();
	private Uri ringUri = null;
	private boolean isRepeat;
	private Handler mHandler = new Handler(){
		
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(debug, "startId:"+startId);
		
		alarmClock = intent.getParcelableExtra("alarmclock-info");
		int activedCount = intent.getIntExtra("actived-count", 0);
		isRepeat = false;
		Log.d(debug, alarmClock.getAlarmTime()+";"+alarmClock.getRing());
		if(alarmClock.getFrequency().size()>0){
			isRepeat = true;
		}
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		for(int i=0; i<alarmClock.getFrequency().size(); i++){
			Log.d(debug, alarmClock.getFrequency().get(i));
		}
		
		long waitTime = toNextAlarm(now, alarmClock.getAlarmTime(), alarmClock.getFrequency());
		int day = (int) (waitTime / (1000*60*60*24));
		int hour = (int) ((waitTime - day*1000*60*60*24) / (1000*60*60));
		int minute = (int)((waitTime - day*1000*60*60*24 - hour*1000*60*60) / (1000*60));
		String notText = "";
		if(day!=0){
			notText += day+"天";
		}
		if(hour!=0){
			notText += hour+"小时";
		}
		if(minute!=0){
			notText += minute+"分钟";
		}
		if(day==0&&hour==0&&minute==0){
			notText += "不到1分钟";
		}
		notText += "后提醒";
		//设置通知
		CharSequence tl = "";
		CharSequence text = "闹铃已设定";
		Notification notification = new Notification(R.drawable.ic_alarm_black_48dp, notText, System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(),tl , text, null);
		NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.notify(1, notification);
		SecondFragment.setStatusBarIcon(this, true);
		//设置等待时间
		AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		long triggerAtTime = SystemClock.elapsedRealtime() + waitTime;
		
		mHandler.postDelayed(new Runnable(){

			@Override
			public void run() {
				
			}
			
		}, waitTime);
		
		Intent intent1 = new Intent(this, AlarmReceiver.class);
		intent1.putExtra("alarm-data-service", alarmClock);
		Log.d(debug, alarmClock.getRing().toString()+"");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		
		Intent intent2 = new Intent(this, CycleReceiver.class);
		intent2.putExtra("isRepeat", isRepeat);
		intent2.putExtra("alarm-data-service", alarmClock);
		intent2.putExtra("actived-count", activedCount);
		PendingIntent pi2 = PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi2);
		return super.onStartCommand(intent1, flags, startId);
	}

	private long toNextAlarm(Calendar calendar, String time, List<String> list){
		Log.d(debug, "toNextAlarm");
		long wt = 0l;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		long secondOfDay = hour*60*60*1000 + minute*60*1000 + second*1000;
		
		String[] str = time.split(":");
		int targetHour = Integer.parseInt(str[0]);
		int targetMinute = Integer.parseInt(str[1]);
		long targetSecondOfDay = targetHour*60*60*1000 + targetMinute*60*1000;
		
		int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		long secondOfWeek = (todayOfWeek-1)*24*60*60*1000 + secondOfDay;
		List<Integer> idOfWeek = new ArrayList<Integer>();
		for(String weekStr: list){
			Log.d(debug, weekStr);
			if(weekStr.equals("日")){
				idOfWeek.add(1);
			}
			if(weekStr.equals("一")){
				idOfWeek.add(2);
			}
			if(weekStr.equals("二")){
				idOfWeek.add(3);
			}
			if(weekStr.equals("三")){
				idOfWeek.add(4);
			}
			if(weekStr.equals("四")){
				idOfWeek.add(5);
			}
			if(weekStr.equals("五")){
				idOfWeek.add(6);
			}
			if(weekStr.equals("六")){
				idOfWeek.add(7);
			}
		}
		
		List<Long> secondOfWeekList = new ArrayList<Long>();
		for(Integer i: idOfWeek){
			long l = (i-1) * 24*60*60*1000 + targetSecondOfDay;
			secondOfWeekList.add(l);
		}
		if(idOfWeek.size()==0){
			if(targetSecondOfDay>secondOfDay){
				wt = targetSecondOfDay - secondOfDay;
			}else{
				wt = targetSecondOfDay - secondOfDay + 24*60*60*1000;
			}
		}else{
			long min = Long.MAX_VALUE;
			for(Long l : secondOfWeekList){
				if(l-secondOfWeek>0){
					if(l-secondOfWeek<min){
						min = l - secondOfWeek;
					}
				}else{
					if(l-secondOfWeek+7*24*60*60<min){
						min = l-secondOfWeek+7*24*60*60*1000;
					}
				}
			}
			wt = min;
		}
		return wt;
	}
}
