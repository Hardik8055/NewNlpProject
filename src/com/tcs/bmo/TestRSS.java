package com.tcs.bmo;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

public class TestRSS {

	public static SyndFeed getSyndFeedForUrl(String url) throws Exception {

	    SyndFeed feed = null;
	    InputStream is = null;

	    try {

	        URLConnection openConnection = new URL(url).openConnection();
	        is = new URL(url).openConnection().getInputStream();
	        if("gzip".equals(openConnection.getContentEncoding())){
	            is = new GZIPInputStream(is);
	        }
	        InputSource source = new InputSource(is);
	        SyndFeedInput input = new SyndFeedInput();
	        feed = input.build(source);

	    } catch (Exception e){
	        e.printStackTrace();
	    } finally {
	        if( is != null) is.close();
	    }

	    return feed;
	}

	public static void main(String[] args) {
	        SyndFeed feed;
	        try {
	            feed = getSyndFeedForUrl("https://www.just-auto.com/alerts/rssarticle.aspx");
	            List<SyndEntry> res = feed.getEntries();
	            for(Object o : res) {
	                System.out.println(((SyndEntryImpl) o).getDescription().getValue());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
}
