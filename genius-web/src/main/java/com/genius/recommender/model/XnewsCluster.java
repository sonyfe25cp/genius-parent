package com.genius.recommender.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.genius.dao.GeniusDao;
import com.genius.model.NewsReport;
import com.genius.utils.Page;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.utils.IndexDirection;

import edu.bit.dlde.recommender.adapter.DLDESimilarityAdapter;
import edu.bit.dlde.utils.DLDELogger;

/**
 * 新闻集群，使用了morphia标注
 * @author horizon
 */
@Entity
public class XnewsCluster {
	@Id
	@Indexed(value = IndexDirection.ASC, name = "IClusterId", unique = true)
	private String clusterId;
	
	@Reference
	private NewsReport centerXnews;
	
	@Embedded
	private List<ClusterMember> clusterMembers=new ArrayList<ClusterMember>();
	@Transient
	DLDELogger logger = new DLDELogger();
	
	private Double threadhold = 0.15;
	
	private Integer minNumber = 5;
	private Integer size = 0;
	
	public Integer getSize() {
		this.setSize(clusterMembers.size());
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Double getThreadhold() {
		return threadhold;
	}
	public void setThreadhold(Double threadhold) {
		this.threadhold = threadhold;
	}
	public Integer getMinNumber() {
		return minNumber;
	}
	public void setMinNumber(Integer minNumber) {
		this.minNumber = minNumber;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public NewsReport getCenterXnews() {
		return centerXnews;
	}
	/**
	 * 设置新闻集群的中心，此方法会从xd中读取所有新闻并按中心进行聚类
	 * @param centerXnews 聚类中心
	 * @param xd 获取新闻的数据库接口
	 */
	public void setCenterXnews(NewsReport centerXnews,GeniusDao gd) {
		this.centerXnews = centerXnews;
		this.clusterId = new String(this.centerXnews.getId().toString());
		
		List<ClusterMember> cms = new ArrayList<ClusterMember>();
		DLDESimilarityAdapter adapter = new DLDESimilarityAdapter();
		
		int pageNo = 1;
		int pageSize = 1000;
		Page page = new Page(pageNo,pageSize);
		List<NewsReport> sources = new ArrayList<NewsReport>();
		for (NewsReport x : gd.loadNewsReportsInRange(page)) {
			sources.add(x);
		}
		//int counts = 0;
		while (sources != null && sources.size() > 0/*&&counts<100*/) {
			//counts+=50;
			try {
				for(NewsReport s:sources)
				{
					Double score = adapter.getSimilarity(s, this.centerXnews);
					//System.out.println(s.getId().toString()+":" + score);
					if(score > threadhold)
					{
						System.out.println("add one "+s.getId().toString());
						ClusterMember cm = new ClusterMember();
						cm.setXnews(s);
						cm.setSim(score);
						cm.setClusterMemberId(clusterId+"&"+s.getId().toString());
						cms.add(cm);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sources.clear();
			pageNo ++;
			page.setPageNo(pageNo);
			for (NewsReport x : gd.loadNewsReportsInRange(page)) {
				sources.add(x);
			}
		}
		logger.debug("size "+cms.size());
		if(cms.size()>=minNumber)
		{
			ClusterComparator clusterComparator = new ClusterComparator();
			Collections.sort(cms,clusterComparator);
			this.clusterMembers = cms;
			this.setSize(cms.size());
		}
	}
	/**
	 * @param news the news need to be add
	 * @return true for changed,false for not changed
	 */
	public boolean AddANews(NewsReport news)
	{
		boolean changed = false;
		DLDESimilarityAdapter adapter = new DLDESimilarityAdapter();
		Double score = adapter.getSimilarity(news, this.centerXnews);
		//System.out.println(s.getId().toString()+":" + score);
		if(score > threadhold)
		{
			System.out.println("add one "+news.getId().toString());
			ClusterMember cm = new ClusterMember();
			cm.setXnews(news);
			cm.setSim(score);
			cm.setClusterMemberId(clusterId+"&"+news.getId().toString());
			clusterMembers.add(cm);
			changed = true;
		}
		return changed;
	}
	/**
	 * @param obsolete the news need to be checked and delete
	 * @return true for changed,false for not changed
	 */
	public boolean CleanANews(NewsReport obsolete)
	{
		boolean changed = false;
		ClusterMember cm = new ClusterMember();
		cm.setClusterMemberId(clusterId+"&"+obsolete.getId().toString());
		if(clusterMembers.contains(cm))
		{
			//System.out.println(true);
			clusterMembers.remove(cm);
			changed = true;
		}
		return changed;
	}
	
	public List<ClusterMember> getClusterMembers() {
		return clusterMembers;
	}
	public void setClusterMembers(List<ClusterMember> clusterMembers) {
		this.clusterMembers = clusterMembers;
		//this.setSize(clusterMembers.size());
	}
	public List<NewsReport> getXnewsList(){
		List<NewsReport> result= new ArrayList<NewsReport>();
		if(clusterMembers==null)
		{
			return null;
		}
		for(ClusterMember cm:clusterMembers)
		{
			result.add(cm.getXnews());
		}
		return result;
	}
	public boolean equals(Object another)
	{
		if(another instanceof XnewsCluster)
		{
			XnewsCluster an = (XnewsCluster) another;
			if(this.getClusterId()==null||an.getClusterId()==null)
			{
				return false;
			}
			else return an.getClusterId().equals(this.getClusterId());
		}
		else
		{
			return false;
		}
	}
}

