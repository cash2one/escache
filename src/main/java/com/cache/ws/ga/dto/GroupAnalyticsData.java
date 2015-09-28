package com.cache.ws.ga.dto;

public class GroupAnalyticsData {

	/** 客户 */
	private String type;
	/*** userId */
	private String userId;
	/** 浏览量 */
	private String pv;
	/**是否新客户*/
	private int isNew;

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

}
