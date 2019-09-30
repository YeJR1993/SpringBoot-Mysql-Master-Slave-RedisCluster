package com.shanghai.webapi.shop.request.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.shanghai.common.persistence.Request;

/**
* @author: YeJR 
* @version: 2018年8月24日 上午11:28:18
* 内存队列对象
* 采用单例模式，使用静态内部类的方式保证请求内存队列的线程安全
*/
public class ShopGoodsQueue {
	
	/**
	 * list中装有很多的BlockingQueue并且在线程池初始化的时候与线程完成绑定，BlockingQueue中用来存放接收请求
	 * ************************************* 
	 * *	内存队列list					   *
	 * * 	 -------------------------	   *	
	 * *	| 塞request的BlockingQueue|	   *
	 * *     -------------------------     *
	 * * 	 -------------------------	   *	
	 * *	| 塞request的BlockingQueue|	   *
	 * *     -------------------------     *
	 * * 	 -------------------------	   *	
	 * *	| 塞request的BlockingQueue|	   *
	 * *     -------------------------     *
	 * *								   *
	 * *								   *
	 * *************************************
	 */
	private List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();
	
	/**
	 * 标识位flagMap
	 */
	private Map<Integer, Boolean> flagMap = new ConcurrentHashMap<Integer, Boolean>();
	
	/**
	 * JVM的机制去保证多线程安全
	 * 即：内部类的初始化，一定只发生一次，不论多少线程并发初始化
	 * 保证内存中只有一个RequestQueue对象，自然也只有一个queues变量
	 * @return
	 */
	public static ShopGoodsQueue getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * 向list中添加一个queue
	 * @param queue
	 */
	public void addQueue(ArrayBlockingQueue<Request> queue) {
		this.queues.add(queue);
	}
	
	/**
	 * 获取处理商品库存的内存队列的长度
	 * @return
	 */
	public int goodsStockQueueSize() {
		return queues.size();
	}
	
	/**
	 * 获取商品库存内存队列中的某一位置上的一个
	 * @param index
	 * @return
	 */
	public ArrayBlockingQueue<Request> getQueue(int index) {
		return queues.get(index);
	}
	
	/**
	 * 获取内存队列中的标识位Map
	 * @return
	 */
	public Map<Integer, Boolean> getFlagMap() {
		return flagMap;
	}
	
	/**
	 * @author YeJR
	 * @version 2018年8月28日 下午9:01:12
	 * 采用静态内部类的方式，去初始化单例，保证线程绝对安全
	 * 利用类中静态变量的唯一性
	 */
	private static class Singleton {
		
		private static ShopGoodsQueue goodsStockQueue;
		
		static {
			goodsStockQueue = new ShopGoodsQueue();
		}
		
		public static ShopGoodsQueue getInstance() {
			return goodsStockQueue;
		}
	}

}
