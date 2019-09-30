package com.shanghai.webapi.shop.request;

import com.shanghai.common.persistence.Request;

/**
* @author：YeJR
* @version：2018年9月25日 上午10:32:10
* 商品库存相关请求的统一封装
*/
public abstract class ShopGoodsRequest implements Request{
	
	/**
	 * 请求处理方法
	 */
	public abstract void process();
	
	/**
	 * 获取商品ID
	 * @return
	 */
	public abstract Integer getGoodsId();
	
}
