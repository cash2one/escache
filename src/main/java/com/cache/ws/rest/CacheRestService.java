package com.cache.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.cache.ws.es.service.EsQueryService;
import com.cache.ws.mongo.MongoDBOperate;
import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;


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
	public Response getMessage(@PathParam("param") String param) {

		
		RestPraram rp = FastJsonUtils.json2obj(param, RestPraram.class);
		
	
		//是否有缓存
		//operate.isMongoDataExist(collection, filters, type);
		
			//没有
		    //esService.queryDataindexTable();
		   // operate.insertMongoData();
		    
		
		//查询
		    //operate.query(collectionNames, filters, type);
		
		
		
		String result = "Restful example12321321 : " +rp;

		return Response.status(200).entity(result).build();

	}

}