package com.tcs.bmo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
 
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
public class TestTwitter {

	public static void main(String[] arg) throws IOException{
		
		 
		        ConfigurationBuilder cb = new ConfigurationBuilder();
		        cb.setDebugEnabled(true).setOAuthConsumerKey("0YP8TX9r5Ur8q7nJHt4m5hekL")
		                .setOAuthConsumerSecret("bVCc9SZ3HxLPe5PO1EgLqS2mWiGYOIcDphJNisjpiFxXTVOjwr")
		                .setOAuthAccessToken("1072542401435193346-xhaPFAEltHsizV2ADXTOErBBsLBNfY")
		                .setOAuthAccessTokenSecret("6cJYzBxBfqdOH7jTDSwy2ggPF3WFjXWoDYiheW763WIS4");
		        TwitterFactory tf = new TwitterFactory(cb.build());
		        Twitter twitter = tf.getInstance();
		        Query query = new Query("#RISKUTHANAAMBANA");
		        query.setResultType(query.MIXED);
		        query.setCount(20);
		        query.setLocale("en");
		        query.setLang("en");
		        FileWriter writer = new FileWriter("H:/personal/TCS/Ideaton/test.txt");
		        try {
		            QueryResult queryResult = twitter.search(query);
		            writer.write(queryResult.getTweets().toString()+"/n");
		           //System.out.println(queryResult.getTweets().get(0).getURLEntities().toString());
		        } catch (TwitterException e) {
		            // ignore
		        	 writer.close();
		            e.printStackTrace();
		        }
		       
		 System.out.println("Done");
		 writer.close();
		
	}
}
