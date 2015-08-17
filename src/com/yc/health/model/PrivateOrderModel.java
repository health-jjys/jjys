package com.yc.health.model;

public class PrivateOrderModel {
	
	private Integer privateOrderId;
	private String name;
	private String imagePath;
	private String description;
	private String phone;
	private String address;
	private String type;
	public Integer getPrivateOrderId() {
		return privateOrderId;
	}
	public void setPrivateOrderId(Integer privateOrderId) {
		this.privateOrderId = privateOrderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
