
package com.tcs.bmo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "office",
    "brand",
    "model",
    "from",
    "to",
    "count"
})
public class Impact {

    @JsonProperty("office")
    private String office;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("model")
    private String model;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("count")
    private String count;
    
    @JsonProperty("office")
	public String getOffice() {
		return office;
	}
    
    @JsonProperty("office")
	public void setOffice(String office) {
		this.office = office;
	}
    
    @JsonProperty("brand")
	public String getBrand() {
		return brand;
	}
    
    @JsonProperty("brand")
	public void setBrand(String brand) {
		this.brand = brand;
	}
    
    @JsonProperty("model")
	public String getModel() {
		return model;
	}
    
    @JsonProperty("model")
	public void setModel(String model) {
		this.model = model;
	}
    
    @JsonProperty("from")
	public String getFrom() {
		return from;
	}
    
    @JsonProperty("from")
	public void setFrom(String from) {
		this.from = from;
	}
    
    @JsonProperty("to")
	public String getTo() {
		return to;
	}
    
    @JsonProperty("to")
	public void setTo(String to) {
		this.to = to;
	}
    
    @JsonProperty("count")
	public String getCount() {
		return count;
	}
    
    @JsonProperty("count")
	public void setCount(String count) {
		this.count = count;
	}  
    
}