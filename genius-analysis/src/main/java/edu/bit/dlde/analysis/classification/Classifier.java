package edu.bit.dlde.analysis.classification;

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
import java.util.List;
import java.util.Map;
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
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Classifier {

	/**
	 * @param JiangXiaoTian
	 */
	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

	 public static ArrayList<Result> Classify() throws IOException {
//	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		int VectorNum = 1200;
		int kValue = 40;
		int dfLowerLimit = 1;
		int typeUpperLimit = 8;

		System.err.println("测试阶段开始" + formatter.format(new Date()));
		System.err.flush();
		// FileWriter fout = new FileWriter(".\\LuceneProjectNec\\result.txt",
		// true);

		String testSetDir = "C:\\Classification_Corpus\\testSet";
		File file1 = new File("C:\\Classification_Index\\testSetIndex");
		if (file1.exists() == false)
			file1.mkdirs();
		String testSetIndex = "C:\\Classification_Index\\testSetIndex";

		// 取出数据
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "KNN");
		Map<String, Integer> typeSet = new HashMap<String, Integer>();
		DBCollection coll = mongo.getDB("KNN").getCollection("typeSet");

		DBCursor cur = coll.find();
		DBObject dbo = cur.toArray().get(0);
		// String key = dbo.keySet().toArray(new String[0])[1];
		typeSet = dbo.toMap();
		typeSet.remove("_id");
		// typeSet.put(key, dbo.get(key));
		List<FileWithTermVector> trainingSet = ds
				.find(FileWithTermVector.class).asList();
		List<generalizedDF> dfSet = ds.find(generalizedDF.class).asList();

		// 预处理一下测试数据
		System.out.println("Building Index...");
		BuildReader(testSetIndex, testSetDir);
		System.out.println("Complete!");
		Directory dir = FSDirectory.open(new File(testSetIndex));
		IndexReader testSetReader = IndexReader.open(dir);
		ArrayList<FileWithTermVector> testSet = new ArrayList<FileWithTermVector>();
		TestSetIndependence(testSetReader, testSet);

		// KNN主体
		ArrayList<Result> results = KNNBody(trainingSet, testSet, dfSet,
				typeSet, kValue);
		// String str =
		// "VectorNum="+VectorNum+" | K="+kValue+" | ["+dfLowerLimit+","+typeUpperLimit+"] "
		// +
		// "总准确率="+value;
		// fout.write(str+"\r\n");
		// fout.write("\r\n");
		// fout.close();
		//
		System.out.println("---------------------termainated");
		 System.out.println("存储至mongoDB:开始");
		   ds = morphia.createDatastore(mongo, "KNN");
		   ds.save(results);
		   
		 return results;
	}

	public static ArrayList<Result> KNNBody(
			List<FileWithTermVector> trainingSet,
			List<FileWithTermVector> testSet, List<generalizedDF> dfSet,
			Map<String, Integer> typeSet, int kValue) throws IOException {

		ArrayList<Result> results = new ArrayList<Result>();
		// 初始化两个Set
		HashMap<String, Integer> typeSet_AC_1 = new HashMap<String, Integer>();
		HashMap<String, Integer> typeSet_AC_YY = new HashMap<String, Integer>();
		Set<String> ks = typeSet.keySet();
		for (String type : ks) {
			typeSet_AC_1.put(type, 0);
			typeSet_AC_YY.put(type, 0);
		}

		// 测试集规约
		for (int t = 0; t < testSet.size(); t++) {
			System.err.print(testSet.get(t).title + testSet.get(t).fileType
					+ ":");
			System.err.print(testSet.get(t).termVectorSet.size() + "->");
			for (int j = 0; j < testSet.get(t).termVectorSet.size(); j++) {
				boolean isIn = false;
				for (int k = 0; k < dfSet.size(); k++)
					if (dfSet.get(k).word.equals(testSet.get(t).termVectorSet
							.get(j).word)) {
						isIn = true;
						break;
					}
				if (isIn == false)
					testSet.get(t).termVectorSet.remove(j--);
			}
			System.err.println(testSet.get(t).termVectorSet.size());
			System.err.flush();
		}

		// KNN主体（根据距离度量加权计算）
		int yyCount = 0;
		double MacroF1 = 0;
		for (int i = 0; i < testSet.size(); i++) {
			// 基于倒排索引的KNN分类法:在查找样本的k个邻居时，只查找与待分类文本的词条有重叠的文本。
			// 思路： 找到向量V 中每个词条ti (1≤i≤n) (n 表示文档向量的长度)的文本链表li(1≤i≤n)
			// 合并li，去掉链表中重复的文本ID，得到文本ID 的集合
			// 与集合中的文本计算相似度
			// System.err.println("计算重叠的文件开始"+formatter.format(new Date()));
			ArrayList<SSPair> overlappingFiles = new ArrayList<SSPair>();
			for (int q = 0; q < testSet.get(i).termVectorSet.size(); q++) {
				for (int j = 0; j < dfSet.size(); j++) {
					if (testSet.get(i).termVectorSet.get(q).word.equals(dfSet
							.get(j).word)) {
						for (int k = 0; k < dfSet.get(j).map2File.size(); k++) {
							boolean isIn = false;
							for (int n = 0; n < overlappingFiles.size(); n++)
								if (dfSet.get(j).map2File.get(k).fileName
										.equals(overlappingFiles.get(n).fileName)) {
									isIn = true;
									break;
								}
							if (isIn == false)
								overlappingFiles
										.add(new SSPair(
												dfSet.get(j).map2File.get(k).fileName,
												dfSet.get(j).map2File.get(k).fileType,
												dfSet.get(j).map2File.get(k).filePosition));
						}
					}
				}
			}

			// 取得目标到所有训练集元素的距离
			ArrayList<SDPair> distanceSetWithType = new ArrayList<SDPair>();
			for (int j = 0; j < overlappingFiles.size(); j++) {
				double distance = CalculateDistance(testSet.get(i),
						trainingSet.get(overlappingFiles.get(j).filePosition),
						dfSet);
				distanceSetWithType.add(new SDPair(
						overlappingFiles.get(j).fileType, distance));
			}

			Collections.sort(distanceSetWithType);

			// 找前n个,合并同类项
			ArrayList<SDPair> reducedDSWithType = new ArrayList<SDPair>();
			int min = kValue > distanceSetWithType.size() ? distanceSetWithType
					.size() : kValue;
			for (int j = 0; j < min; j++) {
				boolean isIn = false;
				String rType = distanceSetWithType.get(j).str;
				double rValue = distanceSetWithType.get(j).distance;
				for (int m = 0; m < reducedDSWithType.size(); m++)
					if (reducedDSWithType.get(m).str.equals(rType)) {
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
			for (int j = 0; j < reducedDSWithType.size(); j++) {
				double tmp = reducedDSWithType.get(j).distance;
				if (minValue < tmp) {
					minValue = tmp;
					finalType = reducedDSWithType.get(j).str;
				}
			}
			System.out.flush();
			String result = testSet.get(i).fileType.equals(finalType) ? " "
					: "X";
			if (testSet.get(i).fileType.equals(finalType)) {
				yyCount++;
				if (finalType != null)
					typeSet_AC_YY.put(finalType,
							typeSet_AC_YY.get(finalType) + 1);
			}
			if (finalType != null)
				typeSet_AC_1.put(finalType, typeSet_AC_1.get(finalType) + 1);
			System.err.println(testSet.get(i).title + testSet.get(i).fileType
					+ "->" + finalType + ":" + result + "   ---"
					+ (double) yyCount / (double) testSet.size());
			System.err.flush();
			results.add(new Result(testSet.get(i).title, finalType, testSet
					.get(i).filePath));
		}

		Double rate = ((double) yyCount) / ((double) testSet.size());
		Set<String> ks2 = typeSet.keySet();
		for (String type : ks2) {
			double A = typeSet_AC_YY.get(type);
			double C = testSet.size() / typeSet.size() - A;
			double B = typeSet_AC_1.get(type) - A;
			double Recall = A / (A + C);
			double Precision = A / (A + B);
			double F1 = 2 * Recall * Precision / (Precision + Recall);
			System.err.println("类别:" + type + "->召回率" + Recall + "|准确率"
					+ Precision + "|F1值" + F1 + "|A=" + A + "|B=" + B + "|C="
					+ C);
			MacroF1 += F1;
		}
		MacroF1 /= typeSet.size();
		System.err.println("总准确率:" + rate.toString());
		System.err.println("程序结束,MacroF1：" + MacroF1 + " 完成时间 "
				+ formatter.format(new Date()));
		String str = "总准确率=" + rate;

		return results;
	}

	public static double CalculateDistance(FileWithTermVector testFile,
			FileWithTermVector trainingFile, List<generalizedDF> dfSet)
			throws IOException {
		double trainingFileMode = 0;
		double testFileMode = 0;
		double up = 0;
		for (int i = 0; i < testFile.termVectorSet.size(); i++) {
			for (int j = 0; j < trainingFile.termVectorSet.size(); j++) {
				if (trainingFile.termVectorSet.get(j).word
						.equals(testFile.termVectorSet.get(i).word)) {
					// 获得交叉项的tf,idf
					int tf1 = testFile.termVectorSet.get(i).tf;
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
		for (int i = 0; i < testFile.termVectorSet.size(); i++) {
			double idf = 0;
			for (int j = 0; j < dfSet.size(); j++)
				if (dfSet.get(j).word
						.equals(testFile.termVectorSet.get(i).word))
					idf = dfSet.get(j).df;
			testFileMode += idf * idf * testFile.termVectorSet.get(i).tf * idf
					* idf * testFile.termVectorSet.get(i).tf;
		}
		testFileMode = Math.sqrt(testFileMode);
		return Math.acos(up / (trainingFileMode * testFileMode));
	}

	public static void BuildReader(String indexDir, String dataDir)
			throws IOException {
		Directory dir = FSDirectory.open(new File(indexDir));

		Analyzer analyzer = new IKAnalyzer();
		IndexWriter writer = new IndexWriter(dir, analyzer, true,
				IndexWriter.MaxFieldLength.UNLIMITED);

		File[] files = new File(dataDir).listFiles();
		for (File f : files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
					&& (f.getName().toLowerCase().endsWith(".txt"))) {
				Document doc = new Document();
				Field d = new Field("subject", "Whatever", Field.Store.NO,
						Field.Index.ANALYZED,
						Field.TermVector.WITH_POSITIONS_OFFSETS);

				InputStreamReader isr = new InputStreamReader(
						new FileInputStream(f), "UTF-8");

				d.setValue(isr);
				// d.setValue(new FileReader(f));
				doc.add(d);
				String beforeS = f.getName().replace(".txt", "");
				String[] s = beforeS.split("@");
				for (int k = 0; k < s.length; k++) {
					if (k == 0)
						doc.add(new Field("type", s[k], Field.Store.YES,
								Field.Index.NOT_ANALYZED));
					if (k == s.length - 1)
						doc.add(new Field("title", s[k], Field.Store.YES,
								Field.Index.NOT_ANALYZED));
				}
				doc.add(new Field("fullpath", f.getCanonicalPath(),
						Field.Store.YES, Field.Index.NOT_ANALYZED));
				writer.addDocument(doc);
			}
		}
		writer.close();
	}

	public static void TestSetIndependence(IndexReader testSetReader,
			ArrayList<FileWithTermVector> testSet) throws IOException {
		System.err.println("预处理1：解除Lecene依赖，获得tf---（测试集）开始"
				+ formatter.format(new Date()));
		for (int i = 0; i < testSetReader.numDocs(); i++) {
			testSet.add(new FileWithTermVector(testSetReader.document(i)
					.getField("title").stringValue(), testSetReader.document(i)
					.getField("type").stringValue(), testSetReader.document(i)
					.getField("fullpath").stringValue()));
			TermFreqVector testSetTermVector = testSetReader.getTermFreqVector(
					i, "subject");
			String[] termSet = testSetTermVector.getTerms();
			int[] termFreqSet = testSetTermVector.getTermFrequencies();
			for (int j = 0; j < termSet.length; j++)
				testSet.get(i).insertTermVector(termSet[j], termFreqSet[j],
						"VACANCY");
		}
		System.err.println("预处理1：解除Lecene依赖，获得tf---（测试集）结束"
				+ formatter.format(new Date()));
	}

}
