package com.shanghai.common.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.util.SerializationUtils;

import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.redis.rediskey.BasePrefix;

/**
 * @author YeJR
 * @version: 2018年5月28日 上午10:07:51 redis 实现cache共享
 *           这里在redis中存储的是字节数组，若存储字符串会出现各种转化错误
 */
public class ShiroRedisCache<K, V> implements Cache<K, V>, Serializable {

	
	private static final long serialVersionUID = 1L;

	/**
	 * 前缀字符串
	 */
	private String str = "shiro:cache";
	
	/**
	 * 前缀
	 */
	private BasePrefix prefix;
	

	public ShiroRedisCache(int cacheExpireTime) {
		prefix = new BasePrefix(cacheExpireTime / 1000, str);
	}

	@Override
	public void clear() throws CacheException {
		JedisUtils.deleteByte(prefix);
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(K k) throws CacheException {
		return (V) JedisUtils.getByte(prefix, String.valueOf(k));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<K> keys() {
		Set<K> keys = new HashSet<K>(16);
		Set<byte[]> bytes = JedisUtils.keysByte(prefix);
		for(byte[] key : bytes) {
			keys.add((K) SerializationUtils.deserialize(key));
		}
		return keys;
	}

	@Override
	public V put(K k, V v) throws CacheException {
		JedisUtils.setByte(prefix, String.valueOf(k), v);
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V remove(K k) throws CacheException {
		return (V) JedisUtils.deleteByte(prefix, String.valueOf(k));
	}

	@Override
	public int size() {
		Set<byte[]> keys = JedisUtils.keysByte(prefix);
		return keys.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		Collection<Object> collection = JedisUtils.valuesByte(prefix);
		return (Collection<V>) collection;
	}


}