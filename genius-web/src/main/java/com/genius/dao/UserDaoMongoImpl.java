/**
 * 
 */
package com.genius.dao;

import java.net.UnknownHostException;
import java.util.List;

import com.genius.model.ExtractConfiguration;
import com.genius.model.User;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author horizon
 * 
 */
public class UserDaoMongoImpl extends MongoDAOBase implements UserDao {

	/**
	 * 
	 */
	public UserDaoMongoImpl() {
		
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class).asList();
		return result;
	}

	@Override
	public List<User> getFirstList(int count) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class).order("username")
				.filter("username >=", "A").limit(count).asList();
		return result;
	}

	@Override
	public List<User> getNextList(int count, User startUser) {
		// TODO Auto-generated method stub
		return getNextList(count, startUser.getUsername());
	}

	@Override
	public List<User> getNextList(int count, String startUsername) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class).order("username")
				.filter("username >", startUsername).limit(count).asList();
		return result;
	}

	@Override
	public boolean existUser(User user) {
		// TODO Auto-generated method stub
		return existUser(user.getUsername(), user.getPassword());
	}

	@Override
	public boolean existUser(String username, String password) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class)
				.filter("username =", username).filter("password =", password)
				.asList();
		if (result.size() == 1) {
			return true;
		} else if (result.size() == 0) {
			return false;
		} else {
			logger.error("more than one user named " + username
					+ " with password " + password);
			return false;
		}
	}

	@Override
	public boolean existUser(String username) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class)
				.filter("username =", username).asList();
		if (result.size() == 1) {
			return true;
		} else if (result.size() == 0) {
			return false;
		} else {
			logger.error("more than one user named " + username);
			return false;
		}
	}

	@Override
	public boolean save(User user) {
		// TODO Auto-generated method stub
		Key<User> result = datastore.save(user);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<User> getList(int count, int offset) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class).offset(offset)
				.limit(count).asList();
		return result;
	}

	@Override
	public List<User> getOrderList(int count, int offset) {
		List<User> result = datastore.find(User.class).order("username")
				.filter("username >=", "A").offset(offset).limit(count)
				.asList();
		return result;
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		long result = datastore.find(User.class).countAll();
		return result;
	}

	@Override
	public User getUserById(String username) {
		// TODO Auto-generated method stub
		List<User> result = datastore.find(User.class)
				.filter("username =", username).asList();
		if (result.size() == 0) {
			logger.warn("User :" + username + " not found");
			return null;
		} else {
			return result.get(0);
		}
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

	@Override
	public boolean removeUserById(String username) {
		// TODO Auto-generated method stub
		if(existUser(username))
		{
			datastore.delete(datastore.find(User.class)
				.filter("username =", username));
			return true;
		}
		else
		{
			return false;
		}
	}

}
