package edu.bit.dlde.analysis.classification_jxt_forweb;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value="ClassificationResult",noClassnameStored=true)
public class ClassificationResult {
	String id = null;
	String type = null;
	String author = null;
	String title = null; 

public ClassificationResult(String id,String type,String author,String title){
	this.id = id;
	this.type = type;
	this.author = author;
	this.title = title;
}
public ClassificationResult(){
	}
public String getTitle(){
	return this.title;
}
public void setTitle(String title){
	this.title = title;
}
public String getType(){
	return this.type;
}
public void setType(String type){
	this.type = type;
}
public String getId(){
	return this.id;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public void setId(String id) {
	this.id = id;
}

public static void main(String[] args)
{
	System.out.println("1");}
}
