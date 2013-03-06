package com.genius.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

import edu.bit.dlde.utils.DLDELogger;

/**
 * @author lins
 */
public abstract class MongoDAOBase {
	protected Mongo mongo = null;
	protected Morphia morphia = null;
	protected Datastore datastore = null;

	protected String mongoHost = "";
	protected int mongoPort = 27017;
	protected String mongoDbName = "genius";

	protected DLDELogger logger;
	
	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDbName() {
		return mongoDbName;
	}

	public void setMongoDbName(String mongoDbName) {
		this.mongoDbName = mongoDbName;
	}

	public void close() {
//		datastore = null;
//		if(mongo != null)
//		{
//			mongo.close();
//			mongo = null;
//		}
	}
	public abstract void init();

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	
	
}
