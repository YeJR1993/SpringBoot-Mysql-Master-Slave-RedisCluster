package com.shanghai.modules.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.common.utils.keyutils.SystemModule;
import com.shanghai.modules.sys.dao.RoleDao;
import com.shanghai.modules.sys.entity.Role;
import com.shanghai.modules.sys.service.RoleService;

/**
 * @author YeJR
 * @version 2018年6月21日 上午21:19:22
 */
@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Role getRoleById(Integer id) {
		Role role = JedisUtils.getObj(SystemModule.roleId, id.toString(), Role.class);
		if (role == null) {
			role = roleDao.get(new Role(id));
			JedisUtils.setStr(SystemModule.roleId, id.toString(), role);
		}
		return role;
	}
	
	@Override
	public PageInfo<Role> findUserByPage(int pageNo, int pageSize, Role role) {
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(roleDao.findList(role));
	}
	
	@Override
	public List<Role> findAllList(Role role) {
		String key = role.toString();
		List<Role> roles = JedisUtils.getList(SystemModule.roleAll, key, Role.class);
		if (roles == null || roles.size() == 0) {
			roles = roleDao.findList(role);
			JedisUtils.setStr(SystemModule.roleAll, key, roles);
		}
		return roles;
	}

	@Override
	public List<Role> findUserAllRole(Integer userId) {
		List<Role> roles = JedisUtils.getList(SystemModule.roleUser, userId.toString(), Role.class);
		if (roles == null || roles.size() == 0) {
			roles = roleDao.findUserAllRole(userId);
			JedisUtils.setStr(SystemModule.roleUser, userId.toString(), roles);
		}
		return roles;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveRole(Role role) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.roleAll);
		return roleDao.insert(role);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateRole(Role role) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		return roleDao.update(role);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteRoleById(Integer id) {
		
		Role role = getRoleById(id);
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		
		roleDao.delete(role);
		// 删除角色--菜单关系
		roleDao.deleteRoleMenu(role);
		// 删除角色--用户关系
		roleDao.deleteRoleUser(role);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteRoleByIds(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				deleteRoleById(id);
			}
		}
	}

	@Override
	public boolean verifyRoleName(String roleName, String oldRoleName) {
		if (StringUtils.isNotBlank(roleName)) {
			if (roleName.equals(oldRoleName)) {
				return true;
			}
			Role role = JedisUtils.getObj(SystemModule.roleName, roleName, Role.class);
			if (role == null) {
				role = roleDao.getByRoleName(roleName);
				if (role == null) {
					return true;
				} else {
					JedisUtils.setStr(SystemModule.roleName, roleName, role);
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void authSave(Role role) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		// 先删除角色原有的对应的菜单
		roleDao.deleteRoleMenu(role);
		//接着插入新的对应关系
		if (role.getMenus() != null && role.getMenus().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
	}


}
