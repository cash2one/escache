package com.cache.ws.es.dto;

/**
 * 指标显示
 * 
 * @author TomDing
 *
 */
public class IndicatorData {

	private String key_as_string;
	/** 浏览量 */
	private String pv;
	/** 访问次数 */
	private String vc;
	/** 访客数 */
	private String uv;
	/** 新访客数需要的参数 */
	private String new_visitor_aggs;
	/** 新访客比率 需要的参数 */
	private String uv_filter;
	/** IP数 */
	private String ip;
	/** 跳出率 需要的参数 */
	private int single_visitor_aggs;
	/** 平均访问时长需要的参数 */
	private long tvt;

	public String getKey_as_string() {
		return key_as_string;
	}

	public void setKey_as_string(String key_as_string) {
		this.key_as_string = key_as_string;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getVc() {
		return vc;
	}

	public void setVc(String vc) {
		this.vc = vc;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getNew_visitor_aggs() {
		return new_visitor_aggs;
	}

	public void setNew_visitor_aggs(String new_visitor_aggs) {
		this.new_visitor_aggs = new_visitor_aggs;
	}

	public String getUv_filter() {
		return uv_filter;
	}

	public void setUv_filter(String uv_filter) {
		this.uv_filter = uv_filter;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getSingle_visitor_aggs() {
		return single_visitor_aggs;
	}

	public void setSingle_visitor_aggs(int single_visitor_aggs) {
		this.single_visitor_aggs = single_visitor_aggs;
	}

	public long getTvt() {
		return tvt;
	}

	public void setTvt(long tvt) {
		this.tvt = tvt;
	}
}
