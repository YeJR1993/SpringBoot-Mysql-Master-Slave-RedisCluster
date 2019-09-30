package com.shanghai.webapi.shop.thread.worker;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import com.shanghai.common.persistence.Request;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;
import com.shanghai.webapi.shop.request.process.ShopGoodsCacheRefresh;
import com.shanghai.webapi.shop.request.process.ShopGoodsUpdate;
import com.shanghai.webapi.shop.request.queue.ShopGoodsQueue;

/**
 * @author YeJR
 * @version 2018年8月28日 下午8:55:11
 * worker线程，不断的监控自己的queue
 */
public class ShopGoodsProcessorThread implements Callable<Boolean>{

	/**
	 * 监控的请求队列
	 */
	private ArrayBlockingQueue<Request> queue;
	
	/**
	 * 在初始化worker线程的时候，传入需要绑定的请求队列
	 * @param queue
	 */
	public ShopGoodsProcessorThread(ArrayBlockingQueue<Request> queue) {
		this.queue = queue;
	}
	
	/**
	 * 消费请求，不停的监听绑定的队列
	 */
	@Override
	public Boolean call() throws Exception {
		try {
			while(true) {
				// 获取ArrayBlockingQueue中的请求
				ShopGoodsRequest request = (ShopGoodsRequest) queue.take();
				// 内存队列
				ShopGoodsQueue shopGoodsQueue = ShopGoodsQueue.getInstance();
				// 内存队列中的标志Map
				Map<Integer, Boolean> flagMap = shopGoodsQueue.getFlagMap();
				
				if (request instanceof ShopGoodsUpdate) {
					// 如果是一个更新数据库的请求，那么就将那个productId对应的标识设置为true
					flagMap.put(request.getGoodsId(), true);
				} else if (request instanceof ShopGoodsCacheRefresh) {
					// 如果是刷新请求
					Boolean forceRefresh = ((ShopGoodsCacheRefresh) request).isForceRefresh();
					// 不需要强制刷新的时候，进行去重操作
					if (!forceRefresh) {
						Boolean flag = flagMap.get(request.getGoodsId());
						// 表示 未进行过更新请求和读请求，改变标识，不错操作
						if (flag == null) {
							flagMap.put(request.getGoodsId(), false);
						}
						// 标识不为空，而且是true，就说明之前有一个这个商品的数据库更新请求，改变标识，不错操作
						if (flag != null && flag) {
							flagMap.put(request.getGoodsId(), false);
						}
						// 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
						// 说明前面已经有一个数据库更新请求+一个缓存刷新请求了
						if (flag != null && !flag) {
							// 对于这种读请求，直接就过滤掉，不要用继续进行process操作
							return true;
						}
						
					}
				}
				// 进行两种请求的操作	
				request.process();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public ArrayBlockingQueue<Request> getQueue() {
		return queue;
	}

	public void setQueue(ArrayBlockingQueue<Request> queue) {
		this.queue = queue;
	}
	
}
