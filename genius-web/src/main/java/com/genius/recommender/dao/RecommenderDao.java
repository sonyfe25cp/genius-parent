/**
 * 
 */
package com.genius.recommender.dao;

import java.util.List;

import com.genius.model.NewsReport;
import com.genius.model.User;
import com.genius.recommender.model.XnewsCluster;


/**
 * 与推荐相关的数据库查询接口
 * @author horizon
 *
 */
public interface RecommenderDao {
	
	/**
	 * 创建或更新一条推荐得分记录，记录不存在是创建该用户
	 * @param user 推荐的对象
	 * @param xnews 推荐的内容
	 * @param score 推荐的得分
	 */
	public boolean PutScoreItem(User user,NewsReport xnews,Double score);
	//public ScoreItem GetScoreItem(User user,Xnews xnews,Double score);
	/**
	 * 获取特定用户的推荐新闻计数
	 * @param user 推荐的对象
	 */
	public long GetSize(User user);
	
	/**
	 * 获取特定用户的前count条推荐新闻
	 * @param count 获取数目
	 * @param user 推荐的对象
	 */
	public List<NewsReport> GetTopScoreItems(int count,User user);
	/**
	 * 获取特定用户从offset处开始的count条推荐新闻，即按得分降序排在offset到offset+count之间的推荐新闻
	 * @param count 获取数目
	 * @param user 推荐的对象
	 */
	public List<NewsReport> GetTopScoreItems(int count,User user,int offset);
	
	/**
	 * 更新一个新闻集群，不存在时创建
	 * @param xc 需要更新的新闻集群
	 * @return
	 */
	public boolean PutXnewsCluster(XnewsCluster xc);
	/**
	 * 删除一个集群
	 * @param xc 需要更新的新闻集群
	 * @return
	 */
	public void CleanXnewsCluster(XnewsCluster xc);
	/**
	 * 删除一个集群
	 * @param xc 需要更新的新闻集群的id
	 * @return
	 */
	public void CleanXnewsCluster(String clusterId);
	
	/**
	 * 按id号获取新闻集群
	 * @param clusterID 目标集群id号
	 * @return
	 */
	public XnewsCluster GetXnewsClusterById(String clusterID);
	
	/**
	 * 获取所有新闻集群
	 */
	public List<XnewsCluster> GetAllXnewsClusters();
	/**
	 * 关闭数据库，释放资源
	 */
	public void close();
	
	/**
	 * 清除所有用户user的推荐得分记录
	 * @param user 目标用户
	 * @return
	 */
	boolean CleanScoreItems(User user);
	
}
