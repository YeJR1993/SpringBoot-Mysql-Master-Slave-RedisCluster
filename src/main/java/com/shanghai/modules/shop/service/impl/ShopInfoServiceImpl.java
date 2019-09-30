package com.shanghai.modules.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.dao.ShopInfoDao;
import com.shanghai.modules.shop.entity.ShopInfo;
import com.shanghai.modules.shop.service.ShopInfoService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:20:58
* 商店基础信息
*/
@Service
public class ShopInfoServiceImpl implements ShopInfoService{
	
	@Autowired
	private ShopInfoDao shopInfoDao;

	@Override
	public ShopInfo getShopInfoById(ShopInfo shopInfo) {
		return shopInfoDao.getById(shopInfo);
	}

	@Override
	public ShopInfo getShopInfoByUserId(ShopInfo shopInfo) {
		return shopInfoDao.getByUserId(shopInfo);
	}
	
	@Override
	public PageInfo<ShopInfo> getShopInfoByPage(int pageNo, int pageSize, ShopInfo shopInfo) {
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(shopInfoDao.findList(shopInfo));
	}
	
	@Override
	public List<ShopInfo> getAllShops(ShopInfo shopInfo) {
		shopInfo.setState(0);
		return shopInfoDao.findList(shopInfo);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveShopInfo(ShopInfo shopInfo) {
		return shopInfoDao.insert(shopInfo);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateShopInfo(ShopInfo shopInfo) {
		return shopInfoDao.update(shopInfo);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteLogicById(Integer id) {
		shopInfoDao.deleteByLogic(new ShopInfo(id));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteLogicByIds(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				deleteLogicById(id);
			}
		}
		
	}


}
