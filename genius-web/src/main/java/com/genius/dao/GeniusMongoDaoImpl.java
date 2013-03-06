package com.genius.dao;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.genius.model.ExtractConfiguration;
import com.genius.model.ForumPost;
import com.genius.model.Giftable;
import com.genius.model.NewsReport;
import com.genius.model.QueryLog;
import com.genius.model.User;
import com.genius.utils.Page;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 建议对所有能转换成gift的对象的数据库操作在这里都能找到
 */
public class GeniusMongoDaoImpl extends MongoDAOBase implements GeniusDao,
		UserDetailsService {
	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(ExtractConfiguration.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		mongo.close();
	}

	@Override
	public void saveNewsReport(NewsReport newsReport) {
		datastore.updateFirst(
				datastore.find(NewsReport.class).filter("url ==",
						newsReport.getUrl()), newsReport, true);
	}

	@Override
	public List<NewsReport> loadNewsReportsInRange(Date beginDate, Date endDate) {
		return datastore.find(NewsReport.class)
				.filter("publishTime >=", beginDate)
				.filter("publishTime <", endDate).order("-publishTime")
				.limit(500).asList();
	}

	@Override
	public NewsReport loadNewsReportByUrl(String url) {
		return datastore.find(NewsReport.class).filter("url ==", url).get();
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		return datastore.find(User.class).filter("username ==", username).get();
	}

	@Override
	public List<NewsReport> loadNewsReportsInRange(Page page) {
		String host = datastore.getDB().getMongo().getAddress().getHost();
		logger.info("host:" + host);
		Query<NewsReport> query = datastore.find(NewsReport.class)
				.order("-publishTime").offset(page.getBeginNum())
				.limit(page.getPageSize());
		List<NewsReport> list = query.asList();
		page.setTotal((int) query.countAll());
		return list;
	}

	@Override
	public NewsReport loadNewsReportByID(String id) {
		return loadNewsReportByID(new ObjectId(id));
	}

	@Override
	public NewsReport loadNewsReportByID(ObjectId id) {
		return datastore.find(NewsReport.class).filter("_id", id).get();
	}

	@Override
	public boolean save(QueryLog log) {
		datastore.save(log);
		return true;
	}

	@Override
	public List<QueryLog> loadQueryLogInRange(Page page) {
		List<QueryLog> listAll = datastore.find(QueryLog.class).asList();
		if (page != null) {
			List<QueryLog> list = datastore.find(QueryLog.class).order("-date")
					.offset(page.getBeginNum()).limit(page.getPageSize())
					.asList();
			page.setTotal(listAll.size());
			return list;
		} else {
			return listAll;
		}
	}

	@Override
	public List<QueryLog> loadQueryLogs() {
		return datastore.find(QueryLog.class).asList();
	}

	@Override
	public void deleteNewsReport(ObjectId id) {
		Query<NewsReport> query = datastore.find(NewsReport.class);
		query = query.filter("id == ", id);
		datastore.delete(query);
	}

	@Override
	public Giftable loadGiftableByUrl(String url) {
		Giftable giftable = datastore.find(NewsReport.class)
				.filter("url ==", url).get();
		if (giftable == null)
			giftable = datastore.find(ForumPost.class).filter("url ==", url)
					.get();

		return giftable;
	}

	@Override
	public void saveGiftable(Giftable giftable) {
		if (giftable instanceof NewsReport)
			datastore.updateFirst(
					datastore.find(NewsReport.class).filter("url ==",
							giftable.getUrl()), (NewsReport) giftable, true);
		else if (giftable instanceof ForumPost) {
			datastore.updateFirst(
					datastore.find(ForumPost.class).filter("url ==",
							giftable.getUrl()), (ForumPost) giftable, true);
		}

	}

	@Override
	public long getGiftableCount() {
		return datastore.find(NewsReport.class).countAll()
				+ datastore.find(ForumPost.class).countAll();
	}
}
