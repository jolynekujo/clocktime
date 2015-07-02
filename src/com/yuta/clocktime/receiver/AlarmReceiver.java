package com.yuta.clocktime.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import com.yuta.clocktime.model.AlarmClock;
import com.yuta.clocktime.service.RingtoneService;

public class AlarmReceiver extends BroadcastReceiver{
	private static final String debug = "com.yuta.clocktime.receiver.AlarmReceiver";
	private AlarmClock alarmClock = new AlarmClock();
	private Uri ringUri = null;
	private int volume;
	private String label;
	final MediaPlayer mediaPlayer = new MediaPlayer();
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		alarmClock = intent.getParcelableExtra("alarm-data-service");
		ringUri = alarmClock.getRing();
		Log.d(debug, alarmClock.getRing().toString());
		Log.d(debug, ringUri.toString());
		volume = alarmClock.getVolume();
		label = alarmClock.getLabel();
		
		//启动播放音频的服务
		final Intent ringIntent = new Intent(context, RingtoneService.class);
		ringIntent.putExtra("ringtone", ringUri);
		ringIntent.putExtra("volume", volume);
		context.startService(ringIntent);
				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(label);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.stopService(ringIntent);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
