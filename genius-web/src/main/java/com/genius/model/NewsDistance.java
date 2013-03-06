package com.genius.model;
import edu.bit.dlde.recommender.adapter.DLDESimilarityAdapter;
import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Distance;
import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Item;

	
public class NewsDistance implements Distance{
	private static DLDESimilarityAdapter dsa= new DLDESimilarityAdapter();
	@Override
	public Double getDistance(Item i1, Item i2) {
		// TODO Auto-generated method stub
		return 1-dsa.getSimilarity((NewsReport)i1, (NewsReport)i2);
	}
}