package com.genius.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.genius.model.IgnoredHotTerms;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author lins
 * @date 2012-5-23
 **/
public class IgnoredHotTermMongoDaoImpl extends MongoDAOBase implements
		IIgnoredHotTermsDao {

	@Override
	public void addIgnored(String term) {
		List<IgnoredHotTerms> ihts = datastore.find(IgnoredHotTerms.class)
				.asList();
		IgnoredHotTerms iht;
		if (ihts == null || ihts.size() == 0) {
			iht = new IgnoredHotTerms();
			iht.setTerms(new ArrayList<String>());
		} else {
			iht = ihts.get(0);
		}
		iht.getTerms().add(term);
		datastore.updateFirst(datastore.find(IgnoredHotTerms.class), iht, true);
	}

	@Override
	public void removeIgnored(String[] terms) {
		List<IgnoredHotTerms> ihts = datastore.find(IgnoredHotTerms.class)
				.asList();
		if (ihts == null || ihts.size() == 0) {
			return;
		} else {
			IgnoredHotTerms iht = ihts.get(0);
			if (iht == null) {
				return;
			}

			for (String term : terms) {
				iht.getTerms().remove(term);
			}
			datastore.updateFirst(datastore.find(IgnoredHotTerms.class), iht,
					true);
		}
	}

	@Override
	public void removeIgnored(String term) {
		List<IgnoredHotTerms> ihts = datastore.find(IgnoredHotTerms.class)
				.asList();
		if (ihts == null || ihts.size() == 0) {
			return;
		} else {
			IgnoredHotTerms iht = ihts.get(0);
			if (iht == null) {
				return;
			}

			iht.getTerms().remove(term);
			datastore.updateFirst(datastore.find(IgnoredHotTerms.class), iht,
					true);
		}
	}

	@Override
	public void addIgnored(String[] terms) {
		List<IgnoredHotTerms> ihts = datastore.find(IgnoredHotTerms.class)
				.asList();
		IgnoredHotTerms iht;
		if (ihts == null || ihts.size() == 0) {
			iht = new IgnoredHotTerms();
			iht.setTerms(new ArrayList<String>());
		} else {
			iht = ihts.get(0);
			if(iht.getTerms()==null){
				iht.setTerms(new ArrayList<String>());
			}
		}

		for (String term : terms) {
			iht.getTerms().add(term.substring(1, term.length() - 1));
		}

		datastore.updateFirst(datastore.find(IgnoredHotTerms.class), iht, true);
	}

	@Override
	public IgnoredHotTerms getIgnored() {
		List<IgnoredHotTerms> ihts = datastore.find(IgnoredHotTerms.class)
				.asList();
		IgnoredHotTerms iht;
		if (ihts == null || ihts.size() == 0) {
			iht = new IgnoredHotTerms();
			iht.setTerms(new ArrayList<String>());
			return iht;
		} else {
			iht = ihts.get(0);
			if(iht.getTerms()==null)
				iht.setTerms(new ArrayList<String>());
			return iht;
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
					morphia.mapPackageFromClass(IgnoredHotTerms.class);
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
