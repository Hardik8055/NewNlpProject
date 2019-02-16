//package com.tcs.bmo.util;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;
//import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
//import org.apache.solr.common.util.NamedList;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import com.google.gson.JsonObject;
//
//public class SolrCellRequestDemo {
//	
////	private static final String DOCUMENT_URL = "http://192.168.1.105:8080/listener/documents/westpaclending2.pdf";
//
//	public static void main(String[] args) throws IOException, SolrServerException {
//		
//		SolrClient client = new HttpSolrClient.Builder().withBaseSolrUrl("http://localhost:8983/solr/crawler").build();
////		SolrClient client = new HttpSolrClient("http://localhost:8983/solr/mobdocuments");
//		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
//		req.addFile(new File("d:\\PDFBOT\\NADA_Report.pdf"), "application/pdf");
//		//req.addFile(new File("D:\\DL.jpg"), "image/jpg");		
//		
//		req.setParam("extractOnly", "true");
//		req.setParam("extractFormat", "application/xml");
//		String documentTitle = null;
//		NamedList<Object> result = client.request(req);
//		
//		//retrieve document details from response
//		if (result.get("null_metadata") instanceof NamedList<?>) {
//			NamedList<Object> documentInfo = (NamedList<Object>) result.get("null_metadata");
//			ArrayList<String> titles = (ArrayList<String>) documentInfo.get("title");
//			documentTitle = (titles != null && !titles.isEmpty()) ? titles.get(0) : null;
//		}
//
//		for (int i = 0; i < result.size(); i++) {
//			System.out.println("response is " + result.getName(i) + " " + result.getVal(i));
//			Object value = result.getVal(i);
//			//retrieve extracted data
//			if (value instanceof String) {
//				String htmlString = (String) value;
//				if (htmlString.contains("html")) {
//					Document document = Jsoup.parse(htmlString);
//					//extract data by page
//					Elements pageElements = document.getElementsByClass("page");
//					for (int j = 0; j < pageElements.size(); j++) {
//						String pageNumber = "page" + (j+1);
//						System.out.println("Div is " + pageElements.get(j).outerHtml());
//						//retrieve paragraphs in a page
//						/*Elements paragraphElements = pageElements.get(j).select("p");
//						for (Element element : paragraphElements) {
//							String paragraph = element.text();
//							System.out.println("Paragraph is " + paragraph);
//							if (paragraph != null && !"".equalsIgnoreCase(paragraph.trim())) {
//								JsonObject jsonElement = new JsonObject();
//								jsonElement.addProperty("text", paragraph);
//								jsonElement.addProperty("pageNumber", pageNumber);
//								jsonElement.addProperty("title", documentTitle);
//								storeData(client, jsonElement);
//							}
//						}*/
//						JsonObject jsonElement = new JsonObject();
//						jsonElement.addProperty("text", pageElements.get(j).text());
//						jsonElement.addProperty("pageNumber", pageNumber);
//						jsonElement.addProperty("title", documentTitle);
//						storeData(client, jsonElement);
//						
//					}
//				}
//			}
//		}
//		//client.commit();
//		client.close();
//	}
//	
//	private static void storeData(SolrClient client, JsonObject element) {
//		
//		byte[] jsonBytes = null;
//		try {
//			jsonBytes = element.toString().getBytes("UTF-8");
//			InputStream jsonInput = new ByteArrayInputStream(jsonBytes);
//			JSONUpdateRequest request = new JSONUpdateRequest(jsonInput);
//			request.process(client);
//			client.commit();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}