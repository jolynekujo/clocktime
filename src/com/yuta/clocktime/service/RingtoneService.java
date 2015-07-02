package com.yuta.clocktime.service;

import java.io.IOException;

import com.yuta.clocktime.util.MyApplication;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class RingtoneService extends Service{
	private static final String debug = "com.yuta.clocktime.service.RingtoneService";
	
	MediaPlayer player = new MediaPlayer();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		player.setLooping(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(debug, "startId"+startId);
		int maxVolume = 100;
		Uri ringUri = intent.getParcelableExtra("ringtone");
		int volume = intent.getIntExtra("volume", 0);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource(getApplicationContext(), ringUri);
			float log1 = (float)(Math.log(maxVolume-volume)/Math.log(maxVolume));
			player.setVolume(1, log1);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		player.release();
		super.onDestroy();
	}
	
	
}
