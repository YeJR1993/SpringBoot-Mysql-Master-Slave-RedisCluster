package com.shanghai.common.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shanghai.common.redis.RedisClusterNodes;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author YeJR
 * @version: 2018年5月28日 上午10:07:51
 * Jedis cluster 配置类
 */
@Configuration
public class JedisClusterConfig {

	/**
	 * master nodes
	 */
	@Autowired
	private RedisClusterNodes redisClusterNodes;
	
	@Value("${spring.redis.cluster.max-redirects}")
	private int maxRedirects;
	
	/**
	 * 密码
	 */
	@Value("${spring.redis.password}")
	private String password;
	
	/**
	 * 超时
	 */
	@Value("${spring.redis.timeout}")
	private int timeout;
	
	
	
	/**
	 * 连接池最大连接数
	 */
	@Value("${spring.redis.jedis.pool.max-active}")
	private int poolMaxActive;
	
	/**
	 * 连接池最大阻塞等待时间
	 */
	@Value("${spring.redis.jedis.pool.max-wait}")
	private int poolMaxWait;
	
	/**
	 * 连接池中的最大空闲连接
	 */
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int poolMaxIdle;
	
	/**
	 * 连接池中的最小空闲连接
	 */
	@Value("${spring.redis.jedis.pool.min-idle}")
	private int poolMinIdle;
	
	/**
	 * 配置JedisCluster
	 * @return
	 */
	@Bean
	public JedisCluster jedisCluster () {
		// master nodes设置
		Set<HostAndPort> nodes = new HashSet<HostAndPort>(3);
		redisClusterNodes.getNodes().stream().forEach(masterNode -> {
			String[] split = masterNode.split(":");
			String host = split[0];
			int port = Integer.valueOf(split[1]);
			nodes.add(new HostAndPort(host, port));
		});
		// JedisPool 设置
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(poolMaxActive);
		poolConfig.setMaxWaitMillis(poolMaxWait);
		poolConfig.setMaxIdle(poolMaxIdle);
		poolConfig.setMinIdle(poolMinIdle);
		
		JedisCluster jedisCluster = new JedisCluster(nodes, timeout, timeout, maxRedirects, password, poolConfig);
		return jedisCluster;
	}
}
