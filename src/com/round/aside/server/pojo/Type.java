package com.round.aside.server.pojo;

public class Type {
	private String id;
	private String typename;
	private String small_type_id;//
	private String small_type_name;//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getSmall_type_id() {
		return small_type_id;
	}
	public void setSmall_type_id(String small_type_id) {
		this.small_type_id = small_type_id;
	}
	public String getSmall_type_name() {
		return small_type_name;
	}
	public void setSmall_type_name(String small_type_name) {
		this.small_type_name = small_type_name;
	}
	
	@Override
	public String toString() {
		return typename+":"+small_type_name;
	}
}
