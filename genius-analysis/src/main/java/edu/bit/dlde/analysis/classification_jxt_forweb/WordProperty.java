package edu.bit.dlde.analysis.classification_jxt_forweb;
import java.util.*;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

@Entity(value="WordProperty",noClassnameStored=true)
public class WordProperty {
String word=null;
double df;
double idf;
int typeCount;
@Embedded
ArrayList<SSPair> map2File;
@Embedded
HashSet<String> typeRelated=null;
boolean delete = true;

public WordProperty(String word,double df){
	    this.word = word;
	    this.df = df;
	    map2File = new ArrayList<SSPair>();
	    typeCount = 0;
	    typeRelated = new HashSet<String>();
	    delete = true;
}
public WordProperty(){
	
}

public void insertMap(String fileName,String fileType,int filePosition){
	boolean isIn = false;
	for(int i=0;i<map2File.size();i++){
		if(map2File.get(i).fileType.equals(fileType))
		isIn = true;
		break;
	}
	   if(isIn == false)
	 	   typeCount++;
 	   map2File.add(new SSPair(fileName, fileType,filePosition));
}

//get&set
public String getWord(){
    return word;	
}
public void setWord(String word){
	this.word = word;
}
public double getDf(){
    return df;	
}
public void setDF(double df){
	this.df = df;
}
public double getIdf(){
    return idf;	
}
public void setIdF(double idf){
	this.idf = idf;
}
public int getTypeCount(){
    return typeCount;	
}
public void setTypeCount(int typeCount){
	this.typeCount = typeCount;
}
//end get&set
}
