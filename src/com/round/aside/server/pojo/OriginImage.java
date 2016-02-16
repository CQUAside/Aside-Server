package com.round.aside.server.pojo;

public class OriginImage {
	private String id;
	private String ad_id;//所属广告ID
	private String path;//路径
	private String order;//顺序
	private String thumbnail_id;//缩略图ID
	private String thumbnail_path;//缩略图位置
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAd_id() {
		return ad_id;
	}
	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getThumbnail_id() {
		return thumbnail_id;
	}
	public void setThumbnail_id(String thumbnail_id) {
		this.thumbnail_id = thumbnail_id;
	}
	public String getThumbnail_path() {
		return thumbnail_path;
	}
	public void setThumbnail_path(String thumbnail_path) {
		this.thumbnail_path = thumbnail_path;
	}
	
	
}
