package edu.bit.dlde.analysis.classification;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class KNN {

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
		
	    int VectorNum = 1200;
		int kValue = 40;
		int dfLowerLimit = 1;
		int typeUpperLimit = 8;
		
		//FileWriter fout = new FileWriter(".\\LuceneProjectNec\\result.txt", true);
		//fout.write(new SimpleDateFormat("HH:mm:ss").format(new Date())+"\r\n");
		
		String indexDir = ".\\LuceneProjectNec\\indexDir";
		String dataDir = ".\\LuceneProjectNec\\dataDir";
		
		File file1 = new File(".\\LuceneProjectNec\\indexDir2C");
		if(file1.exists()==false)
			file1.mkdir();
		String indexDir2C = ".\\LuceneProjectNec\\indexDir2C";
		File file2 = new File(".\\LuceneProjectNec\\DataDir2C");
		if(file2.exists()==false)
			file2.mkdir();
		String dataDir2C = ".\\LuceneProjectNec\\DataDir2C";
		
		HashMap<String,Integer> typeSet;
		typeSet =new HashMap<String,Integer>();
		KNN knn = new KNN();
		
		System.out.println("Building Index...");
		knn.BuildReader(indexDir, dataDir);
		knn.BuildReader(indexDir2C, dataDir2C);
		System.out.println("Complete!");
		
		Directory dir1 = FSDirectory.open(new File(indexDir));	
		Directory dir2 = FSDirectory.open(new File(indexDir2C));
		IndexReader trainingSetReader = IndexReader.open(dir1);
		IndexReader testSetReader = IndexReader.open(dir2);
		ArrayList<FileWithTermVector> trainingSet = new ArrayList<FileWithTermVector>();
		ArrayList<FileWithTermVector> testSet = new ArrayList<FileWithTermVector>();
		
		   System.err.println("程序开始："+formatter.format(new Date()));
		
		   //预处理1：解除Lucene依赖，获得tf
		   knn.IndepFromLucene(trainingSetReader,testSetReader,trainingSet,testSet,typeSet);
		   int docNum = trainingSet.size();
		   
		   //预处理2：训练得到dfSet,此时里面存储的是df
		   ArrayList<generalizedDF> dfSet = knn.trainingDFSet(trainingSet, testSet);
	       System.err.println("DFSIZE:"+dfSet.size());
	       
		   //预处理3：筛选出出现次数>2的词且typeCount<3/5*类别总数的词
	       knn.MiddleLeft(dfSet,dfLowerLimit,typeUpperLimit);
		   System.err.println("DFSIZE-Reduced:"+dfSet.size());
	
		   //预处理4：进行Chi-Square特征选择
		   knn.ChiSquareReduce(dfSet,typeSet,VectorNum,docNum);
		   
		   //预处理5：对整个trainingSet筛出特征值向量,且加入idf参数,同时剩下的dfSet中df变idf
		   knn.ReduceTrainingSet_PutIDF(trainingSet,dfSet);
		
		   System.err.println("训练阶段结束");
		   System.err.println("测试阶段开始"+formatter.format(new Date()));
		   System.err.flush();
		
		   //KNN主体
		   double value =  knn.KNNBody(trainingSet, testSet, dfSet, typeSet,kValue);
		   String str = "VectorNum="+VectorNum+" | K="+kValue+" | ["+dfLowerLimit+","+typeUpperLimit+"] " +
		   		"总准确率="+value;
		 //  fout.write(str+"\r\n");
		   //fout.write("\r\n");
		   //fout.close();
		   System.err.println("---------------------termainated");
		   /*
		    * 一定要关闭!
		    */
		   trainingSetReader.close();
		   testSetReader.close();
		}
	private double CalculateDistance(FileWithTermVector testFile,
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
	
	private void BuildReader(String indexDir,String dataDir) throws IOException{
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
        	      
        	      InputStreamReader isr= new InputStreamReader(new FileInputStream(f),"UTF-8");

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
  
	private SDPair ChiSquare(ArrayList<generalizedDF> dfSet,
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
			System.err.println("!!!|A="+A+"|B="+B+"|C="+C+"|D="+D
					+"|up="+up+"|down="+down+"|chiSquare="+chiSquare+"!trainingSet.size()="+docNum);
			System.err.println("trainingSet.size()="+docNum+
					"(A*D-B*C)*(A*D-B*C)="+(A*D-B*C)*(A*D-B*C));
		}
		return new SDPair(dfSet.get(dfSetNum).word,chiSquare);
	}

   private void IndepFromLucene(IndexReader trainingSetReader, IndexReader testSetReader,
		   ArrayList<FileWithTermVector> trainingSet,ArrayList<FileWithTermVector> testSet
		   ,HashMap<String,Integer> typeSet)  throws IOException
   {
	   System.err.println("预处理1：解除Lecene依赖，获得tf---（训练集）开始"+formatter.format(new Date()));
		  for(int i=0;i<trainingSetReader.numDocs();i++)
		  {
			  String tp = trainingSetReader.document(i).getField("type").stringValue();
			  trainingSet.add(new FileWithTermVector(
					  trainingSetReader.document(i).getField("title").stringValue(),tp,
					  trainingSetReader.document(i).getField("fullpath").stringValue()));
			  if(typeSet.containsKey(tp)==false)
				      typeSet.put(tp, 1);
			  else  typeSet.put(tp, typeSet.get(tp)+1);
			  TermFreqVector trainingSetTermVector = trainingSetReader.getTermFreqVector(i,"subject");
				  String[] termSet = trainingSetTermVector.getTerms();
				  int[] termFreqSet = trainingSetTermVector.getTermFrequencies();
				  for(int j=0;j<termSet.length;j++)
			          trainingSet.get(i).insertTermVector(termSet[j], termFreqSet[j], "VACANCY"); 
		  }
		  System.err.println("预处理1：解除Lecene依赖，获得tf---（训练集）结束"+formatter.format(new Date()));
		  System.err.println("预处理1：解除Lecene依赖，获得tf---（测试集）开始"+formatter.format(new Date()));	  
		  for(int i=0;i<testSetReader.numDocs();i++)
		  {
			  testSet.add(new FileWithTermVector(
					  testSetReader.document(i).getField("title").stringValue()
					  ,testSetReader.document(i).getField("type").stringValue(),
					  testSetReader.document(i).getField("fullpath").stringValue()));
			  TermFreqVector testSetTermVector = testSetReader.getTermFreqVector(i,"subject");
				  String[] termSet = testSetTermVector.getTerms();
				  int[] termFreqSet = testSetTermVector.getTermFrequencies();
				  for(int j=0;j<termSet.length;j++)
					  testSet.get(i).insertTermVector(termSet[j], termFreqSet[j], "VACANCY");
		  }
		  System.err.println("预处理1：解除Lecene依赖，获得tf---（测试集）结束"+formatter.format(new Date()));
   }
   
   private ArrayList<generalizedDF> trainingDFSet (ArrayList<FileWithTermVector> trainingSet,
		   ArrayList<FileWithTermVector> testSet)
   {
	   ArrayList<generalizedDF> dfSet = new  ArrayList<generalizedDF>();
	   System.err.println("预处理2：训练dfSet---开始"+formatter.format(new Date()));
		for(int i=0;i<trainingSet.size();i++)
		{
			System.err.println("training:"+trainingSet.get(i).title+trainingSet.get(i).fileType);
			 for(int j=0;j<trainingSet.get(i).termVectorSet.size();j++)
			 {
				 boolean isIn = false;
				 for(int k=0;k<dfSet.size();k++)
			        	if(dfSet.get(k).word.equals(trainingSet.get(i).termVectorSet.get(j).word))
			    	    	{
			        		    dfSet.get(k).df++;
			        		    dfSet.get(k).insertMap(trainingSet.get(i).title,trainingSet.get(i).fileType,i);
			        		    isIn = true;
			        		    break;
			        		}
				 if(!isIn)
			     	{
					 generalizedDF tmp = new generalizedDF(trainingSet.get(i).termVectorSet.get(j).word, 1);
					 tmp.insertMap(trainingSet.get(i).title,trainingSet.get(i).fileType,i);
					 dfSet.add(tmp);
			     	}
			 }
		}	
	  System.err.println("预处理2：训练dfSet---结束"+formatter.format(new Date()));
	  return dfSet;
   }
   
   private void MiddleLeft(ArrayList<generalizedDF> dfSet,int dfLowerLimit,int typeUpperLimit)
   {
	   System.err.println("预处理3：筛选---开始"+formatter.format(new Date()));
	 		for(int i=0;i<dfSet.size();i++)
	 		{
	 			if(dfSet.get(i).df<=dfLowerLimit||dfSet.get(i).getTypeCount()>=typeUpperLimit)
	 				dfSet.remove(i--);
	 		}
   }
   
   private void ChiSquareReduce(ArrayList<generalizedDF> dfSet,
		   HashMap<String,Integer> typeSet,int VectorNum,int docNum)
   {
	   System.err.println("预处理4：进行Chi-Square特征选择---开始"+formatter.format(new Date()));
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
		System.err.println("预处理4：进行Chi-Square特征选择(idf:"+dfSet.size()+")---结束"+formatter.format(new Date()));
   }
 
   private void ReduceTrainingSet_PutIDF(ArrayList<FileWithTermVector> trainingSet,ArrayList<generalizedDF> dfSet)
   {
	   for(int i=0;i<trainingSet.size();i++)
		{
		    System.err.print(trainingSet.get(i).title+trainingSet.get(i).fileType+":");
		    System.err.print(trainingSet.get(i).termVectorSet.size()+"->");
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
			System.err.println(trainingSet.get(i).termVectorSet.size());
			System.err.flush();
		}
		System.err.println("预处理5：筛trainingSet---结束"+formatter.format(new Date()));
   }
   
   private double KNNBody(ArrayList<FileWithTermVector> trainingSet,
		   ArrayList<FileWithTermVector> testSet,ArrayList<generalizedDF> dfSet,
		   HashMap<String,Integer> typeSet,int kValue) throws IOException
   {
	   //初始化两个Set
	   HashMap<String,Integer> typeSet_AC_1 = new  HashMap<String,Integer>();
	   HashMap<String,Integer> typeSet_AC_YY = new  HashMap<String,Integer>();
	   Set<String> ks = typeSet.keySet();
	   for(String type :ks)
	     {
		     typeSet_AC_1.put(type, 0);
		     typeSet_AC_YY.put(type,0);
	     }
	   
	 //测试集规约 
		 for(int t=0;t<testSet.size();t++)
			{
				  System.err.print(testSet.get(t).title+testSet.get(t).fileType+":");
				    System.err.print(testSet.get(t).termVectorSet.size()+"->");
				for(int j=0;j<testSet.get(t).termVectorSet.size();j++)
				{
					boolean isIn = false;
					for(int k=0;k<dfSet.size();k++)
				    	if(dfSet.get(k).word.equals(testSet.get(t).termVectorSet.get(j).word))
					    	{
					   		  isIn = true;
					   		  break;
					    	}
					if(isIn==false)
				       testSet.get(t).termVectorSet.remove(j--);
				}
				System.err.println(testSet.get(t).termVectorSet.size());
				System.err.flush();
			 }
	   
	    //KNN主体（根据距离度量加权计算）
    	int yyCount=0;
	 	double MacroF1 = 0;
	 	for(int i=0;i<testSet.size();i++)
	 	{
	 		//基于倒排索引的KNN分类法:在查找样本的k个邻居时，只查找与待分类文本的词条有重叠的文本。
	 		//思路： 找到向量V 中每个词条ti (1≤i≤n) (n 表示文档向量的长度)的文本链表li(1≤i≤n)
	 		//     合并li，去掉链表中重复的文本ID，得到文本ID 的集合
	 		//     与集合中的文本计算相似度
	 	    //	System.err.println("计算重叠的文件开始"+formatter.format(new Date()));
	 		ArrayList<SSPair> overlappingFiles = new ArrayList<SSPair>();
	 		for(int q=0;q<testSet.get(i).termVectorSet.size();q++)
	 		{
	 		   for(int j=0;j<dfSet.size();j++)
	 			 {
	 				if(testSet.get(i).termVectorSet.get(q).word.equals(dfSet.get(j).word))
	 				{
	 				    for(int k=0;k<dfSet.get(j).map2File.size();k++)
	 				    {
	 				  	    boolean isIn = false;
	 				    	for(int n=0;n<overlappingFiles.size();n++)
	 						    if(dfSet.get(j).map2File.get(k).fileName.equals(
	 								    overlappingFiles.get(n).fileName))
	 						    {
	 						    	isIn = true;
	 						    	break;
	 						    }
	 					    if(isIn==false)
	 					    overlappingFiles.add(new SSPair(dfSet.get(j).map2File.get(k).fileName,
	 							dfSet.get(j).map2File.get(k).fileType,
	 							dfSet.get(j).map2File.get(k).filePosition));
	 				     }
	 			    }
	 			 }
	 		 }
	 			
	 		//取得目标到所有训练集元素的距离
	 		ArrayList<SDPair> distanceSetWithType = new ArrayList<SDPair>(); 
	 		for(int j=0;j<overlappingFiles.size();j++)
	 		{
	 			double distance = CalculateDistance(testSet.get(i),
	 					trainingSet.get(overlappingFiles.get(j).filePosition),dfSet);
	 			distanceSetWithType.add(new SDPair(overlappingFiles.get(j).fileType,distance));
	 		}
	 		
	 	    Collections.sort(distanceSetWithType);
	 			
	 		//找前n个,合并同类项
	 		ArrayList<SDPair> reducedDSWithType = new ArrayList<SDPair>();
	 		int min = kValue>distanceSetWithType.size()?distanceSetWithType.size():kValue;
	 		for(int j=0;j<min;j++)
	 		{
	 			 boolean isIn = false;
	 			 String rType = distanceSetWithType.get(j).str;
	 			 double rValue = distanceSetWithType.get(j).distance;
	 			  for(int m=0;m<reducedDSWithType.size();m++)
	 			    if(reducedDSWithType.get(m).str.equals(rType))
	 			      {
	 			    	double combinedDistance = reducedDSWithType.get(m).distance;
	 			    	reducedDSWithType.set(m, new SDPair(rType, combinedDistance+Math.cos(rValue)));
	 			    	isIn = true;
	 			    	break;
	 			      }
	 			    if(!isIn) //距离变权值
	 			    	reducedDSWithType.add(new SDPair(distanceSetWithType.get(j).str,
	 			    			Math.cos(distanceSetWithType.get(j).distance)));
	 			 }
	 			
	 		//找最大的值
	 		double minValue = -999999;
	 		String finalType = null;
	 		for(int j=0;j<reducedDSWithType.size();j++)
	 		{
	 			double tmp = reducedDSWithType.get(j).distance;
	 			if(minValue<tmp)
	 				{
	 				   minValue = tmp;
	 				   finalType = reducedDSWithType.get(j).str;
	 				}
	 		}
	 		System.out.flush();
	 		String result = testSet.get(i).fileType.equals(finalType)?" ":"X";
	 		if(testSet.get(i).fileType.equals(finalType))
	 			{
	 			   yyCount++;
	 			   if(finalType!=null)
	 			   typeSet_AC_YY.put(finalType,typeSet_AC_YY.get(finalType)+1);
	 			}
	 		if(finalType!=null)
	 			typeSet_AC_1.put(finalType, typeSet_AC_1.get(finalType)+1);
	 		System.err.println(testSet.get(i).title+testSet.get(i).fileType+"->"+finalType+
	 				":"+result+"   ---"+(double)yyCount/(double)testSet.size());
	 		System.err.flush();
	 	}
	 	
	 	Double rate = ((double)yyCount)/((double)testSet.size());
	 	Set<String> ks2 = typeSet.keySet();
	 	for(String type :ks2)
	 		{
	 			double A = typeSet_AC_YY.get(type);
	 			double C = testSet.size()/typeSet.size()-A;
	 			double B = typeSet_AC_1.get(type)-A;
	 			double Recall = A/(A+C);
	 			double Precision = A/(A+B);
	 			double F1 = 2*Recall*Precision/(Precision+Recall);
	 			System.err.println("类别:"+type+"->召回率"+Recall+"|准确率"+Precision+"|F1值"+F1+"|A="+A+"|B="+B+"|C="+C);
	 			MacroF1+=F1;
	 		}
	 	MacroF1/=typeSet.size();
	 	System.err.println("总准确率:"+rate.toString());
	    System.err.println("程序结束,MacroF1："+MacroF1+" 完成时间 "+formatter.format(new Date()));
	    return rate;    
   }
   
   private void BuildReader2(String indexDir,String dataDir) throws IOException
   {
	   
	   
   }
}
