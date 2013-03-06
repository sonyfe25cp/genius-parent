package com.genius.dao;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;

import com.genius.model.ExtractConfiguration;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author lins
 */
public class ExtractConfigurationMongoDAOImpl extends MongoDAOBase implements
		IExtractConfigurationDAO {

	public ExtractConfigurationMongoDAOImpl() {

	}
	
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

	@Override
	public List<ExtractConfiguration> loadExtCfgs(int offect, int num) {
		return datastore.find(ExtractConfiguration.class).offset(offect)
				.limit(10).asList();
	}

	@Override
	public void saveExtCfg(ExtractConfiguration extCfg) {
		datastore.updateFirst(datastore.find(ExtractConfiguration.class)
				.filter("name ==", extCfg.getName()).filter("seed ==", extCfg.getSeed()), extCfg, true);
	}

	@Override
	public void updateExtCfg(ExtractConfiguration extCfg) {
		datastore.updateFirst(datastore.find(ExtractConfiguration.class)
				.filter("_id", extCfg.getId()), extCfg, true);
	}

	@Override
	public ExtractConfiguration loadExtCfg(String id) {
		return datastore.find(ExtractConfiguration.class)
				.filter("_id", new ObjectId(id)).asList().get(0);
	}
	
	@Override
	public List<ExtractConfiguration> loadExtCfgBySeed(String name) {
		return datastore.find(ExtractConfiguration.class)
				.filter("seed ==", name).asList();
	}

	@Override
	public void removeExtCfg(String id) {
		datastore.findAndDelete(datastore.find(ExtractConfiguration.class)
				.filter("_id", new ObjectId(id)));
	}
	
	
	@Override
	public void changeExtCfgStatus(String id) {
		Query<ExtractConfiguration> q = datastore.find(
				ExtractConfiguration.class).filter("_id", new ObjectId(id));
		Boolean enabled = q.asList().get(0).isEnabled();
		datastore.findAndModify(q,
				datastore.createUpdateOperations(ExtractConfiguration.class)
						.set("enabled", !enabled));

	}
	
	@Override
	public void removeExtCfgBySeed(String name) {
		List<ExtractConfiguration> list = datastore
				.find(ExtractConfiguration.class).filter("seed ==", name)
				.asList();
		for (ExtractConfiguration ecfg : list)
			datastore.delete(ecfg);
	}

	public static void main(String[] args) throws UnknownHostException,
			MongoException {
		ExtractConfigurationMongoDAOImpl em = new ExtractConfigurationMongoDAOImpl();
		// ExtractConfiguration ec = new ExtractConfiguration();
		// ec.setName("sina news");
		// ec.setType("news");
		// ec.setUri("sina");
		// ec.setXml("<rule><title>.//*[@id='artibodyTitle']</title><content>.//*[@id='artibody']/p</content></rule> ");
		// ec.setEnabled(true);
		em.init();
		em.changeExtCfgStatus("sina news");
	}

	@Override
	public long getCount() {
		return  datastore
				.find(ExtractConfiguration.class).countAll();
	}

	@Override
	public long getCount(String type) {
		return  datastore
				.find(ExtractConfiguration.class).filter("type ==", type).countAll();
	}

}
