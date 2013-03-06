package com.genius.utils;

/**
 * 对Query的辅助
 * @author ChenJie
 *
 */
public class QueryUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 检查query的合法性
	 * @param q
	 * May 29, 2012
	 */
	public static String verifyQuery(String q){
		q=q.trim();
		if(q.length()==0){
			return null;
		}
		return q;
	}
}
