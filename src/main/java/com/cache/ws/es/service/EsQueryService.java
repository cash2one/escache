package com.cache.ws.es.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Component;

import com.cache.ws.es.ESConfiguration;
import com.cache.ws.es.dto.EsResultData;
import com.cache.ws.es.dto.IndicatorData;
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;
import com.cache.ws.util.FreeMarkerUtils;

@Component
public class EsQueryService {

	@Inject
	private ESConfiguration esConfiguration;

	public List<IndicatorData> queryDataindexTable(String start, String end,
			String dsl, String index, String[] types) throws Exception {
		List<IndicatorData> resultData = new ArrayList<IndicatorData>();
		JestClient client = ESConfiguration.getInstance().getClient();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("start", start);
		data.put("end", end);

		String query = FreeMarkerUtils.getDSL(data, dsl);

		Search search = (Search) new Search.Builder(query)
		// multiple index or types can be added.
				.addIndex(index).addType(Arrays.asList(types)).build();

		JestResult result = client.execute(search);

		System.out.println(result.getJsonObject()
				.getAsJsonObject("aggregations").getAsJsonObject("result")
				.getAsJsonArray("buckets").toString());

		List<EsResultData> list = FastJsonUtils.json2list(
				result.getJsonObject().getAsJsonObject("aggregations")
						.getAsJsonObject("result").getAsJsonArray("buckets")
						.toString(), EsResultData.class);

		for (EsResultData esResultData : list) {
			IndicatorData indicatorData = new IndicatorData();

			indicatorData.setAvgPage(esResultData.getAvgPage());
			// indicatorData.setAvgTime(esResultData.getAvgTime().getValue());
			// indicatorData.setIp(esResultData.getIp().getAggs().getValue());
			indicatorData.setNuv(esResultData.getNuv());
			indicatorData.setNuvRate(esResultData.getNuvRate());
			indicatorData.setOutRate(esResultData.getOutRate());
			indicatorData.setPv(esResultData.getPv().getValue());
			indicatorData.setUv(esResultData.getUv().getValue());
			indicatorData.setVc(esResultData.getVc().getAggs().getValue());
			resultData.add(indicatorData);

		}

		return resultData;

	}

	public List<IndicatorData> queryDataindexTable(RestPraram praram)
			throws Exception {

		List<IndicatorData> resultData = new ArrayList<IndicatorData>();
		JestClient client = ESConfiguration.getInstance().getClient();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("start", praram.getStartTime());
		data.put("end", praram.getEndTime());

		String query = FreeMarkerUtils.getDSL(data, praram.getDsl());

		Search search = (Search) new Search.Builder(query)
				// multiple index or types can be added.
				.addIndex(Arrays.asList(praram.getIndexes()))
				.addType(Arrays.asList(praram.getTypes())).build();

		JestResult result = client.execute(search);

		System.out.println(result.getJsonObject()
				.getAsJsonObject("aggregations").getAsJsonObject("result")
				.getAsJsonArray("buckets").toString());

		List<EsResultData> list = FastJsonUtils.json2list(
				result.getJsonObject().getAsJsonObject("aggregations")
						.getAsJsonObject("result").getAsJsonArray("buckets")
						.toString(), EsResultData.class);

		for (EsResultData esResultData : list) {
			IndicatorData indicatorData = new IndicatorData();

			indicatorData.setAvgPage(esResultData.getAvgPage());
			// indicatorData.setAvgTime(esResultData.getAvgTime().getValue());
			// indicatorData.setIp(esResultData.getIp().getAggs().getValue());
			indicatorData.setNuv(esResultData.getNuv());
			indicatorData.setNuvRate(esResultData.getNuvRate());
			indicatorData.setOutRate(esResultData.getOutRate());
			indicatorData.setPv(esResultData.getPv().getValue());
			indicatorData.setUv(esResultData.getUv().getValue());
			indicatorData.setVc(esResultData.getVc().getAggs().getValue());
			resultData.add(indicatorData);

		}

		return resultData;

	}

	public static void main(String[] args) throws Exception {

		EsQueryService e = new EsQueryService();

		RestPraram rp = new RestPraram();

		rp.setEndTime("1437321599999");
		rp.setStartTime("1437235200000");
		rp.setIndexes(new String[] { "access-2015-07-19" });
		rp.setTypes(new String[] { "1" });
		rp.setDsl("period_hour.ftl");

		e.queryDataindexTable(rp);

	}

}
