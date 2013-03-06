package edu.bit.dlde.analysis.dao;

import java.util.List;

import edu.bit.dlde.analysis.model.DLDEWebPage;

/**
 * @author ChenJie
 *
 */
public interface IStoreData {

	
	void store(String link);
	
	public List<DLDEWebPage> store(List<DLDEWebPage> pageList);
	
	public void store(DLDEWebPage page);
	
	public boolean hasLink(String link);
	
	public String getPageBody(String link);
}
