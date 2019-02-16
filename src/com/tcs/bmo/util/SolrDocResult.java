package com.tcs.bmo.util;



import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"source.id",
"source.name",
"author",
"title",
"description",
"url",
"urlToImage",
"publishedAt",
"content",
"id",
"url_str",
"title_str",
"source.id_str",
"_version_",
"author_str",
"description_str",
"content_str",
"source.name_str",
"urlToImage_str"
})
public class SolrDocResult {
	
	@JsonProperty("source.id")
	private String sourceId;
	@JsonProperty("source.name")
	private String sourceName;
	@JsonProperty("author")
	private String author;
	@JsonProperty("title")
	private String title;
	@JsonProperty("description")
	private String description;
	@JsonProperty("url")
	private String url;
	@JsonProperty("urlToImage")
	private String urlToImage;
	@JsonProperty("publishedAt")
	private String publishedAt;
	@JsonProperty("content")
	private String content;
	@JsonProperty("id")
	private String id;
	@JsonProperty("url_str")
	private String urlStr;
	@JsonProperty("title_str")
	private String titleStr;
	@JsonProperty("source.id_str")
	private String sourceIdStr;
	@JsonProperty("_version_")
	private String version;
	@JsonProperty("author_str")
	private String authorStr;
	@JsonProperty("description_str")
	private String descriptionStr;
	@JsonProperty("content_str")
	private String contentStr;
	@JsonProperty("source.name_str")
	private String sourceNameStr;
	@JsonProperty("urlToImage_str")
	private String urlToImageStr;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("source.id")
	public String getSourceId() {
	return sourceId;
	}

	@JsonProperty("source.id")
	public void setSourceId(String sourceId) {
	this.sourceId = sourceId;
	}

	@JsonProperty("source.name")
	public String getSourceName() {
	return sourceName;
	}

	@JsonProperty("source.name")
	public void setSourceName(String sourceName) {
	this.sourceName = sourceName;
	}

	@JsonProperty("author")
	public String getAuthor() {
	return author;
	}

	@JsonProperty("author")
	public void setAuthor(String author) {
	this.author = author;
	}

	@JsonProperty("title")
	public String getTitle() {
	return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
	this.title = title;
	}

	@JsonProperty("description")
	public String getDescription() {
	return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
	this.description = description;
	}

	@JsonProperty("url")
	public String getUrl() {
	return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
	this.url = url;
	}

	@JsonProperty("urlToImage")
	public String getUrlToImage() {
	return urlToImage;
	}

	@JsonProperty("urlToImage")
	public void setUrlToImage(String urlToImage) {
	this.urlToImage = urlToImage;
	}

	@JsonProperty("publishedAt")
	public String getPublishedAt() {
	return publishedAt;
	}

	@JsonProperty("publishedAt")
	public void setPublishedAt(String publishedAt) {
	this.publishedAt = publishedAt;
	}

	@JsonProperty("content")
	public String getContent() {
	return content;
	}

	@JsonProperty("content")
	public void setContent(String content) {
	this.content = content;
	}

	@JsonProperty("id")
	public String getId() {
	return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
	this.id = id;
	}

	@JsonProperty("url_str")
	public String getUrlStr() {
	return urlStr;
	}

	@JsonProperty("url_str")
	public void setUrlStr(String urlStr) {
	this.urlStr = urlStr;
	}

	@JsonProperty("title_str")
	public String getTitleStr() {
	return titleStr;
	}

	@JsonProperty("title_str")
	public void setTitleStr(String titleStr) {
	this.titleStr = titleStr;
	}

	@JsonProperty("source.id_str")
	public String getSourceIdStr() {
	return sourceIdStr;
	}

	@JsonProperty("source.id_str")
	public void setSourceIdStr(String sourceIdStr) {
	this.sourceIdStr = sourceIdStr;
	}

	@JsonProperty("_version_")
	public String getVersion() {
	return version;
	}

	@JsonProperty("_version_")
	public void setVersion(String version) {
	this.version = version;
	}

	@JsonProperty("author_str")
	public String getAuthorStr() {
	return authorStr;
	}

	@JsonProperty("author_str")
	public void setAuthorStr(String authorStr) {
	this.authorStr = authorStr;
	}

	@JsonProperty("description_str")
	public String getDescriptionStr() {
	return descriptionStr;
	}

	@JsonProperty("description_str")
	public void setDescriptionStr(String descriptionStr) {
	this.descriptionStr = descriptionStr;
	}

	@JsonProperty("content_str")
	public String getContentStr() {
	return contentStr;
	}

	@JsonProperty("content_str")
	public void setContentStr(String contentStr) {
	this.contentStr = contentStr;
	}

	@JsonProperty("source.name_str")
	public String getSourceNameStr() {
	return sourceNameStr;
	}

	@JsonProperty("source.name_str")
	public void setSourceNameStr(String sourceNameStr) {
	this.sourceNameStr = sourceNameStr;
	}

	@JsonProperty("urlToImage_str")
	public String getUrlToImageStr() {
	return urlToImageStr;
	}

	@JsonProperty("urlToImage_str")
	public void setUrlToImageStr(String urlToImageStr) {
	this.urlToImageStr = urlToImageStr;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}

	}
	

