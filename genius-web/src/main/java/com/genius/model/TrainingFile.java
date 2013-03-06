package com.genius.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 *	@author zhangchangmin
 **/
@Entity(value="TrainingFile",noClassnameStored=true)
public class TrainingFile {
	@Id
	private String id=null;
	@Indexed
	private String title=null;
	@Indexed
	private String type=null;
	private String body=null;
	private String author=null;
	@Indexed
	private boolean enabled=true;
	
	public TrainingFile(){};
	public TrainingFile(String id,String title,String type,String body,String author)
	{
		this.id = id;
		this.title = title;
		this.type = type;
		this.body = body;
		this.author = author;
	}
	
	public TrainingFile(String id,String title,String type,String body,String author,boolean enabled)
	{
		this.id = id;
		this.title = title;
		this.type = type;
		this.body = body;
		this.author = author;
		this.enabled = enabled;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
