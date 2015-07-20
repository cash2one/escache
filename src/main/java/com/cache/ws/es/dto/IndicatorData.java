package com.cache.ws.es.dto;

/**
 * 指标显示
 * 
 * @author TomDing
 *
 */
public class IndicatorData {

	/** 浏览量 */
	private String pv;
	/** 访问次数 */
	private String vc;
	/** 访客数 */
	private String uv;
	/** 新访客数 */
	private String nuv;
	/** 新访客比率 */
	private String nuvRate;
	/** IP数 */
	private String ip;
	/** 跳出率 */
	private String outRate;
	/** 平均访问时长 */
	private String avgTime;
	/** 平均访问页数 */
	private String avgPage;
	/**日期*/
	private String date;
	/**日期格式*/
	private String dateformat;
	
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
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

	public String getNuv() {
		return nuv;
	}

	public void setNuv(String nuv) {
		this.nuv = nuv;
	}

	public String getNuvRate() {
		return nuvRate;
	}

	public void setNuvRate(String nuvRate) {
		this.nuvRate = nuvRate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOutRate() {
		return outRate;
	}

	public void setOutRate(String outRate) {
		this.outRate = outRate;
	}

	public String getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(String avgTime) {
		this.avgTime = avgTime;
	}

	public String getAvgPage() {
		return avgPage;
	}

	public void setAvgPage(String avgPage) {
		this.avgPage = avgPage;
	}

}
