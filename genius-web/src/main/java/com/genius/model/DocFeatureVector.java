package com.genius.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 该类用来保存文档特征向量
 * 
 * @author lins
 * @date 2012-6-3
 **/
@Entity
public class DocFeatureVector {
	@Id
	private ObjectId id = null;
	@Indexed
	private String url;
	private Date updateDate;
	private String[] termsInBody;
	private int[] occurenceInBody;
	private String[] termsInAnchor;
	private int[] occurenceInAnchor;
	private String[] termsInTitle;
	private int[] occurenceInTitle;
	private String[] termsInUrl;
	private int[] occurenceInUrl;
	private String[] termsInWhole;
	private int[] occurenceInWhole;
	// length of body, anchor, title, url ,whole
	private int[] length = new int[5];

	// private int[] idf = new int[5];

	public DocFeatureVector() {
		updateDate = new Date();
	}

	public DocFeatureVector(String url) {
		this.url = url;
		updateDate = new Date();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String[] getTermsInBody() {
		return termsInBody;
	}

	public void setTermsInBody(String[] termsInBody) {
		this.termsInBody = termsInBody;
	}

	public int[] getOccurenceInBody() {
		return occurenceInBody;
	}

	public void setOccurenceInBody(int[] occurenceInBody) {
		this.occurenceInBody = occurenceInBody;
	}

	public void setOccurenceInBody(Integer[] occurenceInBody) {
		this.occurenceInBody = new int[occurenceInBody.length];
		int i = 0;
		for (Integer o : occurenceInBody) {
			this.occurenceInBody[i] = o;
			i++;
		}
	}

	public String[] getTermsInAnchor() {
		return termsInAnchor;
	}

	public void setTermsInAnchor(String[] termsInAnchor) {
		this.termsInAnchor = termsInAnchor;
	}

	public int[] getOccurenceInAnchor() {
		return occurenceInAnchor;
	}

	public void setOccurenceInAnchor(int[] occurenceInAnchor) {
		this.occurenceInAnchor = occurenceInAnchor;
	}

	public String[] getTermsInTitle() {
		return termsInTitle;
	}

	public void setTermsInTitle(String[] termsInTitle) {
		this.termsInTitle = termsInTitle;
	}

	public int[] getOccurenceInTitle() {
		return occurenceInTitle;
	}

	public void setOccurenceInTitle(int[] occurenceInTitle) {
		this.occurenceInTitle = occurenceInTitle;
	}

	public String[] getTermsInUrl() {
		return termsInUrl;
	}

	public void setTermsInUrl(String[] termsInUrl) {
		this.termsInUrl = termsInUrl;
	}

	public int[] getOccurenceInUrl() {
		return occurenceInUrl;
	}

	public void setOccurenceInUrl(int[] occurenceInUrl) {
		this.occurenceInUrl = occurenceInUrl;
	}

	public String[] getTermsInWhole() {
		return termsInWhole;
	}

	public void setTermsInWhole(String[] termsInWhole) {
		this.termsInWhole = termsInWhole;
	}

	public int[] getOccurenceInWhole() {
		return occurenceInWhole;
	}

	public void setOccurenceInWhole(int[] occurenceInWhole) {
		this.occurenceInWhole = occurenceInWhole;
	}

	public int[] getLength() {
		return length;
	}

	public void setLength(int[] length) {
		this.length = length;
	}

	public int getBodyLength() {
		return length[0];
	}

	public void setBodyLength(int l) {
		length[0] = l;
	}

	public int getAnchorLength() {
		return length[1];
	}

	public void setAnchorLength(int l) {
		length[1] = l;
	}

	public int getTitleLength() {
		return length[2];
	}

	public void setTitleLength(int l) {
		length[2] = l;
	}

	public int getUrlLength() {
		return length[3];
	}

	public void setUrlLength(int l) {
		length[3] = l;
	}

	public int getWholeLength() {
		return length[4];
	}

	public void setWholeLength(int l) {
		length[4] = l;
	}
}
