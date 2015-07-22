package com.cache.ws.es.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.inject.Inject;

import com.cache.ws.es.ESConfiguration;
import com.cache.ws.es.dto.EsResultData;
import com.cache.ws.es.dto.IndicatorData;
import com.cache.ws.util.FastJsonUtils;
import com.cache.ws.util.FreeMarkerUtils;

public class EsQueryService {

	@Inject
	private ESConfiguration esConfiguration;

	public List<IndicatorData> queryDataindexTable() throws Exception {

		JestClient client = ESConfiguration.getInstance().getClient();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("start", "1437235200000");
		data.put("end", "1437321599999");
		
		
		String query = FreeMarkerUtils.getDSL(data, "period_hour.ftl");

		Search search = (Search) new Search.Builder(query)
		// multiple index or types can be added.
				.addIndex("access-2015-07-19").addType("1").build();

		JestResult result = client.execute(search);

		System.out.println(result.getJsonObject()
				.getAsJsonObject("aggregations").getAsJsonObject("result")
				.getAsJsonArray("buckets").toString());

		List<EsResultData> list = FastJsonUtils.json2list(
				result.getJsonObject().getAsJsonObject("aggregations")
						.getAsJsonObject("result").getAsJsonArray("buckets")
						.toString(), EsResultData.class);

		for (EsResultData test : list) {

			System.out.println(test.getPv().getValue());
			System.out.println(test.getVc().getAggs().getValue());
			
			System.out.println(test.getOutRate());
			
		}

		return null;

	}

	public static void main(String[] args) throws Exception {

		EsQueryService e = new EsQueryService();

		e.queryDataindexTable();
		//
		// String s = "{\"value\":409}";
		//
		// System.out.print(s.substring(s.indexOf("value")+7,s.lastIndexOf("}")));
		//
		// RestPraram rp = new RestPraram();
		//
		// rp.setEndTime(new Long(123213));
		// rp.setStartTime(new Long(456456));
		// rp.setIndics(new String[]{"pv","uv","ip"});
		// rp.setIndexes(new String[]{"index1","index2"});
		// rp.setTypes(new String[]{"1"});
		// rp.setFormartInterval(new Long(213));
		//
		//
		//
		// String s = FastJsonUtils.obj2json(rp);
		//
		// System.out.print(s);

	}

}
