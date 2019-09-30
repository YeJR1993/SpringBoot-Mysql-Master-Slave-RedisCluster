package com.shanghai.webapi.shop.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shanghai.webapi.shop.entity.ShopGoodsApi;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:05:34
* 商店商品
*/
@Mapper
public interface ShopGoodsApiDao {

	/**
	 * 通过酒店商品ID查询
	 * @param shopGoodsApi
	 * @return
	 */
	public ShopGoodsApi get(ShopGoodsApi shopGoodsApi);
	
	/**
	 * 更新商品库存
	 * @param shopGoodsApi
	 * @return
	 */
	public Integer updateGoodsStock(ShopGoodsApi shopGoodsApi);
}
