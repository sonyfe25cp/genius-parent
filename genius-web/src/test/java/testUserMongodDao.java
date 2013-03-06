//import java.util.List;
//
//import com.genius.model.User;
//
//import com.genius.dao.UserDao;
//import com.genius.dao.UserDaoMongoImpl;
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
//public class testUserMongodDao extends TestCase {
//
//	/**
//	 * 
//	 */
//	public testUserMongodDao() {
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @param name
//	 */
//	public testUserMongodDao(String name) {
//		super(name);
//		// TODO Auto-generated constructor stub
//	}
//
//	public static void testUser()
//	{
////		UserDao ud = new UserDaoMongoImpl();
//		
////		addUsers(ud);
//		
////		List<User> users = ud.getAll();
////		for(User u:users)
////		{
////			System.out.println(u.getUsername()+";"+u.getPassword());
////		}
//		
//		
////		List<User> users = ud.getFirstList(4);	
////		User lastUser = null;
////		int count = 0;
////		
////		while(users.size()>0)
////		{
////			lastUser = users.get(users.size()-1);
////			for(User u:users)
////			{
////				System.out.println(count++ +":"+u.getUsername()+";"+u.getPassword());
////			}
////			users = ud.getNextList(5, lastUser.getUsername());
////		}
//		
////		System.out.println(ud.existUser("aa")+";false");
////		System.out.println(ud.existUser("A")+";true");
////		
////		System.out.println(ud.existUser("AAA")+";true");
////		System.out.println(ud.existUser("AAA","000002")+";true");
////		System.out.println(ud.existUser("AAA","000004")+";false");
////		System.out.println(ud.getSize());
////		List<User> users = ud.getOrderList(5, 5);
////			for(User u:users)
////			{
////				System.out.println(u.getUsername()+";"+u.getPassword());
////			}
////		ud.close();
//	}
//	public static void addUsers(UserDao ud)
//	{
//		
//		String[] names = {"A","AA","AAA","bisn","jack",
//				"rose","david","lily","lucy","pork",
//				"pllo","hama","andy","jusce","shana",
//				"taoma","simena","shonom","usiem","hodopa"};
//
//		for(int i = 0;i<20;i++)
//		{
//			User user = new User();
//			user.setUsername(names[i]);
//			user.setPassword("00000"+i);
//			ud.save(user);
//		}
//	}
//
//}
