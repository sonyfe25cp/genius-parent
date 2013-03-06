package edu.bit.dlde.analysis.classification;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class SDPair implements Comparable<SDPair> {
String str;
Double distance;

public SDPair(String str,double distance)
    {
	   this.str = str;
	   this.distance = distance;
    }
public int compareTo(SDPair obj)
    {
	    return this.distance.compareTo(obj.distance);
    }
public SDPair(){}
}
