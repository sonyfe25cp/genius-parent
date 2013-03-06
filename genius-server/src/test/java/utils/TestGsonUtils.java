package utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.genius.utils.GsonUtils;
import edu.bit.dlde.utils.DLDELogger;

public class TestGsonUtils extends TestCase {

	private ResultsUnit unit;
	private DLDELogger logger = new DLDELogger();

	public void init() {
		List<Results> resultsList = new ArrayList<Results>();
		resultsList.add(new Results(1+"", 1.123));
		resultsList.add(new Results(2+"", 2.234));
		resultsList.add(new Results(3+"", 3.345));
		resultsList.add(new Results(4+"", 4.456));

		unit = new ResultsUnit();
		unit.setUniqueQueryKey(1);
		unit.setResultsList(resultsList);
	}

	String unitAnswer = "{\"uniqueQueryKey\":1,\"resultsList\":[{\"resourceKey\":\"1\",\"score\":1.123},{\"resourceKey\":\"2\",\"score\":2.234},{\"resourceKey\":\"3\",\"score\":3.345},{\"resourceKey\":\"4\",\"score\":4.456}],\"totalNum\":0,\"time\":0}";

	@Test
	public void testUnitToJson() {
		init();
		String json = GsonUtils.getResultsUnitJson(unit);
		logger.info(unitAnswer);
		logger.info(json);
		assertEquals(unitAnswer,json);
	}
	
	@Test
	public void testJsonToUnit() {
		init();
		ResultsUnit unit2=GsonUtils.getResultsUnitFromJson(unitAnswer);
//		assertEquals(unit2,unit);
		logger.info(GsonUtils.getResultsUnitJson(unit2));
	}
	

}
