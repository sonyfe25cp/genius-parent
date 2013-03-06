//package com.genius.model;
//
//import java.util.Date;
//
//import com.google.code.morphia.annotations.Entity;
//import com.google.code.morphia.annotations.Reference;
//
///**
// * 用来保存用户信息的
// *@author lins
// *@date 2012-5-29
// **/
//@Entity
//public class ClickLog {
//	@Reference
//	private QueryLog queryLog;
//	
//	private String resourceKey;
//	
//	private Long rankerVersion;
//	
//	private Date clickTime;
//	
//	@Reference
//	private User user;
//	
//	private long rankPos;
//
//	public QueryLog getQueryLog() {
//		return queryLog;
//	}
//
//	public void setQueryLog(QueryLog queryLog) {
//		this.queryLog = queryLog;
//	}
//
//	public String getResourceKey() {
//		return resourceKey;
//	}
//
//	public void setResourceKey(String resourceKey) {
//		this.resourceKey = resourceKey;
//	}
//
//	public Long getRankerVersion() {
//		return rankerVersion;
//	}
//
//	public void setRankerVersion(Long rankerVersion) {
//		this.rankerVersion = rankerVersion;
//	}
//
//	public Date getClickTime() {
//		return clickTime;
//	}
//
//	public void setClickTime(Date clickTime) {
//		this.clickTime = clickTime;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public long getRankPos() {
//		return rankPos;
//	}
//
//	public void setRankPos(long rankPos) {
//		this.rankPos = rankPos;
//	}
//}
