//package com.tcs.bmo.util;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;
//import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
//import org.apache.solr.client.solrj.request.UpdateRequest;
//import org.apache.solr.common.util.NamedList;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.google.gson.JsonObject;
//
//public class Webcrawler {
//
//	// private static final String DOCUMENT_URL =
//	// "http://192.168.1.105:8080/listener/documents/westpaclending2.pdf";
//
//	private static final int MAX_DEPTH = 2;
//	private HashSet<String> links;
//	int pageNumber = 1;
//	
//	public Webcrawler(){
//		links = new HashSet<>();
//	}
//	public void getPageLinks(String URL, int depth) {
//		SolrClient client = new HttpSolrClient.Builder().withBaseSolrUrl("http://localhost:8983/solr/crawler").build();
//        if ((!links.contains(URL) && (depth < MAX_DEPTH))) {
//            System.out.println(">> Depth: " + depth + " [" + URL + "]");
//            try {
//                links.add(URL);
//                
//                Document document = Jsoup.connect(URL).proxy("proxy.tcs.com", 8080).get();
//                Elements linksOnPage = document.select("a[href]");
//
//                depth++;
//                for (Element page : linksOnPage) {
//                	System.out.println("Indexing page results = "+page.id());
//					JsonObject jsonElement = new JsonObject();
//					jsonElement.addProperty("text", page.text());
//					jsonElement.addProperty("linkNumber", pageNumber++);
//					jsonElement.addProperty("title", page.getElementsByAttribute("data-title").val());
//					
//					System.out.println("title = "+page.text());
//					System.out.println("title = "+page.getElementsByAttribute("data-title").val());
//					
//					storeData(client, jsonElement);
//					getPageLinks(page.attr("abs:href"), depth);
//                    
//                }
//            } catch (IOException e) {
//                System.err.println("For '" + URL + "': " + e.getMessage());
//            }
//        }
//	}
//	
//	
//	public static void main(String[] args) throws IOException, SolrServerException {
//		new Webcrawler().getPageLinks("http://www.autonews.com/", 0);
//		
//	}
//
//	private static void storeData(SolrClient client, JsonObject element) {
//
//		byte[] jsonBytes = null;
//		try {
//			jsonBytes = element.toString().getBytes("UTF-8");
//			// InputStream jsonInput = new ByteArrayInputStream(jsonBytes);
//			UpdateRequest request = new UpdateRequest(element.toString());
//			request.process(client);
//			client.commit();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}