package com.yuta.clocktime.model;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AlarmClock implements Parcelable{
	private String alarmTime;
	private String label;
	private List<String> frequency;
	private Uri ring;
	private int volume;
	public AlarmClock() {
		super();
	}
	public AlarmClock(String alarmTime, String label, List<String> frequency,
			Uri ring, int volume) {
		super();
		this.alarmTime = alarmTime;
		this.label = label;
		this.frequency = frequency;
		this.ring = ring;
		this.volume = volume;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getFrequency() {
		return frequency;
	}
	public void setFrequency(List<String> frequency) {
		this.frequency = frequency;
	}
	public Uri getRing() {
		return ring;
	}
	public void setRing(Uri ring) {
		this.ring = ring;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(ring, flags);
		dest.writeString(alarmTime);
		dest.writeString(label);
		
		dest.writeInt(volume);
		dest.writeList(frequency);
	}
	
	private AlarmClock(Parcel in){
		ring = (Uri)in.readParcelable(Uri.class.getClassLoader());
		alarmTime = in.readString();
		label = in.readString();
		volume = in.readInt();
		frequency = new ArrayList<String>();
		in.readList(frequency, null);
		
	}
	
	
	public static final Parcelable.Creator<AlarmClock> CREATOR = new Parcelable.Creator<AlarmClock>() {

		@Override
		public AlarmClock createFromParcel(Parcel source) {
			
			return new AlarmClock(source);
		}

		@Override
		public AlarmClock[] newArray(int size) {
			return new AlarmClock[size];
		}
	};
	
}
