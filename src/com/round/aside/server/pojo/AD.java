package com.round.aside.server.pojo;

import java.util.Date;

public class AD {
	private String id;
	private String thumbnail_id;//缩略图ID
	private String carouse_id;//轮播ID
	private String title;
	private String content;
	private Date start_time;
	private Date end_time;
	private String money;
	private String status;
	private String clicks;//点击量
	private String stars;//收藏量
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getThumbnail_id() {
		return thumbnail_id;
	}
	public void setThumbnail_id(String thumbnail_id) {
		this.thumbnail_id = thumbnail_id;
	}
	public String getCarouse_id() {
		return carouse_id;
	}
	public void setCarouse_id(String carouse_id) {
		this.carouse_id = carouse_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	
	
}
