package com.genius.dao;

import java.util.List;
import java.util.Map;

import com.genius.model.Category;

/**
 *	@author zhangchangmin
 **/
public interface ICategoryDAO {
	
	public List<Category> loadCategories();
	public List<Category> loadCategories(int offect, int num);
	public List<Category> loadCategoriesByStatus(boolean status);
	public List<Category> loadCategoriesByStatus(int off,int num);
	public Category loadCategory(String name);
	public void saveCategory(Category category);
	public void updateCategory(Category category);
	public void removeCategory(String name);
	public void changeCategoryStatus(String name);
	public void setTrainingSetNum(String type, int number);
	public void setClassifiedFileNum(String type, int number);
	public void saveLocalFile2TrainingFile(List<String> dirs);
	public void saveWebPage2TrainingFile(Map<String,Integer> urls);
	public void changeFileStatus(String id);
	public void changeAllFilesStatus(boolean status);
}
