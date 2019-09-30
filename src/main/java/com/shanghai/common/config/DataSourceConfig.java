package com.shanghai.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
* @author: YeJR 
* @version: 2018年8月1日 下午2:25:09
* 主从数据源配置
*/

@Configuration
public class DataSourceConfig {
	
	/**
	 * 数据源类型（Druid）
	 */
	@Value("${druid.type}")
	private Class<? extends DataSource> dataSourceType;
    
	/**
	 * 主（写）库
	 * @Primary：指定在同一个接口有多个实现类可以注入的时候，默认选择哪一个
	 * @return
	 */
	@Primary
	@Bean(name = "writeDataSource")
	@ConfigurationProperties(prefix = "druid.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
	
	/**
	 * 从（读）库
     * 有多少个从库就要配置多少个
     * @return
     */
	@Bean(name = "readDataSource1")
	@ConfigurationProperties(prefix = "druid.read1")
    public DataSource readDataSourceOne() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
 
	@Bean(name = "readDataSource2")
	@ConfigurationProperties(prefix = "druid.read2")
    public DataSource readDataSourceTwo() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

}
