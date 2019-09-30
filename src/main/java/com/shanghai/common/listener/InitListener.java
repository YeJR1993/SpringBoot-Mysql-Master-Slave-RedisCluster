package com.shanghai.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.shanghai.webapi.shop.thread.pool.ShopGoodsThreadPool;

/**
 * @author YeJR
 * @version 2018年8月23日 下午8:34:35
 * 系统初始化监听器，在MyRegistrationBean类中的ServletListenerRegistrationBean中进行注册
 */
public class InitListener implements ServletContextListener{

	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//初始化商品库存线程池
		ShopGoodsThreadPool.getInstance();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		
	}

}
