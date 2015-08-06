package com.cache.ws.es.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cache.ws.util.FastJsonUtils;

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
	private Aggs vc;
	/** 访客数 */
	private EsValue uv;

	/** 新访客数 */
	private String nuv;
	private Aggs new_visitor_aggs;

	/** 新访客比率 */
	private String nuvRate;
	private Aggs uv_filter;

	/** IP数 */
	private Aggs ip;

	/** 跳出率 */
	private String outRate;
	private Aggs single_visitor_aggs;

	/** 平均访问时长 */
	@SuppressWarnings("unused")
	private String avgTime;
	private Aggs tvt_aggs;
	private Long tvt = new Long("0");

	/** 平均访问页数 */
	private String avgPage;

	public void setTvt(Long tvt) {
		this.tvt = tvt;
	}

	public Aggs getSingle_visitor_aggs() {
		return single_visitor_aggs;
	}

	public void setSingle_visitor_aggs(Aggs single_visitor_aggs) {
		this.single_visitor_aggs = single_visitor_aggs;
	}

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

	public Aggs getVc() {
		return vc;
	}

	public void setVc(Aggs vc) {
		this.vc = vc;
	}

	public EsValue getUv() {
		return uv;
	}

	public void setUv(EsValue uv) {
		this.uv = uv;
	}

	public Aggs getNew_visitor_aggs() {
		return new_visitor_aggs;
	}

	public void setNew_visitor_aggs(Aggs new_visitor_aggs) {
		this.new_visitor_aggs = new_visitor_aggs;
	}

	public String getNuv() {

		if (StringUtils.isBlank(this.nuv)) {
			setNuv(getNew_visitor_aggs().getAggs().getValue());
		}

		return nuv;
	}

	public void setNuv(String nuv) {
		this.nuv = nuv;
	}

	public String getNuvRate() {

		Float nRate = new Float(0);
		if (StringUtils.isBlank(this.nuvRate)) {

			Float nuv = Float.valueOf(this.new_visitor_aggs.getAggs()
					.getValue());
			Float nv = Float.valueOf(this.uv_filter.getAggs().getValue());

			if (Float.valueOf(nv) > 0) {
				nRate = (nuv.floatValue() / nv.floatValue() * 100);
			}

		}

		return String.format("%10.2f%%", nRate);
	}

	public String getAvgPage() {

		Float avgPage = new Float(0);
		if (StringUtils.isBlank(this.avgPage)) {

			Float pv = Float.valueOf(this.getPv().getValue());

			Float uv = Float.valueOf(this.getUv().getValue());

			if (uv > 0) {

				avgPage = (pv / uv * 100);
			}

		}

		return String.format("%10.2f%%", avgPage);

	}

	public String getOutRate() {
		Float orate = new Float(0);
		if (StringUtils.isBlank(this.outRate)) {

			Float vc = Float.valueOf(this.vc.getAggs().getValue());

			Float svc = vc - single_visitor_aggs.getBuckets().size();

			if (vc > 0) {
				orate = (svc / vc * 100);
			}

		}

		return String.format("%10.2f%%", orate);
	}

	public void setOutRate(String outRate) {
		this.outRate = outRate;
	}

	public String getAvgTime() {

		Long tvt = getTvt();

		Float vc = Float.valueOf(this.vc.getAggs().getValue());

		Float avgTime = new Float("0");
		if (vc > 0) {
			avgTime = (float) Math.ceil(tvt / 1000 / vc);
		}

		return avgTime.toString();
	}

	public void setAvgTime(String avgTime) {
		this.avgTime = avgTime;
	}

	public void setAvgPage(String avgPage) {
		this.avgPage = avgPage;
	}

	public Aggs getUv_filter() {
		return uv_filter;
	}

	public void setUv_filter(Aggs uv_filter) {
		this.uv_filter = uv_filter;
	}

	public void setNuvRate(String nuvRate) {

		this.nuvRate = nuvRate;
	}

	public Aggs getIp() {
		return ip;
	}

	public void setIp(Aggs ip) {
		this.ip = ip;
	}

	public Aggs getTvt_aggs() {
		return tvt_aggs;
	}

	public void setTvt_aggs(Aggs tvt_aggs) {
		this.tvt_aggs = tvt_aggs;
	}

	public Long getTvt() {

		List<Map<String, Object>> buckets = tvt_aggs.getBuckets();

		if (buckets != null && buckets.size() == 0) {
			return new Long(0);
		}

		for (Map<String, Object> bucket : buckets) {
			Map<String, Object> maxMap = FastJsonUtils.json2map(bucket.get(
					"max_aggs").toString());
			BigDecimal max = new BigDecimal(maxMap.get("value").toString());

			Map<String, Object> minMap = FastJsonUtils.json2map(bucket.get(
					"min_aggs").toString());

			BigDecimal min = new BigDecimal(minMap.get("value").toString());

			tvt += max.subtract(min).longValue();
		}

		tvt = tvt / buckets.size();

		return tvt;
	}

}
