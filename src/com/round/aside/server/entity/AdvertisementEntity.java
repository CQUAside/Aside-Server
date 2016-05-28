package com.round.aside.server.entity;


/**
 * 广告实体类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 *
 */
public class AdvertisementEntity {
	int AdID;		 	//广告ID
	String Thumbnail_ID;	//缩略图ID
	int CarrouselID; 	//轮播ID
	String Title; 		//广告标题
	String Content; 	//广告内容
	String StartTime; 	//开始时间
	String Deadline; 		//截止时间
	double Money;
	int Status = 0;		//广告的状态，默认是未审核状态，状态：未审核（0）、已审核（1）、下架（2）、过期（3）。
	int ClickCount;		//点击量
	int CollectCount;	//收藏量
	int UserID;			//生成该广告的用户ID
	
	
	public AdvertisementEntity() {
		super();
	}
	
	public AdvertisementEntity(int adID, String thumbnail_ID, int carrouselID,
			String title, String content, String startTime, String deadline,
			double money, int status, int clickCount, int collectCount,
			int userID) {
		super();
		AdID = adID;
		Thumbnail_ID = thumbnail_ID;
		CarrouselID = carrouselID;
		Title = title;
		Content = content;
		StartTime = startTime;
		Deadline = deadline;
		Money = money;
		Status = status;
		ClickCount = clickCount;
		CollectCount = collectCount;
		UserID = userID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getAdID() {
		return AdID;
	}
	public void setAdID(int adID) {
		AdID = adID;
	}
	public String getThumbnail_ID() {
		return Thumbnail_ID;
	}
	public void setThumbnail_ID(String thumbnail_ID) {
		Thumbnail_ID = thumbnail_ID;
	}
	public int getCarrouselID() {
		return CarrouselID;
	}
	public void setCarrouselID(int carrouselID) {
		CarrouselID = carrouselID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getDeadline() {
		return Deadline;
	}

	public void setDeadline(String deadline) {
		Deadline = deadline;
	}

	public double getMoney() {
		return Money;
	}
	public void setMoney(double money) {
		Money = money;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getClickCount() {
		return ClickCount;
	}
	public void setClickCount(int clickCount) {
		ClickCount = clickCount;
	}
	public int getCollectCount() {
		return CollectCount;
	}
	public void setCollectCount(int collectCount) {
		CollectCount = collectCount;
	}
	
	

	

}
