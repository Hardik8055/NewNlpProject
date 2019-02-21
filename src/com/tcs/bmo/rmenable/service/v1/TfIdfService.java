package com.tcs.bmo.rmenable.service.v1;

import java.util.List;

public class TfIdfService {
	
	
	public double tf(List<String> doc, String term) {
	    double result = 0;
	    for (String word : doc) {
	       if (term.equalsIgnoreCase(word))
	              result++;
	       }
	    
	    double tfRes = result / doc.size();
	    return tfRes;
	}
	
	
	public double idf(List<List<String>> docs, String term) {
	    double n = 0;
	    for (List<String> doc : docs) {
	        for (String word : doc) {
	            if (term.equalsIgnoreCase(word)) {
	                n++;
	                break;
	            }
	        }
	    }
	    
	    double idfRes = Math.log(docs.size() / n);
	    return idfRes;
	}
	
	
	public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
	    return tf(doc, term) * idf(docs, term);
	}




}
