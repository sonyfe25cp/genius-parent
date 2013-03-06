package edu.bit.dlde.genius.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;

/**
 * json 相关辅助类
 * @author ChenJie
 *
 */
public class GsonUtils {

//	private static Gson gson=new Gson();
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public static String getResultsJson(Results results) {
		return null;
	}

	public static String getResultsUnitJson(ResultsUnit resultsUnit) {
		String json=gson.toJson(resultsUnit, ResultsUnit.class);
		return json;
	}
	
	public static Results getResultsFromJson(String json){
		return null;
	}
	
	public static ResultsUnit getResultsUnitFromJson(String json){
		ResultsUnit unit=gson.fromJson(json, new TypeToken<ResultsUnit>(){}.getType());
		return unit;
	}

}
