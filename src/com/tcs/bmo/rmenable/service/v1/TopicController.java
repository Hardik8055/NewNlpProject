package com.tcs.bmo.rmenable.service.v1;

import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tcs.bmo.util.Article;
import com.tcs.bmo.util.GoogleClient;
import com.tcs.bmo.util.IndEntity;
import com.tcs.bmo.util.NewsWithTopic;
import com.tcs.bmo.util.TopicCompare;

import java.io.*;

@RestController
public class TopicController {

	@Autowired
	SolrUpdateController solr;
	
	@Autowired
	NLPController nlpController;

	static String filename = TopicController.class.getClassLoader().getResource("config/sample.json").toString();

	// public static void main(String[] args) throws Exception // We hope this LDA
	public List<TopicCompare> processTopic(String filename) throws Exception {

		List<TopicCompare> st = new ArrayList<TopicCompare>();
		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/TopicStopList.txt"), "UTF-8", false, false,
				false));
		pipeList.add(new TokenSequence2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		/*
		 * Reader fileReader = new InputStreamReader(new FileInputStream( new
		 * File(TopicController.class.getResourceAsStream("config/sample.json"))
		 * ), "UTF-8");
		 */
		// Reader fileReader=new FileReader("C:/BMODev/TopicInput.txt");
		Reader fileReader = new FileReader(filename);
		instances
				.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data,
																															// label,
																															// name
																															// fields

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		int numTopics = 2;
		int numKeywords = 5;
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);
		// model.setNumTopics(100);
		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);
		int noIteration = 10;
		model.setNumIterations(noIteration);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();

		LabelSequence topics = model.getData().get(0).topicSequence;

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		/*
		 * for (int position = 0; position < tokens.getLength(); position++) {
		 * out.format("%s-%d ",
		 * dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)),
		 * topics.getIndexAtPosition(position)); } System.out.println(out);
		 */

		// Estimate the topic distribution of the first instance,
		// given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(0);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		StringBuffer sb1 = new StringBuffer();
		List<String> sb = new ArrayList<String>();
		// Show top 5 words in topics with proportions for the first document
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
			TopicCompare sTopic = new TopicCompare();
			out = new Formatter(new StringBuilder(), Locale.US);
			// out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			sTopic.setWaitage(topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < numKeywords) {
				IDSorter idCountPair = iterator.next();
				out.format("%s, ", dataAlphabet.lookupObject(idCountPair.getID()));
				rank++;
				// sb1.append("##"+dataAlphabet.lookupObject(idCountPair.getID()));
				// sb.add((String)dataAlphabet.lookupObject(idCountPair.getID()));
			}
			String str = out.toString();
			if (str != null && str.length() > 3) {
				str = str.substring(0, str.length() - 2);
				System.out.println(str);
				// sb.add("Topic "+(topic+1)+": "+str);
				sTopic.setTopicString(str);
			}
			st.add(sTopic);
		}
		// System.out.println("Hello"+sb.toString());
		// Create a new instance with high probability of topic 0
		StringBuilder topicZeroText = new StringBuilder();
		Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

		int rank = 0;
		while (iterator.hasNext() && rank < numKeywords) {
			IDSorter idCountPair = iterator.next();
			topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
			rank++;
		}

		// Create a new instance named "test instance" with empty target and
		// source fields.
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

		TopicInferencer inferencer = model.getInferencer();
		double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
		System.out.println("0\t" + testProbabilities[0]);
		System.out.println(st);
		return st;
	}
	
	
	// public static void main(String[] args) throws Exception // We hope this LDA
		public List<TopicCompare> processTopicByTfidf(String filename) throws Exception {

			List<TopicCompare> st = new ArrayList<TopicCompare>();
			// Begin by importing documents from text to feature sequences
			ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

			// Pipes: lowercase, tokenize, remove stopwords, map to features
			pipeList.add(new CharSequenceLowercase());
			pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
			pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/TopicStopList.txt"), "UTF-8", false, false,
					false));
			pipeList.add(new TokenSequence2FeatureSequence());

			InstanceList instances = new InstanceList(new SerialPipes(pipeList));
			/*
			 * Reader fileReader = new InputStreamReader(new FileInputStream( new
			 * File(TopicController.class.getResourceAsStream("config/sample.json"))
			 * ), "UTF-8");
			 */
			// Reader fileReader=new FileReader("C:/BMODev/TopicInput.txt");
			Reader fileReader = new FileReader(filename);
			instances
					.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data,
																																// label,
																																// name
																																// fields

			// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
			// Note that the first parameter is passed as the sum over topics, while
			// the second is the parameter for a single dimension of the Dirichlet
			// prior.
			int numTopics = 2;
			int numKeywords = 5;
			ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

			model.addInstances(instances);
			// model.setNumTopics(100);
			// Use two parallel samplers, which each look at one half the corpus and
			// combine
			// statistics after every iteration.
			model.setNumThreads(2);
			int noIteration = 10;
			model.setNumIterations(noIteration);
			model.estimate();

			// Show the words and topics in the first instance

			// The data alphabet maps word IDs to strings
			Alphabet dataAlphabet = instances.getDataAlphabet();

			FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();

			LabelSequence topics = model.getData().get(0).topicSequence;

			Formatter out = new Formatter(new StringBuilder(), Locale.US);
			/*
			 * for (int position = 0; position < tokens.getLength(); position++) {
			 * out.format("%s-%d ",
			 * dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)),
			 * topics.getIndexAtPosition(position)); } System.out.println(out);
			 */

			// Estimate the topic distribution of the first instance,
			// given the current Gibbs state.
			double[] topicDistribution = model.getTopicProbabilities(0);

			// Get an array of sorted sets of word ID/count pairs
			ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
			StringBuffer sb1 = new StringBuffer();
			List<String> sb = new ArrayList<String>();
			// Show top 5 words in topics with proportions for the first document
			for (int topic = 0; topic < numTopics; topic++) {
				Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
				TopicCompare sTopic = new TopicCompare();
				out = new Formatter(new StringBuilder(), Locale.US);
				// out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
				sTopic.setWaitage(topicDistribution[topic]);
				int rank = 0;
				/*while (iterator.hasNext() && rank < numKeywords) {
					IDSorter idCountPair = iterator.next();
					out.format("%s, ", dataAlphabet.lookupObject(idCountPair.getID()));
					rank++;
					// sb1.append("##"+dataAlphabet.lookupObject(idCountPair.getID()));
					// sb.add((String)dataAlphabet.lookupObject(idCountPair.getID()));
				}*/
				StringBuilder topicZeroText = new StringBuilder();
				
				while (iterator.hasNext() && rank < numKeywords) {
					IDSorter idCountPair = iterator.next();
					topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
					rank++;
				}
				String str = topicZeroText.toString();
				if (str != null && str.length() > 3) {
					str = str.substring(0, str.length() - 2);
					System.out.println(str);
					// sb.add("Topic "+(topic+1)+": "+str);
					sTopic.setTopicString(str);
				}
				st.add(sTopic);
			}
			// System.out.println("Hello"+sb.toString());
			// Create a new instance with high probability of topic 0
			/*StringBuilder topicZeroText = new StringBuilder();
			Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();
			int rank = 0;
			while (iterator.hasNext() && rank < numKeywords) {
				IDSorter idCountPair = iterator.next();
				topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
				rank++;
			}*/
/*
			// Create a new instance named "test instance" with empty target and
			// source fields.
			InstanceList testing = new InstanceList(instances.getPipe());
			testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

			TopicInferencer inferencer = model.getInferencer();
			double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
			System.out.println("0\t" + testProbabilities[0]);
			System.out.println(st);*/
			
			
			//for (String doc : docsList) {
				/*String[] keywords = doc.split(" ");
				List<String> docs = Arrays.asList(keywords);
				docsListCollection.add(docs);
				for (String term : keywords) {
					double wietage = service.tfIdf(docs,docsListCollection , term);
					System.out.println(" Wietage for the Term " + term  + "-" + wietage);
				}*/
				
				
				
			//}
			
			
			return st;
		}


	public List<NewsWithTopic> processTopicWithNews(String filename, String entityName, String entity) throws Exception {

		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		 /*IndEntity ie=new IndEntity();
		 String entity=null;
		 for(String key:ie.getEntity().keySet()){
			 if(ie.getEntity().get(key).equalsIgnoreCase(entityName)){
				 entity=key;
			 }
		 }*/
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(new File("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/TopicStopList.txt"), "UTF-8", false, false,
				false));
		pipeList.add(new TokenSequence2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		/*
		 * Reader fileReader = new InputStreamReader(new FileInputStream( new
		 * File(TopicController.class.getResourceAsStream("config/sample.json"))
		 * ), "UTF-8");
		 */
		// Reader fileReader=new FileReader("C:/BMODev/TopicInput.txt");
		Reader fileReader = new FileReader(filename);
		instances
				.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data,
																															// label,
																															// name
																															// fields

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		int numTopics = 2;
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);
		// model.setNumTopics(100);
		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);
		int noIteration=10;
		int noKeywords=5;
		model.setNumIterations(noIteration);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();

		LabelSequence topics = model.getData().get(0).topicSequence;

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		/*
		 * for (int position = 0; position < tokens.getLength(); position++) {
		 * out.format("%s-%d ",
		 * dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)),
		 * topics.getIndexAtPosition(position)); } System.out.println(out);
		 */

		// Estimate the topic distribution of the first instance,
		// given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(0);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		StringBuffer sb1 = new StringBuffer();
		List<String> sb = new ArrayList<String>();
		List<NewsWithTopic> resultTopic=new ArrayList<NewsWithTopic>();
		// Show top 5 words in topics with proportions for the first document
		//Thread.sleep(5000);
		 HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/"+entity+"news").build(); // Hotspot IP
		 
		for (int topic = 0; topic < numTopics; topic++) {
			NewsWithTopic nw=new NewsWithTopic();
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

			out = new Formatter(new StringBuilder(), Locale.US);
			// out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < noKeywords) {
				IDSorter idCountPair = iterator.next();
				out.format("%s ", dataAlphabet.lookupObject(idCountPair.getID()));
				rank++;
				// sb1.append("##"+dataAlphabet.lookupObject(idCountPair.getID()));
				// sb.add((String)dataAlphabet.lookupObject(idCountPair.getID()));
			}
			
			String str = out.toString();
			System.out.println("####################"+str);
			if (str != null) {
				//str="industry training network event leading ";
				TreeMap<Double, Article> hArticle = update_hArticles(instances, model, noIteration,
						client, str, noKeywords);
				System.out.println(hArticle.size());
				
				//if(hArticle.size()>=3){
					hArticle=hArticle.entrySet().stream()
							.collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), TreeMap::putAll);
					System.out.println(hArticle.size());

				//}
				List<Article> art=new ArrayList<Article>();
				for(Article a:hArticle.values()){
					art.add(a);
				}

				nw.setArticle(art);
				nw.setTopic(str);
				nw.setWaitageTopic(topicDistribution[topic]);
				nw.setEntity(entityName);
				resultTopic.add(nw);
			}
		}
		GoogleClient response1 = solr.updateTopicContentWithNews(entityName);
		
		out.close();
		resultTopic=resultTopic.stream().sorted(Comparator.comparing(NewsWithTopic::getWaitageTopic).reversed()).collect(Collectors.toList());
		return resultTopic;

	}

	private TreeMap<Double, Article> update_hArticles(InstanceList instances,
			ParallelTopicModel model, int noIteration, HttpSolrClient client, String str, int key_words_count) throws UnirestException, Exception {
		GoogleClient googleClient = new GoogleClient();
		googleClient = solr.updateTopicContentWithNews(str+"US");
		TreeMap<Double, Article> hArticle=new TreeMap<Double, Article>(Collections.reverseOrder());
		if(googleClient.getArticles()!=null){
			for (Article article : googleClient.getArticles()) {
				if(article.getContent()!=null){
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json;
					try {
						json = ow.writeValueAsString(article);
						JsonObject jsonContent = solr.getJsonContent(json);
						//System.out.println(json);
						solr.updateContent(client,jsonContent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					InstanceList testing = new InstanceList(instances.getPipe());
					testing.addThruPipe(new Instance(article.getContent(), null, "test instance", null));
					TopicInferencer inferencer = model.getInferencer();
					double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), noIteration, 1, 5);
					System.out.println("0\t" + testProbabilities[0]);
					hArticle.put(testProbabilities[0], article);
				}
			}
			
			// To check records, If there are no found.
			
			if(googleClient.getArticles().isEmpty() && key_words_count > 1) {
				--key_words_count;
				String[] strArray = str.split(" ");
				StringBuilder sb = new StringBuilder();
				for (int i=0;i<key_words_count;i++) {
					sb.append(strArray[i]).append(" ");
				}
				update_hArticles(instances, model, noIteration, client, sb.toString().trim(), key_words_count);
			}
		}
		return hArticle;
	}

	/*private List<Article> doNLPProcessForRemainingArticles(TreeMap<Double, Article> hArticle,
			TreeMap<Double, Article> resultedTop3HArticle, TreeMap<Double, Article> resultedRemainingHArticle)
					throws ClassNotFoundException, IOException, ParseException, Exception {
		for(Entry<Double, Article> entryHArticle: hArticle.entrySet()) {
			if(resultedTop3HArticle.get(entryHArticle.getKey()) != null) {

			}else {
				// We caught remaining 17.
				resultedRemainingHArticle.put(entryHArticle.getKey(), entryHArticle.getValue());
			}
		}


		List<Article> artForRemaining=new ArrayList<Article>();
		for(Article a:resultedRemainingHArticle.values()){
			artForRemaining.add(a);
		}

		List<Article> nlpProcessed = nlpController.processNLP(artForRemaining);
		return nlpProcessed;
	}*/

}
