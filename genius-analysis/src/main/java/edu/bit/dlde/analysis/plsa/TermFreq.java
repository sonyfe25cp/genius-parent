package edu.bit.dlde.analysis.plsa;

import com.google.code.morphia.annotations.Embedded;

/**
 *	@author zhangchangmin
 **/
//@Embedded
public class TermFreq {

	private String term;
	private int freq;
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
}
