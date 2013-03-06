/**
 * 
 */
package com.genius.dao;

import java.net.UnknownHostException;
import java.util.List;

import com.genius.model.ExtractConfiguration;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import edu.bit.dlde.recommender.model.Xnews;

/**
 * @author horizon
 * 
 */
public class XnewsDaoMongoImpl extends MongoDAOBase implements XnewsDao {


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.genius.dao.XnewsDao#getList(int, int)
	 */
	@Override
	public List<Xnews> getList(int count, int offset) {
		// TODO Auto-generated method stub
		List<Xnews> result = datastore.find(Xnews.class).offset(offset)
				.limit(count).asList();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.genius.dao.XnewsDao#getOrderList(int, int)
	 */
	@Override
	public List<Xnews> getOrderList(int count, int offset) {
		// TODO Auto-generated method stub
		List<Xnews> result = datastore.find(Xnews.class).order("xid")
				.filter("xid >=", "0").offset(offset).limit(count).asList();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.genius.dao.XnewsDao#getSize()
	 */
	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		long result = datastore.find(Xnews.class).countAll();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.genius.dao.XnewsDao#save(edu.bit.dlde.recommender.model.Xnews)
	 */
	@Override
	public boolean save(Xnews xnews) {
		// TODO Auto-generated method stub
		Key<Xnews> result = datastore.save(xnews);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Xnews getXnewsByXid(String xid) {
		List<Xnews> result = datastore.find(Xnews.class).filter("xid =", xid)
				.asList();
		if (result.size() == 0) {
			logger.warn("Xnews :" + xid + " not found");
			return null;
		} else {
			return result.get(0);
		}
	}

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

}
