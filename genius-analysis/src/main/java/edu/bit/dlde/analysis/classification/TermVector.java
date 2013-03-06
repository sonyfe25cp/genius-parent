package edu.bit.dlde.analysis.classification;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class TermVector {
public String word;
public int tf;
public double idf;
public String wordType;

public TermVector(String word,int tf,String wordType)
{
	this.word = word;
	this.tf = tf;
	this.wordType = wordType;
}
public TermVector(){}
}
