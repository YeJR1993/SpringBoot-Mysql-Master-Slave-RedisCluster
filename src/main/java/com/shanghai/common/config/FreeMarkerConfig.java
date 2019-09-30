package com.shanghai.common.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.shanghai.common.tag.MenuTag;

/**
* @author: YeJR 
* @version: 2018年5月31日 上午11:41:28
* freemarker配置
*/
@Configuration
public class FreeMarkerConfig implements InitializingBean{
	
	@Autowired
    protected freemarker.template.Configuration configuration;
	
	@Autowired
    protected MenuTag menuTag;
	
	/**
	 * 自定义标签
	 * @PostConstruct :被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法。
	 * 					被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
	 */
	@PostConstruct
    public void setSharedVariable() {
        // menu即为页面上调用的标签名
        configuration.setSharedVariable("menu", menuTag);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		//可以在页面上使用shiro标签
		configuration.setSharedVariable("shiro", new ShiroTags());
	}

}
