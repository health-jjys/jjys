package com.yc.health.model;

public class MemberShoppeModel {

	private Integer memberShoppeId;
	private String name;
	private String imagePath;
	private String description;
	private String address;
	private String type;
	
	public Integer getMemberShoppeId() {
		return memberShoppeId;
	}
	public void setMemberShoppeId(Integer memberShoppeId) {
		this.memberShoppeId = memberShoppeId;
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
