package com.shanghai.common.zookeeper;
/** 
* @author: YeJR 
* @version: 2018年10月22日 上午9:52:21
* ZookeeperSession
*/

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghai.common.utils.YmlLoadUtil;
import com.shanghai.common.utils.constant.SysConstants;

public class ZooKeeperSession {
	
	private final static Logger logger = LoggerFactory.getLogger(ZooKeeperSession.class);
	/**
	 * CountDownLatch
	 * java多线程并发同步的一个工具类，会传递一些数字如 1，2，3
	 * 然后调用await()，若数字不是0，就会等待
	 * 其他的线程可以调用countDown()，减1，若数字减到0，那么其他所有在等待的线程，就会跳出阻塞状态
	 */
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	private ZooKeeper zookeeper;
	
	/**
	 * 初始化
	 */
	public ZooKeeperSession() {
		try {
			// 连接zookeeper server， 创建会话连接的时候，是异步的
			// 所以需要给一个监听器（watcher），告诉我们是什么时候与zookeeper server 连接
			this.zookeeper = new ZooKeeper(YmlLoadUtil.getInstance().getProperty("zooKeeper").toString(), 
					SysConstants.ZOOKEEPER_SESSION_TIMEOUT, new ZooKeeperWatcher());
			// 异步的连接，会给一个状态CONNECTING，连接中
			logger.info(zookeeper.getState().toString());
			// 调用await()，若数字不是0，就会等待
			// 在这里 ZooKeeperWatcher中调用了countDown()，使其变0，然后代码继续向下走
			connectedSemaphore.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 走到这里，表示连接成功
		logger.info("Zookeeper session established...");
	}
	
	/**
	 * 获取分布式锁
	 * @param path
	 * @param key
	 */
	public void acquireDistributedLock(String path, Object key) {
		path = path + key;
		try {
			// zk 通过创建一个临时唯一的node, 如果创建成功表示锁获取成功
			zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.info("success to acquire lock for ["+ path +"]"); 
		} catch (Exception exception) {
			int count = 0;
			// 如果创建node 出错NodeExistsException，表示别的线程正在使用该锁
			while (true) {
				try {
					// 每隔20ms再去尝试获取锁（创建node）
					Thread.sleep(20);
					zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch (Exception e) {
					// 如果再次创建失败,继续
					count++;
					continue;
				}
				// 创建成功就退出
				logger.info("success to acquire lock for ["+ path +"] after " + count + " times try......");
				break;
			}
			
		}
		
	}
	
	/**
	 * 释放分布式锁
	 * @param path
	 * @param key
	 */
	public void releaseDistributedLock(String path, Object key) {
		path = path + key;
		try {
			zookeeper.delete(path, -1);
			logger.info("release the lock for [" + path + "]......");  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author YeJR
	 * ZooKeeperWatcher 建立连接之后的回调类
	 */
	private class ZooKeeperWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			// 与zookeeper建立连接之后，会传入一个WatchedEvent
			logger.info("receive watched event:" + event.getState());
			// 当状态是SyncConnected的时候表示可以连接
			if (KeeperState.SyncConnected == event.getState()) {
				// 调用countDown()，减1
				connectedSemaphore.countDown();
			}
		}
		
	}
	
	/**
	 * @author YeJR
	 * 封装单例的静态内部类,用来初始化ZooKeeperSession
	 */
	private static class Singleton {
		
		private static ZooKeeperSession instance;
		
		static {
			instance = new ZooKeeperSession();
		}
		
		private static ZooKeeperSession getInstance() {
			return instance;
		}
		
	}
	
	/**
	 * 获取单例，初始化ZookeeperSession，保证内存中只有一个对象，保证线程安全
	 * @return
	 */
	public static ZooKeeperSession getInstance() {
		return Singleton.getInstance();
	}
	
	
}
