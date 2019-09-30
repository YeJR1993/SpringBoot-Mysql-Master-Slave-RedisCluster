package com.shanghai.common.datasource;

/**
 * @author: YeJR
 * @version: 2018年8月1日 下午2:08:11
 * 数据源枚举类
 */
public enum DataSourceType {
	// 主（写）库
	write("write", "主库"),
	// 从（读）库
	read("read", "从库");

	private String type;

	private String name;

	DataSourceType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
