package edu.bit.dlde.analysis.classification;
import java.util.*;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

@Entity("idfSet")
public class generalizedDF {
public String word;
public double df;
private int typeCount;
@Embedded
public ArrayList<SSPair> map2File;
@Embedded
public HashSet<String> typeRelated;
public boolean delete = true;

public generalizedDF(String word,double df)
    {
	    this.word = word;
	    this.df = df;
	    map2File = new ArrayList<SSPair>();
	    typeCount = 0;
	    typeRelated = new HashSet<String>();
	    delete = true;
	}

public generalizedDF(){}

public void insertMap(String fileName,String fileType,int filePosition)
   {
	boolean isIn = false;
	for(int i=0;i<map2File.size();i++)
	{
		if(map2File.get(i).fileType.equals(fileType))
		isIn = true;
		break;
	}
	   if(isIn == false)
	 	   typeCount++;
 	   map2File.add(new SSPair(fileName, fileType,filePosition));
   }
public int getTypeCount()
   {
      return typeCount;	
   }
}
