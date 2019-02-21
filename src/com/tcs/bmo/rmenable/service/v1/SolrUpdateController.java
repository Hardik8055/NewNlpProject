package com.tcs.bmo.rmenable.service.v1;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tcs.bmo.util.Article;
import com.tcs.bmo.util.GoogleClient;
import com.tcs.bmo.util.IndEntity;
import com.tcs.bmo.util.JSONUpdateRequest;
import com.tcs.bmo.util.SearchTopic;

@RestController
public class SolrUpdateController {
	
	@Autowired
	SolrController solr;
	
	public String entity;
	
	private final ObjectMapper mapper = new ObjectMapper();
	Properties prop = new Properties();
	InputStream inStream; 
	 @RequestMapping(value="/update", method = RequestMethod.GET)	
	 public ResponseEntity<List<SolrDocument>> updateSearchContent(@RequestParam String searchQuery) throws UnirestException, Exception
	 {
		 System.out.println("Inside SolrUpdateController1 ");		 
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.233:8983/solr/nutch").build(); // WIFI
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.233:8983/solr/update/json").build(); // WIFI
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.225:8983/solr/nutch").build(); - DELL NETWORK
		 HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/"+this.entity).build(); // Hotspot IP
		 		 
		 //	Search through newsAPI
		 loadSolrConfiguration();
		 //String searchResult = getContentFromConfig("input3");
	/*	 String auto = getContentFromConfig("auto");
		 String[] autoResult = auto.split(",");*/
		 /* String searchResult = searchWeb(queryString);
		 System.out.println("searchResult-->"+searchResult);
		 JsonObject jsonContent = getJsonContent(searchResult);*/
		 //HttpResponse<JsonNode> response = searchWeb(searchQuery);
		 String response = searchWeb1(searchQuery);
		 
		 Gson gson = new Gson();
		 GoogleClient googleClient = gson.fromJson(response,GoogleClient.class);
		 System.out.println(googleClient.getArticles().size());
		 
		 for (Article article : googleClient.getArticles()) {
			 ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			 String json;
			try {
				json = ow.writeValueAsString(article);
				JsonObject jsonContent = getJsonContent(json);
				System.out.println(json);
				 updateContent(client,jsonContent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
				}
				
		 
		System.out.println(response);
		
		 /*GoogleClient userResponse =
					mapper.readValue(response.getRawBody(),
					GoogleClient.class);*/

		// updateContent(client,response);	
		 //String searchResult = getURLContent();
		 
		 //System.out.println("searchResult-->"+searchResult);
		 //System.out.println("searchResult length-->"+searchResult.length());
		
		 System.out.println("Update complete");
		 return new ResponseEntity<List<SolrDocument>>(HttpStatus.OK);
	 }
	
	 public GoogleClient updateTopicContentWithNews(String searchQuery) throws UnirestException, Exception
	 {
		 Gson gson = new Gson();
		 String response1 = searchWeb1(searchQuery.toString());
				 GoogleClient googleClient=gson.fromJson(response1,GoogleClient.class);
				 if(googleClient!=null)
					 System.out.println(googleClient.getArticles().size());
					 
					 
		return googleClient;
	 }
	

	 @RequestMapping(value="/topicSearch", method = RequestMethod.POST)	
	 public ResponseEntity<List<Article>> updateTopicContent(@RequestBody SearchTopic searchQuery) throws UnirestException, Exception
	 {
		 IndEntity ie=new IndEntity();
		 String entityName=searchQuery.getEntity();
		 String entity=null;
		 for(String key:ie.getEntity().keySet()){
			 if(ie.getEntity().get(key).equalsIgnoreCase(entityName)){
				 entity=key;
			 }
		 }
		 System.out.println("Inside aws content ");		 
		 HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/"+entity+"news").build(); // Hotspot IP
		 		 
		 //	Search through newsAPI
		 //loadSolrConfiguration();
		 int random = (int )(Math. random() * 50 + 1);
		 String filename=String.valueOf(random);
		 FileWriter writer = new FileWriter(filename+".txt");
		 String topics[]=searchQuery.getTopic();
		 int top=1;
		 int down=1;
		 int key=1;
		 StringBuffer sbuffer=new StringBuffer();
		 Gson gson = new Gson();
		 for(String topic:topics){
			 down=1;
			 topic=topic.substring(topic.indexOf(':')+2, topic.length());
			 String keywords[]=topic.split(", ");
			 for(String keyword:keywords){
				sbuffer.append(keyword+" ");
				 /*String response=searchWeb1(keyword);
				 GoogleClient googleClient = gson.fromJson(response,GoogleClient.class);
				 
				if(googleClient!=null)
				 System.out.println(googleClient.getArticles().size());
				 
				 for (Article article : googleClient.getArticles()) {
					 ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					 String json;
					try {
						json = ow.writeValueAsString(article);
						JsonObject jsonContent = getJsonContent(json);
						//System.out.println(json);
						 updateContent(client,jsonContent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
				 if(key==1){
					 String response1 = searchWeb1(entityName);
					 googleClient.addArticles(gson.fromJson(response1,GoogleClient.class).getArticles());
					 System.out.println(googleClient.getArticles().size());
					 key=2;
				 }*/
				
					System.out.println(entityName+" " +keyword);	
				 writer.write(keyword+"\t"+"Topic"+top+""+down+"\n");
				//System.out.println(response);
				 down++;
			 }
			 
				 String response1 = searchWeb1(sbuffer.toString());
				 GoogleClient googleClient=gson.fromJson(response1,GoogleClient.class);
				 if(googleClient!=null)
					 System.out.println(googleClient.getArticles().size());
					 
					 for (Article article : googleClient.getArticles()) {
						 ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						 String json;
						try {
							json = ow.writeValueAsString(article);
							JsonObject jsonContent = getJsonContent(json);
							//System.out.println(json);
							 updateContent(client,jsonContent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
							}
				 System.out.println(googleClient.getArticles().size());
				
			
			 
			 top++;
		 }
		 writer.close();
		
		 //app
		 File file=new File(filename+"+txt");
		 List<Article> art= solr.displayTopicDetails("*:*","1000", filename+".txt", topics.length,entity);
	//	 art=
		 Set<Article> setTopic=art.stream().collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Article::getUrl))));
		 art.clear();
		 art.addAll(setTopic);
		//file.delete();	
		return new ResponseEntity<List<Article>>(art,HttpStatus.OK);
	 }
	
	 
	 @RequestMapping(value="/dealer", method = RequestMethod.GET)	
	 public ResponseEntity<List<SolrDocument>> updateDealerSearchContent(@RequestParam String searchQuery) throws UnirestException, Exception
	 {
		 System.out.println("Inside SolrUpdateController1 ");		 
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.233:8983/solr/nutch").build(); // WIFI
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.233:8983/solr/update/json").build(); // WIFI
		 //HttpSolrClient client = new HttpSolrClient.Builder("http://172.16.244.225:8983/solr/nutch").build(); - DELL NETWORK
		 HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/dealer").build(); // Hotspot IP
		 		 
		 //	Search through newsAPI
		 loadSolrConfiguration();
		 //String searchResult = getContentFromConfig("input3");
	/*	 String auto = getContentFromConfig("auto");
		 String[] autoResult = auto.split(",");*/
		 /* String searchResult = searchWeb(queryString);
		 System.out.println("searchResult-->"+searchResult);
		 JsonObject jsonContent = getJsonContent(searchResult);*/
		 //HttpResponse<JsonNode> response = searchWeb(searchQuery);
		 String response = searchWeb1(searchQuery);
		 
		 Gson gson = new Gson();
		 GoogleClient googleClient = gson.fromJson(response,GoogleClient.class);
		 System.out.println(googleClient.getArticles().size());
		 
		 for (Article article : googleClient.getArticles()) {
			 ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			 String json;
			try {
				json = ow.writeValueAsString(article);
				JsonObject jsonContent = getJsonContent(json);
				System.out.println(json);
				 updateContent(client,jsonContent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
				}
				
		 
		System.out.println(response);
		
		 /*GoogleClient userResponse =
					mapper.readValue(response.getRawBody(),
					GoogleClient.class);*/

		// updateContent(client,response);	
		 //String searchResult = getURLContent();
		 
		 //System.out.println("searchResult-->"+searchResult);
		 //System.out.println("searchResult length-->"+searchResult.length());
		 
		 
		 
		 System.out.println("Update complete");
		 return new ResponseEntity<List<SolrDocument>>(HttpStatus.OK);
	 }
	
	
	 private String searchWeb1(String queryString)
	 {
		 System.out.println("Inside searchWeb() 2");
		 JsonObject json = new JsonObject();
		 String searchResult = "";
		 String query = "toyota";
		 try {
		/*	System.setProperty("https.proxyHost", "http://proxy.tcs.com");
			System.setProperty("https.proxyPort", "8080"); */
			//String httpsURL = "https://newsapi.org/v2/everything?q="+queryString+"&from=2018-10-18&sortBy=relavancy&apiKey=a588e9af0c794b2b9c655ea7dc0e9b1b";
			String httpsURL = "http://newsapi.org/v2/everything?q="+URLEncoder.encode(queryString,"UTF-8")+"&sortBy=relavancy&apiKey=a588e9af0c794b2b9c655ea7dc0e9b1b";
			//String encodeURL = URLEncoder.encode();
			System.out.println("httpsURL--->"+httpsURL);
			URL myUrl = new URL(httpsURL);
			URLConnection conn = (URLConnection) myUrl.openConnection();
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			} 
			br.close();
			System.out.println("URL Response 1-->"+response.toString());
			searchResult = response.toString();
			System.out.println("searchResult 1-->"+searchResult);
			
		} catch (Exception e) {
			System.out.println("Inside Exception"+e);
			e.printStackTrace();
		}		 
		 return searchResult;	        
	 }	
	 
	 private HttpResponse<JsonNode> searchWeb(String queryString) throws UnirestException, UnsupportedEncodingException
	 {
		 queryString= queryString.replaceAll(" ","+");
		 System.out.println(queryString);
		 queryString=URLEncoder.encode(queryString,"UTF-8");
	//	 Unirest.setProxy(new HttpHost("10.199.252.62",8080,"http"));
		 
		return Unirest.get("http://newsapi.org/v2/everything?q="+queryString+"&from=2018-10-18&sortBy=relavancy&apiKey=a588e9af0c794b2b9c655ea7dc0e9b1b")
			.header("Accept", "Application/json")
			.asJson();
		        
	 }	
	 
	 public void updateContent(HttpSolrClient client, JsonObject json)
	 {
		 System.out.println("Inside updateContent 11");
		 byte[] jsonBytes = null;
			try {
				jsonBytes = json.toString().getBytes("UTF-8");
				InputStream jsonInput = new ByteArrayInputStream(jsonBytes);
				JSONUpdateRequest request = new JSONUpdateRequest(jsonInput);
				request.process(client);
				client.commit();
				System.out.println("Inside updateContent22");
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 private void updateJson(HttpSolrClient client,String uploadData)
	 {
		 //SolrInputDocument input = new SolrInputDocument();
		 SolrInputDocument input = new SolrInputDocument(uploadData);
		 //input.addField("id", "A");
		 //input.addField("value", 10);
		 try {
			client.add(input);
			client.commit();
		} catch (Exception e) {
			System.out.println("Exception in updateJson"+e);
			e.printStackTrace();
		}		 
	 }
	 private void loadSolrConfiguration()
	 {
		 System.out.println("Inside loadSolrConfiguration");
		 try 
		 {
			 	inStream = getClass().getClassLoader().getResourceAsStream("config/SolrConfig.properties");
			 	System.out.println("1-->"+inStream);
				if (inStream != null) 
				{
					System.out.println("2--");
					prop.load(inStream);
				} 
		} catch (Exception e) {
			System.out.println("Exception in loading configuration file");
		}
	 }
	 
	 private String getContentFromConfig(String content){
		 System.out.println("inside getContentFromConfig");
		 return prop.getProperty(content);		 
	 }
	 
	 public JsonObject getJsonContent(String searchResult)
	 {
		 System.out.println("Inside getJsonContent 1");
		 JsonObject json = new JsonObject();
		 try {			
			JsonParser parser = new JsonParser();
			json = (JsonObject)parser.parse(searchResult.trim()).getAsJsonObject();
			
			/*String result = json.get("status").getAsString();
			System.out.println("result--> 2"+result);*/
			
		} catch (Exception e) {
			System.out.println("Inside Exception"+e);
			e.printStackTrace();
		}		 
		 return json;	        
	 }
	 private String getQueryValue()
	 {
		 	String contentArray = getContentFromConfig("auto");
			ArrayList<String> aList= new ArrayList<String> (Arrays.asList(contentArray.split(",")));
			StringBuilder commaSepValueBuilder = new StringBuilder();		
			for(int i=0;i<aList.size();i++)
			{			
				commaSepValueBuilder.append(aList.get(i));
				if ( i != aList.size()-1){
			        commaSepValueBuilder.append(" OR ");
			      }
			}			
			return commaSepValueBuilder.toString();
	 }
}
