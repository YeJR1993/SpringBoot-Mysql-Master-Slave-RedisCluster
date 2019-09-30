package com.shanghai.modules.shop.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shanghai.modules.shop.entity.ShopGoods;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:05:34
* 商店商品
*/
@Mapper
public interface ShopGoodsDao {

	/**
	 * 通过酒店商品ID查询
	 * @param shopGoods
	 * @return
	 */
	public ShopGoods get(ShopGoods shopGoods);
	
	/**
	 * 查询所有酒店商品
	 * @param shopGoods
	 * @return
	 */
	public Page<ShopGoods> findList(ShopGoods shopGoods);
	
	/**
	 * 插入酒店商品
	 * @param shopGoods
	 * @return
	 */
	public Integer insert(ShopGoods shopGoods);
	
	/**
	 * 更新酒店商品
	 * @param shopGoods
	 * @return
	 */
	public Integer update(ShopGoods shopGoods);
	
	/**
	 * 删除酒店商品
	 * @param shopGoods
	 * @return
	 */
	public Integer delete(ShopGoods shopGoods);
}
