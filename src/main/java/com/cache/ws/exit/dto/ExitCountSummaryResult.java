package com.cache.ws.exit.dto;

public class ExitCountSummaryResult {

	/** 客户 */
	private String type;
	/** 退出次数 */
	private Integer ec;

	public Integer getEc() {
		return ec;
	}

	public void setEc(Integer ec) {
		this.ec = ec;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
