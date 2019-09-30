package com.shanghai.common.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: YeJR
 * @version: 2018年8月22日 下午6:40:15 
 * master nodes 获取配置文件中的数据
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterNodes {
	
	/**
	 * master node节点
	 */
	private List<String> nodes = new ArrayList<>();

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

}
