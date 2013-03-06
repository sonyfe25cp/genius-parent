package com.genius.dao;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.genius.model.SensitiveTerm;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 用来保存，删除敏感词的mongo dao
 * 
 * @author lins
 * @date 2012-5-17
 **/
public class SensitiveTermMongoDaoImpl extends MongoDAOBase implements
		ISensitiveTermDao {

	@Override
	public void disable(String id) {
		datastore.update(
				datastore.find(SensitiveTerm.class).field("_id")
						.equal(new ObjectId(id)),
				datastore.createUpdateOperations(SensitiveTerm.class).set(
						"enabled", false));
	}

	/**
	 * useless now
	 * 
	 * @see com.genius.dao.ISensitiveTermDao#disableAll(java.lang.String[])
	 */
	@Override
	public void disableAll(String[] terms) {

	}

	/**
	 * 往里面添加新的SensitiveTerm
	 * 
	 * @see com.genius.dao.ISensitiveTermDao#add(com.genius.model.SensitiveTerm)
	 */
	@Override
	public void add(SensitiveTerm input) {
		if (input == null)
			return;

		// 寻找到包含有该term的一个记录,也就是说如果想更新那么必须给全term.term
		List<SensitiveTerm> list = datastore.find(SensitiveTerm.class).asList();

		// 假如数据库是空的话，那么直接添加
		if (list == null || list.size() == 0) {
			datastore.save(input);
			return;
		}

		// 否则的话，查看原本的那些SensitiveTerm。
		// 看新加的SensitiveTerm是不是能够扩展，缩小原本的SensitiveTerm
		for (SensitiveTerm current : list) {
			switch (current.relationWith(input.getTerm())) {
			case -1:
				// 对于input包含current的情况，需要扩展current里面的term，并且激活
				datastore.updateFirst(datastore.find(SensitiveTerm.class)
						.field("_id").equal(current.getId()),
						datastore.createUpdateOperations(SensitiveTerm.class)
								.set("term", input.getTerm()), false);
				datastore.update(
						datastore.find(SensitiveTerm.class).field("_id")
								.equal(current.getId()), datastore
								.createUpdateOperations(SensitiveTerm.class)
								.set("enabled", true));
				break;
			case 1:
				// 对于current包含input的情况，需要所小current里面的term，并且激活
				current.setTerm(input.getTerm());
				current.setEnabled(true);
				datastore.updateFirst(datastore.find(SensitiveTerm.class)
						.filter("_id", current.getId()), current, true);
				break;
			case 2:
				// 对于current等于input的情况，简单的激活
				datastore.update(
						datastore.find(SensitiveTerm.class).field("_id")
								.equal(current.getId()), datastore
								.createUpdateOperations(SensitiveTerm.class)
								.set("enabled", true));
				break;
			case 0:
				// 对于current跟input的无关西情况，直接存储
				datastore.save(input);
				break;
			}
		}
	}

	@Override
	public void addAll(Collection<SensitiveTerm> terms) {
		if (terms == null)
			return;
		for (SensitiveTerm t : terms)
			add(t);
	}

	/**
	 * 保存新的统计记录
	 * 
	 * @see com.genius.dao.ISensitiveTermDao#save(com.genius.model.SensitiveTerm)
	 */
	@Override
	public void save(SensitiveTerm term) {
		datastore
				.updateFirst(
						datastore.find(SensitiveTerm.class).filter("_id",
								term.getId()), term, true);
	}

	@Override
	public void saveAll(Collection<SensitiveTerm> terms) {
		if (terms == null)
			return;
		for (SensitiveTerm t : terms)
			save(t);
	}

	@Override
	public List<SensitiveTerm> getAll() {
		return datastore.find(SensitiveTerm.class).filter("enabled", true)
				.asList();
	}

	@Override
	public List<SensitiveTerm> getSome(int front, int end) {
		return datastore
				.find(SensitiveTerm.class)
				.filter("enabled", true)
				.order("-prevCount")
				.asList()
				.subList(
						front,
						(int) Math.min(datastore.find(SensitiveTerm.class)
								.filter("enabled", true).countAll(), end));
	}

	@Override
	public int getRecordCount() {
		return (int) datastore.find(SensitiveTerm.class)
				.filter("enabled", true).countAll();
	}

	@Override
	public SensitiveTerm get(String term) {
		return datastore.find(SensitiveTerm.class).filter("term", term).get();
	}

	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(SensitiveTerm.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SensitiveTerm getById(String id) {
		return datastore.find(SensitiveTerm.class)
				.filter("_id", new ObjectId(id)).get();
	}

}
