package com.shanghai.webapi.shop.request.process;

import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;
import com.shanghai.webapi.shop.service.ShopGoodsApiService;

/**
* @author：YeJR
* @version：2018年9月25日 上午11:13:11
* 
*/
public class ShopGoodsUpdate extends ShopGoodsRequest{
	
	/**
	 * 商品
	 */
	private ShopGoodsApi shopGoodsApi;

	/**
	 * 商品service
	 */
	private ShopGoodsApiService shopGoodsApiService;
	
	
	public ShopGoodsUpdate(ShopGoodsApi shopGoodsApi, ShopGoodsApiService shopGoodsApiService) {
		this.shopGoodsApi = shopGoodsApi;
		this.shopGoodsApiService = shopGoodsApiService;
	}

	@Override
	public void process() {
		// 删除缓存
		shopGoodsApiService.removeGoodsInfoRedisCache(shopGoodsApi.getId());
		shopGoodsApiService.removeGoodsInfoLocalCache(shopGoodsApi.getId());
		// 更新数据库
		shopGoodsApiService.updateGoodsStock(shopGoodsApi);
	}

	@Override
	public Integer getGoodsId() {
		return shopGoodsApi.getId();
	}

}
