package com.round.aside.server.entity;

import java.util.Date;

public class InformAdsEntity {
	int userID;
	int adID;
	Date time;
	
	
	public InformAdsEntity() {
		super();
	}
	public InformAdsEntity(int userID, int adID, Date time) {
		super();
		this.userID = userID;
		this.adID = adID;
		this.time = time;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getAdID() {
		return adID;
	}
	public void setAdID(int adID) {
		this.adID = adID;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	

}
