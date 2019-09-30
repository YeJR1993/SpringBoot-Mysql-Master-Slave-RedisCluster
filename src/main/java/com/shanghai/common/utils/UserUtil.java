package com.shanghai.common.utils;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;

import com.shanghai.common.shiro.ShiroRedisCacheManager;
import com.shanghai.common.shiro.ShiroRedisSession;
import com.shanghai.common.utils.constant.SysConstants;
import com.shanghai.modules.sys.entity.Menu;
import com.shanghai.modules.sys.entity.User;
import com.shanghai.modules.sys.service.MenuService;
import com.shanghai.modules.sys.service.UserService;

/**
* @author: YeJR 
* @version: 2018年5月11日 下午4:29:21
* 用户工具类
*/
public class UserUtil {
	
	/**
	 * 获取对应的bean
	 */
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	private static MenuService menuService = SpringContextHolder.getBean(MenuService.class);
	private static ShiroRedisSession sessionService = SpringContextHolder.getBean(ShiroRedisSession.class);
	private static ShiroRedisCacheManager cacheManager = SpringContextHolder.getBean(ShiroRedisCacheManager.class);
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		return userService.getByLoginName(loginName);
	}

	/**
	 * 查询该用户id对应下的所有菜单列表
	 * @param userId
	 * @param isAdmin
	 * @param isShow null:所有菜单， 0：隐藏的菜单， 1：显示的菜单
	 * @return
	 */
	public static List<Menu> getMenuList(Integer userId, Boolean isAdmin, Integer isShow) {
		return menuService.getUserMenuList(userId, isAdmin, isShow);
	}
	
	/**
	 * 登录密码加密，使用shiro的盐值加密方法
	 * @param name
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String name, String password) {
		Object salt = ByteSource.Util.bytes(name);
		Object newPassword = new SimpleHash(SysConstants.SHIRO_CREDENTIALSMATCHER, password, salt, SysConstants.SHIRO_ENCRYPTION_NUMBER);
		return newPassword.toString();
	}
	
	/**
	 * 删除相同用户的其他登入session、cache
	 */
	public static void singleAccess() {
		// 当前登入者的session与登入名
		Subject subject = SecurityUtils.getSubject();
		Session currentSession = subject.getSession();
		String loginName = subject.getPrincipal().toString();
		// 系统中所有登入者session
		Collection<Session> sessions = sessionService.getActiveSessions();
		// 遍历session进行删除
		for (Session session : sessions) {
			//过滤掉当前的登录session
			if (!String.valueOf(session.getId()).equals(String.valueOf(currentSession.getId()))) {
				// 如果当前的登录名与其他session中的登录名一致
				if (loginName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
					// 删除session
					sessionService.delete(session);
					// 删除cache
					cacheManager.getCache("").remove(session.getId());
				}
			}
		}
	}
	
}
