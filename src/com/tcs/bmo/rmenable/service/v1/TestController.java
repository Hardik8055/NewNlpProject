package com.tcs.bmo.rmenable.service.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.tcs.bmo.util.Article;
import com.tcs.bmo.util.GoogleClient;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TestController {

	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, Exception {
	

		        // Begin by importing documents from text to feature sequences
		        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		        // Pipes: lowercase, tokenize, remove stopwords, map to features
		        pipeList.add( new CharSequenceLowercase() );
		        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		        pipeList.add( new TokenSequenceRemoveStopwords(new File("/Users/HardikBharat/eclipse-workspacefinal/NLPProject/misc/TopicStopList.txt"), "UTF-8", false, false, false) );
		        pipeList.add( new TokenSequence2FeatureSequence() );

		        InstanceList instances = new InstanceList (new SerialPipes(pipeList));
		        /*Reader fileReader = new InputStreamReader(new FileInputStream(
		        		new File(TopicController.class.getResourceAsStream("config/sample.json"))), "UTF-8");
		      */  
		      Reader fileReader=new FileReader("/Users/HardikBharat/eclipse-workspacefinal/NLPProject/misc/TopicInput.txt");
		    //   Reader fileReader=new FileReader(filename);
		        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
		                                               3, 2, 1)); // data, label, name fields

		        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		        //  Note that the first parameter is passed as the sum over topics, while
		        //  the second is the parameter for a single dimension of the Dirichlet prior.
		        int numTopics = 5;
		        int numKeywords=5;
		        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		        model.addInstances(instances);
		      //  model.setNumTopics(100);
		        // Use two parallel samplers, which each look at one half the corpus and combine
		        //  statistics after every iteration.
		        model.setNumThreads(2);

		        model.setNumIterations(100);
		        model.estimate();

		        // Show the words and topics in the first instance

		        // The data alphabet maps word IDs to strings
		        Alphabet dataAlphabet = instances.getDataAlphabet();
		        
		        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();

		        LabelSequence topics = model.getData().get(0).topicSequence;
		        
		      Formatter out = new Formatter(new StringBuilder(), Locale.US);
		        for (int position = 0; position < tokens.getLength(); position++) {
		            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
		        }
		        System.out.println(out);
		        
		        // Estimate the topic distribution of the first instance, 
		        //  given the current Gibbs state.
		        double[] topicDistribution = model.getTopicProbabilities(0);

		        // Get an array of sorted sets of word ID/count pairs
		        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		       StringBuffer sb1=new StringBuffer();
		        	List<String> sb=new ArrayList<String>();
		        // Show top 5 words in topics with proportions for the first document
		        for (int topic = 0; topic < numTopics; topic++) {
		            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
		            
		            out = new Formatter(new StringBuilder(), Locale.US);
		            out.format("%d\t%.8f\t", topic, topicDistribution[topic]);
		            int rank = 0;
		            while (iterator.hasNext() && rank <5) {
		                IDSorter idCountPair = iterator.next();
		                out.format("%s, ", dataAlphabet.lookupObject(idCountPair.getID()));
		                rank++;
		               //sb1.append("##"+dataAlphabet.lookupObject(idCountPair.getID()));
		               //sb.add((String)dataAlphabet.lookupObject(idCountPair.getID()));
		            }
		            String str = out.toString();
		            if(str!=null && str.length()>3){
		            str=str.substring(0, str.length() - 2);
		            System.out.println(str);
		           sb.add("Topic "+(topic+1)+": "+str);
		            }
		          
		        }
		       // System.out.println("Hello"+sb.toString());
		        // Create a new instance with high probability of topic 0
		        StringBuilder topicZeroText = new StringBuilder();
		        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

		        int rank = 0;
		        while (iterator.hasNext() && rank < numKeywords) {
		            IDSorter idCountPair = iterator.next();
		            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
		            System.out.println(dataAlphabet.lookupObject(idCountPair.getID())+"%%%%%%%%%%%%%%");
		            rank++;
		        }

		        // Create a new instance named "test instance" with empty target and source fields.
		        InstanceList testing = new InstanceList(instances.getPipe());
		        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

		        TopicInferencer inferencer = model.getInferencer();
		        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 1000, 1, 5);
		        System.out.println("..;;;;0\t" + testProbabilities[0]);
		        System.out.println(sb);
		        
		        
		    }

}
