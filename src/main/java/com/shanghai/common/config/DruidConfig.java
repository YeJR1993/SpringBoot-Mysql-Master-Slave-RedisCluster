package com.shanghai.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @author: YeJR
 * @version: 2018年8月1日 下午5:57:22
 * Druid监控配置 
 */
@Configuration
public class DruidConfig {
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		ServletRegistrationBean<StatViewServlet> reg = new ServletRegistrationBean<StatViewServlet>();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		// 在日志中打印执行慢的sql语句
		reg.addInitParameter("logSlowSql", "true");
		return reg;
	}

	@Bean
	public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		// 过滤文件类型
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		// 监控单个url调用的sql列表
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		return filterRegistrationBean;
	}

}
