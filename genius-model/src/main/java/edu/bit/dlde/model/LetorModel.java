package edu.bit.dlde.model;

public class LetorModel {

	private int id;//序号
	private int relevance;//相关度
	private int qId;//查询号
	private double[] vector;//特征向量
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("id:"+id+",");
		sb.append("relevance:"+relevance+",");
		sb.append("qid:"+qId+",");
		sb.append("vector:[");
		for(int i=1;i<=vector.length;i++){
			sb.append(i+":"+vector[i-1]+",");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	public int getqId() {
		return qId;
	}
	public void setqId(int qId) {
		this.qId = qId;
	}
	public double[] getVector() {
		return vector;
	}
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	
	
}
