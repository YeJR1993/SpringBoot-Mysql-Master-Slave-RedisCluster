package com.shanghai.webapi.shop.request.process;

import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;
import com.shanghai.webapi.shop.service.ShopGoodsApiService;

/**
* @author：YeJR
* @version：2018年9月25日 上午11:00:22
* 商品库存刷新
*/
public class ShopGoodsCacheRefresh extends ShopGoodsRequest{
	
	/**
	 * 商品
	 */
	private ShopGoodsApi shopGoodsApi;
	
	/**
	 * 是否强制刷新
	 */
	private Boolean forceRefresh;
	
	/**
	 * 商品service
	 */
	private ShopGoodsApiService shopGoodsApiService;
	
	
	public ShopGoodsCacheRefresh(ShopGoodsApi shopGoodsApi, ShopGoodsApiService shopGoodsApiService, Boolean forceRefresh) {
		this.shopGoodsApi = shopGoodsApi;
		this.shopGoodsApiService = shopGoodsApiService;
		this.forceRefresh = forceRefresh;
	}

	@Override
	public void process() {
		// 从数据库中查询最新的商品信息
		shopGoodsApi = shopGoodsApiService.getByShopGoodsId(shopGoodsApi);
		// 将最新的商品库存数量，放入缓存中
		shopGoodsApiService.setGoodsInfoRedisCache(shopGoodsApi);
		shopGoodsApiService.setGoodsInfoLocalCache(shopGoodsApi);
	}

	@Override
	public Integer getGoodsId() {
		return shopGoodsApi.getId();
	}
	
	/**
	 * 是否强制刷新
	 * @return
	 */
	public Boolean isForceRefresh() {
		return forceRefresh;
	}

}
