package com.shanghai.common.datasource;

/**
 * @author: YeJR
 * @version: 2018年8月1日 上午11:04:07 获取设置数据源的类型
 */
public class DataSourceContextHolder {

	/**
	 * 线程本地变量, 存储使用的数据源类型
	 */
	private static final ThreadLocal<String> LOCAL = new ThreadLocal<String>() {

		/**
		 * 设置默认的数据源
		 */
		@Override
		protected String initialValue() {
			return DataSourceType.write.getType();
		}

	};

	/**
	 * 设置数据源
	 * @param dataSourceType
	 */
	public static void setDataSourceType(String dataSourceType) {
		if (dataSourceType == null) {
			throw new NullPointerException();
		}
		LOCAL.set(dataSourceType);
	}

	/**
	 * 获取数据源，没有的话就使用WRITE数据源
	 * 
	 * @return
	 */
	public static String getReadOrWrite() {
		return LOCAL.get() == null ? DataSourceType.write.getType() : LOCAL.get();
	}

}
