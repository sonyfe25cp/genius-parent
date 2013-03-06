package com.genius.utils;

public class Page {

	private int pageNo;
	private int pageSize;
	private int total;
	
	private int beginNum;
	private int endNum;
	
	private int showPageNoBegin;
	private int showpageNoEnd;
	
	private int totalP;
	
	public final static int defaultPageSize=10;
	
	public Page(int pageNo) {
		super();
		this.pageNo = pageNo;
		this.pageSize=defaultPageSize;
		calculate();
	}
	public Page(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		calculate();
	}
	
	public void calculate(){
		this.beginNum=pageNo==0?0:(pageNo-1)*pageSize;
		this.endNum=pageNo*pageSize;
		this.showPageNoBegin=1;
		this.showpageNoEnd=1;
	}
	
	public void calculateForShow(){
		
		this.totalP=(total%pageSize==0)?(total/pageSize):((total/pageSize)+1);
		if(totalP==0){
			this.showPageNoBegin=1;
			this.showpageNoEnd=1;
		}else{
			int b=(pageNo-5)>0?(pageNo-5):1;
			this.showPageNoBegin=b;
			int e=(b+10)>totalP?totalP:(b+10);
			this.showpageNoEnd=e;
		}
	}
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		calculate();
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calculate();
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
		calculateForShow();
	}
	public int getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(int beginNum) {
		this.beginNum = beginNum;
	}
	public int getEndNum() {
		return endNum;
	}
	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
	public int getShowPageNoBegin() {
		return showPageNoBegin;
	}
	public void setShowPageNoBegin(int showPageNoBegin) {
		this.showPageNoBegin = showPageNoBegin;
	}
	public int getShowpageNoEnd() {
		return showpageNoEnd;
	}
	public void setShowpageNoEnd(int showpageNoEnd) {
		this.showpageNoEnd = showpageNoEnd;
	}
	public int getTotalP() {
		return totalP;
	}
	public void setTotalP(int totalP) {
		this.totalP = totalP;
	}
}
