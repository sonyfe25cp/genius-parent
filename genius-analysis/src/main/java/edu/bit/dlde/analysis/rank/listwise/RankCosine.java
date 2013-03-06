package edu.bit.dlde.analysis.rank.listwise;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.bit.dlde.analysis.model.LetorModel;
import edu.bit.dlde.math.VectorCompute;

public class RankCosine {

	public RankCosine() {

	}
	
	public Map<Integer,List<LetorModel>> wholeData;
	
	private double lossAll=Double.MAX_VALUE;
	
	private double finalRankFunction(){
		double lossThis=0;
		for(Entry<Integer,List<LetorModel>> entry:wholeData.entrySet()){
			int qId=entry.getKey();
			List<LetorModel> relDocs=entry.getValue();
			WeakLearner4RankCosine learner=new WeakLearner4RankCosine();
			learner.setModelList(relDocs);
			learner.train();
			double lossTmp=learner.getMinLoss();
			lossTmp+=lossTmp;
		}
		return lossThis;
	}
	
	private void train(){
		
		while(true){
			
		}
	}
	
	public double lossFunction(double[] labelData, double[] computeData) {// 公式6

		double cosineValue = VectorCompute.cosValue(labelData, computeData);

		double lf = (1 - cosineValue) / 2;

		return lf;
	}
}

class WeakLearner4RankCosine {
	private int times = 2000000;
	private double[] init = new double[136];
	private double minLoss = Double.MAX_VALUE;
	private List<LetorModel> modelList;
	private double[] computeData;// 同一个qid的 relevance结果
	private double[] labelData;// 同一个qId的 relevance 标注
								// 1 3
								// 1 3

	public double rankFunction(double[] vector) {
		double rf = VectorCompute.dotMulti(init, vector);
		return rf;
	}

	public void update() {// 變更 rank function

	}

	public double[] computeList(List<LetorModel> modelList) {
		double[] cd = new double[modelList.size()];
		int i = 0;
		for (LetorModel model : modelList) {
			cd[i] = rankFunction(model.getVector());
			i++;
		}
		return cd;
	}

	public double lossFunction(double[] labelData, double[] computeData) {// 公式6

		double cosineValue = VectorCompute.cosValue(labelData, computeData);

		double lf = (1 - cosineValue) / 2;

		return lf;
	}

	public void train() {
		double[] cl = computeList(modelList);
		double loss = lossFunction(labelData, cl);
		if (loss < minLoss) {
			update();
		}
	}

	public void go() {
		int i = 0;
		while (i < times) {
			train();
			i++;
		}
	}

	public void setModelList(List<LetorModel> modelList) {
		int i = 0;
		labelData = new double[modelList.size()];
		for (LetorModel model : modelList) {
			labelData[i] = model.getRelevance();
			i++;
		}
		this.modelList = modelList;
	}

	public double getMinLoss() {
		return minLoss;
	}

	public void setMinLoss(double minLoss) {
		this.minLoss = minLoss;
	}
}
