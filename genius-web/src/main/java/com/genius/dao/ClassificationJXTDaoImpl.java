package com.genius.dao;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genius.model.Category;
import com.genius.model.ExtractConfiguration;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import edu.bit.dlde.analysis.classification_jxt_forweb.ClassificationResult;

public class ClassificationJXTDaoImpl extends MongoDAOBase implements
		IClassificationJXTDao {
	private CategoryDAOMongoImpl catDAO;

	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(ExtractConfiguration.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveClassificationResults(List<ClassificationResult> classificationResults) {
		catDAO.resetClassifiedFileNum();
		//catDAO.removeClassificationResult();
		datastore.save(classificationResults);
		System.out.println("saved_classificationResults:"+classificationResults
				+"size="+classificationResults.size());
		//更新category中的已分类文件数目
		Map<String,Integer> typeDocNum = new HashMap<String,Integer>();
		for(int i=0;i<classificationResults.size();i++)
		{
			if(typeDocNum.containsKey(classificationResults.get(i).getType()))
				typeDocNum.put(classificationResults.get(i).getType(), 
						typeDocNum.get(classificationResults.get(i).getType())+1);
			else typeDocNum.put(classificationResults.get(i).getType(),1);
		}
		System.out.println("typeDocNum="+typeDocNum);
		for(int i=0;i<classificationResults.size();i++)
			if(typeDocNum.containsKey(classificationResults.get(i).getType()))
				catDAO.setClassifiedFileNum(classificationResults.get(i).getType(),
						typeDocNum.get(classificationResults.get(i).getType()));
	}

	@Override
	public List<ClassificationResult> getClassificationResults() {
		return datastore.find(ClassificationResult.class).asList();
	}

	@Override
	public List<ClassificationResult> getClassificationResults41Type(String type) {
		return datastore.find(ClassificationResult.class).field("type")
				.equal(type).asList();
	}

	@Override
	public void deleteClassificationResults(String type) {
		// TODO Auto-generated method stub
		datastore.delete(datastore.createQuery(ClassificationResult.class)
				.field("name").equal(type));
	}

	@Override
	public void deleteClassificationResultsAll() {
		// TODO Auto-generated method stub
		datastore.delete(datastore.createQuery(ClassificationResult.class));
	}

	@Override
	public List<Category> loadCategories(boolean state) {
		// TODO Auto-generated method stub
		return catDAO.loadCategoriesByStatus(state);
	}

	@Override
	public void setTrainingSetNum(String type, int number) {
		// TODO Auto-generated method stub
		catDAO.setTrainingSetNum(type, number);
	}

	@Override
	public void setClassifiedFileNum(String type, int number) {
		// TODO Auto-generated method stub
		catDAO.setClassifiedFileNum(type, number);
	}
	public CategoryDAOMongoImpl getCatDAO() {
		return catDAO;
	}

	public void setCatDAO(CategoryDAOMongoImpl catDAO) {
		this.catDAO = catDAO;
	}
	public static void main(String[] args)
	{
		ClassificationJXTDaoImpl impl = new ClassificationJXTDaoImpl();
		impl.init();
		impl.datastore.delete(impl.datastore.createQuery(ClassificationResult.class));
	}
}
