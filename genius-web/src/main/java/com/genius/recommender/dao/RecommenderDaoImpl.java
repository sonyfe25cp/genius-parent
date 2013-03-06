/**
 * 
 */
package com.genius.recommender.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.genius.dao.MongoDAOBase;
import com.genius.model.ExtractConfiguration;
import com.genius.model.NewsReport;
import com.genius.model.User;
import com.genius.recommender.model.ScoreItem;
import com.genius.recommender.model.XnewsCluster;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

//import edu.bit.dlde.recommender.model.Xnews;

/**
 * 与推荐相关的Mongo数据库查询的实现
 * @author horizon
 * @see RecommenderDao
 * @see MongoDAOBase
 */
public class RecommenderDaoImpl extends MongoDAOBase implements RecommenderDao {

	/** (non-Javadoc)
	 * @see com.genius.dao.MongoDAOBase#init()
	 */
	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(ExtractConfiguration.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.genius.recommender.dao.RecommenderDao#GetTopScoreItems(int,
	 * com.genius.model.User)
	 */
	@Override
	public List<NewsReport> GetTopScoreItems(int count, User user) {
		// TODO Auto-generated method stub
		List<ScoreItem> scoreresult = datastore.find(ScoreItem.class)
				.filter("user =", user).order("-score").limit(count).asList();
		List<NewsReport> xnewsresult = new ArrayList<NewsReport>();
		if (scoreresult != null) {
			for (ScoreItem si : scoreresult) {
				xnewsresult.add(si.getXnews());
			}
		}
		return xnewsresult;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.genius.recommender.dao.RecommenderDao#PutScoreItem(com.genius.model
	 * .User, edu.bit.dlde.recommender.model.Xnews, java.lang.Double)
	 */
	@Override
	public boolean PutScoreItem(User user, NewsReport xnews, Double score) {
		// TODO Auto-generated method stub
		ScoreItem si = new ScoreItem();
		si.setUser(user);
		si.setXnews(xnews);
		si.setScore(score);

		Key<ScoreItem> result = datastore.save(si);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#GetTopScoreItems(int, com.genius.model.User, int)
	 */
	@Override
	public List<NewsReport> GetTopScoreItems(int count, User user, int offset) {
		// TODO Auto-generated method stub
		List<ScoreItem> scoreresult = datastore.find(ScoreItem.class)
				.filter("user =", user).order("-score").offset(offset)
				.limit(count).asList();
		List<NewsReport> xnewsresult = new ArrayList<NewsReport>();
		if (scoreresult != null) {
			for (ScoreItem si : scoreresult) {
				xnewsresult.add(si.getXnews());
			}
		}
		return xnewsresult;
	}

	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#CleanScoreItems(com.genius.model.User)
	 */
	@Override
	public boolean CleanScoreItems(User user) {
		try {
			datastore.delete(datastore.createQuery(ScoreItem.class).filter(
					"user =", user));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#GetSize(com.genius.model.User)
	 */
	@Override
	public long GetSize(User user) {
		// TODO Auto-generated method stub
		long result = datastore.find(ScoreItem.class).filter("user =", user)
				.countAll();
		return result;
	}

	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#PutXnewsCluster(com.genius.recommender.model.XnewsCluster)
	 */
	@Override
	public boolean PutXnewsCluster(XnewsCluster xc) {
		// TODO Auto-generated method stub

		Key<XnewsCluster> result = datastore.save(xc);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#GetAllXnewsClusters()
	 */
	@Override
	public List<XnewsCluster> GetAllXnewsClusters() {
		// TODO Auto-generated method stub
		List<XnewsCluster> result = datastore.find(XnewsCluster.class).asList();
		return result;
	}

	/** (non-Javadoc)
	 * @see com.genius.recommender.dao.RecommenderDao#GetXnewsClusterById(java.lang.String)
	 */
	@Override
	public XnewsCluster GetXnewsClusterById(String clusterID) {
		// TODO Auto-generated method stub
		List<XnewsCluster> result = datastore.find(XnewsCluster.class)
				.filter("clusterId", clusterID).asList();
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void CleanXnewsCluster(XnewsCluster xc) {
		// TODO Auto-generated method stub
		datastore.delete(datastore.find(XnewsCluster.class).filter("_id", xc.getClusterId()));
	}

	@Override
	public void CleanXnewsCluster(String clusterId) {
		// TODO Auto-generated method stub
		datastore.delete(datastore.find(XnewsCluster.class).filter("_id", clusterId));
		
	}
}