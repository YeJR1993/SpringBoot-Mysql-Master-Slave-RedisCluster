package com.shanghai.modules.shop.entity;

import java.math.BigDecimal;

import com.shanghai.common.persistence.BaseEntity;

/**
* @author：YeJR
* @version：2018年9月18日 下午3:07:54
* 订单商品
*/
public class ShopOrderGoods extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	private Integer id;
	
	/**
	 * 订单ID
	 */
	private Integer orderId;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品图片
	 */
	private String picture;
	
	/**
	 * 销售时单价
	 */
	private BigDecimal salePrice;
	
	/**
	 * 购买数量
	 */
	private Integer buyNum;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	@Override
	public String toString() {
		return "ShopOrderGoods [id=" + id + ", orderId=" + orderId + ", goodsName=" + goodsName + ", picture=" + picture
				+ ", salePrice=" + salePrice + ", buyNum=" + buyNum + "]";
	}

}
