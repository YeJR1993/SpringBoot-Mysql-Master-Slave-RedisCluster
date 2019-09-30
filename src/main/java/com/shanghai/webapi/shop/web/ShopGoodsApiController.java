package com.shanghai.webapi.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shanghai.common.datasource.ReadDataSource;
import com.shanghai.common.datasource.WriteDataSource;
import com.shanghai.common.persistence.BaseController;
import com.shanghai.common.utils.CodeMsg;
import com.shanghai.common.utils.Result;
import com.shanghai.webapi.shop.entity.ShopGoodsApi;
import com.shanghai.webapi.shop.request.ShopGoodsRequest;
import com.shanghai.webapi.shop.request.process.ShopGoodsCacheRefresh;
import com.shanghai.webapi.shop.request.process.ShopGoodsUpdate;
import com.shanghai.webapi.shop.service.ShopGoodsApiService;

/**
* @author：YeJR
* @version：2018年9月20日 上午10:25:16
* 商品外部接口
*/
@RestController
@RequestMapping(value = "${filterChain}/shop/goods")
public class ShopGoodsApiController extends BaseController{
	
	@Autowired
	private ShopGoodsApiService shopGoodsApiService;
	
 	
	/**
	 * 读取商品的库存
	 * 这类实时性要求较高的数据，采用异步内存队列的方式解决缓存和数据库双写不一致的问题
	 * @param shopGoodsApi
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "getGoodsStock")
	public Result<ShopGoodsApi> getGoodsStock(ShopGoodsApi shopGoodsApi) {
		ShopGoodsApi shopGoodsStock = null;
		try {
			ShopGoodsRequest request = new ShopGoodsCacheRefresh(shopGoodsApi, shopGoodsApiService, false);
			shopGoodsApiService.process(request);
			// 将请求交给service（线程）异步处理之后，就需要while/true 一会，在这里hang朱
			// 去尝试等待前面有商品库存更新的操作，同时刷新缓存操作，将最新的数据放入缓存
			long startTime = System.currentTimeMillis();
			long endTime = 0L;
			long waitTime = 0L;
			
			while(true) {
				// 超过一定时间退出
				if (waitTime > 200) {
					break;
				}
				// 尝试去缓存中获取商品数据
				shopGoodsStock = shopGoodsApiService.getGoodsInfoRedisCache(shopGoodsApi.getId());
				if (shopGoodsStock != null) {
					return Result.success(shopGoodsStock);
				} else {
					// 如果没有获取到数据，歇息20ms，继续
					Thread.sleep(20);
					endTime = System.currentTimeMillis();
					waitTime = endTime - startTime;
				}
			}
			// 尝试从数据库中读取数据
			shopGoodsApi = shopGoodsApiService.getByShopGoodsId(shopGoodsApi);
			if (shopGoodsApi != null) {
				// 代码会运行到这里，只有三种情况：
				// 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false， 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
				// 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行 , 在实际生产环境中，需要优化机器，优化DB）所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
				// 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库
				
				// 强制性的刷新缓存
				request = new ShopGoodsCacheRefresh(shopGoodsApi, shopGoodsApiService, true);
				shopGoodsApiService.process(request);
				return Result.success(shopGoodsApi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	/**
	 * 更新商品库存，采用异步的方式，放入到内存队列中，交给worker线程
	 * @param shopGoodsApi
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "updateGoodsStock")
	public Result<Integer> updateGoodsStock(ShopGoodsApi shopGoodsApi) {
		try {
			ShopGoodsRequest request = new ShopGoodsUpdate(shopGoodsApi, shopGoodsApiService);
			shopGoodsApiService.process(request);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		return Result.success(shopGoodsApi.getStock());
	}
	
	/**
	 * 通过商品Id获取商品信息
	 * @param shopGoodsApi
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "getGoodsInfoById")
	public Result<ShopGoodsApi> getGoodsInfoById(Integer id) {
		ShopGoodsApi shopGoodsApi = null;
		// 先从redis中获取数据
		shopGoodsApi = shopGoodsApiService.getGoodsInfoRedisCache(id);
		// 再从ehcache中获取
		if (shopGoodsApi == null) {
			shopGoodsApi = shopGoodsApiService.getGoodsInfoLocalCache(id);
		}
		// 从数据源获取数据，重建缓存
		if (shopGoodsApi == null) {
			shopGoodsApi = shopGoodsApiService.getByShopGoodsId(new ShopGoodsApi(id));
			shopGoodsApiService.sendRebuildGoodsInfoCacheMsg(shopGoodsApi);
		}
		return Result.success(shopGoodsApi);
	}
	
}
