package com.shanghai.modules.shop.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shanghai.modules.shop.entity.ShopOrder;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:08:15
* 商店订单
*/
@Mapper
public interface ShopOrderDao {
	
	/**
	 * 通过订单id或者订单编号查询单个订单
	 * @param shopOrder
	 * @return
	 */
	public ShopOrder get(ShopOrder shopOrder);
	
	/**
	 * 分页查询商店订单信息，不带商品信息
	 * @param shopOrder
	 * @return
	 */
	public Page<ShopOrder> findList(ShopOrder shopOrder);
	
	
}
