package com.genius.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


import edu.bit.dlde.analysis.classification_jxt_forweb.ClassificationResult;
import edu.bit.dlde.analysis.classification_jxt_forweb.FileProperty;
import edu.bit.dlde.analysis.plsa.DocFreq;
import edu.bit.dlde.analysis.plsa.DocType;
import edu.bit.dlde.analysis.plsa.PLSA;
import edu.bit.dlde.analysis.plsa.Pw_z;
import edu.bit.dlde.analysis.plsa.Pz;
import edu.bit.dlde.analysis.plsa.TermDoc;
import edu.bit.dlde.analysis.plsa.TermFreq;
import edu.bit.dlde.analysis.plsa.Type;

/**
 *	@author zhangchangmin
 **/
public class PlsaDAOMongoImpl extends MongoDAOBase implements IPlsaDAO {
	
	CategoryDAOMongoImpl catDAO=null;
	PLSA plsa=null;
	
	public PlsaDAOMongoImpl(){
		init();
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
//					morphia.mapPackageFromClass(DocType.class);
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
	public List<DocType> loadDocs(int off,int num){
		return datastore.find(DocType.class).offset(off)
				.limit(num).asList();
	}
	@Override
	public List<DocType> loadDocsByType(String type){
		return datastore.find(DocType.class).filter("docType ==", type).asList();
	}
	public List<DocType> loadDocsByType(String type,int off,int num){
		return datastore.find(DocType.class).filter("docType ==", type)
				.offset(off).limit(num).asList();
	}
	@Override
	public HashMap<String,Integer> typeList(int off,int num){
		HashMap<String,Integer> types=new HashMap<String,Integer>();
		catDAO=new CategoryDAOMongoImpl();
		ArrayList<Category> categories=(ArrayList<Category>)catDAO.loadCategoriesByStatus(off, num);
		for(int i=0;i<categories.size();i++){
			int n=datastore.find(DocType.class).filter("docType ==", categories.get(i).getName()).asList().size();
			types.put(categories.get(i).getName(), n);
		}
		return types;
	}
	public HashMap<String,Integer> typeList(){
		HashMap<String,Integer> types=new HashMap<String,Integer>();
		catDAO=new CategoryDAOMongoImpl();
		ArrayList<Category> categories=(ArrayList<Category>)catDAO.loadCategoriesByStatus(true);
		for(int i=0;i<categories.size();i++){
			int n=datastore.find(DocType.class).filter("docType ==", categories.get(i).getName()).asList().size();
			types.put(categories.get(i).getName(), n);
		}	
		return types;
	}
	public void classify(){
		plsa=new PLSA();
		List<DocType> dtList=plsa.classify();
		datastore.delete(datastore.createQuery(DocType.class));
		datastore.save(dtList);
		
		
	}
	public List<ClassificationResult> classify_plsa(List<String> localIds,List<String> pageIds){		
		ArrayList<TermDoc> testSet=new ArrayList<TermDoc>();
		List<DocFreq> dfSet1=datastore.find(DocFreq.class).asList();
		ArrayList<DocFreq> dfSet=new ArrayList<DocFreq>();
		for(int i=0;i<dfSet1.size();i++){
			dfSet.add(dfSet1.get(i));
		}
		List<ClassificationResult> classificationResults=new ArrayList<ClassificationResult>();
		try {
			Directory dir=new RAMDirectory();
			Analyzer analyzer = new IKAnalyzer();
			IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
					Version.LUCENE_CURRENT, analyzer));
			writer.deleteAll();
			if(localIds!=null&&localIds.size()!=0)
	        {
		        ArrayList<File> localFiles = new ArrayList<File>();
		        for(int i=0;i<localIds.size();i++)
		        {
		        	File file = new File(localIds.get(i));
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
		        
		        for(File f: localFiles)
		          {
		        	if(!f.isDirectory()&&!f.isHidden()&&f.exists()&&f.canRead()
		        			&&(f.getName().toLowerCase().endsWith(".txt")))
		        	{
		        	      Document doc = new Document();
		        	      //id
		        	      doc.add(new Field("id",f.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		        	      //body
		        	      Field bodyField = new Field("body","Whatever",Field.Store.NO, 
		        	    		  Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
		        	      bodyField.setValue(new InputStreamReader(new FileInputStream(f),"UTF-8"));
		        	      doc.add(bodyField);
		        	      //title&type
		        	      String beforeSplit = f.getName().replace(".txt", ""); 
		        	      String[] splited = beforeSplit.split("@");
		        	      for(int k = 0; k<splited.length ; k++){
		        	    	  if(k == 0)
		        	    		  doc.add(new Field("type", splited[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
		        	          if(k == splited.length-1)
		        	        	  doc.add(new Field("title", splited[k],Field.Store.YES,Field.Index.ANALYZED));
		        	          if((k == splited.length-2)&&k>0)
		        	        	  doc.add(new Field("author", splited[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
		        	      }
		        	      writer.addDocument(doc);
		        	  }
		          }
	        }
	        writer.close();
	        IndexReader reader = IndexReader.open(dir,false);
			TermDoc td = null;
			List<TermFreq> tfList = null;
			TermFreq tf = null;
			for (int i = 0; i < reader.numDocs(); i++) {
				td = new TermDoc();
				// 得到tf
				TermFreqVector testSetTermVector = reader
						.getTermFreqVector(i, "body");
				String[] termSet = testSetTermVector.getTerms();
				int[] termFreqSet = testSetTermVector.getTermFrequencies();

				tfList = new ArrayList<TermFreq>();
				for (int j = 0; j < termSet.length; j++) {
					tf = new TermFreq();
					tf.setTerm(termSet[j]);
					tf.setFreq(termFreqSet[j]);
					tfList.add(tf);
				}
				td.setTermFreq(tfList);
				td.setFileTitle(reader.document(i).get("title"));
				td.setFileType(reader.document(i).get("type"));
				td.setId(reader.document(i).get("id"));
				testSet.add(td);
			}
			reader.close();
			if(pageIds!=null&&pageIds.size()!=0){
				Directory dir2 = FSDirectory.open(new File(System.getProperty("user.home")
						+"\\data\\index\\genius2"));
				IndexReader classifySetReader2 = IndexReader.open(dir2);
				for (int i = 0; i < classifySetReader2.numDocs(); i++){ 
					if(pageIds.contains(classifySetReader2.document(i).get("id"))){
						td = new TermDoc();
						TermFreqVector pageTermVector = reader
								.getTermFreqVector(i, "body");
						String[] termSet = pageTermVector.getTerms();
						int[] termFreqSet = pageTermVector.getTermFrequencies();

						tfList = new ArrayList<TermFreq>();
						for (int j = 0; j < termSet.length; j++) {
							tf = new TermFreq();
							tf.setTerm(termSet[j]);
							tf.setFreq(termFreqSet[j]);
							tfList.add(tf);
						}
						String title = classifySetReader2.document(i).get("title")!=null?
								classifySetReader2.document(i).get("title"):null;
						String author = classifySetReader2.document(i).get("author")!=null?
								classifySetReader2.document(i).get("author"):null;
						String type = classifySetReader2.document(i).get("type")!=null?
								classifySetReader2.document(i).get("type"):null;
						td.setId(classifySetReader2.document(i).get("id"));
						td.setFileTitle(title);
						td.setFileType(type);
						td.setFileAuthor(author);
						td.setTermFreq(tfList);
						testSet.add(td);
						break;
					}
				}
				classifySetReader2.close();
			}
			List<Pz> pzl=datastore.find(Pz.class).asList();
			List<Pw_z> pwzl=datastore.find(Pw_z.class).asList();
			List<Type> tl=datastore.find(Type.class).asList();
			PLSA plsa=new PLSA();
			plsa.ReduceSet(testSet, dfSet);
			List<DocType> dtList=plsa.doplsa_test_new(testSet, dfSet.size(), 
					tl.size(), pzl.get(0).getLs(), pwzl.get(0).getLs(), tl);
			
			for(int i=0;i<dtList.size();i++){
				String id=dtList.get(i).getDocPath();
				String type=dtList.get(i).getDocType();
				String title=dtList.get(i).getDocTitle();
				ClassificationResult classificationResult=new ClassificationResult(id,type,"",title);
				classificationResults.add(classificationResult);
			}
			
//			datastore.delete(datastore.createQuery(DocType.class));
//			datastore.save(dtList);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classificationResults;
		
	}
	public CategoryDAOMongoImpl getCatDAO() {
		return catDAO;
	}
	public void setCatDAO(CategoryDAOMongoImpl catDAO) {
		this.catDAO = catDAO;
	}

	public PLSA getPlsa() {
		return plsa;
	}

	public void setPlsa(PLSA plsa) {
		this.plsa = plsa;
	}

	
}
