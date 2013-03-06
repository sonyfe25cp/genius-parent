package edu.bit.dlde.analysis.classification;
import java.io.*;  
import java.text.*;
import java.io.IOException;  

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;  
import org.apache.lucene.analysis.cn.smart.*;
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
	 * @author JiangXiaoTian
	 * 不要提交LuceneProjectNec目录下的indexDir2C、DataDir2C和result.txt
	 *
	 */
    //private static ArrayList<FileWithTermVector> trainingSet;
    //private static ArrayList<FileWithTermVector> testSet;
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 //for(int VectorNum = 1100;VectorNum<=2000;VectorNum+=100)
		  // {
	    int VectorNum = 1200;
		int kValue = 40;
		int dfLowerLimit = 1;
		int typeUpperLimit = 8;
		
		//FileWriter fout = new FileWriter(".\\LuceneProjectNec\\result.txt", true);
		//fout.write(new SimpleDateFormat("HH:mm:ss").format(new Date())+"\r\n");
		
		String trainingSetDir = ".\\Classification_Corpus\\trainingSet";
		//String testSetDir = ".\\Classification_Corpus\\testSet";
		
		File file1 = new File(".\\Classification_Index\\trainingSetIndex");
		if(file1.exists()==false)
			file1.mkdirs();
		String trainingSetIndex = ".\\Classification_Index\\trainingSetIndex";
//		File file2 = new File(".\\Classification_Index\\testSetIndex");
//		if(file2.exists()==false)
//			file2.mkdirs();
//		String testSetIndex = ".\\Classification_Index\\testSetIndex";
		
		
		HashMap<String,Integer> typeSet;
		typeSet =new HashMap<String,Integer>();
		
		//用Lucene建立trainingSet的索引
		System.out.println("Building Index...");
		BuildReader(trainingSetIndex, trainingSetDir);
		System.out.println("Complete!");
		
		//从Lucene中读出索引，建立TrainingSet
		Directory dir1 = FSDirectory.open(new File(trainingSetIndex));	
		IndexReader trainingSetReader = IndexReader.open(dir1);
		ArrayList<FileWithTermVector> trainingSet = new ArrayList<FileWithTermVector>();
		
	   System.out.println("程序开始："+formatter.format(new Date()));
	
	   //预处理1：解除Lucene依赖，获得tf
	   TrainingSetIndependence(trainingSetReader,trainingSet,typeSet);
	   int docNum = trainingSet.size();
	   
	   //预处理2：训练得到dfSet,此时里面存储的是df
	   ArrayList<generalizedDF> dfSet = TrainDFSet(trainingSet);
       System.out.println("DFSIZE:"+dfSet.size());
       
	   //预处理3：筛选出出现次数>2的词且typeCount<3/5*类别总数的词
       MiddleLeft(dfSet,dfLowerLimit,typeUpperLimit);
	   System.out.println("DFSIZE-Reduced:"+dfSet.size());

	   //预处理4：进行Chi-Square特征选择
	   ChiSquareReduce(dfSet,typeSet,VectorNum,docNum);
	   
	   //预处理5：对整个trainingSet筛出特征值向量,且加入idf参数,使得剩下的dfSet中df变idf
	   ReduceTrainingSet_PutIDF(trainingSet,dfSet);
	
	   System.out.println("存储至mongoDB:开始");
	   Mongo mongo = new Mongo("127.0.0.1", 27017);
	   Morphia morphia = new Morphia();
	   Datastore ds = morphia.createDatastore(mongo, "KNN");
	   ds.save(trainingSet);
	   ds.save(dfSet);
	   
	   DBCollection coll = mongo.getDB("KNN").createCollection("typeSet", null);
	   DBObject obj= new BasicDBObject(typeSet);
	   coll.insert(obj);
	   System.out.println("存储至mongoDB:完成！");
		}
	
	public static double CalculateDistance(FileWithTermVector testFile,
			FileWithTermVector trainingFile,ArrayList<generalizedDF> dfSet) 
					throws IOException
	{
		double trainingFileMode = 0;
		double testFileMode = 0;
		double  up = 0;
		for(int i=0;i<testFile.termVectorSet.size();i++)
		{
			for(int j=0;j<trainingFile.termVectorSet.size();j++)
			{
				if(trainingFile.termVectorSet.get(j).word.equals
						(testFile.termVectorSet.get(i).word))
				{
					//获得交叉项的tf,idf
					int tf1 = testFile.termVectorSet.get(i).tf;
					int tf2 = trainingFile.termVectorSet.get(j).tf;
					double idf = trainingFile.termVectorSet.get(j).idf;
					up+=tf1*tf2*idf*idf;
			    }
		    }
		}
		//trainingFile的模
		for(int i=0;i<trainingFile.termVectorSet.size();i++)
					trainingFileMode+=trainingFile.termVectorSet.get(i).idf
					                  *trainingFile.termVectorSet.get(i).tf
					                  *trainingFile.termVectorSet.get(i).idf
					                  *trainingFile.termVectorSet.get(i).tf;
		trainingFileMode = Math.sqrt(trainingFileMode);
		
		//testFile的模
		for(int i=0;i<testFile.termVectorSet.size();i++)
			 {
			     double idf=0;
			     for(int j=0;j<dfSet.size();j++)
				   if(dfSet.get(j).word.equals(testFile.termVectorSet.get(i).word))
				      idf=dfSet.get(j).df;
			     testFileMode+=idf*idf*testFile.termVectorSet.get(i).tf*
			    		       idf*idf*testFile.termVectorSet.get(i).tf;
	        }
		testFileMode = Math.sqrt(testFileMode);
		return Math.acos(up/(trainingFileMode*testFileMode));
	    
	}
	
	public static void BuildReader(String indexDir,String dataDir) throws IOException{
        Directory dir = FSDirectory.open(new File(indexDir));
        
        Analyzer analyzer = new IKAnalyzer();
        IndexWriter writer = new IndexWriter(dir, analyzer, true , IndexWriter.MaxFieldLength.UNLIMITED);
        
        File[] files = new File(dataDir).listFiles();
        for(File f: files)
        {
        	if(!f.isDirectory()&&!f.isHidden()&&f.exists()&&f.canRead()
        			&&(f.getName().toLowerCase().endsWith(".txt")))
        	  {
        	      Document doc = new Document();
        	      Field d = new Field("subject","Whatever",Field.Store.NO, 
        	    		  Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
        	      
        	      InputStreamReader isr = new InputStreamReader(new FileInputStream(f),"UTF-8");

        	      d.setValue(isr);
        	      //d.setValue(new FileReader(f));
        	      doc.add(d);
        	      String beforeS = f.getName().replace(".txt", ""); 
        	      String[] s = beforeS.split("@");
        	      for(int k = 0; k<s.length ; k++)
        	      {
        	    	  if(k == 0)
        	    		  doc.add(new Field("type", s[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
        	          if(k == s.length-1)
        	        	  doc.add(new Field("title", s[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
        	      }
        	      doc.add(new Field("fullpath", f.getCanonicalPath(),Field.Store.YES, Field.Index.NOT_ANALYZED));
        	      writer.addDocument(doc);
        	  }
        }
        writer.close();
	}
  
	public static SDPair ChiSquare(ArrayList<generalizedDF> dfSet,
			HashMap<String,Integer> typeSet,int dfSetNum,String type,int docNum)
	{
		int A,B,C,D;
		double chiSquare,up,down;
		int count=0,iCount=0;
		for(int i=0;i<dfSet.get(dfSetNum).map2File.size();i++)
		{
			if(dfSet.get(dfSetNum).map2File.get(i).fileType.equals(type))
				count++;
			else iCount++;
		}
		A=count;
		B=iCount;
		C=typeSet.get(type)-A;
		D=docNum-A-B-C;
		up = docNum*(A*D-B*C)*(A*D-B*C);
		down = (A+C)*(A+B)*(B+D)*(C+D);
		chiSquare=up/down;
		if(chiSquare<0)
		{
			System.out.println("!!!|A="+A+"|B="+B+"|C="+C+"|D="+D
					+"|up="+up+"|down="+down+"|chiSquare="+chiSquare+"!trainingSet.size()="+docNum);
			System.out.println("trainingSet.size()="+docNum+
					"(A*D-B*C)*(A*D-B*C)="+(A*D-B*C)*(A*D-B*C));
		}
		return new SDPair(dfSet.get(dfSetNum).word,chiSquare);
	}

   public static void TrainingSetIndependence(IndexReader reader,
		   ArrayList<FileWithTermVector> set,HashMap<String,Integer> typeSet)  throws IOException
   {
	   System.out.println("预处理1：解除Lecene依赖，获得tf---（训练集）开始"+formatter.format(new Date()));
		  for(int i=0;i<reader.numDocs();i++)
		  {
			  String tp = reader.document(i).getField("type").stringValue();
			  set.add(new FileWithTermVector(
					  reader.document(i).getField("title").stringValue(),tp,
					  reader.document(i).getField("fullpath").stringValue()));
			  if(typeSet.containsKey(tp)==false)
				      typeSet.put(tp, 1);
			  else  typeSet.put(tp, typeSet.get(tp)+1);
			  TermFreqVector trainingSetTermVector = reader.getTermFreqVector(i,"subject");
				  String[] termSet = trainingSetTermVector.getTerms();
				  int[] termFreqSet = trainingSetTermVector.getTermFrequencies();
				  for(int j=0;j<termSet.length;j++)
					  set.get(i).insertTermVector(termSet[j], termFreqSet[j], "VACANCY"); 
		  }
		  System.out.println("预处理1：解除Lecene依赖，获得tf---（训练集）结束"+formatter.format(new Date()));
   }
   
   public static ArrayList<generalizedDF> TrainDFSet (ArrayList<FileWithTermVector> setToTrain)
   {
	   ArrayList<generalizedDF> dfSet = new  ArrayList<generalizedDF>();
	   System.out.println("预处理2：训练dfSet---开始"+formatter.format(new Date()));
		for(int i=0;i<setToTrain.size();i++)
		{
			System.out.println("training:"+setToTrain.get(i).title+setToTrain.get(i).fileType);
			 for(int j=0;j<setToTrain.get(i).termVectorSet.size();j++)
			 {
				 boolean isIn = false;
				 for(int k=0;k<dfSet.size();k++)
			        	if(dfSet.get(k).word.equals(setToTrain.get(i).termVectorSet.get(j).word))
			    	    	{
			        		    dfSet.get(k).df++;
			        		    dfSet.get(k).insertMap(setToTrain.get(i).title,setToTrain.get(i).fileType,i);
			        		    isIn = true;
			        		    break;
			        		}
				 if(!isIn)
			     	{
					 generalizedDF tmp = new generalizedDF(setToTrain.get(i).termVectorSet.get(j).word, 1);
					 tmp.insertMap(setToTrain.get(i).title,setToTrain.get(i).fileType,i);
					 dfSet.add(tmp);
			     	}
			 }
		}	
	  System.out.println("预处理2：训练dfSet---结束"+formatter.format(new Date()));
	  return dfSet;
   }
   
   public static void MiddleLeft(ArrayList<generalizedDF> dfSet,int dfLowerLimit,int typeUpperLimit)
   {
	   System.out.println("预处理3：筛选---开始"+formatter.format(new Date()));
	 		for(int i=0;i<dfSet.size();i++)
	 		{
	 			if(dfSet.get(i).df<=dfLowerLimit||dfSet.get(i).getTypeCount()>=typeUpperLimit)
	 				dfSet.remove(i--);
	 		}
   }
   
   public static void ChiSquareReduce(ArrayList<generalizedDF> dfSet,
		   HashMap<String,Integer> typeSet,int VectorNum,int docNum)
   {
	   System.out.println("预处理4：进行Chi-Square特征选择---开始"+formatter.format(new Date()));
		for(int i=0;i<typeSet.size();i++)
		{
			String type = typeSet.keySet().toArray(new String[0])[i];
			ArrayList<SDPair> words = new ArrayList<SDPair>();
			for(int j=0;j<dfSet.size();j++)
				words.add(ChiSquare(dfSet,typeSet,j,type,docNum));
			Collections.sort(words);
			for(int j=words.size()-1;j>words.size()-VectorNum-1;j--)
				for(int k=0;k<dfSet.size();k++)
					if(words.get(j).str.equals(dfSet.get(k).word))
						dfSet.get(k).delete = false;
		}
		//应用ChiSquare对于多类的处理结果进行规约
		for(int j=0;j<dfSet.size();j++)
			if(dfSet.get(j).delete==true)
				dfSet.remove(j--);
		System.out.println("预处理4：进行Chi-Square特征选择(idf:"+dfSet.size()+")---结束"+formatter.format(new Date()));
   }
 
   public static void ReduceTrainingSet_PutIDF(ArrayList<FileWithTermVector> trainingSet,ArrayList<generalizedDF> dfSet)
   {
	   for(int i=0;i<trainingSet.size();i++)
		{
		    System.out.print(trainingSet.get(i).title+trainingSet.get(i).fileType+":");
		    System.out.print(trainingSet.get(i).termVectorSet.size()+"->");
			for(int j=0;j<trainingSet.get(i).termVectorSet.size();j++)
			{
				boolean isIn = false;
				for(int k=0;k<dfSet.size();k++)
					if(dfSet.get(k).word.equals(trainingSet.get(i).termVectorSet.get(j).word))
					{
						isIn = true;
						 dfSet.get(k).df = trainingSet.get(i).termVectorSet.get(j).idf = 
								Math.log10(trainingSet.size()/dfSet.get(k).df);
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
}
