package com.cache.ws.exit.dto;

public class ExitCountResult {

	/**访问地址*/
	private String url;
	/**退出次数*/
	private Integer exitCount;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getExitCount() {
		return exitCount;
	}
	public void setExitCount(Integer exitCount) {
		this.exitCount = exitCount;
	}
	
}
