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
	private String nuvRate = "0";
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
	private String avgPage = "0";

	private int outVcAggs = 0;

	public int getOutVcAggs() {
		return outVcAggs;
	}

	public void setOutVcAggs(int outVcAggs) {
		this.outVcAggs = outVcAggs;
	}

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

	public String getNuvRate() {

		if (uv_filter > 0) {

			float nuvRate = new Float(new_visitor_aggs) / new Float(uv_filter)
					* new Float(100);

			return String.format("%.2f ", nuvRate);
		}

		return nuvRate;
	}

	public void setNuvRate(String nuvRate) {
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

		int svc = outVcAggs - single_visitor_aggs;

		if (outVcAggs > 0) {
			return new Float(svc) / new Float(outVcAggs);
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
			return (long) Math.ceil(new Float(tvt) / 1000 / new Float(vc));
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

	public String getAvgPage() {

		Float pv = Float.valueOf(this.pv);

		Float vc = Float.valueOf(this.vc);
		Float avgPage = new Float("0");
		if (uv > 0) {
			avgPage = (pv / vc);
		}

		return String.format("%.2f ", avgPage);

	}

	public void setAvgPage(String avgPage) {
		this.avgPage = avgPage;
	}

}
