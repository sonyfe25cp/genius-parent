package com.genius.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Transient;

import edu.bit.dlde.genius.core.Gift;
import edu.bit.dlde.recommender.model.Xnews;
import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod;

@Entity
public class NewsReport implements Giftable,Xnews,ClusterCenterMethod.Item{
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
	
	@Transient
	private String fullUrl;
	@Transient
	private boolean selected = false;
	@Embedded
	private List<String> comments;

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getCommentsToString() {
		StringBuilder sb=new StringBuilder();
		if(comments!=null){
			for(String co:comments){
				sb.append(co+"$$");
			}
		}
		return sb.toString();
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("url:" + url + "\n");
		sb.append("title:" + title + "\n");
		sb.append("content:" + content + "\n");
		return sb.toString();
	}

	public boolean equals(Object another)
	{
		if(another instanceof NewsReport)
		{
			NewsReport an = (NewsReport) another;
			if(this.getId()==null||an.getId()==null)
			{
				return false;
			}
			return an.getId().equals(this.getId());
		}
		else
		{
			return false;
		}
	}

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
		this.fullUrl = this.url;
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
		gift.setDate(formatDate(this.getPublishTime()));
		return gift;
	}
	
	private String formatDate(Date date){
		DateFormat df=new SimpleDateFormat("yyyy-mm-dd");
		String d=df.format(date);
		return d;
	}

	@Override
	public void print() {
		String s= getId().toString();
		System.out.print("	"+s.subSequence(s.length()-3, s.length()));
	}
}
