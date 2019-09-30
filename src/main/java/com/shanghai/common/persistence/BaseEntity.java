package com.shanghai.common.persistence;

import java.io.Serializable;
import java.util.Date;

/** 
* @author: YeJR 
* @version: 2018年10月22日 下午2:54:16
* 基础类
*/
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获取数据的时间
	 */
	private Date acquireDataTime;

	public Date getAcquireDataTime() {
		return acquireDataTime;
	}

	public void setAcquireDataTime(Date acquireDataTime) {
		this.acquireDataTime = acquireDataTime;
	}
	
}
