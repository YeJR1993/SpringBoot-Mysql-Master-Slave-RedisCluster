package com.shanghai.webapi.shop.service.impl;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shanghai.common.persistence.Request;
import com.shanghai.common.redis.JedisUtils;
import com.shanghai.common.utils.constant.KafkaConstants;
import com.shanghai.common.utils.keyutils.ShopModule;
import com.shanghai.webapi.shop.dao.ShopGoodsApiDao;
import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;
import com.shanghai.webapi.shop.request.queue.ShopGoodsQueue;
import com.shanghai.webapi.shop.service.ShopGoodsApiService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:19:47
*/
@Service
public class ShopGoodsApiServiceImpl implements ShopGoodsApiService{
	
	/**
	 * 对应ehcache.xml 中的缓存策略
	 */
	public static final String CACHE_NAME = "local";
	
	@Autowired
	private ShopGoodsApiDao shopGoodsApiDao;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public ShopGoodsApi getByShopGoodsId(ShopGoodsApi shopGoodsApi) {
		return shopGoodsApiDao.get(shopGoodsApi);
	}

	@Override
	public Integer updateGoodsStock(ShopGoodsApi shopGoodsApi) {
		Integer result = shopGoodsApiDao.updateGoodsStock(shopGoodsApi);
		sendRebuildGoodsInfoCacheMsg(shopGoodsApi);
		return result;
	}

	@Override
	public void process(ShopGoodsRequest request) {
		try {
			ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getGoodsId());
			queue.put(request);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取应该路由到内存队列中的某个ArrayBlockingQueue
	 * @param goodsId
	 * @return
	 */
	private ArrayBlockingQueue<Request> getRoutingQueue(Integer goodsId) {
		
		ShopGoodsQueue shopGoodsQueue = ShopGoodsQueue.getInstance();
		// 先获取goodsId的hash值
		String key = String.valueOf(goodsId);
		int h;
		int hash = (goodsId == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
		// 对goodsId 的 hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
		// 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
		// 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
		int index = (shopGoodsQueue.goodsStockQueueSize() - 1) & hash;
		return shopGoodsQueue.getQueue(index);
	}
	
	@Override
	public void sendRebuildGoodsInfoCacheMsg(ShopGoodsApi shopGoodsApi) {
		kafkaTemplate.send(KafkaConstants.TOPIC_SHOP_CHANGE, KafkaConstants.KEY_SHOP_GOODS_CHANGE, JSONObject.toJSON(shopGoodsApi).toString());
	}

	@Override
	@CachePut(value = CACHE_NAME, key = "'shop:goods:id:'+#shopGoodsApi.getId()")
	public ShopGoodsApi setGoodsInfoLocalCache(ShopGoodsApi shopGoodsApi) {
		return shopGoodsApi;
	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "'shop:goods:id:'+#id")
	public ShopGoodsApi getGoodsInfoLocalCache(Integer id) {
		return null;
	}

	@Override
	@CacheEvict(value= CACHE_NAME, key = "'shop:goods:id:'+#id")
	public void removeGoodsInfoLocalCache(Integer id) {
		
	}

	@Override
	public void setGoodsInfoRedisCache(ShopGoodsApi shopGoodsApi) {
		if (shopGoodsApi.getStock() != null && shopGoodsApi.getId() != null) {
			JedisUtils.setStr(ShopModule.goodsInfoId, String.valueOf(shopGoodsApi.getId()), shopGoodsApi);
		}
	}

	@Override
	public ShopGoodsApi getGoodsInfoRedisCache(Integer id) {
		if (id != null) {
			return JedisUtils.getObj(ShopModule.goodsInfoId, String.valueOf(id), ShopGoodsApi.class);
		}
		return null;
	}

	@Override
	public void removeGoodsInfoRedisCache(Integer id) {
		if (id != null) {
			JedisUtils.deleteStr(ShopModule.goodsInfoId, String.valueOf(id));
		}
	}

}
