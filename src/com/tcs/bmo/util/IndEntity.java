package com.tcs.bmo.util;

import java.util.HashMap;

public class IndEntity {
	private HashMap<String,String> entity=new HashMap<>();

	public HashMap<String, String> getEntity() {
		return this.entity;
	}

	public void setEntity(HashMap<String, String> entity) {
		this.entity = entity;
	}

	public IndEntity() {
		super();
		//this.entity.put("nutch", "Food");
		//this.entity.put("cbiz", "CBIZ L");
		//this.entity.put("farmington", "Farmington Foods, Inc.");
		//this.entity.put("packaging", "Green Bay Packaging, Inc.");
	//	this.entity.put("tollywood", "tollywood, This is Tollywood news");
	//	this.entity.put("bollywood", "bollywood, This is Bollywood news.");
	//	this.entity.put("wine", "wine,This is wine news.");
	//	this.entity.put("cricket", "This is cricket news.");
		this.entity.put("cricket", "This is cricket news.");
		//this.entity.put("construction", "Holt Texas, Ltd.");
		//this.entity.put("wine", "Superior Beverage Group, Ltd.");
		//this.entity.put("manufacturing", "Warrior Mfg LLC");
		//this.entity.put("snackindustry", "Wyoandot Inc");
		//this.entity.put("warrior", "Warrior Mfg LLC");
		
		}

}
