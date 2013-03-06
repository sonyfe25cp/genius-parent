package com.genius.dao;

import java.net.UnknownHostException;
import java.util.List;

import com.genius.model.DocFeatureVector;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author lins
 * @date 2012-6-3
 **/
public class DocFeatureVectorMongoDAOImpl extends MongoDAOBase implements
		IDocFeatureVectorDAO {

	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(DocFeatureVector.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DocFeatureVector getFVByUrl(String url) {
		List<DocFeatureVector> list = datastore.find(DocFeatureVector.class)
				.filter("url", url).asList();
		if (list == null || list.size() == 0)
			return null;
		else
			return list.get(0);
	}

	@Override
	public void saveDocFeatureVector(DocFeatureVector docFV) {
		if (docFV == null || docFV.getUrl() == null
				|| docFV.getUrl().trim().equals(""))
			return;
		datastore.updateFirst(
				datastore.find(DocFeatureVector.class).filter("url",
						docFV.getUrl()), docFV, true);
	}
}
