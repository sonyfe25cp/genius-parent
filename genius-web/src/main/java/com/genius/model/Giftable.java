package com.genius.model;

import java.util.Date;

import org.bson.types.ObjectId;

import edu.bit.dlde.genius.core.Gift;

/**
 * 建议可以检索的东西都继承改类
 */
public interface Giftable {
	public Gift getGift();

	public void setUrl(String url);
	
	public String getUrl();

	public void setSourceHost(String host);
	public String getSourceHost();
	public void setContent(String content);

	public void setTitle(String title);
	
	public Date getPublishTime();
	
	public void setPublishTime(Date publishTime);
	
	public ObjectId getId() ;

	public void setId(ObjectId id) ;
}
