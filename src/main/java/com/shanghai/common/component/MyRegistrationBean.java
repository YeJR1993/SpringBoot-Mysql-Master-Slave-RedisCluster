package com.shanghai.common.component;

import java.util.EventListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shanghai.common.listener.InitListener;

/**
 * @author YeJR
 * @version 2018年8月23日 下午8:48:24
 * 自定义注册Bean
 */
@Component
public class MyRegistrationBean {
	
	/**
	 * 注册自定义的监听器
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<EventListener> servletListenerRegistrationBean () {
		ServletListenerRegistrationBean<EventListener> servletListenerRegistrationBean = 
				new ServletListenerRegistrationBean<EventListener>();
		servletListenerRegistrationBean.setListener(new InitListener());
		return servletListenerRegistrationBean;
	}
	
}
