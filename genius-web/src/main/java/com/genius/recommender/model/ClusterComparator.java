package com.genius.recommender.model;
import java.util.Comparator;

	public class ClusterComparator implements Comparator<ClusterMember> {
		@Override
		public int compare(ClusterMember o1, ClusterMember o2) {
			// TODO Auto-generated method stub
			ClusterMember cm1=(ClusterMember)o1;
			ClusterMember cm2=(ClusterMember)o2;
			Double result = cm1.getSim()-cm2.getSim();
			if(result == 0 )
			{
				return 0;
			}
			else if(result >0)
			{
			return 1;
			}
			else
			{
				return -1;
			}		
		}
	}