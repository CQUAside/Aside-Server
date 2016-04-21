package com.round.aside.server.entity;

import java.util.Date;

public class PersonalCollectionEntity {
	int userID;
	int adID;
	Date collectTime;
	
	
	public PersonalCollectionEntity() {
		super();
	}
	public PersonalCollectionEntity(int userID, int adID, Date collectTime) {
		super();
		this.userID = userID;
		this.adID = adID;
		this.collectTime = collectTime;
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
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
}
