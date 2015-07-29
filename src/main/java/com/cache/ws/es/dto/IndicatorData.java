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

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public void setPvObject(Object pv) {
		if (pv != null) {
			this.pv = pv.toString();
		}
	}

	public String getVc() {
		return vc;
	}

	public void setVc(String vc) {
		this.vc = vc;
	}

	public void setVcObject(Object vc) {
		if (vc != null) {
			this.vc = vc.toString();
		}
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public void setUvObject(Object uv) {
		if (uv != null) {
			this.uv = uv.toString();
		}
	}

	public String getNuv() {
		return nuv;
	}

	public void setNuv(String nuv) {
		this.nuv = nuv;
	}

	public void setNuvObject(Object nuv) {
		if (nuv != null) {
			this.nuv = nuv.toString();
		}
	}

	public String getNuvRate() {
		return nuvRate;
	}

	public void setNuvRate(String nuvRate) {
		this.nuvRate = nuvRate;
	}

	public void setNuvRateObject(Object nuvRate) {
		if (nuvRate != null) {
			this.nuvRate = nuvRate.toString();
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setIpObject(Object ip) {
		if (ip != null) {
			this.ip = ip.toString();
		}
	}

	public String getOutRate() {
		return outRate;
	}

	public void setOutRate(String outRate) {
		this.outRate = outRate;
	}

	public void setOutRateObject(Object outRate) {
		if (outRate != null) {
			this.outRate = outRate.toString();
		}
	}

	public String getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(String avgTime) {
		this.avgTime = avgTime;
	}

	public void setAvgTimeObject(Object avgTime) {
		if (avgTime != null) {
			this.avgTime = avgTime.toString();
		}
	}

	public String getAvgPage() {
		return avgPage;
	}

	public void setAvgPage(String avgPage) {
		this.avgPage = avgPage;
	}

	public void setAvgPageObject(Object avgPage) {
		if (avgPage != null) {
			this.avgPage = avgPage.toString();
		}
	}

}
