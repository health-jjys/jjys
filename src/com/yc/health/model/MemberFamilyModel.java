package com.yc.health.model;

public class MemberFamilyModel {

	private Integer membersFamilyId;
	private String membersName;
	private String constitution;
	private Integer userId;
	private String loginName;
	
	public Integer getMembersFamilyId() {
		return membersFamilyId;
	}
	public void setMembersFamilyId(Integer membersFamilyId) {
		this.membersFamilyId = membersFamilyId;
	}
	public String getMembersName() {
		return membersName;
	}
	public void setMembersName(String membersName) {
		this.membersName = membersName;
	}
	public String getConstitution() {
		return constitution;
	}
	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
