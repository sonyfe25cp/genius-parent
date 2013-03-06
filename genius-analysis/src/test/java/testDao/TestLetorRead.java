package testDao;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;

import edu.bit.dlde.analysis.dao.ILetorDao;
import edu.bit.dlde.analysis.dao.impl.LetorDaoImplMicrosoftData;
import edu.bit.dlde.analysis.model.LetorModel;
import edu.bit.dlde.utils.DLDELogger;

public class TestLetorRead extends TestCase{

	private DLDELogger logger=new DLDELogger();
	
	@Test
	public void testReadMicrosoftData(){
//		String filePath="/store/dataStore/learntorank/MSLR-WEB10K/Fold1/test.txt";
//		ILetorDao dao=new LetorDaoImplMicrosoftData();
//		Map<Integer, List<LetorModel>> map=dao.readData(new File(filePath));
//		int total=0;
//		for(Entry<Integer, List<LetorModel>> entry:map.entrySet()){
//			int qId=entry.getKey();
//			List<LetorModel> list=entry.getValue();
////			for(LetorModel model:list){
////				logger.info(model.toString());
////			}
//			logger.info(qId+" : doucument list size : "+list.size());
//			total+=list.size();
//		}
//		logger.info("map.size:"+map.size());
//		logger.info("total:"+total);
	}
}
