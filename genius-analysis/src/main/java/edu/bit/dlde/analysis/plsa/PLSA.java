package edu.bit.dlde.analysis.plsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import Jama.Matrix;

/**
 *	@author zhangchangmin
 **/
public class PLSA {

	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String indexDir = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir1";
	String dataDir = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\dataDir1";
	String indexDir2C = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir2C1";
	String dataDir2C = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\DataDir2C1";
	/**
	 * web端调用此方法，进行PLSA训练
	 * @param trainingSet	训练集
	 * @param typeSet		类别集
	 */
	public void training(ArrayList<TermDoc> trainingSet,HashMap<String, Integer> typeSet,String mongoHost){
		int dfLowerLimit = 2;
		int typeUpperLimit = typeSet.size()*3/5;
		System.err.println("dfLowerLimit:"+dfLowerLimit);
		System.err.println("typeUpperLimit:"+typeUpperLimit);
		int docNum_training = trainingSet.size();
		ArrayList<DocFreq> dfSet = trainingDFSet(trainingSet);
		MiddleLeft(dfSet, dfLowerLimit, typeUpperLimit);
		ReduceSet(trainingSet, dfSet);
		try {
			Mongo mongo = new Mongo(mongoHost, 27017);
			Morphia morphia = new Morphia();
			Datastore datastore = morphia.createDatastore(mongo, "genius");
			datastore.delete(datastore.createQuery(DocFreq.class));
			datastore.save(dfSet);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		int termNum = dfSet.size();
		int typeNum = typeSet.size();
		System.err.println("termNum="+termNum);
		System.err.println("typeNum="+typeNum);
		//EM算法参数，最大迭代次数及中止条件
		Para Par = new Para();
		Par.setMaxit(200);
		Par.setLeps(0.001);
		System.err.println("PLSA训练开始");
		//开始PLSA训练
		doplsa(trainingSet, typeSet, termNum, docNum_training, Par,mongoHost);
	}
	/**
	 * web端调用此方法对新的文档进行分类
	 * @param testSet	新的文档集
	 * @param termNum	词汇量
	 * @param typeNum	类别数目
	 * @param pzl		从数据库取出的Pz的变形
	 * @param pwzl		从数据库取出的Pw_z的变形
	 * @param cat		从数据库取出的类别cat的变形
	 * @return			分类结果
	 */
	public List<DocType> doplsa_test_new(ArrayList<TermDoc> testSet,int termNum,int typeNum,
			List<String> pzl,List<String> pwzl,List<Type> cat){
		Matrix Pz=change2Matrix(pzl);
		Matrix Pw_z=change2Matrix(pwzl);
		Plsa_EM plsa_em = new Plsa_EM();
		Matrix tf_test = new Matrix(termNum, 1);
		List<DocType> dtList=new ArrayList<DocType>();
		for (int i = 0; i < testSet.size(); i++) {
			for (int j = 0; j < termNum; j++) {
				tf_test.set(j, 0, testSet.get(i).getTermFreq().get(j).getFreq());
			}
			// 执行测试
			int z_d = plsa_em.folding_in(tf_test, Pz, Pw_z);

			String cat_test = cat.get(z_d).getType();
			DocType dt=new DocType();
			dt.setDocTitle(testSet.get(i).getFileTitle());
			dt.setDocType(cat_test);
			dt.setDocPath(testSet.get(i).getId());
			dtList.add(dt);
			System.err.println(testSet.get(i).getFileTitle() + "-->" + cat_test);
		}
		return dtList;
	}
	/**
	 * 基于索引进行分类
	 * @return
	 */
	public List<DocType> classify(){
		int dfLowerLimit = 2;
		int typeUpperLimit = 5;
		
		File file1 = new File("D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir2C1");
		if (file1.exists() == false)
			file1.mkdir();
		File file2 = new File("D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\DataDir2C1");
		if (file2.exists() == false)
			file2.mkdir();
		
		List<DocType> dtList=null;
		BuildReader(indexDir, dataDir);// index training
		BuildReader(indexDir2C, dataDir2C);// index test

		ArrayList<TermDoc> trainingSet = new ArrayList<TermDoc>();
		ArrayList<TermDoc> testSet = new ArrayList<TermDoc>();
		HashMap<String, Integer> typeSet = new HashMap<String, Integer>();;// <类型，文档数>

		System.err.println("程序开始：" + formatter.format(new Date()));

		// 1：从Lucene获得每篇文档的tf
		IndepFromLucene(trainingSet, testSet, typeSet);
		int docNum_training = trainingSet.size();// 训练集大小
		int docNum_test = testSet.size();// 测试集大小
		int typeNum = typeSet.size();// 类型数
		System.err.println("训练集大小：" + docNum_training + ",测试集大小：" + docNum_test);
		System.err.flush();

		// 2：训练得到训练集的dfSet
		ArrayList<DocFreq> dfSet = trainingDFSet(trainingSet);
		System.err.println("DFSIZE:" + dfSet.size());

		// 3：筛选出出现次数>2的词且typeCount<3/5*类别总数的词
		MiddleLeft(dfSet, dfLowerLimit, typeUpperLimit);
		System.err.println("DFSIZE-Reduced:" + dfSet.size());

		int termNum = dfSet.size();
		System.err.println("词汇集大小：" + termNum);
		System.err.flush();

		// 4：精简trainingSet
		ReduceSet(trainingSet, dfSet);
		try {
			Mongo mongo = new Mongo("10.1.0.171", 27017);
			Morphia morphia = new Morphia();
			Datastore datastore = morphia.createDatastore(mongo, "genius");
			datastore.delete(datastore.createQuery(DocFreq.class));
			datastore.save(dfSet);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		ReduceSet(testSet, dfSet);
		dfSet = null;

		System.err.println("训练集大小：" + docNum_training + ",测试集大小：" + docNum_test);
		System.err.println("词汇集大小：" + termNum);
		System.err.flush();

		// 开始PLSA
		Para Par = new Para();
		Par.setMaxit(200);
		Par.setLeps(0.001);
		System.err.println("PLSA训练开始");
		doplsa(trainingSet, typeSet, termNum, docNum_training, Par,"10.1.0.171");
		trainingSet = null;
		System.err.println("PLSA训练结束 " + formatter.format(new Date()));

		System.err.println("PLSA测试开始 " + formatter.format(new Date()));
		dtList = doplsa_test(testSet, termNum, typeNum);
		testSet = null;
		System.err.println("程序结束 " + formatter.format(new Date()));
			
		return dtList;
	}
	/**
	 * Indexing
	 * @param indexDir	索引目录
	 * @param dataDir	源数据目录
	 */
	private void BuildReader(String indexDir, String dataDir){
		try {
			Directory dir = FSDirectory.open(new File(indexDir));
			Analyzer analyzer = new IKAnalyzer();
			IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
					Version.LUCENE_CURRENT, analyzer));

			writer.deleteAll();
			File[] files = new File(dataDir).listFiles();
			for (File f : files) {
				if (!f.isDirectory() && !f.isHidden() && f.exists()
						&& f.canRead()
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
		} catch (Exception e) {
			
		}
	}
	/**
	 * 得出每篇文档的词汇频率tf
	 * @param trainingSet
	 * @param testSet
	 * @param typeSet
	 */
	private void IndepFromLucene(
			ArrayList<TermDoc> trainingSet,
			ArrayList<TermDoc> testSet,
			HashMap<String, Integer> typeSet)  {
		
		try {
			Directory dir1 = FSDirectory.open(new File(indexDir));
			Directory dir2 = FSDirectory.open(new File(indexDir2C));
			IndexReader trainingSetReader = IndexReader.open(dir1);
			IndexReader testSetReader = IndexReader.open(dir2);
			System.err.println("预处理1：从Lucene获取每篇文档的tf---（训练集）开始"
					+ formatter.format(new Date()));
			TermDoc td = null;
			List<TermFreq> tfList = null;
			TermFreq tf = null;
			for (int i = 0; i < trainingSetReader.numDocs(); i++) {
				td = new TermDoc();
				// 得到tf
				TermFreqVector trainingSetTermVector = trainingSetReader
						.getTermFreqVector(i, "subject");
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
				td.setFileTitle(trainingSetReader.document(i).get("title"));
				String tp = trainingSetReader.document(i).get("type");
				td.setFileType(tp);
				td.setFilePath(trainingSetReader.document(i).get("fullpath"));

				trainingSet.add(td);
				if (typeSet.containsKey(tp) == false)
					typeSet.put(tp, 1);// hash<类型,文档数>
				else
					typeSet.put(tp, typeSet.get(tp) + 1);
			}
			trainingSetReader.close();
			System.err.println("预处理1：从Lucene获取每篇文档的tf---（训练集）结束"
					+ formatter.format(new Date()));
			System.err.println("预处理1：从Lucene获取每篇文档的tf---（测试集）开始"
					+ formatter.format(new Date()));
			for (int i = 0; i < testSetReader.numDocs(); i++) {
				td = new TermDoc();
				// 得到tf
				TermFreqVector testSetTermVector = testSetReader
						.getTermFreqVector(i, "subject");
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
				td.setFileTitle(testSetReader.document(i).get("title"));
				td.setFileType(testSetReader.document(i).get("type"));
				td.setFilePath(testSetReader.document(i).get("fullpath"));
				testSet.add(td);
			}
			testSetReader.close();
			System.err.println("预处理1：从Lucene获取每篇文档的tf---（测试集）结束"
					+ formatter.format(new Date()));
		} catch (Exception e) {

		}
	}
	/**
	 * 得出词汇，文档对dfSet
	 * @param trainingSet	训练集
	 * @return				dfSet
	 */
	private ArrayList<DocFreq> trainingDFSet(ArrayList<TermDoc> trainingSet) {
		ArrayList<DocFreq> dfSet = new ArrayList<DocFreq>();
		System.err.println("预处理2：获取DocFreq---开始" + formatter.format(new Date()));
		for (int i = 0; i < trainingSet.size(); i++) {
			System.err.println("training:" + trainingSet.get(i).getFileTitle()
					+ trainingSet.get(i).getFileType());
			for (int j = 0; j < trainingSet.get(i).getTermFreq().size(); j++) {
				boolean isIn = false;
				for (int k = 0; k < dfSet.size(); k++)
					if (dfSet.get(k).getTerm().equals(trainingSet.get(i).getTermFreq().get(j).getTerm())) {
						dfSet.get(k).setDf(dfSet.get(k).getDf()+1);
						dfSet.get(k).computeType(trainingSet.get(i).getFileType());
						isIn = true;
						break;
					}
				if (!isIn) {
					DocFreq tmp = new DocFreq(trainingSet.get(i).getTermFreq().get(j).getTerm(), 1);
					tmp.computeType(trainingSet.get(i).getFileType());
					dfSet.add(tmp);
				}
			}
		}
		System.err.println("预处理2：获取DocFreq---结束" + formatter.format(new Date()));
		return dfSet;
	}
	/**
	 * 精简dfSet
	 * @param dfSet				词汇，文档对
	 * @param dfLowerLimit		最低文档频率
	 * @param typeUpperLimit	最高类别频率
	 */
	private void MiddleLeft(ArrayList<DocFreq> dfSet, int dfLowerLimit,
			int typeUpperLimit) {
		System.err.println("预处理3：筛选---开始" + formatter.format(new Date()));
		for (int i = 0; i < dfSet.size(); i++) {
			if (dfSet.get(i).getDf() <= dfLowerLimit
					|| dfSet.get(i).getTypeCount() > typeUpperLimit)
				dfSet.remove(i--);
		}
		System.err.println("预处理3：筛选---结束" + formatter.format(new Date()));
	}
	/**
	 * 根据dfSet精简训练集中每篇文档的词汇量
	 * @param Set		训练集
	 * @param dfSet		词汇，文档对
	 */
	public void ReduceSet(
			ArrayList<TermDoc> Set,
			ArrayList<DocFreq> dfSet) {
		System.err.println("预处理4：筛trainingSet,testSet---开始"
				+ formatter.format(new Date()));
		List<TermFreq> ltv =null; 
		TermFreq tv = null;
		for (int i = 0; i < Set.size(); i++) {
			ltv=new ArrayList<TermFreq>();
			for (int k1 = 0; k1 < dfSet.size(); k1++) {
				tv = new TermFreq();//(dfSet.get(k1).getTerm(), 0);
				tv.setTerm(dfSet.get(k1).getTerm());
				tv.setFreq(0);
				ltv.add(tv);
			}
			System.err.print(Set.get(i).getFileTitle()
					+ Set.get(i).getFileType() + ":");
			System.err.print(Set.get(i).getTermFreq().size() + "->");
			for (int j = 0; j < Set.get(i).getTermFreq().size(); j++) {
				boolean isIn = false;
				for (int k = 0; k < dfSet.size(); k++) {
					if (dfSet.get(k).getTerm()
							.equals(Set.get(i).getTermFreq().get(j).getTerm())) {
						TermFreq tmp=new TermFreq();
						tmp.setTerm(dfSet.get(k).getTerm());
						tmp.setFreq(Set.get(i).getTermFreq().get(j).getFreq());
						ltv.set(k, tmp);
						isIn = true;
						break;
					}
				}
				if (isIn == false)
					Set.get(i).getTermFreq().remove(j--);
			}
			// 训练集中文档i的有效term的数量
			System.err.println(Set.get(i).getTermFreq().size());
			System.err.flush();
			
			Set.get(i).setTermFreq(ltv);
		}
		System.err.println("预处理4：筛trainingSet,testSet---结束"
				+ formatter.format(new Date()));
	}

	Plsa_EM plsa_em = new Plsa_EM();
	P_z_d_w pzdw=new P_z_d_w();
	Map<Integer, String> cat = new HashMap<Integer, String>();//训练后z对应的类别顺序
	/**
	 * PLSA训练
	 * @param trainingSet	训练集
	 * @param typeSet		类别集
	 * @param termNum		词汇量
	 * @param docNum		文档数目
	 * @param Par			EM算法参数
	 */
	public void doplsa(ArrayList<TermDoc> trainingSet, HashMap<String, Integer> typeSet, int termNum,
			int docNum, Para Par,String mongoHost) {
		int typeNum=typeSet.size();
		Matrix tf = new Matrix(termNum, docNum);
		for (int i = 0; i < docNum; i++) {
			for (int j = 0; j < termNum; j++) {
				tf.set(j, i, trainingSet.get(i).getTermFreq().get(j).getFreq());
			}
		}
		//初始化Pz、Pw_z、Pd_z
		pzdw = plsa_em.pLSA_init(termNum, docNum, typeNum);
		//EM算法
		pzdw = plsa_em.pLSA_EM(tf, typeNum, Par, pzdw, false);
		Matrix diagPz=new Matrix(typeNum,typeNum,0);
		for(int i=0;i<typeNum;i++){
			diagPz.set(i, i, pzdw.getPz().get(0, i));
		}
		//P(z|d)
		Matrix Pz_d=pzdw.getPd_z().times(diagPz);
		//训练结束后，判断每个分组的类别
		cat = label(Pz_d, trainingSet,typeSet);
		//转化Pz、Pw_z和cat，以便存入数据库
		Matrix pz=pzdw.getPz();
		Matrix pw_z=pzdw.getPw_z();
		List<String> pzl=change2List(pz);
		List<String> pw_zl=change2List(pw_z);
		Pz pze=new Pz(pzl);
		Pw_z pw_ze=new Pw_z(pw_zl);
		List<Type> lt=new ArrayList<Type>();
		for(int i=0;i<cat.size();i++){
			Type type=new Type(i,cat.get(i));
			lt.add(type);
		}
		//存储Pz、Pw_z和cat
		try {
			Mongo mongo = new Mongo(mongoHost, 27017);
			Morphia morphia = new Morphia();
			Datastore datastore = morphia.createDatastore(mongo, "genius");
			datastore.delete(datastore.createQuery(Pw_z.class));
			datastore.save(pw_ze);
			datastore.delete(datastore.createQuery(Pz.class));
			datastore.save(pze);
			datastore.delete(datastore.createQuery(Type.class));
			datastore.save(lt);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * PLSA测试
	 * @param testSet	测试集
	 * @param termNum	词汇量
	 * @param typeNum	类别数目
	 * @return			测试结果
	 */
	public List<DocType> doplsa_test(ArrayList<TermDoc> testSet,int termNum,int typeNum) {
		Matrix tf_test = new Matrix(termNum, 1);
		List<DocType> dtList=new ArrayList<DocType>();
		int[] match = new int[typeNum];// 检索出相关的
		int[] retrieval = new int[typeNum];// 检索出的
		int[] relevant = new int[typeNum];// 相关的
		for (int i = 0; i < testSet.size(); i++) {
			for (int j = 0; j < termNum; j++) {
				tf_test.set(j, 0, testSet.get(i).getTermFreq().get(j).getFreq());
			}
			// 执行测试
			int z_d = plsa_em.folding_in(tf_test, pzdw.getPz(), pzdw.getPw_z());

			String cat_test = cat.get(z_d);
			DocType dt=new DocType();
			dt.setDocTitle(testSet.get(i).getFileTitle());
			dt.setDocType(cat_test);
			dt.setDocPath(testSet.get(i).getFilePath());
			dtList.add(dt);
			System.err.println(testSet.get(i).getFileType() + "-->" + cat_test);
			if (cat_test.equals(testSet.get(i).getFileType())) {
				match[z_d]++;
				relevant[z_d]++;
			} else {
				for (int k = 0; k < typeNum; k++) {
					if (testSet.get(i).getFileType().equals(cat.get(k))) {
						relevant[k]++;
						break;
					}
				}
			}
			retrieval[z_d]++;
		}
//		ds.delete(ds.createQuery(DocType.class));
//		ds.save(dtList);
		int sum = 0;
		for (int l = 0; l < typeNum; l++) {
			sum += match[l];
			double recall=(double)(match[l]) / 5;
			double precision=(double)match[l] / retrieval[l];
			System.err.println(cat.get(l) + "的召回率为：" + recall
					+ ", 查准率为：" + precision);
		}
		System.err.println("总查准率为：" + (double)sum / testSet.size());
		return dtList;
	}
	/**
	 * 训练结束后，判断每个分组的类别
	 * @param Pz_d			矩阵Pz_d
	 * @param trainingSet	训练集
	 * @param typeSet		类别集
	 * @return				分组的类别
	 */
	public Map<Integer, String> label(Matrix Pz_d,
			ArrayList<TermDoc> trainingSet,HashMap<String, Integer> typeSet) {
		int docNum = Pz_d.getRowDimension();
		int typeNum = Pz_d.getColumnDimension();
		Map<Integer, String> cat = new HashMap<Integer, String>();
		Map<Integer, String> cat_train = new HashMap<Integer, String>();
		Object[] types=typeSet.keySet().toArray();
		for(int i=0;i<types.length;i++){
			cat.put(i, types[i].toString());
		}
		
		double[] d=new double[docNum];
		for(int i=0;i<docNum;i++){
			d[i]=max(Pz_d.getMatrix(i, i, 0, typeNum-1).getRowPackedCopy());
		}
		int[] c =null; 
		for (int i = 0; i < typeNum; i++) {
			c=new int[typeNum];
			for (int j = 0; j < docNum; j++) {
				if(d[j]==i){
					for (int k = 0; k < 10; k++) {
						if ((trainingSet.get(j).getFileType()).equals(cat.get(k))) {
							c[k] ++;//= Pd_z.get(j, i);
							break;
						}
					}
				}
			}
			for(int x=0;x<typeNum;x++){
				System.err.print(c[x]+" ");
			}
			System.err.println(" ");
			int l = max(c);
			cat_train.put(i, cat.get(l));
			System.err.println(i+" "+cat_train.get(i));
		}
		return cat_train;
	}
	/**
	 * 求数组的最大数所在的位置
	 * @param a		数组
	 * @return		最大数所在的位置
	 */
	public int max(double[] a) {
		int m = 0;
		for (int i = 1; i < a.length; i++) {
			if (a[m] < a[i])
				m = i;
		}
		return m;
	}
	public int max(int[] a) {
		int m = 0;
		for (int i = 1; i < a.length; i++) {
			if (a[m] < a[i])
				m = i;
		}
		return m;
	}
	/**
	 * 把矩阵转换成List，以便存入数据库
	 * @param matrix	要转换的矩阵
	 * @return			转换结果
	 */
	public List<String> change2List(Matrix matrix){
		List<String> ls=new ArrayList<String>();
		int m=matrix.getRowDimension();
		int n=matrix.getColumnDimension();
		for(int i=0;i<m;i++){
			double[] d=matrix.getMatrix(i,i, 0, n-1).getRowPackedCopy();
			StringBuilder strb=new StringBuilder();
			for(int j=0;j<d.length;j++){
				strb.append(d[j]);
				strb.append(",");
			}
			String string=strb.toString();
			ls.add(string);
		}
		return ls;
	}
	/**
	 * 把从数据库中取出的List，转换为矩阵
	 * @param ls	要转换的List
	 * @return		转换结果
	 */
	public Matrix change2Matrix(List<String> ls){
		String[] st=ls.get(0).split(",");
		double[][] da=new double[ls.size()][st.length];
		for(int i=0;i<ls.size();i++){
			String string=ls.get(i);
			String[] s=string.split(",");
			for(int j=0;j<s.length;j++){
				da[i][j]=Double.parseDouble(s[j]);				
			}
		}
		Matrix matrix=new Matrix(da);
		return matrix;
	}
	
	public Plsa_EM getPlsa_em() {
		return plsa_em;
	}

	public void setPlsa_em(Plsa_EM plsa_em) {
		this.plsa_em = plsa_em;
	}

	public P_z_d_w getPzdw() {
		return pzdw;
	}

	public void setPzdw(P_z_d_w pzdw) {
		this.pzdw = pzdw;
	}
	
	public static void main(String[] args) throws IOException {
		PLSA plsa=new PLSA();
		String indexDir = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir1";
		String dataDir = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\dataDir1";
		String indexDir2C = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir2C1";
		String dataDir2C = "D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\DataDir2C1";
		
		File file1 = new File("D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\indexDir2C1");
		if (file1.exists() == false)
			file1.mkdir();
		File file2 = new File("D:\\Program Files\\eclipse\\workspace\\genius-analysis\\trunk\\data\\DataDir2C1");
		if (file2.exists() == false)
			file2.mkdir();
		plsa.BuildReader(indexDir, dataDir);// index training
		plsa.BuildReader(indexDir2C, dataDir2C);// index test

		ArrayList<TermDoc> trainingSet = new ArrayList<TermDoc>();
		ArrayList<TermDoc> testSet = new ArrayList<TermDoc>();
		HashMap<String, Integer> typeSet = new HashMap<String, Integer>();;// <类型，文档数>

		System.err.println("程序开始：" + formatter.format(new Date()));

		// 预处理1：从Lucene获得每篇文档的tf
		// IndepFromLucene(trainingSetReader, testSetReader,
		// trainingSet,testSet, typeSet);
		plsa.IndepFromLucene(trainingSet, testSet, typeSet);
		plsa.training(trainingSet, typeSet, "10.1.0.171");
//		plsa.classify();

	}
}
