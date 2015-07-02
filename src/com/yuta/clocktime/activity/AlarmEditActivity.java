package com.yuta.clocktime.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yuta.clocktime.R;
import com.yuta.clocktime.model.AlarmClock;

public class AlarmEditActivity extends BaseActivity implements OnClickListener{
	private static final String debug = "com.yuta.clocktime.activity.AlarmEditActivity";
	
	
	private TimePicker mTimePicker;
	private TextView mLabel;
	private TextView mRingtone;
	private SeekBar mSeekBar;
	private TextView delete;
	private TextView finish;
	private TextView mMonday;
	private TextView mTuesday;
	private TextView mWednesday;
	private TextView mThursday;
	private TextView mFriday;
	private TextView mSaturday;
	private TextView mSunday;
	
	private static final String MONDAY = "一";
	private static final String TUESDAY = "二";
	private static final String WEDNESDAY = "三";
	private static final String THURSDAY = "四";
	private static final String FRIDAY = "五";
	private static final String SATURDAY = "六";
	private static final String SUNDAY = "日";
	
	private AlarmClock mAlarmClock;
	private List<String> mRepeat;
	private Uri ringUri = null;
	private String time;
	private boolean switchState;
	private int position = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_alarmclock_layout);
		mMonday = (TextView)findViewById(R.id.id_textview_mon);
		mTuesday = (TextView)findViewById(R.id.id_textview_tue);
		mWednesday = (TextView)findViewById(R.id.id_textview_wed);
		mThursday = (TextView)findViewById(R.id.id_textview_thu);
		mFriday = (TextView)findViewById(R.id.id_textview_fri);
		mSaturday = (TextView)findViewById(R.id.id_textview_sat);
		mSunday = (TextView)findViewById(R.id.id_textview_sun);
		mTimePicker = (TimePicker)findViewById(R.id.id_timepicker_alarm);
		mLabel = (TextView)findViewById(R.id.id_textview_label);
		mRingtone = (TextView)findViewById(R.id.id_textview_ring);
		mSeekBar = (SeekBar)findViewById(R.id.id_seekbar_volume);
		delete = (TextView)findViewById(R.id.id_textview_delete);
		finish = (TextView)findViewById(R.id.id_textview_finish);
		
		mAlarmClock = new AlarmClock();
		mRepeat = new ArrayList<String>();
		mTimePicker.setIs24HourView(true);
		mMonday.setOnClickListener(this);
		mTuesday.setOnClickListener(this);
		mWednesday.setOnClickListener(this);
		mThursday.setOnClickListener(this);
		mFriday.setOnClickListener(this);
		mSaturday.setOnClickListener(this);
		mSunday.setOnClickListener(this);
		mLabel.setOnClickListener(this);
		mRingtone.setOnClickListener(this);
		delete.setOnClickListener(this);
		finish.setOnClickListener(this);
		updateEditUi();
	}

	private void updateEditUi(){
		Intent i = getIntent();
		if(i.getStringExtra("key").equals("add")){
			mAlarmClock = i.getParcelableExtra("alarm-clock-"+i.getIntExtra("count", 0)+"-add");
			switchState = i.getBooleanExtra("switch-state", false);
		}
		if(i.getStringExtra("key").equals("modify")){
			position = i.getIntExtra("position", -1);
			switchState = i.getBooleanExtra("switch-state", false);
			mAlarmClock = i.getParcelableExtra("alarm-clock-"+i.getIntExtra("count", 0)+"-modify");
		}
		ringUri = mAlarmClock.getRing();
		String[] mTime = mAlarmClock.getAlarmTime().split(":");
		mTimePicker.setCurrentHour(Integer.valueOf(mTime[0]));
		mTimePicker.setCurrentMinute(Integer.valueOf(mTime[1]));
		mLabel.setText(mAlarmClock.getLabel());
		this.mRingtone.setText(RingtoneManager.getRingtone(this, ringUri).getTitle(this));
		mSeekBar.setProgress(mAlarmClock.getVolume());
		mRepeat = mAlarmClock.getFrequency();
		if(mRepeat.contains(MONDAY)){
			mMonday.setTextColor(Color.BLACK);
		}else{
			mMonday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(TUESDAY)){
			mTuesday.setTextColor(Color.BLACK);
		}else{
			mTuesday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(WEDNESDAY)){
			mWednesday.setTextColor(Color.BLACK);
		}else{
			mWednesday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(THURSDAY)){
			mThursday.setTextColor(Color.BLACK);
		}else{
			mThursday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(FRIDAY)){
			mFriday.setTextColor(Color.BLACK);
		}else{
			mFriday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(SATURDAY)){
			mSaturday.setTextColor(Color.BLACK);
		}else{
			mSaturday.setTextColor(Color.GRAY);
		}
		if(mRepeat.contains(SUNDAY)){
			mSunday.setTextColor(Color.BLACK);
		}else{
			mSunday.setTextColor(Color.GRAY);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
//	public static void actionStart(Context context){
//		Intent intent = new Intent(context, AlarmEditActivity.class);
//		context.startActivity(intent);
//	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_textview_mon:
			if(!mRepeat.contains(MONDAY)){
				Log.d(debug, "monday selected");
				mMonday.setTextColor(Color.BLACK);
				mRepeat.add(MONDAY);
			}else{
				Log.d(debug, "monday canceled");
				mMonday.setTextColor(Color.GRAY);
				mRepeat.remove(MONDAY);
			}
			break;
		case R.id.id_textview_tue:
			if(!mRepeat.contains(TUESDAY)){
				Log.d(debug, "tuesday selected");
				mTuesday.setTextColor(Color.BLACK);
				mRepeat.add(TUESDAY);
			}
			else{
				Log.d(debug, "tuesday canceled");
				mTuesday.setTextColor(Color.GRAY);
				mRepeat.remove(TUESDAY);
			}
			break;
		case R.id.id_textview_wed:
			if(!mRepeat.contains(WEDNESDAY)){
				Log.d(debug, "wednesday selected");
				mWednesday.setTextColor(Color.BLACK);
				mRepeat.add(WEDNESDAY);
			}
			else{
				Log.d(debug, "wednesday canceled");
				mWednesday.setTextColor(Color.GRAY);
				mRepeat.remove(WEDNESDAY);
			}
			break;
		case R.id.id_textview_thu:
			if(!mRepeat.contains(THURSDAY)){
				Log.d(debug, "thursday selected");
				mThursday.setTextColor(Color.BLACK);
				mRepeat.add(THURSDAY);
			}else{
				Log.d(debug, "thursday canceled");
				mThursday.setTextColor(Color.GRAY);
				mRepeat.remove(THURSDAY);
			}
			break;
		case R.id.id_textview_fri:
			if(!mRepeat.contains(FRIDAY)){
				Log.d(debug, "friday selected");
				mFriday.setTextColor(Color.BLACK);
				mRepeat.add(FRIDAY);
			}else{
				Log.d(debug, "friday canceled");
				mFriday.setTextColor(Color.GRAY);
				mRepeat.remove(FRIDAY);
			}
			break;
		case R.id.id_textview_sat:
			if(!mRepeat.contains(SATURDAY)){
				Log.d(debug, "saturday selected");
				mSaturday.setTextColor(Color.BLACK);
				mRepeat.add(SATURDAY);
			}else{
				Log.d(debug, "saturday canceled");
				mSaturday.setTextColor(Color.GRAY);
				mRepeat.remove(SATURDAY);
			}
			break;
		case R.id.id_textview_sun:
			if(!mRepeat.contains(SUNDAY)){
				Log.d(debug, "sunday selected");
				mSunday.setTextColor(Color.BLACK);
				mRepeat.add(SUNDAY);
			}else{
				Log.d(debug, "sunday canceled");
				mSunday.setTextColor(Color.GRAY);
				mRepeat.remove(SUNDAY);
			}
			break;
		case R.id.id_textview_label:
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
			LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.layout_dialog, null);
			Builder dialogBuilder = new AlertDialog.Builder(AlarmEditActivity.this);
			dialogBuilder.setTitle("标签");
			dialogBuilder.setView(ll);
			final EditText input = (EditText)ll.findViewById(R.id.id_edittext_dialog);
			input.setText(mLabel.getText().toString());
			input.setHighlightColor(Color.RED);
			
			dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String inputLabel = input.getText().toString();
					mLabel.setText(inputLabel);
				}
			});
			dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
			break;
		case R.id.id_textview_ring:
			Intent intent = new Intent();
			intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
	        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹玲铃声");
	        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
	        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
	        if(ringUri!=null){
	        	intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringUri);
	        }
			startActivityForResult(intent, 0);
			break;
		case R.id.id_textview_finish:
			DecimalFormat df = new DecimalFormat("00");
			String hour = df.format(mTimePicker.getCurrentHour());
			String minute = df.format(mTimePicker.getCurrentMinute());
			time = hour + ":" +minute;
			mAlarmClock.setAlarmTime(time);
			mAlarmClock.setFrequency(mRepeat);
			mAlarmClock.setLabel(mLabel.getText().toString());
			mAlarmClock.setRing(ringUri);
			Log.d(debug, ringUri.toString());
			mAlarmClock.setVolume(mSeekBar.getProgress());
			Intent mIntent = new Intent(AlarmEditActivity.this, ClockActivity.class);
			mIntent.putExtra("alarm-data", mAlarmClock);
			mIntent.putExtra("return-state", switchState);
			if(position!=-1){
				mIntent.putExtra("position-back", position);
			}
//			startActivity(mIntent);
			setResult(RESULT_OK, mIntent);
			finish();
		case R.id.id_textview_delete:
			Intent dIntent = new Intent(AlarmEditActivity.this, ClockActivity.class);
			if(position!=-1){
				dIntent.putExtra("position-back", position);
			}
			setResult(RESULT_FIRST_USER, dIntent);
			finish();
		default:
			break;
		}
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case 0:
			Uri returnUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			ringUri = returnUri;
			Log.d(debug, ringUri.toString()+"");
			String title = RingtoneManager.getRingtone(this, returnUri).getTitle(this);
			Log.d(debug, title);
			mRingtone.setText(title);
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

}
