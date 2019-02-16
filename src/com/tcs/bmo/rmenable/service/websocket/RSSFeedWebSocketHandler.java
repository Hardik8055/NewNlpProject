package com.tcs.bmo.rmenable.service.websocket;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.xml.sax.InputSource;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

@Component
public class RSSFeedWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	ApplicationContext context;
	private static Logger logger = LoggerFactory.getLogger(RSSFeedWebSocketHandler.class);

	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
		logger.error("error occured at sender " + session, throwable);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("Connected ... " + session.getId());

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
		logger.info("message received: getPayload format" + jsonTextMessage.getPayload());
		
		 SyndFeed feed;
	        try {
	            feed = getSyndFeedForUrl("https://www.just-auto.com/alerts/rssarticle.aspx");
	            System.out.println("Got the feed output"+feed);
	            List<SyndEntry> res = feed.getEntries();
	            for(SyndEntry newsEntry : res) {	
	            	TextMessage responseMessage = new TextMessage(newsEntry.getDescription().getValue());
	            	System.out.println("Inside for loop message = "+newsEntry.getDescription().getValue());
					session.sendMessage(responseMessage);
//					wait(1000);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
	
	private static SyndFeed getSyndFeedForUrl(String url) throws Exception {

	    SyndFeed feed = null;
	    InputStream is = null;

	    try {

	        URLConnection openConnection = new URL(url).openConnection();
	        is = new URL(url).openConnection().getInputStream();
	        if("gzip".equals(openConnection.getContentEncoding())){
	            is = new GZIPInputStream(is);
	        }
	        System.out.println("URL Connection Successful");
	        InputSource source = new InputSource(is);
	        SyndFeedInput input = new SyndFeedInput();
	        feed = input.build(source);
	        System.out.println("Got the feed");

	    } catch (Exception e){
	        e.printStackTrace();
	    } finally {
	        if( is != null) is.close();
	    }

	    return feed;
	}
	
}
