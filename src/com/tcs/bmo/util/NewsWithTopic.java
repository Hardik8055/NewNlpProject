package com.tcs.bmo.util;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsWithTopic {
	private double waitageTopic;
	private List<Article> articles;
	private String topic;
	private String entity;
	public double getWaitageTopic() {
		return waitageTopic;
	}
	public void setWaitageTopic(double waitageTopic) {
		this.waitageTopic = waitageTopic;
	}
	public List<Article> getArticle() {
		return articles;
	}
	public void setArticle(List<Article> article) {
		this.articles = article;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}

}
