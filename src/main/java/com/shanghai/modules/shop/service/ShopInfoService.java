package com.shanghai.modules.shop.service;
/**
* @author：YeJR
* @version：2018年9月3日 下午4:10:35
* 商店基础信息
*/

import java.util.List;

import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.entity.ShopInfo;

public interface ShopInfoService {
	
	/**
	 * 通过主键ID获取商家信息
	 * @param id
	 * @return
	 */
	public ShopInfo getShopInfoById(ShopInfo shopInfo);
	
	/**
	 * 通过userId获取商店信息
	 * @param shopInfo
	 * @return
	 */
	public ShopInfo getShopInfoByUserId(ShopInfo shopInfo);
	
	/**
	 * 分页查询酒店信息
	 * @param pageNo
	 * @param pageSize
	 * @param shopInfo
	 * @return
	 */
	public PageInfo<ShopInfo> getShopInfoByPage(int pageNo, int pageSize, ShopInfo shopInfo);
	
	/**
	 * 获取所有酒店信息
	 * @param shopInfo
	 * @return
	 */
	public List<ShopInfo> getAllShops(ShopInfo shopInfo);
	
	/**
	 * 保存商店信息
	 * @param shopInfo
	 * @return
	 */
	public Integer saveShopInfo(ShopInfo shopInfo);
	
	/**
	 * 更新商店信息
	 * @param shopInfo
	 * @return
	 */
	public Integer updateShopInfo(ShopInfo shopInfo);
	
	/**
	 * 逻辑删除
	 * @param id
	 */
	public void deleteLogicById(Integer id);
	
	/**
	 * 批量逻辑删除
	 * @param ids
	 */
	public void deleteLogicByIds(Integer[] ids);
}
