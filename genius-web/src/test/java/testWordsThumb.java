//import java.util.HashMap;
//import java.util.List;
//
//import junit.framework.TestCase;
//
//import org.junit.Test;
//
//import com.genius.dao.GeniusMongoDaoImpl;
//import com.genius.model.QueryLog;
//import com.mongodb.MongoException;
//
//import edu.bit.dlde.tools.graphics.WordsThumb;
//
//
//public class testWordsThumb extends TestCase{
//
//	@Test
//	public void testGeneratePNG(){
//		GeniusMongoDaoImpl dao=new GeniusMongoDaoImpl();
//		String qCloud = "/home/coder/workspace/mavenSpace/genius-web/src/main/webapp/cloud/qCloud.png";
//		String bgPng = "/home/coder/workspace/mavenSpace/genius-web/src/main/webapp/cloud/blank.png";
//		try {
//			dao.init();
//		}  catch (MongoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		List<QueryLog> logList=dao.loadQueryLogInRange(null);
//		
//		HashMap<String,Integer> wordsMap= new HashMap<String,Integer>();
//		for(QueryLog log:logList){
//			String word=log.getQuery();
//			if(wordsMap.containsKey(word)){
//				int count=wordsMap.get(word);
//				wordsMap.put(word, count+1);
//			}else{
//				wordsMap.put(word, 1);
//			}
//		}
//		WordsThumb.getWordCloud(wordsMap, qCloud, bgPng, null);//
//	}
//}
