package com.shanghai.common.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.redis.rediskey.BasePrefix;

/**
 * @author YeJR
 * @version: 2018年5月28日 上午10:07:51 redis 实现session共享
 *         	这里在redis中存储的是字节数组，若存储字符串会出现各种转化错误
 */
@Component
public class ShiroRedisSession extends EnterpriseCacheSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(ShiroRedisSession.class);

	/**
	 * 前缀字符串
	 */
	private String str = "shiro:session";

	/**
	 * 前缀
	 */
	private BasePrefix prefix;
	
	
	/**
	 * 不使用注入的方式，因为这里注入失败
	 * @param sessionExpireTime
	 */
	public ShiroRedisSession(int sessionExpireTime) {
		prefix = new BasePrefix(sessionExpireTime / 1000, str);
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);
		logger.debug("创建session:{}", sessionId);
		JedisUtils.setByte(prefix, sessionId.toString(), session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		logger.debug("获取session:{}", sessionId);
		// 先从缓存中获取session，如果没有再去数据库中获取
		Session session = super.doReadSession(sessionId);
		if (session == null) {
			session = (Session) JedisUtils.getByte(prefix, sessionId.toString());
		}
		return session;
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
		logger.debug("更新session:{}", session.getId());
		JedisUtils.setByte(prefix, session.getId().toString(), session);
	}

	@Override
	protected void doDelete(Session session) {
		logger.debug("删除session:{}", session.getId());
		super.doDelete(session);
		JedisUtils.deleteByte(prefix, session.getId().toString());
	}

	@Override
	public Collection<Session> getActiveSessions() {
		List<Session> sessions = new ArrayList<Session>();
		Collection<Object> collection = JedisUtils.valuesByte(prefix);
		for (Object object : collection) {
			Session session = (Session) object;
			//将登入过的session放入集合
			if (session != null && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
				sessions.add(session);
			}
		}
		return sessions;
	}

}
