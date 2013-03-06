//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.genius.dao.GeniusMongoDaoImpl;
//import com.genius.model.ExtractConfiguration;
//import com.genius.model.NewsDistance;
//import com.genius.model.NewsReport;
//import com.genius.utils.Page;
//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.Morphia;
//import com.mongodb.Mongo;
//import com.mongodb.MongoException;
//
//import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod;
//import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Distance;
//import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Item;
//
//import edu.bit.dlde.utils.DLDELogger;
//
//import junit.framework.TestCase;
//
///**
// * 
// */
//
///**
// * @author horizon
// *
// */
//public class TestCluster extends TestCase {
//
//	/**
//	 * 
//	 */
//	public TestCluster() {
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @param name
//	 */
//	public TestCluster(String name) {
//		super(name);
//		// TODO Auto-generated constructor stub
//	}
//	public static void testCluster(){
//		try {
//			GeniusMongoDaoImpl gmd = new GeniusMongoDaoImpl();
//			gmd.setMongoHost("10.1.0.171");
//			//gmd.setMongoHost("127.0.0.1");
//			gmd.setMongoPort(27017);
//			gmd.setLogger(new DLDELogger());
//			gmd.setMongoDbName("genius");
//			gmd.init();
//			List<Item> sources = new ArrayList<Item>();
//			List<NewsReport> allnews = gmd.loadNewsReportsInRange(new Page(1, 10));
//			System.out.println(allnews.size());
//			for(NewsReport nr:allnews)
//			{
//				sources.add(nr);
//			}
//			ClusterCenterMethod ccm = new ClusterCenterMethod();
//			Distance dis = new NewsDistance();
//			List<Item> result = ccm.getKMedoidsCenter(sources, dis, 2);
//			System.out.println(result.size());
//			for(Item i:result)
//			{
//				System.out.println(((NewsReport)i).getTitle());
//			}
//		} catch (MongoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
