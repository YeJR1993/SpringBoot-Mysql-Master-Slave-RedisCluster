package com.shanghai.modules.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.dao.ShopOrderDao;
import com.shanghai.modules.shop.entity.ShopOrder;
import com.shanghai.modules.shop.service.ShopOrderService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:21:27
* 商店订单
*/
@Service
public class ShopOrderServiceImpl implements ShopOrderService{
	
	@Autowired
	private ShopOrderDao shopOrderDao;

	@Override
	public ShopOrder getShopOrderByIdOrNumber(ShopOrder shopOrder) {
		return shopOrderDao.get(shopOrder);
	}

	@Override
	public PageInfo<ShopOrder> getShopOrderWithOutGoodsByPage(int pageNo, int pageSize, ShopOrder shopOrder) {
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(shopOrderDao.findList(shopOrder));
	}


}
