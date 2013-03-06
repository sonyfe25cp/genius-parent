package edu.bit.dlde.analysis.plsa;

import java.util.ArrayList;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;

/**
 *	@author zhangchangmin
 **/
@Entity
public class DocFreq {

	@Indexed
	private String term;
	@NotSaved
	private int df;
	@NotSaved
	private int typeCount;
	@NotSaved
	private ArrayList<String> typename=new ArrayList<String>();
	public DocFreq(){
		
	}
	public DocFreq(String term){
		this.term=term;
	}
	public DocFreq(String term, int df) {
		this.term = term;
		this.df = df;
		typeCount = 0;
	}
	
	public void computeType(String fileType) {
		boolean isIn = false;
		for (int i = 0; i < typename.size(); i++) {
			if (typename.get(i).equals(fileType)){
				isIn = true;
				break;
			}				
		}
		if (isIn == false){
			typeCount++;//某一word所在的类别的数目
			typename.add(fileType);
		}
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getDf() {
		return df;
	}
	public void setDf(int df) {
		this.df = df;
	}
	public int getTypeCount() {
		return typeCount;
	}
	public void setTypeCount(int typecount){
		this.typeCount=typecount;
	}
}
