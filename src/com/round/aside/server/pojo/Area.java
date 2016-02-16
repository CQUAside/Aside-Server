package com.round.aside.server.pojo;

public class Area {
	private String id;
	private String province;
	private String city;//市
	private String area;//区、县
	private String town;//镇、街道
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	
	@Override
	public String toString() {
		return province+city+area+town;
	}
}
