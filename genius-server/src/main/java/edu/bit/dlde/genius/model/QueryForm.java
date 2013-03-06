package edu.bit.dlde.genius.model;



public class QueryForm extends Order {
	
	private static final long serialVersionUID = 2L;
	private int uniqueQueryKey;//查询唯一key
	private int uniqueUserKey;//查询用户的唯一key
	
	private String keyWords;//查询关键词
	
	private int pageNo;
	private int pageSize;
	
	public static final int DEFAULTPAGESIZE=10;
	
	public QueryForm(){
		this.setType(QUERY);
		this.pageSize=DEFAULTPAGESIZE;
	}

	public int getUniqueUserKey() {
		return uniqueUserKey;
	}
	public void setUniqueUserKey(int uniqueUserKey) {
		this.uniqueUserKey = uniqueUserKey;
	}
	public int getUniqueQueryKey() {
		return uniqueQueryKey;
	}
	public void setUniqueQueryKey(int uniqueQueryKey) {
		this.uniqueQueryKey = uniqueQueryKey;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
