package com.yc.health.model;

import java.util.List;

public class KnowledgeModel {

	private Integer knowledgeId;
	private String title;
	private String des;
	private List<String> imagePath;
	private List<String> describes;
	
	public Integer getKnowledgeId() {
		return knowledgeId;
	}
	public void setKnowledgeId(Integer knowledgeId) {
		this.knowledgeId = knowledgeId;
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
	public List<String> getImagePath() {
		return imagePath;
	}
	public void setImagePath(List<String> imagePath) {
		this.imagePath = imagePath;
	}
	public List<String> getDescribes() {
		return describes;
	}
	public void setDescribes(List<String> describes) {
		this.describes = describes;
	}
	
}
