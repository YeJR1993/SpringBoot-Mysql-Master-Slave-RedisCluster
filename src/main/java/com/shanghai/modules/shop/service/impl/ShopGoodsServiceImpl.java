package com.shanghai.modules.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.common.utils.constant.KafkaConstants;
import com.shanghai.modules.shop.dao.ShopGoodsDao;
import com.shanghai.modules.shop.entity.ShopGoods;
import com.shanghai.modules.shop.service.ShopGoodsService;
import com.shanghai.webapi.shop.entity.ShopGoodsApi;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:19:47
* 商店商品
*/
@Service
public class ShopGoodsServiceImpl implements ShopGoodsService{
	
	@Autowired
	private ShopGoodsDao shopGoodsDao;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public ShopGoods getByShopGoodsId(ShopGoods shopGoods) {
		return shopGoodsDao.get(shopGoods);
	}

	@Override
	public PageInfo<ShopGoods> getShopGoodsByPage(int pageNo, int pageSize, ShopGoods shopGoods) {
		PageHelper.startPage(pageNo, pageSize);
		return new PageInfo<>(shopGoodsDao.findList(shopGoods));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveShopGoods(ShopGoods shopGoods) {
		Integer result = shopGoodsDao.insert(shopGoods);
		sendRebuildGoodsInfoCacheMsg(new ShopGoodsApi(shopGoods.getId()));
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateShopGoods(ShopGoods shopGoods) {
		Integer result = shopGoodsDao.update(shopGoods);
		sendRebuildGoodsInfoCacheMsg(new ShopGoodsApi(shopGoods.getId()));
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteShopGoodsById(Integer id) {
		shopGoodsDao.delete(new ShopGoods(id));
		sendRebuildGoodsInfoCacheMsg(new ShopGoodsApi(id));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteShopGoodsByIds(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				deleteShopGoodsById(id);
			}
		}
	}

	@Override
	public void sendRebuildGoodsInfoCacheMsg(ShopGoodsApi shopGoodsApi) {
		kafkaTemplate.send(KafkaConstants.TOPIC_SHOP_CHANGE, KafkaConstants.KEY_SHOP_GOODS_CHANGE, JSONObject.toJSON(shopGoodsApi).toString());
	}

}
