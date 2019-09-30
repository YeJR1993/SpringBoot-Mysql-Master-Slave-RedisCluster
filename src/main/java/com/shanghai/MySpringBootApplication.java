package com.shanghai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author YeJR
 * @EnableCaching 开启缓存支持
 * @ImportResource 导入xml文件
 * @ServletComponentScan 为了扫描在common文件夹下定义的filter
 * @EnableTransactionManagement 开启事物
 */
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan(basePackages="com.shanghai.*")
@ImportResource(locations = { "classpath:druid-bean.xml" })
public class MySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringBootApplication.class, args);
	}
}
