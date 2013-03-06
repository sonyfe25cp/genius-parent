package edu.bit.dlde.analysis.classification;

public class Result {
public String title = null;
public String type = null;
public String path = null;
public Result(String title,String type,String path){
	this.title = title;
	this.type = type;
	this.path = path;
}
public Result(){
	}
public String getTitle(){
	return title;
}
public void setTitle(String title){
	this.title = title;
}
public String getType(){
	return type;
}
public void setType(String type){
	this.type = type;
}
public String getPath(){
	return path;
}
public void setPath(String path){
	this.path = path;
}
}
