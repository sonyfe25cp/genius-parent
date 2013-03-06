package edu.bit.dlde.analysis.classification_jxt_forweb;

import java.util.*;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;


@Entity(value="TrainingFileProperty",noClassnameStored=true)
public class FileProperty {
	
@Embedded
List<TermVector> termVectorSet = new ArrayList<TermVector>();
String id = null;
String type = null;
String author = null;
String title = null;

public FileProperty(List<TermVector> termVectorSet,String id,String type,String author,String title){
	this.termVectorSet = termVectorSet;
	this.id = id;
	this.type = type;
	this.author = author;
	this.title = title;
}
public FileProperty(String id,String type,String author,String title){
	this.id = id;
	this.type = type;
	this.author = author;
	this.title = title;
}
public FileProperty(){
}

public void AddTermVector(String word,int tf,String wordType)
{
	TermVector tv = new TermVector(word, tf, wordType);
	termVectorSet.add(tv);
}

//set&get
public List<TermVector> getTermVectorSet(){
	return termVectorSet;
}
public void setTermVectorSet(List<TermVector> termVectorSet){
	this.termVectorSet = termVectorSet;
}
public String getId(){
	return id;
}
public void setId(String id){
	this.id = id;
}
public String getType(){
	return type;
}
public void setType(String type){
	this.type = type;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public String getTitle(){
	return title;
}
public void setTitle(String title){
	this.title = title;
}
//end get&set
}
