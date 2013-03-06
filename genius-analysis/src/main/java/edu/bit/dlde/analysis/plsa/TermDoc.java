package edu.bit.dlde.analysis.plsa;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

/**
 *	@author zhangchangmin
 **/

//@Entity
public class TermDoc {

//	@Embedded
	private List<TermFreq> termFreq;
	private String fileTitle;
	private String fileType;	
	private String fileAuthor;
	private String filePath;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TermFreq> getTermFreq() {
		return termFreq;
	}

	public void setTermFreq(List<TermFreq> termFreq) {
		this.termFreq = termFreq;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileAuthor() {
		return fileAuthor;
	}

	public void setFileAuthor(String fileAuthor) {
		this.fileAuthor = fileAuthor;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
