package com.shanghai.modules.sys.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.common.utils.UserUtil;
import com.shanghai.common.utils.keyutils.SystemModule;
import com.shanghai.modules.sys.dao.UserDao;
import com.shanghai.modules.sys.entity.User;
import com.shanghai.modules.sys.service.UserService;

/**
 * @author: YeJR
 * @version: 2018年4月28日 上午10:19:22
 * 
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User getByLoginName(String loginName) {
		User user = JedisUtils.getObj(SystemModule.userName, loginName, User.class);
		if (user == null) {
			user = userDao.getUserByName(loginName);
			JedisUtils.setStr(SystemModule.userName, loginName, user);
		}
		return user;
	}
	
	@Override
	public User getUserById(Integer id) {
		User user = JedisUtils.getObj(SystemModule.userId, id.toString(), User.class);
		if (user == null) {
			user = userDao.get(new User(id));
			JedisUtils.setStr(SystemModule.userId, id.toString(), user);
		}
		return user;
	}
	
	@Override
	public List<User> findAllUsers(User user) {
		List<User> users = JedisUtils.getList(SystemModule.userExport, user.toString(), User.class);
		if (users == null || users.size() == 0) {
			users = userDao.findList(user);
			JedisUtils.setStr(SystemModule.userExport, user.toString(), users);
		}
		return users;
	}
	
	@Override
	public PageInfo<User> findUserByPage(int pageNo, int pageSize, User user) {
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(userDao.findList(user));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveUser(User user) throws IOException {
		String newPassword = UserUtil.encryptPassword(user.getUsername(), user.getPassword());
		user.setPassword(newPassword);
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.userExport);
		// 保存到db中
		Integer result = userDao.insert(user);
		if (user.getRoleIds() != null && user.getRoleIds().size() > 0) {
			//插入新的用户角色关联关系
			userDao.insertUserRole(user);
		}
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateUser(User user) throws IOException {
		if (StringUtils.isNotBlank(user.getPassword())) {
			String newPassword = UserUtil.encryptPassword(user.getUsername(), user.getPassword());
			user.setPassword(newPassword);
		}
		
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		
		// 更新数据库
		Integer result = userDao.update(user);
		
		//删除原有的用户角色关联关系
		userDao.deleteUserRole(user);
		if (user.getRoleIds() != null && user.getRoleIds().size() > 0) {
			//插入新的用户角色关联关系
			userDao.insertUserRole(user);
		}
		
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteUserById(Integer id) throws IOException {
		//获取需要删除的用户
		User user = getUserById(id);
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		
		// 删除数据库
		userDao.delete(user);
		// 删除用户角色--角色关系
		userDao.deleteUserRole(user);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteUserByIds(Integer[] ids) throws IOException {
		if (ids != null) {
			for (Integer id : ids) {
				deleteUserById(id);
			}
		}
		
	}
	
	@Override
	public boolean verifyUsername(String username, String oldUsername) {
		if (StringUtils.isNotBlank(username)) {
			if (username.equals(oldUsername)) {
				return true;
			}
			User user = getByLoginName(username);
			if (user == null) {
				return true;
			}
		}
		return false;
	}


}
