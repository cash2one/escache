package com.cache.ws.exit.dto;

public class ExitCountResult {

	/** 访问地址 */
	private String url;
	/** 退出次数 */
	private Integer ec;

	public Integer getEc() {
		return ec;
	}

	public void setEc(Integer ec) {
		this.ec = ec;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
