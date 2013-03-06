/**
 * 
 */
package com.genius.recommender.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genius.dao.GeniusDao;
import com.genius.dao.UserDao;
import com.genius.model.NewsReport;
import com.genius.model.User;
import com.genius.recommender.dao.RecommenderDao;
import com.genius.recommender.model.XnewsCluster;
import edu.bit.dlde.recommender.adapter.DLDEItemSimilarityBuilder;
import edu.bit.dlde.recommender.model.Xnews;

/**
 * @author horizon
 *
 */
public class UpdateModels {
	public static void update(User cUser,RecommenderDao rd) {
		Map<Xnews, Double> result;
		List<Xnews> userCollections = new ArrayList<Xnews>();
		for (NewsReport x : cUser.getCollections()) {
			userCollections.add(x);
		}
		List<Xnews> sources = new ArrayList<Xnews>();
		for (XnewsCluster xc : cUser.getXnewsClusters()) {
			for(NewsReport xnews:xc.getXnewsList())
			{
				boolean have = false;
				for(Xnews i:sources)
				{
					if(((NewsReport)i).getId().equals(xnews.getId()))
					{
						have = true;
						break;
					}
				}
				if(!have)
				{
					sources.add(xnews);
				}
			}
		}
		try {
			DLDEItemSimilarityBuilder dsb = new DLDEItemSimilarityBuilder();
			dsb.setTreshold(0.05);
			rd.CleanScoreItems(cUser);
			result = dsb.UpdateSimilarities(
					cUser.getUsername(), userCollections, cUser
							.getKeywords(), sources);
			for (Xnews key : result.keySet()) {
				System.out.println("add a scoreitem:username "
						+ cUser.getUsername() + ",newsid "
						+ ((NewsReport) key).getId() + ",score "
						+ (Double) result.get(key));
				rd.PutScoreItem(cUser, (NewsReport) key, (Double) result
						.get(key));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void addANews(NewsReport news,RecommenderDao rd){
		List<XnewsCluster> all = rd.GetAllXnewsClusters();
		for(XnewsCluster xc:all)
		{
			if(xc.AddANews(news))
			{
				rd.PutXnewsCluster(xc);
			}
		}
	}
	public static void removeANews(NewsReport obsolete,RecommenderDao rd,UserDao ud,GeniusDao gd){
		List<XnewsCluster> all = rd.GetAllXnewsClusters();
		XnewsCluster tar = null;
		for(XnewsCluster xc:all)
		{
			if(xc.getCenterXnews().equals(obsolete))
			{
				tar = xc;
				break;
			}
		}
		//clean user
		int offset = 0;
		int count = 100;
		List<User> users = new ArrayList<User>();
		users = ud.getOrderList(count, offset);
		boolean changed = false;
		while (users != null && users.size() > 0) {
			try {
				for(User user:users)
				{
					changed = user.CleanANews(obsolete);
					if(tar!=null)
					{
						if(user.getXnewsClusters().contains(tar))
						{
							user.getXnewsClusters().remove(tar);
							changed = true;
						}
					}
					if(changed)
					{
						UpdateModels.update(user,rd);
						ud.save(user);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			users.clear();
			offset+=count;
			users = ud.getOrderList(count, offset);
		}
		//clean xnewscluster
		if(null!=tar)
		{
			rd.CleanXnewsCluster(tar);
			all.remove(tar);
		}
		//clean clustermembers
		for(XnewsCluster xc:all)
		{
			if(xc.CleanANews(obsolete))
			{
				rd.PutXnewsCluster(xc);
			}
		}
		//clean xnews
		gd.deleteNewsReport(obsolete.getId());
		
	}
	public static void removeAXnewsCluster(XnewsCluster obsolete,RecommenderDao rd,UserDao ud)
	{
		//clean user
		int offset = 0;
		int count = 100;
		List<User> users = new ArrayList<User>();
		users = ud.getOrderList(count, offset);
		while (users != null && users.size() > 0) {
			try {
				for(User user:users)
				{
					if(user.getXnewsClusters().contains(obsolete))
					{
						user.getXnewsClusters().remove(obsolete);
						UpdateModels.update(user,rd);
						ud.save(user);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			users.clear();
			offset+=count;
			users = ud.getOrderList(count, offset);
		}
		//			
		rd.CleanXnewsCluster(obsolete);
	}

}
