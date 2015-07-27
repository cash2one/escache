package com.cache.ws.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cache.ws.es.dto.IndicatorData;
import com.cache.ws.es.service.EsQueryService;
import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;
import com.mongodb.DBObject;

@Path("/loading")
public class CacheRestService {

	private MongoDBOperate operate = new MongoDBOperate();

	private EsQueryService esService = new EsQueryService();

	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {

		String result = "Restful example : " + msg;

		return Response.status(200).entity(result).build();

	}

	@GET
	@Path("/data/{param}")
	@Produces("application/json")
	public List<DBObject> getMessage(@PathParam("param") String param) {
		List<DBObject> resultData = new ArrayList<DBObject>();
		try {
			RestPraram rp = FastJsonUtils.json2obj(param, RestPraram.class);

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
			resultData = operate.query(rp.getIndexes(), null, rp.getDsl());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;

	}

}