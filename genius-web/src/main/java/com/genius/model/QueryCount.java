package com.genius.model;


import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

public class QueryCount implements Comparable<QueryCount> {
	public QueryCount() {
	}
	@Id
	private String query;
	@Indexed
	private int count;
	
	public QueryCount(String query, int count)
	{
		this.query = query;
		this.count = count ;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public String toString()
	{
		return "key is : " + this.query + "| count is :" + this.count;
	}
	/**
	 * 改成倒序
	 */
	@Override
	public int compareTo(QueryCount other) {
		if(this.count < other.count)
		{
			return 1;
		}
		else if (this.count > other.count)
		{
			return -1;
		}
		else return 0;
		
	}
}

