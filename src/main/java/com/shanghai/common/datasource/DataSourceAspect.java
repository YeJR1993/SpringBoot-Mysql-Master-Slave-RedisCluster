package com.shanghai.common.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author: YeJR
 * @version: 2018年8月1日 下午3:24:57
 * 切面，切换数据源，具体使用哪个数据源是由（MybatisConfig）AbstractRoutingDataSource中的determineCurrentLookupKey()方法决定
 */
@Aspect
@Component
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class DataSourceAspect implements PriorityOrdered {
	
	/**
	 * 当有@ReadDataSource注解的时候，先将数据源切换至读库
	 */
	@Before("@annotation(com.shanghai.common.datasource.ReadDataSource)")
	public void setReadDataSourceType() {
		DataSourceContextHolder.setDataSourceType(DataSourceType.read.getType());
	}

	/**
	 * 当有@WriteDataSource注解的时候，先将数据源切换至写库
	 */
	@Before("@annotation(com.shanghai.common.datasource.WriteDataSource)")
	public void setWriteDataSourceType() {
		DataSourceContextHolder.setDataSourceType(DataSourceType.write.getType());
	}

	/**
	 * 值越小，越优先执行 要优于事务的执行 在启动类中加上了@EnableTransactionManagement(order = 10)
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
