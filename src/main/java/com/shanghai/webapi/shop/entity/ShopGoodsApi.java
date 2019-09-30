package com.shanghai.webapi.shop.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.shanghai.common.persistence.BaseEntity;

/**
* @author：YeJR
* @version：2018年9月3日 下午3:18:39
* 商店商品实体
*/
public class ShopGoodsApi extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商品ID
	 */
	private Integer id;
	
	/**
	 * 商店ID
	 */
	private Integer shopId;
	
	/**
	 * 商店名
	 */
	private String shopName;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品图片
	 */
	private String picture;
	
	/**
	 * 商品售卖价格
	 */
	private BigDecimal salePrice;

	/**
	 * 商品库存
	 */
	private Integer stock;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 描述
	 */
	private String description;
	
	public ShopGoodsApi() {}
	
	public ShopGoodsApi(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ShopGoodsApi [id=" + id + ", shopId=" + shopId + ", shopName=" + shopName + ", goodsName=" + goodsName
				+ ", picture=" + picture + ", salePrice=" + salePrice + ", stock=" + stock + ", createTime="
				+ createTime + ", description=" + description + "]";
	}

	
}
