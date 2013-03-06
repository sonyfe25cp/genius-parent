package edu.bit.dlde.analysis.plsa;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 *	@author zhangchangmin
 **/
public class Type {

	@Id
	private String id;
	@Indexed
	private int key;
	@Indexed
	private String type;
	public Type(){
		
	}
	public Type(int key,String type){
		this.key=key;
		this.type=type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
