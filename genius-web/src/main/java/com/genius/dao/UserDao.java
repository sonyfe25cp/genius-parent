package com.genius.dao;

import java.util.List;

import com.genius.model.User;

/**
 * 与用户相关的数据库查询接口
 * @author horizon
 *
 */
public interface UserDao {
	/**
	 * 关闭数据库，释放资源
	 */
	public void close();
	/**
	 * 获取用户计数
	 */
	public long getSize();
	/**
	 * 获取从offset处开始的count个用户，即排在offset到offset+count之间的用户
	 * @param count 获取数目
	 * @param offset 其实偏移量
	 */
	public List<User> getList(int count,int offset);
	/**
	 * 获取从offset处开始的以用户Id进行排序的count个用户，即排在offset到offset+count之间的用户
	 * @param count 获取数目
	 * @param offset 其实偏移量
	 */
	public List<User> getOrderList(int count,int offset);
	
	/**
	 * 不再使用
	 * 推荐使用 getList 或 getOrderList
	 * @see #getList(int count,int offset)
	 * @see #getOrderList(int count,int offset)
	 */
	public List<User> getFirstList(int count);
	/**
	 * 不再使用
	 * 推荐使用 getList 或 getOrderList
	 * @see #getList(int count,int offset)
	 * @see #getOrderList(int count,int offset)
	 */
	public List<User> getNextList(int count,User startUser);
	/**
	 * 不再使用
	 * 推荐使用 getList 或 getOrderList
	 * @see #getList(int count,int offset)
	 * @see #getOrderList(int count,int offset)
	 */
	public List<User> getNextList(int count,String startUsername);
	/**
	 * 获取所有用户
	 */
	public List<User> getAll();
	/**
	 * 按用户名获取用户
	 * @param username 用户名，全局唯一标示
	 */
	public User getUserById(String username);
	/**
	 * 更新用户，不存在时创建（以用户名标示）
	 */
	public boolean save(User user);
	/**
	 * 删除用户，存在时删除（以用户名标示）
	 * @return true 存在，false 不存在
	 */
	public boolean removeUserById(String username);
	/**
	 * 检查是否存在特定用户
	 */
	public boolean existUser(User user);
	/**
	 * 检查是否存在特定用户，及密码是否正确
	 */
	public boolean existUser(String username,String password);
	/**
	 * 检查是否存在特定用户
	 */
	public boolean existUser(String username);
}