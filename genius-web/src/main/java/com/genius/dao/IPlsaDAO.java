package com.genius.dao;
import java.util.HashMap;
import java.util.List;

import edu.bit.dlde.analysis.plsa.DocType;

/**
 *	@author zhangchangmin
 **/
public interface IPlsaDAO {

	public List<DocType> loadDocs(int off,int num);
	public List<DocType> loadDocsByType(String type);
	public HashMap<String,Integer> typeList(int off,int num);
}
