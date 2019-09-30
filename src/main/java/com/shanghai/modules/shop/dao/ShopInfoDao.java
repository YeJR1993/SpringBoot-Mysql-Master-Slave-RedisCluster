package com.shanghai.modules.shop.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shanghai.modules.shop.entity.ShopInfo;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:07:48
* 商店基础信息
*/
@Mapper
public interface ShopInfoDao {
	
	/**
	 * 通过酒店ID获取商店信息
	 * @param shopInfo
	 * @return
	 */
	public ShopInfo getById(ShopInfo shopInfo);
	
	/**
	 * 通过userId获取商店信息
	 * @param shopInfo
	 * @return
	 */
	public ShopInfo getByUserId(ShopInfo shopInfo);
	
	/**
	 * 分页查询商店信息
	 * @param shopInfo
	 * @return
	 */
	public Page<ShopInfo> findList(ShopInfo shopInfo);

	/**
	 * 保存商店信息
	 * @param shopInfo
	 * @return
	 */
	public Integer insert(ShopInfo shopInfo);
	
	/**
	 * 更新商店信息
	 * @param shopInfo
	 * @return
	 */
	public Integer update(ShopInfo shopInfo);
	
	/**
	 * 逻辑删除酒店信息
	 * @param shopInfo
	 * @return
	 */
	public Integer deleteByLogic(ShopInfo shopInfo);
}
