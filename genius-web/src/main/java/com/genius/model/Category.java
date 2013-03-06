package com.genius.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.mongodb.Mongo;

/**
 *	@author zhangchangmin
 **/

@Entity(value="Category",noClassnameStored=true)
public class Category {

	@Id
	private ObjectId id = null;
	@Indexed
	private String name = null;
	@Indexed
	private String seed=null;
	@Indexed
	private boolean enabled = false;
	@Indexed
	private int trainingSetNum=0;
	@Indexed
	private int classifiedFileNum=0;
	
	public Category(){};
	public Category(String name,int trainingSetNum,boolean enabled)
	{
		this.name = name;
		this.trainingSetNum = trainingSetNum;
		this.enabled =  enabled;
	}
	public Category(String name,int trainingSetNum)
	{
		this.name = name;
		this.trainingSetNum = trainingSetNum;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getTrainingSetNum() {
		return trainingSetNum;
	}
	public void setTrainingSetNum(int trainingSetNum) {
		this.trainingSetNum = trainingSetNum;
	}
	public int getClassifiedFileNum() {
		return classifiedFileNum;
	}
	public void setClassifiedFileNum(int classifiedFileNum) {
		this.classifiedFileNum = classifiedFileNum;
	}
		 public static void main(String[] args) throws IOException
		   {
			    Mongo mongo = new Mongo("127.0.0.1", 27017);
				Morphia morphia = new Morphia();
				Datastore ds = morphia.createDatastore(mongo, "genius");
				List<Category> categories = new ArrayList<Category>();
				categories.add( new Category("环境",151,true));
				categories.add( new Category("政治",455,true));
				categories.add( new Category("医药",154,true));
				categories.add( new Category("计算机",150,true));
				categories.add( new Category("教育",170,true));
				categories.add( new Category("经济",275,true));
				categories.add( new Category("交通",164,true));
				categories.add( new Category("体育",400,true));
				categories.add( new Category("艺术",198,true));
				categories.add( new Category("军事",199,true));
				ds.save(categories);
		   }
	}
