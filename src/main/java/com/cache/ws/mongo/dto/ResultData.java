package com.cache.ws.mongo.dto;

import java.io.Serializable;

public class ResultData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key_as_string;

	/** 浏览量 */
	private int pv = 0;

	/** 访问次数 */
	private int vc = 0;
	/** 访客数 */
	private int uv = 0;

	/** 新访客数 */
	private int nuv = 0;
	private int new_visitor_aggs = 0;

	/** 新访客比率 */
	private float nuvRate = 0;
	private int uv_filter = 0;

	/** IP数 */
	private int ip = 0;

	/** 跳出率 */
	private float outRate = 0;
	private int single_visitor_aggs = 0;

	/** 平均访问时长 */
	private long avgTime = 0;
	private long tvt = 0;
	/** 平均访问页数 */
	private float avgPage = 0;

	public String getKey_as_string() {
		return key_as_string;
	}

	public void setKey_as_string(String key_as_string) {
		this.key_as_string = key_as_string;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getVc() {
		return vc;
	}

	public void setVc(int vc) {
		this.vc = vc;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getNuv() {
		return nuv;
	}

	public void setNuv(int nuv) {
		this.nuv = nuv;
	}

	public int getNew_visitor_aggs() {
		return new_visitor_aggs;
	}

	public void setNew_visitor_aggs(int new_visitor_aggs) {
		this.new_visitor_aggs = new_visitor_aggs;
	}

	public float getNuvRate() {

		if (uv_filter > 0) {
			return new_visitor_aggs / uv_filter * 100;
		}

		return nuvRate;
	}

	public void setNuvRate(float nuvRate) {
		this.nuvRate = nuvRate;
	}

	public int getUv_filter() {
		return uv_filter;
	}

	public void setUv_filter(int uv_filter) {
		this.uv_filter = uv_filter;
	}

	public int getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	public float getOutRate() {
		int svc = vc - single_visitor_aggs;

		if (vc > 0) {
			return svc / vc * 100;
		}

		return outRate;
	}

	public void setOutRate(float outRate) {
		this.outRate = outRate;
	}

	public int getSingle_visitor_aggs() {
		return single_visitor_aggs;
	}

	public void setSingle_visitor_aggs(int single_visitor_aggs) {
		this.single_visitor_aggs = single_visitor_aggs;
	}

	public long getAvgTime() {
		if (vc > 0) {
			return (long) Math.ceil(tvt / 1000 / vc);
		}

		return avgTime;
	}

	public void setAvgTime(long avgTime) {
		this.avgTime = avgTime;
	}

	public long getTvt() {
		return tvt;
	}

	public void setTvt(long tvt) {
		this.tvt = tvt;
	}

	public float getAvgPage() {

		if (uv > 0) {
			return pv / uv * 100;
		}

		return avgPage;
	}

	public void setAvgPage(float avgPage) {
		this.avgPage = avgPage;
	}

}
