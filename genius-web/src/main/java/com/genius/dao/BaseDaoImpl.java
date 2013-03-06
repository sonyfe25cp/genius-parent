///**
// * 
// */
//package com.genius.dao;
//
//import java.net.UnknownHostException;
//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.Morphia;
//import com.mongodb.Mongo;
//import com.mongodb.MongoException;
//
//import edu.bit.dlde.utils.DLDELogger;
//
///**
// * @author horizon
// *
// */
//public class BaseDaoImpl{
//	protected DLDELogger logger;
//
//	public DLDELogger getLogger() {
//		return logger;
//	}
//
//	public void setLogger(DLDELogger logger) {
//		this.logger = logger;
//	}
//
//	protected Mongo mongo = null;
//	protected Morphia morphia = null;
//	protected Datastore datastore = null;
//
//	protected String mongoHost = "10.1.0.171";
//	protected int mongoPort = 27017;
//	protected String mongoDbName = "genius";
//	
//	public String getMongoHost() {
//		return mongoHost;
//	}
//
//	public void setMongoHost(String mongoHost) {
//		this.mongoHost = mongoHost;
//	}
//
//	public int getMongoPort() {
//		return mongoPort;
//	}
//
//	public void setMongoPort(int mongoPort) {
//		this.mongoPort = mongoPort;
//	}
//
//	public String getMongoDbName() {
//		return mongoDbName;
//	}
//
//	public void setMongoDbName(String mongoDbName) {
//		this.mongoDbName = mongoDbName;
//	}
//	
//	public void init(Class<?> clazz){
//		if (datastore == null) {
//			if (mongo == null) {
//				try {
//					mongo = new Mongo(mongoHost, mongoPort);
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (MongoException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if (morphia == null) {
//				morphia = new Morphia();
//				morphia.mapPackageFromClass(clazz);
//			}
//			datastore = morphia.createDatastore(mongo, mongoDbName);
//			datastore.ensureIndexes();
//		}
//	}
//	public void close() {
//		// TODO Auto-generated method stub
//		datastore = null;
//		if(mongo != null)
//		{
//			mongo.close();
//			mongo = null;
//		}		
//	}
//	
//	public BaseDaoImpl() {
//		// TODO Auto-generated constructor stub
////		try {
////			init();
////		} catch (UnknownHostException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (MongoException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
//
//	public Mongo getMongo() {
//		return mongo;
//	}
//
//	public void setMongo(Mongo mongo) {
//		this.mongo = mongo;
//	}
//
//	public Morphia getMorphia() {
//		return morphia;
//	}
//
//	public void setMorphia(Morphia morphia) {
//		this.morphia = morphia;
//	}
//
//	public Datastore getDatastore() {
//		return datastore;
//	}
//
//	public void setDatastore(Datastore datastore) {
//		this.datastore = datastore;
//	}
//}
