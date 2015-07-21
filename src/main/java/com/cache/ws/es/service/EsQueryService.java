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
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;
import com.cache.ws.util.FreeMarkerUtils;

public class EsQueryService {

	@Inject
	private ESConfiguration esConfiguration;
	
	
	public List<IndicatorData> queryDataindexTable () throws Exception {
		

		
		JestClient client = ESConfiguration.getInstance().getClient();

		String query = "{"
				+ "        \"size\": 0, "
				+ "        \"aggs\": { "
			    + "        \"result\": { "
			    + "        \"date_histogram\": { "
			    + "        \"field\": \"utime\","
			    + "        \"interval\": \"1h\","
			    + "        \"format\": \"HH\","
			    + "        \"time_zone\": \"+08:00\","
			    + "        \"order\": {"
			    + "        \"_key\": \"asc\""
			    + "        },"
			    + "        \"min_doc_count\": 0,"
			    + "        \"extended_bounds\": {"
			    + "        \"min\": \"1437235200000\","
			    + "        \"max\": \"1437321599999\""
			    + " }"
			    + " },"
			    + "        \"aggs\": {"
			    + "        \"pv\": {"
			    + "        \"value_count\": {"
			    + "        \"field\": \"loc\" "
			    		  + "  }"
			    		  + " }"
			    		  + "}"
			    		  + "}"
			    		  + "}"
			    		  + "}";
		
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("name", "loc");
		
		query =  FreeMarkerUtils.getDSL(data,"test.ftl");
		
	
		
		

		Search search = (Search) new Search.Builder(query)
		// multiple index or types can be added.
				.addIndex("access-2015-07-19").addType("1").build();

		JestResult result = client.execute(search);

		
		

		System.out.println(result.getJsonObject().getAsJsonObject("aggregations").getAsJsonObject("result").getAsJsonArray("buckets").toString());
		
		
		List <EsResultData> list  = FastJsonUtils.json2list(result.getJsonObject().getAsJsonObject("aggregations").getAsJsonObject("result").getAsJsonArray("buckets").toString(), EsResultData.class);
		
	

	    for(EsResultData test : list) {
	    	System.out.println(test.getPv());
	    }
		
		
		
		
	
		
		
		
		return null;
		
		
	}
	
	public static void main(String[] args) throws Exception {
	
		
		EsQueryService e = new EsQueryService();
		
		e.queryDataindexTable();
//		
//		String s = "{\"value\":409}";
//
//		System.out.print(s.substring(s.indexOf("value")+7,s.lastIndexOf("}")));
//		
//		RestPraram rp = new RestPraram();
//		
//		rp.setEndTime(new Long(123213));
//		rp.setStartTime(new Long(456456));
//		rp.setIndics(new String[]{"pv","uv","ip"});
//		rp.setIndexes(new String[]{"index1","index2"});
//		rp.setTypes(new String[]{"1"});
//		rp.setFormartInterval(new Long(213));
//		
//		
//		
//		 String s =  FastJsonUtils.obj2json(rp);
//		   
//		   System.out.print(s);
		   
		    		
			    	
	
		
	}
	
}
