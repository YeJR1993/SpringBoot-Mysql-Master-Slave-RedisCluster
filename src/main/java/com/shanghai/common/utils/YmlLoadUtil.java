package com.shanghai.common.utils;

import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.shanghai.MySpringBootApplication;

/** 
* @author: YeJR 
* @version: 2018年10月22日 上午10:32:33
* YmlLoadUtil 读取yml配置文件工具类
*/
public class YmlLoadUtil {
	
	/**
	 * 读取的map
	 */
	private Map<String, Object> map;
	
	/**
	 * 初始化
	 */
	public YmlLoadUtil() {
		Yaml yaml = new Yaml();
		map = yaml.load(MySpringBootApplication.class.getResourceAsStream("/application.yml"));
	}
	
	/**
	 * @author YeJR
	 * 内部类
	 */
	private static class Singleton {
		
		private static YmlLoadUtil instance;
		
		static {
			instance = new YmlLoadUtil();
		}
		
		private static YmlLoadUtil getInstance() {
			return instance;
		}
	}
	
	/**
	 * 获取内存中YmlLoadUtil初始化后的唯一对象
	 * @return
	 */
	public static YmlLoadUtil getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 获取配置文件
	 * @param key
	 * @return
	 */
	public Object getProperty(String key) {
		return map.get(key);
	}
	
}
