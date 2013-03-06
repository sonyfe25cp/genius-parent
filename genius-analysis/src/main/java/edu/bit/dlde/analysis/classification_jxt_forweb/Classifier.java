package edu.bit.dlde.analysis.classification_jxt_forweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.*;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import edu.bit.dlde.analysis.classification.FileWithTermVector;

public class Classifier {

	/**
	 * @param JiangXiaoTian
	 */
	public static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

	public static ArrayList<ClassificationResult> Classify(
			List<String> localIds,List<String>pageIds,Datastore ds) throws IOException {
//	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int kValue = 40;
		System.out.println("测试阶段开始" + formatter.format(new Date()));
		System.out.flush();
		
		File file1 = new File("./Classification_Index/classifySetIndex");
		if (file1.exists() == false)
			file1.mkdirs();
		String classifySetIndexDir = "./Classification_Index/classifySetIndex";

		// 取出数据
		List<FileProperty> trainingSet = ds.find(FileProperty.class).asList();
		List<WordProperty> wordSet = ds.find(WordProperty.class).asList();
		List<Category> categories = ds.find(Category.class).asList();

		// 预处理一下测试数据
		ArrayList<FileProperty> classifySet = BuildClassifySet(localIds,pageIds,
				classifySetIndexDir,categories);

		// KNN主体
		ArrayList<ClassificationResult> results = KNNBody(trainingSet, classifySet,
				wordSet,categories, kValue);
		System.out.println("---------------------termainated");
		return results;
	}
	
	public static ArrayList<FileProperty> BuildClassifySet(List<String> localIds,
			List<String> pageIds,String classifySetIndexDir,List<Category> categories
			) throws IOException
		{
        Directory dir = FSDirectory.open(new File(classifySetIndexDir));
        //1 得到lucene索引
        System.out.print("Building Index......");
        Analyzer analyzer = new IKAnalyzer();
        IndexWriter writer = new IndexWriter(dir, analyzer, true , IndexWriter.MaxFieldLength.UNLIMITED);
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
        System.out.println("完成!");
        
        //2.从Lucene中读出刚刚建立的本地文件索引，建立classifySet
        System.out.print("Building classifySet...");
		Directory dir1 = FSDirectory.open(new File(classifySetIndexDir));	
		IndexReader classifySetReader = IndexReader.open(dir1);
		ArrayList<FileProperty> classifySet = new ArrayList<FileProperty>();
			
		for (int i = 0; i < classifySetReader.numDocs(); i++) {
			String title = classifySetReader.document(i).getField("title")!=null?
					classifySetReader.document(i).getField("title").stringValue():null;
			String author = classifySetReader.document(i).getField("author")!=null?
					classifySetReader.document(i).getField("author").stringValue():null;
				String type = classifySetReader.document(i).getField("type")!=null?
						classifySetReader.document(i).getField("type").stringValue():null;
			classifySet.add(new FileProperty(classifySetReader.document(i).getField("id").stringValue(),
					  type,author,title));
			TermFreqVector testSetTermVector = classifySetReader.getTermFreqVector(
					i, "body");
			String[] termSet = testSetTermVector.getTerms();
			int[] termFreqSet = testSetTermVector.getTermFrequencies();
			for (int j = 0; j < termSet.length; j++)
				classifySet.get(i).AddTermVector(termSet[j], termFreqSet[j],
						"VACANCY");
		}
		//把genius页面加入classifySet
		if(pageIds!=null&&pageIds.size()!=0)
		{
		Directory dir2 = FSDirectory.open(new File("C:\\Users\\jiang\\data\\index\\genius2"));
		IndexReader classifySetReader2 = IndexReader.open(dir2);
		for (int i = 0; i < classifySetReader2.numDocs(); i++) 
			if(pageIds.contains(classifySetReader2.document(i).getField("id").stringValue()))
			{
				String title = classifySetReader2.document(i).getField("title")!=null?
						classifySetReader2.document(i).getField("title").stringValue():null;
				String author = classifySetReader2.document(i).getField("author")!=null?
						classifySetReader2.document(i).getField("author").stringValue():null;
				String type = classifySetReader2.document(i).getField("type")!=null?
						classifySetReader2.document(i).getField("type").stringValue():null;
				classifySet.add(new FileProperty(classifySetReader2.document(i).getField("id").stringValue(),
								type,author,title));
			
				TermFreqVector pageTermVector = classifySetReader2.getTermFreqVector(i, "body");
				String[] termSet = pageTermVector.getTerms();
				int[] termFreqSet = pageTermVector.getTermFrequencies();
				int sz = classifySet.size();
				
				for (int j = 0; j < termSet.length; j++)
					classifySet.get(sz-1).AddTermVector(termSet[j], termFreqSet[j],
							"VACANCY");
				break;
			}
		}
		System.out.println("完成!");
		return classifySet;
	}
	
	public static ArrayList<ClassificationResult> KNNBody(List<FileProperty> trainingSet,
			List<FileProperty> classifySet,List<WordProperty> wordSet,
			List<Category> categories, int kValue)throws IOException 
			{
		ArrayList<ClassificationResult> results = new ArrayList<ClassificationResult>();
		// 初始化两个Set
		HashMap<String, Integer> typeSet_AC_1 = new HashMap<String, Integer>();
		HashMap<String, Integer> typeSet_AC_YY = new HashMap<String, Integer>();
		for (Category category : categories) 
		{
			typeSet_AC_1.put(category.getName(), 0);
			typeSet_AC_YY.put(category.getName(), 0);
		}

		// 测试集规约
		for (int t = 0; t < classifySet.size(); t++) {
			System.out.print(classifySet.get(t).id+ ":");
			System.out.print(classifySet.get(t).termVectorSet.size() + "->");
			for (int j = 0; j < classifySet.get(t).termVectorSet.size(); j++) 
			{
				boolean isIn = false;
				for (int k = 0; k < wordSet.size(); k++)
					if (wordSet.get(k).word.equals(classifySet.get(t).termVectorSet
							.get(j).word))
					{
						isIn = true;
						break;
					}
				if (isIn == false)
					classifySet.get(t).termVectorSet.remove(j--);
			}
			System.out.println(classifySet.get(t).termVectorSet.size());
			System.out.flush();
		}

		// KNN主体（根据距离度量加权计算）
		int yyCount = 0;
		double MacroF1 = 0;
		for (int i = 0; i < classifySet.size(); i++) {
			// 基于倒排索引的KNN分类法:在查找样本的k个邻居时，只查找与待分类文本的词条有重叠的文本。
			// 思路： 找到向量V 中每个词条ti (1≤i≤n) (n 表示文档向量的长度)的文本链表li(1≤i≤n)
			// 合并li，去掉链表中重复的文本ID，得到文本ID 的集合
			// 与集合中的文本计算相似度
			// System.out.println("计算重叠的文件开始"+formatter.format(new Date()));
			ArrayList<SSPair> overlappingFiles = new ArrayList<SSPair>();
			for (int q = 0; q < classifySet.get(i).termVectorSet.size(); q++) 
			{
				for (int j = 0; j < wordSet.size(); j++) 
				{
					if (classifySet.get(i).termVectorSet.get(q).word.equals(wordSet
							.get(j).word)) 
					{
						for (int k = 0; k < wordSet.get(j).map2File.size(); k++) 
						{
							boolean isIn = false;
							for (int n = 0; n < overlappingFiles.size(); n++)
								if (wordSet.get(j).map2File.get(k).fileName
										.equals(overlappingFiles.get(n).fileName)) 
								{
									isIn = true;
									break;
								}
							if (isIn == false)
								overlappingFiles.add(new SSPair(
									wordSet.get(j).map2File.get(k).fileName,
									wordSet.get(j).map2File.get(k).fileType,
									wordSet.get(j).map2File.get(k).filePosition));
						}
					}
				}
			}

			// 取得目标到所有训练集元素的距离
			ArrayList<SDPair> distanceSetWithType = new ArrayList<SDPair>();
			for (int j = 0; j < overlappingFiles.size(); j++) 
			{
				double distance = CalculateDistance(classifySet.get(i),
						trainingSet.get(overlappingFiles.get(j).filePosition),wordSet);
				distanceSetWithType.add(new SDPair(overlappingFiles.get(j).fileType, distance));
			}

			Collections.sort(distanceSetWithType);

			// 找前n个,合并同类项
			ArrayList<SDPair> reducedDSWithType = new ArrayList<SDPair>();
			int min = kValue > distanceSetWithType.size() ? distanceSetWithType
					.size() : kValue;
			for (int j = 0; j < min; j++) 
			{
				boolean isIn = false;
				String rType = distanceSetWithType.get(j).str;
				double rValue = distanceSetWithType.get(j).distance;
				for (int m = 0; m < reducedDSWithType.size(); m++)
					if (reducedDSWithType.get(m).str.equals(rType)) 
					{
						double combinedDistance = reducedDSWithType.get(m).distance;
						reducedDSWithType.set(m, new SDPair(rType,
								combinedDistance + Math.cos(rValue)));
						isIn = true;
						break;
					}
				if (!isIn) // 距离变权值
					reducedDSWithType.add(new SDPair(
							distanceSetWithType.get(j).str, Math
									.cos(distanceSetWithType.get(j).distance)));
			}

			// 找最大的值
			double minValue = -999999;
			String finalType = null;
			for (int j = 0; j < reducedDSWithType.size(); j++) 
			{
				double tmp = reducedDSWithType.get(j).distance;
				if (minValue < tmp) 
				{
					minValue = tmp;
					finalType = reducedDSWithType.get(j).str;
				}
			}
			if(classifySet.get(i).type!=null)
			{
				String result = classifySet.get(i).type.equals(finalType) ? " ": "X";
				if (classifySet.get(i).type.equals(finalType)) 
				{
					yyCount++;
					if (finalType != null)
						typeSet_AC_YY.put(finalType,typeSet_AC_YY.get(finalType) + 1);
				}
				if (finalType != null)
					typeSet_AC_1.put(finalType, typeSet_AC_1.get(finalType) + 1);
				System.out.println(classifySet.get(i).title + classifySet.get(i).type
						+ "->" + finalType + ":" + result + "   ---"
						+ (double) yyCount / (double) classifySet.size());
				System.out.flush();
				results.add(new ClassificationResult(classifySet.get(i).id, 
						finalType, classifySet.get(i).author,classifySet.get(i).title));
			}
			else
			{
				System.out.println(classifySet.get(i).id+ ":->" + finalType);
				results.add(new ClassificationResult(classifySet.get(i).id, 
						finalType, classifySet.get(i).author,classifySet.get(i).title));
			}
		}
		if(classifySet.get(0).type!=null)
		{
			Double rate = ((double) yyCount) / ((double) classifySet.size());
			for (Category category : categories) {
				double A = typeSet_AC_YY.get(category.getName());
				double C = classifySet.size() / categories.size() - A;
				double B = typeSet_AC_1.get(category.getName()) - A;
				double Recall = A / (A + C);
				double Precision = A / (A + B);
				double F1 = 2 * Recall * Precision / (Precision + Recall);
				System.out.println("类别:" + category.getName() + "->召回率" + Recall + "|准确率"
						+ Precision + "|F1值" + F1 + "|A=" + A + "|B=" + B + "|C="
						+ C);
				MacroF1 += F1;
			}
			MacroF1 /= categories.size();
			System.out.println("总准确率:" + rate.toString());
			System.out.println("程序结束,MacroF1：" + MacroF1 + " 完成时间 "
					+ formatter.format(new Date()));
		}
		return results;
	}

	public static double CalculateDistance(FileProperty classifyFile,
			FileProperty trainingFile, List<WordProperty> wordSet)
			throws IOException 
			{
		double trainingFileMode = 0;
		double testFileMode = 0;
		double up = 0;
		for (int i = 0; i < classifyFile.termVectorSet.size(); i++) 
		{
			for (int j = 0; j < trainingFile.termVectorSet.size(); j++) 
			{
				if (trainingFile.termVectorSet.get(j).word
						.equals(classifyFile.termVectorSet.get(i).word)) 
				{
					// 获得交叉项的tf,idf
					int tf1 = classifyFile.termVectorSet.get(i).tf;
					int tf2 = trainingFile.termVectorSet.get(j).tf;
					double idf = trainingFile.termVectorSet.get(j).idf;
					up += tf1 * tf2 * idf * idf;
				}
			}
		}
		// trainingFile的模
		for (int i = 0; i < trainingFile.termVectorSet.size(); i++)
			trainingFileMode += trainingFile.termVectorSet.get(i).idf
					* trainingFile.termVectorSet.get(i).tf
					* trainingFile.termVectorSet.get(i).idf
					* trainingFile.termVectorSet.get(i).tf;
		trainingFileMode = Math.sqrt(trainingFileMode);

		// testFile的模
		for (int i = 0; i < classifyFile.termVectorSet.size(); i++) 
		{
			double idf = 0;
			for (int j = 0; j < wordSet.size(); j++)
				if (wordSet.get(j).word
						.equals(classifyFile.termVectorSet.get(i).word))
					idf = wordSet.get(j).idf;
			testFileMode += idf * idf * classifyFile.termVectorSet.get(i).tf * idf
					* idf * classifyFile.termVectorSet.get(i).tf;
		}
		testFileMode = Math.sqrt(testFileMode);
		return Math.acos(up / (trainingFileMode * testFileMode));
	}

	//测试knn效果时使用，用于建造测试数据的classifySet.这个方法暂时不用。
	public static ArrayList<FileProperty> BuildClassifySet(String classifySetIndexDir,
			String classifySetDataDir) throws IOException
			{
        Directory dir = FSDirectory.open(new File(classifySetIndexDir));
        //1 得到lucene索引
        System.out.print("Building Index......");
        Analyzer analyzer = new IKAnalyzer();
        IndexWriter writer = new IndexWriter(dir, analyzer, true , IndexWriter.MaxFieldLength.UNLIMITED);
        
        File[] files = new File(classifySetDataDir).listFiles();
        for(File f: files){
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
        	        	  doc.add(new Field("title", splited[k],Field.Store.YES,Field.Index.NOT_ANALYZED));
        	      }
        	      writer.addDocument(doc);
        	  }
        }
        writer.close();
        System.out.println("完成!");
        
        //2.从Lucene中读出索引，建立classifySet
        System.out.print("Building classifySet...");
		Directory dir1 = FSDirectory.open(new File(classifySetIndexDir));	
		IndexReader classifySetReader = IndexReader.open(dir1);
		ArrayList<FileProperty> classifySet = new ArrayList<FileProperty>();
		
		  for(int i=0;i<classifySetReader.numDocs();i++)
		  {
			  String type = classifySetReader.document(i).getField("type")!=null?
					  classifySetReader.document(i).getField("type").stringValue():null;
			  String author = classifySetReader.document(i).getField("author")!=null?
					  classifySetReader.document(i).getField("author").stringValue():null;
			  String title = classifySetReader.document(i).getField("title")!=null?
					  classifySetReader.document(i).getField("title").stringValue():null;
			  classifySet.add(new FileProperty(classifySetReader.document(i).getField("id").stringValue(),
					  type,author,title));
			  TermFreqVector classifySetTermVector = classifySetReader.getTermFreqVector(i,"body");
			  String[] termSet = classifySetTermVector.getTerms();
			  int[] termFreqSet = classifySetTermVector.getTermFrequencies();
			  for(int j=0;j<termSet.length;j++)
				  classifySet.get(i).AddTermVector(termSet[j], termFreqSet[j], "VACANCY"); 
		  }
		System.out.println("完成!");
		return classifySet;
	}
	public static void main(String[] args) throws IOException
	{
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = new Morphia().createDatastore(mongo, "genius");
		List<String> localIds = new ArrayList<String>();
		localIds.add("C:\\Classification_Corpus\\classifySet");
//		List<String> pageIds = new ArrayList<String>();
//		pageIds.add("http://economy.caixin.com/2012-03-13/100367620.html");
		ArrayList<ClassificationResult> results = Classifier.Classify(localIds,null,ds);
		
		ds.findAndDelete(ds.createQuery(ClassificationResult.class));
	}
}
