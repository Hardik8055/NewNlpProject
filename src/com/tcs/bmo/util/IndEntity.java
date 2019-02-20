package com.tcs.bmo.util;

import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@PropertySource(value="classpath:config/entity.properties", name="entity.props")
public class IndEntity {
	
	@Autowired
    Environment env;
	
	private HashMap<String, String> entity = new HashMap<>();

	public HashMap<String, String> getEntity() {
		return this.entity;
	}

	public void setEntity(HashMap<String, String> entity) {
		this.entity = entity;
	}

	
	public void config() {
		AbstractEnvironment ae = (AbstractEnvironment)env;
		org.springframework.core.env.PropertySource<?> source =
		                              ae.getPropertySources().get("entity.props");
		Properties props = (Properties)source.getSource();
		
		for(Object key : props.keySet()){
			   System.out.println(props.get(key));
			   this.entity.put(key.toString(), props.get(key).toString());
			}
	}
	
	public IndEntity() {
		
		/* 
		 this.entity.put("nutch", "Food");
		 this.entity.put("cbiz", "CBIZ L");
		 this.entity.put("farmington", "Farmington Foods, Inc.");
		 this.entity.put("packaging", "Green Bay Packaging, Inc.");
		 this.entity.put("tollywood", "tollywood, This is Tollywood news");
	     this.entity.put("bollywood", "bollywood, This is Bollywood news.");
		 this.entity.put("wine", "wine,This is wine news.");
		 this.entity.put("cricket", "This is cricket news.");
		 this.entity.put("cricket", "This is cricket news.");

		 this.entity.put("pulwama", "This is pulwama news.");

		 this.entity.put("australia", "This is australia news");
		 this.entity.put("wine", "Superior Beverage Group, Ltd.");
		 this.entity.put("manufacturing", "Warrior Mfg LLC");
		 this.entity.put("snackindustry", "Wyoandot Inc");
		 this.entity.put("warrior", "Warrior Mfg LLC");
        */
	}

}
