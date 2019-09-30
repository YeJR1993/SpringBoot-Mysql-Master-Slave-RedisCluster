package com.shanghai.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.utils.keyutils.SystemModule;
import com.shanghai.modules.sys.dao.MenuDao;
import com.shanghai.modules.sys.entity.Menu;
import com.shanghai.modules.sys.service.MenuService;

/**
 * @author: YeJR
 * @version: 2018年6月25日 下午5:08:58
 * 
 */
@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	
	@Override
	public List<Menu> getUserMenuList(Integer userId, Boolean isAdmin, Integer isShow) {
		String key = userId + ":" + isAdmin + ":" + isShow;
		List<Menu> menus = JedisUtils.getList(SystemModule.menuUser, key, Menu.class);
		if (menus == null || menus.size() == 0) {
			if (isAdmin) {
				Menu menu = new Menu();
				menu.setIsShow(isShow);
				menus = menuDao.findList(menu);
			} else {
				menus = menuDao.findByUserId(userId, isShow);
			}
			JedisUtils.setStr(SystemModule.menuUser, key, menus);
		}
		return menus;
	}


	@Override
	public List<Menu> findAllMenus(Menu menu) {
		String key = menu.toString();
		List<Menu> menus = JedisUtils.getList(SystemModule.menuAll, key, Menu.class);
		if (menus == null || menus.size() == 0) {
			// 查询并进行排序
			menus = new ArrayList<Menu>();
			List<Menu> sourceList = menuDao.findList(menu);
			Menu.sortList(menus, sourceList, 1);
			JedisUtils.setStr(SystemModule.menuAll, key, menus);
		}
		return menus;
	}

	@Override
	public Menu getMenuById(Menu menu) {
		Menu menuRes = JedisUtils.getObj(SystemModule.menuId, menu.getId().toString(), Menu.class);
		if (menuRes == null) {
			menuRes = menuDao.get(menu);
			JedisUtils.setStr(SystemModule.menuId, menu.getId().toString(), menuRes);
		}
		return menuRes;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveMenu(Menu menu) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		return menuDao.insert(menu);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateMenu(Menu menu) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		
		return menuDao.update(menu);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteMenuById(Integer id) {
		// 删除缓存
		JedisUtils.deleteStr(SystemModule.menu);
		JedisUtils.deleteStr(SystemModule.user);
		JedisUtils.deleteStr(SystemModule.role);
		// 因为需要删除该菜单下的所有子菜单以及子子菜单等等，先查询
		Menu menu = menuDao.get(new Menu(id));
		// 递归删除子菜单、子子菜单等等
		if (menu.getChildren() != null && menu.getChildren().size() > 0) {
			for(Menu childMenu : menu.getChildren()){
				deleteMenuById(childMenu.getId());
			}
		}
		menuDao.delete(menu);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deletemenuByIds(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				deleteMenuById(id);
			}
		}
	}

}
