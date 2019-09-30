package com.shanghai.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.shanghai.common.datasource.DataSourceContextHolder;
import com.shanghai.common.datasource.DataSourceType;
import com.shanghai.common.utils.SpringContextHolder;

/**
 * @author: YeJR
 * @version: 2018年8月1日 上午11:26:36 配置数据源
 */
@Configuration
public class MybatisConfig {
	
	/**
	 * 读数据源的数量
	 */
	@Value("${druid.readSize}")
	private String readDataSourceSize;

	/**
	 * entity扫描的包名
	 */
	@Value("${mybatis.type-aliases-package}")
	private String typeAliasesPackage;

	/**
	 * xml所在的包
	 */
	@Value("${mybatis.mapper-locations}")
	private String mapperLocations;

	/**
	 * mybatis-config配置文件
	 */
	@Value("${mybatis.config-location}")
	private String configLocation;

	/**
	 * 主库（写）bean
	 * @Autowired 和 @Qualifier 结合使用时，自动注入的策略就从 byType 转变成 byName
	 */
	@Autowired
	@Qualifier("writeDataSource")
	private DataSource writeDataSource;

	/**
	 * 从库1（读） bean
	 */
	@Autowired
	@Qualifier("readDataSource1")
	private DataSource readDataSource1;

	/**
	 * 从库2（读） bean
	 */
	@Autowired
	@Qualifier("readDataSource2")
	private DataSource readDataSource2;

	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactorys() throws Exception {

		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		// 设置数据源
		sessionFactoryBean.setDataSource(roundRobinDataSouceProxy());
		// 设置entity扫描的包
		sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
		// xml所在的包
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		// 设置mybatis-config.xml配置文件位置
		sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
		//mybatis需要设置spring boot vsf，否则在打包成jar运行会出现扫描不到实体类的错误
		sessionFactoryBean.setVfs(SpringBootVFS.class);
		
		return sessionFactoryBean.getObject();
	}

	/**
	 * 选择合适的数据源
	 * @return
	 */
	@Bean(name = "roundRobinDataSouceProxy")
	public AbstractRoutingDataSource roundRobinDataSouceProxy() {

		Map<Object, Object> targetDataSources = new HashMap<Object, Object>(3);
		// 把所有数据库都放在targetDataSources中,注意key值要和determineCurrentLookupKey()中代码写的一至，
		// 否则切换数据源时找不到正确的数据源
		targetDataSources.put(DataSourceType.write.getType(), writeDataSource);
		targetDataSources.put(DataSourceType.read.getType() + "1", readDataSource1);
		targetDataSources.put(DataSourceType.read.getType() + "2", readDataSource2);

		final int readSize = Integer.parseInt(readDataSourceSize);

		// 路由类，寻找对应的数据源
		AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
			
			private AtomicInteger count = new AtomicInteger(0);
			/**
			 * 这是AbstractRoutingDataSource类中的一个抽象方法，
			 * 而它的返回值是你所要用的数据源dataSource的key值，有了这个key值，
			 * targetDataSources就从中取出对应的DataSource，如果找不到，就用配置默认的数据源。
			 */
			@Override
			protected Object determineCurrentLookupKey() {
				// 该值是由后面的切面进行赋值
				String typeKey = DataSourceContextHolder.getReadOrWrite();
				// 写库，就直接返回
				if (typeKey.equals(DataSourceType.write.getType())) {
					logger.debug("切换数据源至："+ DataSourceType.write.getType());
					return DataSourceType.write.getType();
				}
				// 读库， 简单负载均衡（轮询）
				int number = count.getAndAdd(1);
				int lookupKey = number % readSize;
				logger.debug("切换数据源至："+ DataSourceType.read.getType() + (lookupKey + 1));
				return DataSourceType.read.getType() + (lookupKey + 1);
			}
		};
		// 默认库
		proxy.setDefaultTargetDataSource(writeDataSource);
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}

	@Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
     * 事务管理
     * @return
     */
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager((DataSource)SpringContextHolder.getBean("roundRobinDataSouceProxy"));
    }

}
