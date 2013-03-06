package com.genius.dao;

import com.genius.model.DocFeatureVector;

/**
 *
 *@author lins
 *@date 2012-6-3
 **/
public interface IDocFeatureVectorDAO {
	public DocFeatureVector getFVByUrl(String url);
	public void saveDocFeatureVector(DocFeatureVector docFV);
}
