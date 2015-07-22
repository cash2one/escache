package com.cache.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.cache.ws.rest.dto.RestPraram;
import com.cache.ws.util.FastJsonUtils;


@Path("/loading")
public class CacheRestService {
	
	
	public CacheRestService() {}

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
		
		String result = "Restful example12321321 : " +rp;

		return Response.status(200).entity(result).build();

	}

}