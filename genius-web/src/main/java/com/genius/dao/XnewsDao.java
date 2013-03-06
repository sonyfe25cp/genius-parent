/**
 * 
 */
package com.genius.dao;

import java.util.List;

import edu.bit.dlde.recommender.model.Xnews;

/**
 * @author horizon
 *
 */
public interface XnewsDao {
	
	public long getSize();
	
	public List<Xnews> getList(int count,int offset);
	public List<Xnews> getOrderList(int count,int offset);
		
	public boolean save(Xnews xnews);
	public Xnews getXnewsByXid(String xid);
	
}
