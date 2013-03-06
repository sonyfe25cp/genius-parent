package edu.bit.dlde.analysis.plsa;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 *	@author zhangchangmin
 **/
@Entity
public class Pz {

	@Id
	private String id;
	@Embedded
	private List<String> ls;
	public Pz(){
		
	}
	public Pz(List<String> ls){
		this.ls=ls;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getLs() {
		return ls;
	}
	public void setLs(List<String> ls) {
		this.ls = ls;
	}
}
