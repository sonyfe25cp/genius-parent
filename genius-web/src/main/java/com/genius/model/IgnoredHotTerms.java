package com.genius.model;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 *
 *@author lins
 *@date 2012-5-22
 **/
@Entity
public class IgnoredHotTerms {
	@Id
	private ObjectId id = null;

	// 词
	private Collection<String> terms;

	public Collection<String> getTerms() {
		return terms;
	}

	public void setTerms(Collection<String> terms) {
		this.terms = terms;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
