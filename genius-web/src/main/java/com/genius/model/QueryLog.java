package com.genius.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 记录查询日志
 * @author ChenJie
 *
 */

@Entity
public class QueryLog {
	
	@Id 
	private ObjectId id;//自增id
	@Indexed
	private String qid;//查询id
	@Indexed
	private String query;//查询词
	private List<String> docNum;//文档对的id值
	@Indexed
	private String userId;//用户id
	@Indexed
	private Date date;//查询日期
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public List<String> getDocNum() {
		return docNum;
	}
	public void setDocNum(List<String> docNum) {
		this.docNum = docNum;
	}
	public String toString()
	{
		return "qid = " + id + " userid = " + qid + " query = " + query + " time = " + date;
	}
}
