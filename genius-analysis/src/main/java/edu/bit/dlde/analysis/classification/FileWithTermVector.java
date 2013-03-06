package edu.bit.dlde.analysis.classification;
import java.util.*;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
@Entity("trainingSet")
public class FileWithTermVector {
@Embedded
public List<TermVector> termVectorSet;
public String fileType;
public String title;
public String filePath;
public String id = null;



public FileWithTermVector(String title,String fileType,String filePath)
{
	this.title = title;
	this.fileType = fileType;
	this.filePath = filePath;
	termVectorSet = new ArrayList<TermVector>();
}

public FileWithTermVector(){}

public void insertTermVector(String word,int tf,String wordType)
{
	TermVector tv = new TermVector(word, tf, wordType);
	termVectorSet.add(tv);
}
}
