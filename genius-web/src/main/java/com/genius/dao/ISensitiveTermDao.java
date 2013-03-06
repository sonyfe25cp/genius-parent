package com.genius.dao;

import java.util.Collection;
import java.util.List;

import com.genius.model.SensitiveTerm;

/**
 *
 *@author lins
 *@date 2012-5-17
 **/
public interface ISensitiveTermDao {
	public void disable(String id);
	public void disableAll(String[] ids);
	public void add(SensitiveTerm term);
	public void addAll(Collection<SensitiveTerm> terms);
	public void save(SensitiveTerm term);
	public void saveAll(Collection<SensitiveTerm> terms);
	public List<SensitiveTerm> getAll();
	/**
	 *  front is included while end not
	 */
	public List<SensitiveTerm> getSome(int front, int end);
	public SensitiveTerm get(String term);
	public SensitiveTerm getById(String id);
	public int getRecordCount();
}
