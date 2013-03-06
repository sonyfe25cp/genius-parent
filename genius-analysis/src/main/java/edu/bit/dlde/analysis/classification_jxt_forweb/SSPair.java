package edu.bit.dlde.analysis.classification_jxt_forweb;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class SSPair {
String fileName;
String fileType;
int filePosition;
public SSPair(String fileName,String fileType,int filePosition)
    {
	    this.fileName = fileName;
	    this.fileType = fileType;
	    this.filePosition=filePosition;
	}
public SSPair(){};
}
