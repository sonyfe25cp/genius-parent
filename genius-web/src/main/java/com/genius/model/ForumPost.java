package com.genius.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

import edu.bit.dlde.genius.core.Gift;

@Entity
public class ForumPost implements Giftable {
	@Id
	private ObjectId id = null;

	private String anchor = null;
	@Indexed
	private String url = null;
	private String title = null;
	private Set<String> keywords = new HashSet<String>();
	private String description = null;
	private String origin = null;
	@Indexed
	private Date publishTime = null;

	@Indexed
	private String sourceHost = null;

	private String author = null;
	private String content = null;
	private Integer webHostId = null;
	private Integer topicId = null;
	private Double heatDegree = null;
	private Integer viewCount = null;

	private Integer sentiPolar = null;
	private Integer section = null;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getSourceHost() {
		return sourceHost;
	}

	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getWebHostId() {
		return webHostId;
	}

	public void setWebHostId(Integer webHostId) {
		this.webHostId = webHostId;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Double getHeatDegree() {
		return heatDegree;
	}

	public void setHeatDegree(Double heatDegree) {
		this.heatDegree = heatDegree;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getSentiPolar() {
		return sentiPolar;
	}

	public void setSentiPolar(Integer sentiPolar) {
		this.sentiPolar = sentiPolar;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	@Override
	public Gift getGift() {
		Gift gift = new Gift();
		gift.setBody(this.getContent());
		gift.setUrl(this.getUrl());
		gift.setAuthor(this.getSourceHost());
		gift.setUniqueGiftId(this.getUrl());
		gift.setTitle(this.getTitle());
		return gift;
	}
}
