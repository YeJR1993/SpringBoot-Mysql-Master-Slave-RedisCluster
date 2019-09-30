package com.shanghai.common.utils.keyutils;

import com.shanghai.common.redis.rediskey.BasePrefix;
import com.shanghai.common.utils.constant.SysConstants;

/**
 * @author YeJR
 * @version: 2018年5月28日 上午10:07:51
 * 每个模块需要定义不同的key，防止在保存到redis时出现key重复覆盖的现象
 */
public class SystemModule extends BasePrefix{

	private SystemModule(String prefix) {
		super(prefix);
	}
	
	private SystemModule(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	/**
	 * 登录验证码
	 */
	public static SystemModule validateCode = new SystemModule(SysConstants.VALIDATECODE_EXPIRETIME, "validateCode");
	
	/**
	 * 用户前缀
	 */
	public static SystemModule user = new SystemModule("system:user");
	
	/**
	 * 用户名前缀
	 */
	public static SystemModule userName = new SystemModule("system:user:name");
	
	/**
	 * 用户ID前缀
	 */
	public static SystemModule userId = new SystemModule("system:user:id");
	
	/**
	 * 导出用户前缀
	 */
	public static SystemModule userExport = new SystemModule("system:user:export");
	
	/**
	 * 用户分页前缀
	 */
	public static SystemModule userPage = new SystemModule("system:user:page");
	
	/**
	 * 菜单前缀
	 */
	public static SystemModule menu = new SystemModule("system:menu");
	
	/**
	 * 所有菜单前缀
	 */
	public static SystemModule menuAll = new SystemModule("system:menu:all");
	
	/**
	 * 菜单Id前缀
	 */
	public static SystemModule menuId = new SystemModule("system:menu:id");
	
	/**
	 * 用户菜单前缀
	 */
	public static SystemModule menuUser = new SystemModule("system:menu:user");
	
	/**
	 * 角色前缀
	 */
	public static SystemModule role = new SystemModule("system:role");
	
	/**
	 * 角色Id前缀
	 */
	public static SystemModule roleId = new SystemModule("system:role:id");
	
	/**
	 * 角色name前缀
	 */
	public static SystemModule roleName = new SystemModule("system:role:name");
	
	/**
	 * 所有角色前缀
	 */
	public static SystemModule roleAll = new SystemModule("system:role:all");
	
	/**
	 * 用户的所有角色前缀
	 */
	public static SystemModule roleUser = new SystemModule("system:role:user");
}
