package com.tcs.bmo.rmenable.service.v1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tcs.bmo.util.Article;
import com.tcs.bmo.util.GoogleClient;
import com.tcs.bmo.util.IndEntity;
import com.tcs.bmo.util.NewsWithTopic;
import com.tcs.bmo.util.SearchTopic;
import com.tcs.bmo.util.SolrDocResult;
import com.tcs.bmo.util.SolrResult;
import com.tcs.bmo.util.Source;
import com.tcs.bmo.util.TopicCompare;


@RestController
public class SolrController {

	@Autowired
	NLPController nlpController;

	@Autowired
	TopicController topic;
	
	@Autowired
	SolrUpdateController solrU;

	@Autowired
	IndEntity ent;

	@RequestMapping(value = "/news", method = RequestMethod.GET)
	public ResponseEntity<SolrResult> displayQueryDetails(@RequestParam("queryDetails") String queryDetails, @RequestParam("row") String row)
			throws ClassNotFoundException, ParseException, Exception {
		// ModelAndView model = new ModelAndView();

		SolrDocumentList results = getSolrDocument("nutch", queryDetails, row);
		SolrDocumentList resultDealer = getSolrDocument("dealer", queryDetails, row);
		List<SolrDocument> listSolrDocument = null;
		List<SolrDocument> listSolrDealerDoc = null;
		// System.out.println("list size"+ listSolrDocument.size());
		int count = 1;
		if (results != null && results.size() > 0) {
			System.out.println("results.size() -->" + results.size());
			if (results.size() > 10) {
				System.out.println("count if-->" + count);
				listSolrDocument = results.subList(0, results.size());
			} else {
				System.out.println("count else-->" + count);
				listSolrDocument = results.subList(0, results.size());
			}
			count++;
		}
		listSolrDealerDoc = resultDealer.subList(0, resultDealer.size());
		SolrDocResult scr = new SolrDocResult();
		SolrResult resultFinal = new SolrResult();
		List<Article> article = new ArrayList<>();
		List<Article> dealerArticle = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (SolrDocument doc : listSolrDocument) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(doc);
			scr = mapper.readValue(json, SolrDocResult.class);
			article.add(convert(scr));
		}
		for (SolrDocument doc : listSolrDealerDoc) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(doc);
			scr = mapper.readValue(json, SolrDocResult.class);
			dealerArticle.add(convert(scr));
		}
		List<Article> art = nlpController.processNLP(article);
		List<Article> generalArticle = new ArrayList<>();

		List<Article> saarArticle = new ArrayList<>();
		for (Article a : art) {
			if (a.getCatagory() == 1)
				generalArticle.add(a);
			if (a.getCatagory() == 2)
				dealerArticle.add(a);
			if (a.getCatagory() == 3)
				saarArticle.add(a);
		}
		resultFinal.setArticles(generalArticle);
		resultFinal.setDealer(dealerArticle);
		resultFinal.setSaar(saarArticle);

		resultFinal.setTotalResults(10);
		resultFinal.setStatus("OK");

		System.out.println("listSolrDocument.size()-->" + listSolrDocument.size());
		return new ResponseEntity<SolrResult>(resultFinal, HttpStatus.OK);

	}

	public List<Article> displayTopicDetails(String queryDetails, String row, String filename, int numTopic,
			String entity) throws ClassNotFoundException, ParseException, Exception {
		ent.config();
		SolrDocumentList resultCrawl = getSolrDocument(entity + "news", queryDetails, row);
		String strEntity = "";
		if (ent.getEntity() != null) {
			strEntity = ent.getEntity().get(entity);
		} else {
			strEntity = entity;
		}
		SolrDocumentList results = getSolrDocument(entity, "content:*" + ent + "*", row);

		// ModelAndView model = new ModelAndView();

		List<SolrDocument> listSolrDocument = new ArrayList<>();

		// System.out.println("list size"+ listSolrDocument.size());
		int count = 1;
		if (results != null && results.size() > 0) {
			System.out.println("results.size() -->" + results.size());
			if (results.size() > 10) {
				System.out.println("count if-->" + count);
				// listSolrDocument = results.subList(0,results.size());
			} else {
				System.out.println("count else-->" + count);
				// listSolrDocument = results.subList(0, results.size());
			}
			count++;
		}
		for (SolrDocument s : resultCrawl) {
			listSolrDocument.add(s);
		}
		SolrDocResult scr = new SolrDocResult();
		List<Article> article = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (SolrDocument doc : listSolrDocument) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(doc);
			scr = mapper.readValue(json, SolrDocResult.class);
			article.add(convertTopicArticle(scr));
		}

		System.out.println(article);
		List<Article> art = nlpController.processTopicNLP(article, filename, numTopic);
		for (SolrDocument sd : results) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(sd);
			scr = mapper.readValue(json, SolrDocResult.class);
			art.add(convertTopicArticle(scr));
		}

		System.out.println("listSolrDocument.size()-->" + listSolrDocument.size());
		return art;

	}

	@RequestMapping(value = "/Gtopic", method = RequestMethod.GET)
	public ResponseEntity<List<SearchTopic>> displayTopics(@RequestParam String row)
			throws ClassNotFoundException, ParseException, Exception {
		List<SearchTopic> topicText = new ArrayList<>();

		for (String en : ent.getEntity().keySet()) {
			SearchTopic st = new SearchTopic();
			String queryDetails = "*:*";
			SolrDocumentList results = getSolrDocument(en, queryDetails, row);
			List<SolrDocument> listSolrDocument = null;

			if (results != null && results.size() > 0) {
				System.out.println("results.size() -->" + results.size());
				listSolrDocument = results.subList(0, results.size());

			}
			StringBuilder sb = new StringBuilder();
			for (SolrDocument doc : listSolrDocument) {
				sb.append(doc.getFieldValue("content")).append("/n");
			}
			FileWriter writer = new FileWriter("../desc.txt");
			writer.write(sb.toString());
			writer.close();
			System.out.println(en + " EEEEEEEEEEEEEEEEEE" + ent.getEntity().get(en));
			st.setEntity(ent.getEntity().get(en));
			
			st.setTopicList(topic.processTopic("../desc.txt").stream().map(TopicCompare::getTopicString)
					.collect(Collectors.toList()));
			st.setSelectedTopic(new ArrayList<>()); // what supposed to be pass here, for now its empty
			topicText.add(st);

		}
		return new ResponseEntity<List<SearchTopic>>(topicText, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/tfidf", method = RequestMethod.GET)
	public ResponseEntity<List<SearchTopic>> displayTopicsUsingTfIdf(@RequestParam String row)
			throws ClassNotFoundException, ParseException, Exception {
		List<SearchTopic> topicText = new ArrayList<>();
		ent.config();
		for (String en : ent.getEntity().keySet()) {
			SearchTopic st = new SearchTopic();
			String queryDetails = "*:*";
			SolrDocumentList results = getSolrDocument(en, queryDetails, row);
			List<SolrDocument> listSolrDocument = null;

			if (results != null && results.size() > 0) {
				System.out.println("results.size() -->" + results.size());
				listSolrDocument = results.subList(0, results.size());

			}
			StringBuilder sb = new StringBuilder();
			List<String> docsList = new ArrayList<>();
			List<List<String>> docsListCollection = new ArrayList<>();
			for (SolrDocument doc : listSolrDocument) {
				sb.append(doc.getFieldValue("content"));
				sb.append("/n");
				docsList = Arrays.asList(doc.getFieldValue("content").toString().split(" "));
				docsListCollection.add(docsList);
			}
			FileWriter writer = new FileWriter("../desc.txt");
			writer.write(sb.toString());
			writer.close();
			System.out.println(en + " EEEEEEEEEEEEEEEEEE" + ent.getEntity().get(en));
			st.setEntity(ent.getEntity().get(en));
			
			st.setTopicList(topic.processTopicByTfidf("../desc.txt").stream().map(TopicCompare::getTopicString)
					.collect(Collectors.toList()));
			st.setSelectedTopic(new ArrayList<>()); // what supposed to be pass here, for now its empty
			topicText.add(st);
			TfIdfService service = new TfIdfService();

			for (String tc : st.getTopicList()) {
				//docsList.add(tc.getTopicString());
					
			}
			for (String tc : st.getTopicList()) {
				docsList = Arrays.asList(tc.split(" "));
				for (String term : docsList) {
					double wietage = service.tfIdf(docsList,docsListCollection , term);
					System.out.println(" weightage  for the Term " + term  + ":" + wietage);
				}
			}

		}
		return new ResponseEntity<List<SearchTopic>>(topicText, HttpStatus.OK);
	}

	@RequestMapping(value = "/EntityNews", method = RequestMethod.GET)
	public ResponseEntity<List<NewsWithTopic>> displayTopicsWithNews()
			throws ClassNotFoundException, ParseException, Exception {
		List<NewsWithTopic> topicNews=new ArrayList<>();
		
		for (String en : ent.getEntity().keySet()) {
			NewsWithTopic nwt=new NewsWithTopic();
			GoogleClient gc=solrU.updateTopicContentWithNews(ent.getEntity().get(en));
			nwt.setArticle(gc.getArticles());
			nwt.setEntity(ent.getEntity().get(en));
			topicNews.add(nwt);
			}
		return new ResponseEntity<List<NewsWithTopic>>(topicNews, HttpStatus.OK);
	}
	@RequestMapping(value = "/NewsTopic", method = RequestMethod.GET)
	public ResponseEntity<List<NewsWithTopic>> displayTopicsWithNews(@RequestParam String row)
			throws ClassNotFoundException, ParseException, Exception {
		List<NewsWithTopic> topicNews=new ArrayList<>();
		ent.config();
		for (String en : ent.getEntity().keySet()) {
			SearchTopic st = new SearchTopic();
			
			String queryDetails = "*:*";
			SolrDocumentList results = getSolrDocument(en, queryDetails, row);
			List<SolrDocument> listSolrDocument = null;

			if (results != null && results.size() > 0) {
				System.out.println("results.size() -->" + results.size());
				listSolrDocument = results.subList(0, results.size());

			}
			StringBuilder sb = new StringBuilder();
			for (SolrDocument doc : listSolrDocument) {
				sb.append(doc.getFieldValue("content"));
				sb.append("/n");
			}
			FileWriter writer = new FileWriter("../desc.txt");
			writer.write(sb.toString());
			writer.close();
			System.out.println(en + " EEEEEEEEEEEEEEEEEE" + ent.getEntity().get(en));
			st.setEntity(ent.getEntity().get(en));
			List<NewsWithTopic> preTopic=topic.processTopicWithNews("../desc.txt", st.getEntity(), en);
			if(preTopic.size()>=3){
				preTopic=preTopic.subList(0, 3);
			}
			for(NewsWithTopic nt:preTopic ){
				topicNews.add(nt);
			}
//			NewsWithTopic nwt=new NewsWithTopic();
//			GoogleClient gc=solrU.updateTopicContentWithNews("fabricated structural metal manufacturing");
//			nwt.setArticle(gc.getArticles());
//			nwt.setEntity(ent.getEntity().get(en));
//			topicNews.add(nwt);

		}
		
		
		return new ResponseEntity<List<NewsWithTopic>>(topicNews, HttpStatus.OK);
	}

	public SolrDocumentList getSolrDocument(String core, String queryDetails, String row) {
		HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/" + core).build();
		client.setParser(new XMLResponseParser());
		System.out.println(queryDetails);
		SolrQuery query = new SolrQuery(queryDetails);
		Map<String, String> p = new HashMap<String, String>();
		p.put("rows", row);
		SolrParams params = new MapSolrParams(p);

		query.add(params);
		QueryResponse response = null;
		try {

			System.out.println("query-->" + query);
			response = client.query(query);
			// System.out.println("response-->"+response);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return response.getResults();

	}

	public Article convert(SolrDocResult doc) {

		Article ar = new Article();
		ar.setAuthor(doc.getAuthor());
		ar.setContent(doc.getContent());
		ar.setDescription(doc.getDescription());
		ar.setPublishedAt(doc.getPublishedAt());
		Source spr = new Source();
		spr.setId(doc.getSourceId());
		spr.setName(doc.getSourceName());
		ar.setSource(spr);
		ar.setTitle(doc.getTitle());
		ar.setUrl(doc.getUrl());
		ar.setUrlToImage(doc.getUrlToImage());
		return ar;

	}

	public Article convertTopicArticle(SolrDocResult doc) {

		Article ar = new Article();
		// ar.setAuthor(doc.getAuthor());
		ar.setContent(doc.getContent());
		// ar.setDescription(doc.getDescription());
		// ar.setPublishedAt(doc.getPublishedAt());
		/*
		 * Source spr=new Source(); spr.setId(doc.getSourceId());
		 * spr.setName(doc.getSourceName()); ar.setSource(spr);
		 */ar.setTitle(doc.getTitle());
		ar.setUrl(doc.getUrl());
		// ar.setUrlToImage(doc.getUrlToImage());
		return ar;

	}
}
