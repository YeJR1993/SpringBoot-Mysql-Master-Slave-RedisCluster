package com.shanghai.common.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shanghai.common.redis.rediskey.KeyPrefix;
import com.shanghai.common.utils.SpringContextHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @author YeJR
 * @version: 2018年5月28日 上午10:07:51 这里提供了jedis工具类，用于redis的增删改查 当然也可以使用spring boot
 *           提供的RedisTemplate工具类
 */
public class JedisUtils {

	private static Logger log = LoggerFactory.getLogger(JedisUtils.class);

	/**
	 * jedisPool工具类
	 */
	private static JedisCluster jedisCluster = SpringContextHolder.getBean(JedisCluster.class);

	/**
	 * 获取通过字符串格式存储的value字符串的值
	 * @param prefix
	 * @param key
	 * @return
	 */
	public static String getStr(KeyPrefix prefix, String key) {
		String realKey = prefix.getPrefix() + ":" + key;
		String str = jedisCluster.get(realKey);
		return str;
	}
	
	/**
	 * 获取通过字节数组存储的value值
	 * @param prefix
	 * @param key
	 * @return
	 */
	public static Object getByte(KeyPrefix prefix, String key) {
		String realKey = prefix.getPrefix() + ":" + key;
		byte[] bytes = jedisCluster.get(realKey.getBytes());
		return SerializationUtils.deserialize(bytes);
	}
	
	/**
	 * 获取存储的对象
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getObj(KeyPrefix prefix, String key, Class<T> clazz) {
		String val = getStr(prefix, key);
		T t = stringToBean(val, clazz);
		return t;
	}
	
	/**
	 * 解析为list数组
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(KeyPrefix prefix, String key, Class<T> clazz) {
		String val = getStr(prefix, key);
		List<T> list = stringToList(val, clazz);
		return list;
	}
	
	/**
	 * 以字符串格式存储value
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setStr(KeyPrefix prefix, String key, Object value) {
		String str = beanToString(value);
		if (str == null || str.length() <= 0) {
			return false;
		}
		// 生成真正的key
		String realKey = prefix.getPrefix() + ":" + key;
		int seconds = prefix.expireSeconds();
		if (seconds <= 0) {
			jedisCluster.set(realKey, str);
		} else {
			jedisCluster.setex(realKey, seconds, str);
		}
		return true;
	}
	
	/**
	 * 以字节数组的方式存储value
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setByte(KeyPrefix prefix, String key, Object value) {
		// 生成真正的key
		String realKey = prefix.getPrefix() + ":" + key;
		int seconds = prefix.expireSeconds();
		if (seconds <= 0) {
			jedisCluster.set(realKey.getBytes(), SerializationUtils.serialize(value));
		} else {
			jedisCluster.setex(realKey.getBytes(), seconds, SerializationUtils.serialize(value));
		}
		return true;
	}

	
	
	/**
	 * 模糊查询相应字符串存储的key的集合
	 * @param prefix
	 * @return
	 */
	public static Set<String> keysStr(KeyPrefix prefix) {
		Set<String> keys = new HashSet<String>();
		// 生成真正的key
		String realKey = prefix.getPrefix() + "*";
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String key : clusterNodes.keySet()) {
			JedisPool jedisPool = clusterNodes.get(key);
			Jedis jedis = jedisPool.getResource();
			try {
				keys.addAll(jedis.keys(realKey));
			} catch (Exception e) {
				log.error("Getting keys error: {}", e);
			} finally {
				log.debug("Connection closed.");
				// 关闭连接
				jedis.close();
			}
		}
		return keys;
	}
	
	/**
	 * 模糊查询相应字节数组存储的key的集合
	 * @param prefix
	 * @return
	 */
	public static Set<byte[]> keysByte(KeyPrefix prefix) {
		Set<byte[]> keys = new HashSet<byte[]>();
		// 生成真正的key
		String realKey = prefix.getPrefix() + "*";
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String key : clusterNodes.keySet()) {
			JedisPool jedisPool = clusterNodes.get(key);
			Jedis jedis = jedisPool.getResource();
			try {
				keys.addAll(jedis.keys(realKey.getBytes()));
			} finally {
				log.debug("Connection closed.");
				// 关闭连接
				jedis.close();
			}
		}
		return keys;
	}

	/**
	 * 获取以字符串存储的具有相同前缀的value集合
	 * @param prefix
	 * @return
	 */
	public static Collection<String> valuesStr(KeyPrefix prefix) {
		List<String> lists = new ArrayList<String>(16);
		Set<String> keys = keysStr(prefix);
		for (String key : keys) {
			String val = jedisCluster.get(key);
			lists.add(val);
		}
		return lists;
	}
	
	/**
	 * 获取以字节数组存储的具有相同前缀的value集合
	 * @param prefix
	 * @return
	 */
	public static Collection<Object> valuesByte(KeyPrefix prefix) {
		List<Object> lists = new ArrayList<Object>(16);
		Set<byte[] > keys = keysByte(prefix);
		for (byte[] key : keys) {
			lists.add(SerializationUtils.deserialize(jedisCluster.get(key)));
		}
		return lists;
	}


	/**
	 * 删除某一个具体value
	 * 
	 * @param prefix
	 * @param key
	 * @return
	 */
	public static String deleteStr(KeyPrefix prefix, String key) {
		String realKey = prefix.getPrefix() + ":" + key;
		String val = getStr(prefix, key);
		jedisCluster.del(realKey);
		return val;
	}

	/**
	 * 删除字节数组存储的value
	 * @param prefix
	 * @param key
	 * @return
	 */
	public static Object deleteByte(KeyPrefix prefix, String key) {
		String realKey = prefix.getPrefix() + ":" + key;
		Object val = getByte(prefix, key);
		jedisCluster.del(realKey.getBytes());
		return val;
	}
	
	/**
	 * 删除所有相同前缀的value
	 * @param prefix
	 */
	public static void deleteStr(KeyPrefix prefix) {
		Set<String> keys = keysStr(prefix);
		for (String key : keys) {
			jedisCluster.del(key);
		}
	}
	
	/**
	 * 删除所有相同前缀的value
	 * @param prefix
	 */
	public static void deleteByte(KeyPrefix prefix) {
		Set<byte[]> keys = keysByte(prefix);
		for (byte[] key : keys) {
			jedisCluster.del(key);
		}
	}

	/**
	 * bean 转 String
	 * 
	 * @param value
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String beanToString(Object value) {
		if (value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return "" + value;
		} else if (clazz == String.class) {
			return (String) value;
		} else if (clazz == long.class || clazz == Long.class) {
			return "" + value;
		} else {
			// 使用fastjson不能直接序列化shiro session，会出现数据丢失的问题
			return JSON.toJSONString(value);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	
	public static <T> List<T> stringToList(String str, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		list = JSONArray.parseArray(str, clazz);
		return list;
	}

}
