/**
 * 
 */
package com.genius.recommender.model;

import com.genius.model.NewsReport;
import com.genius.model.User;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;

/**
 * 得分纪录，使用了morphia标注
 * @author horizon
 *
 */
@Entity
public class ScoreItem {
	@Id
	private String uk=null;
	
	public String getUk() {
		return uk;
	}

	public void setUk(String uk) {
		this.uk = uk;
	}

	@Reference
	@Indexed
	private User user;
	@Reference
	@Indexed
	private NewsReport xnews;
	
	@Indexed
	private Double score;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	/**
	 * 
	 */
	public ScoreItem() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if(xnews!=null&&user!=null)
		{
			uk = user.getUsername()+":"+xnews.getId().toString();
		}
	}

	public NewsReport getXnews() {
		return xnews;
	}

	public void setXnews(NewsReport xnews) {
		this.xnews = xnews;
		if(xnews!=null&&user!=null)
		{
			uk = user.getUsername()+":"+xnews.getId().toString();
		}
	}
	public boolean equals(ScoreItem another)
	{
		if(this.getUk()==null||another.getUk()==null)
		{
			return false;
		}
		else return another.getUk().equals(this.getUk());
	}
}
