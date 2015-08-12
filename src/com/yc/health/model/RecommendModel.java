package com.yc.health.model;

import java.util.List;

public class RecommendModel {

	private Integer recommendId;
	private String title;
	private String des;
	private List<String> fit;
	private List<String> notFit;
	
	public Integer getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public List<String> getFit() {
		return fit;
	}
	public void setFit(List<String> fit) {
		this.fit = fit;
	}
	public List<String> getNotFit() {
		return notFit;
	}
	public void setNotFit(List<String> notFit) {
		this.notFit = notFit;
	}
}
