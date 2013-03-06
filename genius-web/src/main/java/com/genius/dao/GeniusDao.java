package com.genius.dao;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.genius.model.Giftable;
import com.genius.model.NewsReport;
import com.genius.model.QueryLog;
import com.genius.utils.Page;

public interface GeniusDao {

	public void saveNewsReport(NewsReport newsReport);

	public void deleteNewsReport(ObjectId id);

	public List<NewsReport> loadNewsReportsInRange(Date beginDate, Date endDate);

	public NewsReport loadNewsReportByUrl(String url);

	public List<NewsReport> loadNewsReportsInRange(Page page);

	public NewsReport loadNewsReportByID(String id);

	public boolean save(QueryLog log);

	public List<QueryLog> loadQueryLogInRange(Page page);

	public Giftable loadGiftableByUrl(String url);

	public void saveGiftable(Giftable giftable);

	List<QueryLog> loadQueryLogs();

	NewsReport loadNewsReportByID(ObjectId id);
	
	public long getGiftableCount();
}
