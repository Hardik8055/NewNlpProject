package com.tcs.bmo.util;

public class Token {
	private String value;
	private String ner;
	private String tag;
	private int index;
	public int getIndex() {
		return index;
	}
	@Override
	public String toString() {
		return "Token [value=" + value + ", ner=" + ner + ", tag=" + tag + ", index=" + index + "]";
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getNer() {
		return ner;
	}
	public void setNer(String ner) {
		this.ner = ner;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

}