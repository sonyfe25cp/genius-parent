/**
 * 
 */
package com.genius.recommender.model;

import javax.persistence.Transient;

import com.genius.model.NewsReport;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.utils.IndexDirection;

/**
 * 新闻集群成员，包含一条新闻以及该新闻同聚类中心的相似的，使用了morphia标注
 * @author horizon
 *
 */
@Entity
public class ClusterMember {
	@Id
	@Indexed(value = IndexDirection.ASC, name = "IClusterMemberId", unique = true)
	private String clusterMemberId;
	
	private Double sim ;
	
	@Reference
	private NewsReport xnews;
	
	public Double getSim() {
		return sim;
	}
	@Transient
	boolean selected = false;
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setSim(Double sim) {
		this.sim = sim;
	}

	public NewsReport getXnews() {
		return xnews;
	}

	public void setXnews(NewsReport xnews) {
		this.xnews = xnews;
	}
	/**
	 * 
	 */
	public ClusterMember() {
		// TODO Auto-generated constructor stub
	}

	public void setClusterMemberId(String clusterMemberId) {
		this.clusterMemberId = clusterMemberId;
	}

	public String getClusterMemberId() {
		return clusterMemberId;
	}
	public boolean equals(Object another)
	{
		if(another instanceof ClusterMember)
		{
			ClusterMember an = (ClusterMember) another;
			if(this.getClusterMemberId()==null||an.getClusterMemberId()==null)
			{
				return false;
			}
			else return an.getClusterMemberId().equals(this.getClusterMemberId());
		}
		else
		{
			return false;
		}
	}

}
