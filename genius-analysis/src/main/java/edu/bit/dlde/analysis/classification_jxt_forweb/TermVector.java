package edu.bit.dlde.analysis.classification_jxt_forweb;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

@Embedded
public class TermVector {
String word;
int tf;
double idf;
String wordType;

public TermVector(String word,int tf,String wordType)
{
	this.word = word;
	this.tf = tf;
	this.wordType = wordType;
}
public TermVector(){}

//get&set
public String getWord(){
	return word;
}
public void setWord(String word){
	this.word = word;
}
public int getTf(){
	return tf;
}
public void setTf(int tf){
	this.tf = tf;
}public double getIdf(){
	return idf;
}
public void setIdf(double idf){
	this.idf = idf;
}
public String getWordType(){
	return wordType;
}
public void setWordType(String wordType){
	this.wordType = wordType;
}
}
