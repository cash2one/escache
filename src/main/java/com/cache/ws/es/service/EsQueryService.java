package com.cache.ws.es.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Component;

import com.cache.ws.es.ESConfiguration;
import com.cache.ws.es.dto.EsResultData;
import com.cache.ws.es.dto.IndicatorData;
import com.cache.ws.redis.RedisDBOperate;
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.CacheUtils;
import com.cache.ws.util.FastJsonUtils;

@Component
public class EsQueryService {

	@Inject
	private ESConfiguration esConfiguration;

	public List<IndicatorData> queryDataindexTable(String redisKey,
			String index, String[] types) throws Exception {
		List<IndicatorData> resultData = new ArrayList<IndicatorData>();
		JestClient client = ESConfiguration.getInstance().getClient();

		// 根据KEY从Redis中获取查询语句
		String query = RedisDBOperate.loadDsl(redisKey);

		Search search = (Search) new Search.Builder(query)

		.addIndex(index).addType(Arrays.asList(types)).build();

		JestResult result = client.execute(search);

		List<EsResultData> esResultDatas = FastJsonUtils.json2list(
				result.getJsonObject().getAsJsonObject("aggregations")
						.getAsJsonObject("result").getAsJsonArray("buckets")
						.toString(), EsResultData.class);

		resultData = CacheUtils.convert(esResultDatas);

		return resultData;

	}

	public static void main(String[] args) throws Exception {

		EsQueryService e = new EsQueryService();

		RestPraram rp = new RestPraram();

		rp.setIndexes(new String[] { "access-2015-07-19" });
		rp.setTypes(new String[] { "1" });
		rp.setRedisKey("test_cache");

		e.queryDataindexTable(rp.getRedisKey(), "access-2015-07-19",
				rp.getTypes());

	}

}
