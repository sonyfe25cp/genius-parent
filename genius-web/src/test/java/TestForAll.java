//import java.util.ArrayList;
//import java.util.List;
//
//import org.duineframework.recommender.core.IRatableItem;
//
//import com.genius.dao.UserDao;
//import com.genius.dao.UserDaoMongoImpl;
//import com.genius.dao.XnewsDao;
//import com.genius.dao.XnewsDaoMongoImpl;
//import com.genius.model.User;
//import com.genius.recommender.dao.RecommenderDao;
//import com.genius.recommender.dao.RecommenderDaoImpl;
//import com.genius.recommender.model.XnewsCluster;
//
//import edu.bit.dlde.recommender.model.Xnews;
//
//import junit.framework.TestCase;
//
//
//public class TestForAll extends TestCase {
//	public static void testforall()
//	{
//		String sourceId = "1006";
//		RecommenderDao rd = new RecommenderDaoImpl();
//		((RecommenderDaoImpl)rd).setMongoHost("10.1.0.127");
//		((RecommenderDaoImpl)rd).setMongoDbName("dlde");
//		((RecommenderDaoImpl) rd).init();
//		List<XnewsCluster> axc = rd.GetAllXnewsClusters();
//		
//		XnewsDao xd = new XnewsDaoMongoImpl();
//		((XnewsDaoMongoImpl) xd).setMongoHost("10.1.0.127");
//		((XnewsDaoMongoImpl) xd).setMongoDbName("dlde");
//		((XnewsDaoMongoImpl) xd).init();
//		Xnews ccenter = xd.getXnewsByXid(sourceId);
//		if(ccenter!= null)
//		{
//			XnewsCluster xc = new XnewsCluster();
//			xc.setCenterXnews(ccenter,xd);
//			if(xc.getClusterMembers()!= null)
//			{
//				rd.PutXnewsCluster(xc);
//			}
//		}
////		if (cUser != null && sourceId != null) {
////			XnewsCluster xc = (new RecommenderDaoImpl()).GetXnewsClusterById(sourceId);
////	
////			cUser.addXnewsCluster(xc);
////			(new com.genius.recommender.action.XnewsAction()).update(cUser);
////			(new UserDaoMongoImpl()).save(cUser);
////		}
//		
////		UserDao ud = new UserDaoMongoImpl();
////		User cUser = ud.getUserById("huangzhi");
////		String sourceId= "10045";
////		if (cUser != null && sourceId != null) {
////			XnewsCluster xc = (new RecommenderDaoImpl()).GetXnewsClusterById(sourceId);
////			cUser.removeXnewsCluster(xc);
////			//cUser.addXnewsCluster(xc);
////			//update(cUser);
////			(new UserDaoMongoImpl()).save(cUser);
////		}
//		
//////		String sourceId = "10136";
////		RecommenderDao rd = new RecommenderDaoImpl();
//////		if (cUser != null && sourceId != null) {
//////			XnewsCluster xc = (new RecommenderDaoImpl()).GetXnewsClusterById(sourceId);
//////	
//////			cUser.addXnewsCluster(xc);
//////			//update(cUser);
//////			(new UserDaoMongoImpl()).save(cUser);
//////		}
////		List<XnewsCluster> all = rd.GetAllXnewsClusters();
////		if (cUser != null) {
////			String username = cUser.getUsername();
////			
////			for(XnewsCluster xc:all)
////			{
////				System.out.println("xc:"+xc.getCenterXnews().getXid());
////				for(XnewsCluster uxc:cUser.getXnewsClusters())
////				{
////					System.out.println("	uxc:"+uxc.getCenterXnews().getXid());
////					if(xc.getClusterId().equals(uxc.getClusterId()))
////					{
////						xc.getCenterXnews().setSelected(true);
////						System.out.println("---equl");
////						break;
////					}
////					else
////					{
////						xc.getCenterXnews().setSelected(false);
////					}
////				}
////			}
////			// System.out.println("have a UserName :" + username);
////		}
////		for(XnewsCluster xc:all)
////		{
////			if(xc.getCenterXnews().getSelected())
////			{
////				System.out.println(xc.getCenterXnews().getXid());
////			}
////		}
//		
////		List<IRatableItem> sources = new ArrayList<IRatableItem>();
////		for (XnewsCluster xc : cUser.getXnewsClusters()) {
////			System.out.print("XnewsCluster("+xc.getClusterId()+"):");
////			for(Xnews xnews:xc.getXnewsList())
////			{
////				System.out.print(xnews.getXid()+";");
////				boolean have = false;
////				for(IRatableItem i:sources)
////				{
////					if(((Xnews)i).getXid().equals(xnews.getXid()))
////					{
////						have = true;
////						break;
////					}
////				}
////				if(!have)
////				{
////					sources.add(xnews);
////				}
////			}
////			System.out.println();
////		}
////		System.out.print("Union:");
////		for(IRatableItem xnews:sources)
////		{
////			System.out.print(((Xnews)xnews).getXid()+";");
////		}
//	}
////	public static void testforeach()
////	{
////		List<String> test = new ArrayList<String>();
////		test.add("1");
////		test.add("2");
////		test.add("3");
////		test.add("4");
////		for(String i:test)
////		{
////			System.out.print(i+";");
////		}
////		System.out.println();
//////		for(int i = 0;i<test.size();i++)
//////		{
//////			if(test.get(i).equals("1")||test.get(i).equals("2"))
//////			{
//////				test.set(i, "s");
//////				System.out.println("s");
//////			}
//////		}
////		for(String i:test)
////		{
////			if(i.equals("1")||i.equals("2"))
////			{
////				i="s";
////				System.out.println("s");
////			}
////		}
////		for(String i:test)
////		{
////			System.out.print(i+";");
////		}
////		System.out.println();
////	}
//
//}
