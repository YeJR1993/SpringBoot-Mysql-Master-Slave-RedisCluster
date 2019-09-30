package com.shanghai.common.utils.keyutils;

import com.shanghai.common.redis.rediskey.BasePrefix;

/**
* @author：YeJR
* @version：2018年9月21日 下午5:00:20
* 商店模块，定义商店模块的缓存前缀
*/
public class ShopModule extends BasePrefix{
	
	private ShopModule(String prefix) {
		super(prefix);
	}
	
	private ShopModule(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	/**
	 * 以商品id前缀存储商品信息
	 */
	public static ShopModule goodsInfoId = new ShopModule("shop:goods:id");
}
