package com.shanghai.modules.shop.service;
/**
* @author：YeJR
* @version：2018年9月3日 下午4:10:10
* 商店商品
*/

import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.entity.ShopGoods;
import com.shanghai.webapi.shop.entity.ShopGoodsApi;

public interface ShopGoodsService {

	/**
	 * 通过商店商品主键ID获取商品信息
	 * @param shopGoods
	 * @return
	 */
	public ShopGoods getByShopGoodsId(ShopGoods shopGoods);
	
	/**
	 * 分页查询酒店商品
	 * @param pageNo
	 * @param pageSize
	 * @param shopGoods
	 * @return
	 */
	public PageInfo<ShopGoods> getShopGoodsByPage(int pageNo, int pageSize, ShopGoods shopGoods);
	
	/**
	 * 插入新的商店商品
	 * @param shopGoods
	 * @return
	 */
	public Integer saveShopGoods(ShopGoods shopGoods);
	
	/**
	 * 更新商店商品
	 * @param shopGoods
	 * @return
	 */
	public Integer updateShopGoods(ShopGoods shopGoods);
	
	/**
	 * 通过商品ID删除商品
	 * @param id
	 */
	public void deleteShopGoodsById(Integer id);
	
	/**
	 * 通过商品ID批量删除商品
	 * @param ids
	 */
	public void deleteShopGoodsByIds(Integer[] ids);
	
	
	/**
	 * 向kafka发送商品缓存重建的消息
	 * @param shopGoodsApi
	 */
	public void sendRebuildGoodsInfoCacheMsg(ShopGoodsApi shopGoodsApi);
}
