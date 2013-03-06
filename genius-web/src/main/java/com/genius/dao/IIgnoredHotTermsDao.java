package com.genius.dao;

import com.genius.model.IgnoredHotTerms;

/**
 *
 *@author lins
 *@date 2012-5-23
 **/
public interface IIgnoredHotTermsDao {
	public void addIgnored(String term);
	public void removeIgnored(String[] term);
	public void removeIgnored(String term);
	public void addIgnored(String[] term);
	public IgnoredHotTerms getIgnored();
}
