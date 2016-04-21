package com.round.aside.server.entity;

import java.util.Date;

public class InformUsersEntity {
	int userID;
	int informedUserID;
	String informReason;
	Date time;
	
	public InformUsersEntity() {
		super();
	}
	
	public InformUsersEntity(int userID, int informedUserID,
			String informReason, Date time) {
		super();
		this.userID = userID;
		this.informedUserID = informedUserID;
		this.informReason = informReason;
		this.time = time;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getInformedUserID() {
		return informedUserID;
	}
	public void setInformedUserID(int informedUserID) {
		this.informedUserID = informedUserID;
	}
	public String getInformReason() {
		return informReason;
	}
	public void setInformReason(String informReason) {
		this.informReason = informReason;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	

}
