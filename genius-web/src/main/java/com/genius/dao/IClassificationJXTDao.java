package com.genius.dao;

import java.util.List;
import java.util.Map;

import com.genius.model.Category;

import edu.bit.dlde.analysis.classification_jxt_forweb.ClassificationResult;

public interface IClassificationJXTDao {
	public void saveClassificationResults(List<ClassificationResult> result);
	public List<ClassificationResult> getClassificationResults();
	public List<ClassificationResult> getClassificationResults41Type(String type);
	public void deleteClassificationResults(String type);
	public void deleteClassificationResultsAll();
	public List<Category> loadCategories(boolean state);
	public void setTrainingSetNum(String type,int number);
	public void setClassifiedFileNum(String type,int number);
}
