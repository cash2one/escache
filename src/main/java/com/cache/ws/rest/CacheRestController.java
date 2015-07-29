package com.cache.ws.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cache.ws.es.dto.IndicatorData;
import com.cache.ws.es.service.EsQueryService;
import com.cache.ws.mongo.IndicatorDataToDBOject;
import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;
import com.mongodb.DBObject;

@Controller
@Path("/load")
public class CacheRestController {
	@Autowired
	private MongoDBOperate operate;

	@Autowired
	private EsQueryService esService;

	@GET
	@Path("/data/{param}")
	@Produces("application/json")
	public List<IndicatorData> loadCacheData(@PathParam("param") String redisKey) {
		List<IndicatorData> resultData = new ArrayList<IndicatorData>();
		try {
			RestPraram rp = FastJsonUtils.json2obj(getDsl(), RestPraram.class);
			// 根据redisKey获取dsl值

			String[] indexs = rp.getIndexes();

			for (String index : indexs) {
				// 是否有缓存
				if (!operate.isMongoDataExist(index, null, rp.getDsl())) {

					List<IndicatorData> data = esService.queryDataindexTable(
							rp.getStartTime(), rp.getEndTime(), rp.getDsl(),
							index, rp.getTypes());

					operate.insertMongoData(data, index, null, rp.getDsl());
				}
			}

			// 查询
			List<DBObject> temp = operate.query(rp.getIndexes(), null,
					rp.getDsl());

			for (DBObject dbObject : temp) {
				resultData.add(IndicatorDataToDBOject.parse(dbObject));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
	}

	private String getDsl() {
		return "{\"dsl\":\"period_hour.ftl\",\"endTime\":1437235200000,\"indexes\":[\"access-2015-07-19\"],\"startTime\":1437235200000,\"types\":[\"1\"]}";
	}
}
