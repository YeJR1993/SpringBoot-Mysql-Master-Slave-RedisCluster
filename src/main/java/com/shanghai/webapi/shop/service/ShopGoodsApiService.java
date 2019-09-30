package com.shanghai.webapi.shop.service;
/**
* @author：YeJR
* @version：2018年9月3日 下午4:10:10
* 商店商品
*/

import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;

public interface ShopGoodsApiService {

	/**
	 * 通过商店商品主键ID获取商品信息
	 * @param shopGoodsApi
	 * @return
	 */
	public ShopGoodsApi getByShopGoodsId(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 更新商品库存
	 * @param shopGoodsApi
	 * @return
	 */
	public Integer updateGoodsStock(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 将封装后的请求按照某一规则放入内存队列中对应的ArrayBlockingQueue中
	 * @param request
	 */
	public void process(ShopGoodsRequest request);
	
	/**
	 * 向kafka发送商品缓存重建的消息
	 * @param shopGoodsApi
	 */
	public void sendRebuildGoodsInfoCacheMsg(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 保存商品信息到ehcache缓存
	 * @param shopGoodsApi
	 * @return
	 */
	public ShopGoodsApi setGoodsInfoLocalCache(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 从ehcache中获取商品信息缓存
	 * @param id
	 * @return
	 */
	public ShopGoodsApi getGoodsInfoLocalCache(Integer id);
	
	/**
	 * 从ehcache中删除商品信息缓存
	 * @param id
	 */
	public void removeGoodsInfoLocalCache(Integer id);
	
	/**
	 * 设置redis中商品信息缓存
	 * @param shopGoodsApi
	 */
	public void setGoodsInfoRedisCache(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 获取redis中商品信息缓存
	 * @param id
	 * @return
	 */
	public ShopGoodsApi getGoodsInfoRedisCache(Integer id);
	
	/**
	 * 删除redis中的商品信息缓存
	 * @param id
	 */
	public void removeGoodsInfoRedisCache(Integer id);
}
