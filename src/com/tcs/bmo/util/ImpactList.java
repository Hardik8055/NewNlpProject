
package com.tcs.bmo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalResults",
    "impacts"
})
public class ImpactList {

    @JsonProperty("totalResults")
    private Integer totalResults;
    @JsonProperty("impacts")
    private List<Impact> impacts = null;
    
    @JsonProperty("totalResults")
	public Integer getTotalResults() {
		return totalResults;
	}
    
    @JsonProperty("totalResults")
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
    
    @JsonProperty("impacts")
	public List<Impact> getImpacts() {
		return impacts;
	}
    
    @JsonProperty("impacts")
	public void setImpacts(List<Impact> impacts) {
		this.impacts = impacts;
	}
   
    

}