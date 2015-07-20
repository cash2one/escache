package com.cache.ws.es.dto;

/**
 * @author TomDing
 *
 */
public class EsResultData {

	private String key_as_string;

	private String key;

	private String doc_count;
	/** 浏览量 */
	private EsValue pv;
	/** 访问次数 */
	private EsValue vc;
	/** 访客数 */
	private EsValue uv;
	/** 新访客数 */
	private EsValue nuv;
	/** 新访客比率 */
	private EsValue nuvRate;
	/** IP数 */
	private EsValue ip;
	/** 跳出率 */
	private EsValue outRate;
	/** 平均访问时长 */
	private EsValue avgTime;
	/** 平均访问页数 */
	private EsValue avgPage;

	public EsValue getPv() {
		return pv;
	}

	public void setPv(EsValue pv) {
		this.pv = pv;
	}

	public String getKey_as_string() {
		return key_as_string;
	}

	public void setKey_as_string(String key_as_string) {
		this.key_as_string = key_as_string;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDoc_count() {
		return doc_count;
	}

	public void setDoc_count(String doc_count) {
		this.doc_count = doc_count;
	}

	public EsValue getVc() {
		return vc;
	}

	public void setVc(EsValue vc) {
		this.vc = vc;
	}

	public EsValue getUv() {
		return uv;
	}

	public void setUv(EsValue uv) {
		this.uv = uv;
	}

	public EsValue getNuv() {
		return nuv;
	}

	public void setNuv(EsValue nuv) {
		this.nuv = nuv;
	}

	public EsValue getNuvRate() {
		return nuvRate;
	}

	public void setNuvRate(EsValue nuvRate) {
		this.nuvRate = nuvRate;
	}

	public EsValue getIp() {
		return ip;
	}

	public void setIp(EsValue ip) {
		this.ip = ip;
	}

	public EsValue getOutRate() {
		return outRate;
	}

	public void setOutRate(EsValue outRate) {
		this.outRate = outRate;
	}

	public EsValue getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(EsValue avgTime) {
		this.avgTime = avgTime;
	}

	public EsValue getAvgPage() {
		return avgPage;
	}

	public void setAvgPage(EsValue avgPage) {
		this.avgPage = avgPage;
	}
}
