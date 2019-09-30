package com.shanghai.modules.shop.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shanghai.common.persistence.BaseEntity;
import com.shanghai.common.utils.excel.ExcelField;

/**
* @author：YeJR
* @version：2018年8月31日 下午3:30:56
* 商店订单实体
*/
public class ShopOrder extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	private Integer id;
	
	/**
	 * 搜索key
	 */
	private String searchKey;
	
	/**
	 * 订单编号
	 */
	private String orderNumber;
	
	/**
	 * 商店ID
	 */
	private Integer shopId;
	
	/**
	 * 商店名称
	 */
	private String shopName;
	
	/**
	 * 收货人姓名
	 */
	private String receivingName;
	
	/**
	 * 收货人电话
	 */
	private String receivingPhone;
	
	/**
	 * 收货人地址
	 */
	private String receivingAddress;
	
	/**
	 * 支付价
	 */
	private BigDecimal actualPrice;

	/**
	 * 快递单号
	 */
	private String expressNumber;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 支付时间
	 */
	private Date payTime;
	
	/**
	 * 配送时间
	 */
	private Date deliveryTime;
	
	/**
	 * 状态（0：未支付，1：待配送，2：已完成）
	 */
	private Integer state;
	
	/**
	 * 该订单对应的商品
	 */
	private ArrayList<ShopOrderGoods> orderGoodsList;
	
	
	public ShopOrder() {}

	
	public Integer getId() {
		return id;
	}

	@JsonIgnore
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ExcelField(title="订单编号", align=2, sort=1)
	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@ExcelField(title="酒店编号", align=2, sort=2)
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

	@ExcelField(title="收货人姓名", align=2, sort=3)
	public String getReceivingName() {
		return receivingName;
	}

	public void setReceivingName(String receivingName) {
		this.receivingName = receivingName;
	}

	@ExcelField(title="收货人电话", align=2, sort=4)
	public String getReceivingPhone() {
		return receivingPhone;
	}

	public void setReceivingPhone(String receivingPhone) {
		this.receivingPhone = receivingPhone;
	}

	@ExcelField(title="收货人地址", align=2, sort=5)
	public String getReceivingAddress() {
		return receivingAddress;
	}

	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}

	@ExcelField(title="支付价", align=2, sort=6)
	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	@ExcelField(title="快递单号", align=2, sort=7)
	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	@ExcelField(title="创建时间", align=2, sort=8)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ExcelField(title="支付时间", align=2, sort=9)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@ExcelField(title="配送时间", align=2, sort=10)
	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public ArrayList<ShopOrderGoods> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(ArrayList<ShopOrderGoods> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	@Override
	public String toString() {
		return "ShopOrder [id=" + id + ", orderNumber=" + orderNumber + ", shopId=" + shopId + ", shopName=" + shopName
				+ ", receivingName=" + receivingName + ", receivingPhone=" + receivingPhone + ", receivingAddress="
				+ receivingAddress + ", actualPrice=" + actualPrice + ", expressNumber=" + expressNumber
				+ ", createTime=" + createTime + ", payTime=" + payTime + ", deliveryTime=" + deliveryTime + ", state="
				+ state + ", orderGoodsList=" + orderGoodsList + "]";
	}

	
}
