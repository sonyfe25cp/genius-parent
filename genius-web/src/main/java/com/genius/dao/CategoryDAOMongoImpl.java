package com.genius.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.genius.model.Category;
import com.genius.model.TrainingFile;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import edu.bit.dlde.analysis.classification_jxt_forweb.ClassificationResult;
import edu.bit.dlde.analysis.plsa.PLSA;
import edu.bit.dlde.analysis.plsa.TermDoc;
import edu.bit.dlde.analysis.plsa.TermFreq;


/**
 *	@author zhangchangmin
 **/

public class CategoryDAOMongoImpl extends MongoDAOBase implements ICategoryDAO {
	
	String indexPath = "C:\\Users\\jiang\\data\\index\\genius2";
	
	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public CategoryDAOMongoImpl(){
	}
	
	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(Category.class);
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
	public List<Category> loadCategories(){
		return datastore.find(Category.class).asList();
	}

	@Override
	public List<Category> loadCategories(int offect, int num){
		return datastore.find(Category.class).field("enabled").equal(true).offset(offect)
				.limit(num).asList();
	}
	@Override
	public List<Category> loadCategoriesByStatus(boolean status){
		return datastore.find(Category.class).field("enabled").equal(status).asList();
	}
	@Override
	public List<Category> loadCategoriesByStatus(int off,int num){
		return datastore.find(Category.class)
				.filter("enabled ==", true).offset(off).limit(num).asList();
	}
	@Override
	public Category loadCategory(String name){
		return datastore.find(Category.class)
				.filter("name ==", name).asList().get(0);
	}
	@Override
	public void saveCategory(Category category){
		datastore.updateFirst(datastore.find(Category.class)
				.filter("name ==", category.getName()), category, true);
	}
	@Override
	public void updateCategory(Category category){
		datastore.updateFirst(datastore.find(Category.class)
				.filter("name ==", category.getName()), category, true);
	}
	@Override
	public void removeCategory(String name){
		datastore.update(datastore.createQuery(Category.class).field("name").equal(name), 
				datastore.createUpdateOperations(Category.class).set("enabled", false));
	}
	@Override
	public void changeCategoryStatus(String name){
		Query<Category> q = datastore.find(
				Category.class).filter("name ==", name);
		Boolean enabled = q.asList().get(0).isEnabled();
		datastore.findAndModify(q,
				datastore.createUpdateOperations(Category.class)
						.set("enabled", !enabled));
	}
	
	@Override
	public void setTrainingSetNum(String type, int number) {
		Category category = datastore.find(Category.class).field("name").equal(type).get();
		datastore.update(category, datastore.createUpdateOperations(Category.class)
				.set("trainingSetNum", number));
	}
	@Override
	public void setClassifiedFileNum(String type, int number) {
		
		Category category = datastore.find(Category.class).field("name").equal(type).get();
		datastore.update(category, datastore.createUpdateOperations(Category.class)
				.set("classifiedFileNum", number));
	}
	
	@Override
	public void saveLocalFile2TrainingFile(List<String> dirs) {
		ArrayList<TrainingFile> trainingFiles = new ArrayList<TrainingFile>();
		ArrayList<File> localFiles = new ArrayList<File>();
        for(int i=0;i<dirs.size();i++)
        {
        	File file = new File(dirs.get(i));
        	//不是目录->添加，是目录->广度遍历其中的所有文件
        	if(file.isDirectory())
        	{
        		Queue<File> fileQueue = new LinkedList<File>();
        		fileQueue.offer(file);
        		while(fileQueue.size()!=0)
        		{
        			File file1 = fileQueue.poll();
        			if(file1.isDirectory())
        			{
        				File[] file1s = file1.listFiles();
        				for(int j=0;j<file1s.length;j++)
        					fileQueue.offer(file1s[j]);
        			}
        			else
        				localFiles.add(file1);
        		}
        	}
        	else
        		localFiles.add(file);
        }
        
        for(File file:localFiles)
        {
        	String body="",tmp="";
        	try {
        		BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file),"UTF-8"));
        		while((tmp=reader.readLine())!=null)
        			body+=tmp;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String beforeSplit = file.getName().replace(".txt", ""); 
   	        String[] splited = beforeSplit.split("@");
   	        String type = null;
   	        String title = null;
   	        for(int k = 0; k<splited.length ; k++){
	    	  if(k == 0)
	    		type =splited[k];
	          if(k == splited.length-1)
	        	title =splited[k];
	      }
   	     trainingFiles.add(new TrainingFile(beforeSplit, title, type, body, null));
        }
        System.out.println("trainingFiles_Size="+trainingFiles.size());
        datastore.save(trainingFiles);
	}
	
	@Override
	public void saveWebPage2TrainingFile(Map<String,Integer> pageIdTypeMap){
		
		Directory indexDir;
		IndexReader pageReader;
		ArrayList<TrainingFile> trainingFiles = new ArrayList<TrainingFile>();
		try {
			indexDir = FSDirectory.open(new File(indexPath));
			pageReader = IndexReader.open(indexDir);
			for (int i = 0; i < pageReader.numDocs(); i++) 
				if(pageIdTypeMap.keySet().contains(pageReader.document(i).getField("id").stringValue()))
				{
					Document doc = pageReader.document(i);
					String id = pageReader.document(i).getField("id").stringValue();
					String author = doc.getField("author").stringValue();
					String type = doc.getField("type").stringValue();
					String title = doc.getField("title").stringValue();
					String body = doc.getField("body").stringValue();
					trainingFiles.add(new TrainingFile(id, title, type, body, author));
					break;
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void changeFileStatus(String id){
		
	}
	
	@Override
	public void changeAllFilesStatus(boolean status){
		
	}
	
	public List<TrainingFile> loadTrainingFile(String catName){
		List<TrainingFile> trainingFiles = datastore.find(TrainingFile.class).
				field("type").equal(catName).asList();
		return trainingFiles;
	}
	
	public ArrayList<Boolean> loadFileState41Cat(String catName){
		ArrayList<Boolean> fileState41Cat = new ArrayList<Boolean>();
		List<TrainingFile> trainingFiles = datastore.find(TrainingFile.class).
				field("type").equal(catName).asList();
		
		int enabledNum=0,disabledNum=0;
		for(int i=0;i<trainingFiles.size();i++)
		{
			if(trainingFiles.get(i).isEnabled()==true)
				enabledNum++;
			else disabledNum++;
			fileState41Cat.add(trainingFiles.get(i).isEnabled());
		}
		System.out.println("fileState41Cat loaded! size="+fileState41Cat.size()
				+"enabledFiles="+enabledNum+",disabledFiles="+disabledNum);
		return fileState41Cat;
	}
	
//	public void analyzeAndSaveFileState(String stateChangeStr,int currentPageNo){
//		String[] stateChangeSplit = stateChangeStr.split("/");
//		List<CategoryFileStateChange> stateChangeList = 
//				new ArrayList<CategoryFileStateChange>();
//		//只记录TrainingFile的最终enabled信息
//		for(int i=0;i<stateChangeSplit.length;i++)
//		{
//			Query<CategoryFileStateChange> query = datastore.find(CategoryFileStateChange.class)
//					.field("pageNo").equal(currentPageNo).field("posNo").equal(i);
//			List<CategoryFileStateChange> list = query.asList();
//			boolean enabled = stateChangeSplit[i].equals("0")?false:true;
//			if(list!=null&&list.size()!=0)
//			{
//				datastore.updateFirst(query,
//						datastore.createUpdateOperations(CategoryFileStateChange.class)
//						.set("enabled", enabled));
//			}
//			else
//			{
//				CategoryFileStateChange stateChange = new CategoryFileStateChange();
//				stateChange.setPageNo(currentPageNo);
//				stateChange.setPosNo(i);
//				stateChange.setEnabled(enabled);
//				stateChangeList.add(stateChange);
//			}
//		}
//		datastore.save(stateChangeList);
//	}
	
	public void changeCategoryState(String type,boolean state){
		if(state==true)
		{
			int catSize=datastore.find(TrainingFile.class).field("type").equal(type)
					.field("enabled").equal(true).asList().size();
			datastore.findAndModify(
			datastore.createQuery(Category.class).field("name").equal(type),
			datastore.createUpdateOperations(Category.class).set("enabled", state)
			.set("trainingSetNum", catSize));
		}
		else
		{
			datastore.findAndModify(
			datastore.createQuery(Category.class).field("name").equal(type),
			datastore.createUpdateOperations(Category.class).set("enabled", state)
			.set("trainingSetNum", 0).set("classifiedFileNum", 0));
			datastore.findAndModify(
				datastore.createQuery(TrainingFile.class).field("type")
				.equal(type),datastore.createUpdateOperations(TrainingFile.class).set("enabled", true));
		}
	}
	
	public void syncFileStateChange(String submitedCategory,List<TrainingFile> TF41Cat
			,ArrayList<Boolean> fileState41Cat){
		System.out.println("fileState41Cat contains false?"+fileState41Cat.contains(false));
		for(int i=0;i<fileState41Cat.size();i++)
			TF41Cat.get(i).setEnabled(fileState41Cat.get(i));
		datastore.delete(datastore.createQuery(TrainingFile.class)
				.field("type").equal(submitedCategory).asList());
		datastore.save(TF41Cat);
	}
	
	public String uploadTrainingFile(List<String> dirs) {
		ArrayList<TrainingFile> trainingFiles = new ArrayList<TrainingFile>();
		ArrayList<File> localFiles = new ArrayList<File>();
		Map<String,Integer> typeDocNum =  new HashMap<String,Integer>();
		String result="",newCatString = "";
        for(int i=0;i<dirs.size();i++)
        {
        	File file = new File(dirs.get(i));
        	//不是目录->添加，是目录->广度遍历其中的所有文件
        	if(file.isDirectory())
        	{
        		Queue<File> fileQueue = new LinkedList<File>();
        		fileQueue.offer(file);
        		while(fileQueue.size()!=0)
        		{
        			File file1 = fileQueue.poll();
        			if(file1.isDirectory())
        			{
        				File[] file1s = file1.listFiles();
        				for(int j=0;j<file1s.length;j++)
        					fileQueue.offer(file1s[j]);
        			}
        			else
        				localFiles.add(file1);
        		}
        	}
        	else
        		localFiles.add(file);
        }
        
        for(File file:localFiles)
        {
        	String body="",tmp="";
        	try {
        		BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file),"UTF-8"));
        		while((tmp=reader.readLine())!=null)
        			body+=tmp;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String beforeSplit = file.getName().replace(".txt", ""); 
   	        String[] splited = beforeSplit.split("@");
   	        String type = null;
   	        String title = null;
   	        for(int k = 0; k<splited.length ; k++){
	    	  if(k == 0)
	    		type =splited[k];
	          if(k == splited.length-1)
	        	title =splited[k];
	      }
   	        
   	     if(typeDocNum.containsKey(type))
   	    	typeDocNum.put(type, typeDocNum.get(type)+1);
   	     else typeDocNum.put(type, 1);
   	    	
   	     trainingFiles.add(new TrainingFile(beforeSplit, title, type, body, null));
        }
        result+="增加训练集合："+trainingFiles.size()+",其中：";
        String[]typeDocNumKeySet = typeDocNum.keySet().toArray(new String[0]);
        for(int i=0;i<typeDocNumKeySet.length;i++)
        {
        	if(datastore.find(Category.class).field("name").equal(typeDocNumKeySet[i]).get()!=null)
        		{
        			Category cat = datastore.find(Category.class).field("name").equal(typeDocNumKeySet[i]).get();
        			datastore.findAndModify(datastore.find(Category.class).field("name").equal(typeDocNumKeySet[i])
        				, datastore.createUpdateOperations(Category.class).set("trainingSetNum", 
        						cat.getTrainingSetNum()+typeDocNum.get(typeDocNumKeySet[i])));
        			result+="|	"+typeDocNumKeySet[i]+":"+typeDocNum.get(typeDocNumKeySet[i]);
        		}
        	else 
        		{
        			Category cat = new Category(typeDocNumKeySet[i], typeDocNum.get(typeDocNumKeySet[i]),false);
        			datastore.save(cat);
        			result+="|	"+typeDocNumKeySet[i]+":"+typeDocNum.get(typeDocNumKeySet[i]);
        			if(newCatString=="")
        				newCatString+="“"+typeDocNumKeySet[i]+"”";
        			else newCatString+=",“"+typeDocNumKeySet[i]+"”";
        		}
        }
        datastore.save(trainingFiles);
        return result+"|"+"新增可用类别：|	"+newCatString;
	}

	public void removeClassificationResult()
	{
		datastore.delete(datastore.createQuery(ClassificationResult.class));
	}

	public void resetClassifiedFileNum()
	{
		datastore.update(datastore.find(Category.class).field("classifiedFileNum").
				greaterThanOrEq(0), datastore.createUpdateOperations(Category.class)
				.set("classifiedFileNum", 0));
	}
	
	public void enableCategory(String name)
	{
		datastore.update(datastore.find(Category.class).field("name").equal(name)
				, datastore.createUpdateOperations(Category.class).set("enabled", true));
	}
	
	public void disableCategory(String name)
	{
		datastore.update(datastore.find(Category.class).field("name").equal(name)
				, datastore.createUpdateOperations(Category.class).set("enabled", false));
	}
	
	public void classificationState_Init(){
		if(!mongo.getDB("genius").collectionExists("classificationStates")||mongo.getDB("genius").getCollection("classificationStates").count()==0)
			{
				HashMap<String,String> classificationStates = new HashMap<String, String>();
				classificationStates.put("trainState", "NOT_TRAINED");
				classificationStates.put("classifyState", "CLASSIFY_DISABLED");
				DBCollection col = mongo.getDB("genius").createCollection("classificationStates", null);
				BasicDBObject obj= new BasicDBObject(classificationStates);
				col.insert(obj);
			}
	}
	
	public Map<String,String> classificationState_getState()
	{
		Map<String,String> classificationStates = new HashMap<String, String>();
		DBCollection coll = mongo.getDB("genius").getCollection("classificationStates");
		DBCursor cur = coll.find();
		DBObject dbo = cur.toArray().get(0);
		classificationStates = dbo.toMap();
		classificationStates.remove("_id");
		return classificationStates;
	}
	
	public void classificationState_setState(String stateType,String state)
	{
		Map<String,String> classificationStates = new HashMap<String, String>();
		DBCollection coll = mongo.getDB("genius").getCollection("classificationStates");
		DBCursor cur = coll.find();
		DBObject dbo = cur.toArray().get(0);
		classificationStates = dbo.toMap();
		classificationStates.remove("_id");
		classificationStates.put(stateType, state);
		coll.drop();
		coll.insert(new BasicDBObject(classificationStates));
	}
	
	public void changeState(String stateType,String state)
	{
		HashMap<String,String> states = new HashMap<String, String>();
		states.put("1", "1");
		states.put("2", "2");
		datastore.save(states);
	}
	
	public void changecheck(String[] ids){
		Query<TrainingFile> q;
		for(int i=0;i<ids.length;i++){
			q=datastore.find(TrainingFile.class).filter("id ==", ids[i]);
			datastore.findAndModify(q, datastore.createUpdateOperations(TrainingFile.class)
							.set("enabled", true));
			
		}
	}
	
	public void plsa_training(){
		List<Category> cl=datastore.find(Category.class)
				.field("enabled").equal(true).asList();
		List<TrainingFile> tfl=new ArrayList<TrainingFile>();
		for(int i=0;i<cl.size();i++){
			tfl.addAll(datastore.find(TrainingFile.class)
					.field("type").equal(cl.get(i).getName())
					.field("enabled").equal(true).asList());
			
		}
		ArrayList<TermDoc> trainingSet=new ArrayList<TermDoc>();
		HashMap<String, Integer> typeSet=new HashMap<String, Integer>();
		try {
			Directory dir=new RAMDirectory();
			Analyzer analyzer = new IKAnalyzer();
			IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
					Version.LUCENE_CURRENT, analyzer));
			writer.deleteAll();
			Document doc = null;
			for(int i=0;i<tfl.size();i++){
				doc = new Document();
				doc.add(new Field("title", tfl.get(i).getTitle(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("type", tfl.get(i).getType(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("body", tfl.get(i).getBody(), Field.Store.NO,
						Field.Index.ANALYZED,
						Field.TermVector.WITH_POSITIONS_OFFSETS));
				writer.addDocument(doc);
			}
			writer.close();
			IndexReader reader = IndexReader.open(dir,false);
			TermDoc td = null;
			List<TermFreq> tfList = null;
			TermFreq tf = null;
			for (int i = 0; i < reader.numDocs(); i++) {
				td = new TermDoc();
				// 得到tf
				TermFreqVector trainingSetTermVector = reader
						.getTermFreqVector(i, "body");
				String[] termSet = trainingSetTermVector.getTerms();
				int[] termFreqSet = trainingSetTermVector.getTermFrequencies();

				tfList = new ArrayList<TermFreq>();
				for (int j = 0; j < termSet.length; j++) {
					tf = new TermFreq();
					tf.setTerm(termSet[j]);
					tf.setFreq(termFreqSet[j]);
					tfList.add(tf);
				}
				td.setTermFreq(tfList);
				td.setFileTitle(reader.document(i).get("title"));
				String tp = reader.document(i).get("type");
				td.setFileType(tp);

				trainingSet.add(td);
				if (typeSet.containsKey(tp) == false)
					typeSet.put(tp, 1);// hash<类型,文档数>
				else
					typeSet.put(tp, typeSet.get(tp) + 1);
			}
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PLSA plsa=new PLSA();
		plsa.training(trainingSet, typeSet, "10.1.0.171");
	}
	
	public static void main(String[] args) throws UnknownHostException, MongoException
	{
		
		CategoryDAOMongoImpl c = new CategoryDAOMongoImpl();
		c.init();
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo,"genius");
		
		HashMap<String,String> classificationStates = new HashMap<String, String>();
		classificationStates.put("trainState", "trained");
		classificationStates.put("classifyState", "classified");
		
		DBCollection col = mongo.getDB("genius").createCollection("typeSet", null);
		BasicDBObject obj= new BasicDBObject(classificationStates);
	    col.insert(obj);
	    
		Map<String, Integer> typeSet = new HashMap<String, Integer>();
		DBCollection coll = mongo.getDB("genius").getCollection("typeSet");
		
		DBCursor cur = coll.find();
		
		DBObject dbo = cur.toArray().get(0);
		typeSet = dbo.toMap();
		typeSet.remove("_id");
		
		}

}
