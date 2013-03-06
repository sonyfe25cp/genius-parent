package edu.bit.dlde.analysis.dao;

import java.io.File;
import java.util.List;
import java.util.Map;

import edu.bit.dlde.analysis.model.LetorModel;

public interface ILetorDao {
	
//	public List<LetorModel> readData(File file);
	
	public Map<Integer,List<LetorModel>> readData(File file);
	
}
