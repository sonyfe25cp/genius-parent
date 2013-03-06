package edu.bit.dlde.analysis.classification;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class SSPair {
public String fileName;
public String fileType;
public int filePosition;
public SSPair(String fileName,String fileType,int filePosition)
    {
	    this.fileName = fileName;
	    this.fileType = fileType;
	    this.filePosition=filePosition;
	}
public SSPair(){};
}
