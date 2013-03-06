//import junit.framework.TestCase;
//
//import com.genius.dao.GeniusMongoDaoImpl;
//import com.genius.dao.UserDaoMongoImpl;
//import com.genius.recommender.dao.RecommenderDaoImpl;
//import com.genius.recommender.utils.UpdateModels;
//
//import edu.bit.dlde.utils.DLDELogger;
//
///**
// * 
// */
//
///**
// * @author horizon
// *
// */
//public class TestUpdateModel extends TestCase{
//
//	/**
//	 * 
//	 */
//	public TestUpdateModel() {
//		// TODO Auto-generated constructor stub
//
//	}
//	public static void testUpdateModel(){
//		GeniusMongoDaoImpl gmd = new GeniusMongoDaoImpl();
////		gmd.setMongoHost("10.1.0.171");
//		gmd.setMongoHost("127.0.0.1");
//		gmd.setMongoPort(27017);
//		gmd.setLogger(new DLDELogger());
//		gmd.setMongoDbName("genius");
//		gmd.init();
//		
//		RecommenderDaoImpl rd = new RecommenderDaoImpl();
//		rd.setMongoHost("127.0.0.1");
//		rd.setMongoPort(27017);
//		rd.setLogger(new DLDELogger());
//		rd.setMongoDbName("genius");
//		rd.init();
//		
//		UserDaoMongoImpl ud = new UserDaoMongoImpl();
//		ud.setMongoHost("127.0.0.1");
//		ud.setMongoPort(27017);
//		ud.setLogger(new DLDELogger());
//		ud.setMongoDbName("genius");
//		ud.init();
//		
//		String newsId = "4fb5f264de41d4b0204a9204";
//		UpdateModels.removeANews(gmd.loadNewsReportByID(newsId), rd, ud, gmd);
//	}
//
//}
