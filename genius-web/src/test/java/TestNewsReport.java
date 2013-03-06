//import java.net.UnknownHostException;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import org.bson.types.ObjectId;
//
//import com.genius.dao.GeniusMongoDaoImpl;
//import com.genius.model.ExtractConfiguration;
//import com.genius.model.NewsReport;
//import com.genius.recommender.model.ClusterComparator;
//import com.genius.recommender.model.ClusterMember;
//import com.genius.recommender.model.XnewsCluster;
//import com.genius.utils.Page;
//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.Morphia;
//import com.mongodb.Mongo;
//import com.mongodb.MongoException;
//
//import edu.bit.dlde.utils.DLDELogger;
//
//import junit.framework.TestCase;
//
//
//
//public class TestNewsReport extends TestCase{
//	public static void testDao()
//	{
////		NewsReport nr = new NewsReport();
////		nr.setAuthor("huangzhi");
////		nr.setContent("content");
////		nr.setUrl("url");
////		nr.setPublishTime(new Date());
//		Mongo mongo;
//		try {
//			mongo = new Mongo("127.0.0.1", 27017);
//			Morphia morphia = new Morphia();
//			morphia.mapPackageFromClass(ExtractConfiguration.class);
//			morphia.map(NewsReport.class);
//			Datastore datastore = morphia.createDatastore(mongo, "genius");
//			
//			GeniusMongoDaoImpl gmd = new GeniusMongoDaoImpl();
//			gmd.setMongoHost("10.1.0.171");
//			//gmd.setMongoHost("127.0.0.1");
//			gmd.setMongoPort(27017);
//			gmd.setLogger(new DLDELogger());
//			gmd.setMongoDbName("genius");
//			gmd.init();
//			gmd.loadNewsReportsInRange(new Page(1,10));
//			
////			List<NewsReport> results = gmd.loadNewsReportsInRange(new Page(1,100));
////			for(NewsReport nr:results)
////			{
////				datastore.save(nr);
////			}
//			
//////			datastore.save(nr);
//			
//			NewsReport nr = datastore.find(NewsReport.class).filter("_id", new ObjectId("4fb5f265de41d4b0204a921a")).get();
//			//List<NewsReport> results = gmd.loadNewsReportsInRange(new Page(1,100));
//			//Query<NewsReport> nr = datastore.find(NewsReport.class).filter("_id", new ObjectId("4fb5f261de41d4b0204a91da"));
//			
//			
////			for(NewsReport nr:results)
////			{
////				datastore.save(nr);
////			}
////			XnewsCluster xc = new XnewsCluster();
//			XnewsCluster xc = datastore.find(XnewsCluster.class).filter("_id", "4fb5f265de41d4b0204a921a").get();
//			List<NewsReport> sum = xc.getXnewsList();
//			System.out.println(sum.contains(nr));
////			NewsReport nr = new NewsReport();
////			nr.setId(new ObjectId("4fb5f261de41d4b0204a91da"));
//			xc.AddANews(nr);
////			datastore.save(xc);
//			//datastore.delete(nr);
////			
////			xc.setMinNumber(0);
////			xc.setCenterXnews(results.get(5), gmd);
//			datastore.save(xc);
//			xc = datastore.find(XnewsCluster.class).filter("_id", "4fb5f265de41d4b0204a921a").get();
//			sum = xc.getXnewsList();
//			for(NewsReport nrs:sum)
//			{
//				System.out.print(nrs.getUrl()+"\n");
//			}
//			System.out.println(sum.contains(nr));
//			
//		} catch (MongoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
