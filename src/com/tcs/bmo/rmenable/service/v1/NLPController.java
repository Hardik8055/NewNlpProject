package com.tcs.bmo.rmenable.service.v1;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcs.bmo.util.Article;
import com.tcs.bmo.util.GoogleClient;
import com.tcs.bmo.util.Impact;
import com.tcs.bmo.util.ImpactList;
import com.tcs.bmo.util.NLPHelper;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.ErasureUtils;

@RestController
public class NLPController {

	// key for matched expressions
	public static class MyMatchedExpressionAnnotation implements CoreAnnotation<List<CoreMap>> {
		@Override
		public Class<List<CoreMap>> getType() {
			return ErasureUtils.<Class<List<CoreMap>>> uncheckedCast(String.class);
		}
	}

	NLPHelper helper = new NLPHelper();

	@RequestMapping(value = "/newsfner", method = RequestMethod.GET)
	public GoogleClient getFilternews() throws ClassNotFoundException, Exception, IOException, ParseException {
		// set properties
		// public static void main(String arg[]) throws Exception, Exception,
		// FileNotFoundException{
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,tokensregex");
		//props.setProperty("tokensregex.rules", "C:/BMODev/tner.rules");
		props.setProperty("tokensregex.rules", this.getClass().getClassLoader().getResource("config/trules.rules").toString());
		props.setProperty("ner.applyFineGrained", "false");

		props.setProperty("tokensregex.matchedExpressionsAnnotationKey",
				"com.tcs.bmo.rmenable.service.v1.NLPController$MyMatchedExpressionAnnotation");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// Object obj = new JSONParser().parse(new
		// FileReader("C:/BMODev/sample.json"));
		int flag = 0;
		// JSONObject jo=(JSONObject)obj;
		Gson gson = new Gson();

		GoogleClient googleClient = gson.fromJson(new FileReader("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/samplener.json"), GoogleClient.class);
		List<Article> article = googleClient.getArticles();
		List<Article> newArticle = new ArrayList<Article>();
		for (Article a : article) {
			if (a.getDescription() != null) {
				Annotation ann = new Annotation(a.getDescription());
				pipeline.annotate(ann);

				// show results

				System.out.println("---");
				System.out.println("tokens\n");

				for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {

					for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
						System.out
								.println(token.word() + "\t" + token.tag() + "\t" + token.ner() + "\t" + token.index());
						if (token.ner() != null)
							helper.setLevel(token);
					}
					System.out.println("");
				}
				System.out.println("---");
				System.out.println("matched expressions\n");
				for (CoreMap me : ann.get(MyMatchedExpressionAnnotation.class)) {
					System.out.println(me);

					flag = 1;
				}

				/*
				 * if (flag == 1) { newArticle.add(a); flag = 0; }
				 */
				if (helper.getSen() == 3) {
					a.setCatagory(1);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 2) {
					a.setCatagory(1);
					System.out.println("TESSSSSSSSSSS" + helper.sen2Templ());
					a.setTitle(helper.sen2Templ());
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 1) {
					a.setCatagory(1);
					helper.toString();
					a.setTitle(helper.sen1Templ());
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 4) {
					a.setCatagory(1);
					helper.toString();
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 5) {
					a.setCatagory(1);
					helper.toString();
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 6) {
					helper.toString();
					a.setCatagory(3);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 7) {
					helper.toString();
					a.setCatagory(3);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				System.out.println(a.getDescription());
				helper.clean();
			}
		}
			googleClient.setArticles(newArticle);
			googleClient.setTotalResults((int) newArticle.stream().count());
			System.out.println(newArticle.stream().count());
			return googleClient;
			// System.out.println(googleClient);
		
	}

	@RequestMapping(value = "/nernews", method = RequestMethod.POST)
	public ResponseEntity<GoogleClient> getFilternews(@RequestBody List<Article> articles)
			throws ClassNotFoundException, Exception, IOException, ParseException {
		// set properties
		// public static void main(String arg[]) throws Exception, Exception,
		// FileNotFoundException{
		if (articles == null || articles.size() == 0) {
			return new ResponseEntity<GoogleClient>(HttpStatus.NOT_FOUND);
		}

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,tokensregex");
		//props.setProperty("tokensregex.rules", "C:/BMODev/tner.rules");
		props.put("regexner.mapping", "Ford\tSCHOOL");
		props.setProperty("tokensregex.rules", this.getClass().getClassLoader().getResource("config/trules.rules").toString());
		props.setProperty("ner.applyFineGrained", "false");

		props.setProperty("tokensregex.matchedExpressionsAnnotationKey",
				"com.tcs.bmo.rmenable.service.v1.NLPController$MyMatchedExpressionAnnotation");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// Object obj = new JSONParser().parse(new
		// FileReader("C:/BMODev/sample.json"));
		int flag = 0;
		// JSONObject jo=(JSONObject)obj;
		Gson gson = new Gson();

		GoogleClient googleClient = new GoogleClient();
		// GoogleClient googleClient = gson.fromJson(new
		// FileReader("C:/BMODev/samplener.json"), GoogleClient.class));
		// List<Article> article = googleClient.getArticles()

		/*
		 * GoogleClient googleClient = gson.fromJson(new
		 * FileReader("C:/BMODev/samplener.json"), GoogleClient.class);
		 * List<Article> articles = googleClient.getArticles();
		 */
		List<Article> newArticle = new ArrayList<Article>();
		for (Article a : articles) {

			Annotation ann = new Annotation(a.getDescription());
			pipeline.annotate(ann);

			// show results

			System.out.println("---");
			System.out.println("tokens\n");

			for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {

				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					System.out.println(token.word() + "\t" + token.tag() + "\t" + token.ner() + "\t" + token.index());
					if (token.ner() != null)
						helper.setLevel(token);
				}
				System.out.println("");
			}
			System.out.println("---");
			System.out.println("matched expressions\n");
			for (CoreMap me : ann.get(MyMatchedExpressionAnnotation.class)) {
				System.out.println(me);

				flag = 1;
			}

			/*
			 * if (flag == 1) { newArticle.add(a); flag = 0; }
			 */
			if (helper.getSen() == 3) {
				newArticle.add(a);
				System.out.println(helper.toString());

			}
			if (helper.getSen() == 2) {
				System.out.println("TESSSSSSSSSSS" + helper.sen2Templ());
				a.setTitle(helper.sen2Templ());
				newArticle.add(a);
				System.out.println(helper.toString());

			}
			if (helper.getSen() == 1) {
				helper.toString();
				a.setTitle(helper.sen1Templ());
				newArticle.add(a);
				System.out.println(helper.toString());

			}
			
			System.out.println(a.getDescription());
			helper.clean();
		}
		googleClient.setArticles(newArticle);
		googleClient.setTotalResults((int) newArticle.stream().count());
		System.out.println(newArticle.stream().count());
		// return googleClient;

		return new ResponseEntity<GoogleClient>(googleClient, HttpStatus.OK);

		// System.out.println(googleClient);
	}

	@RequestMapping(value = "/newsf", method = RequestMethod.GET)
	public GoogleClient getFilternewsf() throws ClassNotFoundException, Exception, IOException, ParseException {
		// set properties
		// public static void main(String arg[]) throws Exception, Exception,
		// FileNotFoundException{
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,tokensregex");
		
		props.setProperty("tokensregex.rules", "/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/trules.rules");
		props.setProperty("tokensregex.matchedExpressionsAnnotationKey",
				"com.tcs.bmo.rmenable.service.v1.NLPController$MyMatchedExpressionAnnotation");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// Object obj = new JSONParser().parse(new
		// FileReader("C:/BMODev/sample.json"));
		int flag = 0;
		// JSONObject jo=(JSONObject)obj;
		Gson gson = new Gson();

		GoogleClient googleClient = gson.fromJson(new FileReader("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/sample.json"), GoogleClient.class);
		List<Article> article = googleClient.getArticles();
		List<Article> newArticle = new ArrayList<Article>();
		for (Article a : article) {

			Annotation ann = new Annotation(a.getDescription());
			pipeline.annotate(ann);
			// show results
			int level1 = 0, level2 = 0, level3 = 0;
			System.out.println("---");
			System.out.println("tokens\n");
			for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					System.out.println(token.word() + "\t" + token.tag() + "\t" + token.ner());
					if (token.ner() != null && token.ner().equalsIgnoreCase("LEVEL1COMPANY")) {
						level1 = 1;
					}
					if (token.ner() != null && token.ner().equalsIgnoreCase("LEVEL2DES")) {
						level2 = 1;
					}
					if (token.ner() != null && token.ner().equalsIgnoreCase("LEVEL3DES")) {
						level3 = 1;
					}
				}
				System.out.println("");
			}
			System.out.println("---");
			System.out.println("matched expressions\n");
			for (CoreMap me : ann.get(MyMatchedExpressionAnnotation.class)) {
				System.out.println(me);
				flag = 1;
			}
			/*
			 * if (flag == 1) { newArticle.add(a); flag = 0; }
			 */
			if (level1 == 1 && level2 == 1 && level3 == 1) {
				newArticle.add(a);
				flag = 0;
				level1 = 0;
				level2 = 0;
				level3 = 0;
			}
		}
		googleClient.setArticles(newArticle);
		googleClient.setTotalResults((int) newArticle.stream().count());
		System.out.println(newArticle.stream().count());
		return googleClient;
		// System.out.println(googleClient);
	}

	@RequestMapping(value = "/findImpact", method = RequestMethod.GET)
	public ImpactList findImpact() throws ClassNotFoundException, Exception, IOException, ParseException {

		Gson gson = new Gson();
		ImpactList impactJSON = gson.fromJson(new FileReader("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/impact.json"), ImpactList.class);
		List<Impact> impactList = impactJSON.getImpacts();
		for (Impact impact : impactList) {
			System.out.println(impact.getBrand());
			System.out.println(impact.getCount());
			System.out.println(impact.getFrom());
			System.out.println(impact.getTo());
			System.out.println(impact.getOffice());
		}
		impactJSON.setImpacts(impactList);
		impactJSON.setTotalResults(5);
		return impactJSON;
		// System.out.println(googleClient);
	}

	// Move this method after POC
	public List<Article> processNLP(List<Article> article)
			throws ClassNotFoundException, IOException, ParseException, Exception {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,tokensregex");
		//props.setProperty("tokensregex.rules", "C:/BMODev/tner.rules");
		props.setProperty("tokensregex.rules", this.getClass().getClassLoader().getResource("config/trules.rules").toString());
		ObjectMapper om=new ObjectMapper();
		props.put("regexner.mapping", "demand	SCHOOL");
		props.setProperty("ner.applyFineGrained", "false");

		props.setProperty("tokensregex.matchedExpressionsAnnotationKey",
				"com.tcs.bmo.rmenable.service.v1.NLPController$MyMatchedExpressionAnnotation");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// Object obj = new JSONParser().parse(new
		// FileReader("C:/BMODev/sample.json"));
		int flag = 0;
		// JSONObject jo=(JSONObject)obj;
		Gson gson = new Gson();

		// GoogleClient googleClient = null;
		// List<Article> article = googleClient.getArticles();
		List<Article> newArticle = new ArrayList<Article>();
		for (Article a : article) {
		//	if (a.getDescription() != null) {
				if (a.getContent() != null) {
				Annotation ann = new Annotation(a.getContent());
				pipeline.annotate(ann);

				// show results

				System.out.println("---");
				System.out.println("tokens\n");

				for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {

					for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
						System.out
								.println(token.word() + "\t" + token.tag() + "\t" + token.ner() + "\t" + token.index());
						if (token.ner() != null)
							helper.setLevel(token);
					}
					System.out.println("");
				}
				System.out.println("---");
				System.out.println("matched expressions\n");
				for (CoreMap me : ann.get(MyMatchedExpressionAnnotation.class)) {
					System.out.println(me);

					flag = 1;
				}

				/*
				 * if (flag == 1) { newArticle.add(a); flag = 0; }
				 */
				if (helper.getSen() == 3) {
					a.setCatagory(1);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 2) {
					a.setCatagory(1);
					a.setTitle(helper.sen2Templ());
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 1) {
					a.setCatagory(1);
					helper.toString();
					a.setTitle(helper.sen1Templ());
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 4) {
					a.setCatagory(1);
					helper.toString();
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 5) {
					a.setCatagory(1);
					helper.toString();
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 6) {
					helper.toString();
					a.setCatagory(3);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				if (helper.getSen() == 7) {
					helper.toString();
					a.setCatagory(2);
					newArticle.add(a);
					System.out.println(helper.toString());

				}
				System.out.println(a.getDescription());
				helper.clean();
			}
	}
		// googleClient.setArticles(newArticle);
		// googleClient.setTotalResults((int) newArticle.stream().count());
		System.out.println(newArticle.stream().count());
		return newArticle;
	}
	public List<Article> processTopicNLP(List<Article> article,String filename, int numTopic)
			throws ClassNotFoundException, IOException, ParseException, Exception {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,tokensregex,regexner");
		//props.setProperty("tokensregex.rules", "C:/BMODev/tner.rules");
		props.setProperty("tokensregex.rules", this.getClass().getClassLoader().getResource("config/trules.rules").toString());
		ObjectMapper om=new ObjectMapper();
		props.put("regexner.mapping", filename);
		props.setProperty("ner.applyFineGrained", "false");
		props.setProperty("regexner.ignorecase", "true");
		props.setProperty("tokensregex.matchedExpressionsAnnotationKey",
				"com.tcs.bmo.rmenable.service.v1.NLPController$MyMatchedExpressionAnnotation");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// Object obj = new JSONParser().parse(new
		// FileReader("C:/BMODev/sample.json"));
		int flag = 0;
		// JSONObject jo=(JSONObject)obj;
		Gson gson = new Gson();

		// GoogleClient googleClient = null;
		// List<Article> article = googleClient.getArticles();
		List<Article> newArticle = new ArrayList<Article>();
		for (Article a : article) {
			StringBuffer sbs=new StringBuffer();
		//	if (a.getDescription() != null) {
			if(a.getContent()!=null){
				sbs.append(a.getContent()+" ");
			}
			if(a.getDescription()!=null){
				sbs.append(a.getDescription()+" ");
			}
			if(a.getTitle()!=null){
				sbs.append(a.getTitle()+" ");
			}
			
				if (sbs.toString()!=null) {
					
				Annotation ann = new Annotation(sbs.toString());
				pipeline.annotate(ann);

				// show results

				System.out.println("---");
				System.out.println("tokens\n");

				for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {

					for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {/*
						System.out
								.println(token.word() + "\t" + token.tag() + "\t" + token.ner() + "\t" + token.index());*/
						if (token.ner() != null)
							helper.setLevel(token);
					}
					System.out.println("");
				}
				System.out.println("---");
				System.out.println("matched expressions\n");
				for (CoreMap me : ann.get(MyMatchedExpressionAnnotation.class)) {
					

					flag = 1;
				}

				
				if (helper.selectTopic(numTopic) == 1) {
					a.setCatagory(1);
					newArticle.add(a);
					System.out.println("##########################"+a.getContent());

				}
				
				System.out.println(a.getContent());
				helper.clean();
			}
	}
		// googleClient.setArticles(newArticle);
		// googleClient.setTotalResults((int) newArticle.stream().count());
		System.out.println(newArticle.stream().count());
		return newArticle;
	}

}
