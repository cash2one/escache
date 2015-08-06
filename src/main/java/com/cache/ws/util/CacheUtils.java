package com.cache.ws.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cache.ws.es.dto.EsResultData;
import com.cache.ws.es.dto.IndicatorData;

public class CacheUtils {

	public static List<IndicatorData> convert(List<EsResultData> esResultDatas) {

		List<IndicatorData> resultData = new ArrayList<IndicatorData>();

		if (esResultDatas != null && esResultDatas.size() > 0) {
			new NullPointerException(
					"CacheUtils.convert: Please check esResultDatas");
		}

		for (EsResultData esResultData : esResultDatas) {
			IndicatorData indicatorData = new IndicatorData();

			indicatorData.setKey_as_string(esResultData.getKey_as_string());

			if (esResultData.getIp() != null
					&& esResultData.getIp().getAggs() != null) {
				indicatorData.setIp(esResultData.getIp().getAggs().getValue());
			}

			if (esResultData.getPv() != null) {
				indicatorData.setPv(esResultData.getPv().getValue());
			}
			if (esResultData.getUv() != null) {

				indicatorData.setUv(esResultData.getUv().getValue());
			}

			if (esResultData.getVc() != null
					&& esResultData.getVc().getAggs() != null) {

				indicatorData.setVc(esResultData.getVc().getAggs().getValue());
			}

			if (esResultData.getUv_filter() != null
					&& esResultData.getUv_filter().getAggs() != null) {
				indicatorData.setUv_filter(esResultData.getUv_filter()
						.getAggs().getValue());
			}

			if (esResultData.getNew_visitor_aggs() != null
					&& esResultData.getNew_visitor_aggs().getAggs() != null) {

				indicatorData.setNew_visitor_aggs(esResultData
						.getNew_visitor_aggs().getAggs().getValue());
			}

			if (esResultData.getTvt() != null) {

				indicatorData.setTvt(esResultData.getTvt());

			}

			if (esResultData.getSingle_visitor_aggs() != null
					&& esResultData.getSingle_visitor_aggs().getBuckets() != null) {

				indicatorData.setSingle_visitor_aggs(esResultData
						.getSingle_visitor_aggs().getBuckets().size());
			}

			resultData.add(indicatorData);

		}
		return resultData;
	}

	public static boolean isSingleIndex(long start, long end) {

		if (start == end) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static String[] createIndexes(Long startDay, Long endDay,
			String prefix) {

		if (startDay == null || endDay == null) {
			new NullPointerException(
					"CacheUtils.CreateIndexes: Please check startDay or endDay.");
		}

		int dayMills = 24 * 60 * 60 * 1000;

		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setSeconds(0);

		Long startTime = date.getTime();

		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		date.setSeconds(999);

		Long endTime = date.getTime();

		startTime = startTime + startDay * dayMills;
		endTime = endTime + endDay * dayMills;

		Double number = Math.floor((endTime - startTime) / dayMills);

		String[] dates = new String[number.intValue()];
		Date start = new Date(startTime);
		for (int i = 0; i < number; i++) {

			if (i == 0) {
				start.setDate(start.getDate());
			} else {
				start.setDate(start.getDate() + 1);
			}

			dates[i] = (prefix + String.format("%tF", start));
		}

		return dates;
	}

	public static void main(String[] args) {
		String[] test = createIndexes(new Long(0), new Long(0), "access-");

		for (String s : test) {
			System.out.println(s);
		}
	}
}
