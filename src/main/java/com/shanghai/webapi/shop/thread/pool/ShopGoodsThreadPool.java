package com.shanghai.webapi.shop.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.shanghai.common.persistence.Request;
import com.shanghai.webapi.shop.request.queue.ShopGoodsQueue;
import com.shanghai.webapi.shop.thread.worker.ShopGoodsProcessorThread;

/**
 * @author YeJR
 * @version 2018年8月23日 下午8:55:11
 * 商品库存请求处理线程池：单例
 */
public class ShopGoodsThreadPool {
	
	/**
	 * 线程池核心池的大小
	 */
	private static int corePoolSize = 10;
	
	/**
	 * 线程池的最大线程数
	 */
	private static int maximumPoolSize = 10;
	
	/**
	 * 线程活动时间
	 */
	private static long keepAliveTime = 5000;
	
	/**
	 * queue的容量
	 */
	private static int queueCapacity = 100;
	
	/**
	 * 创建线程池executor
	 * corePoolSize - 当提交一个任务到线程池时，线程池会创建一个线程来执行任务，即使其他空闲的基本线程能够执行新任务也会创建线程，等到需要执行的任务数大于线程池基本大小时就不再创建。
	 * maximumPoolSize - 线程池的最大线程数。如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是，如果使用了无界的任务队列这个参数就没用了。
	 * keepAliveTime - 线程池的工作线程空闲后，保持存活的时间。所以如果任务很多，并且每个任务执行的时间比较短，可以调大时间，提高线程利用率。
	 * unit - keepAliveTime 的时间单位。
	 * workQueue - 用来储存等待执行任务的队列。 LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列
	 * 			ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。
	 * 			LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。
	 * 			PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
	 * 			DelayQueue： 一个使用优先级队列实现的无界阻塞队列。
	 * 			SynchronousQueue： 一个不存储元素的阻塞队列。
	 * 			LinkedTransferQueue： 一个由链表结构组成的无界阻塞队列。
	 * 			LinkedBlockingDeque： 一个由链表结构组成的双向阻塞队列。
	 * threadFactory - 线程工厂。可以通过线程工厂为每个创建出来的线程设置更有意义的名字，如开源框架guava
	 * handler - 拒绝策略。
	 * 			ThreadPoolExecutor.AbortPolicy: 丢弃任务并抛出RejectedExecutionException异常。 (默认)
	 * 			ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
	 * 			ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
	 * 			ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
	 * 			更多的时候，我们应该通过实现RejectedExecutionHandler 接口来自定义策略，比如记录日志或持久化存储等。
	 * 
	 */
	private ExecutorService executor = new ThreadPoolExecutor(corePoolSize,
			maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(queueCapacity), Executors.defaultThreadFactory(),
			new ThreadPoolExecutor.CallerRunsPolicy());

	/**
	 * 线程池初始化时创建内存队列，添加BlockingQueue并绑定对应的线程
	 * ************************************* 
	 * *	内存队列list					   *
	 * * 	 -------------------------	   *	
	 * *	| BlockingQueue 绑定 处理线程 |	   *
	 * *     -------------------------     *
	 * * 	 -------------------------	   *	
	 * *	| BlockingQueue 绑定 处理线程 |	   *
	 * *     -------------------------     *
	 * * 	 -------------------------	   *	
	 * *	| BlockingQueue 绑定 处理线程 |	   *
	 * *     -------------------------     *
	 * *			    ...				   *
	 * *								   *
	 * *************************************
	 */
	public ShopGoodsThreadPool() {
		// 在线程池初始化的时候，创建唯一的RequestQueue（内存队列）对象
		ShopGoodsQueue requestQueue = ShopGoodsQueue.getInstance();
		// 根据线程的数量创建相同数量的ArrayBlockingQueue，并统一放入RequestQueue对象的list容器中
		for (int i = 0; i < maximumPoolSize; i++) {
			// 创建容量为100的ArrayBlockingQueue
			ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(queueCapacity);
			// 放入RequestQueue对象的list容器中
			requestQueue.addQueue(queue);
			// new RequestProcessorThread(queue)：将该queue对象给工作线程持有一份
			// 通过submit()方法，提交一个请求处理的实例，这个实例将交由线程池中空闲的线程执行
			// 即：一个线程对应一个queue的绑定，放到线程池中
			executor.submit(new ShopGoodsProcessorThread(queue));
		}
	}
	
	/**
	 * JVM的机制去保证多线程安全
	 * 即：内部类的初始化，一定只发生一次，不论多少线程并发初始化
	 * instance = new RequestProcessorThreadPool(); 只会执行一次
	 * @return
	 */
	public static ShopGoodsThreadPool getInstance() {
		return Singleton.getInstance();
	}
	
	/**
	 * @author YeJR
	 * @version 2018年8月23日 下午9:01:12
	 * 采用静态内部类的方式，去初始化单例，保证线程绝对安全
	 * 利用类中静态变量的唯一性
	 */
	private static class Singleton {
		
		private static ShopGoodsThreadPool instance;
		
		static {
			instance = new ShopGoodsThreadPool();
		}
		
		public static ShopGoodsThreadPool getInstance() {
			return instance;
		}
	}
}
