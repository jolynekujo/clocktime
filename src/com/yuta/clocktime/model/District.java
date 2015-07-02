package com.yuta.clocktime.model;


public class District {
	private String city;
	private String timezone;
	private int flag;
	
	public District() {
		super();
	}
	public District(String city, String timezone, int flag) {
		super();
		this.city = city;
		this.timezone = timezone;
		this.flag = flag;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	
}
