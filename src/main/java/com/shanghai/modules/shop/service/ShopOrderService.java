package com.shanghai.modules.shop.service;
/**
* @author：YeJR
* @version：2018年9月3日 下午4:10:53
* 商店订单
*/

import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.entity.ShopOrder;

public interface ShopOrderService {
	
	/**
	 * 通过订单id或者编号查询订单信息
	 * @param shopOrder
	 * @return
	 */
	public ShopOrder getShopOrderByIdOrNumber(ShopOrder shopOrder);

	/**
	 * 分页查询商店订单， 不带goods信息
	 * @param pageNo
	 * @param pageSize
	 * @param shopOrder
	 * @return
	 */
	public PageInfo<ShopOrder> getShopOrderWithOutGoodsByPage(int pageNo, int pageSize, ShopOrder shopOrder);
	
}
