package com.shanghai.webapi.shop.kafka;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.shanghai.common.utils.DateTimeUtils;
import com.shanghai.common.utils.constant.KafkaConstants;
import com.shanghai.common.utils.constant.ZookeeperConstants;
import com.shanghai.common.zookeeper.ZooKeeperSession;
import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.service.ShopGoodsApiService;

/**
* @author：YeJR
* @version：2018年9月29日 上午9:57:48
* 消息消费者
*/
@Component
public class KafkaShopConsumer {
	
	private final static Logger logger = LoggerFactory.getLogger(KafkaShopConsumer.class);
	
	@Autowired
	private ShopGoodsApiService shopGoodsApiService;

	@KafkaListener(topics = {KafkaConstants.TOPIC_SHOP_CHANGE})
    public void shopGoodsListen (ConsumerRecord<?, ?> record) throws Exception {
		// jdk1.8 record.value() 如果是null，则返回一个空的（Optional）messageValue对象
		Optional<?> messageKey = Optional.ofNullable(record.key());
        Optional<?> messageValue = Optional.ofNullable(record.value());
        // 相当于 record.value() != null
        if (messageValue.isPresent() && messageKey.isPresent()) {
        	String msgKey = String.valueOf(messageKey.get());
            String msgVal = String.valueOf(messageValue.get());
            // 转换为json对象
            JSONObject msgObject = JSONObject.parseObject(msgVal);
            // 若是该消息的key 表示是商店的商品信息变更
            if (KafkaConstants.KEY_SHOP_GOODS_CHANGE.equals(msgKey)) {
            	Integer goodsId = msgObject.getInteger("id");
            	// 在写入缓存之前，应该获取zookeeper的分布式锁
    			ZooKeeperSession zooKeeperSession = ZooKeeperSession.getInstance();
    			// 这里如果没有获取到锁，线程会hang住，进入阻塞状态
    			zooKeeperSession.acquireDistributedLock(ZookeeperConstants.SHOP_GOODS_LOCK, goodsId);
            	try {
            		// 获取到分布式锁， 进行缓存更新
					processShopGoodsChangeMessage(goodsId);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放zookeeper 分布式锁
					zooKeeperSession.releaseDistributedLock(ZookeeperConstants.SHOP_GOODS_LOCK, goodsId);
				}
			}
            
        }
    }
	
	/**
	 * 处理商品变更的消息
	 * @param msgObject
	 */
	private void processShopGoodsChangeMessage(Integer goodsId) {
		ShopGoodsApi shopGoodsApi = shopGoodsApiService.getByShopGoodsId(new ShopGoodsApi(goodsId));
		if (shopGoodsApi != null) {
			// 获取缓存中的数据
			ShopGoodsApi goodsInfoLocalCache = shopGoodsApiService.getGoodsInfoLocalCache(goodsId);
			ShopGoodsApi goodsInfoRedisCache = shopGoodsApiService.getGoodsInfoRedisCache(goodsId);
			// 比较时间版本，如果新获取的数据的时间版本比缓存中的新，就更新缓存
			if (goodsInfoLocalCache != null && goodsInfoLocalCache.getAcquireDataTime() != null) {
				if (DateTimeUtils.compareTime(goodsInfoLocalCache.getAcquireDataTime(), shopGoodsApi.getAcquireDataTime())) {
					shopGoodsApiService.setGoodsInfoLocalCache(shopGoodsApi);
				}
			} else {
				shopGoodsApiService.setGoodsInfoLocalCache(shopGoodsApi);
			}
			if (goodsInfoRedisCache != null && goodsInfoRedisCache.getAcquireDataTime() != null) {
				if (DateTimeUtils.compareTime(goodsInfoRedisCache.getAcquireDataTime(), shopGoodsApi.getAcquireDataTime())) {
					shopGoodsApiService.setGoodsInfoRedisCache(shopGoodsApi);
				}
			} else {
				shopGoodsApiService.setGoodsInfoRedisCache(shopGoodsApi);
			}
		}
		logger.info("ehcache缓存的商品信息:{}", shopGoodsApiService.getGoodsInfoLocalCache(goodsId));
		logger.info("redis缓存的商品信息:{}", shopGoodsApiService.getGoodsInfoRedisCache(goodsId));
	}
}
