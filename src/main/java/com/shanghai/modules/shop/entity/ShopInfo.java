package com.shanghai.modules.shop.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shanghai.common.persistence.BaseEntity;
import com.shanghai.common.utils.excel.ExcelField;

/**
* @author：YeJR
* @version：2018年9月3日 下午2:43:08
* 商店基础信息实体
*/
public class ShopInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 酒店Id
	 */
	private Integer id;
	
	/**
	 * 搜索字段
	 */
	private String searchKey;
	
	/**
	 * 酒店使用者ID
	 */
	private Integer userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 酒店名称
	 */
	private String shopName;
	
	/**
	 * 酒店log
	 */
	private String shopLog;
	
	/**
	 * 经度
	 */
	private String longitude;
	
	/**
	 * 纬度
	 */
	private String latitude;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 联系电话
	 */
	private String mobile;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 状态，0：正常，1：冻结
	 */
	private Integer state;
	
	public ShopInfo() {}
	
	public ShopInfo(Integer id) {
		this.id = id;
	}

	@ExcelField(title="ID", type=1, align=2, sort=1)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonIgnore
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@ExcelField(title="用户名", align=2, sort=2)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ExcelField(title="商店名", align=2, sort=3)
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopLog() {
		return shopLog;
	}

	public void setShopLog(String shopLog) {
		this.shopLog = shopLog;
	}

	@ExcelField(title="经度", align=2, sort=4)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@ExcelField(title="纬度", align=2, sort=5)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@ExcelField(title="地址", align=2, sort=6)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ExcelField(title="电话", align=2, sort=7)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ShopInfo [id=" + id + ", userId=" + userId + ", userName=" + userName + ", shopName=" + shopName
				+ ", shopLog=" + shopLog + ", longitude=" + longitude + ", latitude=" + latitude + ", address="
				+ address + ", mobile=" + mobile + ", createTime=" + createTime + ", description=" + description
				+ ", state=" + state + "]";
	}

}
