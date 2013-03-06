package edu.bit.dlde.analysis.classification_jxt_forweb;
import java.io.*;  
import java.text.*;
import java.io.IOException;  

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;  
import org.bson.BSONObject;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import java.util.*;

public class Train {

	/**
	 * @author xiaotian
	 *
	 */
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	//public static void main(String[] args) throws IOException {
	public static void trainModel(Datastore ds) throws IOException {
		// TODO Auto-generated method stub
		 //for(int VectorNum = 1100;VectorNum<=2000;VectorNum+=100)
		  // {
	    int VectorNum = 1200;
		int dfLowerLimit = 1;
		int typeUpperLimit = (int)Math.rint(ds.find(Category.class).field("enabled").equal(true)
				.asList().size()*0.8);
		if(typeUpperLimit<3)
			typeUpperLimit=3;
		File file1 = new File("./Classification_Index/trainingSetIndex");
		if(file1.exists()==false)
			file1.mkdirs();
		String trainingSetIndexDir = "./Classification_Index/trainingSetIndex";
		List<Category> categories = ds.find(Category.class).field("enabled").equal(true).asList();
		
		//预处理1：用lucene分词并得到df,然后从Lucene中读出索引，建立TrainingSet
	    ArrayList<FileProperty> trainingSet = 
	    		BuildTrainingSet(trainingSetIndexDir,categories,ds);
	    
	   //预处理2：训练得到wordSet,此时里面存储的是df
	   ArrayList<WordProperty> wordSet = BuildWordSet(trainingSet);
       
	   //预处理3：筛选出出现次数>2的词且typeCount<3/5*类别总数的词
       MiddleLeft(wordSet,dfLowerLimit,typeUpperLimit);

	   //预处理4：进行Chi-Square特征选择
	   ChiSquareReduce(wordSet,categories,VectorNum,trainingSet.size());
	   
	   //预处理5：对整个trainingSet筛出特征值向量,且加入idf参数,使得剩下的wordSet中df变idf
	   ReduceTrainingSet_GetIdf(trainingSet,wordSet);

	   ds.findAndDelete(ds.createQuery(FileProperty.class));
	   ds.findAndDelete(ds.createQuery(WordProperty.class));
	   System.out.print("存储至mongoDB......");
	   ds.save(trainingSet);
	   ds.save(wordSet);
	   System.out.println("完成！");
	}
	
//	public static ArrayList<FileProperty> BuildTrainingSet(String trainingSetIndexDir,
//			String trainingSetDataDir,HashMap<String,Integer> typeSet) throws IOException
//		{
//        Directory dir = FSDirectory.open(new File(trainingSetIndexDir));
//        //1 得到lucene索引
//        System.out.print("Building Index......");
//        Analyzer analyzer = new IKAnalyzer();
//        IndexWriter writer = new IndexWriter(dir, analyzer, true , IndexWriter.MaxFieldLength.UNLIMITED);
//        
//        File[] files = new File(trainingSetDataDir).listFiles();
//        for(File f: files){
//        	if(!f.isDirectory()&&!f.isHidden()&&f.exists()&&f.canRead()
//        			&&(f.getName().toLowerCase().endsWith(".txt")))
//        	{
//        	      Document doc = new Document();
//        	      //id
//        	      doc.add(new Field("id",f.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
//        	      //body
//        	      Field bodyField = new Field("body","Whatever",Field.Store.NO, 
//        	    		  Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
//        	      bodyField.setValue(new InputStreamReader(new FileInputStream(f),"UTF-8"));
//        	      doc.add(bodyField);
//        	      
//        	      //title&type
//        	      String beforeSplit = f.getName().replace(".txt", ""); 
//        	      String[] splited = beforeSplit.split("@");
//        	      for(int k = 0; k<splited.length ; k++){
//        	    	  if(k == 0)
//        	    		  doc.add(new Field("type", splited[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
//        	          if(k == splited.length-1)
//        	        	  doc.add(new Field("title", splited[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
//        	      }
//        	      writer.addDocument(doc);
//        	  }
//        }
//        writer.close();
//        System.out.println("完成!");
//        
//        //2.从Lucene中读出索引，建立TrainingSet
//        System.out.print("Building TrainingSet...");
//		Directory dir1 = FSDirectory.open(new File(trainingSetIndexDir));	
//		IndexReader trainingSetReader = IndexReader.open(dir1);
//		ArrayList<FileProperty> trainingSet = new ArrayList<FileProperty>();
//		
//		  for(int i=0;i<trainingSetReader.numDocs();i++)
//		  {
//			  String type = trainingSetReader.document(i).getField("type").stringValue();
//			  trainingSet.add(new FileProperty(trainingSetReader.document(i).getField("id").stringValue(),
//					  type,Source.LOCAL_FILE,trainingSetReader.document(i).getField("title").stringValue()));
//			  if(typeSet.containsKey(type)==false)
//				      typeSet.put(type, 1);
//			  else  typeSet.put(type, typeSet.get(type)+1);
//			  TermFreqVector trainingSetTermVector = trainingSetReader.getTermFreqVector(i,"body");
//			  String[] termSet = trainingSetTermVector.getTerms();
//			  int[] termFreqSet = trainingSetTermVector.getTermFrequencies();
//			  for(int j=0;j<termSet.length;j++)
//				  trainingSet.get(i).AddTermVector(termSet[j], termFreqSet[j], "VACANCY"); 
//		  }
//		System.out.println("完成!");
//		return trainingSet;
//	}
	
	public static ArrayList<FileProperty> BuildTrainingSet(String trainingSetIndexDir,
			List<Category> categories,Datastore ds) throws IOException{
	    Directory dir = FSDirectory.open(new File(trainingSetIndexDir));
		List<TrainingFile> trainingFiles = ds.find(TrainingFile.class).field("enabled").equal(true).asList();
        //1 得到lucene索引
        System.out.print("Building Index......");
        Analyzer analyzer = new IKAnalyzer();
        IndexWriter writer = new IndexWriter(dir, analyzer, true , IndexWriter.MaxFieldLength.UNLIMITED);
        for(Category category:categories)
        	for(TrainingFile trainingFile:trainingFiles)
        		if(category.isEnabled()&&category.getName().equals(trainingFile.getType()))
        	{
    			Document doc = new Document();
    			doc.add(new Field("id",trainingFile.getId(),Field.Store.YES,Field.Index.NOT_ANALYZED));
    			doc.add(new Field("body",trainingFile.getBody(),Field.Store.NO, 
      	    		  Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
    			if(trainingFile.getTitle()!=null)
    				doc.add(new Field("title",trainingFile.getTitle(),Field.Store.YES,Field.Index.ANALYZED));
    			doc.add(new Field("type", trainingFile.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
    			if(trainingFile.getAuthor()!=null)
    				doc.add(new Field("author",trainingFile.getAuthor(),Field.Store.YES,Field.Index.NOT_ANALYZED));
    			writer.addDocument(doc);
        	}
        writer.close();
        System.out.println("完成!");
        
        //2.从Lucene中读出索引，建立TrainingSet
        System.out.print("Building TrainingSet...");
		Directory dir1 = FSDirectory.open(new File(trainingSetIndexDir));	
		IndexReader trainingSetReader = IndexReader.open(dir1);
		ArrayList<FileProperty> trainingSet = new ArrayList<FileProperty>();
		
		  for(int i=0;i<trainingSetReader.numDocs();i++)
		  {
			  String author=null;
			  if(trainingSetReader.document(i).getField("author")!=null)
				  author=trainingSetReader.document(i).getField("author").stringValue();
			  String title=null;
			  if(trainingSetReader.document(i).getField("title")!=null)
				  title=trainingSetReader.document(i).getField("title").stringValue();
			  String type=null;
			  if(trainingSetReader.document(i).getField("type")!=null)
				  type=trainingSetReader.document(i).getField("type").stringValue();
			  
			  trainingSet.add(new FileProperty(
					  trainingSetReader.document(i).getField("id").stringValue(),
					  type,author,title));
			  TermFreqVector trainingSetTermVector = trainingSetReader.getTermFreqVector(i,"body");
			  System.out.println("trainingSetTermVector.size="+trainingSetTermVector.size());
			  String[] termSet = trainingSetTermVector.getTerms();
			  int[] termFreqSet = trainingSetTermVector.getTermFrequencies();
			  for(int j=0;j<termSet.length;j++)
				  trainingSet.get(i).AddTermVector(termSet[j], termFreqSet[j], "VACANCY"); 
		  }
		System.out.println("完成!");
		return trainingSet;
	   }
  
	public static ArrayList<WordProperty> BuildWordSet (ArrayList<FileProperty> trainingSet){
		   ArrayList<WordProperty> wordSet = new  ArrayList<WordProperty>();
		   System.out.println("预处理2：训练wordSet---开始"+formatter.format(new Date()));
			for(int i=0;i<trainingSet.size();i++){
				System.out.println("training:"+trainingSet.get(i).title+trainingSet.get(i).type);
				 for(int j=0;j<trainingSet.get(i).getTermVectorSet().size();j++){
					 boolean isIn = false;
					 for(int k=0;k<wordSet.size();k++)
				        	if(wordSet.get(k).getWord().equals(trainingSet.get(i).termVectorSet.get(j).word)){
				        		    wordSet.get(k).df++;
				        		    wordSet.get(k).insertMap(trainingSet.get(i).title,trainingSet.get(i).type,i);
				        		    isIn = true;
				        		    break;
				        		}
					 if(!isIn){
						 WordProperty tmp = new WordProperty(trainingSet.get(i).termVectorSet.get(j).getWord(), 1);
						 tmp.insertMap(trainingSet.get(i).title,trainingSet.get(i).type,i);
						 wordSet.add(tmp);
				     	}
				 }
			}	
		  System.out.println("预处理2：训练wordSet---完成"+formatter.format(new Date()));
		   System.out.println("wordSetSize:"+wordSet.size());
		  return wordSet;
	   }

	public static void MiddleLeft(ArrayList<WordProperty> wordSet,int dfLowerLimit,int typeUpperLimit){
		   System.out.println("预处理3：MiddleLeft---开始"+formatter.format(new Date()));
	 	   for(int i=0;i<wordSet.size();i++){
	 		  if(wordSet.get(i).df<=dfLowerLimit||wordSet.get(i).getTypeCount()>=typeUpperLimit)
	 			 wordSet.remove(i--);
	 		}
	 	  System.out.println("预处理3：MiddleLeft---完成"+formatter.format(new Date()));
		   System.out.println("wordSetSize:"+wordSet.size());
	   }
	
	public static void ChiSquareReduce(ArrayList<WordProperty> wordSet,
			List<Category> categories,int VectorNum,int docNum){
		   System.out.println("预处理4：进行Chi-Square特征选择---开始"+formatter.format(new Date()));
		   
			for(int i=0;i<categories.size();i++){
				ArrayList<SDPair> words = new ArrayList<SDPair>();
				for(int j=0;j<wordSet.size();j++)
					words.add(ChiSquare(wordSet,categories,j,i,docNum));
				Collections.sort(words);
				for(int j=words.size()-1;j>words.size()-VectorNum-1;j--)
					for(int k=0;k<wordSet.size();k++)
						if(words.get(j).str.equals(wordSet.get(k).word))
							wordSet.get(k).delete = false;
			}
			//应用ChiSquare对于多类的处理结果进行规约
		   for(int j=0;j<wordSet.size();j++)
			   if(wordSet.get(j).delete==true)
				   wordSet.remove(j--);
		   System.out.println("预处理4：进行Chi-Square特征选择---完成"+formatter.format(new Date()));
		   System.out.println("wordSetSize:"+wordSet.size());
	   }
	
	public static SDPair ChiSquare(ArrayList<WordProperty> wordSet,
			List<Category> categories,int wordSetNum,int typePos,int docsNum){
		int A,B,C,D;
		double chiSquare,up,down;
		int count=0,iCount=0;
		for(int i=0;i<wordSet.get(wordSetNum).map2File.size();i++){
			if(wordSet.get(wordSetNum).map2File.get(i).fileType.equals(
					categories.get(typePos).getName()))
				count++;
			else iCount++;
		}
		A=count;
		B=iCount;
		C=categories.get(typePos).getTrainingSetNum()-A;
		D=docsNum-A-B-C;
		up = docsNum*(A*D-B*C)*(A*D-B*C);
		down = (A+C)*(A+B)*(B+D)*(C+D);
		chiSquare=up/down;
		if(chiSquare<0){
			System.out.println("!!!|A="+A+"|B="+B+"|C="+C+"|D="+D
			    +"|up="+up+"|down="+down+"|chiSquare="+chiSquare+"!trainingSet.size()="+docsNum);
			System.out.println("trainingSet.size()="+docsNum+"(A*D-B*C)*(A*D-B*C)="+(A*D-B*C)*(A*D-B*C));
		}
		return new SDPair(wordSet.get(wordSetNum).word,chiSquare);
	}
 
   public static void ReduceTrainingSet_GetIdf(ArrayList<FileProperty> trainingSet,ArrayList<WordProperty> wordSet){
	   for(int i=0;i<trainingSet.size();i++){
		    System.out.print(trainingSet.get(i).title+trainingSet.get(i).type+":");
		    System.out.print(trainingSet.get(i).termVectorSet.size()+"->");
			for(int j=0;j<trainingSet.get(i).termVectorSet.size();j++){
				boolean isIn = false;
				for(int k=0;k<wordSet.size();k++)
					if(wordSet.get(k).word.equals(trainingSet.get(i).termVectorSet.get(j).word)){
						isIn = true;
						wordSet.get(k).idf = trainingSet.get(i).termVectorSet.get(j).idf = 
								Math.log10(trainingSet.size()/wordSet.get(k).df);
						break;
					}
				if(isIn==false)
					trainingSet.get(i).termVectorSet.remove(j--);
			}
			System.out.println(trainingSet.get(i).termVectorSet.size());
			System.out.flush();
		}
		System.out.println("预处理5：筛trainingSet---结束"+formatter.format(new Date()));
   }
   public static void main(String[] args) throws IOException
   {
	    Mongo mongo = new Mongo("127.0.0.1",27017);
	    Morphia morphia = new Morphia();
	    Datastore ds = morphia.createDatastore(mongo,"genius");
	    trainModel(ds);
   }
}
