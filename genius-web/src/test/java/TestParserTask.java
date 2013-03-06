//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import bit.mirror.core.Coordinator;
//import bit.mirror.dao.mongo.MongoDao;
//
//import com.genius.dao.DocFeatureVectorMongoDAOImpl;
//import com.genius.dao.ExtractConfigurationMongoDAOImpl;
//import com.genius.dao.GeniusMongoDaoImpl;
//import com.genius.dao.SensitiveTermMongoDaoImpl;
//import com.genius.dao.StatisticRecordMongoDaoImpl;
//import com.genius.task.ParserTask;
//import com.genius.task.help.ContentExtractorChain;
//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.Morphia;
//import com.mongodb.Mongo;
//
///**
// * ParserTask和TermStatisticTask的测试类
// */
//public class TestParserTask {
//	public static void main(String[] args) throws Exception {
//		// ApplicationContext context = new FileSystemXmlApplicationContext(
//		// "src/main/java/test-config.xml");
//		//
//		// ParserTask pt = (ParserTask) context.getBean("DailyParseData");
//		Mongo mongo = new Mongo("10.1.0.171", 27017);
//		Morphia morphia = new Morphia();
//		Datastore datastore = morphia.createDatastore(mongo, "genius");
//		MongoDao mirrorengineDao = new MongoDao();
//		mirrorengineDao.setMongo(mongo);
//		mirrorengineDao.setDatastore(datastore);
//		mirrorengineDao.start();
//		Coordinator mirrorengineCoordinator = new Coordinator();
//		mirrorengineCoordinator.setDao(mirrorengineDao);
//		mirrorengineCoordinator.setSleepAfterCrawling(15000);
//		mirrorengineCoordinator.setLoadSeedsOnStartup(0);
//		mirrorengineCoordinator.setSuspended(true);
//		mirrorengineCoordinator.start();
//
//		GeniusMongoDaoImpl dao = new GeniusMongoDaoImpl();
//		dao.setMongo(mongo);
//		dao.setDatastore(datastore);
//
//		ExtractConfigurationMongoDAOImpl ecmDao = new ExtractConfigurationMongoDAOImpl();
//		ecmDao.setMongo(mongo);
//		ecmDao.setDatastore(datastore);
//		ecmDao.setMorphia(morphia);
//
//		StatisticRecordMongoDaoImpl srmDAO = new StatisticRecordMongoDaoImpl();
//		srmDAO.setMongo(mongo);
//		srmDAO.setDatastore(datastore);
//		srmDAO.setMorphia(morphia);
//
//		SensitiveTermMongoDaoImpl stmDAO = new SensitiveTermMongoDaoImpl();
//		stmDAO.setMongo(mongo);
//		stmDAO.setDatastore(datastore);
//		stmDAO.setMorphia(morphia);
//
//		DocFeatureVectorMongoDAOImpl dfvDAO = new DocFeatureVectorMongoDAOImpl();
//		dfvDAO.setMongo(mongo);
//		dfvDAO.setDatastore(datastore);
//		dfvDAO.setMorphia(morphia);
//
//		ParserTask pt = new ParserTask();
//		pt.setDao(dao);
//		pt.setMirrorengineCoordinator(mirrorengineCoordinator);
//		pt.setSrmDAO(srmDAO);
//		pt.setStmDAO(stmDAO);
//		pt.setDfvDAO(dfvDAO);
//		pt.setEcmDAO(ecmDao);
//
//		pt.runInternal(null, new SimpleDateFormat().parse("2012-06-29 上午01:03"), new Date());
//	}
//}
