package com.tcs.bmo.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import edu.stanford.nlp.ling.CoreLabel;

public class NLPHelper {
	private ArrayList<CoreLabel> keyValues = new ArrayList<>();
	private int level11 = 0, level12 = 0, level13 = 0, level22 = 0, level23 = 0;
	private int level121 = 0, level122 = 0, leveln=0;
	private int levelr=0;
	private int levelc=0;
	private int levels=0;
	private int leveld=0;
	private HashMap<String, Integer> topicHM=new HashMap<String, Integer>();

	public HashMap<String, Integer> getTopicHM() {
		return topicHM;
	}

	public void setTopicHM(HashMap<String, Integer> topicHM) {
		this.topicHM = topicHM;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public int getLevelc() {
		return levelc;
	}

	public void setLevelc(int levelc) {
		this.levelc = levelc;
	}

	public int getLevelr() {
		return levelr;
	}

	public void setLevelr(int levelr) {
		this.levelr = levelr;
	}

	public int getLeveln() {
		return leveln;
	}

	public void setLeveln(int leveln) {
		this.leveln = leveln;
	}

	public int getLevel121() {
		return level121;
	}

	public void setLevel121(int level121) {
		this.level121 = level121;
	}

	public int getLevel122() {
		return level122;
	}

	public void setLevel122(int level122) {
		this.level122 = level122;
	}

	public ArrayList<CoreLabel> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(ArrayList<CoreLabel> keyValues) {
		this.keyValues = keyValues;
	}

	public int getLevel11() {
		return level11;
	}

	public void setLevel11(int level11) {
		this.level11 = level11;
	}

	public int getLevel12() {
		return level12;
	}

	public void setLevel12(int level12) {
		this.level12 = level12;
	}

	public int getLevel13() {
		return level13;
	}

	public void setLevel13(int level13) {
		this.level13 = level13;
	}

	public int getLevel22() {
		return level22;
	}

	public void setLevel22(int level22) {
		this.level22 = level22;
	}

	public int getLevel23() {
		return level23;
	}

	public void setLevel23(int level23) {
		this.level23 = level23;
	}

	public void setLevel(CoreLabel value) {
		switch (value.ner()) {
		case ("LEVEL1COMPANY"):
			this.setLevel11(1);
			this.keyValues.add(value);
			break;
		case ("LEVEL2DES"):
			this.setLevel12(1);
			this.keyValues.add(value);
			break;
		case ("LEVEL3DES"):
			this.setLevel13(1);
			this.keyValues.add(value);
			break;
		case ("LEVEL2REC"):
			this.setLevel22(1);
			this.keyValues.add(value);
			break;
		case ("LEVEL3REC"):
			this.keyValues.add(value);
			this.setLevel23(1);
			break;
		case ("NUMBER"):
			this.keyValues.add(value);
			this.leveln=1;
			break;
		case ("PERCENT"):
			this.keyValues.add(value);

			break;
		case ("LEVEL13DEC"):
			this.keyValues.add(value);
			this.setLevel122(1);
			break;
		case ("ORGANIZATION"):
			this.keyValues.add(value);

			break;
		case ("LEVEL12DEC"):
			this.keyValues.add(value);
			this.setLevel121(1);
			break;
		case ("DATE"):
			if (value.tag().equalsIgnoreCase("CD"))
				this.keyValues.add(value);
			break;
		case ("LEVEL3RAT"):
			this.keyValues.add(value);
			this.setLevelr(1);
			break;
		case("COND1"):
			this.keyValues.add(value);
			this.setLevelc(1);
			break;
		case("SAAR"):
			this.keyValues.add(value);
			this.setLevels(1);
			break;
		case("DEALER"):
			this.keyValues.add(value);
			this.setLeveld(1);
			break;
		/*
		 * case ("TO"): value.setNER(value.before() + " to " + value.after());
		 * this.keyValues.add(value);
		 * 
		 * break;
		 */
		default:
			/*
			 * if(value.tag().equalsIgnoreCase("TO")){
			 * value.setNER(value.before() + " to " + value.after());
			 * this.keyValues.add(value); }
			 */
			checkTopic(value.ner());
			break;

		}

	}

	public void checkTopic(String value){
		
		if(value.contains("Topic")){
			System.out.println(value);
			topicHM.put(value, 1);
		}
		
	}
	
	public int selectTopic(int numt){
		int flag=0;
		int keywords=5;
		HashMap<Integer,HashMap> hfmap=new HashMap<>();

		HashMap<String,Integer> hSmap=new HashMap<>();
		if(topicHM!=null){
		for(String val:topicHM.keySet()){
			
			for(int i=1;i<=numt;i++){
				String topic="topic"+i;
				for(int j=1;j<=keywords;j++){
					String topickey=topic+j;
					if(val.equalsIgnoreCase(topickey)){
					hSmap.put(val, 1);
					System.out.println(val+"%%%%%%%%%%%%%%%%%%%%%");
					}
				}
				hfmap.put(i, hSmap);
			}
		}
		}
		if(hfmap!=null){
			for(int i=1;i<=numt;i++){
				if(hfmap.get(i)!=null && hfmap.get(i).keySet().size()>=2){ //change 3 as keywords
					System.out.println("%%%%%%%%%%%%%%%%%%%%%");
					flag=1;
				}
					
			}
		}
		return flag;
	}
	public int getSen() {
		if (this.getLevel11() == 1 && this.getLevel12() == 1 && this.getLevel13() == 1) {
			return 3; 
		}
		if (this.getLevel11() == 1 && this.getLevel22() == 1 && this.getLevel23() == 1 && this.getLeveln() == 1 ) {
			return 1; //recall
		}
		if (this.getLevel11() == 1 && (this.getLevel121() == 1 || this.getLevel122() == 1)) {
			return 2; //sales
		}
		if (this.getLevelr()==1) {
			return 4; //interest rates level
		}
		if (this.getLevelc()==1 && this.getLevel11()==1) {
			return 5; //Consumer Confidence
		}
		if (this.getLevels()==1 ) {
			return 6; //SAAR
		}
		if (this.getLeveld()==1 ) {
			return 7; //dealer
		}
		
		return 0;

	}

	public void clean() {
		this.keyValues.clear();
		this.setLevel11(0);
		this.setLevel12(0);
		this.setLevel13(0);
		this.setLevel22(0);
		this.setLevel23(0);
		this.setLevel121(0);
		this.setLevel122(0);
		this.setLeveln(0);
		this.setLevelr(0);
		this.setLevelc(0);
		this.setLevels(0);
		this.setLeveld(0);
		if(topicHM!=null)
			this.topicHM.clear();
	}

	public String sen2Templ() {
		StringBuilder newTitle = new StringBuilder();
		List<Token> tokenal = new ArrayList<Token>();
		tokenal = this.keyValues.stream().map(a -> {
			Token t = new Token();
			t.setIndex(a.index());
			t.setNer(a.ner());
			t.setTag(a.tag());
			t.setValue(a.value());
			return t;
		}).collect(Collectors.toList());

		String Level2string = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL2DES"))
				.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
		String Level1string = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL1COMPANY"))
				.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
		if (Level2string.equalsIgnoreCase("sales")) {
			// Toyota Motor Corp car sale drops 25% and light-truck drops 0.3%
			newTitle.append(Level1string);
			int level1index = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL1COMPANY"))
					.map(a -> a.index()).collect(Collectors.toList()).get(0);

			for (Token token : tokenal) {
				if (token.getIndex() == level1index + 1) {
					if (token.getNer().equalsIgnoreCase("ORGANIZATION")) {
						newTitle.append(" " + token.getValue());
						level1index += 1;
					}
				}
			}

			newTitle.append(" car");
			// newTitle.append(this.keyValues.stream().filter(c ->
			// c.ner().equalsIgnoreCase("LEVEL12DEC"))
			// .map(c ->
			// c.value()).collect(Collectors.toList()).get(0).toString());
			level1index = Integer.parseInt(this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL12DEC"))
					.map(c -> c.index()).collect(Collectors.toList()).get(0).toString());
			for (Token token : tokenal) {
				if (token.getIndex() == level1index) {
					if (token.getNer().equalsIgnoreCase("LEVEL12DEC")) {
						newTitle.append(" " + token.getValue());
						level1index += 1;
					} else if (token.getNer().equalsIgnoreCase("PERCENT")) {
						newTitle.append(" " + token.getValue());
						level1index += 1;
					}
				}
			}

			newTitle.append(" and");
			level1index = Integer.parseInt(this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL13DEC"))
					.map(c -> c.index()).collect(Collectors.toList()).get(0).toString());

			for (Token token : tokenal) {
				if (token.getIndex() == level1index) {
					if (token.getNer().equalsIgnoreCase("LEVEL13DEC")) {
						newTitle.append(" " + token.getValue());
						level1index += 1;
					} else if (token.getNer().equalsIgnoreCase("PERCENT")) {
						newTitle.append(" " + token.getValue());
						level1index += 1;
					}
				}
			}
		}
		return newTitle.toString();
	}

	public String sen1Templ() throws ClassNotFoundException, IOException, ParseException, Exception {
		String template = "Toyota recalls [2.4 million hybrid cars worldwide and] 807, 00 Toyota Prius from 2010 to 2104.";
		StringBuilder newTitle = new StringBuilder();

		String Level2string = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL2REC"))
				.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
		String Level1string = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL1COMPANY"))
				.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
		if (Level2string.equalsIgnoreCase("recall") || Level2string.equalsIgnoreCase("recalling") ) {
			newTitle = newTitle.append(Level1string);
			newTitle.append(" ");
			newTitle.append(Level2string);
			newTitle.append("s ");
			String number=this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("NUMBER"))
					.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
			if (number != null && !number.equalsIgnoreCase("")) {
			newTitle.append(number);
			}
			newTitle.append(" ");
			newTitle.append(Level1string);
			newTitle.append(" ");
			String model = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("LEVEL3REC"))
					.filter(a -> a.value() != null).map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
			newTitle.append(model);
			/*
			 * newTitle.append(" from ");
			 * newTitle.append(this.keyValues.stream().filter(c ->
			 * c.tag().equalsIgnoreCase("TO")) .map(c ->
			 * c.value()).collect(Collectors.toList()).get(0).toString());
			 */
			int size = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("DATE")).filter(a -> a.value() != null).map(c -> c.value())
					.collect(Collectors.toList()).size();
			if (size >= 2) {
				/*
				 * newTitle.append(" from ");
				 * newTitle.append(this.keyValues.stream().filter(c ->
				 * c.ner().equalsIgnoreCase("DATE")) .map(c ->
				 * c.value()).collect(Collectors.toList()).get(0).toString());
				 * newTitle.append(" to ");
				 * newTitle.append(this.keyValues.stream().filter(c ->
				 * c.ner().equalsIgnoreCase("DATE")) .map(c ->
				 * c.value()).collect(Collectors.toList()).get(1).toString());
				 * System.out.println("Final S" + newTitle.toString()); } else {
				 * newTitle.append(" year ");
				 * newTitle.append(this.keyValues.stream().filter(c ->
				 * c.ner().equalsIgnoreCase("DATE")) .map(c ->
				 * c.value()).collect(Collectors.toList()).get(0).toString());
				 * 
				 * }
				 */
				String fromYear = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("DATE"))
						.map(c -> c.value()).collect(Collectors.toList()).get(0).toString();
				newTitle.append(" ");
				newTitle.append(fromYear);
				String toYearModel = this.keyValues.stream().filter(c -> c.ner().equalsIgnoreCase("DATE"))
						.map(c -> c.value()).collect(Collectors.toList()).get(1).toString();
				if (toYearModel != null && !toYearModel.equalsIgnoreCase("")) {
					newTitle.append(" to ");
					newTitle.append(toYearModel);
				}
				newTitle.append("/n");
				newTitle = findImpact(Level1string, fromYear, toYearModel, model, newTitle);
			}
			newTitle = findImpact(Level1string, " " , " ", model, newTitle);
		}
		return newTitle.toString();

	}

	public StringBuilder findImpact(String brand, String fromYear, String toYearModel, String model,
			StringBuilder titleUpdated) throws ClassNotFoundException, Exception, IOException, ParseException {
		Gson gson = new Gson();
		List<Impact> impactList = null;
		ImpactList impactJSON = gson.fromJson(new FileReader("/Users/HardikBharat/Desktop/myeclipseworkspace/RMEnablement/misc/impact.json"), ImpactList.class);
		impactList = impactJSON.getImpacts();
		for (Impact impact : impactList) {
			if (brand.equalsIgnoreCase(impact.getBrand()) && model.equalsIgnoreCase(impact.getModel())
				//	&& fromYear.equalsIgnoreCase(impact.getFrom()) && toYearModel.equalsIgnoreCase(impact.getTo())
					) {
				titleUpdated.append("Impact: ");
				titleUpdated.append(impact.getOffice());
				titleUpdated.append(" ");
				titleUpdated.append(impact.getBrand());
				titleUpdated.append(" sold ");
				titleUpdated.append(impact.getCount());
				titleUpdated.append(" ");
				titleUpdated.append(impact.getBrand());
				titleUpdated.append(" ");
				titleUpdated.append(impact.getModel());
				titleUpdated.append(" during this period");
			}
		}
		return titleUpdated;
	}

	@Override
	public String toString() {
		return "NLPHelper [keyValues=" + keyValues + ", level11=" + level11 + ", level12=" + level12 + ", level13="
				+ level13 + ", level22=" + level22 + ", level23=" + level23 + ", level121=" + level121 + ", level122="
				+ level122 + ", leveln=" + leveln + ", levelr=" + levelr + ", levelc=" + levelc + "]";
	}

	public int getLeveld() {
		return leveld;
	}

	public void setLeveld(int leveld) {
		this.leveld = leveld;
	}

}
