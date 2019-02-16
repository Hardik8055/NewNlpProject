package com.tcs.bmo.util;

import java.util.List;

public class SearchTopic {

	private String entity;
	
	private List<String> topicList;
	private List<String> selectedTopic;
	
	public SearchTopic(){
		
	}
	public SearchTopic(String entityName, List<String> sb) {
		// TODO Auto-generated constructor stub
		this.entity=entityName;
		this.topicList=sb;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
	public List<String> getSelectedTopic() {
		return selectedTopic;
	}

	public void setSelectedTopic(List<String> selectedTopic) {
		this.selectedTopic = selectedTopic;
	}

	public List<String> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<String> topicList) {
		this.topicList = topicList;
	}
	private String[] topic;

	public String[] getTopic() {
		return topic;
	}

	public void setTopic(String[] topic) {
		this.topic = topic;
	}
	
}
